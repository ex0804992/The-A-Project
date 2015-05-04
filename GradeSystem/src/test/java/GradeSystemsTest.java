import org.junit.*;
import org.omg.CORBA.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * This class includes all testCase in GradeSystems
 *
 * testGradeSystems() //建構子
 * testContainsID(ID)  //看aGradeSystem有否含此ID
 * testShowGrade(ID)
 * testShowRank(ID)
 * testUpdateWeights ()
 * testGetUserName(ID)
 *
 * **/
public class GradeSystemsTest {
    float[] defaultWeights = {0.1F, 0.1F, 0.1F, 0.3F, 0.4F};
    float[] inputNewWeights = {0.15F, 0.15F, 0.15F, 0.3F, 0.25F};
    String[] userData = {"962001044", "凌宗廷", "87", "86", "98", "88", "87"};
    GradeSystems gradeSystems;

    LinkedList<Grades> aList;

    public GradeSystemsTest(){
        try {
            //開檔 input file
            Scanner scanner = new Scanner(new FileInputStream("gradeInput.txt"));

            //用Java LinkedList建構an empty list of grades叫 aList
            aList = new LinkedList<Grades>();

            //while not endOfFile
            while (scanner.hasNextLine()) {
                //read line
                String[] inputStr = scanner.nextLine().split(" +");

                //call main.Grades() 建構aGrade
                //用 Java Scanner 來 scan line 把各欄位存入aGrade
                Grades agrades = new Grades(inputStr);
                //aGrade.calculateTotalGrade(weights) 回傳aTotalGrade把它存入aGrade
                agrades.calculateTotalGrade(defaultWeights);
                //把 aGrade 存入 aList
                aList.add(agrades);
            }
            //end while
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

    }

    @Before
    public void setUp(){

        gradeSystems = new GradeSystems();

    }

    @After
    public void tearDown(){

        gradeSystems = null;

    }

    /**
     * testGradeSystems()
     *
     * **/
    @Test
    public void testGradeSystems() throws Exception{
        Field privateGradeSystemsAlist = GradeSystems.class.getDeclaredField("aList");
        privateGradeSystemsAlist.setAccessible(true);
        LinkedList<Grades> actualAList = (LinkedList<Grades>) privateGradeSystemsAlist.get(gradeSystems);
        Grades expectGrades;
        Grades actualGrades;
        for(int i = 0 ; i < aList.size(); i++){
                expectGrades = aList.get(i);
                actualGrades = actualAList.get(i);
                assertEquals(expectGrades.getID(), actualGrades.getID());
                assertEquals(expectGrades.getName(), actualGrades.getName());
                assertEquals(expectGrades.getLab1(), actualGrades.getLab1());
                assertEquals(expectGrades.getLab2(), actualGrades.getLab2());
                assertEquals(expectGrades.getLab3(), actualGrades.getLab3());
                assertEquals(expectGrades.getMidTerm(), actualGrades.getMidTerm());
                assertEquals(expectGrades.getFinalExam(), actualGrades.getFinalExam());
                assertEquals(expectGrades.getTotalGrade(), actualGrades.getTotalGrade());
        }
    }

    /**
     * testContainsID(ID)
     *
     * case1: contain
     * case2: not contain
     * **/
    @Test
    public void testContainsID1(){
        assertTrue(gradeSystems.containsID(userData[0]));
    }

    @Test
    public void testContainsID2(){
        assertFalse(gradeSystems.containsID("123456789"));
    }

    /**
     * testShowGrade(ID)
     *
     * **/
    @Test
    public void testShowGrade(){
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        //Ready for assertEqual
        String expectOutput = "凌宗廷成績：\n"+
                "lab1：     87　\n" +
                "lab2：     86　\n" +
                "lab3：     98　\n" +
                "mid-term：     88　\n" +
                "final exam：     87　\n" +
                "total grade：     88　\n";

        gradeSystems.showGrade(userData[0]);

        assertEquals(expectOutput, outContent.toString());
    }

    /**
     * testShowRank(ID)
     *
     * **/
    @Test
    public void testShowRank(){

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String expectOutput =  "凌宗廷排名第38\n";
        gradeSystems.showRank(userData[0]);

        assertEquals(expectOutput, outContent.toString());
    }

    /**
     * testUpdateWeights ()
     *
     * case1:NewWeights = {0.15F, 0.15F, 0.15F, 0.3F, 0.25F} convert to percent(%)
     * case2:NewWeights = {0.15F, 0.15F, 0.15F, 0.3F, 0.25F} convert to percent(%), but user say No
     * case2:NewWeights = {0.15F, 0.15F, 0.15F, 0.3F, 0.4F} 配分錯誤: 總合不為100% 設為原配分
     * **/
    @Test
    public void testUpdateWeights1(){
        String insertNewWeights = "15 15 15 30 25 Y";
        final ByteArrayInputStream inContent = new ByteArrayInputStream(insertNewWeights.getBytes());
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        //ready for assertEqual
        String expectString = "舊配分:\n"
                + "lab1：     10%　\n"
                + "lab2：     10%　\n"
                + "lab3：     10%　\n"
                + "mid-term：     30%　\n"
                + "final exam：     40%　\n"
                +"請依序輸入新配分(%)\r\n"
                +"lab1 : \t"
                +"lab2 : \t"
                +"lab3 : \t"
                +"mid-term : \t"
                +"final exam : \t"
                +"請確認新配分:\n"
                + "lab1：     15%　\n"
                + "lab2：     15%　\n"
                + "lab3：     15%　\n"
                + "mid-term：     30%　\n"
                + "final exam：     25%　\n"
                + "以上正確嗎? Y (Yes) 或 N (No)\r\n";
        float[] expectNewWeights = {0.15F, 0.15F, 0.15F, 0.3F, 0.25F};

        //Set I/O
        System.setOut(new PrintStream(outContent));
        System.setIn(inContent);

        gradeSystems.updateWeights();

        //assertEqual
        assertEquals(expectString, outContent.toString());
        assertTrue(Arrays.equals(expectNewWeights, gradeSystems.getWeights()));
    }

    @Test
    public void testUpdateWeights2(){
        String insertNewWeights = "15 15 15 30 25 N";
        final ByteArrayInputStream inContent = new ByteArrayInputStream(insertNewWeights.getBytes());
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        //ready for assertEqual
        String expectString = "舊配分:\n"
                + "lab1：     10%　\n"
                + "lab2：     10%　\n"
                + "lab3：     10%　\n"
                + "mid-term：     30%　\n"
                + "final exam：     40%　\n"
                +"請依序輸入新配分(%)\r\n"
                +"lab1 : \t"
                +"lab2 : \t"
                +"lab3 : \t"
                +"mid-term : \t"
                +"final exam : \t"
                +"請確認新配分:\n"
                + "lab1：     15%　\n"
                + "lab2：     15%　\n"
                + "lab3：     15%　\n"
                + "mid-term：     30%　\n"
                + "final exam：     25%　\n"
                + "以上正確嗎? Y (Yes) 或 N (No)\r\n";
        float[] expectNewWeights = {0.1F, 0.1F, 0.1F, 0.3F, 0.4F};

        //Set I/O
        System.setOut(new PrintStream(outContent));
        System.setIn(inContent);

        gradeSystems.updateWeights();

        //assertEqual
        assertEquals(expectString, outContent.toString());
        assertTrue(Arrays.equals(expectNewWeights, gradeSystems.getWeights()));
    }

    @Test
    public void testUpdateWeights3(){
        String insertNewWeights = "15 15 15 30 40";
        final ByteArrayInputStream inContent = new ByteArrayInputStream(insertNewWeights.getBytes());
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        //ready for assertEqual
        String expected1 = "舊配分:\n"
                + "lab1：     10%　\n"
                + "lab2：     10%　\n"
                + "lab3：     10%　\n"
                + "mid-term：     30%　\n"
                + "final exam：     40%　\n"
                +"請依序輸入新配分(%)\r\n"
                +"lab1 : \t"
                +"lab2 : \t"
                +"lab3 : \t"
                +"mid-term : \t"
                +"final exam : \t"
                +"配分錯誤: 總合不為100%\r\n"
                + "重設為原配分:\n"
                + "lab1：     10%　\n"
                + "lab2：     10%　\n"
                + "lab3：     10%　\n"
                + "mid-term：     30%　\n"
                + "final exam：     40%　\n";
        float[] expected2 = {0.10F, 0.10F, 0.10F, 0.3F, 0.4F};

        //Set I/O
        System.setOut(new PrintStream(outContent));
        System.setIn(inContent);

        gradeSystems.updateWeights();

        //assertEqual
        assertEquals(expected1, outContent.toString());
        assertTrue(Arrays.equals(expected2, gradeSystems.getWeights()));
    }

    /**
     * testShowOldWeights()
     *
     * **/
    @Test
    public void testShowOldWeights(){
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        String expectShowString = "舊配分:\n"
                + "lab1：     10%　\n"
                + "lab2：     10%　\n"
                + "lab3：     10%　\n"
                + "mid-term：     30%　\n"
                + "final exam：     40%　\n";

        System.setOut(new PrintStream(outContent));

        gradeSystems.showOldWeights();

        assertEquals(expectShowString, outContent.toString());
    }

    /**
     * testCheckWeights()
     *
     * case1: 配分有效，使用者輸入 Y , check = true
     * case2: 配分有效，使用者輸入 N, check = false
     * case3: 配分無效，顯示錯誤訊息, check = false
     * **/
    @Test
    public void testCheckWeights1(){
        final ByteArrayInputStream inContent = new ByteArrayInputStream("Y".getBytes());
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        String expectShowString = "請確認新配分:\n"
                + "lab1：     15%　\n"
                + "lab2：     15%　\n"
                + "lab3：     15%　\n"
                + "mid-term：     30%　\n"
                + "final exam：     25%　\n"
                + "以上正確嗎? Y (Yes) 或 N (No)\r\n";
        System.setOut(new PrintStream(outContent));
        System.setIn(inContent);

        boolean check = gradeSystems.checkWeights(inputNewWeights);

        assertEquals(expectShowString, outContent.toString());
        assertTrue(check);
    }

    @Test
    public void testCheckWeights2(){
        final ByteArrayInputStream inContent = new ByteArrayInputStream("N".getBytes());
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        String expectShowString = "請確認新配分:\n"
                + "lab1：     15%　\n"
                + "lab2：     15%　\n"
                + "lab3：     15%　\n"
                + "mid-term：     30%　\n"
                + "final exam：     25%　\n"
                + "以上正確嗎? Y (Yes) 或 N (No)\r\n";
        System.setOut(new PrintStream(outContent));
        System.setIn(inContent);

        boolean check = gradeSystems.checkWeights(inputNewWeights);

        assertEquals(expectShowString, outContent.toString());
        assertFalse(check);
    }

    @Test
    public void testCheckWeights3(){
        float[] invalidNewWeight = {0.15F, 0.15F, 0.15F, 0.3F, 0.4F};
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        String expectShowString = "配分錯誤: 總合不為100%\r\n"
                + "重設為原配分:\n"
                + "lab1：     10%　\n"
                + "lab2：     10%　\n"
                + "lab3：     10%　\n"
                + "mid-term：     30%　\n"
                + "final exam：     40%　\n";
        System.setOut(new PrintStream(outContent));

        boolean check = gradeSystems.checkWeights(invalidNewWeight);

        assertEquals(expectShowString, outContent.toString());
        assertFalse(check);
    }

    /**
     * testGetNewWeights()
     *
     *  case1: get correct new weights
     *  case2: get wrong new weights
     * **/
    @Test
    public void testGetNewWeights(){
        String userInput = "15 15 15 30 25";
        final ByteArrayInputStream inContent = new ByteArrayInputStream(userInput.getBytes());
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        String expectShowString = "請依序輸入新配分(%)\r\n"
                +"lab1 : \t"
                +"lab2 : \t"
                +"lab3 : \t"
                +"mid-term : \t"
                +"final exam : \t";
        System.setOut(new PrintStream(outContent));
        System.setIn(inContent);

        gradeSystems.getNewWeights();

        assertEquals(expectShowString, outContent.toString());
    }

    /**
     * testSetWeights()
     *
     * **/
    @Test
    public void testSetWeights(){
        gradeSystems.setWeights(inputNewWeights);
        assertTrue(Arrays.equals(inputNewWeights, gradeSystems.getWeights()));
    }

    /**
     * testGetWeights()
     *
     * **/
    @Test
    public void testGetWeights(){
        assertTrue(Arrays.equals(defaultWeights, gradeSystems.getWeights()));
    }

    /**
     * testGetUserName(ID)
     *
     * **/
    @Test
    public void testGetUserName(){
        String expectName = "許文馨";
        String actualName = gradeSystems.getUserName("955002056");
        assertEquals(expectName, actualName);
    }

}



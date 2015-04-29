import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
* Created by Tzu-chao Wang on 2015/4/27.
*/
public class UITest extends TestCase {
    private UI ui;
    private String studentID;
    private String studentName;
    private ArrayList<Integer> studentGrades;
    private ArrayList<String> allStudentID;

    private PrintStream oldOutPut = System.out;
    private PrintStream oldErr = System.err;
    private InputStream oldInput = System.in;

    private ByteArrayOutputStream output = new ByteArrayOutputStream();
    private ByteArrayOutputStream errOutput = new ByteArrayOutputStream();
    private ByteArrayInputStream input = new ByteArrayInputStream("E".getBytes());

    public UITest(String name) {
        super(name);
        studentGrades = new ArrayList<Integer>();
        allStudentID = new ArrayList<String>();

        initStudentData();
        initAllStudentID();
        initSystemIO();
    }

    protected void setUp() {
        System.setOut(new PrintStream(output));
        System.setErr(new PrintStream(errOutput));
        System.setIn(input);
        ui = new UI();
    }

    protected void tearDown() {
        System.setOut(oldOutPut);
        System.setErr(oldErr);
        System.setIn(oldInput);
        ui = null;
    }

    @org.junit.Test
    public void testCheckIDWithExistID() {
        boolean result = ui.checkID(studentID);
        assertEquals("Test checkID returns true (with exist ID)", true, result);
    }

    @org.junit.Test
    public void testCheckIDWithNonExistID() {
        String nonExistStudentID;
        do {
            nonExistStudentID = generateRandomStudentID();
        }
        while (allStudentID.contains(nonExistStudentID));

        boolean result = ui.checkID(nonExistStudentID);
        assertEquals("Test checkID returns false (with non exist ID)", true, result);
    }

    @org.junit.Test
    public void testPromptCommand() {
        ui.promptCommand();
        // Read system output and assert.
    }

    @org.junit.Test
    public void testPromptID() {
        ui.promptID();
        // Read system output and assert.
    }

    @org.junit.Test
    public void testShowFinishMsg() {
        ui.showFinishMsg();

        assertEquals("Test showFinishMsg()", "輸入ID或 Q (結束使用)？", output.toString());
    }

    @org.junit.Test
    public void testShowWelcomeMsg() {
        ui.showWelcomeMsg(studentID);

        assertEquals("Test showWelcomeMsg()", "歡迎，" + studentID, output.toString());
    }

    @org.junit.Test
    public void testShowErrorMsg() {
        ui.showErrorMsg();

        assertEquals("Test showErrorMsg()", "ID錯了!", output.toString());
    }

    private void initStudentData() {
        Scanner s = null;
        Scanner lineScanner;

        try {
            s = new Scanner(new File("gradeInput.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        lineScanner = new Scanner(s.nextLine());
        studentID = lineScanner.next();
        studentName = lineScanner.next();

        while (lineScanner.hasNextInt()) {
            studentGrades.add(lineScanner.nextInt());
        }

        s.close();
    }

    private void initAllStudentID() {
        Scanner s = null;
        Scanner lineScanner;

        try {
            s = new Scanner(new File("gradeInput.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        lineScanner = new Scanner(s.nextLine());

        while (s.hasNextLine()) {
            lineScanner.close();
            lineScanner = new Scanner(s.nextLine());

            if (lineScanner.hasNext()) {
                allStudentID.add(lineScanner.next());
            }
        }
    }

    private void initSystemIO() {
        oldOutPut = System.out;
        oldErr = System.err;
        oldInput = System.in;

        output = new ByteArrayOutputStream();
        errOutput = new ByteArrayOutputStream();
        input = new ByteArrayInputStream("E".getBytes());
    }

    private String generateRandomStudentID() {
        String result = "";
        int randomInteger;

        while (result.length() <= 9) {
            randomInteger= (int)(Math.random() * 8 + 1);
            result += String.valueOf(randomInteger);
        }

        return result;
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(UITest.class);

        return suite;
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }
}

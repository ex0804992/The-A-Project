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

    public UITest(String name) {
        super(name);
        studentGrades = new ArrayList<Integer>();
        allStudentID = new ArrayList<String>();

        initStudentData();
        initAllStudentID();
    }

    protected void setUp() {
        ui = new UI();
    }

    protected void tearDown() {
        ui = null;
    }

    @org.junit.Test
    public void testIsLoginCommandValidWithQ() {
        boolean result = ui.isLoginCommandValid("Q");
        assertEquals("Test isLoginCommandValid() returns true (with command Q)", true, result);
    }

    @org.junit.Test
    public void testIsLoginCommandValidWithNonExistCommand() {
        boolean result = ui.isLoginCommandValid("X");
        assertEquals("Test isLoginCommandValid() returns false (with ono exist command)", false, result);
    }

    @org.junit.Test
    public void testIsLoginCommandValidWithWrongStudentID() {
        boolean result = ui.isLoginCommandValid("100000000");
        assertEquals("Test isLoginCommandValid() returns false (with wrong student ID)", false, result);
    }

    @org.junit.Test
    public void testIsLoginCommandValidWithWrongStudentIDFormat() {
        boolean result = ui.isLoginCommandValid("1005025");
        assertEquals("Test isLoginCommandValid() returns false (with wrong student ID format)", false, result);
    }

    @org.junit.Test
    public void testIsLoginCommandValidWithCorrectStudentID() {
        boolean result = ui.isLoginCommandValid(studentID);
        assertEquals("Test isLoginCommandValid() returns true (with correct student ID)", true, result);
    }

    @org.junit.Test
    public void testCheckIDWithExistID() {
        boolean result = ui.checkID(studentID);
        assertEquals("Test checkID() returns true (with exist ID)", true, result);
    }

    @org.junit.Test
    public void testCheckIDWithNonExistID() {
        String nonExistStudentID;
        do {
            nonExistStudentID = generateRandomStudentID();
        }
        while (allStudentID.contains(nonExistStudentID));

        boolean result = ui.checkID(nonExistStudentID);
        assertEquals("Test checkID() returns false (with non exist ID)", true, result);
    }

    @org.junit.Test
    public void testCheckIDWithWrongStudentIDFormat() {
        boolean result = ui.checkID("1005025");
        assertEquals("Test checkID() returns false (with wrong student ID format)", false, result);
    }

    @org.junit.Test
    public void testPromptCommand() {
        String actualText = ui.promptCommand();
        String expectedText = "輸入指令\n"+
                "1) G 顯示成績 (Grade)\n" +
                "2) R 顯示排名 (Rank)\n" +
                "3) W更新配分 (Weight)\n" +
                "4) E 離開選單 (Exit)\n";

        assertEquals("Test promptCommand()", expectedText, actualText);
    }

    @org.junit.Test
    public void testPromptID() {
        String result = ui.promptID();
        assertEquals("Test promptID()", "輸入ID或 Q (結束使用)？", result);
    }

    @org.junit.Test
    public void testShowFinishMsg() {
        String message = ui.showFinishMsg();

        assertEquals("Test showFinishMsg()", "結束了！", message);
    }

    @org.junit.Test
    public void testShowWelcomeMsg() {
        String message = ui.showWelcomeMsg(studentID);

        assertEquals("Test showWelcomeMsg()", "Welcome " + studentName, message);
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

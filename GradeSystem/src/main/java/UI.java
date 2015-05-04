import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by Tzu-chao Wang on 2015/4/29.
 */
public class UI {
    private GradeSystems gradeSystems;
    private String studentID;
    private  Scanner systemInput;

    /**
         * Class constructor.
         * Creates an UI instance.
         */
    public UI() {
        studentID = "";
        systemInput = new Scanner(System.in);

        try {
            gradeSystems = new GradeSystems();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
         * This method starts the Grade System, monitoring all user's input
         * and call the appropriate methods to handle it.
         */
    public void start() {
        String userInput = "";

        if (userLogin()) {
            while (!userInput.equals("E")) {
                promptCommand();
                userInput = systemInput.next();
                commandController(userInput);
            }
        }

        showFinishMsg();
    }

    /**
         * Attempts to login the user.
         * @return <code>true</code> if user logined successfully;
         *                 <code>false</code> if the user choose to quit this system.
         */
    public boolean userLogin() {
        promptID();
        String userInput = systemInput.next();

        while (!isLoginCommandValid(userInput)) {
            promptID();
            userInput = systemInput.next();
        }

        if (userInput.equals("Q")) {
            return false;
        }
        else {
            studentID = userInput;
            return true;
        }
    }

    /**
         * Checks if the command user inputs when logining in is correct or not.
         *
         * @return <code>true</code> if the command is valid;
         *                 <code>false</code> if the command is invalid.
         */
    public boolean isLoginCommandValid(String input) {
        if (!input.equals("Q") && !input.equals("q")) {
            if (!Pattern.matches("[1-9][0-9]{8}", input)) {
                System.out.println("輸入格式不正確！\n");
                return false;
            }

            if (!checkID(input)) {
                System.out.println("查無此學生 ID！\n");
                return false;
            }
        }

        return true;
    }

    /**
         * Checks if the student ID number exists in grade system or not.
         *
         * @param ID the student ID number to be checked.
         * @return <code>true</code> if the student ID number exists in
         *                 grade system;
         *                 <code>false</code> if the student ID number does not
         *                 exists in grade system.
         */
    public boolean checkID(String ID) {
        if (gradeSystems.containsID(ID)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
         * Calls the corresponding method of grade system according to the command.
         *
         * @param command the command inputted by user.
         */
    public void commandController(String command) {
        char c = command.charAt(0);
        switch (c) {
            case 'G':
                gradeSystems.showGrade(studentID);
                break;
            case 'R':
                gradeSystems.showRank(studentID);
                break;
            case 'W':
                gradeSystems.updateWeights();
                break;
            case 'E':
                System.out.println(studentID + " 已登出\n");
                break;
            default:
                System.out.println("查無此指令!\n");
                break;
        }
    }

    /**
         * Shows available command of this grade system and gets the user's input.
         *
         * @return returns the available commands.
         */
    public String promptCommand() {
        String helperText = "輸入指令\n"+
                "1) G 顯示成績 (Grade)\n" +
                "2) R 顯示排名 (Rank)\n" +
                "3) W更新配分 (Weight)\n" +
                "4) E 離開選單 (Exit)\n";

        System.out.println(helperText);

        return helperText;
    }

    /**
         * Tells user to input the student ID number or input Q to quit.
         *
         * @return returns the helper text.
         */
    public String promptID() {
        String text = "輸入ID或 Q (結束使用)？";

        System.out.println(text);

        return text;
    }

    /**
         * Display the finish message.
         *
         *  @return returns the finish message.
         */
    public String showFinishMsg() {
        String finishMessage = "結束了！";

        System.out.println(finishMessage);

        return finishMessage;
    }

    /**
         * Display the welcome message.
         *
         * @param ID the student ID number of current user.
         * @return returns the welcome message.
         */
    public String showWelcomeMsg(String ID) {
        String welcomeMessage = "Welcome " + gradeSystems.getUserName(ID);

        System.out.println(welcomeMessage);
        return welcomeMessage;
    }
}
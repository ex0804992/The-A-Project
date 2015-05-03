/**
 * Created by Tzu-chao Wang on 2015/4/29.
 */
public class UI {
    private GradeSystems gradeSystems;
    private String studentID;

    /**
         * Class constructor.
         * Creates an UI instance.
         */
    public UI() {
        studentID = "";

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
            userInput = promptCommand();

            while (userInput != "E") {
                commandController(userInput);
            }
        }
    }

    /**
         * Attempts to login the user.
         * @return <code>true</code> if user logined successfully;
         *                 <code>false</code> if the user choose to quit this system.
         */
    public boolean userLogin() {
        String userInput = promptID();

        while (!isLoginCommandValid(userInput)) {
            userInput = promptID();
        }

        if (userInput == "Q") {
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
    public boolean isLoginCommandValid(input) {
        if (input != "Q") {
            if (!Pattern.matches("[1-9][0-9]{8}", input)) {
                System.out.println("Invalid input!\n");
                return false;
            }

            if (!checkID(input)) {
                System.out.println("No such student ID!\n");
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
        switch (command) {
            case "G":
                gradeSystems.showGrade(studentID);
                break;
            case "R":
                gradeSystems.showRank(studentID);
                break;
            case "W":
                gradeSystems.updateWeights();
                break;
            case "E":
                showFinishMsg();
                break;
            default:
                System.out.println("No such command!\n");
                break;
        }
    }

    /**
         * Shows available command of this grade system and gets the user's input.
         *
         * @return returns the user's input.
         */
    public String promptCommand() {
        String helperText = "輸入指令\n" +
                "1) G 顯示成績 (Grade)\n" +
                "2) R 顯示排名 (Rank)\n" +
                "3) W更新配分 (Weight)\n" +
                "4) E 離開選單 (Exit)\n";
        String userInput = "";
        Scanner systemInput = new Scanner(System.in);

        System.out.println(helperText);
        userInput = systemInput.next();

        return userInput;
    }

    /**
         * Tells user to input the student ID number or input Q to quit, and
         * gets the user input then return it.
         *
         * @return returns the user's input.
         */
    public String promptID() {
        String text = "輸入ID或 Q (結束使用)？";
        String userInput = "";
        Scanner systemInput = new Scanner(System.in);

        System.out.println(text);
        userInput = systemInput.next();

        return userInput;
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

    public void showErrorMsg() {

    }
}
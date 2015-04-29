/**
 * Created by Tzu-chao Wang on 2015/4/29.
 */
public class UI {
    private GradeSystems gradeSystems;

    public UI() {
        try {
            gradeSystems = new GradeSystems();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public void start() {

    }

    public boolean checkID(String ID) {
        if (gradeSystems.containsID(ID)) {
            return true;
        }
        else {
            return false;
        }
    }

    public void promptCommand() {

    }

    public String promptID() {
        return "User Input.";
    }

    public void showFinishMsg() {

    }

    public void showWelcomeMsg(String ID) {
        String userName = gradeSystems.getUserName(ID);

        // Prompt "Welcome, userName".
    }

    public void showErrorMsg() {

    }
}
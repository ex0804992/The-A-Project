import java.awt.*;
import java.awt.geom.Arc2D;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class GradeSystems {

    private static final String[] categoryOfGrade = {"lab1","lab2","lab3","mid-term","final exam","total grade"};
    private float[] weights = {0.1F, 0.1F, 0.1F, 0.3F, 0.4F};
    private LinkedList<Grades> aList;

    /**
     * GradSystems()
     *
     * GradeSystems constructor
     * **/
    public GradeSystems(){

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
                agrades.calculateTotalGrade(this.getWeights());

                //把 aGrade 存入 aList
                aList.add(agrades);

            }
            //end while
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

    }

    /**
     *  containsID(String ID)
     *
     *  Check if ID is in alist of build.
     *  @param ID to check
     *
     *  return true if gradesystem contain this ID
     * **/
    public boolean containsID(String ID){

        if(getGrades(ID) != null){
            return true;
        }
        return false;
    }

    /**
     * showGrade(String ID)
     *
     * show corresponding grade of Grades
     * @param ID to show grade
     * **/
    public void showGrade(String ID){

        Grades grades = getGrades(ID);

        System.out.printf("%s成績：\n"
                        + categoryOfGrade[0] + "：     %d　\n"
                        + categoryOfGrade[1] + "：     %d　\n"
                        + categoryOfGrade[2] + "：     %d　\n"
                        + categoryOfGrade[3] + "：     %d　\n"
                        + categoryOfGrade[4] + "：     %d　\n"
                        + categoryOfGrade[5] + "：     %d　\n",
                grades.getName(), grades.getLab1(), grades.getLab2(), grades.getLab3()
                , grades.getMidTerm(), grades.getFinalExam(), grades.getTotalGrade());

    }

    /**
     * showRank(String ID)
     *
     * show corresponding rank of Grades
     * @param ID to show rank
     * **/
    public void showRank(String ID){
        int theTotalGrade = 0;
        int rank = 1;   //令rank 為 1

        //取得這 ID 的 theTotalGrade
        Grades grades = getGrades(ID);
        if(grades != null){
            theTotalGrade = grades.getTotalGrade();
        }
        //loop Grade in aList if aTotalGrade > theTotalGrade then rank加1(退1名) end loop
        for (Grades grade : aList) {
            if (grade.getTotalGrade() > theTotalGrade) {
                rank++;
            }
        }

        //show rank
        System.out.printf("%s排名第%d\n",grades.getName(), rank);

    }

    /**
     * getUserName(String ID)
     *
     * get corresponding username of ID
     * @param ID to find Grades and get the username
     *
     * return username
     * **/
    public String getUserName(String ID){

        Grades grades = getGrades(ID);
        if(grades != null){
            return grades.getName();
        }
        return null;
    }

    /**
     * updateWeights()
     *
     * Let user change the Weights
     * **/
    public void updateWeights(){
        //showOldWeights
        showOldWeights();

        //get new weights
        float[] newWeights = getNewWeights();

        //check if newWeights is valid
        boolean isNewWeightsValid = checkWeights(newWeights);

        //set newWeights
        if(isNewWeightsValid){
            setWeights(newWeights);
        }

    }

    /**
     * showOldWeights()
     *
     * show old weights in gradeSystem
     * **/
    public void showOldWeights(){
        showWeightsWithTitle("舊配分", weights);
    }

    /**
     * getNewWeights()
     *
     * **/
    public float[] getNewWeights(){

        System.out.println("請依序輸入新配分(%)");
        float[] newWeights = new float[5];

        for(int i = 0 ; i < 5 ; i++){
            System.out.printf("%s : \t", categoryOfGrade[i]);
            newWeights[i] = Float.valueOf(getUserInput());
            newWeights[i] = newWeights[i] / 100;
        }
        return newWeights;
    }

    /**
     * checkWeights()
     *
     * if newWeight is valid, then ask if user wants this newWeights
     *      user input Y, then return true.
     *      user input N, then return false.
     * if newWeights is invalid, then return false.
     *
     * return boolean
     * **/
    public boolean checkWeights(float[] newWeights){
        float sum = 0;
        for(float i : newWeights){
            if(i > 1 || i < 0){
                return false;
            }
            sum += i;
        }

        if(sum == 1){
            showWeightsWithTitle("請確認新配分", newWeights);
            System.out.println("以上正確嗎? Y (Yes) 或 N (No)");

            while(true){
                String input = getUserInput();
                if(input.equals("Y") || input.equals("Yes")){
                    return true;
                }else if(input.equals("N") || input.equals("No")){
                    return false;
                }else{
                    System.out.println("請輸入 Y (Yes) 或 N (No)");
                }
            }

        }else{
            System.out.println("配分錯誤: 總合不為100%");
            showWeightsWithTitle("重設為原配分", weights);
            return false;
        }
    }

    /**
     * setWeights()
     *
     * **/
    public void setWeights(float[] newWeights){
        weights = Arrays.copyOf(newWeights, 5);
    }

    /**
     * getWeights()
     *
     * **/
    public float[] getWeights(){
        return weights;
    }

    /**
     * find the corresponding Grades with the given id.
     *
     *  @param id student id
     * **/
    public Grades getGrades(String id) {

        for(Grades grades : aList){
            if( id.equals(grades.getID()) ){
                return grades;
            }
        }
        return null;
    }

    /**
     *  show weights with title to express what you want to display
     *
     *  @param title express what it display
     *  @param weights the weight to display
     * **/
    public void showWeightsWithTitle(String title, float[] weights){
        System.out.printf(title + ":\n"
                + categoryOfGrade[0] + "：     %d%%　\n"
                + categoryOfGrade[1] + "：     %d%%　\n"
                + categoryOfGrade[2] + "：     %d%%　\n"
                + categoryOfGrade[3] + "：     %d%%　\n"
                + categoryOfGrade[4] + "：     %d%%　\n"
                ,(int)(weights[0] * 100), (int)(weights[1] * 100), (int)(weights[2] * 100), (int)(weights[3] * 100), (int)(weights[4] * 100));
    }

    /**
     * getUserInput()
     *
     * return String
     * **/
    public String getUserInput(){
        String userInput = "";
        char ch;
        try {
            while( (ch = (char)System.in.read()) != ' ' && ch != '\uFFFF'){
                    userInput += ch;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return userInput;
    }

}
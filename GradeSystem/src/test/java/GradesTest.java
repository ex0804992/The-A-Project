import org.junit.*;
import static org.junit.Assert.*;

public class GradesTest {

    Grades grades;

    @Before
    public void setUp(){
        String[] data = {"962001044", "凌宗廷", "87", "86", "98", "88", "87"};
        grades = new Grades(data);

    }

    @After
    public void tearDown(){

        grades = null;

    }

    /**
     * testGrades()
     *
     * test the constructor
     * **/
    @Test
    public void testGrades(){

        assertEquals("962001044", grades.getID());
        assertEquals("凌宗廷", grades.getName());
        assertEquals(87, grades.getLab1());
        assertEquals(86, grades.getLab2());
        assertEquals(98, grades.getLab3());
        assertEquals(88, grades.getMidTerm());
        assertEquals(87, grades.getFinalExam());

    }

    /**
     * testCalculateTotalGrade()
     *
     * **/
     @Test
    public void testCalculateTotalGrade(){
        float[] weights = {0.1F, 0.1F, 0.1F, 0.3F, 0.4F};
        int expectTotalGrade = 88;
        grades.calculateTotalGrade(weights);
        assertEquals(expectTotalGrade, grades.getTotalGrade());
    }

}

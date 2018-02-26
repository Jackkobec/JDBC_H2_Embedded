import model.Prepod;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import persestince.ConnectionManager;
import persestince.PrepodDAO;

public class TestPrepodDAO {
    private PrepodDAO prepodDAO;

    @Before
    public void init(){
        prepodDAO = new PrepodDAO();
    }

    @After
    public void closeConnection() {
        ConnectionManager.getConnectionManager().closeConnection();
    }


    @Test
    public void selectPrepodWithMaxExperience() {

        Prepod prepod = new Prepod();

        prepod = prepodDAO.getPrepodsWithMaxExperience();
        String expected = "Prepod(id=4, name=Solomonovich, experience=5, lesson_id=4)";

        Assert.assertEquals(expected, prepod.toString());

    }

    @Test
    public void selectPrepodWithMinExperience() {

        Prepod prepod = new Prepod();

        prepod = prepodDAO.getPrepodsWithMinExperience();
        String expected = "Prepod(id=1, name=Petrovich, experience=2, lesson_id=1)";

        Assert.assertEquals(expected, prepod.toString());

    }

}

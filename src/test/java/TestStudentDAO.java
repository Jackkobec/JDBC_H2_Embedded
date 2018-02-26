import model.Student;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import persestince.ConnectionManager;
import persestince.StudentDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;


/**
 * Created by Jack on 18.11.2016.
 */
public class TestStudentDAO {

    private final static String PATH_TO_PROPERTIES = "src/main/resources/app.properties";
    private Connection connection;
    private Properties properties;
    private StudentDAO studentDAO;

    @Before
    public void init() throws IOException, SQLException {
        studentDAO = new StudentDAO();
    }

    @After
    public void closeConnection() {
        ConnectionManager.getConnectionManager().closeConnection();
    }

    @Test
    public void testGetAllStudents() {

        String expected = "[Student{id=1, name='Vasa', group_id=1}, " +
                "Student{id=2, name='Peta', group_id=1}, " +
                "Student{id=3, name='Vana', group_id=2}, " +
                "Student{id=4, name='Kesha', group_id=2}, " +
                "Student{id=5, name='Anna', group_id=2}, " +
                "Student{id=6, name='Kata', group_id=3}, " +
                "Student{id=7, name='Elena', group_id=3}, " +
                "Student{id=8, name='Kola', group_id=1}]";

        List<Student> students = studentDAO.getAll();
        System.out.println(students);
        //Assert.assertEquals(expected, students.toString());

    }

    @Test
    public void getStudentByID() {

        String expected = "Student(id=2, name=Peta, group_id=1)";
        Student student = studentDAO.getOneByID(2);

        Assert.assertEquals(expected, student.toString());

    }

//    @Test
//    public void getStudentByName() {
//
//        String expected = "Student{id=2, name='Peta', group_id=1}";
//        Student student = studentDAO.(2);
//
//        Assert.assertEquals(expected, student.toString());
//
//    }

}

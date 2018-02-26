package persestince;

import model.Group;
import model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO implements CommonDAO<Student, Integer> {
    private Connection connection;

    public StudentDAO() {
        this.connection = ConnectionManager.getConnectionManager().getConnection();
    }


    @Override
    public List<Student> getAll() {

        String sqlQuery = "SELECT * FROM students";

        return getListOfStudentsBySQLquery(sqlQuery);
    }

    @Override
    public Student getOneByID(Integer id) {

        String sqlQuery;

        if (null != id) {
            sqlQuery = "SELECT * FROM students WHERE students.id = " + id + ";";
        } else throw new NullPointerException("Передано значение null");

        try (ResultSet resultSet = connection.prepareStatement(sqlQuery).executeQuery(sqlQuery)) {

            Student student = new Student();
            while (resultSet.next()) {

                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
                student.setGroup_id(resultSet.getInt("group_id"));
            }

            return student;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addNewEntity(Student entity) {

        String sqlQuery;

        if (null != entity) {
            sqlQuery = "INSERT INTO students(name, group_id) VALUES (?, ?)";
        } else return false;

        return executeQueryInPreparedStatement(entity, sqlQuery);
    }

    @Override
    public boolean updateEntityInfo(Student entity) {


        String sqlQuery = "UPDATE students SET name = ?, group_id = ? WHERE id = " + entity.getId() + ";";

        if (getOneByID(entity.getId()) == null) {
            return false;
        }

        return executeQueryInPreparedStatement(entity, sqlQuery);
    }

    private boolean executeQueryInPreparedStatement(Student entity, String sqlQuery) {

        if (null == sqlQuery || entity == null) {
            throw new NullPointerException("Передан пустой sqlQuery / entity");
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getGroup_id());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Student> getStudentsByGroup(Group group) {

        String sqlQuery;


        if (null != group) {
            sqlQuery = "SELECT * FROM students WHERE students.group_id = " + group.getId() + ";";

        } else throw new NullPointerException("Передано значение null");

        return getListOfStudentsBySQLquery(sqlQuery);

    }

    private List<Student> getListOfStudentsBySQLquery(String sqlQuery) {

        if (null == sqlQuery) {
            throw new NullPointerException("Передан пустой sqlQuery");
        }

        try (ResultSet resultSet = connection.prepareStatement(sqlQuery).executeQuery(sqlQuery)) {

            List<Student> students = new ArrayList<>();

            while (resultSet.next()) {

                Student student = new Student();

                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
                student.setGroup_id(resultSet.getInt("group_id"));

                students.add(student);
            }

            return students;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}


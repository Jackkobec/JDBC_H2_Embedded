package persestince;

import model.Lesson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LessonDAO implements CommonDAO<Lesson, Integer> {
    private Connection connection;

    public LessonDAO() {
        this.connection = ConnectionManager.getConnectionManager().getConnection();
    }

    @Override
    public List<Lesson> getAll() {

        List<Lesson> lessons = new ArrayList<>();
        String sqlQuery = "SELECT * FROM lessons";

        try (ResultSet resultSet = connection.prepareStatement(sqlQuery).executeQuery()) {

            while (resultSet.next()) {

                Lesson lesson = new Lesson();

                lesson.setId(resultSet.getInt("id"));
                lesson.setName(resultSet.getString("name"));
                lesson.setDescription(resultSet.getString("description"));

                lessons.add(lesson);
            }

            return lessons;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Lesson getOneByID(Integer id) {

        String sqlQuery;

        if (null != id) {
            sqlQuery = "SELECT * FROM lessons WHERE lessons.id = " + id + ";";
        } else throw new NullPointerException("Передано значение null");


        try (ResultSet resultSet = connection.prepareStatement(sqlQuery).executeQuery()) {

            Lesson lesson = new Lesson();
            while (resultSet.next()) {

                lesson.setId(resultSet.getInt("id"));
                lesson.setName(resultSet.getString("name"));
                lesson.setDescription(resultSet.getString("description"));
            }

            return lesson;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addNewEntity(Lesson entity) {

        String sqlQuery;

        if (null != entity) {
            sqlQuery = "INSERT INTO lessons(name, description) VALUES (?, ?)";
        } else return false;

        return executeQueryInPreparedStatement(entity, sqlQuery);
    }

    @Override
    public boolean updateEntityInfo(Lesson entity) {

        String sqlQuery = "UPDATE lessons SET name = ?, description = ? WHERE id = " + entity.getId() + ";";

        if (getOneByID(entity.getId()) == null) {
            return false;
        }

        return executeQueryInPreparedStatement(entity, sqlQuery);
    }

    private boolean executeQueryInPreparedStatement(Lesson entity, String sqlQuery) {

        if (null == sqlQuery || entity == null) {
            throw new NullPointerException("Передан пустой sqlQuery / entity");
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}

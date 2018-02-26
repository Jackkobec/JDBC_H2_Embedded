package persestince;

import model.Group;
import model.Lesson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupDAO implements CommonDAO<Group, Integer> {
    private Connection connection;

    public GroupDAO() {
        connection = ConnectionManager.getConnectionManager().getConnection();
    }

    @Override
    public List<Group> getAll() {

        String sqlQuery = "SELECT * FROM GROUPS";

        return getListOfGroupsBySQLquery(sqlQuery);
    }

    @Override
    public Group getOneByID(Integer id) {

        String sqlQuery;

        if (null != id) {
            sqlQuery = "SELECT * FROM groups WHERE groups.id = " + id;
        } else throw new NullPointerException("Передано значение null");

        return getGroupBySQLquery(sqlQuery);
    }

    @Override
    public boolean addNewEntity(Group entity) {

        String sqlQuery;

        if (null != entity) {
            sqlQuery = "INSERT INTO groups(name) VALUES (?)";
        } else return false;

        return executeQueryInPreparedStatement(entity, sqlQuery);
    }

    @Override
    public boolean updateEntityInfo(Group entity) {


        String sqlQuery = "UPDATE groups SET name = ? WHERE id = " + entity.getId() + ";";

        if (getOneByID(entity.getId()) == null) {
            return false;
        }

        return executeQueryInPreparedStatement(entity, sqlQuery);
    }

    private boolean executeQueryInPreparedStatement(Group entity, String sqlQuery) {

        if (null == sqlQuery || entity == null) {
            throw new NullPointerException("Передан пустой sqlQuery / entity");
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, entity.getName());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Group getOneByName(String groupName) {

        String sqlQuery;

        if (null != groupName) {
            sqlQuery = "SELECT * FROM groups WHERE groups.name = " + "'" + groupName + "'";
        } else {
            throw new NullPointerException("groupName == null");
        }

        return getGroupBySQLquery(sqlQuery);
    }

    public List<Group> getGroupsByLesson(String lesson) {

        String sqlQuery;

        if (null != lesson) {
            sqlQuery = "SELECT groups.id, groups.name FROM learning LEFT JOIN lessons ON learning.lesson_id = lessons.id " +
                    "RIGHT JOIN groups ON learning.group_id = groups.id WHERE lessons.name = " + "'" + lesson + "'";
        } else {
            throw new NullPointerException("lesson == null");
        }

        return getListOfGroupsBySQLquery(sqlQuery);

    }

    public List<Group> getGroupsWhoLearnAllLessons() {

        List<Lesson> lessons = new LessonDAO().getAll();

        String sqlQuery = "select groups.id, groups.name, count(DISTINCT lessons.name) from learning " +
                "LEFT JOIN groups on groups.id = learning.group_id " +
                "LEFT JOIN lessons on learning.lesson_id = lessons.id " +
                "GROUP BY groups.name HAVING count(DISTINCT lessons.name) = " + lessons.size();

        return getListOfGroupsBySQLquery(sqlQuery);
    }

    public List<Group> getGroupsWithMoreThan3StudentsLearnLesson(String lesson) {

        String sqlQuery;

        if (null != lesson) {
            sqlQuery = "select * from learning " +
                    "LEFT JOIN  students on students.group_id = learning.group_id " +
                    "LEFT JOIN lessons on learning.lesson_id = lessons.id " +
                    "LEFT JOIN groups on learning.group_id = groups.id where lessons.name = "  + "'" + lesson + "'" +
                    "GROUP BY  groups.name HAVING count(students.id) >= 3;";
        } else {
            throw new NullPointerException("lesson == null");
        }

        return getListOfGroupsBySQLquery(sqlQuery);
    }

    private List<Group> getListOfGroupsBySQLquery(final String sqlQuery) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

//        try (Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(sqlQuery)) {

            List<Group> groups = new ArrayList<>();

            while (resultSet.next()) {
                Group group = new Group();
                group.setId(resultSet.getInt("id"));
                group.setName(resultSet.getString("name"));

                groups.add(group);
            }

            return groups;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    private Group getGroupBySQLquery(String sqlQuery) {

        if (null == sqlQuery) {
            throw new NullPointerException("Передан пустой sqlQuery");
        }

        try (ResultSet resultSet = connection.prepareStatement(sqlQuery).executeQuery(sqlQuery)) {
//             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//             ResultSet resultSet = preparedStatement.executeQuery(sqlQuery)) {

            Group group = new Group();
            while (resultSet.next()) {

                group.setId(resultSet.getInt("id"));
                group.setName(resultSet.getString("name"));

            }

            return group;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}

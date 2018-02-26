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

    private static final String LESSON_NULL = "lesson == null";
    private static final String RECEIVED_EMPTY_SQL_QUERY = "Передан пустой sqlQuery";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String RECEIVED_NULL_VALUE = "Передано значение null";

    private Connection connection;

    public GroupDAO() {
        connection = ConnectionManager.getConnectionManager().getConnection();
    }

    @Override
    public List<Group> getAll() {

        final String sqlQuery = "SELECT * FROM GROUPS";
        return getListOfGroupsBySQLquery(sqlQuery);
    }

    @Override
    public Group getOneByID(final Integer id) {

        final String sqlQuery;

        if (null != id) {
            sqlQuery = "SELECT * FROM groups WHERE groups.id = " + id;
        } else throw new NullPointerException(RECEIVED_NULL_VALUE);

        return getGroupBySQLquery(sqlQuery);
    }

    @Override
    public boolean addNewEntity(final Group entity) {

        final String sqlQuery;

        if (null != entity) {
            sqlQuery = "INSERT INTO groups(name) VALUES (?)";
        } else return false;

        return executeQueryInPreparedStatement(entity, sqlQuery);
    }

    @Override
    public boolean updateEntityInfo(final Group entity) {


        final String sqlQuery = "UPDATE groups SET name = ? WHERE id = " + entity.getId() + ";";

        if (getOneByID(entity.getId()) == null) {
            return false;
        }

        return executeQueryInPreparedStatement(entity, sqlQuery);
    }

    private boolean executeQueryInPreparedStatement(final Group entity, final String sqlQuery) {

        if (null == sqlQuery || entity == null) {
            throw new NullPointerException(RECEIVED_EMPTY_SQL_QUERY);
        }

        try (final PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, entity.getName());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Group getOneByName(final String groupName) {

        final String sqlQuery;

        if (null != groupName) {
            sqlQuery = "SELECT * FROM groups WHERE groups.name = " + "'" + groupName + "'";
        } else {
            throw new NullPointerException(LESSON_NULL);
        }

        return getGroupBySQLquery(sqlQuery);
    }

    public List<Group> getGroupsByLesson(final String lesson) {

        final String sqlQuery;

        if (null != lesson) {
            sqlQuery = "SELECT groups.id, groups.name FROM learning LEFT JOIN lessons ON learning.lesson_id = lessons.id " +
                    "RIGHT JOIN groups ON learning.group_id = groups.id WHERE lessons.name = " + "'" + lesson + "'";
        } else {
            throw new NullPointerException(LESSON_NULL);
        }

        return getListOfGroupsBySQLquery(sqlQuery);

    }

    public List<Group> getGroupsWhoLearnAllLessons() {

        final List<Lesson> lessons = new LessonDAO().getAll();
        final String sqlQuery = "select groups.id, groups.name, count(DISTINCT lessons.name) from learning " +
                "LEFT JOIN groups on groups.id = learning.group_id " +
                "LEFT JOIN lessons on learning.lesson_id = lessons.id " +
                "GROUP BY groups.name HAVING count(DISTINCT lessons.name) = " + lessons.size();

        return getListOfGroupsBySQLquery(sqlQuery);
    }

    public List<Group> getGroupsWithMoreThan3StudentsLearnLesson(final String lesson) {

        final String sqlQuery;

        if (null != lesson) {
            sqlQuery = "select * from learning " +
                    "LEFT JOIN  students on students.group_id = learning.group_id " +
                    "LEFT JOIN lessons on learning.lesson_id = lessons.id " +
                    "LEFT JOIN groups on learning.group_id = groups.id where lessons.name = " + "'" + lesson + "'" +
                    "GROUP BY  groups.name HAVING count(students.id) >= 3;";
        } else {
            throw new NullPointerException(LESSON_NULL);
        }

        return getListOfGroupsBySQLquery(sqlQuery);
    }

    private List<Group> getListOfGroupsBySQLquery(final String sqlQuery) {

        try (final PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
             final ResultSet resultSet = preparedStatement.executeQuery()) {

            final List<Group> groups = new ArrayList<>();

            while (resultSet.next()) {
                Group group = new Group();
                group.setId(resultSet.getInt(ID));
                group.setName(resultSet.getString(NAME));

                groups.add(group);
            }

            return groups;

        } catch (final SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    private Group getGroupBySQLquery(final String sqlQuery) {

        if (null == sqlQuery) {
            throw new NullPointerException(RECEIVED_EMPTY_SQL_QUERY);
        }

        try (final ResultSet resultSet = connection.prepareStatement(sqlQuery).executeQuery()) {

            final Group group = new Group();
            while (resultSet.next()) {

                group.setId(resultSet.getInt(ID));
                group.setName(resultSet.getString(NAME));

            }

            return group;

        } catch (final SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

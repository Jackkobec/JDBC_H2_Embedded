package persestince;

import model.Prepod;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrepodDAO implements CommonDAO<Prepod, Integer> {
    private Connection connection;

    public PrepodDAO() {
        this.connection = ConnectionManager.getConnectionManager().getConnection();
    }

    @Override
    public List<Prepod> getAll() {


        String sqlQuery = "SELECT * FROM prepods";

        return getListOfPrepodsBySQLquery(sqlQuery);
    }

    //java realisation
    public List<Prepod> getAllPrepodsWithExperienceMoreThen3Years() {

        List<Prepod> prepods = new ArrayList<>();

        for (Prepod prepod : getAll()) {

            if (prepod.getExperience() >= 3) {
                prepods.add(prepod);
            }
        }

        return prepods;
    }

    //SQL realisation
    public List<Prepod> getAllPrepodsByExperience(int experience) {

        String sqlQuery = "SELECT * FROM prepods WHERE prepods.experience >= " + experience + ";";

        return getListOfPrepodsBySQLquery(sqlQuery);
    }

    public Prepod getPrepodsWithMaxExperience() {

        String sqlQuery = "select * from prepods ORDER BY prepods.experience DESC LIMIT 1;";

        return getPrepodBySQLquery(sqlQuery);
    }

    public Prepod getPrepodsWithMinExperience() {

        String sqlQuery = "select * from prepods ORDER BY prepods.experience ASC LIMIT 1;";

        return getPrepodBySQLquery(sqlQuery);
    }


    private List<Prepod> getListOfPrepodsBySQLquery(String sqlQuery) {

        if (null == sqlQuery) {
            throw new NullPointerException("Передан пустой sqlQuery");
        }

        try (ResultSet resultSet = connection.prepareStatement(sqlQuery).executeQuery(sqlQuery)) {

            List<Prepod> prepods = new ArrayList<>();

            while (resultSet.next()) {

                Prepod prepod = new Prepod();

                prepod.setId(resultSet.getInt("id"));
                prepod.setName(resultSet.getString("name"));
                prepod.setExperience(resultSet.getInt("experience"));
                prepod.setLesson_id(resultSet.getInt("subject_id"));

                prepods.add(prepod);
            }

            return prepods;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public Prepod getOneByID(Integer id) {

        String sqlQuery;

        if (null != id) {
            sqlQuery = "SELECT * FROM prepods WHERE prepods.id = " + id + ";";
        } else throw new NullPointerException("Передано значение null");

        return getPrepodBySQLquery(sqlQuery);
    }

    private Prepod getPrepodBySQLquery(String sqlQuery) {

        if (null == sqlQuery) {
            throw new NullPointerException("Передан пустой sqlQuery");
        }

        try (ResultSet resultSet = connection.prepareStatement(sqlQuery).executeQuery(sqlQuery)) {

            Prepod prepod = new Prepod();
            while (resultSet.next()) {

                prepod.setId(resultSet.getInt("id"));
                prepod.setName(resultSet.getString("name"));
                prepod.setExperience(resultSet.getInt("experience"));
                prepod.setLesson_id(resultSet.getInt("lesson_id"));
            }

            return prepod;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addNewEntity(Prepod entity) {

        String sqlQuery;

        if (null != entity) {
            sqlQuery = "INSERT INTO prepods(name, experience, subject_id) VALUES (?, ?, ?)";
        } else return false;

        return (executeQueryInPreparedStatement(entity, sqlQuery)) ? true : false;
    }

    @Override
    public boolean updateEntityInfo(Prepod entity) {


        String sqlQuery = "UPDATE groups SET name = ?. experience = ?, subject_id = ? " +
                "WHERE id = " + entity.getId() + ";";

        if (getOneByID(entity.getId()) == null) {
            return false;
        }

        return executeQueryInPreparedStatement(entity, sqlQuery);
    }

    private boolean executeQueryInPreparedStatement(Prepod entity, String sqlQuery) {

        if (null == sqlQuery || entity == null) {
            throw new NullPointerException("Передан пустой sqlQuery / entity");
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getExperience());
            preparedStatement.setInt(3, entity.getLesson_id());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}

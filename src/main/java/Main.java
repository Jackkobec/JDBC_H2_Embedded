import org.h2.tools.DeleteDbFiles;
import persestince.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        try {
            // delete the H2 database named 'test' in the user home directory
            DeleteDbFiles.execute("~", "test", true);

            // Init DB only for groups table
            initGroupsTable();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initGroupsTable() throws SQLException {
         final Connection connection = ConnectionManager.getConnectionManager().getConnection();
         PreparedStatement createPreparedStatement;
         PreparedStatement insertPreparedStatement;

        final String groupsTableInitQuery = "CREATE TABLE groups ( " +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR (20) " +
                ");";
        final String insertDataIntoGroupsTable =
                        "INSERT INTO groups(name) VAlUES ('group1'); " +
                        "INSERT INTO groups(name) VAlUES ('group2'); " +
                        "INSERT INTO groups(name) VAlUES ('group3');";
        try {
            connection.setAutoCommit(false);

            createPreparedStatement = connection.prepareStatement(groupsTableInitQuery);
            createPreparedStatement.executeUpdate();
            createPreparedStatement.close();

            insertPreparedStatement = connection.prepareStatement(insertDataIntoGroupsTable);
            insertPreparedStatement.executeUpdate();
            insertPreparedStatement.close();

            connection.commit();
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
}

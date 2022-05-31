package sample;

import java.sql.*;

public class DBHandler {
    Connection dbConnection;
    Statement statement;
    ResultSet resultSet;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + "localHost" + ":" + "3306" + "/" + "practice";
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, "root", "1234");
        return dbConnection;
    }

    public ResultSet querry(String querry) {
        try{
            Statement statement = getDbConnection().createStatement();
            resultSet = statement.executeQuery(querry);
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return resultSet;
    }
}

package main.db;

import main.Main;

import java.sql.*;

public class SQLDatabaseConnection {
    private int role;
    public Connection connect(String user, String password, Main app) throws SQLException {
        String connectionUrl = "jdbc:sqlserver://localhost\\sqlexpress:49682;database=uni_department;";
        Connection connection = DriverManager.getConnection(connectionUrl, "student", "student");
        app.setConnection(connection);
        Statement statement = connection.createStatement();
        String selectSql = "SELECT user_role FROM logins WHERE logins.user_login = '" + user + "' AND user_password = '" + password + "'";
        ResultSet rs = statement.executeQuery(selectSql);
        ResultSet res;
        String sql;
        if (rs.next()) {
            if (rs.getString(1).equals("teacher")){
                System.out.println("1");
                sql = "SELECT Fio FROM teacher JOIN logins ON teacher.user_login = logins.id WHERE logins.user_login = '" + user + "'";
                res = statement.executeQuery(sql);
                if (res.next()) {
                    app.setFio(res.getString(1));
                    role = 2;
                    return connection;
                }

            }
            if (rs.getString(1).equals("student")){
                sql = "SELECT Fio FROM students JOIN logins ON students.user_login = logins.id WHERE logins.user_login = '" + user + "'";
                 res = statement.executeQuery(sql);
                if (res.next()) {
                    app.setFio(res.getString(1));
                    role = 3;
                    return connection;
                }
            }
            if (rs.getString(1).equals("admin")){
                role = 1;
                return connection;
            }
            app.openErrorWindow("Неправильный логин или пароль", 4);
        } else {
            app.openErrorWindow("Неправильный логин или пароль", 4);
        }
        return null;
    }

    public int getRole() {
        return role;
    }
}
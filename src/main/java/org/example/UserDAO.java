package org.example;
import java.sql.*;

public class UserDAO {
    private Connection connection;

    public UserDAO()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //String url = "jdbc:mysql://localhost:3306/users";
            String url = "jdbc:mysql://localhost/e6testapp";
            String username = "root";
            String password = "";
            connection = DriverManager.getConnection(url, username, password);
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
    }

    public ResultSet getAllUsers() throws SQLException
    {
        Statement statement = connection.createStatement();
        ResultSet res = statement.executeQuery("SELECT * FROM `users`");
        return res;
    }

    public void createUser(User user) throws SQLException
    {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (name, lastname, email, age, job) VALUES (?, ?, ?, ?, ?)");
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getLastname());
        preparedStatement.setString(3, user.getEmail());
        preparedStatement.setInt(4, user.getAge());
        preparedStatement.setString(5, user.getJob());
        preparedStatement.execute();
    }

    public void deleteUser(int id) throws SQLException
    {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?");
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
    }
}

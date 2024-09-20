package DBOperations;

import java.sql.*;

public class AdminDB
{
    private Connection connection;


    public AdminDB() throws SQLException
    {
        String url = "jdbc:mysql://localhost:3308/hospital";
        String username = "root";
        String password = "2003";
        connection = DriverManager.getConnection(url, username, password);
    }

    public ResultSet viewAdmins() throws SQLException
    {
        String query = "SELECT * FROM adminlogin WHERE Aid!='A001'";
        PreparedStatement stmt = connection.prepareStatement(query);
        return stmt.executeQuery(); // Return the ResultSet from the query
    }

    public boolean validateLogin(String username, String password) throws SQLException
    {
        String query = "SELECT * FROM adminlogin WHERE uname=? AND pass=?";
        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet resultSet = stmt.executeQuery())
            {
                return resultSet.next();
            }
        }
    }

    public String generateNextID() throws SQLException
    {
        String query = "SELECT MAX(Aid) FROM adminlogin";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query))
        {
            if (rs.next())
            {
                String maxID = rs.getString(1);
                if (maxID != null)
                {
                    int idNumber = Integer.parseInt(maxID.substring(1)) + 1;
                    return String.format("A%03d", idNumber); //begin like A and length is 3
                }
            }
            return "A001";
        }
    }

    public void addAdmin(String username, String password) throws SQLException
    {
        String adminID = generateNextID();
        String query = "INSERT INTO adminlogin (Aid,uname,pass) VALUES (?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, adminID);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
        }
    }

    public void updateAdmin(String username, String password,String id) throws SQLException
    {
        String query = "UPDATE adminlogin SET uname=?,pass=? WHERE Aid=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, id);
            pstmt.executeUpdate();
        }
    }

    public void deleteAdmin(String id) throws SQLException
    {
        String query = "DELETE FROM adminlogin WHERE Aid=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query))
        {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        }
    }
}

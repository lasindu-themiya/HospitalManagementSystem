package DBOperations;

import java.sql.*;

public class ReceptionistDB
{
    private Connection connection;



    public ReceptionistDB() throws SQLException
    {
        String url = "jdbc:mysql://localhost:3308/hospital";
        String username = "root";
        String password = "2003";
        connection = DriverManager.getConnection(url, username, password);
    }

    public ResultSet viewReceptionists() throws SQLException
    {
        String query = "SELECT * FROM receptionist";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }

    public boolean validateLogin(String username, String password) throws SQLException
    {
        String query = "SELECT * FROM receptionist WHERE uname=? AND pass=?";
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
        String query = "SELECT MAX(Rid) FROM receptionist";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query))
        {
            if (rs.next())
            {
                String maxID = rs.getString(1);
                if (maxID != null)
                {
                    int idNumber = Integer.parseInt(maxID.substring(1)) + 1;
                    return String.format("R%03d", idNumber);
                }
            }
            return "R001";
        }
    }

    public void deleteReceptionists(String id) throws SQLException
    {
        String query = "DELETE FROM receptionist WHERE Rid=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query))
        {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        }
    }

    public void addReceptionists(String uname, String pass, String name, int age, int telephone, String address, String nic) throws SQLException
    {
        String recid = generateNextID();
        String query = "INSERT INTO receptionist (Rid,uname,pass,name,age,telephone,address,nic) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query))
        {
            pstmt.setString(1, recid);
            pstmt.setString(2, uname);
            pstmt.setString(3, pass);
            pstmt.setString(4, name);
            pstmt.setInt(5, age);
            pstmt.setInt(6, telephone);
            pstmt.setString(7, address);
            pstmt.setString(8, nic);
            pstmt.executeUpdate();
        }
    }

    public void updateReceptionists(String recID, String uname, String pass, String name, int age, int telephone, String address, String nic) throws SQLException
    {
        String query = "UPDATE receptionist SET uname=?,pass=?,name=?,age=?,telephone=?,address=?,nic=? WHERE Rid=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query))
        {
            pstmt.setString(1, uname);
            pstmt.setString(2, pass);
            pstmt.setString(3, name);
            pstmt.setInt(4, age);
            pstmt.setInt(5, telephone);
            pstmt.setString(6, address);
            pstmt.setString(7, nic);
            pstmt.setString(8, recID);
            pstmt.executeUpdate();
        }
    }
    public String getReceptionistId(String username) throws SQLException
    {
        String query = "SELECT Rid FROM receptionist WHERE uname=?";
        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setString(1, username);
            try (ResultSet resultSet = stmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getString("Rid");
                }
                return null;
            }
        }
    }

    public ResultSet loadReceptionistProfile(String receptionistId) throws SQLException
    {
        String query = "SELECT * FROM receptionist WHERE Rid=?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, receptionistId);
        return stmt.executeQuery();
    }
}

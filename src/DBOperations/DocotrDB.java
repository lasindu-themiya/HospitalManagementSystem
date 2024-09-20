package DBOperations;

import java.sql.*;

public class DocotrDB
{

    private Connection connection;

    public DocotrDB() throws SQLException
    {
        String url = "jdbc:mysql://localhost:3308/hospital";
        String username = "root";
        String password = "2003";
        connection = DriverManager.getConnection(url, username, password);
    }

    public ResultSet viewDoctors() throws SQLException
    {
        String query = "SELECT * FROM doctors";
        PreparedStatement pstmt = connection.prepareStatement(query);
        return pstmt.executeQuery();
    }

    public ResultSet getAllDoctorIds() throws SQLException
    {
        String query = "SELECT Did FROM doctors";
        PreparedStatement pstmt = connection.prepareStatement(query);
        return pstmt.executeQuery();
    }

    public boolean validateLogin(String username, String password) throws SQLException
    {
        String query = "SELECT * FROM doctors WHERE fname=? AND pass=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query))
        {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                return resultSet.next();
            }
        }
    }

    public String generateNextID() throws SQLException
    {
        String query = "SELECT MAX(Did) FROM doctors";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query))
        {
            if (rs.next())
            {
                String maxID = rs.getString(1);
                if (maxID != null)
                {
                    int idNumber = Integer.parseInt(maxID.substring(1)) + 1;
                    return String.format("D%03d", idNumber);
                }
            }
            return "D001";
        }
    }

    public void addDoctor(String fname, String lname, String pass, Integer cnt, String specialization, String email, double rate) throws SQLException
    {
        String doctorID = generateNextID();
        String query = "INSERT INTO doctors (Did,fname,lname,pass,contact,specialization,email,rate) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query))
        {
            pstmt.setString(1, doctorID);
            pstmt.setString(2, fname);
            pstmt.setString(3, lname);
            pstmt.setString(4, pass);
            pstmt.setInt(5, cnt);
            pstmt.setString(6, specialization);
            pstmt.setString(7, email);
            pstmt.setDouble(8, rate);
            pstmt.executeUpdate();
        }
    }

    public void updateDoctor(String fname, String lname, String pass, Integer cnt, String specialization, String email, double rate, String id) throws SQLException
    {
        String query = "UPDATE doctors SET fname=?,lname=?,pass=?,contact=?,specialization=?,email=?,rate=? WHERE Did=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query))
        {
            pstmt.setString(1, fname);
            pstmt.setString(2, lname);
            pstmt.setString(3, pass);
            pstmt.setInt(4, cnt);
            pstmt.setString(5, specialization);
            pstmt.setString(6, email);
            pstmt.setDouble(7, rate);
            pstmt.setString(8, id);
            pstmt.executeUpdate();
        }
    }

    public void deleteDoctor(String id) throws SQLException
    {
        String query = "DELETE FROM doctors WHERE Did=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query))
        {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        }
    }

    public String getDoctorIdByClinic(String clinicId) throws SQLException
    {

        String query = "SELECT Did FROM clinic WHERE Cid=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query))
        {
            pstmt.setString(1, clinicId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
            {
                return rs.getString("Did");
            }
            return null;
        }
    }

    public double getDoctorRate(String doctorId) throws SQLException
    {
        String query = "SELECT rate FROM doctors WHERE Did=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query))
        {
            pstmt.setString(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
            {
                return rs.getDouble("rate");
            }
            return 0.0;
        }
    }

    public String getDoctorId(String username) throws SQLException
    {
        String query = "SELECT Did FROM doctors WHERE fname=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query))
        {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getString("Did");
                }
            }
        }
        return null;
    }

    public ResultSet loadDoctorProfile(String doctorId) throws SQLException
    {
        String query = "SELECT fname,lname,pass,contact,specialization,email,rate FROM doctors WHERE Did=?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, doctorId);
        return pstmt.executeQuery();
    }
}

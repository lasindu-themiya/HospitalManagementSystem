package DBOperations;

import java.sql.*;

public class PatientDB
{
    private Connection connection;

    public PatientDB() throws SQLException
    {
        String url = "jdbc:mysql://localhost:3308/hospital";
        String username = "root";
        String password = "2003";
        connection = DriverManager.getConnection(url, username, password);
    }

    public void addPatient(String patientId, String firstName, String lastName, String city, String nic, String email, int age, String contact) throws SQLException
    {
        String query = "INSERT INTO patient (PiD,First_Name,Last_Name,City,NIC,Email,Age,Contact_NO) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query))
        {
            pstmt.setString(1, patientId);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, city);
            pstmt.setString(5, nic);
            pstmt.setString(6, email);
            pstmt.setInt(7, age);
            pstmt.setString(8, contact);
            pstmt.executeUpdate();
        }
    }

    public String generateNextID() throws SQLException
    {
        String query = "SELECT MAX(PiD) FROM patient";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query))
        {
            if (rs.next())
            {
                String maxID = rs.getString(1);
                if (maxID != null)
                {
                    int idNumber = Integer.parseInt(maxID.substring(1)) + 1;
                    return String.format("P%03d", idNumber);
                }
            }
            return "P001";
        }
    }
}

package DBOperations;

import java.sql.*;

public class ClinicDB
{
    private Connection connection;


    public ClinicDB() throws SQLException
    {
        String url = "jdbc:mysql://localhost:3308/hospital";
        String username = "root";
        String password = "2003";
        connection = DriverManager.getConnection(url, username, password);
    }

    public ResultSet viewClinics() throws SQLException
    {
        String query = "SELECT * FROM clinic";
        PreparedStatement pstmt = connection.prepareStatement(query);
        return pstmt.executeQuery();
    }


    public String generateNextID() throws SQLException
    {
        String query = "SELECT MAX(Cid) FROM clinic";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query))
        {
            if (rs.next())
            {
                String maxID = rs.getString(1);
                if (maxID != null)
                {
                    int idNumber = Integer.parseInt(maxID.substring(1)) + 1;
                    return String.format("C%03d", idNumber);
                }
            }
            return "C001";
        }
    }




    public void addClinic( String did, Time time, Date date, int plimit, int roomnum) throws SQLException
    {
        String cid = generateNextID();
        String query = "INSERT INTO clinic (Cid,Did,startTime,date,plimit,roomnum) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query))
        {
            pstmt.setString(1, cid);
            pstmt.setString(2, did);
            pstmt.setTime(3, time);
            pstmt.setDate(4, new java.sql.Date(date.getTime()));
            pstmt.setInt(5, plimit);
            pstmt.setInt(6, roomnum);
            pstmt.executeUpdate();
        }
    }


    public void updateClinic(String cid, String did, Time time, Date date, int plimit, int roomnum) throws SQLException
    {
        String query = "UPDATE clinic SET Did=?,startTime=?,date=?,plimit=?,roomnum=? WHERE Cid=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query))
        {
            pstmt.setString(1, did);
            pstmt.setTime(2, time);
            pstmt.setDate(3, new java.sql.Date(date.getTime()));
            pstmt.setInt(4, plimit);
            pstmt.setInt(5, roomnum);
            pstmt.setString(6, cid);
            pstmt.executeUpdate();
        }
    }


    public void deleteClinic(String cid) throws SQLException
    {
        String query = "DELETE FROM clinic WHERE Cid=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, cid);
            pstmt.executeUpdate();
        }
    }

    public ResultSet viewUpcomingClinics() throws SQLException
    {
        String query = "SELECT c.Cid, c.Did, CONCAT(d.fname,' ', d.lname) AS DoctorName, c.startTime, c.date, c.plimit, c.roomnum " +
                "FROM clinic c " +
                "INNER JOIN doctors d ON c.Did = d.Did " +
                "WHERE c.date >= CURDATE()";
        PreparedStatement pstmt = connection.prepareStatement(query);
        return pstmt.executeQuery();
    }

    public void decreasePatientLimit(String clinicId) throws SQLException
    {
        String query = "UPDATE clinic SET plimit=plimit-1 WHERE Cid=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query))
        {
            pstmt.setString(1, clinicId);
            pstmt.executeUpdate();
        }
    }

}

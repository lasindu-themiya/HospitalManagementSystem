package DBOperations;

import java.sql.*;

public class AppointmentDB
{
    private Connection connection;

    public AppointmentDB() throws SQLException
    {
        String url = "jdbc:mysql://localhost:3308/hospital";
        String username = "root";
        String password = "2003";
        connection = DriverManager.getConnection(url, username, password);
    }

    public String generateNextID() throws SQLException
    {
        String query = "SELECT MAX(AiD) FROM appointment";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query))
        {
            if (rs.next())
            {
                String maxID = rs.getString(1);
                if (maxID != null)
                {
                    int idNumber = Integer.parseInt(maxID.substring(2)) + 1;
                    return String.format("AP%03d", idNumber);
                }
            }
            return "AP001";
        }
    }

    public void addAppointment(String aid, String pid, String cid, Date dateAndTime) throws SQLException
    {
        String query = "INSERT INTO appointment (AiD,PiD,CiD,Date_and_Time) VALUES (?,?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query))
        {
            pstmt.setString(1, aid);
            pstmt.setString(2, pid);
            pstmt.setString(3, cid);
            pstmt.setDate(4, dateAndTime);
            pstmt.executeUpdate();
        }
    }

    public ResultSet getAllAppointments() throws SQLException
    {
        String query = "SELECT a.AiD, p.First_Name, p.Last_Name, c.Date, d.fname AS DoctorFirstName, d.lname AS DoctorLastName " +
                "FROM appointment a " +
                "JOIN patient p ON a.PiD = p.PiD " +
                "JOIN clinic c ON a.CiD = c.Cid " +
                "JOIN doctors d ON c.Did = d.Did";

        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return stmt.executeQuery(query);
    }

    public ResultSet searchAppointments(String doctorName, String clinicDate) throws SQLException
    {
        StringBuilder query = new StringBuilder("SELECT a.AiD, p.First_Name, p.Last_Name, c.Date, d.fname AS DoctorFirstName, d.lname AS DoctorLastName " +
                "FROM appointment a " +
                "JOIN patient p ON a.PiD = p.PiD " +
                "JOIN clinic c ON a.CiD = c.Cid " +
                "JOIN doctors d ON c.Did = d.Did WHERE 1=1");

        if (!doctorName.isEmpty())
        {
            query.append(" AND (d.fname LIKE '%").append(doctorName).append("%' OR d.lname LIKE '%").append(doctorName).append("%')");
        }
        if (!clinicDate.isEmpty())
        {
            query.append(" AND c.date = '").append(clinicDate).append("'");
        }

        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return stmt.executeQuery(query.toString());
    }
}
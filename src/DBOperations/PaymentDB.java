package DBOperations;

import java.sql.*;

public class PaymentDB
{
    private Connection connection;

    public PaymentDB() throws SQLException
    {
        String url = "jdbc:mysql://localhost:3308/hospital";
        String username = "root";
        String password = "2003";
        connection = DriverManager.getConnection(url, username, password);
    }

    public void addPayment(String appointmentId, double amount, java.sql.Date paymentDate) throws SQLException
    {
        String query = "INSERT INTO payments (Paid,AiD,Amount,PaymentDate) VALUES (?,?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query))
        {
            pstmt.setString(1, generateNextID());
            pstmt.setString(2, appointmentId);
            pstmt.setDouble(3, amount);
            pstmt.setDate(4, paymentDate);
            pstmt.executeUpdate();
        }
    }

    private String generateNextID() throws SQLException
    {
        String query = "SELECT MAX(PaiD) FROM payments";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query))
        {
            if (rs.next())
            {
                String maxID = rs.getString(1);
                if (maxID != null && maxID.startsWith("PA"))
                {
                    try
                    {
                        int idNumber = Integer.parseInt(maxID.substring(2)) + 1;
                        return String.format("PA%03d", idNumber);
                    }
                    catch (NumberFormatException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            return "PA001";
        }
    }


    public ResultSet getDoctorPaymentsForToday() throws SQLException
    {
        String query = "SELECT d.Did, d.fname, d.lname, COUNT(a.AiD) AS appointment_count, " +
                "d.rate * COUNT(a.AiD) AS doctor_earnings, " +
                "COUNT(a.AiD) * 1000 AS hospital_earnings_per_doctor " +
                "FROM doctors d " +
                "JOIN clinic c ON d.Did = c.Did " +
                "JOIN appointment a ON c.Cid = a.CiD " +
                "WHERE a.Date_and_Time = CURDATE() " +
                "GROUP BY d.Did, d.fname, d.lname, d.rate";

        PreparedStatement pstmt = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return pstmt.executeQuery();
    }
}

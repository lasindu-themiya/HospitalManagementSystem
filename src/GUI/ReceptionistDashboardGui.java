package GUI;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ReceptionistDashboardGui extends JFrame
{

    private ReceptionistPanelGui navPanel;
    private JLabel lblReceptionistPanel;
    private JPanel chartsPanel;

    private String receptionId;

    public ReceptionistDashboardGui(LoginGui loginFrame, String receptionId) throws SQLException
    {
        this.receptionId = receptionId;
        InitializeUI(loginFrame);
        loadChartData();
    }

    private void InitializeUI(LoginGui loginFrame) throws SQLException
    {
        setTitle("Receptionist Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1000,700);
        setLocationRelativeTo(null);

        // Create the navigation panel and add it to the west side
        navPanel = new ReceptionistPanelGui(this, loginFrame, receptionId);
        getContentPane().add(navPanel, BorderLayout.WEST);

        // Create a label for the top of the window
        lblReceptionistPanel = new JLabel("Receptionist Panel", JLabel.CENTER);
        lblReceptionistPanel.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lblReceptionistPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        getContentPane().add(lblReceptionistPanel, BorderLayout.NORTH);


        lblReceptionistPanel.setForeground(new Color(0, 102, 204));

        // Panel for charts
        chartsPanel = new JPanel();
        chartsPanel.setLayout(new GridLayout(2, 1)); // Two rows for two charts
        getContentPane().add(chartsPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void loadChartData() throws SQLException
    {
        // Create datasets for the charts
        PieDataset appointmentDataset = createAppointmentPieDataset();
        DefaultCategoryDataset paymentDataset = new DefaultCategoryDataset();

        // Connect to the database
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/hospital", "root", "2003");

        // Fetch payment totals
        String paymentQuery = "SELECT DATE(PaymentDate) AS Day, SUM(Amount) AS TotalPayments FROM payments " +
                "WHERE PaymentDate >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) GROUP BY DATE(PaymentDate) " +
                "ORDER BY DATE(PaymentDate)";
        Statement statement = connection.createStatement();
        ResultSet rsPayments = statement.executeQuery(paymentQuery);
        while (rsPayments.next())
        {
            paymentDataset.addValue(rsPayments.getDouble("TotalPayments"), "Payments", rsPayments.getDate("Day"));
        }

        // Create charts
        JFreeChart appointmentChart = ChartFactory.createPieChart(
                "Appointments This Week", appointmentDataset, true, true, false);

        JFreeChart paymentChart = ChartFactory.createLineChart(
                "Payments This Week", "Day", "Total Payments",
                paymentDataset, PlotOrientation.VERTICAL, false, true, false);

        // Customize the appearance of the payment chart
        CategoryPlot plot = paymentChart.getCategoryPlot();
        plot.setRenderer(new LineAndShapeRenderer());
        CategoryAxis domainAxis = plot.getDomainAxis();
        ValueAxis rangeAxis = plot.getRangeAxis();
        ((CategoryAxis) domainAxis).setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 12));
        rangeAxis.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Add charts to the panel
        chartsPanel.add(new ChartPanel(appointmentChart));
        chartsPanel.add(new ChartPanel(paymentChart));

        // Close connections
        rsPayments.close();
        statement.close();
        connection.close();
    }

    private PieDataset createAppointmentPieDataset() throws SQLException
    {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Connect to the database
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/hospital", "root", "2003");

        // Fetch appointment counts for the last 7 days
        String appointmentQuery = "SELECT DATE(Date_and_Time) AS Day, COUNT(AiD) AS AppointmentCount FROM appointment " +
                "WHERE Date_and_Time >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) GROUP BY DATE(Date_and_Time) " +
                "ORDER BY DATE(Date_and_Time)";
        Statement statement = connection.createStatement();
        ResultSet rsAppointments = statement.executeQuery(appointmentQuery);

        while (rsAppointments.next())
        {
            dataset.setValue(rsAppointments.getDate("Day").toString(), rsAppointments.getInt("AppointmentCount"));
        }

        // Close connections
        rsAppointments.close();
        statement.close();
        connection.close();

        return dataset;
    }
}

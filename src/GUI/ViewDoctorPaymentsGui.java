package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class ViewDoctorPaymentsGui extends JFrame
{

    private String doctorId;
    private JTable paymentsTable;
    private JLabel paymentCountLabel;
    private JLabel totalAmountLabel;
    private DoctorPanelGui doctorPanel;

    public ViewDoctorPaymentsGui(LoginGui loginFrame, String doctorId)
    {
        this.doctorId = doctorId;
        initializeUI(loginFrame);
        loadPayments();
        loadTotalPayments();
    }

    private void initializeUI(LoginGui loginFrame)
    {
        setTitle("View Payments");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1000,700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(new Color(0,102,204));

        JLabel paymentsLabel = new JLabel("View Payments", SwingConstants.CENTER);
        paymentsLabel.setFont(new Font("Segoe UI", Font.BOLD, 25));
        paymentsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Padding
        titlePanel.add(paymentsLabel, BorderLayout.CENTER);
        paymentsLabel.setForeground(Color.WHITE);

        // Create content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Table to display payments
        paymentsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(paymentsTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);


        JTableHeader tableHeader = paymentsTable.getTableHeader();
        tableHeader.setBackground(new Color(0, 102, 204));
        tableHeader.setForeground(Color.white);
        tableHeader.setFont(new Font("Arial",Font.BOLD,15));


        paymentsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
        {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
            {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row % 2 == 0)
                {
                    c.setBackground(new Color(240, 240, 240));
                }
                else
                {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        });

        paymentsTable.setRowHeight(30);


        // Payment count and total amount labels
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(2, 1));
        infoPanel.setBackground(Color.WHITE);

        paymentCountLabel = new JLabel("Total Payments: 0", SwingConstants.CENTER);
        paymentCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        totalAmountLabel = new JLabel("Total Amount Today: 0.00", SwingConstants.CENTER);
        totalAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        infoPanel.add(paymentCountLabel);
        infoPanel.add(totalAmountLabel);

        paymentCountLabel.setForeground(new Color(0,102,204));
        totalAmountLabel.setForeground(new Color(204, 0, 54));

        contentPanel.add(infoPanel, BorderLayout.SOUTH);

        // Initialize navigation panel
        doctorPanel = new DoctorPanelGui(this, doctorId, loginFrame);

        // Add the title panel, content panel, and navigation panel to the frame
        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(doctorPanel, BorderLayout.WEST); // Navigation panel on the west
        add(contentPanel, BorderLayout.CENTER); // Content panel in the center

        setVisible(true);
    }

    private void loadPayments()
    {
        // Database connection details
        String url = "jdbc:mysql://localhost:3308/hospital"; // Update with your database URL
        String user = "root"; // Update with your database user
        String password = "2003"; // Update with your database password

        String query = "SELECT p.PaiD, a.Date_and_Time, p.Amount, p.PaymentDate " +
                "FROM payments p " +
                "JOIN appointment a ON p.AiD = a.AiD " +
                "WHERE a.CiD IN (SELECT Cid FROM clinic WHERE Did = ?)"; // Filter by doctor ID

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query))
        {

            statement.setString(1, doctorId);
            ResultSet resultSet = statement.executeQuery();

            // Create table model
            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"Payment ID", "Appointment Date and Time", "Amount", "Payment Date"}, 0);

            // Populate table model with data
            while (resultSet.next())
            {
                String paymentId = resultSet.getString("PaiD");
                Date appointmentDateTime = resultSet.getDate("Date_and_Time");
                double amount = resultSet.getDouble("Amount");
                Date paymentDate = resultSet.getDate("PaymentDate");

                model.addRow(new Object[]{paymentId, appointmentDateTime, amount, paymentDate});
            }

            paymentsTable.setModel(model);

            // Update payment count
            int paymentCount = model.getRowCount();
            paymentCountLabel.setText("Total Payments: " + paymentCount);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading payments", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTotalPayments()
    {
        // Database connection details
        String url = "jdbc:mysql://localhost:3308/hospital"; // Update with your database URL
        String user = "root"; // Update with your database user
        String password = "2003"; // Update with your database password

        String query = "SELECT SUM(p.Amount) AS TotalAmount " +
                "FROM payments p " +
                "JOIN appointment a ON p.AiD = a.AiD " +
                "WHERE a.CiD IN (SELECT Cid FROM clinic WHERE Did = ?) " +
                "AND p.PaymentDate = CURDATE()"; // Filter payments by today's date

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query))
        {

            statement.setString(1, doctorId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                double totalAmount = resultSet.getDouble("TotalAmount");
                totalAmountLabel.setText(String.format("Total Amount Today: %.2f", totalAmount));
            }
            else
            {
                totalAmountLabel.setText("Total Amount Today: 0.00");
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading total payments", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class ViewDoctorAppointmentsGui extends JFrame
{

    private String doctorId;
    private JTable appointmentsTable;
    private JLabel appointmentCountLabel;

    public ViewDoctorAppointmentsGui(LoginGui loginForm, String doctorId)
    {
        this.doctorId = doctorId;
        initializeUI(loginForm);
        loadAppointments();
    }

    private void initializeUI(LoginGui loginForm)
    {
        setTitle("View Appointments");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1000,700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(new Color(0,102,204));

        JLabel appointmentsLabel = new JLabel("View Appointments", JLabel.CENTER);
        appointmentsLabel.setFont(new Font("Segoe UI", Font.BOLD, 25));
        appointmentsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add padding
        titlePanel.add(appointmentsLabel, BorderLayout.CENTER);

        appointmentsLabel.setForeground(Color.WHITE);

        // Create content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Table to display appointments
        appointmentsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JTableHeader tableHeader = appointmentsTable.getTableHeader();
        tableHeader.setBackground(new Color(0, 102, 204));
        tableHeader.setForeground(Color.white);
        tableHeader.setFont(new Font("Arial",Font.BOLD,15));


        appointmentsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
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

        appointmentsTable.setRowHeight(30);


        // Appointment count label
        appointmentCountLabel = new JLabel("Total Appointments: 0", JLabel.CENTER);
        appointmentCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        appointmentCountLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add padding
        contentPanel.add(appointmentCountLabel, BorderLayout.SOUTH);

        appointmentCountLabel.setForeground(new Color(0,102,204));

        // Add title panel and content panel to the frame
        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);

        // Add the navigation panel
        DoctorPanelGui navPanel = new DoctorPanelGui(this, doctorId, loginForm);
        add(navPanel, BorderLayout.WEST); // Navigation panel on the west

        add(contentPanel, BorderLayout.CENTER); // Content panel in the center

        setVisible(true);
    }

    private void loadAppointments()

    {
        // Database connection details
        String url = "jdbc:mysql://localhost:3308/hospital"; // Update with your database URL
        String user = "root"; // Update with your database user
        String password = "2003"; // Update with your database password

        String query = "SELECT a.AiD, p.First_Name, p.Last_Name, c.Cid, c.date, c.startTime " +
                "FROM appointment a " +
                "JOIN patient p ON a.PiD = p.PiD " +
                "JOIN clinic c ON a.CiD = c.Cid " +
                "WHERE c.Did = ?"; // Filter by doctor ID

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query))
        {

            statement.setString(1, doctorId);
            ResultSet resultSet = statement.executeQuery();

            // Create table model
            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"Appointment ID", "Patient First Name", "Patient Last Name", "Clinic ID", "Date", "Start Time"}, 0);

            // Populate table model with data
            while (resultSet.next())
            {
                String appointmentId = resultSet.getString("AiD");
                String firstName = resultSet.getString("First_Name");
                String lastName = resultSet.getString("Last_Name");
                String clinicId = resultSet.getString("Cid");
                Date date = resultSet.getDate("date");
                Time startTime = resultSet.getTime("startTime");

                model.addRow(new Object[]{appointmentId, firstName, lastName, clinicId, date, startTime});
            }

            appointmentsTable.setModel(model);

            // Update appointment count
            int appointmentCount = model.getRowCount();
            appointmentCountLabel.setText("Total Appointments: " + appointmentCount);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading appointments", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

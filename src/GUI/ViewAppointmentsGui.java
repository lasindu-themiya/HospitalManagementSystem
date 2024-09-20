package GUI;

import DBOperations.AppointmentDB;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewAppointmentsGui extends JFrame {

    private ReceptionistPanelGui receptionistPanel;
    private AppointmentDB appointmentDB;

    private JTextField doctorSearchField;
    private JTextField dateSearchField;
    private JButton searchButton;
    private JTable appointmentsTable;
    private JScrollPane tableScrollPane;

    private String receptionistid;

    public ViewAppointmentsGui(LoginGui loginFrame,String receptionistid)
    {
        try
        {
            appointmentDB = new AppointmentDB();
            this.receptionistid=receptionistid;
            initializeUi(loginFrame);
        } catch (SQLException e)
        {
            JOptionPane.showMessageDialog(this, "Error connecting to the database: " + e.getMessage());
        }
    }

    private void initializeUi(LoginGui loginFrame) throws SQLException
    {
        setTitle("View Appointments");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1000,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize navigation panel
        receptionistPanel = new ReceptionistPanelGui(this, loginFrame, receptionistid);

        // Create title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("View Appointments", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        titleLabel.setForeground(new Color(0, 102, 204));

        // Create content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create panel for search fields
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(3, 2, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Initialize search fields
        doctorSearchField = createTextField();
        dateSearchField = createTextField();
        searchButton = new JButton("Search");

        searchButton.setForeground(Color.white);
        searchButton.setBackground(new Color(0, 102, 204));

        // Add search fields to the searchPanel
        searchPanel.add(new JLabel("Doctor Name:"));
        searchPanel.add(doctorSearchField);
        searchPanel.add(new JLabel("Clinic Date (YYYY-MM-DD):"));
        searchPanel.add(dateSearchField);
        searchPanel.add(new JLabel(""));
        searchPanel.add(searchButton);

        // Create table for displaying appointments
        appointmentsTable = new JTable();
        tableScrollPane = new JScrollPane(appointmentsTable);


        JTableHeader tableHeader = appointmentsTable.getTableHeader();
        tableHeader.setBackground(new Color(0, 102, 204));
        tableHeader.setForeground(Color.white);
        tableHeader.setFont(new Font("Arial",Font.BOLD,15));

        appointmentsTable.setRowHeight(30);


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
        loadAppointments(); // Load initial appointment data

        // Add action listener to search button
        searchButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    searchAppointments();
                }
                catch (SQLException ex)
                {
                    JOptionPane.showMessageDialog(null, "Error searching appointments: " + ex.getMessage());
                }
            }
        });

        // Add searchPanel and tableScrollPane to contentPanel
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        contentPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Set layout and add components to the frame
        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(receptionistPanel, BorderLayout.WEST); // Navigation panel on the west
        add(contentPanel, BorderLayout.CENTER); // Content panel in the center

        setVisible(true);
    }

    private JTextField createTextField()
    {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return textField;
    }

    private void loadAppointments() throws SQLException
    {
        ResultSet rs = appointmentDB.getAllAppointments();
        appointmentsTable.setModel(new AppointmentTableModel(rs));
    }

    private void searchAppointments() throws SQLException
    {
        String doctorName = doctorSearchField.getText();
        String clinicDate = dateSearchField.getText();
        ResultSet rs = appointmentDB.searchAppointments(doctorName, clinicDate);
        appointmentsTable.setModel(new AppointmentTableModel(rs));
    }
}

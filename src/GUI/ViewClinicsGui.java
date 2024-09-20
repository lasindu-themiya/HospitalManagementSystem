package GUI;

import DBOperations.ClinicDB;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewClinicsGui extends JFrame
{

    private ReceptionistPanelGui receptionistPanel;
    private ClinicDB clinicDB;
    private JTable clinicTable;
    private String receptionistId;

    public ViewClinicsGui(LoginGui loginFrame,String receptionistId)
    {
        try {
            this.receptionistId=receptionistId;
            clinicDB = new ClinicDB();
            initializeUi(loginFrame);
            loadClinicData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error connecting to the database: " + e.getMessage());
        }
    }

    private void initializeUi(LoginGui loginFrame) throws SQLException
    {
        setTitle("View Clinics");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1000,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize navigation panel
        receptionistPanel = new ReceptionistPanelGui(this, loginFrame, receptionistId);

        // Create title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Available Clinics", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add padding around the label
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        titleLabel.setForeground(new Color(0, 102, 204));

        // Create table panel
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        DefaultTableModel tableModel = new DefaultTableModel();
        clinicTable = new JTable(tableModel);
        tableModel.addColumn("Clinic ID");
        tableModel.addColumn("Doctor Name");
        tableModel.addColumn("Start Time");
        tableModel.addColumn("Date");
        tableModel.addColumn("Patient Limit");
        tableModel.addColumn("Room Number");

        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(clinicTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JTableHeader tableHeader = clinicTable.getTableHeader();
        tableHeader.setBackground(new Color(0, 102, 204));
        tableHeader.setForeground(Color.white);
        tableHeader.setFont(new Font("Arial",Font.BOLD,15));


        clinicTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
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

        clinicTable.setRowHeight(30);

        // Add the title panel and table panel to the frame
        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(receptionistPanel, BorderLayout.WEST); // Navigation panel on the west
        add(tablePanel, BorderLayout.CENTER); // Table panel in the center

        setVisible(true);
    }

    private void loadClinicData()
    {
        try
        {
            ResultSet rs = clinicDB.viewUpcomingClinics();
            DefaultTableModel model = (DefaultTableModel) clinicTable.getModel();
            model.setRowCount(0); // Clear existing rows

            while (rs.next())
            {
                Object[] row = new Object[6];
                row[0] = rs.getString("Cid");
                row[1] = rs.getString("DoctorName");
                row[2] = rs.getTime("startTime");
                row[3] = rs.getDate("date");
                row[4] = rs.getInt("plimit");
                row[5] = rs.getInt("roomnum");
                model.addRow(row);
            }
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(this, "Error loading clinic data: " + e.getMessage());
        }
    }
}
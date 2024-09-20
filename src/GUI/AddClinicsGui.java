package GUI;

import DBOperations.ClinicDB;
import DBOperations.DocotrDB;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

public class AddClinicsGui extends JFrame
{

    private ClinicDB clinicDB;
    private DocotrDB doctorDB;
    private AdminPanelGui navpanel;
    private LoginGui loginFrame;

    private JTable clinicTable;
    private DefaultTableModel tableModel;

    private JComboBox<String> comboDid;
    private JTextField txtPlimit;
    private JTextField txtRoomnum;
    private JDateChooser dateChooser;
    private JSpinner timeSpinner;

    private JLabel lblAddClinicPanel;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;

    public AddClinicsGui(LoginGui loginFrame) throws SQLException
    {
        this.loginFrame = loginFrame;
        clinicDB = new ClinicDB();
        doctorDB = new DocotrDB();
        InitializeUI(loginFrame);
        loadClinicData();
        loadDoctorIDs();
    }

    private void InitializeUI(LoginGui loginFrame)
    {
        setTitle("Clinic Control");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1000,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Initialize navigation panel
        navpanel = new AdminPanelGui(this, loginFrame);
        getContentPane().add(navpanel, BorderLayout.WEST);

        lblAddClinicPanel = new JLabel("Clinic Control", JLabel.CENTER);
        lblAddClinicPanel.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lblAddClinicPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add padding around the label
        getContentPane().add(lblAddClinicPanel, BorderLayout.NORTH);

        lblAddClinicPanel.setForeground(new Color(0, 102, 204));

        // Create and configure the clinic table
        String[] columnNames = {"Cid", "Did", "Start Time", "Date", "Patient Limit", "Room Number"};
        tableModel = new DefaultTableModel(columnNames, 0);
        clinicTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(clinicTable);


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
        TableColumnModel columnModel = clinicTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(50);
        columnModel.getColumn(2).setPreferredWidth(100);

        // Create panel for text fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2, 10, 10)); // 6 rows, 2 columns with 10px gaps
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the panel

        // Text fields for each column
        comboDid = new JComboBox<>(); // Initialize the combo box
        txtPlimit = new JTextField();
        txtRoomnum = new JTextField();

        // Date chooser and time spinner
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        SpinnerDateModel timeModel = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
        timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
        timeSpinner.setEditor(timeEditor);

        // Add labels and text fields to inputPanel
        inputPanel.add(new JLabel("Doctor ID:"));
        inputPanel.add(comboDid); // Add the combo box to the panel
        inputPanel.add(new JLabel("Start Time:"));
        inputPanel.add(timeSpinner);
        inputPanel.add(new JLabel("Date:"));
        inputPanel.add(dateChooser);
        inputPanel.add(new JLabel("Patient Limit:"));
        inputPanel.add(txtPlimit);
        inputPanel.add(new JLabel("Room Number:"));
        inputPanel.add(txtRoomnum);

        // Create a panel for buttons and position it at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        btnAdd.setBackground(new Color(0, 102, 204));
        btnAdd.setForeground(Color.white);
        btnUpdate.setBackground(new Color(0, 102, 204));
        btnUpdate.setForeground(Color.white);
        btnDelete.setBackground(new Color(0, 102, 204));
        btnDelete.setForeground(Color.white);
        btnClear.setBackground(new Color(0, 102, 204));
        btnClear.setForeground(Color.white);


        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        // Create a main panel to hold the input panel and button panel
        JPanel inputAndButtonPanel = new JPanel(new BorderLayout());
        inputAndButtonPanel.add(inputPanel, BorderLayout.CENTER);
        inputAndButtonPanel.add(buttonPanel, BorderLayout.SOUTH); // Add buttons at the bottom

        // Create a main panel to hold the table and the input and button panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputAndButtonPanel, BorderLayout.SOUTH); // Add input and buttons panel at the bottom

        // Add the main panel to the CENTER region of the JFrame
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        // Add action listeners for buttons
        btnAdd.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    handleAdd();
                }
                catch (SQLException ex)
                {
                    JOptionPane.showMessageDialog(AddClinicsGui.this, "An error occurred while adding clinic", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnUpdate.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    handleUpdate();
                }
                catch (SQLException ex)
                {
                    JOptionPane.showMessageDialog(AddClinicsGui.this, "An error occurred while updating clinic", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnDelete.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                handleDelete();
            }
        });

        btnClear.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                clearInput();
            }
        });

        // Add mouse listener to table to populate text fields on row selection
        clinicTable.getSelectionModel().addListSelectionListener(e ->
        {
            int selectedRow = clinicTable.getSelectedRow();
            if (selectedRow != -1)
            {
                comboDid.setSelectedItem(tableModel.getValueAt(selectedRow, 1));
                timeSpinner.setValue((Date) tableModel.getValueAt(selectedRow, 2));
                dateChooser.setDate((Date) tableModel.getValueAt(selectedRow, 3));
                txtPlimit.setText(String.valueOf(tableModel.getValueAt(selectedRow, 4)));
                txtRoomnum.setText(String.valueOf(tableModel.getValueAt(selectedRow, 5)));
            }
        });

        setVisible(true);
    }

    private void loadClinicData()
    {
        try
        {
            ResultSet rs = clinicDB.viewClinics();
            tableModel.setRowCount(0);

            while (rs.next())
            {
                String cid = rs.getString("Cid");
                String did = rs.getString("Did");
                Date startTime = rs.getTime("startTime");
                Date date = rs.getDate("date");
                int plimit = rs.getInt("plimit");
                int roomnum = rs.getInt("roomnum");

                tableModel.addRow(new Object[]{cid, did, startTime, date, plimit, roomnum});
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while loading clinic data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDoctorIDs()
    {
        try
        {
            ResultSet rs = doctorDB.getAllDoctorIds(); // Method to fetch doctor IDs
            while (rs.next())
            {
                String did = rs.getString("Did");
                comboDid.addItem(did); // Add each doctor ID to the combo box
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while loading doctor IDs", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAdd() throws SQLException
    {
        String did = (String) comboDid.getSelectedItem(); // Get selected doctor ID from combo box
        Date startTime = (Date) timeSpinner.getValue();
        Date date = dateChooser.getDate();
        String plimitStr = txtPlimit.getText();
        String roomnumStr = txtRoomnum.getText();

        try
        {
            int plimit = Integer.parseInt(plimitStr);
            int roomnum = Integer.parseInt(roomnumStr);

            // Assuming CID is auto-generated or managed elsewhere
            String cid = ""; // Replace with actual CID logic if needed

            clinicDB.addClinic( did, new java.sql.Time(startTime.getTime()), new java.sql.Date(date.getTime()), plimit, roomnum);
            loadClinicData();
            clearInput();
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(this, "Patient limit and room number must be integers", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdate() throws SQLException
    {
        int selectedRow = clinicTable.getSelectedRow();
        if (selectedRow != -1) {
            String cid = (String) tableModel.getValueAt(selectedRow, 0);
            String did = (String) comboDid.getSelectedItem(); // Get selected doctor ID from combo box
            Date startTime = (Date) timeSpinner.getValue();
            Date date = dateChooser.getDate();
            String plimitStr = txtPlimit.getText();
            String roomnumStr = txtRoomnum.getText();

            try
            {
                int plimit = Integer.parseInt(plimitStr);
                int roomnum = Integer.parseInt(roomnumStr);

                clinicDB.updateClinic(cid, did, new java.sql.Time(startTime.getTime()), new java.sql.Date(date.getTime()), plimit, roomnum);
                loadClinicData();
                clearInput();
            }

            catch (NumberFormatException e)
            {
                JOptionPane.showMessageDialog(this, "Patient limit and room number must be integers", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        else
        {
            JOptionPane.showMessageDialog(this, "Please select a clinic to update", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleDelete()
    {
        int selectedRow = clinicTable.getSelectedRow();
        if (selectedRow != -1)
        {
            String cid = (String) tableModel.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this clinic?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION)
            {
                try
                {
                    clinicDB.deleteClinic(cid);
                    loadClinicData();
                    clearInput();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "An error occurred while deleting the clinic", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Please select a clinic to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearInput()
    {
        comboDid.setSelectedIndex(0);
        timeSpinner.setValue(new Date());
        dateChooser.setDate(new Date());
        txtPlimit.setText("");
        txtRoomnum.setText("");
    }
}


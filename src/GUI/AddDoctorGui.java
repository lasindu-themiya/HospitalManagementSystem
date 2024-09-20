package GUI;

import DBOperations.DocotrDB;

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

public class AddDoctorGui extends JFrame
{

    private DocotrDB docotrDB;
    private AdminPanelGui navpanel;
    private JLabel lblAddDoctorPanel;
    private LoginGui loginFrame;

    private JTable doctorTable;
    private DefaultTableModel tableModel;

    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtPassword;
    private JTextField txtContact;
    private JTextField txtSpecialization;
    private JTextField txtEmail;
    private JTextField txtRate;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;

    public AddDoctorGui(LoginGui loginFrame) throws SQLException
    {
        this.loginFrame = loginFrame;
        docotrDB = new DocotrDB();
        InitializeUI(loginFrame);
        loadDoctorData();
    }

    private void InitializeUI(LoginGui loginFrame)
    {
        setTitle("Doctor Control");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1000,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Initialize navigation panel
        navpanel = new AdminPanelGui(this, loginFrame);
        getContentPane().add(navpanel, BorderLayout.WEST);

        lblAddDoctorPanel = new JLabel("Doctor Control", JLabel.CENTER);
        lblAddDoctorPanel.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lblAddDoctorPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add padding around the label
        getContentPane().add(lblAddDoctorPanel, BorderLayout.NORTH);

        lblAddDoctorPanel.setForeground(new Color(0, 102, 204));

        // Create and configure the doctor table
        String[] columnNames = {"DID", "First Name", "Last Name", "Password", "Contact", "Specialization", "Email", "Rate"};
        tableModel = new DefaultTableModel(columnNames, 0);
        doctorTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(doctorTable);

        JTableHeader tableHeader = doctorTable.getTableHeader();
        tableHeader.setBackground(new Color(0, 102, 204));
        tableHeader.setForeground(Color.white);
        tableHeader.setFont(new Font("Arial",Font.BOLD,15));


        doctorTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
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


        doctorTable.setRowHeight(30);
        TableColumnModel columnmodel = doctorTable.getColumnModel();
        columnmodel.getColumn(0).setPreferredWidth(30);
        columnmodel.getColumn(6).setPreferredWidth(230);

        // Create panel for text fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 2, 10, 10)); // 7 rows, 2 columns with 10px gaps
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the panel

        // Text fields for each column
        txtFirstName = new JTextField();
        txtLastName = new JTextField();
        txtPassword = new JTextField();
        txtContact = new JTextField();
        txtSpecialization = new JTextField();
        txtEmail = new JTextField();
        txtRate = new JTextField();

        // Add labels and text fields to inputPanel
        inputPanel.add(new JLabel("First Name:"));
        inputPanel.add(txtFirstName);
        inputPanel.add(new JLabel("Last Name:"));
        inputPanel.add(txtLastName);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(txtPassword);
        inputPanel.add(new JLabel("Contact:"));
        inputPanel.add(txtContact);
        inputPanel.add(new JLabel("Specialization:"));
        inputPanel.add(txtSpecialization);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(txtEmail);
        inputPanel.add(new JLabel("Rate:"));
        inputPanel.add(txtRate);

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
                handleAdd();
            }
        });

        btnUpdate.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                handleUpdate();
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
        doctorTable.getSelectionModel().addListSelectionListener(e ->
        {
            int selectedRow = doctorTable.getSelectedRow();
            if (selectedRow != -1) {
                txtFirstName.setText((String) tableModel.getValueAt(selectedRow, 1));
                txtLastName.setText((String) tableModel.getValueAt(selectedRow, 2));
                txtPassword.setText((String) tableModel.getValueAt(selectedRow, 3));
                txtContact.setText((String) tableModel.getValueAt(selectedRow, 4));
                txtSpecialization.setText((String) tableModel.getValueAt(selectedRow, 5));
                txtEmail.setText((String) tableModel.getValueAt(selectedRow, 6));
                txtRate.setText(String.valueOf(tableModel.getValueAt(selectedRow, 7)));
            }
        });

        setVisible(true);
    }

    private void loadDoctorData()
    {
        try
        {
            ResultSet rs = docotrDB.viewDoctors(); // Assuming you have this method in your AdminDB class
            tableModel.setRowCount(0); // Clear existing data

            while (rs.next())
            {
                String doctorID = rs.getString("Did");
                String firstName = rs.getString("fname");
                String lastName = rs.getString("lname");
                String password = rs.getString("pass");
                String contact = rs.getString("contact");
                String specialization = rs.getString("specialization");
                String email = rs.getString("email");
                double rate = rs.getDouble("rate");
                tableModel.addRow(new Object[]{doctorID, firstName, lastName, password, contact, specialization, email, rate});
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while loading doctor data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAdd()
    {
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String password = txtPassword.getText();
        String contactStr = txtContact.getText();
        String specialization = txtSpecialization.getText();
        String email = txtEmail.getText();
        double rate = Double.parseDouble(txtRate.getText());

        try
        {
            int contact = Integer.parseInt(contactStr); // Parse the contact as an integer
            docotrDB.addDoctor(firstName, lastName, password, contact, specialization, email, rate);
            tableModel.addRow(new Object[]{docotrDB.generateNextID(), firstName, lastName, password, contact, specialization, email, rate});
            loadDoctorData();
            clearInput();
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(this, "Please enter a valid contact number.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while adding the doctor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdate()
    {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow != -1)
        {
            String firstName = txtFirstName.getText();
            String lastName = txtLastName.getText();
            String password = txtPassword.getText();
            String contactStr = txtContact.getText();
            String specialization = txtSpecialization.getText();
            String email = txtEmail.getText();
            double rate = Double.parseDouble(txtRate.getText());

            try
            {
                int contact = Integer.parseInt(contactStr); // Parse the contact as an integer
                int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to update this record?", "Confirm Update", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION)
                {
                    String doctorID = (String) tableModel.getValueAt(selectedRow, 0); // Get doctor ID
                    docotrDB.updateDoctor(firstName, lastName, password, contact, specialization, email, rate, doctorID);
                    loadDoctorData();
                    clearInput();
                }
            }
            catch (NumberFormatException e)
            {
                JOptionPane.showMessageDialog(this, "Please enter a valid contact number.", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred while updating the doctor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Please select a doctor to update.", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleDelete()
    {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow != -1)
        {
            String doctorID = (String) tableModel.getValueAt(selectedRow, 0);
            int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION)
            {
                try
                {
                    docotrDB.deleteDoctor(doctorID);
                    loadDoctorData();
                    clearInput();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "An error occurred while deleting the doctor.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Please select a doctor to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearInput()
    {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtPassword.setText("");
        txtContact.setText("");
        txtSpecialization.setText("");
        txtEmail.setText("");
        txtRate.setText("");
    }
}

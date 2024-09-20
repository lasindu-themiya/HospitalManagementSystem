package GUI;

import DBOperations.AdminDB;

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

public class AddAdminsGui extends JFrame
{

    private AdminDB adminDB;
    private AdminPanelGui navpanel;
    private JLabel lblAddAdminPanel;
    private LoginGui loginFrame;

    private JTable adminTable;
    private DefaultTableModel tableModel;

    private JTextField txtUsername;
    private JTextField txtPassword;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;

    private JButton btnClear;

    public AddAdminsGui(LoginGui loginFrame) throws SQLException
    {
        this.loginFrame = loginFrame;
        adminDB = new AdminDB();
        InitializeUI(loginFrame);
        loadAdminData();
    }

    private void InitializeUI(LoginGui loginFrame)
    {
        setTitle("Admins Control");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1000,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Initialize navigation panel
        navpanel = new AdminPanelGui(this,loginFrame);
        getContentPane().add(navpanel, BorderLayout.WEST);

        lblAddAdminPanel = new JLabel("Admin Control", JLabel.CENTER);
        lblAddAdminPanel.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lblAddAdminPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add padding around the label
        getContentPane().add(lblAddAdminPanel, BorderLayout.NORTH);

        lblAddAdminPanel.setForeground(new Color(0, 102, 204));

        // Create and configure the admin table
        String[] columnNames = {"AID", "Username", "Password"};
        tableModel = new DefaultTableModel(columnNames, 0);
        adminTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(adminTable);


        JTableHeader tableHeader = adminTable.getTableHeader();
        tableHeader.setBackground(new Color(0, 102, 204));
        tableHeader.setForeground(Color.white);
        tableHeader.setFont(new Font("Arial",Font.BOLD,15));


        adminTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
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



        adminTable.setRowHeight(35);
        TableColumnModel columnmodel = adminTable.getColumnModel();
        columnmodel.getColumn(0).setPreferredWidth(20);
        adminTable.setBackground(Color.WHITE);


        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Username:"), gbc);

        gbc.gridy = 1;
        inputPanel.add(new JLabel("Password:"), gbc);

        gbc.anchor = GridBagConstraints.WEST; // Align text fields to the left
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make the text fields fill the available space
        txtUsername = new JTextField(20);
        inputPanel.add(txtUsername, gbc);
        gbc.gridy = 1;
        txtPassword = new JTextField(20);
        inputPanel.add(txtPassword, gbc);


        // Create panel for buttons and add buttons in a single row
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

        JPanel inputAndButtonPanel = new JPanel(new BorderLayout());
        inputAndButtonPanel.add(inputPanel, BorderLayout.CENTER);
        inputAndButtonPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Create a main panel to hold the table and input fields with buttons
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputAndButtonPanel, BorderLayout.SOUTH);

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
                ClearInput();
            }
        });

        // Add mouse listener to table to populate text fields on row selection
        adminTable.getSelectionModel().addListSelectionListener(e ->
        {
            int selectedRow = adminTable.getSelectedRow();
            if (selectedRow != -1) {
                txtUsername.setText((String) tableModel.getValueAt(selectedRow, 1));
                txtPassword.setText((String) tableModel.getValueAt(selectedRow, 2));
            }
        });

        setVisible(true);
    }


    private void loadAdminData()
    {
        try
        {
            ResultSet rs = adminDB.viewAdmins();
            tableModel.setRowCount(0); // Clear existing data

            while (rs.next())
            {
                String adminID = rs.getString("Aid");
                String username = rs.getString("uname");
                String password = rs.getString("pass");
                tableModel.addRow(new Object[]{adminID, username, password});
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while loading admin data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAdd()
    {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        try
        {
            adminDB.addAdmin(username, password);
            tableModel.addRow(new Object[]{adminDB.generateNextID(), username, password});
            loadAdminData();
            ClearInput();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while adding the admin", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Fields cannot be Null", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdate()
    {
        int selectedRow = adminTable.getSelectedRow();
        if (selectedRow != -1)
        {
            String username = txtUsername.getText();
            String password = txtPassword.getText();

            int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to update this record?", "Confirm Update", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                try
                {
                    String adminID = (String) tableModel.getValueAt(selectedRow, 0);
                    adminDB.updateAdmin(username, password, adminID);
                    tableModel.setValueAt(username, selectedRow, 1);
                    tableModel.setValueAt(password, selectedRow, 2);
                    loadAdminData();
                    ClearInput();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "An error occurred while updating the admin", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void handleDelete()
    {
        int selectedRow = adminTable.getSelectedRow();
        if (selectedRow != -1)
        {
            int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION)
            {
                String adminID = (String) tableModel.getValueAt(selectedRow, 0);
                try
                {
                    adminDB.deleteAdmin(adminID);
                    tableModel.removeRow(selectedRow);
                    loadAdminData();
                    ClearInput();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "An error occurred while deleting the admin", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void ClearInput()
    {
        txtUsername.setText("");
        txtPassword.setText("");
    }

}

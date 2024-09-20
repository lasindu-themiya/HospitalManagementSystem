package GUI;

import DBOperations.ReceptionistDB;

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

public class AddReceptionistGui extends JFrame
{

    private ReceptionistDB receptionistDB;
    private AdminPanelGui navpanel;
    private JLabel lblAddReceptionistPanel;
    private LoginGui loginFrame;

    private JTable receptionistTable;
    private DefaultTableModel tableModel;

    private JTextField txtuname;
    private JTextField txtpassword;
    private JTextField txtname;
    private JTextField txtage;
    private JTextField txttelephone;
    private JTextField txtaddress;
    private JTextField txtnic;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;

    private JButton btnClear;


    public AddReceptionistGui(LoginGui loginFrame) throws SQLException
    {
        this.loginFrame = loginFrame;
        receptionistDB = new ReceptionistDB();
        InitializeUI(loginFrame);
        loadReceptionistData();
    }

    private void InitializeUI(LoginGui loginFrame)
    {
        setTitle("Receptionist Control");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1000,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Initialize navigation panel
        navpanel = new AdminPanelGui(this, loginFrame);
        getContentPane().add(navpanel, BorderLayout.WEST);

        lblAddReceptionistPanel = new JLabel("Receptionist Control", JLabel.CENTER);
        lblAddReceptionistPanel.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lblAddReceptionistPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add padding around the label
        getContentPane().add(lblAddReceptionistPanel, BorderLayout.NORTH);

        lblAddReceptionistPanel.setForeground(new Color(0, 102, 204));

        // Create and configure the doctor table
        String[] columnNames = {"RID","User Name","Password","Name","Age","Telephone","Address","NIC"};
        tableModel = new DefaultTableModel(columnNames, 0);
        receptionistTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(receptionistTable);

        JTableHeader tableHeader = receptionistTable.getTableHeader();
        tableHeader.setBackground(new Color(0, 102, 204));
        tableHeader.setForeground(Color.white);
        tableHeader.setFont(new Font("Arial",Font.BOLD,15));


        receptionistTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
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



        receptionistTable.setRowHeight(30);
        TableColumnModel columnmodel = receptionistTable.getColumnModel();
        columnmodel.getColumn(0).setPreferredWidth(30);


        // Create panel for text fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 2, 10, 10)); // 7 rows, 2 columns with 10px gaps
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the panel

        // Text fields for each column
        txtuname = new JTextField();
        txtpassword = new JTextField();
        txtname = new JTextField();
        txtage = new JTextField();
        txttelephone = new JTextField();
        txtaddress = new JTextField();
        txtnic = new JTextField();

        // Add labels and text fields to inputPanel
        inputPanel.add(new JLabel("User Name:"));
        inputPanel.add(txtuname);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(txtpassword);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(txtname);
        inputPanel.add(new JLabel("Age:"));
        inputPanel.add(txtage);
        inputPanel.add(new JLabel("Telephone:"));
        inputPanel.add(txttelephone);
        inputPanel.add(new JLabel("Address:"));
        inputPanel.add(txtaddress);
        inputPanel.add(new JLabel("NIC:"));
        inputPanel.add(txtnic);

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
                    throw new RuntimeException(ex);
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
                    throw new RuntimeException(ex);
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
        receptionistTable.getSelectionModel().addListSelectionListener(e ->
        {
            int selectedRow = receptionistTable.getSelectedRow();
            if (selectedRow != -1) {
                txtuname.setText((String) tableModel.getValueAt(selectedRow, 1));
                txtpassword.setText((String) tableModel.getValueAt(selectedRow, 2));
                txtname.setText((String) tableModel.getValueAt(selectedRow, 3));
                txtage.setText(String.valueOf( tableModel.getValueAt(selectedRow, 4)));
                txttelephone.setText((String.valueOf( tableModel.getValueAt(selectedRow, 5))));
                txtaddress.setText((String) tableModel.getValueAt(selectedRow, 6));
                txtnic.setText((String)tableModel.getValueAt(selectedRow, 7));
            }
        });

        setVisible(true);
    }

    private void loadReceptionistData()
    {
        try
        {
            ResultSet rs = receptionistDB.viewReceptionists();
            tableModel.setRowCount(0);

            while (rs.next())
            {
                String recid = rs.getString("Rid");
                String uname = rs.getString("uname");
                String pass = rs.getString("pass");
                String name = rs.getString("name");
                Integer age = rs.getInt("age");
                Integer telephone = rs.getInt("telephone");
                String address = rs.getString("address");
                String nic = rs.getString("nic");

                tableModel.addRow(new Object[]{recid, uname, pass, name, age, telephone, address, nic});
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while loading receptionist data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAdd() throws SQLException
    {
        String uname = txtuname.getText();
        String pass = txtpassword.getText();
        String name = txtname.getText();
        String ageStr = txtage.getText();
        String tele = txttelephone.getText();
        String address = txtaddress.getText();
        String nic = txtnic.getText();

        try
        {
            int age = Integer.parseInt(ageStr);
            int telephone = Integer.parseInt(tele);
            receptionistDB.addReceptionists(uname, pass, name, age, telephone, address, nic);
            loadReceptionistData();
            clearInput();
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(this, "Please enter a valid age or telephone.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleUpdate() throws SQLException
    {
        int selectedRow = receptionistTable.getSelectedRow();
        if (selectedRow != -1)
        {
            String uname = txtuname.getText();
            String pass = txtpassword.getText();
            String name = txtname.getText();
            String ageStr = txtage.getText();
            String tele = txttelephone.getText();
            String address = txtaddress.getText();
            String nic = txtnic.getText();

            try
            {
                int age = Integer.parseInt(ageStr);
                int telephone = Integer.parseInt(tele);
                int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to update this record?", "Confirm Update", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    String recID = (String) tableModel.getValueAt(selectedRow, 0);
                    receptionistDB.updateReceptionists(recID, uname, pass, name, age, telephone, address, nic);
                    loadReceptionistData();
                    clearInput();
                }
            }
            catch (NumberFormatException e)
            {
                JOptionPane.showMessageDialog(this, "Please enter a valid age or Telephone.", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Please select a receptionist to update.", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleDelete()
    {
        int selectedRow = receptionistTable.getSelectedRow();
        if (selectedRow != -1)
        {
            String receptionistId = (String) tableModel.getValueAt(selectedRow, 0);
            int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION)
            {
                try
                {
                    receptionistDB.deleteReceptionists(receptionistId);
                    loadReceptionistData();
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
            JOptionPane.showMessageDialog(this, "Please select a Receptionist to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearInput()
    {
        txtuname.setText("");
        txtpassword.setText("");
        txtname.setText("");
        txtnic.setText("");
        txtaddress.setText("");
        txtage.setText("");
        txttelephone.setText("");
    }
}

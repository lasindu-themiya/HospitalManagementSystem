package GUI;

import DBOperations.ReceptionistDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileGui extends JFrame
{

    private ReceptionistPanelGui receptionistPanel;
    private JTextField fullNameField;
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField ageField;
    private JTextField contactField;
    private JTextField addressField;
    private JTextField nicField;
    private JButton updateButton;
    private String receptionistId;
    private ReceptionistDB receptionistDB;

    public ProfileGui(LoginGui loginFrame, String receptionistId) throws SQLException
    {
        this.receptionistId = receptionistId;
        receptionistDB = new ReceptionistDB();
        initializeUi(loginFrame);
        loadProfileData();
    }

    private void initializeUi(LoginGui loginFrame) throws SQLException
    {
        setTitle("Receptionist Profile");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize navigation panel
        receptionistPanel = new ReceptionistPanelGui(this, loginFrame, receptionistId);

        // Create title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);

        JLabel profileTitle = new JLabel("My Profile", JLabel.CENTER);
        profileTitle.setFont(new Font("Segoe UI", Font.BOLD, 25));
        profileTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add padding around the label
        titlePanel.add(profileTitle, BorderLayout.CENTER);

        profileTitle.setForeground(new Color(0, 102, 204));

        // Create content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create panel for text fields
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for more control
        fieldsPanel.setBackground(Color.WHITE);
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the panel

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Initialize text fields
        fullNameField = createStyledTextField();
        usernameField = createStyledTextField();
        passwordField = createStyledTextField();
        ageField = createStyledTextField();
        contactField = createStyledTextField();
        addressField = createStyledTextField();
        nicField = createStyledTextField();

        // Add text fields to the fieldsPanel with labels
        addFieldToPanel(fieldsPanel, gbc, "Name:", fullNameField, 0);
        addFieldToPanel(fieldsPanel, gbc, "Username:", usernameField, 1);
        addFieldToPanel(fieldsPanel, gbc, "Password:", passwordField, 2);
        addFieldToPanel(fieldsPanel, gbc, "Age:", ageField, 3);
        addFieldToPanel(fieldsPanel, gbc, "Contact Number:", contactField, 4);
        addFieldToPanel(fieldsPanel, gbc, "Address:", addressField, 5);
        addFieldToPanel(fieldsPanel, gbc, "NIC:", nicField, 6);

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        updateButton = new JButton("Update Profile");
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        updateButton.setBackground(new Color(0, 102, 204));
        updateButton.setForeground(Color.WHITE);
        updateButton.setPreferredSize(new Dimension(200, 40));
        updateButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Add a hand cursor for better UX
        updateButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    updateProfile();
                }
                catch (SQLException ex)
                {
                    JOptionPane.showMessageDialog(null, "Error updating profile: " + ex.getMessage());
                }
            }
        });
        buttonPanel.add(updateButton);

        // Add fieldsPanel and buttonPanel to contentPanel
        contentPanel.add(fieldsPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the title panel, content panel, and navigation panel to the frame
        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(receptionistPanel, BorderLayout.WEST); // Navigation panel on the west
        add(contentPanel, BorderLayout.CENTER); // Content panel in the center

        setVisible(true);
    }

    private JTextField createStyledTextField()
    {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        textField.setBackground(new Color(245, 245, 245)); // Light grey background
        textField.setForeground(new Color(0, 102, 204));
        return textField;
    }

    private void addFieldToPanel(JPanel panel, GridBagConstraints gbc, String labelText, JTextField textField, int row)
    {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.2;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(new Color(0, 102, 204));
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        panel.add(textField, gbc);
    }

    private void loadProfileData() throws SQLException
    {
        ResultSet rs = receptionistDB.loadReceptionistProfile(receptionistId);
        if (rs.next()) {
            fullNameField.setText(rs.getString("name"));
            usernameField.setText(rs.getString("uname"));
            passwordField.setText(rs.getString("pass"));
            ageField.setText(String.valueOf(rs.getInt("age")));
            contactField.setText(String.valueOf(rs.getLong("telephone")));
            addressField.setText(rs.getString("address"));
            nicField.setText(rs.getString("nic"));
        }
    }

    private void updateProfile() throws SQLException
    {
        String name = fullNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        int age = Integer.parseInt(ageField.getText());
        int telephone = Integer.parseInt(contactField.getText());
        String address = addressField.getText();
        String nic = nicField.getText();

        receptionistDB.updateReceptionists(receptionistId, username, password, name, age, telephone, address, nic);
        JOptionPane.showMessageDialog(this, "Profile updated successfully!");
    }
}

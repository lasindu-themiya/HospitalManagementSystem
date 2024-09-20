package GUI;

import DBOperations.DocotrDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoctorProfileGui extends JFrame
{

    private DoctorPanelGui doctorPanel;
    private JTextField fullNameField;
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField contactField;
    private JTextField specializationField;
    private JTextField emailField;
    private JTextField rateField;
    private JButton updateButton;
    private String doctorId;
    private DocotrDB doctorDB;

    public DoctorProfileGui(LoginGui loginFrame, String doctorId) throws SQLException
    {
        this.doctorId = doctorId;
        this.doctorDB = new DocotrDB();
        initializeUI(loginFrame);
        loadProfileData();
    }

    private void initializeUI(LoginGui loginFrame)
    {
        setTitle("Doctor Profile");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize navigation panel
        doctorPanel = new DoctorPanelGui(this, doctorId, loginFrame);

        // Create title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(new Color(0, 102, 204));

        JLabel profileTitle = new JLabel("Doctor Profile", JLabel.CENTER);
        profileTitle.setFont(new Font("Segoe UI", Font.BOLD, 25));
        profileTitle.setForeground(Color.WHITE);
        profileTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        titlePanel.add(profileTitle, BorderLayout.CENTER);

        // Create content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create panel for text fields using GridBagLayout
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridBagLayout());
        fieldsPanel.setBackground(Color.WHITE);
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Initialize text fields
        fullNameField = createTextField();
        usernameField = createTextField();
        passwordField = createTextField();
        contactField = createTextField();
        specializationField = createTextField();
        emailField = createTextField();
        rateField = createTextField();

        // Add components to fieldsPanel
        gbc.gridx = 0; gbc.gridy = 0; fieldsPanel.add(createLabel("Full Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; fieldsPanel.add(fullNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; fieldsPanel.add(createLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; fieldsPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; fieldsPanel.add(createLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; fieldsPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; fieldsPanel.add(createLabel("Contact Number:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; fieldsPanel.add(contactField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; fieldsPanel.add(createLabel("Specialization:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; fieldsPanel.add(specializationField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; fieldsPanel.add(createLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; fieldsPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; fieldsPanel.add(createLabel("Rate:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6; fieldsPanel.add(rateField, gbc);

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        updateButton = new JButton("Update Profile");
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        updateButton.setBackground(new Color(0, 102, 204));
        updateButton.setForeground(Color.WHITE);
        updateButton.setPreferredSize(new Dimension(200, 40));
        updateButton.setFocusPainted(false); // Remove focus border around text
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        add(doctorPanel, BorderLayout.WEST); // Navigation panel on the west
        add(contentPanel, BorderLayout.CENTER); // Content panel in the center

        setVisible(true);
    }

    private JTextField createTextField()
    {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return textField;
    }

    private JLabel createLabel(String text)
    {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(0, 102, 204));
        return label;
    }

    private void loadProfileData() throws SQLException
    {
        ResultSet rs = doctorDB.loadDoctorProfile(doctorId);
        if (rs.next())
        {
            fullNameField.setText(rs.getString("fname") + " " + rs.getString("lname"));
            usernameField.setText(rs.getString("fname")); // Assuming username is stored in fname
            passwordField.setText(rs.getString("pass"));
            contactField.setText(rs.getString("contact"));
            specializationField.setText(rs.getString("specialization"));
            emailField.setText(rs.getString("email"));
            rateField.setText(String.valueOf(rs.getDouble("rate")));
        }
    }

    private void updateProfile() throws SQLException
    {
        String[] names = fullNameField.getText().split(" ", 2);
        String fname = names[0];
        String lname = names.length > 1 ? names[1] : "";
        String password = passwordField.getText();
        int contact = Integer.parseInt(contactField.getText());
        String specialization = specializationField.getText();
        String email = emailField.getText();
        double rate = Double.parseDouble(rateField.getText());

        doctorDB.updateDoctor(fname, lname, password, contact, specialization, email, rate, doctorId);
        JOptionPane.showMessageDialog(this, "Profile updated successfully!");
    }
}

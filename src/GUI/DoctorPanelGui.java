package GUI;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class DoctorPanelGui extends JPanel
{

    private String doctorId;
    private JFrame parentFrame;
    private JFrame profileFrame;
    private JFrame appointmentsFrame;
    private JFrame paymentsFrame;

    private LoginGui loginFrame;

    private JButton profileButton;
    private JButton appointmentsButton;
    private JButton paymentsButton;
    private JButton logoutButton;

    public DoctorPanelGui(JFrame parentFrame, String doctorId, LoginGui loginFrame)
    {
        this.parentFrame = parentFrame;
        this.doctorId = doctorId;
        this.loginFrame = loginFrame;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, getHeight()));
        setBackground(new Color(0, 102, 204)); // Background color

        initializeButtons();
        addEventListeners();
        addComponents();
    }

    private void initializeButtons()
    {
        profileButton = createNavButton("Profile");
        appointmentsButton = createNavButton("View Appointments");
        paymentsButton = createNavButton("View Payments");
        logoutButton = createNavButton("Logout");
    }

    private void addEventListeners()
    {
        profileButton.addActionListener(e ->
        {
            try
            {
                showProfile();
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(this, "Error loading profile", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        appointmentsButton.addActionListener(e -> showAppointments());
        paymentsButton.addActionListener(e -> showPayments());
        logoutButton.addActionListener(e -> handleLogout());
    }

    private void addComponents()
    {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(0, 102, 204)); // Background color

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        buttonPanel.add(profileButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(appointmentsButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(paymentsButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(Box.createVerticalGlue()); // Push logoutButton to the bottom
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createNavButton(String text)
    {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(0, 102, 204));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
        button.setMaximumSize(new Dimension(200, 40));
        return button;
    }

    private void showProfile() throws SQLException
    {
        if (profileFrame == null)
        {
            profileFrame = new DoctorProfileGui(loginFrame, doctorId);
        }
        parentFrame.setVisible(false);
        profileFrame.setVisible(true);
    }

    private void showAppointments()
    {
        if (appointmentsFrame == null)
        {
            appointmentsFrame = new ViewDoctorAppointmentsGui(loginFrame, doctorId);
        }
        parentFrame.setVisible(false);
        appointmentsFrame.setVisible(true);
    }

    private void showPayments()
    {
        if (paymentsFrame == null)
        {
            paymentsFrame = new ViewDoctorPaymentsGui(loginFrame, doctorId);
        }
        parentFrame.setVisible(false);
        paymentsFrame.setVisible(true);
    }

    private void handleLogout()
    {
        if (profileFrame != null)
        {
            profileFrame.dispose();
        }
        if (appointmentsFrame != null)
        {
            appointmentsFrame.dispose();
        }
        if (paymentsFrame != null)
        {
            paymentsFrame.dispose();
        }

        parentFrame.dispose();
        loginFrame.setVisible(true);
        loginFrame.toFront();
    }
}

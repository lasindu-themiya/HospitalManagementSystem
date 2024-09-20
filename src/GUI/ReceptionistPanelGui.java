package GUI;

import DBOperations.ReceptionistDB;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ReceptionistPanelGui extends JPanel
{

    private String receptionistId;
    private JFrame receptionistFrame;
    private JFrame profileFrame;
    private JFrame viewClinicsFrame;
    private JFrame makeAppointmentsFrame;
    private JFrame viewAppointmentsFrame;
    private JFrame viewPaymentsFrame;

    private JFrame viewDashboard;
    private LoginGui loginFrame;
    private ReceptionistDB receptionistDB;

    private JButton dashboardBtn;
    private JButton profileBtn;
    private JButton viewClinicsBtn;
    private JButton makeAppointmentsBtn;
    private JButton viewAppointmentsBtn;
    private JButton viewPaymentsBtn;
    private JButton logoutBtn;

    public ReceptionistPanelGui(JFrame receptionistFrame, LoginGui loginFrame, String receptionistId) throws SQLException
    {
        this.receptionistFrame = receptionistFrame;
        this.loginFrame = loginFrame;
        this.receptionistId = receptionistId;
        receptionistDB = new ReceptionistDB();

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, getHeight()));
        setBackground(new Color(0, 102, 204));

        initializeButtons();
        addEventListeners();
        addComponents();
    }

    private void initializeButtons()
    {
        dashboardBtn = createNavButton("Dashboard");
        profileBtn = createNavButton("Profile");
        viewClinicsBtn = createNavButton("Available Clinics");
        makeAppointmentsBtn = createNavButton("Make Appointments");
        viewAppointmentsBtn = createNavButton("View Appointments");
        viewPaymentsBtn = createNavButton("View Payments");
        logoutBtn = createNavButton("Logout");
    }

    private void addEventListeners()
    {
        dashboardBtn.addActionListener(e ->
        {
            try
            {
                showDashboard();
            }
            catch (SQLException ex)
            {
                throw new RuntimeException(ex);
            }
        });
        profileBtn.addActionListener(e ->
        {
            try
            {
                showProfile();
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading profile", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        viewClinicsBtn.addActionListener(e -> showAvailableClinics());
        makeAppointmentsBtn.addActionListener(e -> showMakeAppointments());
        viewAppointmentsBtn.addActionListener(e -> showViewAppointments());
        viewPaymentsBtn.addActionListener(e -> showViewPayments());
        logoutBtn.addActionListener(e -> handleLogout());
    }

    private void addComponents()
    {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(0, 102, 204)); // Matching background color

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        buttonPanel.add(dashboardBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(profileBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(viewClinicsBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(makeAppointmentsBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(viewAppointmentsBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(viewPaymentsBtn);

        add(buttonPanel, BorderLayout.CENTER);
        add(logoutBtn, BorderLayout.SOUTH);
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

    private void showDashboard() throws SQLException
    {
        if (viewDashboard == null)
        {
            viewDashboard = new ReceptionistDashboardGui(loginFrame, receptionistId);
        }
        receptionistFrame.setVisible(false);
        viewDashboard.setVisible(true);
    }


    private void showProfile() throws SQLException
    {
        if (profileFrame == null)
        {
            profileFrame = new ProfileGui(loginFrame, receptionistId);
        }
        receptionistFrame.setVisible(false);
        profileFrame.setVisible(true);
    }

    private void showAvailableClinics()
    {
        if (viewClinicsFrame == null)
        {
            viewClinicsFrame = new ViewClinicsGui(loginFrame,receptionistId);
        }
        receptionistFrame.setVisible(false);
        viewClinicsFrame.setVisible(true);
    }

    private void showMakeAppointments()
    {
        if (makeAppointmentsFrame == null)
        {
            makeAppointmentsFrame = new MakeAppointmentsGui(loginFrame,receptionistId);
        }
        receptionistFrame.setVisible(false);
        makeAppointmentsFrame.setVisible(true);
    }

    private void showViewAppointments()
    {
        if (viewAppointmentsFrame == null)
        {
            viewAppointmentsFrame = new ViewAppointmentsGui(loginFrame,receptionistId);
        }
        receptionistFrame.setVisible(false);
        viewAppointmentsFrame.setVisible(true);
    }

    private void showViewPayments()
    {
        if (viewPaymentsFrame == null)
        {
            viewPaymentsFrame = new ViewPaymentsGui(loginFrame,receptionistId);
        }
        receptionistFrame.setVisible(false);
        viewPaymentsFrame.setVisible(true);
    }

    private void handleLogout()
    {
        if (receptionistFrame != null)
        {
            receptionistFrame.dispose();
        }
        if (profileFrame != null)
        {
            profileFrame.dispose();
        }
        if (viewClinicsFrame != null)
        {
            viewClinicsFrame.dispose();
        }
        if (makeAppointmentsFrame != null)
        {
            makeAppointmentsFrame.dispose();
        }
        if (viewAppointmentsFrame != null)
        {
            viewAppointmentsFrame.dispose();
        }
        if (viewPaymentsFrame != null)
        {
            viewPaymentsFrame.dispose();
        }

        loginFrame.setVisible(true);
        loginFrame.toFront();
    }
}

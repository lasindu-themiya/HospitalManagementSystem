package GUI;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AdminPanelGui extends JPanel
{

    private JFrame adminFrame;
    private JFrame admindashboardFrame;
    private JFrame addAdminsFrame;
    private JFrame addDoctorFrame;
    private JFrame addReceptionistFrame;
    private JFrame addClinicsFrame;
    private LoginGui loginFrame;

    private JButton adminDashboardBtn;
    private JButton adminBtn;
    private JButton receptionistBtn;
    private JButton doctorBtn;
    private JButton clinicBtn;
    private JButton logoutBtn;
    private JPanel buttonPanel;

    public AdminPanelGui(JFrame adminFrame, LoginGui loginFrame)
    {
        this.adminFrame = adminFrame;
        this.loginFrame = loginFrame;
        InitializeUI();
    }

    private void InitializeUI()
    {
        setLayout(new BorderLayout());
        setBackground(new Color(0, 102, 204));

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(0, 102, 204));

        adminDashboardBtn = createNavButton("Dashboard");
        adminDashboardBtn.addActionListener(evt ->
        {
            try
            {
                showAdminDashboardFrame();
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        });
        adminBtn = createNavButton("Admins");
        adminBtn.addActionListener(evt ->
        {
            try
            {
                showAddAdminsFrame();
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        });
        receptionistBtn = createNavButton("Receptionists");
        receptionistBtn.addActionListener(evt ->
        {
            try
            {
                showAddReceptionistFrame();
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        });
        doctorBtn = createNavButton("Doctors");
        doctorBtn.addActionListener(evt ->
        {
            try
            {
                showAddDoctorFrame();
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        });
        clinicBtn = createNavButton("Clinics");
        clinicBtn.addActionListener(evt ->
        {
            try
            {
                showAddClinicsFrame();
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        });

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        buttonPanel.add(adminDashboardBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(adminBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(receptionistBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(doctorBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(clinicBtn);

        add(buttonPanel, BorderLayout.WEST);

        logoutBtn = createNavButton("Logout");
        logoutBtn.addActionListener(evt -> handleLogout());
        add(logoutBtn, BorderLayout.SOUTH);

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private JButton createNavButton(String text)
    {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(0, 102, 204));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setMaximumSize(new Dimension(150, 40));
        return button;
    }

    private void showAdminDashboardFrame() throws SQLException
    {
        if (admindashboardFrame == null)
        {
            admindashboardFrame = new AdminGui(loginFrame);
        }
        adminFrame.setVisible(false);
        admindashboardFrame.setVisible(true);
    }

    private void showAddAdminsFrame() throws SQLException
    {
        if (addAdminsFrame == null)
        {
            addAdminsFrame = new AddAdminsGui(loginFrame);
        }
        adminFrame.setVisible(false);
        addAdminsFrame.setVisible(true);
    }

    private void showAddReceptionistFrame() throws SQLException
    {
        if (addReceptionistFrame == null)
        {
            addReceptionistFrame = new AddReceptionistGui(loginFrame);
        }
        adminFrame.setVisible(false);
        addReceptionistFrame.setVisible(true);
    }

    private void showAddDoctorFrame() throws SQLException
    {
        if (addDoctorFrame == null)
        {
            addDoctorFrame = new AddDoctorGui(loginFrame);
        }
        adminFrame.setVisible(false);
        addDoctorFrame.setVisible(true);
    }

    private void showAddClinicsFrame() throws SQLException
    {
        if (addClinicsFrame == null)
        {
            addClinicsFrame = new AddClinicsGui(loginFrame);
        }
        adminFrame.setVisible(false);
        addClinicsFrame.setVisible(true);
    }

    private void handleLogout()
    {
        // Hide all frames associated with the admin panel
        if (adminFrame != null)
        {
            adminFrame.setVisible(false);
        }
        if (addAdminsFrame != null)
        {
            addAdminsFrame.setVisible(false);
        }
        if (addDoctorFrame != null)
        {
            addDoctorFrame.setVisible(false);
        }
        if (addReceptionistFrame != null)
        {
            addReceptionistFrame.setVisible(false);
        }
        if (addClinicsFrame != null)
        {
            addClinicsFrame.setVisible(false);
        }

        // Show the login frame and bring it to the front
        if (loginFrame != null)
        {
            loginFrame.setVisible(true);
            loginFrame.toFront(); // Bring the login frame to the front
        }
    }

}

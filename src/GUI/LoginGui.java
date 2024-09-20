package GUI;

import DBOperations.AdminDB;
import DBOperations.DocotrDB;
import DBOperations.ReceptionistDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.sql.SQLException;

public class LoginGui extends JFrame
{

    private JButton loginBtn;
    private JLabel lblUsername;
    private JLabel lblPassword;
    private JPasswordField passwordTxt;
    private JTextField userNameTxt;
    private JLabel profilePic;
    private JPanel bgPanel;

    public LoginGui()
    {
        InitializeUI();
    }

    private void InitializeUI()
    {
        setSize(800, 900);
        setTitle("Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLocation(500,130);
        setBackground(Color.WHITE);

        bgPanel = new JPanel();
        bgPanel.setBackground(Color.WHITE);
        bgPanel.setLayout(new GridBagLayout());

        // Create GridBagConstraints for component placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Profile Picture with rounded image
        ImageIcon originalIcon = new ImageIcon("G:\\films\\OneDrive - National Institute of Business Management\\Desktop\\profile.png");
        Image image = originalIcon.getImage();
        Image roundedImage = makeRoundedImage(image);
        profilePic = new JLabel(new ImageIcon(roundedImage));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        bgPanel.add(profilePic, gbc);

        // Username Label
        lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        bgPanel.add(lblUsername, gbc);


        // Username TextField
        userNameTxt = new JTextField(20);
        userNameTxt.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 255), 1));
        userNameTxt.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    passwordTxt.requestFocus();
                }
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        bgPanel.add(userNameTxt, gbc);

        // Password Label
        lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        bgPanel.add(lblPassword, gbc);

        // Password TextField
        passwordTxt = new JPasswordField(20);
        passwordTxt.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 255), 1));
        passwordTxt.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    loginBtn.doClick(); // Simulate login button click
                }
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        bgPanel.add(passwordTxt, gbc);



        // Login Button
        loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginBtn.setBackground(new Color(0, 153, 255));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        bgPanel.add(loginBtn, gbc);
        loginBtn.addActionListener(evt -> handleLogin());

        add(bgPanel);
        pack();
        setVisible(true);
    }


    private void handleLogin()
    {
        String username = userNameTxt.getText();
        String password = new String(passwordTxt.getPassword());

        try
        {
            AdminDB adminDB = new AdminDB();
            ReceptionistDB receptionistDB = new ReceptionistDB();
            DocotrDB doctorDB = new DocotrDB(); // Corrected name

            if (adminDB.validateLogin(username, password))
            {
                new AdminGui(this); // Open AdminGui
                dispose(); // Close LoginGui
            }
            else if (receptionistDB.validateLogin(username, password))
            {
                String receptionistId = receptionistDB.getReceptionistId(username);
                new ReceptionistDashboardGui(this, receptionistId); // Pass ID to ReceptionistDashboardGui
                dispose();
            }
            else if (doctorDB.validateLogin(username, password))
            {
                String doctorId = doctorDB.getDoctorId(username);
                new DoctorProfileGui(this, doctorId); // Pass ID to DoctorPanelGui
                dispose();
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                userNameTxt.setFocusable(true);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while trying to log in", "Error", JOptionPane.ERROR_MESSAGE);
            userNameTxt.setFocusable(true);
        }
        finally
        {
            ClearInput();
            userNameTxt.setFocusable(true);
        }
    }


    private void ClearInput()
    {
        userNameTxt.setText("");
        passwordTxt.setText("");
    }

    private Image makeRoundedImage(Image image)
    {
        int diameter = Math.min(image.getWidth(null), image.getHeight(null));
        BufferedImage mask = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = mask.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fill(new Ellipse2D.Double(0, 0, diameter, diameter));
        g2d.dispose();

        BufferedImage masked = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        g2d = masked.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(image, 0, 0, diameter, diameter, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
        g2d.drawImage(mask, 0, 0, diameter, diameter, null);
        g2d.dispose();

        return masked;
    }

    public static void main(String[] args) {
        new LoginGui();
    }
}

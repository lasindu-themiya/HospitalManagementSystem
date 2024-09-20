package GUI;

import DBOperations.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MakeAppointmentsGui extends JFrame
{

    private ReceptionistPanelGui receptionistPanel;
    private PatientDB patientDB;
    private AppointmentDB appointmentDB;
    private ClinicDB clinicDB;
    private DocotrDB doctorDB;
    private PaymentDB paymentDB;

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField cityField;
    private JTextField nicField;
    private JTextField emailField;
    private JTextField ageField;
    private JTextField contactField;
    private JComboBox<String> clinicComboBox;
    private JButton submitButton;

    private String receptionistId;

    public MakeAppointmentsGui(LoginGui loginFrame, String receptionistId)
    {
        try
        {
            this.receptionistId = receptionistId;
            patientDB = new PatientDB();
            appointmentDB = new AppointmentDB();
            clinicDB = new ClinicDB();
            doctorDB = new DocotrDB();
            paymentDB = new PaymentDB();
            initializeUi(loginFrame);
            loadClinicData();
            clinicComboBox.setSelectedIndex(-1);
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(this, "Error connecting to the database: " + e.getMessage());
        }
    }

    private void initializeUi(LoginGui loginFrame) throws SQLException
    {
        setTitle("Make Appointment");
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

        JLabel titleLabel = new JLabel("Make an Appointment", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 25)); // Larger title font for emphasis
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add more spacing around title
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        titleLabel.setForeground(new Color(0, 102, 204)); // Blue color for the title text

        // Create content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create panel for form fields
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(8, 2, 15, 15)); // Increased spacing between fields
        fieldsPanel.setBackground(Color.WHITE);
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // More padding around fields panel

        // Initialize text fields
        firstNameField = createTextField();
        lastNameField = createTextField();
        cityField = createTextField();
        nicField = createTextField();
        emailField = createTextField();
        ageField = createTextField();
        contactField = createTextField();
        clinicComboBox = new JComboBox<>();

        // Add text fields to the fieldsPanel
        fieldsPanel.add(createLabel("First Name:"));
        fieldsPanel.add(firstNameField);
        fieldsPanel.add(createLabel("Last Name:"));
        fieldsPanel.add(lastNameField);
        fieldsPanel.add(createLabel("City:"));
        fieldsPanel.add(cityField);
        fieldsPanel.add(createLabel("NIC:"));
        fieldsPanel.add(nicField);
        fieldsPanel.add(createLabel("Email:"));
        fieldsPanel.add(emailField);
        fieldsPanel.add(createLabel("Age:"));
        fieldsPanel.add(ageField);
        fieldsPanel.add(createLabel("Contact No:"));
        fieldsPanel.add(contactField);
        fieldsPanel.add(createLabel("Select Clinic:"));
        fieldsPanel.add(clinicComboBox);

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Larger font for the button
        submitButton.setBackground(new Color(0, 102, 204)); // Blue background color for the button
        submitButton.setForeground(Color.WHITE); // White text color
        submitButton.setFocusPainted(false);
        submitButton.setPreferredSize(new Dimension(220, 45)); // Larger button size
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        submitButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    submitAppointment();
                }
                catch (SQLException ex)
                {
                    JOptionPane.showMessageDialog(null, "Error submitting appointment: " + ex.getMessage());
                }
            }
        });
        buttonPanel.add(submitButton);

        // Add fieldsPanel and buttonPanel to contentPanel
        contentPanel.add(fieldsPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set layout and add components to the frame
        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(receptionistPanel, BorderLayout.WEST); // Navigation panel on the west
        add(contentPanel, BorderLayout.CENTER); // Content panel in the center

        setVisible(true);
    }

    private JTextField createTextField()
    {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 18)); // Slightly larger font for better readability
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204), 1), // Blue border
                BorderFactory.createEmptyBorder(5, 5, 5, 5))); // Add padding inside the text field
        return textField;
    }

    private JLabel createLabel(String text)
    {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Bold font for labels
        label.setForeground(new Color(0, 102, 204)); // Blue color for the label text
        return label;
    }

    private void loadClinicData()
    {
        try
        {
            ResultSet rs = clinicDB.viewUpcomingClinics();
            while (rs.next())
            {
                clinicComboBox.addItem(rs.getString("Cid"));
            }
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(this, "Error loading clinic data: " + e.getMessage());
        }
    }

    private void submitAppointment() throws SQLException
    {
        try
        {
            String patientId = patientDB.generateNextID();
            String appointmentId = appointmentDB.generateNextID();

            // Retrieve input data
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String city = cityField.getText();
            String nic = nicField.getText();
            String email = emailField.getText();
            int age = Integer.parseInt(ageField.getText());
            String contact = contactField.getText();
            String clinicId = (String) clinicComboBox.getSelectedItem();
            Date appointmentDate = new Date(); // Set to current date for demonstration
            java.sql.Date sqlDate = new java.sql.Date(appointmentDate.getTime());

            if(clinicId == null)
            {
                JOptionPane.showMessageDialog(this,"Please Select a Clinic First");
            }
            else {
                // Add patient and appointment to the database
                patientDB.addPatient(patientId, firstName, lastName, city, nic, email, age, contact);
                appointmentDB.addAppointment(appointmentId, patientId, clinicId, sqlDate);

                // Get the doctor ID and rate from the selected clinic
                String doctorId = doctorDB.getDoctorIdByClinic(clinicId);
                double doctorRate = doctorDB.getDoctorRate(doctorId);

                // Calculate the total payment amount
                double hospitalCharge = 1000.0;
                double totalAmount = doctorRate + hospitalCharge;

                // Add payment record
                paymentDB.addPayment(appointmentId, totalAmount, sqlDate);

                // Decrease the patient limit of the clinic
                clinicDB.decreasePatientLimit(clinicId);

                // Generate PDF with payment details
                generatePdf(firstName, lastName, doctorRate, hospitalCharge, totalAmount);

                // Clear the form fields
                clearFields();

                JOptionPane.showMessageDialog(this, "Appointment made successfully!");
            }
        }
        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Check if Age and Mobile number is Numeric");
        }
    }

    private void generatePdf(String firstName, String lastName, double doctorRate, double hospitalCharge, double totalAmount)
    {
        Document document = new Document();
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = sdf.format(new Date());

            // Create a new PDF file with the patient's name
            String fileName = "D:\\Appointments\\Appointment Details for " + firstName + " " + lastName +" On " +dateString+ ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileName));

            document.open();
            document.add(new Paragraph("Appointment Details"));
            document.add(new Paragraph("Patient Name: " + firstName + " " + lastName));
            document.add(new Paragraph("Doctor Charge: " + doctorRate));
            document.add(new Paragraph("Hospital Charge: " + hospitalCharge));
            document.add(new Paragraph("Total Amount: " + totalAmount));
            document.add(new Paragraph("Appointment Date: " + dateString));
            document.close();

        }
        catch (DocumentException | IOException e)
        {
            JOptionPane.showMessageDialog(this, "Error generating PDF: " + e.getMessage());
        }
    }

    private void clearFields()
    {
        firstNameField.setText("");
        lastNameField.setText("");
        cityField.setText("");
        nicField.setText("");
        emailField.setText("");
        ageField.setText("");
        contactField.setText("");
        clinicComboBox.setSelectedIndex(-1);
    }
}

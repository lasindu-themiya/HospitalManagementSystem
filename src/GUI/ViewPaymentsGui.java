package GUI;

import DBOperations.PaymentDB;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewPaymentsGui extends JFrame
{

    private JTable paymentTable;
    private PaymentDB paymentDB;
    private ReceptionistPanelGui receptionistPanel;

    public ViewPaymentsGui(LoginGui loginFrame, String receptionistId)
    {
        try {
            setTitle("View Payments");
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setSize(1000,700);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            paymentDB = new PaymentDB();
            ResultSet rs = paymentDB.getDoctorPaymentsForToday();
            PaymentTableModel model = new PaymentTableModel(rs);
            paymentTable = new JTable(model);

            JTableHeader tableHeader = paymentTable.getTableHeader();
            tableHeader.setBackground(new Color(0, 102, 204));
            tableHeader.setForeground(Color.white);
            tableHeader.setFont(new Font("Arial",Font.BOLD,15));


            paymentTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
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

            paymentTable.setRowHeight(30);
            // Initialize navigation panel
            receptionistPanel = new ReceptionistPanelGui(this, loginFrame, receptionistId);

            // Create title panel
            JPanel titlePanel = new JPanel();
            titlePanel.setLayout(new BorderLayout());
            titlePanel.setBackground(Color.WHITE);

            JLabel titleLabel = new JLabel("Today's Payments", JLabel.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 25));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            titlePanel.add(titleLabel, BorderLayout.CENTER);

            titleLabel.setForeground(new Color(0,102,204));

            // Set up the layout and add components
            setLayout(new BorderLayout());
            add(titlePanel, BorderLayout.NORTH);
            add(receptionistPanel, BorderLayout.WEST);
            add(new JScrollPane(paymentTable), BorderLayout.CENTER);


            setLocationRelativeTo(null);
            setVisible(true);

        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(this, "Error connecting to the database: " + e.getMessage());
        }
    }
}

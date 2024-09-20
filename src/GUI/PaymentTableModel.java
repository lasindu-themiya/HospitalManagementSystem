package GUI;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentTableModel extends AbstractTableModel
{

    private ResultSet resultSet;
    private int rowCount;
    private int columnCount;

    private final String[] columnNames = {
            "Doctor Id",
            "Doctor First Name",
            "Doctor Last Name",
            "Appointments Count",
            "Doctor Earnings",
            "Hospital Earnings"
    };

    public PaymentTableModel(ResultSet resultSet) throws SQLException
    {
        this.resultSet = resultSet;
        resultSet.last();
        rowCount = resultSet.getRow();
        columnCount = columnNames.length;
        resultSet.beforeFirst();
    }

    @Override
    public int getRowCount()
    {
        return rowCount;
    }

    @Override
    public int getColumnCount()
    {
        return columnCount;
    }

    @Override
    public String getColumnName(int columnIndex)
    {
        return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        try
        {
            resultSet.absolute(rowIndex + 1);
            switch (columnIndex)
            {
                case 0:
                    return resultSet.getString("Did");
                case 1:
                    return resultSet.getString("fname");
                case 2:
                    return resultSet.getString("lname");
                case 3:
                    return resultSet.getInt("appointment_count");
                case 4:
                    return resultSet.getDouble("doctor_earnings");
                case 5:
                    return resultSet.getDouble("hospital_earnings_per_doctor");
                default:
                    return null;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}


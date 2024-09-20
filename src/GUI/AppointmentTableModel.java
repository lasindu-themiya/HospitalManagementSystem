package GUI;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentTableModel extends AbstractTableModel
{

    private ResultSet resultSet;
    private int rowCount;
    private int columnCount;

    private final String[] columnNames = {
            "Appointment Id",
            "Patient First Name",
            "Patient Last Name",
            "Date",
            "Doctor First Name",
            "Doctor Last Name"
    };

    public AppointmentTableModel(ResultSet resultSet) throws SQLException
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
                    return resultSet.getString("AiD");
                case 1:
                    return resultSet.getString("First_Name");
                case 2:
                    return resultSet.getString("Last_Name");
                case 3:
                    return resultSet.getDate("Date");
                case 4:
                    return resultSet.getString("DoctorFirstName");
                case 5:
                    return resultSet.getString("DoctorLastName");
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

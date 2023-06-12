package org.example;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserGUI extends JFrame {
    private final UserDAO userDAO;
    private final JTable table;
    private final DefaultTableModel tableModel;


    public UserGUI()
    {
        super("Tableau des utilisateurs (triple clic pour trier)");
        userDAO = new UserDAO();
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Lastname", "Email", "Age", "Job"}, 0) {
            /*CODE POUR FORCER LES VALEURS ID ET AGE EN INT PARCE QUE LE CODE LES
            PRENDS POUR DES STRINGS ET JE NE SAIS PAS POURQUOI*/

            /*UPDATE: DefaultTableModel définit toutes les variables en String juste au-dessus.
            * on va toucher a rien pour éviter de tout casser.*/
            @Override
            public Class<?> getColumnClass(int columnIndex)
            {
                if (columnIndex == 0)
                {
                    return Integer.class;
                }
                else if (columnIndex == 4)
                {
                    return Integer.class;
                }
                else
                {
                    return super.getColumnClass(columnIndex);
                }
            }
        };
        table = new JTable(tableModel);
        try
        {
            ResultSet rs = userDAO.getAllUsers();
            while (rs.next())
            {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String lastname = rs.getString("lastname");
                String email = rs.getString("email");
                int age = rs.getInt("age");
                String job = rs.getString("job");
                //
                Object[] rowData = new Object[] {id, name, lastname, email, age, job};
                //
                tableModel.addRow(rowData);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        JScrollPane scrollPane = new JScrollPane(table);

        /*Sorting algorithm*/

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        // Mouse listener for ID column header
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1)
                {
                    int columnIndex = table.columnAtPoint(e.getPoint());
                    sorter.toggleSortOrder(columnIndex);
                }
            }
        });

        /*DELETE BUTTON*/

        JButton deleteButton = new JButton("Delete User");
        deleteButton.addActionListener(e ->
        {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0)
            {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                try
                {
                    userDAO.deleteUser(id);
                    tableModel.removeRow(selectedRow);
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
        });

        /*CREATE BUTTON*/

        JButton createButton = new JButton("Create User");
        createButton.addActionListener(e ->
        {
            String name = JOptionPane.showInputDialog(this, "Enter name:");
            String lastname = JOptionPane.showInputDialog(this, "Enter lastname:");
            String email = JOptionPane.showInputDialog(this, "Enter email:");
            int age = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter age:"));
            String job = JOptionPane.showInputDialog(this, "Enter job:");
            User user = new User(0, name, lastname, email, age, job);

            try
            {
                userDAO.createUser(user);
                Object[] rowData = new Object[]{user.getId(), name, lastname, email, age, job};
                // LE BUG EST ICI PATCHE LE
                tableModel.addRow(rowData);
                tableModel.fireTableDataChanged();
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        });


            /*OLD METHOD FOR DATA SORT. KEEP IN CASE OF EMERGENCY
            try
            {
                userDAO.createUser(user);
                ResultSet rs = userDAO.getAllUsers();
                rs.last();
                int id = rs.getInt("id");
                Object[] rowData = new Object[]{id, name, lastname, email, age, job};
                tableModel.addRow(rowData);
                int lastIndex = tableModel.getRowCount() - 1;
                Rectangle rect = table.getCellRect(lastIndex, 0, true);
                table.scrollRectToVisible(rect);
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        });*/


        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(deleteButton);
        buttonPanel.add(createButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args)
    {
        new UserGUI();
    }
}

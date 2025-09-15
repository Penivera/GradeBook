package org.gradebook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;

/**
 * Panel for managing students: view, add, delete, and view grades.
 */
class StudentPanel extends JPanel {
    private GradebookFrame parentFrame;
    private DatabaseManager dbManager;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JButton addButton, viewGradesButton, deleteButton;

    public StudentPanel(GradebookFrame parentFrame, DatabaseManager dbManager) {
        this.parentFrame = parentFrame;
        this.dbManager = dbManager;
        setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[]{"ID", "First Name", "Last Name"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Student");
        viewGradesButton = new JButton("View Grades");
        deleteButton = new JButton("Delete Student");
        viewGradesButton.setEnabled(false);
        deleteButton.setEnabled(false);
        buttonPanel.add(addButton);
        buttonPanel.add(viewGradesButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Selection listener
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = studentTable.getSelectedRow() != -1;
            viewGradesButton.setEnabled(selected);
            deleteButton.setEnabled(selected);
        });

        // Add Student
        addButton.addActionListener(e -> {
            AddStudentDialog dialog = new AddStudentDialog(parentFrame, dbManager, this);
            dialog.setVisible(true);
        });

        // View Grades
        viewGradesButton.addActionListener(e -> {
            int row = studentTable.getSelectedRow();
            if (row != -1) {
                int studentId = (int) tableModel.getValueAt(row, 0);
                String name = tableModel.getValueAt(row, 1) + " " + tableModel.getValueAt(row, 2);
                parentFrame.showGradePanel(studentId, name);
            }
        });

        // Delete Student
        deleteButton.addActionListener(e -> {
            int row = studentTable.getSelectedRow();
            if (row != -1) {
                int studentId = (int) tableModel.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Delete selected student?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dbManager.deleteStudent(studentId);
                    refreshTable();
                }
            }
        });

        refreshTable();
    }

    /** Refreshes the student table from the database. */
    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Map<String, Object>> students = dbManager.getAllStudents();
        for (Map<String, Object> s : students) {
            tableModel.addRow(new Object[]{s.get("student_id"), s.get("first_name"), s.get("last_name")});
        }
    }
}

/**
 * Dialog for adding a new student.
 */
class AddStudentDialog extends JDialog {
    public AddStudentDialog(JFrame parent, DatabaseManager dbManager, StudentPanel studentPanel) {
        super(parent, "Add Student", true);
        setLayout(new GridLayout(3, 2, 10, 10));
        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstNameField = new JTextField();
        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastNameField = new JTextField();
        JButton saveButton = new JButton("Save");
        add(firstNameLabel); add(firstNameField);
        add(lastNameLabel); add(lastNameField);
        add(new JLabel()); add(saveButton);
        saveButton.addActionListener(e -> {
            String first = firstNameField.getText().trim();
            String last = lastNameField.getText().trim();
            if (!first.isEmpty() && !last.isEmpty()) {
                dbManager.addStudent(first, last);
                studentPanel.refreshTable();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please enter both first and last names.");
            }
        });
        setSize(300, 150);
        setLocationRelativeTo(parent);
    }
}

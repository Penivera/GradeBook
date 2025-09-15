package org.gradebook;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Panel for managing grades of a selected student.
 */
class GradePanel extends JPanel {
    private GradebookFrame parentFrame;
    private DatabaseManager dbManager;
    private int studentId;
    private String studentName;
    private JLabel nameLabel;
    private JLabel gpaLabel;
    private JTable gradeTable;
    private DefaultTableModel tableModel;
    private JButton addGradeButton, backButton;

    public GradePanel(GradebookFrame parentFrame, DatabaseManager dbManager) {
        this.parentFrame = parentFrame;
        this.dbManager = dbManager;
        setLayout(new BorderLayout());
        nameLabel = new JLabel("Grades for Student", SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gpaLabel = new JLabel("GPA/CGPA: 0.00", SwingConstants.CENTER);
        gpaLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(nameLabel);
        topPanel.add(gpaLabel);
        add(topPanel, BorderLayout.NORTH);
        tableModel = new DefaultTableModel(new Object[]{"Course", "Score", "Grade", "Credit Unit"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        gradeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(gradeTable);
        add(scrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        addGradeButton = new JButton("Add Grade");
        backButton = new JButton("Back");
        buttonPanel.add(addGradeButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add Grade
        addGradeButton.addActionListener(e -> {
            AddGradeDialog dialog = new AddGradeDialog(parentFrame, dbManager, this, studentId);
            dialog.setVisible(true);
        });

        // Back
        backButton.addActionListener(e -> parentFrame.showStudentPanel());
    }

    /**
     * Sets the student to view grades for and refreshes the table.
     */
    public void setStudent(int studentId, String studentName) {
    this.studentId = studentId;
    this.studentName = studentName;
    nameLabel.setText("Grades for " + studentName);
    refreshTable();
    double gpa = dbManager.calculateGPA(studentId);
    gpaLabel.setText(String.format("GPA/CGPA: %.2f", gpa));
    }

    /** Refreshes the grade table from the database. */
    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Map<String, Object>> grades = dbManager.getGradesForStudent(studentId);
        for (Map<String, Object> g : grades) {
            tableModel.addRow(new Object[]{g.get("course_name"), g.get("score"), g.get("grade_letter"), g.get("credit_unit")});
        }
    }
}

/**
 * Dialog for adding a new grade for a student.
 */
class AddGradeDialog extends JDialog {
    public AddGradeDialog(JFrame parent, DatabaseManager dbManager, GradePanel gradePanel, int studentId) {
        super(parent, "Add Grade", true);
        setLayout(new GridLayout(4, 2, 10, 10));
        JLabel courseLabel = new JLabel("Course:");
        JComboBox<String> courseCombo = new JComboBox<>();
        List<Map<String, Object>> courses = dbManager.getAllCourses();
        for (Map<String, Object> c : courses) {
            courseCombo.addItem((String) c.get("course_name"));
        }
        JLabel scoreLabel = new JLabel("Score:");
        JTextField scoreField = new JTextField();
        JLabel creditLabel = new JLabel("Credit Unit:");
        JTextField creditField = new JTextField();
        JButton saveButton = new JButton("Save");
        add(courseLabel); add(courseCombo);
        add(scoreLabel); add(scoreField);
        add(creditLabel); add(creditField);
        add(new JLabel()); add(saveButton);
        saveButton.addActionListener(e -> {
            String courseName = (String) courseCombo.getSelectedItem();
            String score = scoreField.getText().trim();
            String creditStr = creditField.getText().trim();
            if (courseName != null && !score.isEmpty() && !creditStr.isEmpty()) {
                int courseId = -1;
                for (Map<String, Object> c : courses) {
                    if (courseName.equals(c.get("course_name"))) {
                        courseId = (int) c.get("course_id");
                        break;
                    }
                }
                int creditUnit = 0;
                try {
                    creditUnit = Integer.parseInt(creditStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Credit unit must be a number.");
                    return;
                }
                if (courseId != -1 && creditUnit > 0) {
                    dbManager.addGrade(studentId, courseId, score, creditUnit);
                    gradePanel.refreshTable();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter valid data.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
            }
        });
        setSize(350, 200);
        setLocationRelativeTo(parent);
    }
}

package org.gradebook;

import javax.swing.*;
import java.awt.*;

/**
 * Main application entry point for the Student Gradebook.
 * Sets up JFrame and CardLayout for Student and Grade management panels.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GradebookFrame frame = new GradebookFrame();
            frame.setVisible(true);
        });
    }
}

/**
 * Main JFrame for the Gradebook application.
 * Uses CardLayout to switch between Student and Grade management views.
 */
class GradebookFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private StudentPanel studentPanel;
    private GradePanel gradePanel;
    private DatabaseManager dbManager;

    public GradebookFrame() {
        setTitle("Student Gradebook");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        dbManager = new DatabaseManager();
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        studentPanel = new StudentPanel(this, dbManager);
        gradePanel = new GradePanel(this, dbManager);
        cardPanel.add(studentPanel, "students");
        cardPanel.add(gradePanel, "grades");
        add(cardPanel);
        showStudentPanel();
    }

    public void showStudentPanel() {
        studentPanel.refreshTable();
        cardLayout.show(cardPanel, "students");
    }

    public void showGradePanel(int studentId, String studentName) {
        gradePanel.setStudent(studentId, studentName);
        cardLayout.show(cardPanel, "grades");
    }

    public void closeDatabase() {
        dbManager.close();
    }
}
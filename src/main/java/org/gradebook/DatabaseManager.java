package org.gradebook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles all database operations for the Gradebook application.
 * Automatically sets up the SQLite database and tables on startup.
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:gradebook.db";
    private Connection conn;

    public DatabaseManager() {
        try {
            conn = DriverManager.getConnection(DB_URL);
            setupDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates tables if they do not exist and populates initial courses.
     */
    private void setupDatabase() throws SQLException {
    Statement stmt = conn.createStatement();
    // Create students table
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS students (" +
        "student_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "first_name TEXT NOT NULL, " +
        "last_name TEXT NOT NULL)");
    // Create courses table
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS courses (" +
        "course_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "course_name TEXT NOT NULL UNIQUE)");
    // Create grades table with grade_letter
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS grades (" +
        "grade_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "student_id INTEGER NOT NULL, " +
        "course_id INTEGER NOT NULL, " +
        "score TEXT NOT NULL, " +
        "grade_letter TEXT NOT NULL, " +
        "credit_unit INTEGER NOT NULL, " +
        "FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE, " +
        "FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE)");
    // Insert default courses if not present
    stmt.executeUpdate("INSERT OR IGNORE INTO courses (course_name) VALUES ('Mathematics'), ('History'), ('Biology')");
    stmt.close();
    }

    // --- Student Operations ---
    public List<Map<String, Object>> getAllStudents() {
        List<Map<String, Object>> students = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {
            while (rs.next()) {
                Map<String, Object> student = new HashMap<>();
                student.put("student_id", rs.getInt("student_id"));
                student.put("first_name", rs.getString("first_name"));
                student.put("last_name", rs.getString("last_name"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public boolean addStudent(String firstName, String lastName) {
        String sql = "INSERT INTO students (first_name, last_name) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- Course Operations ---
    public List<Map<String, Object>> getAllCourses() {
        List<Map<String, Object>> courses = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM courses")) {
            while (rs.next()) {
                Map<String, Object> course = new HashMap<>();
                course.put("course_id", rs.getInt("course_id"));
                course.put("course_name", rs.getString("course_name"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    // --- Grade Operations ---
    public List<Map<String, Object>> getGradesForStudent(int studentId) {
        List<Map<String, Object>> grades = new ArrayList<>();
        String sql = "SELECT g.grade_id, c.course_name, g.score, g.grade_letter, g.credit_unit FROM grades g JOIN courses c ON g.course_id = c.course_id WHERE g.student_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> grade = new HashMap<>();
                grade.put("grade_id", rs.getInt("grade_id"));
                grade.put("course_name", rs.getString("course_name"));
                grade.put("score", rs.getString("score"));
                grade.put("grade_letter", rs.getString("grade_letter"));
                grade.put("credit_unit", rs.getInt("credit_unit"));
                grades.add(grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }

    public boolean addGrade(int studentId, int courseId, String score, int creditUnit) {
        double scoreVal;
        try {
            scoreVal = Double.parseDouble(score);
        } catch (NumberFormatException e) {
            scoreVal = 0.0;
        }
        String gradeLetter = evaluateGradeLetter(scoreVal);
        String sql = "INSERT INTO grades (student_id, course_id, score, grade_letter, credit_unit) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            pstmt.setString(3, score);
            pstmt.setString(4, gradeLetter);
            pstmt.setInt(5, creditUnit);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Grade evaluation according to Nigerian system
    public String evaluateGradeLetter(double score) {
        if (score >= 70) return "A";
        if (score >= 60) return "B";
        if (score >= 50) return "C";
        if (score >= 45) return "D";
        if (score >= 40) return "E";
        return "F";
    }

    public int gradePoint(String gradeLetter) {
        switch (gradeLetter) {
            case "A": return 5;
            case "B": return 4;
            case "C": return 3;
            case "D": return 2;
            case "E": return 1;
            default: return 0;
        }
    }

    public double calculateGPA(int studentId) {
        String sql = "SELECT grade_letter, credit_unit FROM grades WHERE student_id = ?";
        int totalWeightedPoints = 0, totalCredits = 0;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String gradeLetter = rs.getString("grade_letter");
                int creditUnit = rs.getInt("credit_unit");
                totalWeightedPoints += gradePoint(gradeLetter) * creditUnit;
                totalCredits += creditUnit;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalCredits == 0 ? 0.0 : (double) totalWeightedPoints / totalCredits;
    }

    public boolean deleteGrade(int gradeId) {
        String sql = "DELETE FROM grades WHERE grade_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, gradeId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

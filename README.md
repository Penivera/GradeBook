# GradeBook - Student Grade Management System

## Table of Contents
1. [Overview](#overview)
2. [Features](#features)
3. [Architecture & Design](#architecture--design)
4. [Prerequisites](#prerequisites)
5. [Installation & Setup](#installation--setup)
6. [How to Run](#how-to-run)
7. [Database Schema](#database-schema)
8. [Detailed Code Documentation](#detailed-code-documentation)
9. [Grading System](#grading-system)
10. [User Guide](#user-guide)
11. [Technical Concepts for Beginners](#technical-concepts-for-beginners)
12. [Contributing](#contributing)
13. [License](#license)

## Overview

**GradeBook** is a desktop application built with Java Swing that allows educational institutions to manage student records and their academic grades. The application provides an intuitive graphical user interface (GUI) for adding students, recording their grades across different courses, and calculating their Grade Point Average (GPA) using the Nigerian academic grading system.

### What This Application Does
- **Student Management**: Add, view, and delete student records
- **Grade Management**: Record grades for students across multiple courses
- **GPA Calculation**: Automatically compute Grade Point Average based on credit units and grade letters
- **Data Persistence**: Store all data in a local SQLite database
- **User-Friendly Interface**: Intuitive GUI with easy navigation between different views

### Target Audience
This application is designed for:
- Educational administrators
- Teachers and instructors
- Academic coordinators
- Anyone learning Java desktop application development

## Features

### Core Functionality
✅ **Student Registration**: Add new students with first and last names  
✅ **Student Directory**: View all registered students in a table format  
✅ **Grade Recording**: Add grades for students in different courses  
✅ **GPA Calculation**: Automatic calculation using Nigerian grading system  
✅ **Data Persistence**: SQLite database for reliable data storage  
✅ **Course Management**: Pre-defined courses (Mathematics, History, Biology)  
✅ **Grade Validation**: Automatic letter grade assignment based on numerical scores  

### User Interface Features
✅ **Tabular Data Display**: Easy-to-read tables for students and grades  
✅ **Modal Dialogs**: Clean forms for data entry  
✅ **Navigation**: Seamless switching between student and grade views  
✅ **Selection Management**: Enable/disable buttons based on user selections  

## Architecture & Design

### Design Patterns Used

1. **Model-View-Controller (MVC) Pattern**
   - **Model**: `DatabaseManager` class handles all data operations
   - **View**: `StudentPanel`, `GradePanel`, and dialog classes handle UI
   - **Controller**: `GradebookFrame` manages navigation and coordinates between views

2. **Singleton Pattern** (Implicit)
   - Each panel maintains a single instance of `DatabaseManager`

3. **Observer Pattern**
   - Event listeners respond to user actions (button clicks, table selections)

### Application Structure
```
GradeBook/
├── src/main/java/org/gradebook/
│   ├── Main.java                 # Application entry point
│   ├── GradebookFrame.java      # Main window (embedded in Main.java)
│   ├── DatabaseManager.java     # Database operations and business logic
│   ├── StudentPanel.java        # Student management UI
│   └── GradePanel.java          # Grade management UI
├── README.md                     # This documentation
├── LICENSE                       # MIT License
└── .gitignore                   # Git ignore rules
```

## Prerequisites

### Software Requirements
- **Java Development Kit (JDK)**: Version 8 or higher
  - The application uses Java Swing (part of standard JDK)
  - SQLite JDBC driver (can be downloaded separately if needed)

### System Requirements
- **Operating System**: Windows, macOS, or Linux
- **Memory**: Minimum 256MB RAM (recommended 512MB)
- **Storage**: At least 50MB free space

### Knowledge Prerequisites (For Developers)
- Basic understanding of Java programming
- Familiarity with Object-Oriented Programming concepts
- Basic knowledge of SQL databases (helpful but not required)
- Understanding of event-driven programming

## Installation & Setup

### Step 1: Clone the Repository
```bash
git clone https://github.com/Penivera/GradeBook.git
cd GradeBook
```

### Step 2: Verify Java Installation
```bash
java --version
javac --version
```
Both commands should return version 8 or higher.

### Step 3: Download SQLite JDBC Driver (if needed)
If you encounter database connection issues, download the SQLite JDBC driver:
1. Visit: https://github.com/xerial/sqlite-jdbc/releases
2. Download the latest `sqlite-jdbc-x.x.x.jar` file
3. Place it in your project directory

### Step 4: Compile the Application
```bash
# Navigate to project root
cd GradeBook

# Compile all Java files
javac -cp . -d . src/main/java/org/gradebook/*.java
```

## How to Run

### Method 1: Run from Compiled Classes
```bash
# After compilation, run the main class
java -cp . org.gradebook.Main
```

### Method 2: Compile and Run in One Step
```bash
# Compile and run directly from source
javac src/main/java/org/gradebook/*.java -d . && java org.gradebook.Main
```

### Expected Behavior
When you run the application:
1. A window titled "Student Gradebook" will appear
2. The database file `gradebook.db` will be created automatically in the project directory
3. Three default courses (Mathematics, History, Biology) will be inserted into the database
4. You'll see the Student Management panel first

## Database Schema

The application uses SQLite database with three main tables:

### Students Table
```sql
CREATE TABLE students (
    student_id INTEGER PRIMARY KEY AUTOINCREMENT,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL
);
```
- **student_id**: Unique identifier for each student
- **first_name**: Student's first name
- **last_name**: Student's last name

### Courses Table
```sql
CREATE TABLE courses (
    course_id INTEGER PRIMARY KEY AUTOINCREMENT,
    course_name TEXT NOT NULL UNIQUE
);
```
- **course_id**: Unique identifier for each course
- **course_name**: Name of the course (Mathematics, History, Biology)

### Grades Table
```sql
CREATE TABLE grades (
    grade_id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id INTEGER NOT NULL,
    course_id INTEGER NOT NULL,
    score TEXT NOT NULL,
    grade_letter TEXT NOT NULL,
    credit_unit INTEGER NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
);
```
- **grade_id**: Unique identifier for each grade record
- **student_id**: References the student (foreign key)
- **course_id**: References the course (foreign key)
- **score**: Numerical score (stored as text for flexibility)
- **grade_letter**: Calculated letter grade (A, B, C, D, E, F)
- **credit_unit**: Number of credit units for the course

### Database Relationships
- **One-to-Many**: One student can have many grades
- **One-to-Many**: One course can have many grades
- **Cascade Delete**: When a student is deleted, all their grades are automatically removed

## Detailed Code Documentation

### 1. Main.java - Application Entry Point

```java
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
```

#### Key Concepts Explained:

**SwingUtilities.invokeLater()**
- Ensures that the GUI is created on the Event Dispatch Thread (EDT)
- This is a best practice in Swing applications to avoid threading issues
- The lambda expression `() -> { ... }` is a modern Java way to write anonymous functions

**Why This Pattern?**
- Separates the entry point from the main application logic
- Follows the Single Responsibility Principle
- Makes the code more testable and maintainable

### 2. GradebookFrame.java - Main Application Window

```java
class GradebookFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private StudentPanel studentPanel;
    private GradePanel gradePanel;
    private DatabaseManager dbManager;
    
    // Constructor and methods...
}
```

#### Class Responsibilities:
1. **Window Management**: Sets up the main application window
2. **Navigation Controller**: Manages switching between different views
3. **Component Coordination**: Connects panels with the database manager

#### Key Methods:

**Constructor `GradebookFrame()`**
```java
public GradebookFrame() {
    setTitle("Student Gradebook");                    // Window title
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // Exit on close
    setSize(700, 500);                               // Window dimensions
    setLocationRelativeTo(null);                     // Center on screen
    
    dbManager = new DatabaseManager();               // Initialize database
    cardLayout = new CardLayout();                   // Layout manager
    cardPanel = new JPanel(cardLayout);              // Container panel
    
    // Create and add panels
    studentPanel = new StudentPanel(this, dbManager);
    gradePanel = new GradePanel(this, dbManager);
    cardPanel.add(studentPanel, "students");
    cardPanel.add(gradePanel, "grades");
    
    add(cardPanel);                                  // Add to frame
    showStudentPanel();                              // Show initial view
}
```

**Navigation Methods**
```java
public void showStudentPanel() {
    studentPanel.refreshTable();           // Update data
    cardLayout.show(cardPanel, "students"); // Switch view
}

public void showGradePanel(int studentId, String studentName) {
    gradePanel.setStudent(studentId, studentName); // Set context
    cardLayout.show(cardPanel, "grades");          // Switch view
}
```

#### Design Patterns in GradebookFrame:
- **CardLayout Pattern**: Manages multiple views in single window
- **Dependency Injection**: Passes database manager to child components
- **Event Coordination**: Acts as mediator between different panels

### 3. DatabaseManager.java - Data Access Layer

This is the most complex class, handling all database operations:

#### Class Structure:
```java
public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:gradebook.db";
    private Connection conn;
    
    // Constructor, database setup, and CRUD operations
}
```

#### Key Concepts:

**Database Connection Management**
```java
public DatabaseManager() {
    try {
        conn = DriverManager.getConnection(DB_URL);  // Connect to SQLite
        setupDatabase();                            // Create tables
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
```

**Database Schema Setup**
```java
private void setupDatabase() throws SQLException {
    Statement stmt = conn.createStatement();
    
    // Create students table with auto-incrementing ID
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS students (" +
        "student_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "first_name TEXT NOT NULL, " +
        "last_name TEXT NOT NULL)");
    
    // Create courses table with unique constraint
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS courses (" +
        "course_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "course_name TEXT NOT NULL UNIQUE)");
    
    // Create grades table with foreign key relationships
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS grades (" +
        "grade_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "student_id INTEGER NOT NULL, " +
        "course_id INTEGER NOT NULL, " +
        "score TEXT NOT NULL, " +
        "grade_letter TEXT NOT NULL, " +
        "credit_unit INTEGER NOT NULL, " +
        "FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE, " +
        "FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE)");
    
    // Insert default courses
    stmt.executeUpdate("INSERT OR IGNORE INTO courses (course_name) VALUES " +
        "('Mathematics'), ('History'), ('Biology')");
    
    stmt.close();
}
```

#### CRUD Operations Explained:

**CREATE - Adding New Records**
```java
public boolean addStudent(String firstName, String lastName) {
    String sql = "INSERT INTO students (first_name, last_name) VALUES (?, ?)";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, firstName);  // Parameterized query prevents SQL injection
        pstmt.setString(2, lastName);
        pstmt.executeUpdate();
        return true;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
```

**READ - Retrieving Data**
```java
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
```

**DELETE - Removing Records**
```java
public boolean deleteStudent(int studentId) {
    String sql = "DELETE FROM students WHERE student_id = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, studentId);
        pstmt.executeUpdate();
        return true;  // Success
    } catch (SQLException e) {
        e.printStackTrace();
        return false; // Failure
    }
}
```

#### Advanced Database Operations:

**Grade Calculation Logic**
```java
public String evaluateGradeLetter(double score) {
    if (score >= 70) return "A";      // Excellent
    if (score >= 60) return "B";      // Very Good
    if (score >= 50) return "C";      // Good
    if (score >= 45) return "D";      // Satisfactory
    if (score >= 40) return "E";      // Pass
    return "F";                       // Fail
}

public int gradePoint(String gradeLetter) {
    switch (gradeLetter) {
        case "A": return 5;           // Highest point
        case "B": return 4;
        case "C": return 3;
        case "D": return 2;
        case "E": return 1;
        default: return 0;            // Fail
    }
}
```

**GPA Calculation Algorithm**
```java
public double calculateGPA(int studentId) {
    String sql = "SELECT grade_letter, credit_unit FROM grades WHERE student_id = ?";
    int totalWeightedPoints = 0;
    int totalCredits = 0;
    
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, studentId);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            String gradeLetter = rs.getString("grade_letter");
            int creditUnit = rs.getInt("credit_unit");
            
            // Weighted grade points = grade point × credit units
            totalWeightedPoints += gradePoint(gradeLetter) * creditUnit;
            totalCredits += creditUnit;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    // GPA = Total weighted points ÷ Total credit units
    return totalCredits == 0 ? 0.0 : (double) totalWeightedPoints / totalCredits;
}
```

### 4. StudentPanel.java - Student Management Interface

#### Class Structure:
```java
class StudentPanel extends JPanel {
    private GradebookFrame parentFrame;    // Reference to main window
    private DatabaseManager dbManager;     // Database access
    private JTable studentTable;           // Display students
    private DefaultTableModel tableModel;  // Table data model
    private JButton addButton, viewGradesButton, deleteButton; // Actions
}
```

#### Key UI Components:

**Table Setup**
```java
tableModel = new DefaultTableModel(new Object[]{"ID", "First Name", "Last Name"}, 0) {
    public boolean isCellEditable(int row, int column) { 
        return false; // Make table read-only
    }
};
studentTable = new JTable(tableModel);
studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only one row at a time
```

**Dynamic Button State Management**
```java
// Enable/disable buttons based on table selection
studentTable.getSelectionModel().addListSelectionListener(e -> {
    boolean selected = studentTable.getSelectedRow() != -1;
    viewGradesButton.setEnabled(selected);  // Only enable if row selected
    deleteButton.setEnabled(selected);
});
```

**Event Handling Patterns**
```java
// Add Student Button
addButton.addActionListener(e -> {
    AddStudentDialog dialog = new AddStudentDialog(parentFrame, dbManager, this);
    dialog.setVisible(true);  // Show modal dialog
});

// View Grades Button
viewGradesButton.addActionListener(e -> {
    int row = studentTable.getSelectedRow();
    if (row != -1) {
        int studentId = (int) tableModel.getValueAt(row, 0);
        String name = tableModel.getValueAt(row, 1) + " " + tableModel.getValueAt(row, 2);
        parentFrame.showGradePanel(studentId, name); // Navigate to grades view
    }
});

// Delete Student Button
deleteButton.addActionListener(e -> {
    int row = studentTable.getSelectedRow();
    if (row != -1) {
        int studentId = (int) tableModel.getValueAt(row, 0);
        // Show confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Delete selected student?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dbManager.deleteStudent(studentId);
            refreshTable(); // Update display
        }
    }
});
```

**Data Refresh Pattern**
```java
public void refreshTable() {
    tableModel.setRowCount(0); // Clear existing data
    List<Map<String, Object>> students = dbManager.getAllStudents();
    for (Map<String, Object> s : students) {
        tableModel.addRow(new Object[]{
            s.get("student_id"), 
            s.get("first_name"), 
            s.get("last_name")
        });
    }
}
```

### 5. AddStudentDialog.java - Student Registration Form

```java
class AddStudentDialog extends JDialog {
    public AddStudentDialog(JFrame parent, DatabaseManager dbManager, StudentPanel studentPanel) {
        super(parent, "Add Student", true); // Modal dialog
        setLayout(new GridLayout(3, 2, 10, 10)); // 3 rows, 2 columns, 10px gaps
        
        // Create form components
        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstNameField = new JTextField();
        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastNameField = new JTextField();
        JButton saveButton = new JButton("Save");
        
        // Add components to dialog
        add(firstNameLabel); add(firstNameField);
        add(lastNameLabel); add(lastNameField);
        add(new JLabel()); add(saveButton); // Empty cell and save button
        
        // Save button logic
        saveButton.addActionListener(e -> {
            String first = firstNameField.getText().trim();
            String last = lastNameField.getText().trim();
            
            if (!first.isEmpty() && !last.isEmpty()) {
                dbManager.addStudent(first, last);  // Save to database
                studentPanel.refreshTable();       // Update parent view
                dispose();                          // Close dialog
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Please enter both first and last names.");
            }
        });
        
        setSize(300, 150);
        setLocationRelativeTo(parent); // Center on parent window
    }
}
```

### 6. GradePanel.java - Grade Management Interface

#### Class Features:
- **Student Context**: Shows grades for a specific student
- **GPA Display**: Real-time GPA calculation and display
- **Grade Management**: Add new grades for the student

#### Key Components:

**Dynamic Content Updates**
```java
public void setStudent(int studentId, String studentName) {
    this.studentId = studentId;
    this.studentName = studentName;
    nameLabel.setText("Grades for " + studentName);     // Update header
    refreshTable();                                     // Load grades
    double gpa = dbManager.calculateGPA(studentId);     // Calculate GPA
    gpaLabel.setText(String.format("GPA/CGPA: %.2f", gpa)); // Display GPA
}
```

**Grade Table Structure**
```java
tableModel = new DefaultTableModel(
    new Object[]{"Course", "Score", "Grade", "Credit Unit"}, 0) {
    public boolean isCellEditable(int row, int column) { return false; }
};
```

### 7. AddGradeDialog.java - Grade Entry Form

#### Advanced Form Components:

**Course Selection Dropdown**
```java
JComboBox<String> courseCombo = new JComboBox<>();
List<Map<String, Object>> courses = dbManager.getAllCourses();
for (Map<String, Object> c : courses) {
    courseCombo.addItem((String) c.get("course_name"));
}
```

**Form Validation Logic**
```java
saveButton.addActionListener(e -> {
    String courseName = (String) courseCombo.getSelectedItem();
    String score = scoreField.getText().trim();
    String creditStr = creditField.getText().trim();
    
    if (courseName != null && !score.isEmpty() && !creditStr.isEmpty()) {
        // Find course ID by name
        int courseId = -1;
        for (Map<String, Object> c : courses) {
            if (courseName.equals(c.get("course_name"))) {
                courseId = (int) c.get("course_id");
                break;
            }
        }
        
        // Validate credit unit is a number
        int creditUnit = 0;
        try {
            creditUnit = Integer.parseInt(creditStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Credit unit must be a number.");
            return;
        }
        
        // Save if all validations pass
        if (courseId != -1 && creditUnit > 0) {
            dbManager.addGrade(studentId, courseId, score, creditUnit);
            gradePanel.refreshTable(); // Update display
            dispose(); // Close dialog
        } else {
            JOptionPane.showMessageDialog(this, "Please enter valid data.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Please fill all fields.");
    }
});
```

## Grading System

### Nigerian Academic Grading System

This application implements the Nigerian university grading system:

| Score Range | Letter Grade | Grade Point | Classification |
|-------------|--------------|-------------|----------------|
| 70-100      | A            | 5           | Excellent      |
| 60-69       | B            | 4           | Very Good      |
| 50-59       | C            | 3           | Good           |
| 45-49       | D            | 2           | Satisfactory   |
| 40-44       | E            | 1           | Pass           |
| 0-39        | F            | 0           | Fail           |

### GPA Calculation Formula

```
GPA = Σ(Grade Point × Credit Unit) ÷ Σ(Credit Unit)
```

**Example Calculation:**
- Mathematics: Score 75 (A=5 points) × 3 credit units = 15 weighted points
- History: Score 65 (B=4 points) × 2 credit units = 8 weighted points
- Biology: Score 55 (C=3 points) × 3 credit units = 9 weighted points

**Total:** 32 weighted points ÷ 8 total credits = **4.0 GPA**

### GPA Classification
- **4.5-5.0**: First Class Honours
- **3.5-4.49**: Second Class Honours (Upper Division)
- **2.5-3.49**: Second Class Honours (Lower Division)
- **1.5-2.49**: Third Class Honours
- **1.0-1.49**: Pass
- **Below 1.0**: Fail

## User Guide

### Getting Started

1. **Launch the Application**
   - Run the application using the instructions in [How to Run](#how-to-run)
   - The main window will open showing the Student Management panel

2. **Add Your First Student**
   - Click the "Add Student" button
   - Enter the student's first and last name
   - Click "Save"
   - The student will appear in the table

### Managing Students

#### Adding Students
1. Click "Add Student" button
2. Fill in first name and last name
3. Click "Save"
4. Student appears in the main table

#### Viewing Student Grades
1. Select a student from the table (click on their row)
2. Click "View Grades" button
3. Switch to the grade management view for that student

#### Deleting Students
1. Select a student from the table
2. Click "Delete Student" button
3. Confirm deletion in the popup dialog
4. **Note**: This will also delete all grades for that student

### Managing Grades

#### Adding Grades
1. From the student list, select a student and click "View Grades"
2. In the grade panel, click "Add Grade"
3. Select a course from the dropdown
4. Enter the numerical score (0-100)
5. Enter the credit units for the course
6. Click "Save"
7. The grade letter and GPA will be calculated automatically

#### Understanding the Grade Display
- **Course**: The subject name
- **Score**: Numerical score entered
- **Grade**: Automatically calculated letter grade
- **Credit Unit**: Weight of the course
- **GPA/CGPA**: Displayed at the top, updates automatically

### Navigation
- **Back Button**: Return to student list from grade view
- **Window Controls**: Use standard window controls to minimize, maximize, or close

### Data Persistence
- All data is automatically saved to `gradebook.db` in the application directory
- No manual save operation is required
- Data persists between application sessions

## Technical Concepts for Beginners

### Object-Oriented Programming Concepts

#### 1. Classes and Objects
```java
// Class definition - a blueprint for creating objects
public class Student {
    private String firstName;  // Instance variable
    private String lastName;   // Instance variable
    
    // Constructor - special method to create objects
    public Student(String first, String last) {
        this.firstName = first;
        this.lastName = last;
    }
    
    // Method - behavior of the object
    public String getFullName() {
        return firstName + " " + lastName;
    }
}

// Creating objects (instances) from the class
Student student1 = new Student("John", "Doe");
Student student2 = new Student("Jane", "Smith");
```

#### 2. Inheritance
```java
// Parent class
public class JFrame {
    protected String title;
    public void setTitle(String title) { this.title = title; }
}

// Child class inherits from parent
public class GradebookFrame extends JFrame {
    // GradebookFrame automatically has all JFrame methods
    // Plus its own additional methods
}
```

#### 3. Encapsulation
```java
public class DatabaseManager {
    private Connection conn;  // Private - only this class can access
    
    public boolean addStudent(String first, String last) {
        // Public method - external classes can call this
        // Implementation details are hidden
    }
}
```

#### 4. Polymorphism
```java
// Different dialog classes can all be treated as JDialog
JDialog studentDialog = new AddStudentDialog(...);
JDialog gradeDialog = new AddGradeDialog(...);

// Both respond to the same method call, but behave differently
studentDialog.setVisible(true);
gradeDialog.setVisible(true);
```

### Database Concepts

#### 1. Relational Database
- **Tables**: Store data in rows and columns (like spreadsheets)
- **Relationships**: Tables connect to each other through foreign keys
- **Primary Key**: Unique identifier for each row
- **Foreign Key**: Reference to primary key in another table

#### 2. SQL Operations
```sql
-- CREATE: Make new records
INSERT INTO students (first_name, last_name) VALUES ('John', 'Doe');

-- READ: Retrieve data
SELECT * FROM students WHERE last_name = 'Doe';

-- UPDATE: Modify existing records
UPDATE students SET first_name = 'Johnny' WHERE student_id = 1;

-- DELETE: Remove records
DELETE FROM students WHERE student_id = 1;
```

#### 3. Database Transactions
- All database operations are atomic (all succeed or all fail)
- Ensures data consistency and integrity

### GUI Programming Concepts

#### 1. Event-Driven Programming
```java
button.addActionListener(e -> {
    // This code runs when the button is clicked
    System.out.println("Button was clicked!");
});
```

#### 2. Layout Managers
- **BorderLayout**: Divides container into 5 regions (North, South, East, West, Center)
- **GridLayout**: Arranges components in a rectangular grid
- **CardLayout**: Shows one component at a time (like tabs)

#### 3. Model-View Separation
- **Model**: Data and business logic (`DatabaseManager`)
- **View**: User interface components (`JPanel`, `JTable`, `JButton`)
- **Controller**: Coordinates between model and view (`GradebookFrame`)

### Threading in GUI Applications

#### Event Dispatch Thread (EDT)
```java
SwingUtilities.invokeLater(() -> {
    // This code runs on the EDT - safe for GUI operations
    frame.setVisible(true);
});
```

**Why This Matters:**
- GUI components are not thread-safe
- All GUI updates must happen on the EDT
- Prevents deadlocks and race conditions

### Exception Handling

#### Try-Catch Blocks
```java
try {
    // Code that might fail
    int result = Integer.parseInt("not a number");
} catch (NumberFormatException e) {
    // Handle the specific error
    System.out.println("Invalid number format");
} catch (Exception e) {
    // Handle any other errors
    System.out.println("Something went wrong: " + e.getMessage());
}
```

#### Resource Management (Try-with-Resources)
```java
try (PreparedStatement stmt = conn.prepareStatement(sql)) {
    // Use the statement
    stmt.executeUpdate();
} // Statement is automatically closed here, even if an exception occurs
```

### Lambda Expressions (Modern Java)
```java
// Old way - anonymous inner class
button.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        System.out.println("Clicked!");
    }
});

// New way - lambda expression
button.addActionListener(e -> System.out.println("Clicked!"));

// Lambda with multiple statements
button.addActionListener(e -> {
    System.out.println("Button clicked");
    doSomething();
});
```

### Memory Management in Java

#### Automatic Garbage Collection
- Java automatically manages memory allocation and deallocation
- Objects are cleaned up when no longer referenced
- No need for manual memory management (unlike C/C++)

#### Best Practices
```java
// Close database connections to free resources
public void close() {
    try {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
```

## Extending the Application

### Adding New Features

#### 1. Add New Course
To add support for new courses, modify the `setupDatabase()` method:
```java
stmt.executeUpdate("INSERT OR IGNORE INTO courses (course_name) VALUES " +
    "('Mathematics'), ('History'), ('Biology'), ('Physics'), ('Chemistry')");
```

#### 2. Add Grade Editing
Currently, grades can only be added. To add editing capability:

1. Add an "Edit Grade" button to `GradePanel`
2. Create an `EditGradeDialog` class
3. Implement the `updateGrade()` method in `DatabaseManager`

#### 3. Add Student Search
Implement a search feature in `StudentPanel`:
```java
private JTextField searchField;

private void filterStudents(String searchTerm) {
    // Filter table based on search term
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
    studentTable.setRowSorter(sorter);
    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchTerm));
}
```

#### 4. Add Data Export
Export student data to CSV:
```java
public void exportToCSV(String filename) throws IOException {
    try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
        writer.println("Student ID,First Name,Last Name,GPA");
        for (Map<String, Object> student : getAllStudents()) {
            int id = (int) student.get("student_id");
            double gpa = calculateGPA(id);
            writer.printf("%d,%s,%s,%.2f%n", 
                id, student.get("first_name"), student.get("last_name"), gpa);
        }
    }
}
```

### Performance Optimizations

#### 1. Database Connection Pooling
For larger applications, implement connection pooling to manage database connections efficiently.

#### 2. Lazy Loading
Load data only when needed to improve startup time and memory usage.

#### 3. Background Processing
Move long-running operations to background threads to keep the GUI responsive.

### Security Considerations

#### 1. Input Validation
Always validate user input to prevent data corruption and security issues:
```java
private boolean isValidName(String name) {
    return name != null && name.trim().length() > 0 && name.length() <= 50;
}
```

#### 2. SQL Injection Prevention
The application already uses parameterized queries to prevent SQL injection:
```java
PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM students WHERE name = ?");
pstmt.setString(1, userInput); // Safe from SQL injection
```

#### 3. Data Backup
Implement regular backups of the SQLite database file.

## Troubleshooting

### Common Issues

#### 1. "Class not found" Error
**Problem**: `java.lang.ClassNotFoundException: org.gradebook.Main`
**Solution**: 
- Ensure you're running from the correct directory
- Verify the classpath includes compiled classes
- Recompile with: `javac -cp . -d . src/main/java/org/gradebook/*.java`

#### 2. Database Connection Errors
**Problem**: Cannot connect to SQLite database
**Solution**:
- Ensure the application has write permissions in the current directory
- Check if `gradebook.db` file is created
- Verify SQLite JDBC driver is available

#### 3. Window Not Appearing
**Problem**: Application runs but no window shows
**Solution**:
- Check if window is minimized or behind other windows
- Verify display settings and screen resolution
- Try running on Event Dispatch Thread: `SwingUtilities.invokeLater(...)`

#### 4. Table Data Not Refreshing
**Problem**: Changes don't appear in tables
**Solution**:
- Ensure `refreshTable()` is called after database operations
- Check for database transaction issues
- Verify the table model is properly updated

### Debug Mode

Add debug output to understand application flow:
```java
public class DatabaseManager {
    private static final boolean DEBUG = true;
    
    private void debug(String message) {
        if (DEBUG) {
            System.out.println("[DEBUG] " + message);
        }
    }
    
    public boolean addStudent(String firstName, String lastName) {
        debug("Adding student: " + firstName + " " + lastName);
        // ... rest of method
    }
}
```

### Performance Monitoring

Monitor application performance:
```java
public void refreshTable() {
    long startTime = System.currentTimeMillis();
    
    // ... refresh logic ...
    
    long endTime = System.currentTimeMillis();
    System.out.println("Table refresh took: " + (endTime - startTime) + "ms");
}
```

## Contributing

### Development Setup

1. **Fork the Repository**
   ```bash
   # On GitHub, click "Fork" button
   git clone https://github.com/YOUR_USERNAME/GradeBook.git
   ```

2. **Create a Development Branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make Changes**
   - Follow existing code style and patterns
   - Add comments for complex logic
   - Update documentation if needed

4. **Test Your Changes**
   ```bash
   # Compile and test
   javac -cp . -d . src/main/java/org/gradebook/*.java
   java -cp . org.gradebook.Main
   ```

5. **Submit a Pull Request**
   - Write clear commit messages
   - Describe what your changes do
   - Include screenshots for UI changes

### Code Style Guidelines

#### Java Conventions
- Use camelCase for variables and methods
- Use PascalCase for class names
- Use UPPER_CASE for constants
- Include JavaDoc comments for public methods

#### Example:
```java
/**
 * Calculates the Grade Point Average for a student.
 * @param studentId The unique identifier of the student
 * @return The calculated GPA as a double value
 */
public double calculateGPA(int studentId) {
    // Method implementation
}
```

### Testing Guidelines

#### Manual Testing Checklist
- [ ] Application starts without errors
- [ ] Can add new students
- [ ] Can delete students
- [ ] Can add grades for students
- [ ] GPA calculates correctly
- [ ] Navigation between panels works
- [ ] Database persists data between sessions

#### Integration Testing
- Test with different data combinations
- Verify foreign key constraints work
- Test with edge cases (empty names, invalid scores)

### Documentation Standards

When adding new features:
1. Update this README with new functionality
2. Add inline code comments
3. Include usage examples
4. Document any new dependencies

## Future Enhancements

### Planned Features
- [ ] **Grade Editing**: Modify existing grades
- [ ] **Bulk Import**: Import students from CSV files
- [ ] **Reporting**: Generate PDF reports of student performance
- [ ] **Multiple Semesters**: Track grades across different terms
- [ ] **User Authentication**: Add login system for multiple users
- [ ] **Advanced Grading**: Support for weighted categories (exams, assignments, etc.)
- [ ] **Grade Curves**: Apply curves to final grades
- [ ] **Attendance Tracking**: Monitor student attendance
- [ ] **Parent Portal**: Allow parents to view student grades

### Technical Improvements
- [ ] **Modern UI**: Migrate to JavaFX for better user experience
- [ ] **Database Migration**: Support PostgreSQL/MySQL for larger deployments
- [ ] **Web Interface**: Create web-based version using Spring Boot
- [ ] **Mobile App**: Develop mobile companion app
- [ ] **Cloud Sync**: Synchronize data across multiple devices
- [ ] **API Development**: RESTful API for external integrations
- [ ] **Real-time Updates**: Live updates when multiple users access the system

### Architecture Enhancements
- [ ] **Plugin System**: Allow third-party extensions
- [ ] **Configuration File**: Externalize settings (database URL, grading scales)
- [ ] **Logging Framework**: Implement proper logging with levels
- [ ] **Unit Testing**: Add comprehensive test suite
- [ ] **Build System**: Add Maven/Gradle build configuration
- [ ] **Continuous Integration**: Set up automated testing and deployment

## Learning Resources

### Java Swing
- [Oracle Swing Tutorial](https://docs.oracle.com/javase/tutorial/uiswing/)
- [Swing Layout Managers](https://docs.oracle.com/javase/tutorial/uiswing/layout/)

### Database Programming
- [JDBC Tutorial](https://docs.oracle.com/javase/tutorial/jdbc/)
- [SQLite Documentation](https://www.sqlite.org/docs.html)

### Design Patterns
- [Head First Design Patterns](https://www.oreilly.com/library/view/head-first-design/0596007124/)
- [Java Design Patterns](https://java-design-patterns.com/)

### Version Control
- [Git Handbook](https://guides.github.com/introduction/git-handbook/)
- [GitHub Flow](https://guides.github.com/introduction/flow/)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

### What This Means
- ✅ **Commercial Use**: Use this code in commercial projects
- ✅ **Modification**: Modify the code to suit your needs
- ✅ **Distribution**: Share this code with others
- ✅ **Private Use**: Use for personal projects
- ❗ **Attribution**: Include the original license and copyright notice
- ❌ **Liability**: The authors are not liable for any damages
- ❌ **Warranty**: No warranty is provided with this software

---

## Contact & Support

### Getting Help
- **Issues**: Report bugs or request features on [GitHub Issues](https://github.com/Penivera/GradeBook/issues)
- **Discussions**: Ask questions in [GitHub Discussions](https://github.com/Penivera/GradeBook/discussions)

### Author Information
- **Developer**: Peniel Ben
- **GitHub**: [@Penivera](https://github.com/Penivera)
- **Project**: [GradeBook Repository](https://github.com/Penivera/GradeBook)

### Acknowledgments
- SQLite team for the excellent embedded database
- Oracle for Java Swing framework
- GitHub for hosting and collaboration tools

---

*Thank you for using GradeBook! We hope this application helps you manage student grades effectively and serves as a great learning resource for Java desktop application development.*
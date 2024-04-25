package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import Models.Classes;


import Models.Course;
import Models.Teacher;
import Models.User;

public class DatabaseConnector {
	  private static final String JDBC_URL = "jdbc:mysql://localhost:3307/quizdb";
	    private static final String USERNAME = "root";
	    private static final String PASSWORD = "";
	    
	    private static final String SELECT_STUDENTS = "SELECT * FROM users WHERE role = 'Student'";
	    private static final String INSERT_USER = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
	    private static final String UPDATE_USER = "UPDATE users SET username = ?, password = ? WHERE user_id = ?";
	    private static final String DELETE_USER = "DELETE FROM users WHERE user_id = ?";
	    // SQL queries for classes
	    private static final String SELECT_ALL_CLASSES = "SELECT * FROM classes";
	    private static final String INSERT_CLASS = "INSERT INTO classes (class_name) VALUES (?)";
	    private static final String UPDATE_CLASS = "UPDATE classes SET class_name = ? WHERE class_id = ?";
	    private static final String DELETE_CLASS = "DELETE FROM classes WHERE class_id = ?";
	    private static final String SELECT_CLASSES = "SELECT class_id, class_name FROM classes;";
	    private static final String INSERT_Student= "INSERT INTO users (username, password, role, class_id) VALUES (?, ?, ?, ?);";
	      


	    private static Connection connection;

	    static {
	        try {
	            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	        } catch (SQLException e) {
	            System.err.println("Failed to connect to database: " + e.getMessage());
	        }
	    }
	    
	    private static Connection getConnection() throws SQLException {
	        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	    }

	    public static boolean authenticate(String username, String password) throws SQLException {
	        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setString(1, username);
	            statement.setString(2, password);
	            ResultSet resultSet = statement.executeQuery();
	            return resultSet.next();
	        }
	    }

	    public static boolean registerUser(String username, String password, String role) throws SQLException {
	        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setString(1, username);
	            statement.setString(2, password);
	            statement.setString(3, role);
	            return statement.executeUpdate() > 0;
	        }
	    }
	    
	    public static List<Course> getAllCourses() throws SQLException {
	        List<Course> courses = new ArrayList<>();
	        String query = "SELECT course_id, course_name FROM courses";
	        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	             PreparedStatement statement = conn.prepareStatement(query);
	             ResultSet resultSet = statement.executeQuery()) {
	            while (resultSet.next()) {
	                int courseId = resultSet.getInt("course_id");
	                String courseName = resultSet.getString("course_name");
	                Course course = new Course(courseId, courseName);
	                courses.add(course);
	            }
	        }
	        return courses;
	    }

	    public static void createCourse(String courseName) throws SQLException {
	        String query = "INSERT INTO courses (course_name) VALUES (?)";
	        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	             PreparedStatement statement = conn.prepareStatement(query)) {
	            statement.setString(1, courseName);
	            statement.executeUpdate();
	        }
	    }

	    public static void deleteCourse(int courseId) throws SQLException {
	        String query = "DELETE FROM courses WHERE course_id = ?";
	        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	             PreparedStatement statement = conn.prepareStatement(query)) {
	            statement.setInt(1, courseId);
	            statement.executeUpdate();
	        }
	    }

	    public static void updateCourse(int courseId, String newCourseName) throws SQLException {
	        String query = "UPDATE courses SET course_name = ? WHERE course_id = ?";
	        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	             PreparedStatement statement = conn.prepareStatement(query)) {
	            statement.setString(1, newCourseName);
	            statement.setInt(2, courseId);
	            statement.executeUpdate();
	        }
	    }
	    
	    public static String authenticateAndGetRole(String username, String password) throws SQLException {
	        Connection conn = null;
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        String role = null;

	        try {
	            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	            String query = "SELECT role FROM users WHERE username = ? AND password = ?";
	            stmt = conn.prepareStatement(query);
	            stmt.setString(1, username);
	            stmt.setString(2, password);
	            rs = stmt.executeQuery();

	            if (rs.next()) {
	                role = rs.getString("role");
	            }
	        } finally {
	            if (rs != null) {
	                rs.close();
	            }
	            if (stmt != null) {
	                stmt.close();
	            }
	            if (conn != null) {
	                conn.close();
	            }
	        }

	        return role;
	    }
	    
	    
	 // Get all students (users with role "Student") from the database
	    public static List<User> getAllStudents() throws SQLException {
	        List<User> students = new ArrayList<>();
	        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(SELECT_STUDENTS)) {
	            while (rs.next()) {
	                int userId = rs.getInt("user_id");
	                String username = rs.getString("username");
	                String className = rs.getString("class_id"); // Assuming the class_id is stored as a String in the database
	                students.add(new User(userId, username, className, "Student"));
	            }
	        }
	        return students;
	    }

	    // Insert a new user into the database with role "Student"
	    public static void createStudent(String username, String password, int classId) throws SQLException {
	        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	             PreparedStatement stmt = conn.prepareStatement(INSERT_Student)) {
	            stmt.setString(1, username);
	            stmt.setString(2, password);
	            stmt.setString(3, "Student");
	            stmt.setInt(4, classId);
	            stmt.executeUpdate();
	        }
	    }

	    // Update a student's username and class in the database
	    public static void updateStudent(int userId, String newUsername, int selectedClass) throws SQLException {
	        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	             PreparedStatement stmt = conn.prepareStatement(UPDATE_USER)) {
	            stmt.setString(1, newUsername);
	            stmt.setInt(2, selectedClass);
	            stmt.setInt(3, userId);
	            stmt.executeUpdate();
	        }
	    }

	    // Delete a student (user with role "Student") from the database
	    public static void deleteStudent(int userId) throws SQLException {
	        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	             PreparedStatement stmt = conn.prepareStatement(DELETE_USER)) {
	            stmt.setInt(1, userId);
	            stmt.executeUpdate();
	        }
	    }

	    
	    
	    public static List<Classes> getAllClasses() throws SQLException {
	        List<Classes> classes = new ArrayList<>();
	        String query = "SELECT class_id, class_name FROM classes";
	        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	             PreparedStatement statement = conn.prepareStatement(query);
	             ResultSet resultSet = statement.executeQuery()) {
	            while (resultSet.next()) {
	                int classId = resultSet.getInt("class_id");
	                String className = resultSet.getString("class_name");
	                Classes classObj = new Classes(classId, className);
	                classes.add(classObj);
	            }
	        }
	        return classes;
	    }

	    public static void createClass(String className) throws SQLException {
	        String query = "INSERT INTO classes (class_name) VALUES (?)";
	        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	             PreparedStatement statement = conn.prepareStatement(query)) {
	            statement.setString(1, className);
	            statement.executeUpdate();
	        }
	    }

	    public static void deleteClass(int classId) throws SQLException {
	        String query = "DELETE FROM classes WHERE class_id = ?";
	        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	             PreparedStatement statement = conn.prepareStatement(query)) {
	            statement.setInt(1, classId);
	            statement.executeUpdate();
	        }
	    }

	    public static void updateClass(int classId, String newClassName) throws SQLException {
	        String query = "UPDATE classes SET class_name = ? WHERE class_id = ?";
	        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	             PreparedStatement statement = conn.prepareStatement(query)) {
	            statement.setString(1, newClassName);
	            statement.setInt(2, classId);
	            statement.executeUpdate();
	        }
	    }
	    
	   
	    public static List<User> getAllUsersWithRole(String role) throws SQLException {
	        List<User> users = new ArrayList<>();
	        String query = "SELECT user_id, username, password FROM users WHERE role = ?";
	        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	             PreparedStatement statement = conn.prepareStatement(query)) {
	            statement.setString(1, role);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                while (resultSet.next()) {
	                    int userId = resultSet.getInt("user_id");
	                    String userName = resultSet.getString("username");
	                    String password = resultSet.getString("password");
	                    User user = new User(userId, userName, password, role);
	                    System.out.println(user);
	                    users.add(user);
	                }
	            }
	        }
	        return users;
	    }

	    public static void createUser(String userName, String password, String role) throws SQLException {
	        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
	        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	             PreparedStatement statement = conn.prepareStatement(query)) {
	            statement.setString(1, userName);
	            statement.setString(2, password);
	            statement.setString(3, role);
	            statement.executeUpdate();
	        }
	    }

	    public static void deleteUser(int userId) throws SQLException {
	        String query = "DELETE FROM users WHERE user_id = ?";
	        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	             PreparedStatement statement = conn.prepareStatement(query)) {
	             statement.setInt(1, userId);
	             statement.executeUpdate();
	        }
	    }

	    public static void updateUser(int userId, String newUserName, String newPassword) throws SQLException {
	        String query = "UPDATE users SET username = ?, password = ? WHERE user_id = ?";
	        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	             PreparedStatement statement = conn.prepareStatement(query)) {
	            statement.setString(1, newUserName);
	            statement.setString(2, newPassword);
	            statement.setInt(3, userId);
	            statement.executeUpdate();
	        }
	    }
	    
	    public static void assignTeacherToCourse(int courseId, int teacherId) throws SQLException {
	        String query = "UPDATE courses SET teacher_id = ? WHERE course_id = ?";
	        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	             PreparedStatement statement = conn.prepareStatement(query)) {
	            statement.setInt(1, teacherId);
	            statement.setInt(2, courseId);
	            statement.executeUpdate();
	        }
	    }

	    public static void assignTeacherToClass(int classId, int teacherId) throws SQLException {
	        String query = "UPDATE classes SET teacher_id = ? WHERE class_id = ?";
	        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	             PreparedStatement statement = conn.prepareStatement(query)) {
	            statement.setInt(1, teacherId);
	            statement.setInt(2, classId);
	            statement.executeUpdate();
	        }
	    }



	}
	    
	    

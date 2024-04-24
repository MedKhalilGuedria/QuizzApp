package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Models.Course;

public class DatabaseConnector {
	  private static final String JDBC_URL = "jdbc:mysql://localhost:3307/quizdb";
	    private static final String USERNAME = "root";
	    private static final String PASSWORD = "";
	    private static Connection connection;

	    static {
	        try {
	            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	        } catch (SQLException e) {
	            System.err.println("Failed to connect to database: " + e.getMessage());
	        }
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
}
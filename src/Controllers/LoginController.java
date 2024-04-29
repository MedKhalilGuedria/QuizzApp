package Controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.DatabaseConnector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

	  @FXML
	    private TextField usernameField;

	    @FXML
	    private PasswordField passwordField;

	    @FXML
	    private void handleLoginButtonAction(ActionEvent event) {
	        String username = usernameField.getText();
	        String password = passwordField.getText();

	        try {
	            String role = DatabaseConnector.authenticateAndGetRole(username, password);
	            if (role != null) {
	                System.out.println("Login successful!");

	                // Redirect based on user role
	                switch (role) {
	                    case "Administrator":
	                        redirectToAdminInterface();
	                        break;
	                    case "Teacher":
	                    	redirectToTeacherInterface();
	                        break;
	                    case "Student":
	                    	redirectToStudentInterface();	                        
	                    	break;
	                    default:
	                        System.out.println("Unknown user role.");
	                }
	            } else {
	                System.out.println("Invalid username or password.");
	            }
	        } catch (SQLException e) {
	            System.err.println("Error authenticating user: " + e.getMessage());
	        }
	    }

	    private void redirectToAdminInterface() {
	        try {
	            Stage stage = (Stage) usernameField.getScene().getWindow();
	            Parent root = FXMLLoader.load(getClass().getResource("/Views/admin.fxml"));
	            Scene scene = new Scene(root);
	            stage.setScene(scene);
	            stage.setTitle("Admin Interface");
	        } catch (IOException e) {
	            System.err.println("Error loading Admin interface: " + e.getMessage());
	        }
	    }
	    
	    private void redirectToTeacherInterface() {
	        try {
	            Stage stage = (Stage) usernameField.getScene().getWindow();
	            Parent root = FXMLLoader.load(getClass().getResource("/Views/TeacherWelcome.fxml"));
	            Scene scene = new Scene(root);
	            stage.setScene(scene);
	            stage.setTitle("Teacher Interface");
	        } catch (IOException e) {
	            System.err.println("Error loading Student interface: " + e.getMessage());
	        }
	    }
	    
	    
	    
	    private void redirectToStudentInterface() {
	        try {
	            Stage stage = (Stage) usernameField.getScene().getWindow();
	            Parent root = FXMLLoader.load(getClass().getResource("/Views/StudentWelcome.fxml"));
	            Scene scene = new Scene(root);
	            stage.setScene(scene);
	            stage.setTitle("Student Interface");
	        } catch (IOException e) {
	            System.err.println("Error loading Student interface: " + e.getMessage());
	        }
	    }


	    @FXML
	    private void handleRegisterButtonAction(ActionEvent event) {
	        try {
	            Stage stage = (Stage) usernameField.getScene().getWindow();
	            Parent root = FXMLLoader.load(getClass().getResource("/Views/registration.fxml"));
	            Scene scene = new Scene(root);
	            stage.setScene(scene);
	            stage.setTitle("Registration");
	        } catch (IOException e) {
	            System.err.println("Error loading registration screen: " + e.getMessage());
	        }
	    }
}
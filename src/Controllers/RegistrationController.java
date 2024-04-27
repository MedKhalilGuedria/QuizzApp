package Controllers;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import util.DatabaseConnector;

public class RegistrationController {
	
	@FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ChoiceBox<String> roleChoiceBox;
    @FXML
    private Button backButton;
    
    public void initialize() {
        // Initialize the items for the ChoiceBox
        roleChoiceBox.getItems().addAll("Administrator", "Teacher", "Student");
    }

    @FXML
    private void handleRegistrationButtonAction(ActionEvent event) {
    	
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleChoiceBox.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            System.out.println("Please fill in all fields.");
            return;
        }

        try {
            if (DatabaseConnector.registerUser(username, password, role)) {
                System.out.println("User registered successfully.");
                
            	Alert successAlert = new Alert(AlertType.INFORMATION);
                successAlert.setTitle("Register ");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Registered successfully!");
                successAlert.showAndWait();
            } else {
                System.out.println("Failed to register user.");
            }
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleBackToLoginButtonAction(ActionEvent event) throws IOException {
    	 FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Login.fxml"));
         Parent root = loader.load();
         
         // Get the stage from the event source
         Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
         
         stage.setScene(new Scene(root));
         stage.show();
    }

}

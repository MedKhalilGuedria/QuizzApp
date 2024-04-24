package Controllers;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import util.DatabaseConnector;

public class RegistrationController {
	
	@FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ChoiceBox<String> roleChoiceBox;
    
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
                // TODO: Redirect to login view
            } else {
                System.out.println("Failed to register user.");
            }
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
        }
    }

}

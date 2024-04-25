package Controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import util.DatabaseConnector;
import Models.Classes;
import Models.User;

import java.sql.SQLException;
import java.util.List;

public class StudentsController {

    @FXML
    private TableView<User> studentsTable;

    @FXML
    private TableColumn<User, Integer> userIdColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, Integer> classColumn;

    @FXML
    private TextField newUsernameField;

    @FXML
    private TextField newPasswordField;

    @FXML
    private ComboBox<Classes> classComboBox;

    private ObservableList<User> studentsList;
    private ObservableList<Classes> classList;

    public void initialize() {
        userIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUserId()).asObject());
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        loadStudents();
        loadClasses();
    }

    @FXML
    private void handleAddStudent(ActionEvent event) {
        String newUsername = newUsernameField.getText().trim();
        String newPassword = newPasswordField.getText().trim();
        Classes selectedClass = classComboBox.getValue();
        if (!newUsername.isEmpty() && !newPassword.isEmpty() && selectedClass != null) {
            try {
                DatabaseConnector.createStudent(newUsername, newPassword, selectedClass.getClassId());
                loadStudents();
                newUsernameField.clear();
                newPasswordField.clear();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Please enter both username, password, and select a class.");
        }
    }

    @FXML
    private void handleUpdateStudent(ActionEvent event) {
        User selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
        String newUsername = newUsernameField.getText().trim();
        Classes selectedClass = classComboBox.getValue();
        if (selectedStudent != null && !newUsername.isEmpty() && selectedClass != null ){
            try {
                DatabaseConnector.updateStudent(selectedStudent.getUserId(), newUsername, selectedClass.getClassId());
                loadStudents();
                newUsernameField.clear();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Please select a student, enter a new username, and select a class.");
        }
    }

    @FXML
    private void handleDeleteStudent(ActionEvent event) {
        User selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            try {
                DatabaseConnector.deleteStudent(selectedStudent.getUserId());
                loadStudents();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Please select a student to delete.");
        }
    }

    private void loadStudents() {
        try {
            List<User> allStudents = DatabaseConnector.getAllStudents();
            studentsList = FXCollections.observableArrayList(allStudents);
            studentsTable.setItems(studentsList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadClasses() {
        try {
            List<Classes> allClasses = DatabaseConnector.getAllClasses();
            classList = FXCollections.observableArrayList(allClasses);
            classComboBox.setItems(classList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

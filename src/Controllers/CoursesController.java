package Controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import util.DatabaseConnector;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import Models.Course;

public class CoursesController {

    @FXML
    private TableView<Course> coursesTable;

    @FXML
    private TableColumn<Course, Integer> courseIdColumn;

    @FXML
    private TableColumn<Course, String> courseNameColumn;

    @FXML
    private TextField newCourseNameField;

    private ObservableList<Course> coursesList;

    public void initialize() {
        loadData(); // Initialize the table
    }

    private void loadData() {
        try {
            List<Course> allCourses = DatabaseConnector.getAllCourses();
            coursesList = FXCollections.observableArrayList(allCourses);
            courseIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCourseId()).asObject()); 
            courseNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourseName()));
            coursesTable.setItems(coursesList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddCourse(ActionEvent event) {
        String newCourseName = newCourseNameField.getText();
        if (!newCourseName.isEmpty()) {
            try {
                DatabaseConnector.createCourse(newCourseName);
                loadData();
            	Alert successAlert = new Alert(AlertType.INFORMATION);
                successAlert.setTitle("Course Creation");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Course created successfully!");
                successAlert.showAndWait();// Reload data after adding a new course
                newCourseNameField.clear();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Please enter a course name.");
        }
    }

    @FXML
    private void handleDeleteCourse(ActionEvent event) {
        Course selectedCourse = coursesTable.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            try {
                DatabaseConnector.deleteCourse(selectedCourse.getCourseId());
                loadData(); // Reload data after deleting a course
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Please select a course to delete.");
        }
    }

    @FXML
    private void handleUpdateCourse(ActionEvent event) {
        Course selectedCourse = coursesTable.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            String newCourseName = newCourseNameField.getText();
            if (!newCourseName.isEmpty()) {
                try {
                    DatabaseConnector.updateCourse(selectedCourse.getCourseId(), newCourseName);
                    loadData();
                	Alert successAlert = new Alert(AlertType.INFORMATION);
                    successAlert.setTitle("Course Update");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Course updated successfully!");
                    successAlert.showAndWait();// Reload data after updating a course
                    newCourseNameField.clear();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert("Please enter a new course name.");
            }
        } else {
            showAlert("Please select a course to update.");
        }
    }

    @FXML
    private void handleTableSelection() {
        Course selectedCourse = coursesTable.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            newCourseNameField.setText(selectedCourse.getCourseName());
        }
    }
    
    @FXML
    public void returnToMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/admin.fxml"));
        Parent root = loader.load();
        
        // Get the stage from the event source
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

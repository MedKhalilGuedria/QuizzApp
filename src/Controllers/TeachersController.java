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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.DatabaseConnector;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import Models.Classes;
import Models.Course;
import Models.User;

public class TeachersController {

    @FXML
    private TableView<User> teachersTable;

    @FXML
    private TableColumn<User, Integer> userIdColumn;

    @FXML
    private TableColumn<User, String> userNameColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TextField newUserNameField;

    @FXML
    private PasswordField newPasswordField;
    


    @FXML
    private TableColumn<Course, String> courseColumn;

    @FXML
    private TableColumn<Classes, String> classColumn;

    @FXML
    private  ComboBox<String> courseComboBox;

    @FXML
    private ComboBox<String>  classComboBox;

    private ObservableList<User> teachersList;
    private ObservableList<Course> coursesList;
    private ObservableList<Classes> classesList;

    public void initialize() {
        System.out.print("test");

        loadData();
        System.out.print("test");
        loadCourses();
        System.out.print("test12");

        loadClasses();
        System.out.print("test13");
// Initialize the table
    }

    private void loadData() {
        try {
            List<User> allTeachers = DatabaseConnector.getAllUsersWithRole("Teacher");
            System.out.print("test1");

            teachersList = FXCollections.observableArrayList(allTeachers);
            System.out.print("test2");

            userIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUserId()).asObject());
            System.out.print("test3");

            userNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
            System.out.print("test4");

           

            teachersTable.setItems(teachersList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddTeacher(ActionEvent event) {
        String newUserName = newUserNameField.getText();
        String newPassword = newPasswordField.getText();
        if (!newUserName.isEmpty() && !newPassword.isEmpty()) {
            try {
                DatabaseConnector.createUser(newUserName, newPassword, "Teacher");
                loadData(); // Reload data after adding a new teacher
                newUserNameField.clear();
                newPasswordField.clear();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Please enter both a teacher name and a password.");
        }
    }

    @FXML
    private void handleDeleteTeacher(ActionEvent event) {
        User selectedTeacher = teachersTable.getSelectionModel().getSelectedItem();
        if (selectedTeacher != null) {
            try {
                DatabaseConnector.deleteUser(selectedTeacher.getUserId());
                loadData(); // Reload data after deleting a teacher
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Please select a teacher to delete.");
        }
    }

    @FXML
    private void handleUpdateTeacher(ActionEvent event) {
        User selectedTeacher = teachersTable.getSelectionModel().getSelectedItem();
        if (selectedTeacher != null) {
            String newUserName = newUserNameField.getText();
            String newPassword = newPasswordField.getText();
            if (!newUserName.isEmpty() && !newPassword.isEmpty()) {
                try {
                    DatabaseConnector.updateUser(selectedTeacher.getUserId(), newUserName, newPassword);
                    loadData(); // Reload data after updating a teacher
                    newUserNameField.clear();
                    newPasswordField.clear();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert("Please enter both a new teacher name and a new password.");
            }
        } else {
            showAlert("Please select a teacher to update.");
        }
    }

    @FXML
    private void handleTableSelection() {
        User selectedTeacher = teachersTable.getSelectionModel().getSelectedItem();
        if (selectedTeacher != null) {
            newUserNameField.setText(selectedTeacher.getUsername());
            newPasswordField.setText(selectedTeacher.getPassword());
        }
    }
    
    
    private void loadCourses() {
        try {
            List<Course> allCourses = DatabaseConnector.getAllCourses();
            coursesList = FXCollections.observableArrayList(allCourses);
            ObservableList<String> courseNames = FXCollections.observableArrayList();
            for (Course course : allCourses) {
                courseNames.add(course.getCourseName());
            }
            courseComboBox.setItems(courseNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadClasses() {
        try {
            List<Classes> allClasses = DatabaseConnector.getAllClasses();
            classesList = FXCollections.observableArrayList(allClasses);
            ObservableList<String> classNames = FXCollections.observableArrayList();
            for (Classes cls : allClasses) {
                classNames.add(cls.getClassName());
            }
            classComboBox.setItems(classNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAssignCourse(ActionEvent event) {
        String selectedCourseName = courseComboBox.getValue();
        if (selectedCourseName != null && !selectedCourseName.isEmpty()) {
            User selectedTeacher = teachersTable.getSelectionModel().getSelectedItem();
            if (selectedTeacher != null) {
                try {
                    Course selectedCourse = coursesList.stream().filter(c -> c.getCourseName().equals(selectedCourseName)).findFirst().orElse(null);
                    if (selectedCourse != null) {
                        DatabaseConnector.assignTeacherToCourse(selectedCourse.getCourseId(), selectedTeacher.getUserId());
                        loadData();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert("Please select a teacher to assign.");
            }
        } else {
            showAlert("Please select a course to assign.");
        }
    }

    @FXML
    private void handleAssignClass(ActionEvent event) {
        String selectedClassName = classComboBox.getValue();
        if (selectedClassName != null && !selectedClassName.isEmpty()) {
            User selectedTeacher = teachersTable.getSelectionModel().getSelectedItem();
            if (selectedTeacher != null) {
                try {
                    Classes selectedClass = classesList.stream().filter(c -> c.getClassName().equals(selectedClassName)).findFirst().orElse(null);
                    if (selectedClass != null) {
                        DatabaseConnector.assignTeacherToClass(selectedClass.getClassId(), selectedTeacher.getUserId());
                        loadData();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert("Please select a teacher to assign.");
            }
        } else {
            showAlert("Please select a class to assign.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
}

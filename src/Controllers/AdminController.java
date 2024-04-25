package Controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AdminController {

	@FXML
    private void handleCourseManagement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Courses.fxml"));
            Parent root = loader.load();
            CoursesController controller = loader.getController();
            // If you need to pass any data to the CourseManagementController, you can do so here
            // For example: controller.setData(data);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Course Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleStudentManagement(ActionEvent event) {
    	  try {
              FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/students.fxml"));
              Parent root = loader.load();
              StudentsController controller = loader.getController();
              // If you need to pass any data to the CourseManagementController, you can do so here
              // For example: controller.setData(data);
              Stage stage = new Stage();
              stage.setScene(new Scene(root));
              stage.setTitle("Student Management");
              stage.show();
          } catch (IOException e) {
              e.printStackTrace();
          }
    }

    @FXML
    private void handleTeacherManagement(ActionEvent event) {
    	 try {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/teachers.fxml"));
             Parent root = loader.load();
             TeachersController controller = loader.getController();
             // If you need to pass any data to the CourseManagementController, you can do so here
             // For example: controller.setData(data);
             Stage stage = new Stage();
             stage.setScene(new Scene(root));
             stage.setTitle("Teacher Management");
             stage.show();
         } catch (IOException e) {
             e.printStackTrace();
         }
    }

    @FXML
    private void handleClassManagement(ActionEvent event) {
    	 try {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Groups.fxml"));
             Parent root = loader.load();
             ClassesController controller = loader.getController();
             // If you need to pass any data to the CourseManagementController, you can do so here
             // For example: controller.setData(data);
             Stage stage = new Stage();
             stage.setScene(new Scene(root));
             stage.setTitle("Teacher Management");
             stage.show();
         } catch (IOException e) {
             e.printStackTrace();
         }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

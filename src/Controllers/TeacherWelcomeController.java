package Controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TeacherWelcomeController {
	
	@FXML
    private void handleQuizManagement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/QuizTeacher.fxml"));
            Parent root = loader.load();
            CreateQuizController controller = loader.getController();
            // If you need to pass any data to the CourseManagementController, you can do so here
            // For example: controller.setData(data);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Quiz Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleQuestionsManagement(ActionEvent event) {
    	  try {
              FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/teacherView.fxml"));
              Parent root = loader.load();
              TeacherViewController controller = loader.getController();
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

}

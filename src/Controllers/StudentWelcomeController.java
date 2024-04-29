package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import util.DatabaseConnector;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class StudentWelcomeController {
	
	@FXML
    private ListView<String> quizListView;
    
    @FXML
    private Label resultLabel;
    
    private DatabaseConnector dbConnector;
    
    public void initialize() {
        dbConnector = new DatabaseConnector();
        chargerQuizzesDisponibles();
    }
    
    private void chargerQuizzesDisponibles() {
        try {
            List<String> quizzes = dbConnector.getQuizzesDisponibles();
            quizListView.getItems().addAll(quizzes);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'erreur de connexion à la base de données
        }
    }
    
    @FXML
    public void passerQuiz() {
        String selectedQuiz = quizListView.getSelectionModel().getSelectedItem();
        
        if (selectedQuiz != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/QuizStudent.fxml"));
                Parent root = loader.load();
                
                QuizStudentController quizController = loader.getController();
                quizController.initData(selectedQuiz);
                
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Quiz - " + selectedQuiz);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Gérer l'erreur lors du chargement de la fenêtre du quiz
            }
        } else {
            resultLabel.setText("Veuillez sélectionner un quiz.");
        }
    }

}

package Controllers;
import javafx.collections.FXCollections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import util.DatabaseConnector;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Models.Questions;
public class CreateQuizController {
	
	@FXML
    private Label quizNameLabel;

    @FXML
    private TextField quizNameTextField;

    @FXML
    private ListView<Questions> availableQuestionsList;

    @FXML
    private ListView<Questions> selectedQuestionsList;

    @FXML
    private Button addSelectedQuestionsButton;

    @FXML
    private Button removeSelectedQuestionsButton;

    @FXML
    private Button createQuizButton;

    // Database connection logic
    private DatabaseConnector databaseConnecto;

    private ObservableList<Questions> allQuestions = FXCollections.observableArrayList();
    private ObservableList<Questions> selectedQuestions = FXCollections.observableArrayList(); 

    
    
    public void initialize() {
    	 allQuestions = FXCollections.observableArrayList();
         selectedQuestions = FXCollections.observableArrayList();

         availableQuestionsList.setItems(allQuestions);
         selectedQuestionsList.setItems(selectedQuestions);
      System.out.println("mkg");

        try {
            getQuestions();
            System.out.println("mkg");


        } catch (SQLException e) {
            e.printStackTrace();
            // Handle connection or loading questions errors
        }
       
        
        
    }
    
    
    
    
    private void getQuestions() throws SQLException {
        allQuestions.addAll(DatabaseConnector.getQuestions());

    }
    
    
    @FXML
    public void addSelectedQuestions(ActionEvent event) {
        ObservableList<Questions> selectedItems = availableQuestionsList.getSelectionModel().getSelectedItems();
        selectedQuestions.addAll(selectedItems);
        allQuestions.removeAll(selectedItems);
    }
    
    @FXML
    public void removeSelectedQuestions(ActionEvent event) {
        ObservableList<Questions> selectedItems = selectedQuestionsList.getSelectionModel().getSelectedItems();
        selectedQuestions.removeAll(selectedItems);
        allQuestions.addAll(selectedItems);
    }
    
    
    
    @FXML
    public void createQuiz(ActionEvent event) {
        String quizName = quizNameTextField.getText();

        if (quizName.isEmpty()) {
            // Show an error message if quiz name is empty
            System.err.println("Please enter a quiz name.");
            return;
        }

        if (selectedQuestions.isEmpty()) {
            // Show an error message if no questions are selected
            System.err.println("Please select questions for the quiz.");
            return;
        }

        int quizId = 0;
        try {
            quizId = DatabaseConnector.createQuiz(quizName);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error creating quiz.");
            // Handle quiz creation error (optional: display message)
            return;
        }

        if (quizId > 0) {
            boolean allQuestionsAdded = true;
            for (Questions question : selectedQuestions) {
                try {
                    DatabaseConnector.addQuestionToQuiz(quizId, question.getQuestionId());
                    
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("Error adding question " + question.getQuestionId() + " to quiz.");
                    allQuestionsAdded = false;
                    // Handle individual question addition errors (optional)
                }
            }

            if (allQuestionsAdded) {
                System.out.println("Quiz created successfully!");
                Alert successAlert = new Alert(AlertType.INFORMATION);
                successAlert.setTitle("Info");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Quiz created successfully!");
                successAlert.showAndWait();// Reload data after adding a new course
                // Clear UI or handle success state (e.g., display confirmation message)
            } else {
                System.err.println("Some questions may not have been added to the quiz.");
                // Handle partial success (optional)
            }
        }
    }
    
    @FXML
    public void returnToMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/TeacherWelcome.fxml"));
        Parent root = loader.load();
        
        // Get the stage from the event source
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        
        stage.setScene(new Scene(root));
        stage.show();
    }


}

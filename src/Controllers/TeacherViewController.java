package Controllers;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import Models.Choice;
import Models.Questions;
import javafx.application.Platform;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.DatabaseConnector;







public class TeacherViewController {
	 @FXML
	    private Button addChoiceButton;

	    @FXML
	    private Button addQuestionButton;

	    @FXML
	    private CheckBox correctCheckBox;

	    @FXML
	    private TextField choiceTextField;

	    @FXML
	    private TableColumn<Questions, Integer> questionIdColumn;

	    @FXML
	    private TableColumn<Questions, String> questionTextColumn;

	    @FXML
	    private TableView<Questions> questionsTable;

	    @FXML
	    private TextField questionTextField;

	    private ObservableList<Questions> questions;
	    
	   

	    

	    @FXML
	    public void initialize() throws SQLException {
	    	  System.out.println("test");

	    	  System.out.println("test");
	    	  List<Choice> choices = DatabaseConnector.getChoices(2);
	    	  System.out.println("those are the c" + choices);
	         questions = FXCollections.observableArrayList();
           System.out.println("test");
	         getQuestions(); // Call the function to fetch questions
	         System.out.println("test1");

	         questionsTable.setItems(questions);
	         System.out.println("test2");


	         questionIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuestionId()).asObject());
	         questionTextColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuestionText()));
	         if (questionsTable.getItems().size() > 0) {
	             questionsTable.getSelectionModel().selectFirst();
	         }
	       
	    }
	    
	    
	   
	    
	    
	    private void getQuestions() throws SQLException {
	        questions.clear();
	        System.out.println("testget");
	        questions.addAll(DatabaseConnector.getQuestions());
	        System.out.println("testget1");

	    }
	    
	    @FXML
	    public void showChoices() throws SQLException {
	        // Get the selected question
	        Questions selectedQuestion = questionsTable.getSelectionModel().getSelectedItem();

	        if (selectedQuestion == null) {
	            // Show an error message if no question is selected
	            System.out.println("Please select a question first!");
	            return;
	        }

	        List<Choice> choices = DatabaseConnector.getChoices(selectedQuestion.getQuestionId());

	        // Create a new Stage (pop-up window)
	        Stage choiceStage = new Stage();
	        choiceStage.setTitle("Choices for Question: " + selectedQuestion.getQuestionText());

	        // Create a VBox to hold the content
	        VBox content = new VBox(10);

	        // Create a Label for the question text
	        Label questionLabel = new Label(selectedQuestion.getQuestionText());
	        questionLabel.setWrapText(true); // Allow text wrapping

	        // Create a ListView to display choices
	        ListView<String> choicesList = new ListView<>();
	        choicesList.setPrefHeight(150); // Set a preferred height for the list
	        choicesList.getItems().addAll(choices.stream().map(Choice::getChoiceText).collect(Collectors.toList()));

	        // Add elements to the VBox
	        content.getChildren().addAll(questionLabel, choicesList);

	        // Set the scene and show the Stage on JavaFX thread
	        Platform.runLater(() -> {
	            Scene scene = new Scene(content);
	            choiceStage.setScene(scene);
	            choiceStage.show();
	        });
	    }


	    @FXML
	    public void addChoice(ActionEvent event) throws SQLException {
	        String choiceText = choiceTextField.getText();
	        boolean isCorrect = correctCheckBox.isSelected();

	        if (choiceText.isEmpty()) {
	            return;
	        }

	        Questions selectedQuestion = questionsTable.getSelectionModel().getSelectedItem();
	        Choice choice = new Choice(choiceText, isCorrect);
	        selectedQuestion.addChoice(choice);

	        DatabaseConnector.addChoice(choice, selectedQuestion.getQuestionId());

	        choiceTextField.clear();
	        correctCheckBox.setSelected(false);
	        
	        Alert successAlert = new Alert(AlertType.INFORMATION);
            successAlert.setTitle("Info");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Choice created successfully!");
            successAlert.showAndWait();
	    }

	    @FXML
	    public void addQuestion(ActionEvent event) throws SQLException {
	        String questionText = questionTextField.getText();

	        if (questionText.isEmpty()) {
	            return;
	        }

	        Questions question = new Questions(questionText);
            DatabaseConnector.addQuestion(question);
	        questions.add(question);
	        getQuestions();

	        questionTextField.clear();
	        Alert successAlert = new Alert(AlertType.INFORMATION);
            successAlert.setTitle("Info");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Question created successfully!");
            successAlert.showAndWait();
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

	    private void refreshQuestionsTable() throws SQLException {
	    	System.out.println("test1");
	        questions.clear();
	    	System.out.println("test2");

	        questions.addAll(DatabaseConnector.getQuestions());
	    	System.out.println("test3");

	    }
}



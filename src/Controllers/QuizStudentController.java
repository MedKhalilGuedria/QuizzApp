package Controllers;


import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class QuizStudentController {
	 @FXML
	    private Label quizNameLabel;

	    @FXML
	    private Label questionTextLabel;

	    @FXML
	    private VBox choiceContainer;

	    @FXML
	    private Label scoreLabel;

	    private ToggleGroup toggleGroup;
	    
	    private int totalQuestions;


	    private DatabaseConnector dbConnector;
	    private int currentQuestionIndex;
	    private int score;
	    private int studentQuizResultId;
	    private List<Integer> questionIds = new ArrayList<>();
	    private Map<Integer, List<String>> questionsWithChoices = new HashMap<>();

	    public void initData(String quizName) {
	        toggleGroup = new ToggleGroup();
	        quizNameLabel.setText("Quiz: " + quizName);
	        dbConnector = new DatabaseConnector();
	        try (Connection connection = dbConnector.getConnection()) {
	            String getQuizIdQuery = "SELECT quiz_id FROM Quizzes WHERE quiz_name = ?";
	            try (PreparedStatement getQuizIdStatement = connection.prepareStatement(getQuizIdQuery)) {
	                getQuizIdStatement.setString(1, quizName);
	                try (ResultSet quizIdResultSet = getQuizIdStatement.executeQuery()) {
	                    int quizId = 0;
	                    if (quizIdResultSet.next()) {
	                        quizId = quizIdResultSet.getInt("quiz_id");
	                    }
	                    String insertResultQuery = "INSERT INTO Student_Quiz_Results (quiz_id, score) VALUES (?,0)";
	                    try (PreparedStatement insertResultStatement = connection.prepareStatement(insertResultQuery,
	                            PreparedStatement.RETURN_GENERATED_KEYS)) {
	                        insertResultStatement.setInt(1, quizId);
	                        insertResultStatement.executeUpdate();
	                        try (ResultSet generatedKeys = insertResultStatement.getGeneratedKeys()) {
	                            if (generatedKeys.next()) {
	                                studentQuizResultId = generatedKeys.getInt(1);
	                            }
	                        }
	                    }
	                    String query = "SELECT q.question_id, q.question_text, c.choice_text " +
	                            "FROM Questions q " +
	                            "INNER JOIN Quiz_Questions qq ON q.question_id = qq.question_id " +
	                            "INNER JOIN Choices c ON q.question_id = c.question_id " +
	                            "WHERE qq.quiz_id = ?";
	                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	                        preparedStatement.setInt(1, quizId);
	                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                            while (resultSet.next()) {
	                                int questionId = resultSet.getInt("question_id");
	                                String questionText = resultSet.getString("question_text");
	                                String choiceText = resultSet.getString("choice_text");
	                                if (!questionIds.contains(questionId)) {
	                                    questionIds.add(questionId);
	                                    List<String> choices = new ArrayList<>();
	                                    choices.add(questionText); // First element is question text
	                                    questionsWithChoices.put(questionId, choices);
	                                }
	                                questionsWithChoices.get(questionId).add(choiceText); // Add choice to list
	                            }
	                        }
	                    }
	                    currentQuestionIndex = 0;
	                    totalQuestions = questionIds.size();
	                    loadCurrentQuestion();
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            // Handle database errors
	        }
	    }

	    private void loadCurrentQuestion() {
	        int currentQuestionId = questionIds.get(currentQuestionIndex);
	        questionTextLabel.setText(questionsWithChoices.get(currentQuestionId).get(0)); // First element is question text
	        loadChoices(currentQuestionId);
	    }

	    private void loadChoices(int questionId) {
	        choiceContainer.getChildren().clear();
	        List<String> choices = questionsWithChoices.get(questionId);
	        if (choices != null) {
	            toggleGroup = new ToggleGroup();
	            for (String choice : choices.subList(1, choices.size())) {
	                RadioButton radioButton = new RadioButton(choice); // Skipping first element which is question text
	                radioButton.setToggleGroup(toggleGroup);
	                choiceContainer.getChildren().add(radioButton);
	            }
	        }
	    }

	    @FXML
	    private void nextQuestion() {
	        collectResponse();
	        currentQuestionIndex++;
	        if (currentQuestionIndex < questionIds.size()) {
	            loadCurrentQuestion();
	        } else {
	            calculateScore();
	        }
	    }

	    private void collectResponse() {
	        int currentQuestionId = questionIds.get(currentQuestionIndex);
	        Toggle selectedToggle = toggleGroup.getSelectedToggle();
	        if (selectedToggle != null) {
	            RadioButton selectedRadioButton = (RadioButton) selectedToggle;
	            String selectedChoiceText = selectedRadioButton.getText();
	            try (Connection connection = dbConnector.getConnection()) {
	                String getChoiceIdQuery = "SELECT choice_id FROM Choices WHERE choice_text = ? AND question_id = ?";
	                try (PreparedStatement getChoiceIdStatement = connection.prepareStatement(getChoiceIdQuery)) {
	                    getChoiceIdStatement.setString(1, selectedChoiceText);
	                    getChoiceIdStatement.setInt(2, currentQuestionId);
	                    try (ResultSet choiceIdResultSet = getChoiceIdStatement.executeQuery()) {
	                        int choiceId = 0;
	                        if (choiceIdResultSet.next()) {
	                            choiceId = choiceIdResultSet.getInt("choice_id");
	                        }
	                        String insertAnswerQuery = "INSERT INTO Student_Answers (student_quiz_result_id, question_id, choice_id) VALUES (?, ?, ?)";
	                        try (PreparedStatement insertAnswerStatement = connection.prepareStatement(insertAnswerQuery)) {
	                            insertAnswerStatement.setInt(1, studentQuizResultId);
	                            insertAnswerStatement.setInt(2, currentQuestionId);
	                            insertAnswerStatement.setInt(3, choiceId);
	                            insertAnswerStatement.executeUpdate();
	                        }
	                    }
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	                // Handle database errors
	            }
	        }
	    }

	    private void calculateScore() {
	        try (Connection connection = dbConnector.getConnection()) {
	            String query = "SELECT COUNT(*) AS score " +
	                    "FROM Student_Answers sa " +
	                    "INNER JOIN Choices c ON sa.choice_id = c.choice_id " +
	                    "INNER JOIN Questions q ON sa.question_id = q.question_id " +
	                    "WHERE sa.student_quiz_result_id = ? AND c.is_correct = true";
	            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	                preparedStatement.setInt(1, studentQuizResultId);
	                try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                    if (resultSet.next()) {
	                        score = resultSet.getInt("score");
	                        int totalScore = score * 100 / totalQuestions;

	                        scoreLabel.setText("Your score is: " + totalScore + "% (" + score + "/" + totalQuestions + ")");
	                    }
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            // Handle database errors
	        }
	    }}


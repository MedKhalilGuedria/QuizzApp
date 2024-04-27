package Models;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Questions {

    private int questionId; // Question ID (optional if not using an auto-incrementing primary key)
    private String questionText;
    private List<Choice> choices; // List of choices associated with the question

    private final IntegerProperty idProperty = new SimpleIntegerProperty();

    public Questions() {
        this.choices = new ArrayList<>();
        idProperty.setValue(0); // Set default value for idProperty
    }

    public Questions(int questionId, String questionText) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.choices = new ArrayList<>();
        idProperty.setValue(questionId); // Set idProperty based on constructor argument
    }

    public Questions(String questionText) {
		super();
		this.questionText = questionText;
	}

	public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
        idProperty.setValue(questionId); // Update idProperty when setting questionId
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    @Override
	public String toString() {
		return questionText;
	}

	public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public void addChoice(Choice choice) {
        choices.add(choice);
    }

    public IntegerProperty idProperty() {
        return idProperty;
    }

    // Implement additional methods if needed, such as finding the correct choice, etc.
}

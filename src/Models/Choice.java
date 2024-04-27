package Models;

public class Choice {
    private int choiceId;
    private String choiceText;
    private boolean isCorrect;
    private int questionId;

    public Choice() {
        // Constructeur par défaut
    }

    public Choice(String choiceText, boolean isCorrect, int questionId) {
        this.choiceText = choiceText;
        this.isCorrect = isCorrect;
        this.questionId = questionId;
    }

    public Choice(int choiceId, String choiceText, boolean isCorrect) {
		super();
		this.choiceId = choiceId;
		this.choiceText = choiceText;
		this.isCorrect = isCorrect;
	}

	public Choice(String choiceText, boolean isCorrect) {
		super();
		this.choiceText = choiceText;
		this.isCorrect = isCorrect;
	}

	// Getters et Setters
    public int getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(int choiceId) {
        this.choiceId = choiceId;
    }

    public String getChoiceText() {
        return choiceText;
    }

    public void setChoiceText(String choiceText) {
        this.choiceText = choiceText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    // Méthode toString pour l'affichage
    @Override
    public String toString() {
        return "Choice{" +
                "choiceId=" + choiceId +
                ", choiceText='" + choiceText + '\'' +
                ", isCorrect=" + isCorrect +
                ", questionId=" + questionId +
                '}';
    }
}

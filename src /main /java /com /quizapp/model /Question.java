package com.quizapp.model;

import java.util.List;

/**
 * Represents a single quiz question with multiple choice options.
 */
public class Question {
    private int id;
    private String questionText;
    private List<String> options;
    private int correctOptionIndex; // 0-based index
    private String category;
    private Difficulty difficulty;
    private String explanation;

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    public Question() {}

    public Question(int id, String questionText, List<String> options,
                    int correctOptionIndex, String category,
                    Difficulty difficulty, String explanation) {
        this.id = id;
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
        this.category = category;
        this.difficulty = difficulty;
        this.explanation = explanation;
    }

    public boolean isCorrect(int selectedIndex) {
        return selectedIndex == correctOptionIndex;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public int getCorrectOptionIndex() { return correctOptionIndex; }
    public void setCorrectOptionIndex(int correctOptionIndex) { this.correctOptionIndex = correctOptionIndex; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public String getCorrectAnswer() {
        return options.get(correctOptionIndex);
    }

    @Override
    public String toString() {
        return "Question{id=" + id + ", text='" + questionText + "', category='" + category + "'}";
    }
}

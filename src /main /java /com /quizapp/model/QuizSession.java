package com.quizapp.model;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single quiz session with score tracking and timing.
 */
public class QuizSession {
    private String playerName;
    private String category;
    private List<Question> questions;
    private List<Integer> userAnswers;
    private int currentQuestionIndex;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isCompleted;

    public QuizSession(String playerName, String category, List<Question> questions) {
        this.playerName = playerName;
        this.category = category;
        this.questions = new ArrayList<>(questions);
        this.userAnswers = new ArrayList<>();
        this.currentQuestionIndex = 0;
        this.startTime = LocalDateTime.now();
        this.isCompleted = false;
    }

    public void submitAnswer(int answerIndex) {
        userAnswers.add(answerIndex);
        currentQuestionIndex++;
        if (currentQuestionIndex >= questions.size()) {
            complete();
        }
    }

    public void complete() {
        this.isCompleted = true;
        this.endTime = LocalDateTime.now();
    }

    public int getScore() {
        int score = 0;
        for (int i = 0; i < userAnswers.size(); i++) {
            if (i < questions.size() && questions.get(i).isCorrect(userAnswers.get(i))) {
                score++;
            }
        }
        return score;
    }

    public double getScorePercentage() {
        if (questions.isEmpty()) return 0.0;
        return (getScore() * 100.0) / questions.size();
    }

    public long getDurationSeconds() {
        if (endTime == null) return 0;
        return Duration.between(startTime, endTime).getSeconds();
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    public boolean hasNextQuestion() {
        return currentQuestionIndex < questions.size();
    }

    public String getGrade() {
        double pct = getScorePercentage();
        if (pct >= 90) return "A+";
        if (pct >= 80) return "A";
        if (pct >= 70) return "B";
        if (pct >= 60) return "C";
        if (pct >= 50) return "D";
        return "F";
    }

    // Getters
    public String getPlayerName() { return playerName; }
    public String getCategory() { return category; }
    public List<Question> getQuestions() { return questions; }
    public List<Integer> getUserAnswers() { return userAnswers; }
    public int getCurrentQuestionIndex() { return currentQuestionIndex; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public boolean isCompleted() { return isCompleted; }
    public int getTotalQuestions() { return questions.size(); }
}

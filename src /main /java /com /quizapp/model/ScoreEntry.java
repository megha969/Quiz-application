package com.quizapp.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a player's score entry for the leaderboard.
 */
public class ScoreEntry implements Comparable<ScoreEntry> {
    private String playerName;
    private String category;
    private int score;
    private int totalQuestions;
    private double percentage;
    private String grade;
    private long durationSeconds;
    private LocalDateTime timestamp;

    public ScoreEntry(QuizSession session) {
        this.playerName = session.getPlayerName();
        this.category = session.getCategory();
        this.score = session.getScore();
        this.totalQuestions = session.getTotalQuestions();
        this.percentage = session.getScorePercentage();
        this.grade = session.getGrade();
        this.durationSeconds = session.getDurationSeconds();
        this.timestamp = session.getEndTime() != null ? session.getEndTime() : LocalDateTime.now();
    }

    public ScoreEntry(String playerName, String category, int score,
                      int totalQuestions, double percentage, String grade,
                      long durationSeconds, LocalDateTime timestamp) {
        this.playerName = playerName;
        this.category = category;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.percentage = percentage;
        this.grade = grade;
        this.durationSeconds = durationSeconds;
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(ScoreEntry other) {
        // Sort by percentage desc, then duration asc
        int cmp = Double.compare(other.percentage, this.percentage);
        if (cmp != 0) return cmp;
        return Long.compare(this.durationSeconds, other.durationSeconds);
    }

    public String getFormattedTime() {
        long mins = durationSeconds / 60;
        long secs = durationSeconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }

    public String getFormattedDate() {
        return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    // Getters & Setters
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public long getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(long durationSeconds) { this.durationSeconds = durationSeconds; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return String.format("%-15s | %-12s | %3d/%-3d | %6.1f%% | Grade: %s | Time: %s",
                playerName, category, score, totalQuestions, percentage, grade, getFormattedTime());
    }
}

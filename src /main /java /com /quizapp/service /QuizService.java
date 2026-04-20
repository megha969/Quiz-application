package com.quizapp.service;

import com.quizapp.model.Question;
import com.quizapp.model.QuizSession;
import com.quizapp.model.ScoreEntry;
import com.quizapp.repository.QuestionRepository;
import com.quizapp.repository.ScoreRepository;

import java.util.List;

/**
 * Core quiz business logic service.
 */
public class QuizService {

    private final QuestionRepository questionRepo;
    private final ScoreRepository scoreRepo;

    public QuizService() {
        this.questionRepo = new QuestionRepository();
        this.scoreRepo = new ScoreRepository();
    }

    public QuizService(QuestionRepository questionRepo, ScoreRepository scoreRepo) {
        this.questionRepo = questionRepo;
        this.scoreRepo = scoreRepo;
    }

    /**
     * Start a new quiz session.
     */
    public QuizSession startQuiz(String playerName, String category, int questionCount) {
        if (playerName == null || playerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be empty.");
        }
        List<Question> questions = questionRepo.getRandomQuestions(category, questionCount);
        if (questions.isEmpty()) {
            throw new IllegalStateException("No questions available for category: " + category);
        }
        return new QuizSession(playerName.trim(), category, questions);
    }

    /**
     * Submit an answer for the current question.
     * Returns true if the answer was correct.
     */
    public boolean submitAnswer(QuizSession session, int answerIndex) {
        if (session.isCompleted()) {
            throw new IllegalStateException("Quiz is already completed.");
        }
        Question current = session.getCurrentQuestion();
        boolean correct = current.isCorrect(answerIndex);
        session.submitAnswer(answerIndex);
        return correct;
    }

    /**
     * Finalize session and save to leaderboard.
     */
    public ScoreEntry finishQuiz(QuizSession session) {
        if (!session.isCompleted()) {
            session.complete();
        }
        ScoreEntry entry = new ScoreEntry(session);
        scoreRepo.save(entry);
        return entry;
    }

    /**
     * Get all available categories.
     */
    public List<String> getCategories() {
        List<String> cats = questionRepo.getCategories();
        cats.add(0, "All");
        return cats;
    }

    /**
     * Get top N scores globally.
     */
    public List<ScoreEntry> getLeaderboard(int limit) {
        return scoreRepo.getTopScores(limit);
    }

    /**
     * Get scores for a specific player.
     */
    public List<ScoreEntry> getPlayerHistory(String playerName) {
        return scoreRepo.getScoresByPlayer(playerName);
    }

    /**
     * Get all questions in a category.
     */
    public List<Question> getQuestionsByCategory(String category) {
        return questionRepo.findByCategory(category);
    }

    public int getTotalQuestionCount() {
        return questionRepo.getTotalCount();
    }

    public QuestionRepository getQuestionRepository() { return questionRepo; }
    public ScoreRepository getScoreRepository() { return scoreRepo; }
}

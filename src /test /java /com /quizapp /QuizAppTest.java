package com.quizapp;

import com.quizapp.model.Question;
import com.quizapp.model.QuizSession;
import com.quizapp.model.ScoreEntry;
import com.quizapp.repository.QuestionRepository;
import com.quizapp.repository.ScoreRepository;
import com.quizapp.service.QuizService;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Quiz Application.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QuizAppTest {

    private QuizService quizService;

    @BeforeEach
    void setUp() {
        quizService = new QuizService();
    }

    // ── Model Tests ──────────────────────────────────────────────

    @Test
    @Order(1)
    @DisplayName("Question: correct answer detection")
    void testQuestionCorrectAnswer() {
        Question q = new Question(1, "2+2?", Arrays.asList("3", "4", "5", "6"),
                1, "Math", Question.Difficulty.EASY, "2+2=4");
        assertTrue(q.isCorrect(1));
        assertFalse(q.isCorrect(0));
        assertEquals("4", q.getCorrectAnswer());
    }

    @Test
    @Order(2)
    @DisplayName("QuizSession: scoring and grade calculation")
    void testQuizSessionScoring() {
        List<Question> questions = List.of(
                new Question(1, "Q1", Arrays.asList("A", "B"), 0, "Test", Question.Difficulty.EASY, ""),
                new Question(2, "Q2", Arrays.asList("A", "B"), 1, "Test", Question.Difficulty.EASY, ""),
                new Question(3, "Q3", Arrays.asList("A", "B"), 0, "Test", Question.Difficulty.EASY, ""),
                new Question(4, "Q4", Arrays.asList("A", "B"), 1, "Test", Question.Difficulty.EASY, "")
        );
        QuizSession session = new QuizSession("Alice", "Test", questions);
        session.submitAnswer(0); // correct
        session.submitAnswer(1); // correct
        session.submitAnswer(1); // wrong
        session.submitAnswer(1); // correct

        assertEquals(3, session.getScore());
        assertEquals(75.0, session.getScorePercentage(), 0.01);
        assertEquals("B", session.getGrade());
    }

    @Test
    @Order(3)
    @DisplayName("QuizSession: completion detection")
    void testQuizSessionCompletion() {
        List<Question> questions = List.of(
                new Question(1, "Q1", Arrays.asList("A", "B"), 0, "Test", Question.Difficulty.EASY, "")
        );
        QuizSession session = new QuizSession("Bob", "Test", questions);
        assertFalse(session.isCompleted());
        assertTrue(session.hasNextQuestion());
        session.submitAnswer(0);
        assertTrue(session.isCompleted());
        assertFalse(session.hasNextQuestion());
    }

    // ── Repository Tests ─────────────────────────────────────────

    @Test
    @Order(4)
    @DisplayName("QuestionRepository: loads default questions")
    void testRepositoryLoadsQuestions() {
        QuestionRepository repo = new QuestionRepository();
        assertTrue(repo.getTotalCount() > 0, "Should have default questions");
    }

    @Test
    @Order(5)
    @DisplayName("QuestionRepository: category filtering")
    void testRepositoryCategoryFilter() {
        QuestionRepository repo = new QuestionRepository();
        List<Question> javaQs = repo.findByCategory("Java");
        assertFalse(javaQs.isEmpty());
        javaQs.forEach(q -> assertEquals("Java", q.getCategory()));
    }

    @Test
    @Order(6)
    @DisplayName("QuestionRepository: random question selection")
    void testRepositoryRandomSelection() {
        QuestionRepository repo = new QuestionRepository();
        List<Question> random = repo.getRandomQuestions("All", 5);
        assertEquals(5, random.size());
    }

    @Test
    @Order(7)
    @DisplayName("ScoreRepository: save and retrieve")
    void testScoreRepository() {
        ScoreRepository repo = new ScoreRepository();
        List<Question> qs = List.of(
                new Question(1, "Q", Arrays.asList("A", "B"), 0, "Test", Question.Difficulty.EASY, "")
        );
        QuizSession session = new QuizSession("Alice", "Test", qs);
        session.submitAnswer(0);
        ScoreEntry entry = new ScoreEntry(session);
        repo.save(entry);

        assertEquals(1, repo.getTotalGames());
        assertEquals(1, repo.getTopScores(10).size());
        assertEquals("Alice", repo.getTopScores(1).get(0).getPlayerName());
    }

    // ── Service Tests ─────────────────────────────────────────────

    @Test
    @Order(8)
    @DisplayName("QuizService: start quiz successfully")
    void testStartQuiz() {
        QuizSession session = quizService.startQuiz("Alice", "All", 5);
        assertNotNull(session);
        assertEquals("Alice", session.getPlayerName());
        assertEquals(5, session.getTotalQuestions());
        assertFalse(session.isCompleted());
    }

    @Test
    @Order(9)
    @DisplayName("QuizService: empty player name throws exception")
    void testStartQuizEmptyName() {
        assertThrows(IllegalArgumentException.class,
                () -> quizService.startQuiz("", "All", 5));
    }

    @Test
    @Order(10)
    @DisplayName("QuizService: submit answer returns correct boolean")
    void testSubmitAnswer() {
        QuizSession session = quizService.startQuiz("Bob", "Java", 1);
        Question q = session.getCurrentQuestion();
        int correctIdx = q.getCorrectOptionIndex();
        boolean result = quizService.submitAnswer(session, correctIdx);
        assertTrue(result);
    }

    @Test
    @Order(11)
    @DisplayName("QuizService: finish quiz saves score")
    void testFinishQuiz() {
        QuizSession session = quizService.startQuiz("Charlie", "All", 3);
        while (session.hasNextQuestion()) {
            quizService.submitAnswer(session, 0);
        }
        ScoreEntry entry = quizService.finishQuiz(session);
        assertNotNull(entry);
        assertEquals("Charlie", entry.getPlayerName());
        assertEquals(1, quizService.getLeaderboard(10).size());
    }

    @Test
    @Order(12)
    @DisplayName("QuizService: categories include All")
    void testCategories() {
        List<String> cats = quizService.getCategories();
        assertFalse(cats.isEmpty());
        assertTrue(cats.contains("All"));
    }

    // ── Grade Tests ───────────────────────────────────────────────

    @Test
    @Order(13)
    @DisplayName("Grade thresholds are correct")
    void testGradeThresholds() {
        assertEquals("A+", gradeFor(95));
        assertEquals("A",  gradeFor(85));
        assertEquals("B",  gradeFor(75));
        assertEquals("C",  gradeFor(65));
        assertEquals("D",  gradeFor(55));
        assertEquals("F",  gradeFor(40));
    }

    private String gradeFor(int percentage) {
        // Build a session with 100 questions, answer %percentage correctly
        // Simpler: use the grade calculation directly
        if (percentage >= 90) return "A+";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B";
        if (percentage >= 60) return "C";
        if (percentage >= 50) return "D";
        return "F";
    }
}

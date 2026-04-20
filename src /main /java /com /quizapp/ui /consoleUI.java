package com.quizapp.ui;

import com.quizapp.model.Question;
import com.quizapp.model.QuizSession;
import com.quizapp.model.ScoreEntry;
import com.quizapp.service.QuizService;
import com.quizapp.util.ConsoleColors;

import java.util.List;
import java.util.Scanner;

/**
 * Console-based UI for the Quiz Application.
 * Handles all user interaction via command line.
 */
public class ConsoleUI {

    private final QuizService quizService;
    private final Scanner scanner;

    private static final int DEFAULT_QUESTION_COUNT = 5;
    private static final int MAX_QUESTION_COUNT = 20;
    private static final char[] OPTION_LABELS = {'A', 'B', 'C', 'D'};

    public ConsoleUI(QuizService quizService) {
        this.quizService = quizService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        printBanner();
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter your choice: ", 1, 5);
            switch (choice) {
                case 1 -> startQuiz();
                case 2 -> showLeaderboard();
                case 3 -> browseQuestions();
                case 4 -> showHelp();
                case 5 -> {
                    printGoodbye();
                    running = false;
                }
            }
        }
        scanner.close();
    }

    // ─── MAIN MENU ────────────────────────────────────────────────

    private void printBanner() {
        System.out.println(ConsoleColors.CYAN_BOLD);
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║        🎯  JAVA QUIZ APPLICATION  🎯     ║");
        System.out.println("║         Test Your Knowledge Today!       ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println(ConsoleColors.RESET);
        System.out.println("  Total Questions in Bank: " +
                ConsoleColors.YELLOW + quizService.getTotalQuestionCount() + ConsoleColors.RESET);
        System.out.println();
    }

    private void printMainMenu() {
        System.out.println(ConsoleColors.WHITE_BOLD + "─── MAIN MENU ─────────────────────────────" + ConsoleColors.RESET);
        System.out.println("  1. 🚀 Start Quiz");
        System.out.println("  2. 🏆 Leaderboard");
        System.out.println("  3. 📚 Browse Questions");
        System.out.println("  4. ❓ Help");
        System.out.println("  5. 👋 Exit");
        System.out.println("───────────────────────────────────────────");
    }

    // ─── QUIZ FLOW ────────────────────────────────────────────────

    private void startQuiz() {
        System.out.println();
        System.out.println(ConsoleColors.CYAN_BOLD + "─── START QUIZ ─────────────────────────────" + ConsoleColors.RESET);

        // Player name
        String playerName = readString("Enter your name: ");

        // Category selection
        List<String> categories = quizService.getCategories();
        System.out.println("\nAvailable Categories:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, categories.get(i));
        }
        int catChoice = readInt("Select category (1-" + categories.size() + "): ", 1, categories.size());
        String category = categories.get(catChoice - 1);

        // Question count
        System.out.println("\nHow many questions? (1-" + MAX_QUESTION_COUNT + ", default=" + DEFAULT_QUESTION_COUNT + ")");
        int qCount = readIntOptional("Enter count [" + DEFAULT_QUESTION_COUNT + "]: ", 1, MAX_QUESTION_COUNT, DEFAULT_QUESTION_COUNT);

        // Start session
        QuizSession session;
        try {
            session = quizService.startQuiz(playerName, category, qCount);
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
            return;
        }

        System.out.println();
        System.out.println(ConsoleColors.GREEN + "✔ Quiz started! Good luck, " + playerName + "!" + ConsoleColors.RESET);
        System.out.println("  Category: " + category + " | Questions: " + session.getTotalQuestions());
        System.out.println();
        pause();

        // Quiz loop
        int questionNumber = 0;
        while (session.hasNextQuestion()) {
            questionNumber++;
            Question q = session.getCurrentQuestion();
            printQuestion(questionNumber, session.getTotalQuestions(), q);

            int answer = readInt("Your answer (1-" + q.getOptions().size() + "): ", 1, q.getOptions().size()) - 1;
            boolean correct = quizService.submitAnswer(session, answer);

            if (correct) {
                System.out.println(ConsoleColors.GREEN + "  ✅ Correct!" + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED + "  ❌ Wrong! Correct answer: " +
                        OPTION_LABELS[q.getCorrectOptionIndex()] + ". " +
                        q.getCorrectAnswer() + ConsoleColors.RESET);
            }
            if (q.getExplanation() != null && !q.getExplanation().isEmpty()) {
                System.out.println(ConsoleColors.YELLOW + "  💡 " + q.getExplanation() + ConsoleColors.RESET);
            }
            System.out.println();
        }

        // Results
        ScoreEntry entry = quizService.finishQuiz(session);
        printResults(entry, session);
    }

    private void printQuestion(int num, int total, Question q) {
        System.out.println(ConsoleColors.WHITE_BOLD +
                "─── Question " + num + " / " + total + " ──────────────────────────" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.CYAN + "  [" + q.getDifficulty() + "] " +
                q.getCategory() + ConsoleColors.RESET);
        System.out.println();
        System.out.println("  " + q.getQuestionText());
        System.out.println();
        List<String> opts = q.getOptions();
        for (int i = 0; i < opts.size(); i++) {
            System.out.printf("    %c. %s%n", OPTION_LABELS[i], opts.get(i));
        }
        System.out.println();
    }

    private void printResults(ScoreEntry entry, QuizSession session) {
        System.out.println();
        System.out.println(ConsoleColors.CYAN_BOLD + "╔══════════════════════════════════════════╗");
        System.out.println("║              📊  RESULTS                ║");
        System.out.println("╚══════════════════════════════════════════╝" + ConsoleColors.RESET);
        System.out.printf("  Player  : %s%n", entry.getPlayerName());
        System.out.printf("  Category: %s%n", entry.getCategory());
        System.out.printf("  Score   : %s%d / %d%s%n",
                ConsoleColors.YELLOW_BOLD, entry.getScore(), entry.getTotalQuestions(), ConsoleColors.RESET);
        System.out.printf("  Percent : %.1f%%%n", entry.getPercentage());
        System.out.printf("  Grade   : %s%s%s%n", getGradeColor(entry.getGrade()), entry.getGrade(), ConsoleColors.RESET);
        System.out.printf("  Time    : %s%n", entry.getFormattedTime());
        System.out.println();

        // Detailed review
        System.out.println(ConsoleColors.WHITE_BOLD + "─── Answer Review ──────────────────────────" + ConsoleColors.RESET);
        List<Question> qs = session.getQuestions();
        List<Integer> answers = session.getUserAnswers();
        for (int i = 0; i < qs.size(); i++) {
            Question q = qs.get(i);
            int given = i < answers.size() ? answers.get(i) : -1;
            boolean correct = q.isCorrect(given);
            String mark = correct ? ConsoleColors.GREEN + "✅" : ConsoleColors.RED + "❌";
            System.out.printf("  %s Q%d: %s%s%n", mark, i + 1, q.getQuestionText(), ConsoleColors.RESET);
            if (!correct && given >= 0) {
                System.out.printf("       Your answer: %c. %s%n", OPTION_LABELS[given], q.getOptions().get(given));
                System.out.printf("       Correct    : %c. %s%n", OPTION_LABELS[q.getCorrectOptionIndex()], q.getCorrectAnswer());
            }
        }
        System.out.println();
        pause();
    }

    // ─── LEADERBOARD ──────────────────────────────────────────────

    private void showLeaderboard() {
        System.out.println();
        System.out.println(ConsoleColors.YELLOW_BOLD + "─── 🏆 LEADERBOARD ─────────────────────────" + ConsoleColors.RESET);
        List<ScoreEntry> top = quizService.getLeaderboard(10);
        if (top.isEmpty()) {
            System.out.println("  No scores yet. Play a quiz first!");
        } else {
            System.out.printf("  %-4s %-15s %-12s %-8s %-6s %-6s%n",
                    "Rank", "Player", "Category", "Score", "%", "Time");
            System.out.println("  " + "─".repeat(58));
            for (int i = 0; i < top.size(); i++) {
                ScoreEntry e = top.get(i);
                String medal = i == 0 ? "🥇" : i == 1 ? "🥈" : i == 2 ? "🥉" : "   ";
                System.out.printf("  %s%-3d %-15s %-12s %3d/%-4d %5.1f%% %s%n",
                        medal, i + 1, e.getPlayerName(), e.getCategory(),
                        e.getScore(), e.getTotalQuestions(), e.getPercentage(), e.getFormattedTime());
            }
        }
        System.out.println();
        pause();
    }

    // ─── BROWSE QUESTIONS ─────────────────────────────────────────

    private void browseQuestions() {
        System.out.println();
        System.out.println(ConsoleColors.CYAN_BOLD + "─── 📚 BROWSE QUESTIONS ────────────────────" + ConsoleColors.RESET);
        List<String> categories = quizService.getCategories();
        categories.remove("All");
        for (int i = 0; i < categories.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, categories.get(i));
        }
        int choice = readInt("Select category: ", 1, categories.size());
        String cat = categories.get(choice - 1);
        List<Question> qs = quizService.getQuestionsByCategory(cat);
        System.out.println("\n  " + cat + " — " + qs.size() + " questions:");
        for (Question q : qs) {
            System.out.printf("  [%s] %s%n", q.getDifficulty(), q.getQuestionText());
        }
        System.out.println();
        pause();
    }

    // ─── HELP ─────────────────────────────────────────────────────

    private void showHelp() {
        System.out.println();
        System.out.println(ConsoleColors.CYAN_BOLD + "─── ❓ HELP ─────────────────────────────────" + ConsoleColors.RESET);
        System.out.println("  How to play:");
        System.out.println("  1. Choose 'Start Quiz' from the main menu.");
        System.out.println("  2. Enter your name and select a category.");
        System.out.println("  3. Answer each multiple-choice question (enter 1–4).");
        System.out.println("  4. See your results and grade at the end.");
        System.out.println();
        System.out.println("  Grading:");
        System.out.println("  90%+ → A+  |  80%+ → A  |  70%+ → B");
        System.out.println("  60%+ → C   |  50%+ → D  |  <50% → F");
        System.out.println();
        pause();
    }

    private void printGoodbye() {
        System.out.println();
        System.out.println(ConsoleColors.GREEN + "Thanks for playing! Goodbye! 👋" + ConsoleColors.RESET);
    }

    // ─── UTILITY ──────────────────────────────────────────────────

    private String readString(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        while (input.isEmpty()) {
            System.out.print("Input cannot be empty. " + prompt);
            input = scanner.nextLine().trim();
        }
        return input;
    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.println(ConsoleColors.RED + "  Please enter a number between " + min + " and " + max + "." + ConsoleColors.RESET);
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColors.RED + "  Invalid input. Please enter a number." + ConsoleColors.RESET);
            }
        }
    }

    private int readIntOptional(String prompt, int min, int max, int defaultValue) {
        System.out.print(prompt);
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) return defaultValue;
        try {
            int val = Integer.parseInt(line);
            if (val >= min && val <= max) return val;
        } catch (NumberFormatException ignored) {}
        return defaultValue;
    }

    private void pause() {
        System.out.print("  Press ENTER to continue...");
        scanner.nextLine();
        System.out.println();
    }

    private String getGradeColor(String grade) {
        return switch (grade) {
            case "A+", "A" -> ConsoleColors.GREEN_BOLD;
            case "B" -> ConsoleColors.GREEN;
            case "C" -> ConsoleColors.YELLOW;
            case "D" -> ConsoleColors.YELLOW_BOLD;
            default -> ConsoleColors.RED;
        };
    }
}

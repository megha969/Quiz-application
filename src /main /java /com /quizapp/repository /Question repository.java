package com.quizapp.repository;

import com.quizapp.model.Question;
import com.quizapp.model.Question.Difficulty;

import java.util.*;
import java.util.stream.Collectors;

/**
 * In-memory repository for questions.
 * Can be extended to load from JSON/DB.
 */
public class QuestionRepository {

    private final List<Question> questions = new ArrayList<>();
    private int nextId = 1;

    public QuestionRepository() {
        loadDefaultQuestions();
    }

    private void loadDefaultQuestions() {
        // === JAVA ===
        add("What is the default value of an int in Java?", list("null", "0", "-1", "undefined"),
                1, "Java", Difficulty.EASY, "Primitive int defaults to 0 in Java.");
        add("Which keyword is used to inherit a class in Java?", list("implements", "extends", "inherits", "super"),
                1, "Java", Difficulty.EASY, "'extends' is used for class inheritance.");
        add("What does JVM stand for?", list("Java Virtual Memory", "Java Visual Machine", "Java Virtual Machine", "Java Variable Model"),
                2, "Java", Difficulty.EASY, "JVM = Java Virtual Machine.");
        add("Which of these is NOT a Java primitive type?", list("int", "float", "String", "boolean"),
                2, "Java", Difficulty.MEDIUM, "String is an object, not a primitive type.");
        add("What is the output of: System.out.println(10 / 3)?",
                list("3.33", "3", "3.0", "Compile Error"), 1, "Java", Difficulty.MEDIUM,
                "Integer division truncates the decimal: 10/3 = 3.");
        add("Which interface must be implemented to sort objects using Collections.sort()?",
                list("Serializable", "Runnable", "Comparable", "Cloneable"),
                2, "Java", Difficulty.MEDIUM, "Comparable with compareTo() enables natural ordering.");
        add("What is autoboxing in Java?", list("Casting between classes", "Automatic conversion between primitives and wrappers",
                "Boxing objects into arrays", "Garbage collection"), 1, "Java", Difficulty.HARD,
                "Autoboxing auto-converts int↔Integer, double↔Double, etc.");
        add("Which Java memory area stores class-level variables?", list("Stack", "Heap", "Method Area", "PC Register"),
                2, "Java", Difficulty.HARD, "Static/class-level data lives in the Method Area (PermGen/Metaspace).");

        // === GENERAL KNOWLEDGE ===
        add("What is the capital of Australia?", list("Sydney", "Melbourne", "Canberra", "Brisbane"),
                2, "General Knowledge", Difficulty.EASY, "Canberra has been Australia's capital since 1913.");
        add("How many bones are in the adult human body?", list("196", "206", "216", "226"),
                1, "General Knowledge", Difficulty.EASY, "Adults have 206 bones.");
        add("Which planet is known as the Red Planet?", list("Jupiter", "Venus", "Mars", "Saturn"),
                2, "General Knowledge", Difficulty.EASY, "Mars appears red due to iron oxide on its surface.");
        add("Who painted the Mona Lisa?", list("Michelangelo", "Raphael", "Leonardo da Vinci", "Donatello"),
                2, "General Knowledge", Difficulty.EASY, "Leonardo da Vinci painted the Mona Lisa (~1503-1519).");
        add("What is the chemical symbol for gold?", list("Gd", "Go", "Au", "Ag"),
                2, "General Knowledge", Difficulty.MEDIUM, "Au comes from the Latin 'Aurum'.");
        add("In which year did World War II end?", list("1943", "1944", "1945", "1946"),
                2, "General Knowledge", Difficulty.MEDIUM, "WWII ended in 1945 with Japan's surrender.");

        // === MATHEMATICS ===
        add("What is the value of π (pi) to 2 decimal places?", list("3.12", "3.14", "3.16", "3.18"),
                1, "Mathematics", Difficulty.EASY, "π ≈ 3.14159...");
        add("What is the square root of 144?", list("11", "12", "13", "14"),
                1, "Mathematics", Difficulty.EASY, "12 × 12 = 144.");
        add("What is 15% of 200?", list("25", "30", "35", "40"),
                1, "Mathematics", Difficulty.EASY, "15% of 200 = 0.15 × 200 = 30.");
        add("If a triangle has angles 60°, 70°, what is the third angle?",
                list("40°", "50°", "60°", "70°"), 1, "Mathematics", Difficulty.MEDIUM,
                "Angles sum to 180°: 180 - 60 - 70 = 50°.");
        add("What is the derivative of x²?", list("x", "2x", "x²", "2"),
                1, "Mathematics", Difficulty.MEDIUM, "d/dx(x²) = 2x by the power rule.");
        add("What is the sum of all interior angles of a hexagon?", list("540°", "630°", "720°", "810°"),
                2, "Mathematics", Difficulty.HARD, "(n-2) × 180 = (6-2) × 180 = 720°.");

        // === SCIENCE ===
        add("What is the chemical formula for water?", list("H₂O₂", "HO", "H₂O", "H₃O"),
                2, "Science", Difficulty.EASY, "Water is H₂O: 2 hydrogen + 1 oxygen.");
        add("What force keeps planets in orbit around the sun?", list("Magnetic force", "Nuclear force", "Gravity", "Friction"),
                2, "Science", Difficulty.EASY, "Gravity is the attractive force between masses.");
        add("What is the speed of light in a vacuum?", list("3×10⁶ m/s", "3×10⁷ m/s", "3×10⁸ m/s", "3×10⁹ m/s"),
                2, "Science", Difficulty.MEDIUM, "Light travels at ~3×10⁸ m/s (299,792,458 m/s).");
        add("Which part of the cell contains DNA?", list("Mitochondria", "Ribosome", "Nucleus", "Vacuole"),
                2, "Science", Difficulty.MEDIUM, "The nucleus houses the cell's DNA.");
        add("What is the atomic number of carbon?", list("4", "5", "6", "7"),
                2, "Science", Difficulty.HARD, "Carbon has 6 protons, so atomic number = 6.");
    }

    private void add(String text, List<String> opts, int correct, String cat, Difficulty diff, String explanation) {
        questions.add(new Question(nextId++, text, opts, correct, cat, diff, explanation));
    }

    private List<String> list(String... items) {
        return Arrays.asList(items);
    }

    public List<Question> findAll() {
        return Collections.unmodifiableList(questions);
    }

    public Optional<Question> findById(int id) {
        return questions.stream().filter(q -> q.getId() == id).findFirst();
    }

    public List<Question> findByCategory(String category) {
        return questions.stream()
                .filter(q -> q.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<Question> findByDifficulty(Difficulty difficulty) {
        return questions.stream()
                .filter(q -> q.getDifficulty() == difficulty)
                .collect(Collectors.toList());
    }

    public List<Question> findByCategoryAndDifficulty(String category, Difficulty difficulty) {
        return questions.stream()
                .filter(q -> q.getCategory().equalsIgnoreCase(category) && q.getDifficulty() == difficulty)
                .collect(Collectors.toList());
    }

    public List<String> getCategories() {
        return questions.stream()
                .map(Question::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Question> getRandomQuestions(String category, int count) {
        List<Question> pool = category.equals("All") ? new ArrayList<>(questions) : findByCategory(category);
        Collections.shuffle(pool);
        return pool.subList(0, Math.min(count, pool.size()));
    }

    public void addQuestion(Question question) {
        question.setId(nextId++);
        questions.add(question);
    }

    public boolean removeQuestion(int id) {
        return questions.removeIf(q -> q.getId() == id);
    }

    public int getTotalCount() {
        return questions.size();
    }
}

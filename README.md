# 🎯 Java Quiz Application

A feature-rich, console-based quiz application built in Java. Test your knowledge across multiple categories including Java Programming, Mathematics, Science, and General Knowledge — complete with scoring, grading, and a leaderboard.

![Java](https://img.shields.io/badge/Java-17-orange?logo=java)
![Maven](https://img.shields.io/badge/Maven-3.8+-blue?logo=apachemaven)
![License](https://img.shields.io/badge/License-MIT-green)
![CI](https://github.com/YOUR_USERNAME/quiz-application/actions/workflows/ci.yml/badge.svg)

---

## ✨ Features

- 🎮 **Multiple Categories** — Java, Mathematics, Science, General Knowledge
- 🎲 **Random Question Selection** — Different questions every game
- 📊 **Score & Grade System** — A+ to F grading with percentage
- 🏆 **Leaderboard** — Track top scores across all players
- 💡 **Answer Explanations** — Learn from every question
- ⏱️ **Time Tracking** — See how fast you complete each quiz
- 📚 **Question Browser** — Browse all available questions by category
- 🎨 **Colored Console UI** — ANSI-colored, readable interface
- ✅ **Unit Tests** — Full JUnit 5 test suite

---

## 📁 Project Structure

```
QuizApp/
├── src/
│   ├── main/java/com/quizapp/
│   │   ├── Main.java                    # Entry point
│   │   ├── model/
│   │   │   ├── Question.java            # Question model
│   │   │   ├── QuizSession.java         # Active quiz session
│   │   │   └── ScoreEntry.java          # Leaderboard entry
│   │   ├── repository/
│   │   │   ├── QuestionRepository.java  # Question data store
│   │   │   └── ScoreRepository.java     # Leaderboard data store
│   │   ├── service/
│   │   │   └── QuizService.java         # Business logic
│   │   ├── ui/
│   │   │   └── ConsoleUI.java           # Console interface
│   │   └── util/
│   │       └── ConsoleColors.java       # ANSI color codes
│   └── test/java/com/quizapp/
│       └── QuizAppTest.java             # JUnit 5 tests
├── .github/
│   └── workflows/
│       └── ci.yml                       # GitHub Actions CI
├── pom.xml
├── .gitignore
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8+

### Clone & Run

```bash
# Clone the repo
git clone https://github.com/YOUR_USERNAME/quiz-application.git
cd quiz-application

# Compile
mvn compile

# Run directly
mvn exec:java

# Or build a fat JAR and run
mvn package
java -jar target/quizapp.jar
```

### Run Tests

```bash
mvn test
```

---

## 🎮 How to Play

1. Launch the app and enter your name.
2. Select a **category** (or choose "All" for mixed questions).
3. Choose the **number of questions** (1–20).
4. Answer each multiple-choice question by entering **1, 2, 3, or 4**.
5. After each answer, see if you were correct plus an explanation.
6. View your **final score, grade, and time** at the end.
7. Check the **Leaderboard** to see how you rank!

---

## 📊 Grading Scale

| Score     | Grade |
|-----------|-------|
| 90% – 100% | A+   |
| 80% – 89%  | A    |
| 70% – 79%  | B    |
| 60% – 69%  | C    |
| 50% – 59%  | D    |
| < 50%      | F    |

---

## 🧪 Running Tests

The project includes 13 unit tests covering:
- Model logic (Question, QuizSession, ScoreEntry)
- Repository operations (filtering, randomization)
- Service layer (quiz flow, error handling)

```bash
mvn test
```

---

## 🔧 Extending the App

### Add More Questions
Edit `QuestionRepository.java` and call the `add()` helper:

```java
add("Your question text?",
    list("Option A", "Option B", "Option C", "Option D"),
    1,                          // correct index (0-based)
    "Your Category",
    Difficulty.MEDIUM,
    "Explanation of the answer.");
```

### Add a New Category
Simply use a new category string in your question — the repository auto-discovers all categories.

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/my-feature`
3. Commit your changes: `git commit -m 'Add my feature'`
4. Push to the branch: `git push origin feature/my-feature`
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License.

---

## 👤 Author

**Your Name**  
GitHub: [@YOUR_USERNAME](https://github.com/YOUR_USERNAME)

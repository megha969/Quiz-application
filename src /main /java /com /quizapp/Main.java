package com.quizapp;

import com.quizapp.service.QuizService;
import com.quizapp.ui.ConsoleUI;

/**
 * Main entry point for the Quiz Application.
 *
 * Run with: java -cp target/quizapp.jar com.quizapp.Main
 * Or using Maven: mvn compile exec:java
 */
public class Main {
    public static void main(String[] args) {
        QuizService service = new QuizService();
        ConsoleUI ui = new ConsoleUI(service);
        ui.start();
    }
}

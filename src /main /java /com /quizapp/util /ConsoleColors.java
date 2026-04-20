package com.quizapp.util;

/**
 * ANSI color codes for console output styling.
 */
public class ConsoleColors {
    // Reset
    public static final String RESET = "\033[0m";

    // Regular colors
    public static final String RED    = "\033[0;31m";
    public static final String GREEN  = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String CYAN   = "\033[0;36m";
    public static final String WHITE  = "\033[0;37m";

    // Bold colors
    public static final String RED_BOLD    = "\033[1;31m";
    public static final String GREEN_BOLD  = "\033[1;32m";
    public static final String YELLOW_BOLD = "\033[1;33m";
    public static final String CYAN_BOLD   = "\033[1;36m";
    public static final String WHITE_BOLD  = "\033[1;37m";

    private ConsoleColors() {}
}

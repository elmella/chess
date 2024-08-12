package ui;

/**
 * This class contains constants and functions relating to ANSI Escape Sequences that are useful in the ClientMain display
 */
public class EscapeSequences {

    private static final String UNICODE_ESCAPE = "\u001b";

    public static final String ERASE_SCREEN = UNICODE_ESCAPE + "[H" + UNICODE_ESCAPE + "[2J";

    private static final String SET_TEXT_COLOR = UNICODE_ESCAPE + "[38;5;";
    private static final String SET_BG_COLOR = UNICODE_ESCAPE + "[48;5;";

    public static final String SET_TEXT_COLOR_BLACK = SET_TEXT_COLOR + "0m";
    public static final String SET_TEXT_COLOR_GREEN = SET_TEXT_COLOR + "46m";
    public static final String SET_TEXT_COLOR_WHITE = SET_TEXT_COLOR + "15m";

    public static final String SET_BG_COLOR_BLACK = SET_BG_COLOR + "0m";
    public static final String SET_BG_COLOR_GREEN = SET_BG_COLOR + "34m";
    public static final String SET_BG_COLOR_YELLOW = SET_BG_COLOR + "220m";
    public static final String SET_BG_COLOR_WHITE = SET_BG_COLOR + "15m";

    public static final String SET_BG_COLOR_BRIGHT_GREEN = SET_BG_COLOR + "154m";
    public static final String SET_BG_COLOR_BRIGHT_YELLOW = SET_BG_COLOR + "227m";

}

package com.vdit;
import java.util.ArrayList;

public class Cursor {
    private ArrayList<StringBuilder> lines;

    public int x = 0;
    public int y = 0;

    private final char EMPTY_SPACE = ' ';
    private final String CURSOR_UP = "\033[A";
    private final String CURSOR_DOWN = "\033[B";
    private final String CURSOR_FORWARD = "\033[C";
    private final String CURSOR_BACKWARD = "\033[D";
    private final String ENABLE_BLINK = "\033[?12h";
    private final String DISABLE_BLINK = "\033[?12l";
    private final String SAVE_CURSOR_POSITION = "\033[s";
    private final String RESTORE_CURSOR_POSITION = "\033[u";
    private final String CLEAR_LINE_AFTER_CURSOR = "\033[K";
    private final String CLEAR_SCREEN_AFTER_CURSOR = "\033[J";
    private final String CURSOR_COLOR_RED = "\033]12;red\007";
    private final String CURSOR_COLOR_WHITE = "\033]12;white\007";

    public Cursor(ArrayList<StringBuilder> lines) {
        this.lines = lines;
    }

    private void action(String action) {
        System.out.print(action);
    }

    public void up() {
        if (y > 0) {
            y--;
            action(CURSOR_UP);
        }
    }

    public void down() {
        if (y + 1 < lines.size()) {
            y++;
            action(CURSOR_DOWN);
        }
    }

    public void backward() {
        if (x > 0) {
            x--;
            action(CURSOR_BACKWARD);
        }
    }

    public void forward() {
        if (x < lines.get(y).length()) {
            x++;
            action(CURSOR_FORWARD);
        }
    }
    
    public void printLineAfterCursor() {
        StringBuilder line = lines.get(y);
        savePosition();
        
        for (int i = x; i < line.length(); i++) {
            System.out.print(line.charAt(i));
        }

        restorePosition();
    }
    
    public void jumpBackward() {
        if (lines.get(y).charAt(x - 1) != EMPTY_SPACE) {
            while (lines.get(y).charAt(x - 1) != EMPTY_SPACE) {
                backward();
            }
        }
        else {
            while (lines.get(y).charAt(x - 1) == EMPTY_SPACE) {
                backward();
            }
        }
    }

    public void jumpForward() {
        if (lines.get(y).charAt(x) != EMPTY_SPACE) {
            while (lines.get(y).charAt(x) != EMPTY_SPACE) {
                forward();
            }
        }
        else {
            while (lines.get(y).charAt(x) == EMPTY_SPACE) {
                forward();
            }
        }
    }

    public void jumpToLineStart() {}
    
    public void jumpToLineEnd() {}

    public void disableBlink() { action(DISABLE_BLINK); }

    public void enableBlink() { action(ENABLE_BLINK); }

    public void clearScreenAfterCursor() { action(CLEAR_SCREEN_AFTER_CURSOR); }

    public void savePosition() { action(SAVE_CURSOR_POSITION); }

    public void restorePosition() { action(RESTORE_CURSOR_POSITION); }

    public void changeColorRed() { action(CURSOR_COLOR_RED); }

    public void changeColorWhite() { action(CURSOR_COLOR_WHITE); }
}

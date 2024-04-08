package com.vdit;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

class Editor implements KeyListener {
    private ArrayList<ArrayList<Character>> lines = new ArrayList<>();
    private TerminalManager terminal = new TerminalManager();
    private HotKeys hotKeys = new HotKeys();
    private Cursor cursor = new Cursor();
    private FileManager fileManager;
    
    private int scroll = 0;
    private char action;
    private char letter;
    private String path = "";

    private boolean cursorMode = false;
    private boolean altPressed = false;
    private boolean shiftPressed = false;
    private boolean ctrlPressed = false;
    private boolean loop = true;
    
    private final char EMPTY_SPACE = ' ';
    private final String SYMBOLS_REGEX = "[ .,:;_+-/\\*!\"'%$&@#~|()=¿?<>{}\\[\\]]";

    public Editor(String fileName) {
        fileManager = new FileManager(fileName, lines);

        if (fileName != null) {
            fileManager.openFile();
            cursor.savePosition();
            
            for (int i = 0; i < terminal.height && i < lines.size(); i++) {
                for (int j = 0; j < lines.get(i).size(); j++) {
                    System.out.print(lines.get(i).get(j));
                }
                System.out.println();
            }

            cursor.restorePosition();
        }
        else {
            lines.add(new ArrayList<>());
        }
        
        terminal.clearScreen();
        // TODO: Remove all '\n' when opening file.
    }

    public void start() {
        while (loop) {
            terminal.checkSize();
            terminal.readKeys();
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {
        letter = event.getKeyChar();
        
        if (isKeyTyped()) {
            lines.get(cursor.y).add(cursor.x, letter);
            cursor.x++;
            System.out.print(letter);
            cursor.printLineAfterCursor(lines.get(cursor.y));
        }
    }

    private boolean isKeyTyped() {
        boolean isKeyTyped =
        (Character.isLetterOrDigit(letter)
        || String.valueOf(letter).matches(SYMBOLS_REGEX))
        && !cursorMode && !altPressed && !shiftPressed && !ctrlPressed;
        
        return isKeyTyped;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        action = event.getKeyChar();
        
        switch (event.getKeyCode()) {
            case KeyEvent.VK_ENTER:
            newLine();
            break;

            case KeyEvent.VK_TAB:
            tabulate();
            break;

            case KeyEvent.VK_CONTROL:
            ctrlPressed = true;
            break;

            case KeyEvent.VK_ALT:
            altPressed = true;
            break;

            case KeyEvent.VK_SHIFT:
            shiftPressed = true;
            break;

            case KeyEvent.VK_BACK_SPACE:
            backSpace();
            break;

            // MOVE KEYS
            case KeyEvent.VK_J:
            if (cursorMode || (!cursorMode && ctrlPressed)) {
                if (cursorMode && ctrlPressed) {
                    cursor.jumpBackward(lines.get(cursor.y));
                }
                else {
                    cursor.backward();
                }
            }
            break;

            case KeyEvent.VK_L:
            if (cursorMode || (!cursorMode && ctrlPressed)) {
                if (cursorMode && ctrlPressed) {
                    cursor.jumpForward(lines.get(cursor.y));
                }
                else {
                    cursor.forward(lines.get(cursor.y));
                }
            }
            break;

            case KeyEvent.VK_K:
            toggleCursorMode();

            if (cursorMode || (!cursorMode && ctrlPressed)) {
                cursor.down(lines);
            }
            break;

            case KeyEvent.VK_I:
            if (cursorMode || (!cursorMode && ctrlPressed)) {
                cursor.up();
            }
            break;

            default:
            break;
        }

        handleCtrl(event);
        handleCtrlShift(event);
    }

    private void handleCtrl(KeyEvent event) {
        if (ctrlPressed && !shiftPressed) {
            switch (event.getKeyCode()) {
                case KeyEvent.VK_C:
                hotKeys.close(lines);
                break;

                case KeyEvent.VK_S:
                fileManager.writeFile();
                break;

                default:
                break;
            }
        }
    }

    private void handleCtrlShift(KeyEvent event) {
        if (ctrlPressed && shiftPressed) {
            switch (event.getKeyCode()) {
                default:
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_E:
            if (cursorMode) {
                cursorMode = false;
                cursor.changeColorWhite();
            }
            break;

            case KeyEvent.VK_CONTROL:
            ctrlPressed = false;
            break;

            case KeyEvent.VK_ALT:
            altPressed = false;
            break;

            case KeyEvent.VK_SHIFT:
            shiftPressed = false;
            break;
            
            default:
            break;
        }
    }


    // NEW LINE

    private void newLine() {
        lines.add(cursor.y + 1, new ArrayList<>());
        splitCurrentLine();
        printNewLine();
    }

    private void splitCurrentLine() {
        int size = lines.get(cursor.y).size();
        int newLine = cursor.y + 1;
        if (cursor.x < size) {
            cursor.clearScreenAfterCursor();

            for (int i = cursor.x; i < size; i++) {
                lines.get(newLine).add(lines.get(cursor.y).get(cursor.x));
                lines.get(cursor.y).remove(cursor.x);
            }
        }
    }

    private void printNewLine() {
        cursor.y++;
        cursor.x = 0;
        
        System.out.print(action);
        cursor.clearScreenAfterCursor();
        cursor.savePosition();

        for (int i = cursor.y; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).size(); j++) {
                System.out.print(lines.get(i).get(j));
            }

            System.out.println();
        }

        cursor.restorePosition();
    }


    // BACK SPACE

    private void backSpace() {
        if (cursor.x > 0) {
            cursor.x--;
            lines.get(cursor.y).remove(cursor.x);
            
            // Backspace, print empty space, backspace again.
            System.out.print(action + " " + action);
            
            cursor.printLineAfterCursor(lines.get(cursor.y));
        }
    }


    // TABULATION

    private void tabulate() {
        for (int i = 0; i < 4; i++) {
            lines.get(cursor.y).add(cursor.x, EMPTY_SPACE);
            cursor.x++;
            System.out.print(EMPTY_SPACE);
        }
        
        cursor.printLineAfterCursor(lines.get(cursor.y));
    }

    private void reverseTab() {}


    // TOGGLE CURSOR MODE

    private void toggleCursorMode() {
        if (altPressed) {
            if (cursorMode) {
                cursorMode = false;
                cursor.changeColorWhite();
            }
            else {
                cursorMode = true;
                cursor.changeColorRed();
            }
        }
    }
}

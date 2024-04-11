package com.vdit;

import java.util.ArrayList;

class Editor {
    private ArrayList<ArrayList<Character>> lines = new ArrayList<>();
    private TerminalManager terminal = new TerminalManager();
    private Cursor cursor = new Cursor();
    private FileManager fileManager;
    
    private int scroll = 0;
    private char action;
    private char letter;

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
            
            for (int i = scroll; i < terminal.getHeight() && i < lines.size(); i++) {
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
        
        terminal.command("clear");
        System.out.println(terminal.getWidth() + "x" + terminal.getHeight());
        // TODO: Remove all '\n' when opening file.
    }

    public void start() {
        char key;
        int keyCode;
        boolean isAltPressed = false;
        terminal.command("stty");
        
        while (loop) {
            keyCode = terminal.readKeys();
            key = (char) keyCode;

            if (keyCode == KeyCodes.ALT) {
                isAltPressed = true;
            }
            
            // if (key != '[')
            System.out.print(key);
            // System.out.println(keyCode);
            if (keyCode == KeyCodes.CTRL_C) {
                loop = false;
            }

            if (isAltPressed) {
            }

            switch (keyCode) {
                case KeyCodes.BACKSPACE:
                    // backSpace();
                    break;
            
                default:
                    break;
            }
        }

        terminal.close();
    }

    

    public void keyTyped(char key) {
        letter = key;
        
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

    public void keyPressed(char key, int code) {
        action = key;
        
        switch (code) {
            case KeyCodes.M:
            newLine();
            break;

            case KeyCodes.J:
            tabulate();
            break;

            case KeyCodes.BACKSPACE:
            backSpace();
            break;

            // MOVE KEYS
            case KeyCodes.CTRL_J:
            if (cursorMode || (!cursorMode && ctrlPressed)) {
                if (cursorMode && ctrlPressed) {
                    cursor.jumpBackward(lines.get(cursor.y));
                }
                else {
                    cursor.backward();
                }
            }
            break;

            case KeyCodes.CTRL_L:
            if (cursorMode || (!cursorMode && ctrlPressed)) {
                if (cursorMode && ctrlPressed) {
                    cursor.jumpForward(lines.get(cursor.y));
                }
                else {
                    cursor.forward(lines.get(cursor.y));
                }
            }
            break;

            case KeyCodes.CTRL_K:
            toggleCursorMode();

            if (cursorMode || (!cursorMode && ctrlPressed)) {
                cursor.down(lines);
            }
            break;

            case KeyCodes.CTRL_I:
            if (cursorMode || (!cursorMode && ctrlPressed)) {
                cursor.up();
            }
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

    private void backTab() {}


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

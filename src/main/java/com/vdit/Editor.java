package com.vdit;

import java.util.ArrayList;

class Editor {
    private ArrayList<ArrayList<Character>> lines = new ArrayList<>();
    // private ArrayList<StringBuilder> lines = new ArrayList<>();
    private TerminalManager terminal = new TerminalManager();
    private Cursor cursor = new Cursor(lines);
    private FileManager fileManager;
    
    private int scroll = 0;
    private char action;

    private boolean cursorMode = false;
    private boolean loop = true;
    private boolean beginEscapeSequence = false;
    private boolean squareBracket = false;

    private char key;
    private int keyCode;
    
    private final char EMPTY_SPACE = ' ';

    public Editor(String files) {
        fileManager = new FileManager(files, lines);

        if (files != null) {
            fileManager.openFiles();
        }
        else {
            lines.add(new ArrayList<>());
        }
        
        terminal.command("clear");
        System.out.println(terminal.getWidth() + "x" + terminal.getHeight());
        cursor.enableBlink();
        cursor.changeColorWhite();
        // TODO: Remove all '\n' when opening file.
    }

    public void start() {
        while (loop) {
            keyCode = terminal.readKeys();
            key = (char) keyCode;
            
            cursor.savePosition();
            System.out.print("\033["+terminal.getHeight()/2+";1H");
            System.out.println(keyCode);
            cursor.restorePosition();
            
            if (!specialKey() && !cursorMode) {
                lines.get(cursor.y).add(cursor.x, key);
                cursor.x++;
                System.out.print(key);
                cursor.printLineAfterCursor();
            }
            else if (cursorMode) {
                cursorModeEvents();
            }
        }
        
        terminal.close();
    }

    private void printText() {
        cursor.savePosition();
            
        for (int i = scroll; i < terminal.getHeight() && i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).size(); j++) {
                System.out.print(lines.get(i).get(j));
            }
            System.out.println();
        }

        cursor.restorePosition();
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
        
        System.out.print("\n");
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

    private void backspace() {
        if (cursor.x > 0) {
            cursor.x--;
            lines.get(cursor.y).remove(cursor.x);
            
            // Backspace, print empty space, S again.
            System.out.print(action + " " + action);
            
            cursor.printLineAfterCursor();
        }
    }


    // TABULATION

    private void tab() {
        for (int i = 0; i < 4; i++) {
            lines.get(cursor.y).add(cursor.x, EMPTY_SPACE);
            cursor.x++;
            System.out.print(EMPTY_SPACE);
        }
        
        cursor.printLineAfterCursor();
    }

    private void backtab() {}


    // TOGGLE CURSOR MODE

    private void toggleCursorMode() {
        cursorMode = !cursorMode;
        if (cursorMode) {
            cursor.changeColorRed();
            cursor.disableBlink();
        }
        else {
            cursor.changeColorWhite();
            cursor.enableBlink();
        }
    }

    
    // CURSOR MODE

    private void cursorModeEvents() {
        switch (keyCode) {
            case KeyCodes.I:
                cursor.up();
                break;

            case KeyCodes.L:
                cursor.forward();
                break;
            
            case KeyCodes.J:
                cursor.backward();
                break;

            case KeyCodes.K:
                cursor.down();
                break;
            
            case KeyCodes.CTRL_L:
                cursor.jumpForward();
                break;
                
            case KeyCodes.CTRL_J:
                cursor.jumpBackward();
                break;

            default:
                break;
        }
    }
        
    
    // SPECIAL KEYS

    private boolean specialKey() {
        if (beginEscapeSequence) {
            if (squareBracket) {
                if (keyCode == KeyCodes.BACKTAB) {
                    backtab();
                }
                beginEscapeSequence = false;
                squareBracket = false;
                return true;
            }
            
            switch (keyCode) {
                case KeyCodes.SQR_BRKT:
                    squareBracket = true;
                    break;
            
                default:
                    beginEscapeSequence = false;
                    squareBracket = false;
                    break;
            }

            return true;
        }

        if (keyCode <= KeyCodes.ESC) {
            if (keyCode == KeyCodes.ESC) {
                beginEscapeSequence = true;
                return true;
            }
            
            switch (keyCode) {
                case KeyCodes.CTRL_C:
                    loop = false;
                    printText();
                    break;

                case KeyCodes.CTRL_K:
                    toggleCursorMode();
                    break;

                case KeyCodes.CTRL_L:
                    cursor.forward();
                    break;

                case KeyCodes.CTRL_J:
                    cursor.backward();
                    break;

                case KeyCodes.ENTER:
                    newLine();
                    break;
                    
                case KeyCodes.BACKSPACE:
                    backspace();
                    break;

                case KeyCodes.TAB:
                    tab();
                    break;
            
                default:
                    break;
            }

            return true;
        }

        return false;
    }
}

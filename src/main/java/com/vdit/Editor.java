package com.vdit;

import java.util.ArrayList;

class Editor {
    private ArrayList<StringBuilder> lines = new ArrayList<>();
    private TerminalManager terminal = new TerminalManager();
    private Cursor cursor = new Cursor(lines);
    private FileManager fileManager;
    
    private final char EMPTY_SPACE = ' ';
    private int scroll = 0;
    private char action;
    private char key;
    private int keyCode;

    private boolean beginEscapeSequence = false;
    private boolean squareBracket = false;
    private boolean cursorMode = false;
    private boolean loop = true;

    public Editor(String files) {
        fileManager = new FileManager(files, lines);

        if (files != null) {
            fileManager.openFiles();
        }
        else {
            lines.add(new StringBuilder());
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
            
            // cursor.savePosition();
            // System.out.print("\033["+terminal.getHeight()/2+";1H");
            // System.out.println(keyCode);
            // cursor.restorePosition();
            
            if (!specialKey() && !cursorMode) {
                type();

                //! ESC triggers sequence searching
                //! which blocks keys from being pressed
                //! TODO: must be fixed
                
            }
            else if (cursorMode) {
                cursorModeEvents();
            }
        }
        
        terminal.close();
    }

    private void type() {
        StringBuilder line = lines.get(cursor.y);
        line.insert(cursor.x, key);
        cursor.x++;
        System.out.print(key);
        cursor.printLineAfterCursor();
    }

    private void printText() {
        cursor.savePosition();
            
        for (int i = scroll; i < terminal.getHeight() && i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                System.out.print(lines.get(i).charAt(j));
            }
            System.out.println();
        }

        cursor.restorePosition();
    }


    // NEW LINE

    private void newLine() {
        lines.add(cursor.y + 1, new StringBuilder());
        splitCurrentLine();
        printNewLine();
    }

    private void splitCurrentLine() {
        StringBuilder line = lines.get(cursor.y);
        int length = line.length();
        int newLine = cursor.y + 1;

        if (cursor.x < length) {
            cursor.clearScreenAfterCursor();
            for (int i = cursor.x; i < length; i++) {
                lines.get(newLine).append(line.charAt(cursor.x));
                line.deleteCharAt(cursor.x);
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
            for (int j = 0; j < lines.get(i).length(); j++) {
                System.out.print(lines.get(i).charAt(j));
            }
            System.out.println();
        }

        cursor.restorePosition();
    }


    // BACK SPACE

    private void backspace() {
        StringBuilder line = lines.get(cursor.y);
        if (cursor.x > 0) {
            cursor.x--;
            line.deleteCharAt(cursor.x);
            
            // Backspace, print empty space, S again.
            System.out.print(action + " " + action);
            
            cursor.printLineAfterCursor();
        }
    }


    // TABULATION

    private void tab() {
        StringBuilder line = lines.get(cursor.y);
        for (int i = 0; i < 4; i++) {
            line.insert(cursor.x, EMPTY_SPACE);
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

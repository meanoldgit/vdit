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
    private boolean loop = true;
    private boolean beginEscapeSequence = false;
    private boolean squareBracket = false;

    char key;
    int keyCode;
    
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
        // TODO: Remove all '\n' when opening file.
    }

    public void start() {
        while (loop) {
            keyCode = terminal.readKeys();
            key = (char) keyCode;
            
            System.out.println(keyCode);
            if (checkCode()) {
                lines.get(cursor.y).add(cursor.x, key);
                cursor.x++;
                System.out.print(key);
                // cursor.printLineAfterCursor(lines.get(cursor.y));
            }
        }
        
        terminal.close();
    }
    
    private boolean checkCode() {
        if (keyCode <= KeyCodes.ESC) {
            if (keyCode == KeyCodes.ESC) {
                beginEscapeSequence = true;
                return false;
            }
            
            switch (keyCode) {
                case KeyCodes.CTRL_C:
                    loop = false;
                    System.out.println(lines);
                    break;

                case KeyCodes.INTRO:
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

            return false;
        }
        
        if (beginEscapeSequence) {
            if (squareBracket) {
                if (keyCode == KeyCodes.BACKTAB) {
                    backtab();
                }
                return false;
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

            return false;
        }

        return true;
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

    private void keyPressed(char key, int code) {
        action = key;
        
        // switch (code) {
        //     case KeyCodes.M:
        //     newLine();
        //     break;

        //     case KeyCodes.J:
        //     tab();
        //     break;

        //     case KeyCodes.BACKSPACE:
        //     backspace();
        //     break;

        //     // MOVE KEYS
        //     case KeyCodes.CTRL_J:
        //     if (cursorMode || (!cursorMode && ctrlPressed)) {
        //         if (cursorMode && ctrlPressed) {
        //             cursor.jumpBackward(lines.get(cursor.y));
        //         }
        //         else {
        //             cursor.backward();
        //         }
        //     }
        //     break;

        //     case KeyCodes.CTRL_L:
        //     if (cursorMode || (!cursorMode && ctrlPressed)) {
        //         if (cursorMode && ctrlPressed) {
        //             cursor.jumpForward(lines.get(cursor.y));
        //         }
        //         else {
        //             cursor.forward(lines.get(cursor.y));
        //         }
        //     }
        //     break;

        //     case KeyCodes.CTRL_K:
        //     toggleCursorMode();

        //     if (cursorMode || (!cursorMode && ctrlPressed)) {
        //         cursor.down(lines);
        //     }
        //     break;

        //     case KeyCodes.CTRL_I:
        //     if (cursorMode || (!cursorMode && ctrlPressed)) {
        //         cursor.up();
        //     }
        //     break;

        //     default:
        //     break;
        // }
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

    private void backspace() {
        if (cursor.x > 0) {
            cursor.x--;
            lines.get(cursor.y).remove(cursor.x);
            
            // Backspace, print empty space, S again.
            System.out.print(action + " " + action);
            
            cursor.printLineAfterCursor(lines.get(cursor.y));
        }
    }


    // TABULATION

    private void tab() {
        for (int i = 0; i < 4; i++) {
            lines.get(cursor.y).add(cursor.x, EMPTY_SPACE);
            cursor.x++;
            System.out.print(EMPTY_SPACE);
        }
        
        cursor.printLineAfterCursor(lines.get(cursor.y));
    }

    private void backtab() {}


    // TOGGLE CURSOR MODE

    // private void toggleCursorMode() {
    //     if (altPressed) {
    //         if (cursorMode) {
    //             cursorMode = false;
    //             cursor.changeColorWhite();
    //         }
    //         else {
    //             cursorMode = true;
    //             cursor.changeColorRed();
    //         }
    //     }
    // }
}

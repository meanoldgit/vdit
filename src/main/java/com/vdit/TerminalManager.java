package com.vdit;

import java.io.IOException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

public class TerminalManager {
    private Terminal terminal;
    private NonBlockingReader reader;
    public boolean loop = true;
    public int width = 0;
    public int height = 0;
    public Thread checkSize = new Thread(() -> {
        while (loop) {
            checkForResize();
        }
    });

    public TerminalManager() {
        try {
            terminal = TerminalBuilder.builder().build();
            reader = terminal.reader();
            terminal.enterRawMode();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    // KEYS

    public char readKeys() {
        int key = 0;
        try {
            key = reader.read();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return (char) key;
    }


    // SCREEN

    public void checkForResize() {
        if (windowResized()) {
            height = terminal.getHeight();
            width = terminal.getWidth();
            System.out.println(width + "x" + height);
        }
    }

    private boolean windowResized() {
        return height != terminal.getHeight()
            || width != terminal.getWidth();
    }

    public void clearScreen() {
        try {
            ProcessBuilder clear = new ProcessBuilder("bash", "-c", "clear").inheritIO();
            clear.start().waitFor();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

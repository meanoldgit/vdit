package com.vdit;

import java.io.IOException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

public class TerminalManager {
    private Terminal terminal;
    private NonBlockingReader reader;
    public int width = 0;
    public int height = 0;

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

    public void readKeys() {
        int key = 0;
        try {
            key = reader.read();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print((char) key);
        // if (key == 'q') {
        //     break;
        // }
    }


    // SCREEN

    public void checkSize() {
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

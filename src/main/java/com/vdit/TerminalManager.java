package com.vdit;

import java.io.IOException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

public class TerminalManager {
    private Terminal terminal;
    private NonBlockingReader keyReader;
    public boolean loop = true;
    public int width = 0;
    public int height = 0;
    
    public Thread checkSizeThread = new Thread(() -> {
        while (loop) {
            checkForResize();
        }
    });

    public TerminalManager() {
        try {
            terminal = TerminalBuilder.builder().system(true).build();
            keyReader = terminal.reader();
            terminal.enterRawMode();
            checkSizeThread.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    // KEYS

    public char readKeys() {
        int key = 0;
        try {
            key = keyReader.read();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return (char) key;
    }


    // SCREEN

    private void checkForResize() {
        if (windowResized()) {
            height = terminal.getHeight();
            width = terminal.getWidth();
            System.out.println(width + "x" + height); // TODO: remove
        }
    }

    private boolean windowResized() {
        return height != terminal.getHeight()
            || width != terminal.getWidth();
    }

    public void command(String str) {
        ProcessBuilder command = new ProcessBuilder("bash", "-c", str).inheritIO();
        try {
            command.start().waitFor();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

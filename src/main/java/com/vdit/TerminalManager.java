package com.vdit;

import java.io.IOException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

public class TerminalManager {
    private Terminal terminal;
    private NonBlockingReader keyReader;
    private boolean loop = true;
    private int width = 0;
    private int height = 0;
    // private final String disableInterruptSignals = "stty intr undef susp undef eof undef";
    private final String disableInterruptSignals = "stty intr undef eof undef";
    private final String resetTerminalSettings = "stty sane -brkint -imaxbel";
    
    private Thread checkSizeThread = new Thread(() -> {
        while (loop) {
            handleResize();
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    public TerminalManager() {
        try {
            terminal = TerminalBuilder.builder().system(true).build();
            keyReader = terminal.reader();
            terminal.enterRawMode();
            checkSizeThread.start();
            command(disableInterruptSignals);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    // KEYS

    public int readKeys() {
        int key = 0;
        try {
            key = keyReader.read();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return key;
    }


    // SCREEN

    private void handleResize() {
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    // COMMANDS

    public void command(String str) {
        ProcessBuilder command = new ProcessBuilder("bash", "-c", str).inheritIO();
        try {
            command.start().waitFor();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        loop = false;
        command(resetTerminalSettings);
        try {
            terminal.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

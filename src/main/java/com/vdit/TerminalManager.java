package com.vdit;

import java.io.IOException;
import org.jline.terminal.Terminal;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.TerminalBuilder;

public class TerminalManager {
    Terminal terminal;
    LineReader lineReader;
    int width = 0;
    int height = 0;

    public TerminalManager() {
        try {
            terminal = TerminalBuilder.builder().build();
            lineReader = LineReaderBuilder
                        .builder()
                        .terminal(terminal)
                        .parser(new DefaultParser())
                        .build();
            terminal.enterRawMode();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

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
}

package com.vdit;

public class Main {
    public static void main(String[] args) {
        String fileName;
        // TODO: make fileName an array to support multiple arguments
        if (args.length > 0) {
            fileName = args[0];
            // for x in args files.add x
        }
        else {
            fileName = null;
        }

        Editor editor = new Editor(fileName);
        editor.start();
    }
}

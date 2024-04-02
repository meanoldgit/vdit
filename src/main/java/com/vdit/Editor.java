package com.vdit;

public class Editor {
    public static void main(String[] args) {
        String fileName;
        if (args.length > 0) fileName = args[0];
        else fileName = null;
        Key key = new Key(fileName);
    }
}


package com.vdit;

public class Main {
    public static void main(String[] args) {
        String fileName;
        if (args.length > 0) fileName = args[0];
        else fileName = null;
        
        Editor key = new Editor(fileName);
        key.start();
    }
}
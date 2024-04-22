package com.vdit;

public class Main {
    public static void main(String[] args) {
        int length = args.length;
        String[] files;
        
        if (length > 0) {
            files = new String[length];
            for (int i = 0; i < length; i++) {
                files[i] = args[i];
            }
        }
        else {
            files = new String[1];
            files[0] = null;
        }

        Editor editor = new Editor(files[0]);
        editor.start();
    }
}

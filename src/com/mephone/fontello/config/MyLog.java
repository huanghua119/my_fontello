package com.mephone.fontello.config;

import javax.swing.JTextArea;

public class MyLog {

    private static final boolean DEBUG = true;

    private static JTextArea sTextArea;

    public static final void i(String msg) {
        if (DEBUG) {
            if (sTextArea == null) {
                System.out.println(msg);
            } else {
                sTextArea.append(msg + "\n");
                sTextArea.setCaretPosition(sTextArea.getText().length());
            }
        }
    }

    public static final void setTextArea(JTextArea text) {
        sTextArea = text;
    }
}

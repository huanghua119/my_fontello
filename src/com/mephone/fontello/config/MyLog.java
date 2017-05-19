package com.mephone.fontello.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;

public class MyLog {

    private static final boolean DEBUG = true;

    private static JTextArea sTextArea;

    private static SimpleDateFormat mDateFormat;

    public static final void i(String msg) {
        if (DEBUG) {
            System.out.println(msg);
        }
    }

    public static final void w(String msg) {
        if (sTextArea == null) {
            if (DEBUG) {
                System.out.println(msg);
            }
        } else {
            Date date = new Date();
            if (mDateFormat == null) {
                mDateFormat = new SimpleDateFormat("HH:mm:ss");
            }
            msg = mDateFormat.format(date) + " => " + msg;
            sTextArea.append(msg + "\n");
            sTextArea.setCaretPosition(sTextArea.getText().length());
        }
    }

    public static final void setTextArea(JTextArea text) {
        sTextArea = text;
    }
}

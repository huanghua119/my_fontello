package com.mephone.fontello.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.mephone.fontello.config.MyLog;

public class Cmd {

    private static String OS_NAME = "";

    public static String run(String cmd, boolean showline) {
        Runtime rt = Runtime.getRuntime();

        if (TextUtils.isEmpty(OS_NAME)) {
            OS_NAME = System.getProperty("os.name").toLowerCase();
        }
        if (OS_NAME.startsWith("win")) {
            cmd = "cmd /c " + cmd;
        }
        MyLog.i("run cmd:" + cmd);

        InputStream stderr = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuffer result = new StringBuffer();
        Process proc = null;
        try {
            proc = rt.exec(cmd);
            proc.getOutputStream().close();
            stderr = proc.getInputStream();
            isr = new InputStreamReader(stderr);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                result.append(line);
                if (showline) {
                    MyLog.i("line: " + line);
                }
            }
        } catch (IOException e) {
            MyLog.i("run cmd error:" + e);
            result = new StringBuffer("");
        } finally {
            if (proc != null) {
                proc.destroy();
            }
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                if (stderr != null) {
                    stderr.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return result.toString();
    }
}

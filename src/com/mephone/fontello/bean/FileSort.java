package com.mephone.fontello.bean;

import java.io.File;
import java.util.Comparator;

import com.mephone.fontello.util.TextUtils;

public class FileSort implements Comparator<File> {

    @Override
    public int compare(File f1, File f2) {
        String n1 = f1.getName();
        String n2 = f2.getName();
        n1 = n1.substring(0, n1.lastIndexOf("."));
        n2 = n2.substring(0, n2.lastIndexOf("."));

        if (n1.contains("_") && n2.contains("_")) {
            return compareEnd(n1, n2, "_");
        } else if (n1.contains("-") && n2.contains("-")) {
            return compareEnd(n1, n2, "-");
        }
        return 0;
    }

    private int compareEnd(String n1, String n2, String end) {
        String[] split1 = n1.split(end);
        String[] split2 = n2.split(end);

        String num1str = "";
        String num2str = "";
        for (int i = 0; i < split1.length; i++) {
            boolean is1Num = TextUtils.isNumericZidai(split1[i]);
            boolean is2Num = TextUtils.isNumericZidai(split2[i]);
            if (is1Num) {
                num1str += split1[i];
            }
            if (is2Num) {
                num2str += split2[i];
            }
        }
        try {
            int num1 = Integer.parseInt(num1str);
            int num2 = Integer.parseInt(num2str);
            if (num1 == num2) {
                return 0;
            } else {
                return num1 > num2 ? 1 : -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}

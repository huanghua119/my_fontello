package com.mephone.fontello.bean;

import java.io.File;
import java.util.Comparator;

public class FileSort implements Comparator<File> {

    @Override
    public int compare(File f1, File f2) {
        String n1 = f1.getName();
        String n2 = f2.getName();
        n1 = n1.substring(0, n1.lastIndexOf("."));
        n2 = n2.substring(0, n2.lastIndexOf("."));

        if (n1.contains("_") && n2.contains("_")) {
            String[] split1 = n1.split("_");
            String[] split2 = n2.split("_");
            if (split1.length == 2 && split2.length == 2) {
                int f1_num1 = Integer.parseInt(split1[0]);
                int f1_num2 = Integer.parseInt(split1[1]);

                int f2_num1 = Integer.parseInt(split2[0]);
                int f2_num2 = Integer.parseInt(split2[1]);

                if (f1_num1 == f2_num1) {
                    return f1_num2 > f2_num2 ? 1 : -1;
                } else {
                    return f1_num1 > f2_num1 ? 1 : -1;
                }
            }
        }
        return 0;
    }
}

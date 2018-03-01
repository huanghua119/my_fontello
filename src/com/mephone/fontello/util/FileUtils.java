package com.mephone.fontello.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
    public static void copyFile(String src, String dest) {
        OutputStream out = null;
        try {
            FileInputStream fis = new FileInputStream(src);

            File outFile = new File(dest);
            out = new FileOutputStream(outFile);
            copyFileExt(fis, out);

            out.flush();
            out.close();
            out = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void copyFileExt(InputStream in, OutputStream out)
            throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

}

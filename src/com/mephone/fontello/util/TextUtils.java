package com.mephone.fontello.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mephone.fontello.config.MyLog;

public class TextUtils {

    public static boolean isEmpty(String str) {
        if (str != null && !str.equals("")) {
            return false;
        }
        return true;
    }

    public static String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            unicode.append(Integer.parseInt(Integer.toHexString(c), 16));
            //unicode.append(Integer.toHexString(c));
        }
        return unicode.toString();
    }

    public static String string2UnicodeHex(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            unicode.append(Integer.toHexString(c));
        }
        return unicode.toString();
    }

    public static String getFileText(String f, boolean singleLine) {
        File file = new File(f);
        if (!file.exists()) {
            MyLog.i("getFileText " + f + " not found");
        }
        StringBuffer result = new StringBuffer();
        FileReader fileReader = null;
        BufferedReader in = null;
        try {
            fileReader = new FileReader(file);
            in = new BufferedReader(fileReader);
            String s = null;
            while ((s = in.readLine()) != null) {
                result.append(singleLine ? s + " " : s);
            }
            in.close();
            fileReader.close();
        } catch (Exception e1) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }

    public static void saveFileText(String data, String path) {
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            // MyLog.i("data:" + data);
            FileOutputStream outStream = new FileOutputStream(path, true);
            OutputStreamWriter writer = new OutputStreamWriter(outStream,
                    "UTF-8");
            writer.write(data);
            writer.flush();
            writer.close();// 记得关闭
            outStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] toByteArray(String hexString) {
        if (hexString == null || hexString.length() == 0) {

            throw new IllegalArgumentException(
                    "this hexString must not be empty");
        }
        hexString = hexString.toLowerCase();
        if (hexString.matches("[0]+")) {// 全是0的时候返回0
            return new byte[0];
        }
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {// 因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }

    /**
     * 递归查询所有文件
     * 
     * @param cd
     * @return
     */
    public static List<File> getFiles(File cd, String end) {
        List<File> result = new ArrayList<>();
        File[] files = cd.listFiles();
        for (File f : files) {
            if (f.isFile()) {
                int index = f.getName().lastIndexOf(".");
                if (f.getName().substring(index).equals(end)) {
                    result.add(f);
                }
            } else {
                // 递归
                result.addAll(getFiles(f, end));
            }
        }
        return result;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static String readFile(String path) {
        String data = "";
        // 判断文件是否存在
        File file = new File(path);
        if (!file.exists()) {
            return data;
        }
        // 获取文件编码格式
        String code = getFileEncode(path);
        InputStreamReader isr = null;
        try {
            // 根据编码格式解析文件
            if ("asci".equals(code)) {
                // 这里采用GBK编码，而不用环境编码格式，因为环境默认编码不等于操作系统编码
                // code = System.getProperty("file.encoding");
                code = "GBK";
            }
            isr = new InputStreamReader(new FileInputStream(file), code);
            // 读取文件内容
            int length = -1;
            char[] buffer = new char[1024];
            StringBuffer sb = new StringBuffer();
            while ((length = isr.read(buffer, 0, 1024)) != -1) {
                sb.append(buffer, 0, length);
            }
            data = new String(sb);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static String readFile(InputStream is) {
        String data = "";
        if (is == null) {
            return data;
        }
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(is, "UTF-8");
            // 读取文件内容
            int length = -1;
            char[] buffer = new char[1024];
            StringBuffer sb = new StringBuffer();
            while ((length = isr.read(buffer, 0, 1024)) != -1) {
                sb.append(buffer, 0, length);
            }
            data = new String(sb);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static String getFileEncode(String path) {
        String charset = "asci";
        byte[] first3Bytes = new byte[3];
        BufferedInputStream bis = null;
        try {
            boolean checked = false;
            bis = new BufferedInputStream(new FileInputStream(path));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "Unicode";// UTF-16LE
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "Unicode";// UTF-16BE
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int len = 0;
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF)
                            // 双字节 (0xC0 - 0xDF) (0x80 - 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) { // 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ex) {
                }
            }
        }
        return charset;
    }

    private static String getEncode(int flag1, int flag2, int flag3) {
        String encode = "";
        // txt文件的开头会多出几个字节，分别是FF、FE（Unicode）,
        // FE、FF（Unicode big endian）,EF、BB、BF（UTF-8）
        if (flag1 == 255 && flag2 == 254) {
            encode = "Unicode";
        } else if (flag1 == 254 && flag2 == 255) {
            encode = "UTF-16";
        } else if (flag1 == 239 && flag2 == 187 && flag3 == 191) {
            encode = "UTF8";
        } else {
            encode = "asci";// ASCII码
        }
        return encode;
    }

}

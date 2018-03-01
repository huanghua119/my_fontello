package com.mephone.fontello;

/**
 * 
 * @author huanghua
 * 
 */
public class Bihuachaxun {

    public static void main(String[] args) {
        String haizi = "辊";
        Bihuachaxun chaxun = new Bihuachaxun();
        String url = chaxun.getHaiziUrl(haizi);

        System.out.println("url:" + url);
    }

    public String getHaiziUrl(String haizi) {
        String unicode = cnToUnicode(haizi);

        int len = unicode.length();
        String start = unicode.substring(0, 1);
        String mid = unicode.substring(1, len - 1);
        String end = unicode.substring(len - 1, len);

        int sourse = Integer.parseInt(mid, 16);

        String r = jiami(sourse);
        String result = "e" + start + r + end;
        String url = "https://bihua.51240.com/" + result + "__bihuachaxun/";
        return url;
    }

    /**
     * 破解加密
     * 
     * @param sourse
     * @return
     */
    private String jiami(int sourse) {
        int start = 0x808;
        int offce = 0x4;
        int c = sourse % offce;
        int d = sourse / offce;
        int result = start + (d * 0x10) + c;
        return Integer.toHexString(result);
    }

    private static String cnToUnicode(String cn) {
        char[] chars = cn.toCharArray();
        String returnStr = "";
        for (int i = 0; i < chars.length; i++) {
            returnStr += Integer.toString(chars[i], 16);
        }
        return returnStr;
    }
}

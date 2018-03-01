package com.mephone.fontello;

import java.io.File;
import java.util.List;

import com.mephone.fontello.config.SystemConfig;
import com.mephone.fontello.util.TextUtils;
import com.mephone.fontello.util.Ttf2png;

public class DrawTextForTTF {

    public static void main(String[] args) {
        DrawTextForTTF dtt = new DrawTextForTTF();
        String text = "孽斥镢瘾惭铽墩我魂谜绂攥蹴癍旁齑凤鸨霭籴盏芎靥钺戤蟹魍犍遴龀簿懑焕裘敬虢噱黎钙卵寡庚递";
        for (int i = 0; i < text.length(); i++) {
            String c = text.charAt(i) + "";
            dtt.startDrawText(c);
        }
    }

    private void startDrawText(String text) {
        List<File> fileList = FontelloService.getInstance().getFiles(
                SystemConfig.FileSystem.DRAW_TTF, ".ttf", ".otf");

        if (fileList != null && fileList.size() > 0) {
            for (int i = 0; i < text.length(); i++) {
                for (File file : fileList) {
                    String c = text.charAt(i) + "";
                    String out = SystemConfig.FileSystem.DRAW_PATH + "out/" + c
                            + "/" + TextUtils.getFileName(file) + ".png";
                    Ttf2png.ttf2png(file, out, c, 512f);
                }
            }
        }
    }
}

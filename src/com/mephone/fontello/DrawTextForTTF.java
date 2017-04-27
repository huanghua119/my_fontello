package com.mephone.fontello;

import java.io.File;
import java.util.List;

import com.mephone.fontello.config.SystemConfig;
import com.mephone.fontello.util.TextUtils;
import com.mephone.fontello.util.Ttf2png;

public class DrawTextForTTF {

    public static void main(String[] args) {
        DrawTextForTTF dtt = new DrawTextForTTF();
        dtt.startDrawText("冰");
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
                    Ttf2png.ttf2png(file, out, c, 200f);
                }
            }
        }
    }
}

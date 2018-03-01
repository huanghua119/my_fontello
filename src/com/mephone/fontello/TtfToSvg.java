package com.mephone.fontello;

import java.io.File;
import java.util.List;

import com.mephone.fontello.bean.FontSvg;
import com.mephone.fontello.config.MyLog;
import com.mephone.fontello.config.SystemConfig;
import com.mephone.fontello.svg.SVGParser;
import com.mephone.fontello.util.TextUtils;
import com.mephone.fontello.util.Ttf2png;

public class TtfToSvg {

    public static void main(String[] args) {
        TtfToSvg t2s = new TtfToSvg();
        t2s.doNewTTF2SVG();
    }

    public void doNewTTF2SVG() {
        FontelloService service = FontelloService.getInstance();

        List<File> files = service.getFiles(SystemConfig.FileSystem.FONTS_PATH,
                ".ttf", ".otf");

        String ttfText = TextUtils.readFile(FontelloService.class
                .getResourceAsStream("/com/mephone/fontello/GB2312完整字库.txt"));
        ttfText = TextUtils.replaceBlank(ttfText);

        for (File file : files) {
            startDrawText(file, ttfText);
        }
    }

    private void startDrawText(File ttf, String text) {
        String ttfPath = ttf.getAbsolutePath();
        for (int i = 0; i < text.length(); i++) {
            String c = text.charAt(i) + "";
            String outPath = ttfPath.substring(0, ttfPath.lastIndexOf("."))
                    + File.separator + c + ".png";
            boolean ok = Ttf2png.ttf2png(ttf, outPath, c, 200f);
            if (ok) {
                File pngFile = new File(outPath);
                if (pngFile.exists()) {
                    FontelloService.getInstance().png2svg(new File(outPath));
                    pngFile.delete();
                }
            }
        }
    }

    public void doTTF2SVG() {
        FontelloService service = FontelloService.getInstance();

        List<File> files = service.getFiles(SystemConfig.FileSystem.FONTS_PATH,
                ".ttf");
        System.out.println("files:" + files.size());

        for (File file : files) {
            String name = file.getName().substring(0,
                    file.getName().lastIndexOf("."));
            String oldPath = file.getAbsolutePath();
            String outPath = oldPath.substring(0, oldPath.lastIndexOf("."))
                    + ".svg";

            System.out.println("oldPath:" + oldPath + " outPath:" + outPath);
            // 转换ttf为 svg文件
            service.ttf2svg(oldPath, outPath);

            // 解析svg文件
            List<FontSvg> fontSvgList = SVGParser.getInstance().parserBigSvg(
                    outPath);

            if (fontSvgList == null || fontSvgList.size() == 0) {
                MyLog.i("svg部件文件为空!");
                return;
            }
            for (FontSvg svg : fontSvgList) {
                SVGParser.getInstance().generateFontSvg(svg,
                        SystemConfig.FileSystem.FONTS_PATH + name);
            }
        }
    }
}

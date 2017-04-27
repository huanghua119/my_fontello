package com.mephone.fontello.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.mephone.fontello.config.SystemConfig;

public class Ttf2png {
    public static void main(String[] args) {
        String outPath = "data/蜖.png";
        File fontFile = new File(
                "/home/huanghua/android/workspace/CreateFontTools/data/convert/font/义启圣诞体-v5.01-zh00037.ttf");
        drawTextInImg(outPath, "蜖", "#000000", 200f, fontFile);
    }

    private static Map<String, Font> mCacheFont = new HashMap<>();

    public static boolean ttf2png(String fontFile, String out, String label,
            float textSize) {
        return ttf2png(new File(fontFile), out, label, textSize);
    }

    public static boolean ttf2png(File fontFile, String out, String label,
            float textSize) {
        return drawTextInImg(out, label, "#000000", textSize, fontFile);
    }

    public static boolean checkChar(File fontFile, String text) {
        ImageIcon imgIcon = null;
        try {
            imgIcon = new ImageIcon(IOUtils.input2byte(Ttf2png.class
                    .getResourceAsStream("/com/mephone/fontello/temp.png")));
        } catch (IOException e1) {
            return false;
        }
        Image img = imgIcon.getImage();
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        BufferedImage bimage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g = bimage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getColor("#000000"));
        g.setBackground(Color.white);
        g.drawImage(img, 0, 0, null);

        try {

            Font font = null;
            String key = fontFile.getName();
            if (mCacheFont.containsKey(key)) {
                font = mCacheFont.get(key);
            } else {
                font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                font = font.deriveFont(200f);
                mCacheFont.put(key, font);
            }

            g.setFont(font);
            FontMetrics metrics = new FontMetrics(font) {
                private static final long serialVersionUID = 1L;
            };
            Rectangle2D bounds = metrics.getStringBounds(text, null);
            int textWidth = (int) bounds.getWidth();
            int textHeight = (int) bounds.getHeight();
            int left = (width - textWidth) / 2;

            FontMetrics fm = g.getFontMetrics();
            int ascent = fm.getAscent();
            int descent = Math.abs(fm.getDescent());
            int leading = fm.getLeading();
            int fheight = fm.getHeight();

            int top = height - descent - 50;
            g.drawString(text, left, top);
            g.dispose();

            int ok = cutImage("", bimage, (int) 200f);
            if (ok == 1) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean drawTextInImg(String outFile, String text,
            String color, float textSize, File wmFont) {
        ImageIcon imgIcon = null;

        if (TextUtils.fileExists(SystemConfig.FileSystem.SETTING_DIR
                + "temp.png")) {
            imgIcon = new ImageIcon(SystemConfig.FileSystem.SETTING_DIR
                    + "temp.png");
        } else {
            try {
                imgIcon = new ImageIcon(IOUtils.input2byte(Ttf2png.class
                        .getResourceAsStream("/com/mephone/fontello/temp.png")));
            } catch (IOException e1) {
                return false;
            }
        }
        Image img = imgIcon.getImage();
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        BufferedImage bimage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g = bimage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getColor(color));
        g.setBackground(Color.white);
        g.drawImage(img, 0, 0, null);

        try {

            Font font = null;
            String key = wmFont.getName();
            if (mCacheFont.containsKey(key)) {
                font = mCacheFont.get(key);
            } else {
                font = Font.createFont(Font.TRUETYPE_FONT, wmFont);
                font = font.deriveFont(textSize);
                mCacheFont.put(key, font);
            }

            g.setFont(font);
            FontMetrics metrics = new FontMetrics(font) {
                private static final long serialVersionUID = 1L;
            };
            Rectangle2D bounds = metrics.getStringBounds(text, null);
            int textWidth = (int) bounds.getWidth();
            int textHeight = (int) bounds.getHeight();
            int left = (width - textWidth) / 2;

            FontMetrics fm = g.getFontMetrics();
            int ascent = fm.getAscent();
            int descent = Math.abs(fm.getDescent());
            int leading = fm.getLeading();
            int fheight = fm.getHeight();

            // int top = height - (Math.abs(fheight - height)) / 2 - leading /
            // 2;
            // if (fheight == height) {
            // top = (int) (height - descent - leading / 2);
            // }
            int top = height - descent - 50;
            g.drawString(text, left, top);
            g.dispose();

            // FileOutputStream out = new FileOutputStream(outFile);
            // ImageIO.write(bimage, "PNG", out);
            // out.close();
            int ok = cutImage(outFile, bimage, (int) textSize);
            if (ok == 1) {
                System.out.println("字体" + wmFont.getName() + "中不存在'" + text
                        + "'字");
            } else if (ok == 2) {
                System.out.println("字体" + wmFont.getName() + "中'" + text
                        + "'字超出边界");
            }
            return ok == 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // color #2395439
    public static Color getColor(String color) {
        if (color.charAt(0) == '#') {
            color = color.substring(1);
        }
        if (color.length() != 6) {
            return null;
        }
        try {
            int r = Integer.parseInt(color.substring(0, 2), 16);
            int g = Integer.parseInt(color.substring(2, 4), 16);
            int b = Integer.parseInt(color.substring(4), 16);
            return new Color(r, g, b);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    private static int cutImage(String outFile, BufferedImage bi, int textSize)
            throws IOException {

        Color fillColor = Color.white;

        int maxH = 0;
        int mixH = bi.getHeight();
        int maxW = 0;
        int mixW = bi.getWidth();
        int newH = textSize;
        int newW = textSize;

        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                if (fillColor.getRGB() == bi.getRGB(i, j)) {
                } else {
                    if (maxH < j) {
                        maxH = j;
                    }
                    if (mixH > j) {
                        mixH = j;
                    }
                    if (maxW < i) {
                        maxW = i;
                    }
                    if (mixW > i) {
                        mixW = i;
                    }
                }
            }
        }
        if (maxH == 0) {
            return 1;
        }
        int offsetY = maxH - newH + ((newH - (maxH - mixH)) / 2);
        int offsetX = maxW - newW + ((newW - (maxW - mixW)) / 2);
        // System.out.println("mixH:" + mixH + " maxH:" + maxH + " offsetX:"
        // + offsetX + " offsetY:" + offsetY);
        if ((offsetY + textSize) > bi.getHeight() || (offsetX + textSize) > bi.getWidth()) {
            return 2;
        }
        BufferedImage image = bi.getSubimage(offsetX, offsetY, textSize, textSize);
        if (TextUtils.isEmpty(outFile)) {
            return 4;
        }
        File f = new File(outFile);
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        boolean ok = ImageIO.write(image, "PNG", f);
        image.flush();
        bi.flush();
        return ok ? 0 : 3;
    }

    private static int cutImage(String file, int textSize) throws IOException {
        URL http = new File(file).toURI().toURL();
        BufferedImage bi = ImageIO.read(http.openStream());
        return cutImage(file, bi, textSize);
    }
}

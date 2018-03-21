package com.mephone.fontello;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import com.mephone.fontello.config.MyLog;
import com.mephone.fontello.config.SystemConfig;
import com.mephone.fontello.util.FileUtils;
import com.mephone.fontello.util.TextUtils;
import com.mephone.fontello.util.Ttf2png;

public class ShadowFont {

    public static void main(String[] args) {
        //ShadowFont sf = new ShadowFont();
        // sf.drawGB2312();
    }

    private static int OFFICE = 5;

    private float mTextSize = 50f;
    private float mPngSize = 60f;
    
    private boolean mRunning = false;

    public void startMakeShadowFont() {
        mRunning = true;
        File shadowDir = new File(SystemConfig.FileSystem.SHADOW_PATH);
        File[] fonts = null;
        File[] shadows = null;
        if (shadowDir.exists() && shadowDir.isDirectory()) {
            fonts = shadowDir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory();
                }
            });
            shadows = shadowDir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    String name = pathname.getName();
                    return name.endsWith(".png") && name.startsWith("shadow");
                }
            });
        }
        if (fonts == null) {
            System.out.println("shadow目录下没有找到子目录!");
            MyLog.w("shadow目录下没有找到子目录!");
            return;
        }
        if (shadows == null) {
            System.out.println("shadow目录下没有找到渐变图片!");
            MyLog.w("shadow目录下没有找到渐变图片!");
            return;
        }
        Random random = new Random();
        MyLog.w("正在生成渐变图片....");
        for (File file : fonts) {
            File[] pngs = FileUtils.traverseDir(file.getAbsolutePath(), ".png");
            if (pngs != null && pngs.length != 0) {
                for (File png : pngs) {
                    int index = random.nextInt(shadows.length);
                    makeShadowFont(png.getAbsolutePath(),
                            shadows[index].getAbsolutePath());
                    if (!mRunning) {
                        MyLog.w("操作已中止");
                        return;
                    }
                }
            } else {
                System.out.println("没有字体图片!");
                MyLog.w("没有字体图片!");
            }
            if (!mRunning) {
                MyLog.w("操作已中止");
                return;
            }
        }
        MyLog.w("渐变图片生成完毕!");
    }

    private void makeShadowFont(String pngPath, String shadowPath) {
        System.out.println(pngPath + " 开始");

        Color fillColor = Color.white;
        List<String> blackList = new ArrayList<String>();
        File pngFile = new File(pngPath);

        String outName = TextUtils.string2UnicodeHex(TextUtils
                .getFileName(pngFile)) + ".png";

        File outFile = new File(
                pngFile.getParent() + File.separator + "shadow", outName);
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        if (outFile.exists()) {
            System.out.println(outFile.getAbsolutePath() + " 已存在，不重复生成！");
            MyLog.w(outFile.getAbsolutePath() + " 已存在，不重复生成！");
            return;
        }
        try {
            BufferedImage bi = ImageIO.read(pngFile);
            int biWidth = bi.getWidth(null);
            int biHeight = bi.getHeight(null);

            BufferedImage shadow = ImageIO.read(new File(shadowPath));
            int width = shadow.getWidth(null);
            int height = shadow.getHeight(null);
            if (biWidth != width || biHeight != height) {
                System.out.println(pngPath + " 渐变色图片大小与文本图片大小不一置!");
                MyLog.w(pngPath + " 渐变色图片大小与文本图片大小不一置!");
                return;
            }

            int mixX = biWidth;
            int maxX = 0;
            for (int i = 0; i < biWidth; i++) {
                for (int j = 0; j < biHeight; j++) {
                    int rgb = bi.getRGB(i, j);
                    // System.out.println("rgb:" + rgb + " fillColor:"
                    if (fillColor.getRGB() == rgb) {
                        blackList.add(i + "_" + j);
                    } else {
                        int temp = i;
                        if (temp < mixX) {
                            mixX = temp;
                        }
                        temp = i;
                        if (temp > maxX) {
                            maxX = temp;
                        }
                    }
                }
            }
            if (mixX != 0) {
                mixX = mixX - OFFICE;
            }
            if (maxX != width) {
                maxX = maxX + OFFICE;
            }

            Image im = makeColorTransparent(shadow, blackList);

            BufferedImage bimage = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bimage.createGraphics();
            bimage = g.getDeviceConfiguration().createCompatibleImage(width,
                    height, Transparency.TRANSLUCENT);
            g.dispose();
            g = bimage.createGraphics();

            g.drawImage(im, 0, 0, width, height, null);
            g.dispose();

            if (mixX > 0 || maxX < width) {
                bimage = bimage.getSubimage(mixX, 0, maxX - mixX, height);
            }

            ImageIO.write(bimage, "png", outFile);
            System.out.println(outFile.getAbsolutePath() + " 生成成功!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(pngPath + " 出错！" + e.getMessage());
            MyLog.w(pngPath + " 出错！" + e.getMessage());
        }
    }

    /**
     * 将指定坐标的rbg改为透明
     * 
     * @param im
     * @param rgbList
     * @return
     */
    private Image makeColorTransparent(BufferedImage im,
            final List<String> rgbList) {
        ImageFilter filter = new RGBImageFilter() {

            @Override
            public final int filterRGB(int x, int y, int rgb) {
                String key = x + "_" + y;
                if (rgbList.contains(key)) {
                    return 0x00FFFFFF & rgb;
                } else {
                    return rgb;
                }
            }
        };
        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    public void drawGB2312() {
        mRunning = true;
        String txt = TextUtils.readFile(SystemConfig.FileSystem.SHADOW_PATH
                + "GB2312.txt");
        txt = TextUtils.replaceBlank(txt);
        startDrawText(txt);
    }

    /**
     * 开始绘制文本到图片
     * 
     * @param text
     */
    private void startDrawText(String text) {
        MyLog.w("正在生成文字图片....");
        List<File> fileList = FontelloService.getInstance().getFiles(
                SystemConfig.FileSystem.SHADOW_PATH, ".ttf", ".otf");

        if (fileList != null && fileList.size() > 0) {
            for (int i = 0; i < text.length(); i++) {
                for (File file : fileList) {
                    if (!mRunning) {
                        MyLog.w("操作已中止");
                        return;
                    }
                    String c = text.charAt(i) + "";
                    String out = SystemConfig.FileSystem.SHADOW_PATH
                            + TextUtils.getFileName(file) + File.separator + c
                            + ".png";
//                    if (new File(out).exists()) {
//                        System.out.println(out + " 已存在，不重复生成！");
//                        MyLog.w(out + " 已存在，不重复生成！");
//                        continue;
//                    }
                    Ttf2png.ttf2png(file, out, c, mTextSize, mPngSize);
                    if (!mRunning) {
                        MyLog.w("操作已中止");
                        return;
                    }
                }
            }
        }
        MyLog.w("文字图片生成完毕!");
    }

    public void setTextSize(float textSize) {
        this.mTextSize = textSize;
    }

    public void setPngSize(float pngSize) {
        this.mPngSize = pngSize;
    }
    
    public void stop() {
        mRunning = false;
    }
}

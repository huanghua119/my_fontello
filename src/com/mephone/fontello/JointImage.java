package com.mephone.fontello;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class JointImage {

    public static void main(String[] args) {
        JointImage ji = new JointImage();
        // ji.mergePng("data/joint/楷体128_128/吖.png",
        // "data/joint/宋体128_128/吖.png");
        ji.mergeAllPng("data/joint/128_128/楷体", "data/joint/128_128/宋体");
    }

    public void mergeAllPng(String leftDir, String rightDir) {
        if (!new File(leftDir).exists() || !new File(rightDir).exists()) {
            System.out.println("目录不存在!");
            return;
        }
        File[] leftPngFiles = FontelloService.getInstance().traverseDir(
                leftDir, ".png");
        int okCount = 0;
        for (File leftPng : leftPngFiles) {
            String rightPng = rightDir + File.separator + leftPng.getName();
            boolean isOk = mergePng(leftPng.getAbsolutePath(), rightPng);
            if (isOk) {
                okCount++;
            }
        }
        System.out.println("合并成功，共合并：" + okCount + "个文件!");
    }

    public boolean mergePng(String left, String right) {
        try {
            File leftFile = new File(left);
            File rightFile = new File(right);
            if (!leftFile.exists() || !rightFile.exists()) {
                System.out.println(left + " 或  " + right + " 文件不存在 !");
                return false;
            }

            BufferedImage leftbi = ImageIO.read(leftFile);
            BufferedImage rightbi = ImageIO.read(rightFile);

            BufferedImage newImage = mergeImage(true, leftbi, rightbi);

            String newFile = "data/joint/" + leftFile.getParentFile().getName()
                    + "_" + rightFile.getParentFile().getName()
                    + File.separator + leftFile.getName();
            File output = new File(newFile);
            if (!output.getParentFile().exists()) {
                output.getParentFile().mkdirs();
            }
            System.out.println("out ===> " + output);
            return ImageIO.write(newImage, "PNG", output);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 合并任数量的图片成一张图片
     * 
     * @param isHorizontal
     *            true代表水平合并，fasle代表垂直合并
     * @param imgs
     *            待合并的图片数组
     * @return
     * @throws IOException
     */
    private BufferedImage mergeImage(boolean isHorizontal,
            BufferedImage... imgs) throws IOException {
        // 生成新图片
        BufferedImage destImage = null;
        // 计算新图片的长和高
        int allw = 0, allh = 0, allwMax = 0, allhMax = 0;
        // 获取总长、总宽、最长、最宽
        for (int i = 0; i < imgs.length; i++) {
            BufferedImage img = imgs[i];
            allw += img.getWidth();
            allh += img.getHeight();
            if (img.getWidth() > allwMax) {
                allwMax = img.getWidth();
            }
            if (img.getHeight() > allhMax) {
                allhMax = img.getHeight();
            }
        }
        // 创建新图片
        if (isHorizontal) {
            destImage = new BufferedImage(allw, allhMax,
                    BufferedImage.TYPE_INT_RGB);
        } else {
            destImage = new BufferedImage(allwMax, allh,
                    BufferedImage.TYPE_INT_RGB);
        }
        // 合并所有子图片到新图片
        int wx = 0, wy = 0;
        for (int i = 0; i < imgs.length; i++) {
            BufferedImage img = imgs[i];
            int w1 = img.getWidth();
            int h1 = img.getHeight();
            // 从图片中读取RGB
            int[] ImageArrayOne = new int[w1 * h1];
            ImageArrayOne = img.getRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 逐行扫描图像中各个像素的RGB到数组中
            if (isHorizontal) { // 水平方向合并
                destImage.setRGB(wx, 0, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
            } else { // 垂直方向合并
                destImage.setRGB(0, wy, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
            }
            wx += w1;
            wy += h1;
        }
        return destImage;
    }
}

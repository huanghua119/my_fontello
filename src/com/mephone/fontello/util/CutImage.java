package com.mephone.fontello.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.mephone.fontello.bean.ImageDto;
import com.mephone.fontello.config.MyLog;
import com.mephone.fontello.config.SystemConfig;

public class CutImage {
    public static void main(String[] args) {
        File file = new File(SystemConfig.FileSystem.PNG_DIR + "jz_jiuzheng3.jpg");
        try {
            SystemConfig.DefalutConfig.sPNG_BLACK_SIDE_WIDTH = 10;
            SystemConfig.DefalutConfig.sPNG_BLACK_SIDE_HEIGHT = 10;
            SystemConfig.DefalutConfig.sPNG_WIDTH = 160;
            SystemConfig.DefalutConfig.sPNG_HEIGHT = 160;
            cut2(file, "", 12, 16);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ImageDto cut2(File sourceFile, String fileNames, int cols,
            int rows) throws IOException {
        List<File> list = new ArrayList<File>();
        BufferedImage source = ImageIO.read(sourceFile);
        int sWidth = source.getWidth(); // 图片宽度
        int sHeight = source.getHeight(); // 图片高度
        int eWidth = 0; // 末端切片宽度
        int eHeight = 0; // 末端切片高度
        int width = sWidth / cols;
        int height = sHeight / rows;
        String fileName = null;
        fileNames = TextUtils.replaceBlank(fileNames);

        String targetDir = sourceFile.getAbsolutePath();
        targetDir = targetDir.substring(0, targetDir.lastIndexOf("."));
        File file = new File(targetDir);
        if (!file.exists()) { // 存储目录不存在，则创建目录
            file.mkdirs();
        }
        BufferedImage image = null;
        int cWidth = 0; // 当前切片宽度
        int cHeight = 0; // 当前切片高度

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (SystemConfig.DefalutConfig.sPNG_WIDTH != 0) {
                    cWidth = SystemConfig.DefalutConfig.sPNG_WIDTH;
                } else {
                    cWidth = getWidth(j, cols, eWidth, width);
                }
                if (SystemConfig.DefalutConfig.sPNG_HEIGHT != 0) {
                    cHeight = SystemConfig.DefalutConfig.sPNG_HEIGHT;
                } else {
                    cHeight = getHeight(i, rows, eHeight, height);
                }

                int startX = j
                        * (cWidth) + SystemConfig.DefalutConfig.sPNG_BLACK_SIDE_WIDTH;
                int startY = i
                        * (cHeight) + SystemConfig.DefalutConfig.sPNG_BLACK_SIDE_HEIGHT;

                int office = SystemConfig.DefalutConfig.PNG_BLACK_SIDE_OFFICE;
                int endX = cWidth - SystemConfig.DefalutConfig.sPNG_BLACK_SIDE_WIDTH * 2;
                int endY = cHeight - SystemConfig.DefalutConfig.sPNG_BLACK_SIDE_HEIGHT * 2;

                if ((startX + endX) > sWidth) {
                    office = startX + endX - sWidth;
                }
                // x坐标,y坐标,宽度,高度
                image = source.getSubimage(startX, startY, endX - office, endY);

                if (TextUtils.isEmpty(fileNames)
                        && SystemConfig.DefalutConfig.sPNG_NO_NAME) {
                    fileName = targetDir + "/map_" + i + "_" + j + ".png";
                    MyLog.i("save fileName:" + fileName);
                } else {
                    int index = i * cols + (j + 1);
                    if (index > fileNames.length()) {
                        break;
                    }
                    String name = fileNames.substring(index - 1, index);
                    fileName = targetDir + "/" + name + ".png";
                    MyLog.i("save unicode:" + name + " fileName:" + fileName);
                }
                if (i % 2 != 0) {
                    file = new File(fileName);
                    ImageIO.write(image, "PNG", file);
                    list.add(file);
                }
            }
        }
        return new ImageDto(sWidth, sHeight, list);
    }

    /**
     * 切割图片
     * 
     * @param sourceFile
     *            源文件
     * @param targetDir
     *            存储目录
     * @param width
     *            切片宽度
     * @param height
     *            切片高度
     * @return
     * @throws Exception
     */
    public static ImageDto cut(File sourceFile, String targetDir, int width,
            int height) throws Exception {
        List<File> list = new ArrayList<File>();
        BufferedImage source = ImageIO.read(sourceFile);
        int sWidth = source.getWidth(); // 图片宽度
        int sHeight = source.getHeight(); // 图片高度
        if (sWidth > width && sHeight > height) {
            int cols = 0; // 横向切片总数
            int rows = 0; // 纵向切片总数
            int eWidth = 0; // 末端切片宽度
            int eHeight = 0; // 末端切片高度
            if (sWidth % width == 0) {
                cols = sWidth / width;
            } else {
                eWidth = sWidth % width;
                cols = sWidth / width + 1;
            }
            if (sHeight % height == 0) {
                rows = sHeight / height;
            } else {
                eHeight = sHeight % height;
                rows = sHeight / height + 1;
            }
            String fileName = null;
            File file = new File(targetDir);
            if (!file.exists()) { // 存储目录不存在，则创建目录
                file.mkdirs();
            }
            BufferedImage image = null;
            int cWidth = 0; // 当前切片宽度
            int cHeight = 0; // 当前切片高度
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    cWidth = getWidth(j, cols, eWidth, width);
                    cHeight = getHeight(i, rows, eHeight, height);
                    // x坐标,y坐标,宽度,高度
                    image = source.getSubimage(j * width, i * height, cWidth,
                            cHeight);
                    fileName = targetDir + "/map_" + i + "_" + j + ".jpg";
                    file = new File(fileName);
                    ImageIO.write(image, "JPEG", file);
                    list.add(file);
                }
            }
        }
        return new ImageDto(sWidth, sHeight, list);
    }

    /**
     * 切割图片
     * 
     * @param source
     *            源文件路径
     * @param targetDir
     *            存储目录
     * @param width
     *            切片宽度
     * @param height
     *            切片高度
     * @return
     * @throws Exception
     */
    public static ImageDto cut(String source, String targetDir, int width,
            int height) throws Exception {
        return cut(new File(source), targetDir, width, height);
    }

    /**
     * 获取当前切片的宽度
     * 
     * @param index
     *            横向索引
     * @param cols
     *            横向切片总数
     * @param endWidth
     *            末端切片宽度
     * @param width
     *            切片宽度
     * @return
     */
    private static int getWidth(int index, int cols, int endWidth, int width) {
        if (index == cols - 1) {
            if (endWidth != 0) {
                return endWidth;
            }
        }
        return width;
    }

    /**
     * 获取当前切片的高度
     * 
     * @param index
     *            纵向索引
     * @param rows
     *            纵向切片总数
     * @param endHeight
     *            末端切片高度
     * @param height
     *            切片高度
     * @return
     */
    private static int getHeight(int index, int rows, int endHeight, int height) {
        if (index == rows - 1) {
            if (endHeight != 0) {
                return endHeight;
            }
        }
        return height;
    }
}

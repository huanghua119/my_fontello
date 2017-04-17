package com.mephone.fontello.config;

public class SystemConfig {

    public static class FileSystem {

        /**
         * 数据根目录
         */
        public static final String ROOT_DIR = "data/";

        /**
         * 源png目录
         */
        public static final String PNG_DIR = ROOT_DIR + "png/";

        /**
         * 切割png目录
         */
        public static final String PNG_CUT_DIR = PNG_DIR + "cut/";

        /**
         * 源svg目录
         */
        public static final String SVG_DIR = ROOT_DIR + "svg/";

        public static final String CONFIG_FILE = ROOT_DIR + "config.json";

        public static final String FILE_6763 = ROOT_DIR + "6763.txt";

        /**
         * 最终生成的svg文件
         */
        public static final String FONTELLO_SVG = ROOT_DIR + "fontello.svg";

        /**
         * 最终生成的ttf文件
         */
        public static final String FONTELLO_TTF = ROOT_DIR + "fontello.ttf";

        public static final String CACHE_PATH = SystemConfig.FileSystem.ROOT_DIR
                + ".cache/";
    }

    public static class DefalutConfig {

        public static int sCUT_PNG_COLS = 25;

        public static int sCUT_PNG_ROWS = 18;

        /**
         * 字块正常宽度
         */
        public static int sPNG_WIDTH = 0;

        /**
         * 字块正常高度
         */
        public static int sPNG_HEIGHT = 0;

        /**
         * 两则黑边
         */
        public static int sPNG_BLACK_SIDE_WIDTH = 0;

        /**
         * 上下黑边
         */
        public static int sPNG_BLACK_SIDE_HEIGHT = 0;

        public static int PNG_BLACK_SIDE_OFFICE = 0;

        public static boolean sPNG_NO_NAME = false;

        public static final String ascent = "850";

        public static final String unitsPerEm = "1000";

        public static final String fontName = "iekie";

        public static final String copyRight = "Copyright by iekie & huanghua@iekie.com";

        /**
         * 缓存数据超时时间
         */
        public static final int CACHEL_EXPIRED_TIME = 60 * 60 * 24 * 7;
    }

}

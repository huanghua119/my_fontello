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
         * 设置文件目录
         */
        public static final String SETTING_DIR = "settings/";

        /**
         * path数据临时目录
         */
        public static final String TEMP_PATH = ROOT_DIR + "temp.path";

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

        /**
         * 检测TTF主目录
         */
        public static final String CHECK_PATH = ROOT_DIR + "check/";

        /**
         * 检测TTF 字符目录
         */
        public static final String CHECK_TXT = CHECK_PATH + "txt/";

        /**
         * 检测TTF 字库目录
         */
        public static final String CHECK_TTF = CHECK_PATH + "ttf/";

        /**
         * 提取字符主目录
         */
        public static final String DRAW_PATH = ROOT_DIR + "draw/";

        /**
         * 提取TTF 字库目录
         */
        public static final String DRAW_TTF = DRAW_PATH + "ttf/";

        /**
         * 字库目录
         */
        public static final String FONTS_PATH = ROOT_DIR + "fonts/";
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

        public static boolean sPNG_NO_NAME = true;

        /**
         * 是否是GB2312完整字库
         */
        public static boolean sGB2312_NAME = false;

        /**
         * 是否分段取字库名
         */
        public static boolean sNAME_SPITE = false;

        /**
         * svg图片列
         */
        public static int sCUT_SVG_COLS = 25;

        /**
         * svg图片行
         */
        public static int sCUT_SVG_ROWS = 20;

        /**
         * 切割svg图片宽
         */
        public static int sCUT_SVG_WIDTH = 56;

        /**
         * 切割svg图片高
         */
        public static int sCUT_SVG_HEIGHT = 70;

        public static final String ascent = "221";

        public static final String unitsPerEm = "281";

        public static final String fontName = "iekie";

        public static final String copyRight = "Copyright by iekie & huanghua@iekie.com";

        /**
         * 缓存数据超时时间
         */
        public static final int CACHEL_EXPIRED_TIME = 60 * 60 * 24 * 7;
    }

}

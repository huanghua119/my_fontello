package com.mephone.fontello.config;

public class SystemConfig {

    public static class FileSystem {

        /**
         * 数据根目录
         */
        public static final String ROOT_DIR = "data/";

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
    }

    public static class DefalutConfig {

        public static final String ascent = "850";

        public static final String unitsPerEm = "1000";

        public static final String fontName = "iekie";

        public static final String copyRight = "Copyright by iekie & huanghua@iekie.com";
    }

}

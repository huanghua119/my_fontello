package com.mephone.fontello;

import java.io.File;
import java.io.FileFilter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mephone.fontello.bean.FontSvg;
import com.mephone.fontello.bean.FontelloSvg;
import com.mephone.fontello.bean.SvgConfig;
import com.mephone.fontello.config.MyLog;
import com.mephone.fontello.config.SystemConfig;
import com.mephone.fontello.svg.SVGParser;
import com.mephone.fontello.table.ComputerTable;
import com.mephone.fontello.util.Cmd;
import com.mephone.fontello.util.CommonUtils;
import com.mephone.fontello.util.CutImage;
import com.mephone.fontello.util.JsonUtils;
import com.mephone.fontello.util.TextUtils;

public class FontelloService {

    private static FontelloService sService;

    public static final String CMD_GEN_CONFIG = "gen_config";

    /**
     * 切割png图片
     */
    public static final String CMD_CUT_PNG = "cut_png";

    /**
     * png图片转svg图片
     */
    public static final String CMD_PNG_2_SVG = "png_2_svg";

    /**
     * 切割svg图片
     */
    public static final String CMD_CUT_SVG = "cut_svg";

    private Map<String, String> mHaiZiMap = new HashMap<String, String>();

    private ComputerTable mComputerTable = null;

    private boolean isActivation = false;

    /**
     * 单例
     */
    private FontelloService() {
        init();
    }

    private void init() {
        mHaiZiMap.clear();
        String text = TextUtils.readFile(SystemConfig.FileSystem.FILE_6763);
        if (TextUtils.isEmpty(text)) {
            text = TextUtils.readFile(FontelloService.class
                    .getResourceAsStream("/com/mephone/fontello/6763.txt"));
        }
        String[] lines = text.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (TextUtils.isEmpty(line)) {
                continue;
            }
            String value = line.substring(0, 1).trim();
            String key = line.substring(1, line.length()).trim();
            mHaiZiMap.put(key, value);
            //MyLog.i(key + " " + value);
        }
        isActivation = isEffectiveComputer();
    }

    public static FontelloService getInstance() {
        if (sService == null) {
            synchronized (FontelloService.class) {
                if (sService == null) {
                    sService = new FontelloService();
                }
            }
        }
        return sService;
    }

    public void doButtonCmd(String cmd, String... args) {
        if (CMD_GEN_CONFIG.equals(cmd)) {
            SvgConfig config = new SvgConfig();
            config.setAscent(SystemConfig.DefalutConfig.ascent);
            config.setUnitsPerEm(SystemConfig.DefalutConfig.unitsPerEm);
            config.setSvgDir(SystemConfig.FileSystem.SVG_DIR);
            generateConfig(config);
            doFontello();
        } else if (CMD_CUT_PNG.equals(cmd)) {
            cutPng();
        } else if (CMD_PNG_2_SVG.equals(cmd)) {
            png2svg();
        } else if (CMD_CUT_SVG.equals(cmd)) {
            cutSvg();
        }
    }

    /**
     * 读取config文件并生成字库
     * 
     * @return
     */
    private boolean doFontello() {
        File file = new File(SystemConfig.FileSystem.CONFIG_FILE);
        if (!file.exists()) {
            MyLog.i("config.json文件不存在，请重新生成！");
            return false;
        }

        JSONObject configJson = JsonUtils.stringToJSONObject(TextUtils
                .readFile(file.getAbsolutePath()));

        int ascent = JsonUtils.getJSONInt(configJson, "ascent");
        int units_per_em = JsonUtils.getJSONInt(configJson, "units_per_em");
        int descent = ascent - units_per_em;
        String familyname = JsonUtils.getJSONString(configJson, "name");
        JSONArray glyphs = JsonUtils.getJSONArray(configJson, "glyphs");

        FontelloSvg svg = new FontelloSvg();
        svg.setAscent(ascent + "");
        svg.setDescent(descent + "");
        svg.setUnitsPerEm(units_per_em + "");
        svg.setFontFamily(familyname);
        svg.setFontWeight("400");
        svg.setFontStretch("normal");

        StringBuffer glyph = new StringBuffer();
        for (int i = 0; i < glyphs.size(); i++) {
            JSONObject glyphJson = glyphs.getJSONObject(i);
            String code = Integer.toHexString(JsonUtils.getJSONInt(glyphJson,
                    "code"));

            String css = JsonUtils.getJSONString(glyphJson, "css");
            JSONObject svgJson = JsonUtils.getJSONObject(glyphJson, "svg");
            String path = JsonUtils.getJSONString(svgJson, "path");
            float width = units_per_em * JsonUtils.getJSONInt(svgJson, "width")
                    / 1000;
            BigDecimal bd = new BigDecimal(width);
            width = bd.setScale(1, BigDecimal.ROUND_FLOOR).floatValue();

            float scale = units_per_em / 1000f;
            int newTx = 0;
            int newTy = ascent;
            float newSx = scale;
            float newSy = -scale;
            String newPath = changePath(path, newTx + "", newTy + "", newSx
                    + "", newSy + "", 2);

            String g = "<glyph glyph-name=\"" + css + "\" unicode=\"&#x" + code
                    + ";\" d=\"" + newPath + "\" horiz-adv-x=\"" + width
                    + "\" />";
            glyph.append(g);
        }
        svg.setGlyph(glyph.toString());
        SVGParser.getInstance().generateSVGFont(
                svg,
                SystemConfig.FileSystem.FONTELLO_SVG.replace("fontello",
                        familyname));
        svg2ttf(SystemConfig.FileSystem.FONTELLO_SVG.replace("fontello",
                familyname), SystemConfig.FileSystem.FONTELLO_TTF.replace(
                "fontello", familyname));

        return new File(SystemConfig.FileSystem.FONTELLO_TTF.replace(
                "fontello", familyname)).exists();
    }

    public boolean startWork(int step, SvgConfig config) {
        if (step == 1) {
            return generateConfig(config);
        } else if (step == 2) {
            return doFontello();
        }
        return false;
    }

    /**
     * 生成config.json配置文件
     * 
     * @param config
     * @return
     */
    private boolean generateConfig(SvgConfig config) {
        if (config == null) {
            MyLog.w("没有config.json信息!");
            MyLog.i("没有config.json信息!");
            return false;
        }
        List<FontSvg> svgFileList = readSvgFile(config.getSvgDir());

        JSONArray glyphsArray = new JSONArray();
        for (FontSvg svg : svgFileList) {
            JSONObject glyphsObject = new JSONObject();

            String name = svg.getName();
            String unicode = svg.getUnicode();

            String viewBox = svg.getViewBox();
            String[] boxs = viewBox.split(" ");

            int width = (int) Math
                    .round(1000 * (Float.parseFloat(boxs[2]) / Float
                            .parseFloat(boxs[3])));

            String newPath = getSvgPath(svg);
            JSONObject svgObject = new JSONObject();
            svgObject.put("path", newPath);
            svgObject.put("width", width);
            String uid = UUID.randomUUID().toString();
            glyphsObject.put("uid", uid);
            glyphsObject.put("css", name);
            try {
                glyphsObject.put("code", Integer.parseInt(unicode));
            } catch (Exception e) {
                glyphsObject.put("code", 10000);
            }
            glyphsObject.put("src", "custom_icons");
            glyphsObject.put("selected", true);
            glyphsObject.put("svg", svgObject);
            glyphsArray.add(glyphsObject);
        }

        JSONObject configJson = new JSONObject();
        configJson.put("name", config.getName());
        configJson.put("css_prefix_text", "icon-");
        configJson.put("css_use_suffix", false);
        configJson.put("hinting", true);
        configJson
                .put("units_per_em", Integer.parseInt(config.getUnitsPerEm()));
        configJson.put("ascent", Integer.parseInt(config.getAscent()));
        configJson.put("glyphs", glyphsArray);

        TextUtils.saveFileText(JsonUtils.formatJson(configJson.toString()),
                SystemConfig.FileSystem.CONFIG_FILE);
        svgFileList.clear();
        svgFileList = null;
        configJson.clear();
        configJson = null;
        glyphsArray.clear();
        glyphsArray = null;

        return true;
    }

    /**
     * 获取 svg 图片path
     * 
     * @param svg
     * @return
     */
    private String getSvgPath(FontSvg svg) {
        String path = svg.getPathD();
        String transform = svg.getTransform();
        String viewBox = svg.getViewBox();

        String[] boxs = viewBox.split(" ");
        float width = Float.parseFloat(boxs[2]);
        float height = Float.parseFloat(boxs[3]);
        float translateX = 0f;
        float translateY = 0f;
        float scaleX = 1f;
        float scaleY = 1f;

        boolean hasTranslate = transform.contains("translate");
        boolean hasScale = transform.contains("scale");
        if (hasTranslate && hasScale) { // 包含translate和scale
            String[] transforms = transform.split("\\) ");
            String translate = transforms[0].substring(
                    transforms[0].indexOf("(") + 1).trim();
            String scale = transforms[1].substring(
                    transforms[1].indexOf("(") + 1,
                    transforms[1].lastIndexOf(")")).trim();
            int index = translate.indexOf(",");
            if (index == -1) {
                index = translate.indexOf(" ");
            }
            translateX = Float.parseFloat(translate.substring(0, index));
            translateY = Float.parseFloat(translate.substring(index + 1));

            index = scale.indexOf(",");
            if (index == -1) {
                index = scale.indexOf(" ");
            }
            scaleX = Float.parseFloat(scale.substring(0, index));
            scaleY = Float.parseFloat(scale.substring(index + 1));
        } else if (hasTranslate && !hasScale) { // 包含translate
            String translate = transform.substring(transform.indexOf("(") + 1,
                    transform.indexOf(")")).trim();
            int index = translate.indexOf(",");
            if (index == -1) {
                index = translate.indexOf(" ");
            }
            translateX = Float.parseFloat(translate.substring(0, index));
            translateY = Float.parseFloat(translate.substring(index + 1));
        } else if (!hasTranslate && hasScale) { // 包含scale
            String scale = transform.substring(transform.indexOf("(") + 1,
                    transform.indexOf(")")).trim();
            int index = scale.indexOf(",");
            if (index == -1) {
                index = scale.indexOf(" ");
            }
            scaleX = Float.parseFloat(scale.substring(0, index));
            scaleY = Float.parseFloat(scale.substring(index + 1));
        }

        float newTx = translateX / scaleX;
        float newTy = translateY / scaleY;

        float x = width / height;

        float newSx = 1000 * x / (width / scaleX);
        float newSy = 1000 / (height / scaleY);
        // translate ( translateX / scaleX, translateY / scaleY)
        // scale( 1000 /( width / scaleX) , 1000 / ( height / scaleY))
        return changePath(path, newTx + "", newTy + "", newSx + "", newSy + "",
                1);
    }

    /**
     * 改变文字svg图片path路径
     * 
     * @param path
     * @param translateX
     * @param translateY
     * @param scaleX
     * @param scaleY
     * @param type
     * @return
     */
    private String changePath(String path, String translateX,
            String translateY, String scaleX, String scaleY, int type) {
        path = path.replace(" ", "_");

        String content = path + "\n" + translateX + " " + translateY + " "
                + scaleX + " " + scaleY + " " + type;

        File tempPath = new File(SystemConfig.FileSystem.TEMP_PATH);
        TextUtils.saveFileText(content, tempPath.getAbsolutePath());
        String cmd = "svgpath \"" + tempPath.getAbsolutePath() + "\"";
        String result = Cmd.run(cmd, false);
        tempPath.delete();
        return result;
    }

    /**
     * 读取指定目录下载的svg图片
     * 
     * @param svgPath
     * @return
     */
    private List<FontSvg> readSvgFile(String svgPath) {
        List<FontSvg> result = new ArrayList<FontSvg>();
        File file = new File(svgPath);
        if (file.exists() && file.isDirectory()) {
            File[] svgFiles = file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isFile()
                            && pathname.getName().endsWith(".svg");
                }
            });
            for (File svg : svgFiles) {
                FontSvg fs = SVGParser.getInstance().parserFontSvg(
                        svg.getAbsolutePath());
                if (fs != null) {
                    if (mHaiZiMap != null
                            && mHaiZiMap.containsKey(fs.getName())) {
                        String name = mHaiZiMap.get(fs.getName());
                        fs.setName(name);
                        fs.setUnicode(TextUtils.string2Unicode(name));
                    } else {
                        if (fs.getName().length() == 1) {
                            fs.setUnicode(TextUtils.string2Unicode(fs.getName()));
                        } else {
                            fs.setUnicode(fs.getName());
                        }
                    }
                    result.add(fs);
                }
            }
        }
        return result;
    }

    /**
     * svg转为ttf
     * 
     * @param path
     * @param toPath
     */
    private void svg2ttf(String path, String toPath) {
        if (new File(path).exists()) {
            String cmd = "svg2ttf " + path + " " + toPath;
            Cmd.run(cmd, false);
        }
    }

    public boolean isEffectiveComputer() {
        if (mComputerTable == null) {
            mComputerTable = new ComputerTable();
        }
        String mac = CommonUtils.getLocalMac();
        return mComputerTable.isEffectiveComputer(mac);
    }

    public boolean isActivation() {
        return this.isActivation;
    }

    /**
     * 遍历data/png目录
     * 
     * @return 返回子文件
     */
    public File[] traversePngDir() {
        return traverseDir(SystemConfig.FileSystem.PNG_DIR, "");
    }

    /**
     * 遍历目录
     * @param path
     * @return
     */
    public File[] traverseDir(String path, final String end) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            return file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if (TextUtils.isEmpty(end)) {
                        return pathname.isFile();
                    }
                    return pathname.isFile()
                            && pathname.getName().endsWith(end);
                }
            });
        }
        return null;
    }

    /**
     * 遍历data/png目录下的子目录，将png图片转化为svg文件
     */
    public void png2svg() {
        File[] files = traversePngDir();
        for (File file : files) {
            if (file.exists() && file.isDirectory()) {
                File[] pngs = file.listFiles();
                for (File png : pngs) {
                    if (png.exists() && png.getName().endsWith(".png")) {
                        png2svg(png);
                    }
                }
            }
        }
    }

    /**
     * png图片转化为svg文件
     * 
     * @param png
     */
    public void png2svg(File png) {
        if (!png.exists()) {
            MyLog.i("png2svg error:" + png.getName() + " not found!");
        }
        String outDir = png.getParentFile().getAbsolutePath() + "/svg";
        String name = png.getName();

        File out = new File(outDir);
        if (!out.exists()) {
            out.mkdirs();
        }

        name = outDir + File.separator + name.replace(".png", ".svg");

        String cmd1 = "convert -flatten " + png.getAbsolutePath()
                + " output.pbm";
        String cmd2 = "potrace -s output.pbm -o " + name;

        String cmd3 = "rm output.pbm";

        Cmd.run(cmd1, false);
        Cmd.run(cmd2, false);
        Cmd.run(cmd3, false);
    }

    /**
     * 切割png图片
     */
    private void cutPng() {
        File[] pngs = traversePngDir();
        if (pngs != null) {
            for (File file : pngs) {
                if (file.getName().endsWith(".png")) {
                    MyLog.i("file:" + file.getAbsolutePath());
                    try {
                        String names = TextUtils.readFile(file
                                .getAbsolutePath().replace(".png", ".txt"));
                        if (TextUtils.isEmpty(names)
                                && !SystemConfig.DefalutConfig.sPNG_NO_NAME) {
                            MyLog.w("字样图片对应的文本文件没找到 fileName:" + file.getName());
                        } else {
                            CutImage.cut2(
                                    file,
                                    names,
                                    SystemConfig.DefalutConfig.sCUT_PNG_COLS,
                                    SystemConfig.DefalutConfig.sCUT_PNG_ROWS);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 切割svg图片
     */
    private void cutSvg() {
        File[] pngs = traversePngDir();
        if (pngs != null) {
            for (File file : pngs) {
                if (file.getName().endsWith(".svg")) {
                    MyLog.i("file:" + file.getAbsolutePath());
                    String names = TextUtils.readFile(file
                            .getAbsolutePath().replace(".svg", ".txt"));
                    names = TextUtils.replaceBlank(names);
                    if (TextUtils.isEmpty(names)
                            && !SystemConfig.DefalutConfig.sPNG_NO_NAME) {
                        MyLog.w("字样图片对应的文本文件没找到 fileName:" + file.getName());
                    } else {
                        SVGParser.getInstance().cutSvg(file, names,
                                SystemConfig.DefalutConfig.sCUT_SVG_COLS,
                                SystemConfig.DefalutConfig.sCUT_SVG_ROWS,
                                SystemConfig.DefalutConfig.sCUT_SVG_WIDTH,
                                SystemConfig.DefalutConfig.sCUT_SVG_HEIGHT);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        FontelloService.getInstance().doButtonCmd(CMD_CUT_SVG, "");
    }
}

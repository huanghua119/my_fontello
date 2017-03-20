package com.mephone.fontello;

import java.io.File;
import java.io.FileFilter;
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
import com.mephone.fontello.util.Cmd;
import com.mephone.fontello.util.JsonUtils;
import com.mephone.fontello.util.TextUtils;

public class FontelloService {

    private static FontelloService sService;

    public static final String CMD_GEN_CONFIG = "gen_config";

    private Map<String, String> mHaiZiMap = new HashMap<String, String>();

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
            MyLog.i(key + " " + value);
        }
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
        }
    }

    private boolean doFontello() {
        File[] jsonFile = getJsonFile();
        if (jsonFile == null || jsonFile.length == 0) {
            MyLog.i("config.json文件不存在，请重新生成！");
            return false;
        }

        boolean ok = true;
        for (File json : jsonFile) {
            String cmd = "fontello-cli --config " + json.getAbsolutePath()
                    + " install";
            boolean complete = false;
            int count = 0;
            do {
                complete = false;
                count++;
                MyLog.i("start complete:" + complete);
                MyLog.w("start complete:" + complete);
                String result = Cmd.run(cmd, true);
                if (!TextUtils.isEmpty(result)
                        && result.contains("Install complete")) {
                    complete = true;
                }
                MyLog.i("end complete:" + complete);
                MyLog.w("end complete:" + complete);
            } while (!complete && count < 10);
            if (!complete) {
                ok = false;
                break;
            }
        }
        if (ok) {
            ok = mergeTTF();
        }
        return ok;
    }

    public boolean startWork(int step,SvgConfig config) {
        if (step == 1) {
            return generateConfig(config);
        } else if (step == 2) {
            return doFontello();
        }
        return false;
    }

    private boolean generateConfig(SvgConfig config) {
        if (config == null) {
            MyLog.w("没有config.json信息!");
            MyLog.i("没有config.json信息!");
            return false;
        }
        List<FontSvg> svgFileList = readSvgFile(config.getSvgDir());
        
        int size = svgFileList.size() / SystemConfig.DefalutConfig.CONFIG_FILE_SIZE;
        size = svgFileList.size() % SystemConfig.DefalutConfig.CONFIG_FILE_SIZE == 0 ? size
                : size + 1;
        System.out.println("size:" + size);
        for (int i = 0; i < size; i++) {
            JSONArray glyphsArray = new JSONArray();
            for (int j = 0; j < SystemConfig.DefalutConfig.CONFIG_FILE_SIZE; j++) {
                JSONObject glyphsObject = new JSONObject();

                int index = j + i * SystemConfig.DefalutConfig.CONFIG_FILE_SIZE;
                if (index >= svgFileList.size()) {
                    break;
                }

                FontSvg svg = svgFileList.get(index);
                String name = svg.getName();
                String unicode = svg.getUnicode();

                String newPath = getSvgPath(svg);
                JSONObject svgObject = new JSONObject();
                svgObject.put("path", newPath);
                svgObject.put("width", 1000);
                String uid = UUID.randomUUID().toString();
                glyphsObject.put("uid", uid);
                glyphsObject.put("css", name);
                glyphsObject.put("code", Integer.parseInt(unicode));
                glyphsObject.put("src", "custom_icons");
                glyphsObject.put("selected", true);
                glyphsObject.put("svg", svgObject);
                glyphsArray.add(glyphsObject);
            }

            JSONObject configJson = new JSONObject();
            configJson.put("name", "");
            configJson.put("css_prefix_text", "icon-");
            configJson.put("css_use_suffix", false);
            configJson.put("hinting", true);
            configJson.put("units_per_em", Integer.parseInt(config.getUnitsPerEm()));
            configJson.put("ascent", Integer.parseInt(config.getAscent()));
            configJson.put("glyphs", glyphsArray);

            TextUtils.saveFileText(
                    JsonUtils.formatJson(configJson.toString()),
                    SystemConfig.FileSystem.CONFIG_FILE.replace("x", (i + 1)
                            + ""));
        }

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
        float newSx = 1000 / (width / scaleX);
        float newSy = 1000 / (height / scaleY);
        // translate ( translateX / scaleX, translateY / scaleY)
        // scale( 1000 /( width / scaleX) , 1000 / ( height / scaleY))
        return changePath(path, newTx, newTy, newSx, newSy);
    }

    private String changePath(String path, float translateX, float translateY,
            float scaleX, float scaleY) {
        String cmd = "svgpath \"" + path + "\" " + translateX + " "
                + translateY + " " + scaleX + " " + scaleY;
        return Cmd.run(cmd, false);
    }

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
                    if (mHaiZiMap != null && mHaiZiMap.containsKey(fs.getName())) {
                        String name = mHaiZiMap.get(fs.getName());
                        fs.setName(name);
                        fs.setUnicode(TextUtils.string2Unicode(name));
                    } else {
                        fs.setUnicode(TextUtils.string2Unicode(fs.getName()));
                    }
                    result.add(fs);
                }
            }
        }
        return result;
    }

    public File[] getJsonFile() {
        File file = new File(SystemConfig.FileSystem.ROOT_DIR);
        File[] jsonFile = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile()
                        && pathname.getName().endsWith(".json");
            }
        });
        return jsonFile;
    }

    public boolean mergeTTF() {
        File file = new File(".");
        File[] fontellos = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory()
                        && pathname.getName().startsWith("fontello");
            }
        });
        List<FontelloSvg> fontellList = new ArrayList<>();
        for (File f : fontellos) {
            File ttf = new File(f.getAbsolutePath() + "/font/fontello.svg");
            if (ttf.exists()) {
                fontellList.add(SVGParser.getInstance().parserFontelloSvgText(ttf.getAbsolutePath()));
            }
        }
        SVGParser.getInstance().mergeFontellSvgText(SystemConfig.FileSystem.FONTELLO_SVG, fontellList);

        if (new File(SystemConfig.FileSystem.FONTELLO_SVG).exists()) {
            String cmd = "svg2ttf " + SystemConfig.FileSystem.FONTELLO_SVG
                    + SystemConfig.FileSystem.FONTELLO_TTF;
            Cmd.run(cmd, false);
            return true;
        }
        return false;
    }
}

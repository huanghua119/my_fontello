package com.mephone.fontello;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mephone.fontello.bean.FontSvg;
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

    /**
     * 单例
     */
    private FontelloService() {

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
        File file = new File(SystemConfig.FileSystem.CONFIG_FILE);
        if (!file.exists()) {
            MyLog.i("config.json文件不存在，请重新生成！");
            return false;
        }
        String cmd = "fontello-cli.cmd --config "
                + SystemConfig.FileSystem.CONFIG_FILE + " install";
        boolean complete = false;
        do {
            complete = false;
            MyLog.i("start complete:" + complete);
            MyLog.w("start complete:" + complete);
            String result = Cmd.run(cmd, true);
            if (!TextUtils.isEmpty(result)
                    && result.contains("Install complete")) {
                complete = true;
            }
            MyLog.i("end complete:" + complete);
            MyLog.w("end complete:" + complete);
        } while (!complete);
        return complete;
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
        JSONArray glyphsArray = new JSONArray();
        for (FontSvg svg : svgFileList) {
            JSONObject glyphsObject = new JSONObject();
            // FontSvg svg = svgFileList.get(0);
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

        TextUtils.saveFileText(JsonUtils.formatJson(configJson.toString()),
                SystemConfig.FileSystem.CONFIG_FILE);
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
        String cmd = "svgpath.cmd \"" + path + "\" " + translateX + " "
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
                    result.add(fs);
                }
            }
        }
        return result;
    }
}

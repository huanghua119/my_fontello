package com.mephone.fontello;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mephone.fontello.util.FileUtils;
import com.mephone.fontello.util.JsonUtils;
import com.mephone.fontello.util.TextUtils;

public class BihuaClassify {

    public static void main(String[] args) {
        String path = "C:/Users/huanghua/Desktop/chinese_word_info.json";
        String folder = "C:/Users/huanghua/Desktop/s1";
        String name = "ST";
        String fontName = "宋体";

        if (args != null && args.length > 0) {
            if (args.length != 4) {
                System.out.println("参数错误!" + args.length);
                enterExit();
            }
            for (String arg : args) {
                System.out.println("参数:" + arg);
            }
            path = args[0];
            folder = args[1];
            name = args[2];
            fontName = args[3];
        }
        BihuaClassify bc = new BihuaClassify();
        List<WordInfo> infoList = bc.parserJson(path);
        System.out.println("汉字数量:" + infoList.size());
        bc.moveFile(infoList, folder, name, fontName);
        enterExit();
    }

    private void moveFile(List<WordInfo> infoList, String folder, String name,
            String fontName) {
        String newFolder = fontName + "/";
        String end = ".jpg";
        Map<String, String> ends = getSourseFile(folder);
        for (WordInfo info : infoList) {
            String[] bihua = info.bihua.split("、");
            for (int i = 0; i < bihua.length; i++) {
                int index = i + 1;
                String bihuaName = bihua[i];
                if (bihuaName.contains("/")) {
                    bihuaName = bihuaName.replaceAll("/", "_");
                }
                String fileName = name + info.word + "_"
                        + ((index < 10) ? "0" : "") + index;

                if (ends.containsKey(fileName)) {
                    end = ends.get(fileName);
                }
                fileName += end;

                File pngFile = new File(folder, fileName);
                String dest = newFolder + bihuaName + "/" + info.word + "_"
                        + ((index < 10) ? "0" : "") + index + end;
                if (pngFile.exists()) {
                    File destFile = new File(newFolder + bihuaName);
                    if (!destFile.exists()) {
                        destFile.mkdirs();
                    }
                    FileUtils.copyFile(pngFile.getAbsolutePath(), dest);
                    System.out.println("move " + pngFile.getAbsolutePath()
                            + " to " + dest);
                } else {
                    System.out.println(pngFile.getAbsolutePath() + " 不存在!");
                }
            }
        }
    }

    private Map<String, String> getSourseFile(String folder) {
        File file = new File(folder);
        Map<String, String> map = new HashMap<String, String>();
        if (file.exists() && file.isDirectory()) {
            for (File image : file.listFiles()) {
                String name = image.getName();
                int index = name.lastIndexOf(".");
                String start = name.substring(0, index);
                String end = name.substring(index);
                map.put(start, end);
            }
        }
        return map;
    }

    private List<WordInfo> parserJson(String path) {
        String text = TextUtils.readFile(path);
        JSONArray jsonArray = JSONArray.parseArray(text);

        List<WordInfo> result = new ArrayList<WordInfo>();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONArray subArray = jsonArray.getJSONArray(i);
            WordInfo info = new WordInfo();
            for (int j = 0; j < subArray.size(); j++) {
                JSONObject jsonObject = subArray.getJSONObject(j);
                String label = JsonUtils.getJSONString(jsonObject, "label");
                String value = JsonUtils.getJSONString(jsonObject, "value");
                if (TextUtils.equals(label, "汉字")) {
                    info.word = value;
                } else if (TextUtils.equals(label, "名称")) {
                    info.bihua = value;
                }
            }
            result.add(info);
        }
        return result;
    }

    private static void enterExit() {
        Scanner in = new Scanner(System.in);
        System.out.println("按回车键退出！");
        String name = in.nextLine();
        System.exit(0);
    }

    class WordInfo {
        String word;
        String bihua;
    }
}

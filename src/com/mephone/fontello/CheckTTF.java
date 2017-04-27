package com.mephone.fontello;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mephone.fontello.config.SystemConfig;
import com.mephone.fontello.util.TextUtils;
import com.mephone.fontello.util.Ttf2png;

public class CheckTTF {

    private List<String> mTextList;

    private List<File> mTTFList;

    public static void main(String[] args) {
        CheckTTF checkTTF = new CheckTTF();
        checkTTF.getAllText();
        checkTTF.getAllTTF();
        checkTTF.startCheck();
    }

    private void getAllText() {
        if (mTextList == null) {
            mTextList = new ArrayList<String>();
        }
        File[] textFile = FontelloService.getInstance().traverseDir(
                SystemConfig.FileSystem.CHECK_TXT, ".txt");
        for (File file : textFile) {
            String txt = TextUtils.readFile(file.getAbsolutePath());
            txt = TextUtils.replaceBlank(txt);
            mTextList.add(txt);
        }
    }

    private void getAllTTF() {
        if (mTTFList == null) {
            mTTFList = new ArrayList<File>();
        }
        File[] textFile1 = FontelloService.getInstance().traverseDir(
                SystemConfig.FileSystem.CHECK_TTF, ".ttf");
        for (File file : textFile1) {
            mTTFList.add(file);
        }
        File[] textFile2 = FontelloService.getInstance().traverseDir(
                SystemConfig.FileSystem.CHECK_TTF, ".otf");
        for (File file : textFile2) {
            mTTFList.add(file);
        }
    }

    private void startCheck() {
        System.out.println("开始检测 !");
        for (File file : mTTFList) {
            Map<String, List<String>> outList = new HashMap<String, List<String>>();
            for (String txt : mTextList) {
                for (int i = 0; i < txt.length(); i++) {
                    String c = txt.charAt(i) + "";
                    boolean isExits = Ttf2png.checkChar(file, c);
                    if (!isExits) {
                        List<String> out = null;
                        if (outList.containsKey(file.getName())) {
                            out = outList.get(file.getName());
                        } else {
                            out = new ArrayList<>();
                        }
                        if (!out.contains(c)) {
                            out.add(c);
                            System.out.println(file.getAbsolutePath() + " 不存在:"
                                    + c);
                        }
                        outList.put(file.getName(), out);
                    }
                }
            }
            endCheck(outList);
            outList.clear();
        }
        System.out.println("检测结束!");
    }

    private void endCheck(Map<String, List<String>> outList) {
        if (outList == null || outList.size() == 0) {
            return;
        }
        for (Map.Entry<String, List<String>> entry : outList.entrySet()) {
            StringBuffer str = new StringBuffer();
            for (String txt : entry.getValue()) {
                str.append(txt);
            }
            TextUtils.saveFileText(
                    str.toString(),
                    SystemConfig.FileSystem.CHECK_PATH + "out/"
                            + entry.getKey() + ".txt");
        }
    }
}

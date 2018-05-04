package com.mephone.fontello;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.mephone.fontello.util.StringTools;
import com.mephone.fontello.util.TextUtils;

public class GlyphsPath {

    public static void main(String[] args) {
        float pieceX = 0.3f;
        float pieceY = 0.7f;
        String data = "paths = (\n" +
                "    {\n" +
                "        closed = 1;\n" +
                "        nodes = (\n" +
                "            \"36 -5 OFFCURVE\",\n" +
                "            \"63 -2 OFFCURVE\",\n" +
                "            \"89 1 CURVE\",\n" +
                "            \"89 5 OFFCURVE\",\n" +
                "            \"90 11 OFFCURVE\",\n" +
                "            \"91 18 CURVE\",\n" +
                "            \"59 13 OFFCURVE\",\n" +
                "            \"30 10 OFFCURVE\",\n" +
                "            \"5 7 CURVE\",\n" +
                "            \"9 -10 LINE\"\n" +
                "        );\n" +
                "    },\n" +
                "    {\n" +
                "        closed = 1;\n" +
                "        nodes = (\n" +
                "            \"27 40 OFFCURVE\",\n" +
                "            \"51 42 OFFCURVE\",\n" +
                "            \"88 45 CURVE\",\n" +
                "            \"88 50 OFFCURVE\",\n" +
                "            \"88 56 OFFCURVE\",\n" +
                "            \"89 61 CURVE\",\n" +
                "            \"70 59 OFFCURVE\",\n" +
                "            \"52 58 OFFCURVE\",\n" +
                "            \"35 57 CURVE\",\n" +
                "            \"58 88 OFFCURVE\",\n" +
                "            \"79 119 OFFCURVE\",\n" +
                "            \"97 151 CURVE\",\n" +
                "            \"80 158 LINE\",\n" +
                "            \"73 143 OFFCURVE\",\n" +
                "            \"65 130 OFFCURVE\",\n" +
                "            \"58 117 CURVE\",\n" +
                "            \"47 117 OFFCURVE\",\n" +
                "            \"37 117 OFFCURVE\",\n" +
                "            \"27 117 CURVE\",\n" +
                "            \"41 141 OFFCURVE\",\n" +
                "            \"54 165 OFFCURVE\",\n" +
                "            \"66 188 CURVE\",\n" +
                "            \"48 195 LINE\",\n" +
                "            \"36 163 OFFCURVE\",\n" +
                "            \"22 137 OFFCURVE\",\n" +
                "            \"7 116 CURVE\",\n" +
                "            \"11 100 LINE\",\n" +
                "            \"20 101 OFFCURVE\",\n" +
                "            \"33 102 OFFCURVE\",\n" +
                "            \"49 103 CURVE\",\n" +
                "            \"36 82 OFFCURVE\",\n" +
                "            \"24 65 OFFCURVE\",\n" +
                "            \"13 54 CURVE\",\n" +
                "            \"16 39 LINE\"\n" +
                "        );\n" +
                "    }\n" +
                ");\n" +
                "\n" +
                "\n" +
                "paths = (\n" +
                "    {\n" +
                "        closed = 1;\n" +
                "        nodes = (\n" +
                "            \"36 -5 OFFCURVE\",\n" +
                "            \"58.045 0 OFFCURVE\",\n" +
                "            \"69.489 2.191 CURVE\",\n" +
                "            \"69.489 6.191 OFFCURVE\",\n" +
                "            \"70.489 12.191 OFFCURVE\",\n" +
                "            \"71.489 19.191 CURVE\",\n" +
                "            \"51.895 15.426 OFFCURVE\",\n" +
                "            \"25.607 10 OFFCURVE\",\n" +
                "            \"5 7 CURVE\",\n" +
                "            \"9 -10 LINE\"\n" +
                "        );\n" +
                "    },\n" +
                "    {\n" +
                "        closed = 1;\n" +
                "        nodes = (\n" +
                "            \"25.607 42.335 OFFCURVE\",\n" +
                "            \"52.707 45.802 OFFCURVE\",\n" +
                "            \"71.613 47.335 CURVE\",\n" +
                "            \"71.613 52.335 OFFCURVE\",\n" +
                "            \"71.613 58.335 OFFCURVE\",\n" +
                "            \"72.613 63.335 CURVE\",\n" +
                "            \"63.286 62.353 OFFCURVE\",\n" +
                "            \"50.607 60.335 OFFCURVE\",\n" +
                "            \"33.607 59.335 CURVE\",\n" +
                "            \"53.619 88.055 OFFCURVE\",\n" +
                "            \"72.654 124.398 OFFCURVE\",\n" +
                "            \"78.279 134.398 CURVE\",\n" +
                "            \"63 141 LINE\",\n" +
                "            \"60.894 135.936 OFFCURVE\",\n" +
                "            \"56.544 127.734 OFFCURVE\",\n" +
                "            \"50.026 117.369 CURVE\",\n" +
                "            \"44.674 117.088 OFFCURVE\",\n" +
                "            \"34.032 117 OFFCURVE\",\n" +
                "            \"27 117 CURVE\",\n" +
                "            \"41 141 OFFCURVE\",\n" +
                "            \"51 161.852 OFFCURVE\",\n" +
                "            \"63 184.852 CURVE\",\n" +
                "            \"45 191.852 LINE\",\n" +
                "            \"33 159.852 OFFCURVE\",\n" +
                "            \"22 137 OFFCURVE\",\n" +
                "            \"7 116 CURVE\",\n" +
                "            \"11 100 LINE\",\n" +
                "            \"20 101 OFFCURVE\",\n" +
                "            \"35.633 103 OFFCURVE\",\n" +
                "            \"43.633 103 CURVE\",\n" +
                "            \"33 83.81 OFFCURVE\",\n" +
                "            \"22.607 67.335 OFFCURVE\",\n" +
                "            \"11.607 56.335 CURVE\",\n" +
                "            \"14.607 41.335 LINE\"\n" +
                "        );\n" +
                "    }\n" +
                ");";
        GlyphsPath gp = new GlyphsPath();
        gp.startCalculate(null, data, pieceX, pieceY);
    }

    public void startCalculate(Component frame,String data, float pieceX, float pieceY) {
        data = data.replaceAll("\n", "");
        //System.out.println("data:" + data);

        String[] paths = data.split("paths");
        if (paths.length != 3) {
            showAlert(frame,"paths数据应该为2组!");
            return;
        }
        String path1 = paths[1];
        String path2 = paths[2];

        List<NodeBean> nodeList1 = getNode(path1);
        for (NodeBean nb : nodeList1) {
            //System.out.println("nb1:" + nb);
        }

        List<NodeBean> nodeList2 = getNode(path2);
        for (NodeBean nb : nodeList2) {
            //System.out.println("nb2:" + nb);
        }

        if (nodeList1.size() != nodeList2.size()) {
            showAlert(frame,"两组数据不一置!");
            return;
        }
        int length = nodeList1.size();

        String result = "";
        String result2 = "";
        result = "====正向计算====\n";
        result2 = "====正向计算====\n";
        for (int i = 0; i < length; i++) {
            NodeBean nodeBean1 = nodeList1.get(i);
            NodeBean nodeBean2 = nodeList2.get(i);

            float x1 = StringTools.parseFloat(nodeBean1.x, 0);
            float x2 = StringTools.parseFloat(nodeBean2.x, 0);
            float cx = x1 + (x2 - x1) * pieceX;

            float y1 = StringTools.parseFloat(nodeBean1.y, 0);
            float y2 = StringTools.parseFloat(nodeBean2.y, 0);
            float cy = y1 + (y2 - y1) * pieceY;
            result += "path1:" + nodeBean1.toString() + " path2:"
                    + nodeBean2.toString() + " cx:" + cx + " cy:" + cy + "\n";
            result2 += cx + " " + cy + " " + nodeBean1.type + "\n";
        }
        
        result += "\n\n\n====反向计算====\n";
        result2 += "\n\n\n====反向计算====\n";
        for (int i = 0; i < length; i++) {
            NodeBean nodeBean1 = nodeList1.get(i);
            NodeBean nodeBean2 = nodeList2.get(i);

            float x1 = StringTools.parseFloat(nodeBean1.x, 0);
            float x2 = StringTools.parseFloat(nodeBean2.x, 0);
            float cx = x2 + (x1 - x2) * pieceX;

            float y1 = StringTools.parseFloat(nodeBean1.y, 0);
            float y2 = StringTools.parseFloat(nodeBean2.y, 0);
            float cy = y2 + (y1 - y2) * pieceY;
            result += "path1:" + nodeBean1.toString() + " path2:"
                    + nodeBean2.toString() + " cx:" + cx + " cy:" + cy + "\n";
            result2 += cx + " " + cy + " " + nodeBean1.type + "\n";
        }
        TextUtils.saveFileText(result, "./glyhpsPath_result.txt");
        TextUtils.saveFileText(result2, "./glyhpsPath_result2.txt");
        showAlert(frame,"完成！");
    }

    private List<String> splitPath(String data, String start, String end) {
        String tem = data;
        List<String> result = new ArrayList<>();
        while (tem.contains(start)) {
            tem = tem.substring(tem.indexOf(start) + 1);
            result.add(tem.substring(0, tem.indexOf(end)));
        }
        return result;
    }
    
    private List<NodeBean> getNode(String pathData) {
        List<String> pathList = splitPath(pathData, "{", "}");
        List<NodeBean> nodeList = new ArrayList<>();
        if (pathList != null && pathList.size() > 0) {
            for (String path : pathList) {
                List<String> node = splitPath(path, "(", ")");
                if (node != null && node.size() > 0) {
                    String content = node.get(0);
                    if (!TextUtils.isEmpty(content)) {
                        String[] xys = content.split(",");
                        for (String xy : xys) {
                            if (TextUtils.isEmpty(TextUtils.replaceBlank(xy))) {
                                continue;
                            }
                            //System.out.println("xy:" + xy);
                            String nodeData = xy.substring(
                                    xy.indexOf("\"") + 1, xy.lastIndexOf("\""));
                            String[] xys_read = nodeData.split(" ");
                            if (xys_read.length == 3) {
                                NodeBean nb = new NodeBean();
                                nb.x = xys_read[0];
                                nb.y = xys_read[1];
                                nb.type = xys_read[2];
                                nodeList.add(nb);
                            }
                        }
                    }
                }
            }
        }
        return nodeList;
    }
    
    private void showAlert(Component frame, String data) {
        if (frame != null) {
            JOptionPane.showMessageDialog(frame, data, "提示！",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            System.out.println(data);
        }
    }

    private class NodeBean {
        String x;
        String y;
        String type;

        @Override
        public String toString() {
            return x + " " + y + " " + type;
        }
    }
}

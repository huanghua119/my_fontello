package com.mephone.fontello.svg;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mephone.fontello.bean.CutSvg;
import com.mephone.fontello.bean.FontSvg;
import com.mephone.fontello.bean.FontelloSvg;
import com.mephone.fontello.config.MyLog;
import com.mephone.fontello.config.SystemConfig;
import com.mephone.fontello.util.CommonUtils;
import com.mephone.fontello.util.TextUtils;

public class SVGParser {

    private static SVGParser sSParser;

    private SVGParser() {

    }

    public static SVGParser getInstance() {
        if (sSParser == null) {
            synchronized (SVGParser.class) {
                if (sSParser == null) {
                    sSParser = new SVGParser();
                }
            }
        }
        return sSParser;
    }

    /**
     * 解析部件svg文件
     * 
     * @param path
     * @return
     */
    public FontSvg parserFontSvg(String path) {
        MyLog.i("parserJoinSvg start");
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        FontSvg svg = new FontSvg();
        String name = file.getName().replace(".svg", "");
        svg.setName(name);
        try {
            org.w3c.dom.Document doc = f
                    .createDocument(file.toURI().toString());

            // 解析svg标签
            NodeList svgList = doc.getElementsByTagName("svg");
            if (svgList.getLength() != 1) {
                return null;
            }
            // 解析path标签
            NodeList pathList = doc.getElementsByTagName("path");
            if (pathList == null || pathList.getLength() == 0) {
                return null;
            }

            // 获取svg标签下的width,height
            Node node = svgList.item(0);
            org.w3c.dom.Element els = (org.w3c.dom.Element) node;
            String viewBox = els.getAttribute("viewBox");

            svg.setViewBox(viewBox);

            // 解析g标签
            NodeList gList = doc.getElementsByTagName("g");
            String transform = "";
            if (gList != null && gList.getLength() > 0) {
                Node gNode = gList.item(0);
                org.w3c.dom.Element gEle = (org.w3c.dom.Element) gNode;
                transform = gEle.getAttribute("transform");
            }
            svg.setTransform(TextUtils.isEmpty(transform) ? "" : transform);

            // 获取path标签下的d属性，得到文字路径值
            StringBuffer dataD = new StringBuffer();
            for (int i = 0; i < pathList.getLength(); i++) {
                Node d = pathList.item(i);
                org.w3c.dom.Element svgPath = (org.w3c.dom.Element) d;
                String data = svgPath.getAttribute("d");
                // MyLog.i("parserJoinSvg data: " + data);
                dataD.append(data);
            }
            svg.setPathD(dataD.toString());
            MyLog.i("parserJoinSvg end");
        } catch (Exception e) {
            MyLog.w("解析svg失败...path:" + path);
            MyLog.w(e.toString());
            e.printStackTrace();
        }
        return svg;
    }

    /**
     * 解析fontello svg图片
     * 
     * @param path
     * @return
     */
    public FontelloSvg parserFontelloSvgText(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        FontelloSvg fsvg = new FontelloSvg();
        String svgText = TextUtils.getFileText(path, true);
        int indexStart = svgText.indexOf("<glyph");
        int indexEnd = svgText.lastIndexOf("/>") + "/>".length();
        svgText = svgText.substring(indexStart, indexEnd);
        fsvg.setGlyph(svgText);

        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        try {
            org.w3c.dom.Document doc = f
                    .createDocument(file.toURI().toString());
            NodeList fontFace = doc.getElementsByTagName("font-face");
            if (fontFace == null || fontFace.getLength() == 0) {
                return null;
            }
            Node node = fontFace.item(0);
            org.w3c.dom.Element els = (org.w3c.dom.Element) node;
            String fontFamily = els.getAttribute("font-family");
            String fontWeight = els.getAttribute("font-weight");
            String fontStretch = els.getAttribute("font-stretch");
            String unitsPerEm = els.getAttribute("units-per-em");
            String ascent = els.getAttribute("ascent");
            String descent = els.getAttribute("descent");
            fsvg.setFontFamily(fontFamily);
            fsvg.setFontWeight(fontWeight);
            fsvg.setFontStretch(fontStretch);
            fsvg.setUnitsPerEm(unitsPerEm);
            fsvg.setAscent(ascent);
            fsvg.setDescent(descent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fsvg;
    }

    /**
     * 合并fontell svg图片
     * 
     * @param newPath
     * @param fonts
     */
    public void mergeFontellSvgText(String newPath,
            List<FontelloSvg> fontelloSvgs) {
        if (fontelloSvgs == null || fontelloSvgs.size() == 0) {
            return;
        }

        String head = "<?xml version=\"1.0\" standalone=\"no\"?><!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\"><svg xmlns=\"http://www.w3.org/2000/svg\"><metadata>Copyright (C) 2017 by original authors @ fontello.com</metadata><defs><font id=\"fontello\" horiz-adv-x=\"1000\" >";
        String end = "</font></defs></svg>";

        FontelloSvg one = fontelloSvgs.get(0);
        String fontFace = "<font-face font-family=\"" + one.getFontFamily()
                + "\" font-weight=\"" + one.getFontWeight()
                + "\" font-stretch=\"" + one.getFontStretch()
                + "\" units-per-em=\"" + one.getUnitsPerEm() + "\" ascent=\""
                + one.getAscent() + "\" descent=\"" + one.getDescent()
                + "\" />";

        String svgText = "";
        System.out.println("size:" + fontelloSvgs.size());
        for (FontelloSvg svg : fontelloSvgs) {
            String text = svg.getGlyph();
            svgText += text;
        }
        svgText = head + fontFace + svgText + end;
        svgText = svgText.replace("<", "\n<").replaceFirst("\n<", "<");
        TextUtils.saveFileText(svgText, newPath);
    }

    /**
     * 生成字库svg文件
     * 
     * @param svg
     * @param newPath
     */
    public void generateSVGFont(FontelloSvg svg, String newPath) {

        String ascent = svg.getAscent();
        String units_per_em = svg.getUnitsPerEm();
        String descent = svg.getDescent();
        String familyname = svg.getFontFamily();
        String glyph = svg.getGlyph();
        String fontWeight = svg.getFontWeight();
        String fontStretch = svg.getFontStretch();

        String head = "<?xml version=\"1.0\" standalone=\"no\"?><!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\"><svg xmlns=\"http://www.w3.org/2000/svg\"><metadata>"
                + SystemConfig.DefalutConfig.copyRight + "</metadata><defs>";

        String font = "<font id=\"" + familyname + "\" horiz-adv-x=\""
                + units_per_em + "\" >";

        String fontFace = "<font-face font-family=\"" + familyname
                + "\" font-weight=\"" + fontWeight + "\" font-stretch=\""
                + fontStretch + "\" units-per-em=\"" + units_per_em
                + "\" ascent=\"" + ascent + "\" descent=\"" + descent + "\" />";

        String messGlyph = "<missing-glyph horiz-adv-x=\"" + units_per_em
                + "\" />";

        String end = "</font></defs></svg>";

        StringBuffer sb = new StringBuffer();
        sb.append(head).append(font).append(fontFace).append(messGlyph)
                .append(glyph).append(end);
        CommonUtils.printMemoryInfo();
        String svgText = sb.toString();
        svgText = svgText.replace("<", "\n<").replaceFirst("\n<", "<");
        TextUtils.saveFileText(svgText, newPath);
    }

    public void cutSvg(File svgFile, String names, int cols, int rows,
            int width, int height) {
        if (svgFile == null || !svgFile.exists()) {
            return;
        }

        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        CutSvg[][] cutSvgArray = new CutSvg[cols][rows];

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                cutSvgArray[i][j] = new CutSvg();
                cutSvgArray[i][j].setCols(i);
                cutSvgArray[i][j].setRows(j);
                cutSvgArray[i][j].setWidth(width);
                cutSvgArray[i][j].setHeight(height);
            }
        }

        int count = 0;
        if (SystemConfig.DefalutConfig.sNAME_SPITE) {
            String number = svgFile.getName().replace(".svg", "").split("-")[1];
            count = cols * rows * (Integer.parseInt(number) - 1);
        }
        try {
            MyLog.i("cutSvg start");
            org.w3c.dom.Document doc = f.createDocument(svgFile.toURI()
                    .toString());

            // 解析svg标签
            NodeList svgList = doc.getElementsByTagName("svg");
            if (svgList.getLength() != 1) {
                return;
            }
            // 解析style标签
            Map<String, String> fillClassMap = new HashMap<String, String>();
            NodeList styleList = doc.getElementsByTagName("style");
            if (styleList != null && styleList.getLength() > 0) {
                Node d = styleList.item(0);
                org.w3c.dom.Element svgStyle = (org.w3c.dom.Element) d;
                String style = svgStyle.getTextContent();
                if (!TextUtils.isEmpty(style)) {
                    String[] classJson = style.trim().split("}");
                    for (String json : classJson) {
                        String st = json.trim();
                        int index = st.indexOf("{");
                        String key = st.substring(1, index);
                        String content = st.substring(index + 1, st.length());

                        String[] a = content.split(";");
                        for (String b : a) {
                            if (b.contains("fill")) {
                                String fill = b.split(":")[1];
                                fillClassMap.put(key, fill);
                            }
                        }
                    }
                }
            }
            // 解析path标签
            NodeList pathList = doc.getElementsByTagName("path");
            if (pathList != null) {
                int lenght = pathList.getLength();
                if (lenght == 1) {
                    Node d = pathList.item(0);
                    org.w3c.dom.Element svgPath = (org.w3c.dom.Element) d;
                    String data = svgPath.getAttribute("d");
                    String[] allData = data.split("M");
                    for (String path : allData) {
                        if (!TextUtils.isEmpty(path)) {
                            path = "M" + path;
                            CutSvg svg = caclCutSvg(cutSvgArray, path, names,
                                    cols, rows, width, height, count);
                            if (svg != null) {
                                svg.setSinglePath(true);
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < pathList.getLength(); i++) {
                        Node d = pathList.item(i);
                        org.w3c.dom.Element svgPath = (org.w3c.dom.Element) d;
                        if (filterFill(fillClassMap, svgPath)) {
                            continue;
                        }
                        String data = svgPath.getAttribute("d");
                        CutSvg svg = caclCutSvg(cutSvgArray, data, names, cols,
                                rows, width, height, count);
                        if (svg != null) {
                            svg.setSinglePath(false);
                        }
                    }
                }
            }
            List<String> otherPath = new ArrayList<>();
            // 解析rect标签
            NodeList rectList = doc.getElementsByTagName("rect");
            if (rectList != null) {
                for (int i = 0; i < rectList.getLength(); i++) {
                    Node d = rectList.item(i);
                    org.w3c.dom.Element svgRect = (org.w3c.dom.Element) d;
                    if (filterFill(fillClassMap, svgRect)) {
                        continue;
                    }
                    String path = SVGChange.rect2path(svgRect);
                    if (!TextUtils.isEmpty(path)) {
                        otherPath.add(path);
                    }
                }
            }
            // 解析polygon标签
            NodeList polygonList = doc.getElementsByTagName("polygon");
            if (polygonList != null) {
                for (int i = 0; i < polygonList.getLength(); i++) {
                    Node d = polygonList.item(i);
                    org.w3c.dom.Element svgPolygon = (org.w3c.dom.Element) d;
                    if (filterFill(fillClassMap, svgPolygon)) {
                        continue;
                    }
                    String path = SVGChange.polygon2path(svgPolygon);
                    if (!TextUtils.isEmpty(path)) {
                        otherPath.add(path);
                    }
                }
            }
            // 解析polyline标签
            NodeList polylineList = doc.getElementsByTagName("polyline");
            if (polylineList != null) {
                for (int i = 0; i < polylineList.getLength(); i++) {
                    Node d = polylineList.item(i);
                    org.w3c.dom.Element svgPolyline = (org.w3c.dom.Element) d;
                    if (filterFill(fillClassMap, svgPolyline)) {
                        continue;
                    }
                    String path = SVGChange.polyline2path(svgPolyline);
                    if (!TextUtils.isEmpty(path)) {
                        otherPath.add(path);
                    }
                }
            }
            // 解析ellipse标签
            NodeList ellipseList = doc.getElementsByTagName("ellipse");
            if (ellipseList != null) {
                for (int i = 0; i < ellipseList.getLength(); i++) {
                    Node d = ellipseList.item(i);
                    org.w3c.dom.Element svgEllipse = (org.w3c.dom.Element) d;
                    if (filterFill(fillClassMap, svgEllipse)) {
                        continue;
                    }
                    String path = SVGChange.ellipse2path(svgEllipse);
                    if (!TextUtils.isEmpty(path)) {
                        otherPath.add(path);
                    }
                }
            }
            // 解析circle标签
            NodeList circleList = doc.getElementsByTagName("circle");
            if (circleList != null) {
                for (int i = 0; i < circleList.getLength(); i++) {
                    Node d = circleList.item(i);
                    org.w3c.dom.Element svgCircle = (org.w3c.dom.Element) d;
                    if (filterFill(fillClassMap, svgCircle)) {
                        continue;
                    }
                    String path = SVGChange.circle2path(svgCircle);
                    if (!TextUtils.isEmpty(path)) {
                        otherPath.add(path);
                    }
                }
            }
            for (String path : otherPath) {
                CutSvg svg = caclCutSvg(cutSvgArray, path, names, cols, rows,
                        width, height, count);
                if (svg != null) {
                    svg.setSinglePath(false);
                }
            }
            MyLog.i("cutSvg end");
        } catch (Exception e) {
            MyLog.w("解析svg失败 ...file:" + svgFile.getAbsolutePath());
            MyLog.w(e.toString());
            e.printStackTrace();
        }
        File outDir = new File(svgFile.getAbsolutePath().replace(".svg", ""));
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                CutSvg svg = cutSvgArray[i][j];
                if (svg.getPathList() == null || svg.getPathList().size() == 0) {
                    int nameIndex = j * rows + i + count;
                    if (nameIndex < names.length()) {
                        String name = names.substring(nameIndex, nameIndex + 1);
                        MyLog.w("缺少  " + name + " 字的svg数据");
                    }
                }
                generateCutSvg(svg, outDir.getAbsolutePath());
            }
        }
    }

    private CutSvg caclCutSvg(CutSvg[][] cutSvgArray, String data,
            String names, int cols, int rows, int width, int height, int count) {
        // 计算path起始坐标位置
        int index = CommonUtils.calcMin(data.indexOf("h"), data.indexOf("H"),
                data.indexOf("v"), data.indexOf("V"), data.indexOf("l"),
                data.indexOf("L"), data.indexOf("c"), data.indexOf("C"),
                data.indexOf("a"), data.indexOf("A"));
        String point = data.substring(1, index);
        String[] points = point.replace(",", " ").split(" ");
        int c = (int) Double.parseDouble(points[0]) / width;
        int r = (int) Double.parseDouble(points[1]) / height;
        if (c >= cutSvgArray.length) {
            return null;
        }
        if (r >= cutSvgArray[c].length) {
            return null;
        }
        CutSvg svg = cutSvgArray[c][r];
        List<String> dataList = svg.getPathList();
        if (dataList == null) {
            dataList = new ArrayList<String>();
            svg.setPathList(dataList);
        }
        dataList.add(data);
        if (!TextUtils.isEmpty(names)) {
            // 计算path对应的汉字
            int nameIndex = r * cols + c + count;
            if (nameIndex <= names.length()) {
                String name = names.substring(nameIndex, nameIndex + 1);
                svg.setName(name);
                svg.setUnicode(TextUtils.string2Unicode(name));
            }
        } else {
            String name = "map_" + r + "_" + c;
            svg.setName(name);
            svg.setUnicode(name);
        }
        return svg;
    }

    /**
     * 生成切割的svg图片
     * 
     * @param svg
     * @param newPath
     */
    public void generateCutSvg(CutSvg svg, String newPath) {
        if (svg.getPathList() == null || svg.getPathList().size() == 0) {
            return;
        }
        String head = "<?xml version=\"1.0\" standalone=\"no\"?><!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20010904//EN\" \"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">";
        String svgTag = "<svg version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" width=\""
                + svg.getWidth()
                + "px\" height=\""
                + svg.getHeight()
                + "px\" viewBox=\"0 0 "
                + svg.getWidth()
                + " "
                + svg.getHeight() + "\" preserveAspectRatio=\"xMidYMid meet\">";
        int transX = svg.getCols() * svg.getWidth();
        int transY = svg.getRows() * svg.getHeight();
        String gTag = "<g transform=\"translate(-" + transX + ",-" + transY
                + ")\" >";
        String pathTag = "";
        if (svg.isSinglePath()) {
            pathTag = "<path d=\"";
            for (String path : svg.getPathList()) {
                pathTag += path;
            }
            pathTag = pathTag + "\"/>";
        } else {
            for (String path : svg.getPathList()) {
                pathTag += "<path d=\"" + path + "\"/>";
            }
        }
        String end = "</g></svg>";

        String svgText = head + svgTag + gTag + pathTag + end;
        svgText = svgText.replace("<", "\n<").replaceFirst("\n<", "<");

        String name = svg.getName();
        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(svg.getUnicode())) {
            return;
        }
        if (!TextUtils.isHaizi(name)) {
            name = svg.getUnicode();
        }

        String outPath = newPath + "/" + name + ".svg";
        MyLog.i("generateCutSvg outPath:" + outPath);
        TextUtils.saveFileText(svgText, outPath);
    }

    /**
     * 过滤svg数据的class属性的风格（当fill为none值时为无效数据）
     * 
     * @param fillClassMap
     * @param element
     * @return
     */
    private boolean filterFill(Map<String, String> fillClassMap,
            org.w3c.dom.Element element) {
        if (element.hasAttribute("class")) {
            String key = element.getAttribute("class");
            if (fillClassMap.containsKey(key)) {
                String fill = fillClassMap.get(key);
                if ("none".equals(fill)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 解析ttf2svg生成的svg文件
     * 
     * @param path
     * @return
     */
    public List<FontSvg> parserBigSvg(String path) {
        //MyLog.i("parserBigSvg start");
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        List<FontSvg> result = new ArrayList<>();
        try {
            org.w3c.dom.Document doc = f
                    .createDocument(file.toURI().toString());

            // 解析svg标签
            NodeList svgList = doc.getElementsByTagName("svg");
            if (svgList.getLength() != 1) {
                return null;
            }
            // 解析glyph标签
            NodeList pathList = doc.getElementsByTagName("glyph");
            if (pathList == null || pathList.getLength() == 0) {
                return null;
            }

            NodeList faceList = doc.getElementsByTagName("font-face");
            // 获取font-face标签下的bbox
            Node node = faceList.item(0);
            org.w3c.dom.Element els = (org.w3c.dom.Element) node;
            String bboxText = els.getAttribute("bbox");

            // 解析bbox，生成viewBox格式数据和transform数据
            String[] bbox = bboxText.split(" ");
            if (bbox == null || bbox.length != 4) {
                MyLog.i("bbox error:" + bboxText);
                return null;
            }

            int x = Integer.parseInt(bbox[2]) - Integer.parseInt(bbox[0]);
            int y = Integer.parseInt(bbox[3]) - Integer.parseInt(bbox[1]);
            String viewBox = "0 0 " + x + " " + y;
            String transform = "translate(0.0," + bbox[3] + ") scale(1,-1)";

            // 遍历glyph标签，
            for (int i = 0; i < pathList.getLength(); i++) {
                Node d = pathList.item(i);
                org.w3c.dom.Element svgPath = (org.w3c.dom.Element) d;
                String data = svgPath.getAttribute("d");
                String name = svgPath.getAttribute("glyph-name");
                //MyLog.i("parserJoinSvg name:" + name + " data: " + data);
                FontSvg svg = new FontSvg();
                svg.setPathD(data);
                svg.setTransform(transform);
                svg.setViewBox(viewBox);
                svg.setName(name);
                if (!TextUtils.isEmpty(name.trim()) && !CommonUtils.isContains(result, svg)) {
                    result.add(svg);
                }
            }

            //MyLog.i("parserBigSvg end");
        } catch (Exception e) {
            MyLog.w("解析svg失败...path:" + path);
            MyLog.w(e.toString());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将ttf2svg生成的大svg文件数据生成为可查看的小svg文件
     * 
     * @param svg
     * @param newPath
     */
    public void generateFontSvg(FontSvg svg, String newPath) {
        if (svg == null) {
            return;
        }
        String head = "<?xml version=\"1.0\" standalone=\"no\"?><!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20010904//EN\" \"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">";
        String svgTag = "<svg version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" width=\"256px\" height=\"256px\" viewBox=\""
                + svg.getViewBox()
                + "\" preserveAspectRatio=\"xMidYMid meet\">";
        String transform = "transform=\"" + svg.getTransform() + "\"";
        String pathTag = "<path " + transform + " d=\"" + svg.getPathD()
                + "\"/>";
        String end = "</svg>";

        String svgText = head + svgTag + pathTag + end;
        svgText = svgText.replace("<", "\n<").replaceFirst("\n<", "<");

        String name = svg.getName();
        if (TextUtils.isEmpty(name)) {
            return;
        }

        String outPath = newPath + "/" + name + ".svg";
        MyLog.i("generateFontSvg outPath:" + outPath);
        TextUtils.saveFileText(svgText, outPath);
    }

}

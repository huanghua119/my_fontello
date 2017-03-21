package com.mephone.fontello.svg;

import java.io.File;
import java.util.List;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mephone.fontello.bean.FontSvg;
import com.mephone.fontello.bean.FontelloSvg;
import com.mephone.fontello.config.MyLog;
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
            MyLog.w("解析svg失败...");
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

    public void generateSVGFont(FontelloSvg svg, String newPath) {

        String ascent = svg.getAscent();
        String units_per_em = svg.getUnitsPerEm();
        String descent = svg.getDescent();
        String familyname = svg.getFontFamily();
        String glyph = svg.getGlyph();
        String fontWeight = svg.getFontWeight();
        String fontStretch = svg.getFontStretch();

        String head = "<?xml version=\"1.0\" standalone=\"no\"?><!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\"><svg xmlns=\"http://www.w3.org/2000/svg\"><metadata>huanghua@iekie.com</metadata><defs>";

        String font = "<font id=\"" + familyname + "\" horiz-adv-x=\""
                + units_per_em + "\" >";

        String fontFace = "<font-face font-family=\"" + familyname
                + "\" font-weight=\"" + fontWeight + "\" font-stretch=\""
                + fontStretch + "\" units-per-em=\"" + units_per_em
                + "\" ascent=\"" + ascent + "\" descent=\"" + descent + "\" />";

        String messGlyph = "<missing-glyph horiz-adv-x=\"" + units_per_em
                + "\" />";

        String end = "</font></defs></svg>";

        String svgText = head + font + fontFace + messGlyph + glyph + end;

        svgText = svgText.replace("<", "\n<").replaceFirst("\n<", "<");
        TextUtils.saveFileText(svgText, newPath);
    }
}

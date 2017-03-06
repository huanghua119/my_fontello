package com.mephone.fontello.svg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mephone.fontello.bean.FontSvg;
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
        svg.setUnicode(TextUtils.string2Unicode(name));
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
                //MyLog.i("parserJoinSvg data: " + data);
                dataD.append(data);
            }
            svg.setPathD(dataD.toString());
            MyLog.i("parserJoinSvg end");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return svg;
    }


}

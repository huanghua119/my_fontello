package com.mephone.fontello.svg;

import org.w3c.dom.Element;

import com.mephone.fontello.util.TextUtils;

/**
 * svg基本形状转换工具类
 * @author huanghua
 *
 */
public class SVGChange {

    private static boolean elementHasValue(Element e, String key) {
        if (e.hasAttribute(key)) {
            String value = e.getAttribute(key);
            return !TextUtils.isEmpty(value);
        }
        return false;
    }

    /**
     * 解析并转换rect数据为path
     * @param svgRect
     * @return
     */
    public static String rect2path(Element svgRect) {
        float x = 0;
        float y = 0;
        float rwidth = 0;
        float rheight = 0;
        float rx = -1000;
        float ry = -1000;
        if (elementHasValue(svgRect, "x")) {
            x = Float.parseFloat(svgRect.getAttribute("x"));
        }
        if (elementHasValue(svgRect, "y")) {
            y = Float.parseFloat(svgRect.getAttribute("y"));
        }
        if (elementHasValue(svgRect, "width")) {
            rwidth = Float.parseFloat(svgRect.getAttribute("width"));
        }
        if (elementHasValue(svgRect, "height")) {
            rheight = Float.parseFloat(svgRect.getAttribute("height"));
        }
        if (elementHasValue(svgRect, "rx")) {
            rx = Float.parseFloat(svgRect.getAttribute("rx"));
        }
        if (elementHasValue(svgRect, "ry")) {
            ry = Float.parseFloat(svgRect.getAttribute("ry"));
        }
        return rect2path(x, y, rwidth, rheight, rx, ry);
    }

    /**
     * rect to path
     * 矩形转换为path
     * @param x
     * @param y
     * @param width
     * @param height
     * @param rx
     * @param ry
     * @return
     */
    public static String rect2path(float x, float y, float width, float height,float rx, float ry) {
        /*
        * rx 和 ry 的规则是：
        * 1. 如果其中一个设置为 0 则圆角不生效
        * 2. 如果有一个没有设置则取值为另一个(-1000代表没有设置)
        */
        if (rx == 0 || ry == 0) {
            rx = 0;
            ry = 0;
        }
        if (rx == -1000 && ry != -1000) {
            rx = ry;
        } 
        if (ry == -1000 && rx != -1000) {
            ry = rx;
        }
        if (rx == -1000 && ry == -1000) {
            rx = 0;
            ry = 0;
        }

       //非数值单位计算，如当宽度像100%则移除
       //if (isNaN(x - y + width - height + rx - ry)) return "";

       rx = rx > width / 2 ? width / 2 : rx;
       ry = ry > height / 2 ? height / 2 : ry;
       //如果其中一个设置为 0 则圆角不生效
       String path = "";
       if(0 == rx || 0 == ry){
             path =
                 "M" + x + " " + y +
                 "h" + width +
                 "v" + height +
                 "h" + -width +
                 "z";
       }else{
             path =
                 "M" + x + " " + (y+ry) +
                 "a" + rx + " " + ry + " 0 0 1 " + rx + " " + (-ry) +
                 "h" + (width - rx - rx) +
                 "a" + rx + " " + ry + " 0 0 1 " + rx + " " + ry +
                 "v" + (height - ry -ry) +
                 "a" + rx + " " + ry + " 0 0 1 " + (-rx) + " " + ry +
                 "h" + (rx + rx -width) +
                 "a" + rx + " " + ry + " 0 0 1 " + (-rx) + " " + (-ry) +
                 "z";
       }
       return path;
    }

    /**
     * 解析并转换circle数据为path
     * @param svgEllipse
     * @return
     */
    public static String circle2path(Element svgCircle) {
        float cx = 0;
        float cy = 0;
        float r = 0;
        if (elementHasValue(svgCircle, "cx")) {
            cx = Float.parseFloat(svgCircle.getAttribute("cx"));
        }
        if (elementHasValue(svgCircle, "cy")) {
            cy = Float.parseFloat(svgCircle.getAttribute("cy"));
        }
        if (elementHasValue(svgCircle, "r")) {
            r = Float.parseFloat(svgCircle.getAttribute("r"));
        }
        return ellipse2path(cx, cy, r, r);
    }

    /**
     * 解析并转换ellipse数据为path
     * @param svgEllipse
     * @return
     */
    public static String ellipse2path(Element svgEllipse) {
        float cx = 0;
        float cy = 0;
        float rx = 0;
        float ry = 0;
        if (elementHasValue(svgEllipse, "cx")) {
            cx = Float.parseFloat(svgEllipse.getAttribute("cx"));
        }
        if (elementHasValue(svgEllipse, "cy")) {
            cy = Float.parseFloat(svgEllipse.getAttribute("cy"));
        }
        if (elementHasValue(svgEllipse, "rx")) {
            rx = Float.parseFloat(svgEllipse.getAttribute("rx"));
        }
        if (elementHasValue(svgEllipse, "ry")) {
            ry = Float.parseFloat(svgEllipse.getAttribute("ry"));
        }
        return ellipse2path(cx, cy, rx, ry);
    }

    /**
     * circle/ellipse to path
     * 圆和椭圆转换为path
     * @param cx
     * @param cy
     * @param rx
     * @param ry
     * @return
     */
    public static String ellipse2path(float cx, float cy, float rx, float ry) {
        //非数值单位计算，如当宽度像100%则移除
        //if (isNaN(x - y + width - height + rx - ry)) return;

        String path =
            "M" + (cx-rx) + " " + cy +
            "a" + rx + " " + ry + " 0 1 0 " + 2*rx + " 0" +
            "a" + rx + " " + ry + " 0 1 0 " + (-2*rx) + " 0" +
            "z";
        return path;
    }

    /**
     * line to path
     * 直线转换为path
     * @return
     */
    public static String line2path(float x1, float y1, float x2, float y2) {
        String path = "M" + x1 + " "+ y1 + "L" + x2 + " " + y2;
        return path;
    }

    /**
     * 解析并转换polyline数据为path
     * @param svgPloygon
     * @return
     */
    public static String polyline2path(Element svgPloygon) {
        if (svgPloygon.hasAttribute("points")) {
            String points = svgPloygon.getAttribute("points");
            return polyline2path(points);
        }
        return "";
    }

    /**
     * polygon折线转换path
     * @return
     */
    public static String polyline2path(String points) {
        points = points.replace(",", " ").trim();
        int index = points.indexOf(" ", points.indexOf(" ") + 1);
        String path = "M" + points.substring(0, index) + "L"
                + points.substring(index + 1, points.length());
        return path;
    }

    /**
     * 解析并转换polygon数据为path
     * @param svgPloygon
     * @return
     */
    public static String polygon2path(Element svgPloygon) {
        if (svgPloygon.hasAttribute("points")) {
            String points = svgPloygon.getAttribute("points");
            return polygon2path(points);
        }
        return "";
    }

    /**
     * polygon多边形转换path
     * @return
     */
    public static String polygon2path(String points) {
        points = points.replace(",", " ").trim();
        int index = points.indexOf(" ", points.indexOf(" ") + 1);
        String path = "M" + points.substring(0, index) + "L"
                + points.substring(index + 1, points.length()) + "z";
        return path;
    }

    public static void main(String[] args) {
        //<rect x="12.623" y="33.078" width="31" height="3"/>
        //<rect x="577.671" y="1359.868" width="7.46" height="2.9"/>
        float x = Float.parseFloat("577.671");
        float y = Float.parseFloat("1359.868");
        float width = Float.parseFloat("7.46");
        float height = Float.parseFloat("2.9");
        float rx = -1000;
        float ry = -1000;
        String path = rect2path(x, y, width, height, rx, ry);
        //String polygon = "362.467,50.485 365.467,50.485 365.467,22.568 379.623,22.568 379.623,19.568 348.717,19.568 348.717,22.568 362.467,22.568      ";
        //String path = polygon2path(polygon);
        //<circle cx="100" cy="100" r="50"></circle>
//        float cx = 100;
//        float cy = 100;
//        float r = 50;
//        String path = ellipse2path(cx, cy, r, r);
        System.out.println("path:" + path);
    }
}

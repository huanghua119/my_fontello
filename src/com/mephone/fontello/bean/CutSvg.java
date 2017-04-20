package com.mephone.fontello.bean;

import java.util.List;

public class CutSvg {

    private List<String> pathList;

    private String name;

    private String unicode;

    private int cols;

    private int rows;

    private int width;

    private int height;

    private boolean isSinglePath;

    public List<String> getPathList() {
        return pathList;
    }

    public void setPathList(List<String> pathList) {
        this.pathList = pathList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isSinglePath() {
        return isSinglePath;
    }

    public void setSinglePath(boolean isSinglePath) {
        this.isSinglePath = isSinglePath;
    }

    public String getUnicode() {
        return unicode;
    }

    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }
}

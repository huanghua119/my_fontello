package com.mephone.fontello.bean;

import java.io.File;

public class FontSettingProp {

    private String name;

    private File propFile;

    public FontSettingProp() {

    }

    public FontSettingProp(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getPropFile() {
        return propFile;
    }

    public void setPropFile(File propFile) {
        this.propFile = propFile;
    }

    @Override
    public String toString() {
        return name;
    }

}

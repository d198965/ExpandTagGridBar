package com.zdh.widget.model;

/**
 * Created by zdh on 16/3/25.
 */
public class TagModel {
    private String name;
    private boolean selected;
    private boolean enable = true;
    private Object tag;

    public TagModel(String name, boolean selected, boolean enable, Object tag) {
        this.name = name;
        this.selected = selected;
        this.enable = enable;
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}

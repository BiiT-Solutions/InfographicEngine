package com.biit.infographic.core.files;

import java.util.HashMap;
import java.util.Map;

public class Parameter implements IParameter {
    private String name;
    private Map<String, String> attributes;
    private String type;

    public Parameter() {
        attributes = new HashMap<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name + "(" + getType() + ":" + attributes + ")";
    }
}

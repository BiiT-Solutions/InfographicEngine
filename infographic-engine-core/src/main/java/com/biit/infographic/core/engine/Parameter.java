package com.biit.infographic.core.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parameter {
    private static final int PARAM_FIELDS = 3;
    private static final int PARAM_TYPE_FIELD_INDEX = 0;
    private static final int PARAM_NAME_FIELD_INDEX = 1;
    private static final int PARAM_ATTRIBUTE_FIELD_INDEX = 2;

    private static final String VARIABLE_SEARCH = "[#][A-Z].*?[#]";


    private String name;
    private Map<String, String> attributes;
    private String type;

    public Parameter() {
        attributes = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

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

    public static Set<Parameter> getParams(InfographicTemplate template) {
        final Set<Parameter> parameters = new HashSet<>();
        if (template.getTemplate() != null) {
            // parameters on template are written like: #NAME%TYPE%ATTR#
            final Pattern pattern = Pattern.compile(VARIABLE_SEARCH);
            final Matcher matcher = pattern.matcher(template.getTemplate());
            final List<String> templateParamsList = new ArrayList<>();
            while (matcher.find()) {
                String string = matcher.group();
                string = string.substring(1, string.length() - 1);
                templateParamsList.add(string);
            }

            for (String templateParam : templateParamsList) {
                // name,attributes,type
                final String[] splitParam = templateParam.split("%");
                if (splitParam.length >= PARAM_FIELDS) {
                    boolean paramPresent = false;
                    for (Parameter param : parameters) {
                        //Update existing parameter
                        if (Objects.equals(param.getType(), splitParam[PARAM_TYPE_FIELD_INDEX])
                                && Objects.equals(param.getName(), splitParam[PARAM_NAME_FIELD_INDEX])) {
                            param.getAttributes().put(splitParam[PARAM_ATTRIBUTE_FIELD_INDEX], "");
                            paramPresent = true;
                            break;
                        }
                    }

                    //Or add new parameter
                    if (!paramPresent) {
                        final Parameter param = new Parameter();
                        param.setType(splitParam[PARAM_TYPE_FIELD_INDEX]);
                        param.setName(splitParam[PARAM_NAME_FIELD_INDEX]);
                        param.getAttributes().put(splitParam[PARAM_ATTRIBUTE_FIELD_INDEX], "");
                        parameters.add(param);
                    }
                }
            }
        }
        return parameters;
    }

    @Override
    public String toString() {
        return name + "(" + getType() + ":" + attributes + ")";
    }
}

package com.biit.infographic.core.files;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class InfographicTemplateUtils {
    private static final int PARAM_FIELDS = 3;

    private InfographicTemplateUtils() {

    }

    public static Set<IParameter> getParams(InfographicTemplate template) {
        final Set<IParameter> parameters = new HashSet<>();
        if (template.getTemplate() != null) {
            // parameters on template are written like: #NAME%TYPE%ATTR#
            final Pattern pattern = Pattern.compile("#.*?#");
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
                    for (IParameter param : parameters) {
                        if (param.getName().equals(splitParam[1]) && Objects.equals(param.getType(), splitParam[0])) {
                            param.getAttributes().put(splitParam[2], "");
                            paramPresent = true;
                            break;
                        }
                    }

                    if (!paramPresent) {
                        final Parameter param = new Parameter();
                        param.setType(splitParam[0]);
                        param.setName(splitParam[1]);
                        param.getAttributes().put(splitParam[2], "");
                        parameters.add(param);
                    }
                }
            }
        }
        return parameters;
    }

    public static List<TreeNode<String>> getSelectableElementsTree(String rootPath) {
        final List<InfographicTree> infographicTreeNodes = InfographicTree.getInfographicNodes(rootPath);
        final List<TreeNode<String>> items = new ArrayList<>();
        for (InfographicTree node : infographicTreeNodes) {
            final TreeNode<String> item = node.getSelectableElementsTree();
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }

    public static List<InfographicTemplate> getTemplatesFromPath(String rootPath, List<TreeNode<String>> selections) {
        final List<InfographicTree> infographicTreeNodes = InfographicTree.getInfographicNodes(rootPath);
        final List<InfographicTemplate> templates = new ArrayList<>();
        for (InfographicTree node : infographicTreeNodes) {
            node.selectElements(selections);
            templates.addAll(node.getSelectedTemplates(rootPath));
        }
        return templates;
    }

}

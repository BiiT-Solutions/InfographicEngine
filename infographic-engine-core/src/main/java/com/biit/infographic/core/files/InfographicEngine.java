package com.biit.infographic.core.files;


import com.biit.infographic.core.exceptions.ElementDoesNotExistsException;
import com.biit.infographic.core.exceptions.InvalidParameterException;
import com.biit.infographic.core.exceptions.ReportNotReadyException;
import com.biit.infographic.logger.InfographicEngineLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InfographicEngine implements IInfographicEngine {

    public static final String INFOGRAPHIC_PATH = "infographics/";

    @Override
    public List<Infographic> getInfographicReport(List<TreeNode<String>> selections) {
        return getInfographicReport(selections, "");
    }

    @Override
    public List<Infographic> getInfographicReport(List<TreeNode<String>> selections, String rootPath) {
        final List<Infographic> infographics = new ArrayList<>();
        selections = sortForestWithReference(selections, getSelectableElements(rootPath));
        InfographicEngineLogger.debug(this.getClass().getName(), "Enabled elements '" + selections + "'.");
        final List<InfographicTemplate> templates = getTemplates(selections, rootPath);
        final Map<InfographicIndexFile, Set<IParameter>> params = getParamsFromTemplates(templates);
        try {
            final Map<InfographicIndexFile, Set<IParameter>> filledParams = getValues(params);
            for (InfographicTemplate template : templates) {
                if (template.getTemplate() != null) {
                    final Set<IParameter> templateParams = filledParams.get(template.getIndexFile());
                    if (templateParams != null) {
                        final InfographicTemplateAndContent infographicTemplateAndContent = addContentToTemplate(rootPath, template, templateParams);
                        infographics.add(infographicTemplateAndContent);
                    }
                }
            }
        } catch (InvalidParameterException | ElementDoesNotExistsException e) {
            InfographicEngineLogger.errorMessage(this.getClass().getName(), e);
        } catch (ReportNotReadyException e) {
            InfographicEngineLogger.warning(this.getClass().getName(), e.getMessage());
        }
        return infographics;
    }

    private static InfographicTemplateAndContent addContentToTemplate(String rootPath, InfographicTemplate template, Set<IParameter> templateParams) {
        final InfographicTemplateAndContent infographicTemplateAndContent = new InfographicTemplateAndContent(rootPath);
        infographicTemplateAndContent.setTemplate(template.getTemplate());
        final Map<String, String> variables = new HashMap<>();
        for (IParameter param : templateParams) {
            final Map<String, String> attributes = param.getAttributes();
            for (String key : attributes.keySet()) {
                variables.put("#" + param.getType() + "%" + param.getName() + "%" + key + "#", attributes.get(key));
            }
        }
        infographicTemplateAndContent.setContent(variables);
        return infographicTemplateAndContent;
    }

    @Override
    public List<TreeNode<String>> getSelectableElements() {
        return getSelectableElements("");
    }

    @Override
    public List<TreeNode<String>> getSelectableElements(String rootPath) {
        return InfographicTemplateUtils.getSelectableElementsTree(getReportPath() + rootPath);
    }

    @Override
    public Map<InfographicIndexFile, Set<IParameter>> getValues(Map<InfographicIndexFile, Set<IParameter>> parameters)
            throws java.security.InvalidParameterException, ElementDoesNotExistsException, ReportNotReadyException {
        final Map<InfographicIndexFile, Set<IParameter>> filledParams = new HashMap<>();
        // Group parameters by type.
        for (InfographicIndexFile infographicDefinition : parameters.keySet()) {
            final Map<ParameterType, Set<IParameter>> parametersByType = groupParametersByType(filledParams.get(infographicDefinition));

//            // Obtain parameters values.
//            setDroolsVariablesValues(examinationResult, appointment, parametersByType.get(ParameterType.DROOLS));
//
//            // Obtain goals.
//            setGoalsVariablesValues(examinationResult, appointment, parametersByType.get(ParameterType.GOAL));
//
//            // Obtain defined variables.
//            setTextsVariablesValues(examinationResult, appointment, parametersByType.get(ParameterType.TEXT));
//
//            // Obtain custom defined variables.
//            setCustomTextsVariablesValues(appointment, parametersByType.get(ParameterType.CUSTOM_TEXT),
//                    infographicDefinition.getExaminationFormName());
        }
        InfographicEngineLogger.debug(getClass().toString(), "Filled params: '" + filledParams + "'.");
        return filledParams;
    }

    private Map<ParameterType, Set<IParameter>> groupParametersByType(Set<IParameter> parameters) throws InvalidParameterException {
        final Map<ParameterType, Set<IParameter>> parametersByType = new HashMap<>();
        for (IParameter parameter : parameters) {
            if (parameter.getType().equalsIgnoreCase(ParameterType.DROOLS.name())) {
                parametersByType.computeIfAbsent(ParameterType.DROOLS, k -> new HashSet<>());
                parametersByType.get(ParameterType.DROOLS).add(parameter);
            } else if (parameter.getType().equalsIgnoreCase(ParameterType.GOAL.name())) {
                parametersByType.computeIfAbsent(ParameterType.GOAL, k -> new HashSet<>());
                parametersByType.get(ParameterType.GOAL).add(parameter);
            } else if (parameter.getType().equalsIgnoreCase(ParameterType.TEXT.name())) {
                parametersByType.computeIfAbsent(ParameterType.TEXT, k -> new HashSet<>());
                parametersByType.get(ParameterType.TEXT).add(parameter);
            } else if (parameter.getType().equalsIgnoreCase(ParameterType.CUSTOM_TEXT.name())) {
                parametersByType.computeIfAbsent(ParameterType.CUSTOM_TEXT, k -> new HashSet<>());
                parametersByType.get(ParameterType.CUSTOM_TEXT).add(parameter);
            } else {
                throw new InvalidParameterException(this.getClass(),
                        "Parameter '" + parameter + "' has type '" + parameter.getType() + "' and it is not implemented!");
            }
        }
        return parametersByType;
    }

    @Override
    public String getReportPath() {
        return INFOGRAPHIC_PATH;
    }

    protected List<InfographicTemplate> getTemplates(List<TreeNode<String>> selections, String rootPath) {
        final String reportPath = getReportPath() + rootPath;
        return InfographicTemplateUtils.getTemplatesFromPath(reportPath, selections);
    }

    @Override
    public Map<InfographicIndexFile, Set<IParameter>> getParamsFromAllTemplates(String rootPath) {
        final List<InfographicTemplate> templates = getTemplates(null, rootPath);
        return getParamsFromTemplates(templates);
    }

    private Map<InfographicIndexFile, Set<IParameter>> getParamsFromTemplates(List<InfographicTemplate> templates) {
        final Map<InfographicIndexFile, Set<IParameter>> params = new HashMap<>();
        for (InfographicTemplate template : templates) {
            final InfographicIndexFile templateDef = template.getIndexFile();
            final Set<IParameter> templateParams = InfographicTemplateUtils.getParams(template);
            params.put(templateDef, templateParams);
        }
        return params;
    }

    /*
     * Sorts a List<TreeNode<>> to fit the structure of another Tree
     */
    private List<TreeNode<String>> sortForestWithReference(List<TreeNode<String>> forest, List<TreeNode<String>> referenceForest) {
        final List<TreeNode<String>> sortedForest = new ArrayList<>();
        if (forest != null && referenceForest != null) {
            for (TreeNode<String> referenceTree : referenceForest) {
                for (TreeNode<String> tree : forest) {
                    if (tree != null && referenceTree.getData().equals(tree.getData())) {
                        tree.setChildren(sortForestWithReference(tree.getChildren(), referenceTree.getChildren()));
                        sortedForest.add(tree);
                    }
                }
            }
        }
        return sortedForest;
    }
}

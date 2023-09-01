package com.biit.infographic.core.engine;


import com.biit.infographic.core.engine.files.InfographicFolder;
import com.biit.infographic.core.engine.files.InfographicIndexFile;
import com.biit.infographic.core.engine.files.TreeNode;
import com.biit.infographic.core.exceptions.ElementDoesNotExistsException;
import com.biit.infographic.core.exceptions.InvalidParameterException;
import com.biit.infographic.core.exceptions.ReportNotReadyException;
import com.biit.infographic.logger.InfographicEngineLogger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class InfographicEngineService {

    public static final String INFOGRAPHIC_PATH = "infographics/";

    /**
     * Default access method. No selections and basic root path.
     *
     * @return all the template items with its content.
     */
    public List<InfographicTemplateAndContent> getInfographicReport() {
        return getInfographicReport(null);
    }

    /**
     * Shows only the selected items from the infography.
     *
     * @param selections Nodes that are selected by the user. Null if you want all of them.
     * @return the infographic modules generated.
     */
    public List<InfographicTemplateAndContent> getInfographicReport(List<TreeNode<String>> selections) {
        final List<InfographicTemplateAndContent> infographics = new ArrayList<>();
        if (selections != null) {
            selections = sortForestWithReference(selections, getSelectableElements());
        }
        InfographicEngineLogger.debug(this.getClass(), "Enabled elements '" + selections + "'.");
        final List<InfographicTemplate> templates = getTemplates(selections);
        final Map<InfographicIndexFile, Set<Parameter>> params = getParamsFromTemplates(templates);
        try {
            final Map<InfographicIndexFile, Set<Parameter>> filledParams = getValues(params);
            for (InfographicTemplate template : templates) {
                if (template.getTemplate() != null) {
                    final Set<Parameter> templateParams = filledParams.get(template.getIndexFile());
                    if (templateParams != null) {
                        infographics.add(addContentToTemplate(template, templateParams));
                    }
                }
            }
        } catch (InvalidParameterException | ElementDoesNotExistsException e) {
            InfographicEngineLogger.errorMessage(this.getClass(), e);
        } catch (ReportNotReadyException e) {
            InfographicEngineLogger.warning(this.getClass(), e.getMessage());
        }
        return infographics;
    }

    private InfographicTemplateAndContent addContentToTemplate(InfographicTemplate template, Set<Parameter> templateParams) {
        final InfographicTemplateAndContent infographicTemplateAndContent = new InfographicTemplateAndContent(
                template.getIndexFile() != null ? template.getIndexFile().getCompleteName() : null);
        infographicTemplateAndContent.setTemplate(template.getTemplate());
        final Map<String, String> variables = new HashMap<>();
        for (Parameter param : templateParams) {
            final Map<String, String> attributes = param.getAttributes();
            for (String key : attributes.keySet()) {
                variables.put("#" + param.getType() + "%" + param.getName() + "%" + key + "#", attributes.get(key));
            }
        }
        infographicTemplateAndContent.setContent(variables);
        return infographicTemplateAndContent;
    }

    private List<TreeNode<String>> getSelectableElements() {
        return InfographicFolder.getSelectableElementsTree(getReportPath());
    }

    private Map<InfographicIndexFile, Set<Parameter>> getValues(Map<InfographicIndexFile, Set<Parameter>> parameters)
            throws java.security.InvalidParameterException, ElementDoesNotExistsException, ReportNotReadyException {
        final Map<InfographicIndexFile, Set<Parameter>> filledParams = new HashMap<>();
        // Group parameters by type.
        for (InfographicIndexFile infographicDefinition : parameters.keySet()) {
            final Map<ParameterType, Set<Parameter>> parametersByType = groupParametersByType(filledParams.get(infographicDefinition));

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

    private Map<ParameterType, Set<Parameter>> groupParametersByType(Set<Parameter> parameters) throws InvalidParameterException {
        final Map<ParameterType, Set<Parameter>> parametersByType = new HashMap<>();
        for (Parameter parameter : parameters) {
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

    private String getReportPath() {
        return INFOGRAPHIC_PATH;
    }

    private List<InfographicTemplate> getTemplates(List<TreeNode<String>> selections) {
        final String reportPath = getReportPath();
        return InfographicFolder.getTemplatesFromPath(reportPath, selections);
    }

    private Map<InfographicIndexFile, Set<Parameter>> getParamsFromAllTemplates() {
        final List<InfographicTemplate> templates = getTemplates(null);
        return getParamsFromTemplates(templates);
    }

    private Map<InfographicIndexFile, Set<Parameter>> getParamsFromTemplates(List<InfographicTemplate> templates) {
        final Map<InfographicIndexFile, Set<Parameter>> params = new HashMap<>();
        for (InfographicTemplate template : templates) {
            final InfographicIndexFile templateDef = template.getIndexFile();
            final Set<Parameter> templateParams = Parameter.getParams(template);
            params.put(templateDef, templateParams);
        }
        return params;
    }

    /*
     * Sorts a List<TreeNode<>> to fit the structure from another Tree
     */
    private List<TreeNode<String>> sortForestWithReference(List<TreeNode<String>> forest, List<TreeNode<String>> referenceForest) {
        final List<TreeNode<String>> sortedForest = new ArrayList<>();
        if (forest != null && referenceForest != null) {
            for (TreeNode<String> referenceTree : referenceForest) {
                for (TreeNode<String> tree : forest) {
                    if (tree != null && referenceTree.getJsonFile().equals(tree.getJsonFile())) {
                        tree.setChildren(sortForestWithReference(tree.getChildren(), referenceTree.getChildren()));
                        sortedForest.add(tree);
                    }
                }
            }
        }
        return sortedForest;
    }
}

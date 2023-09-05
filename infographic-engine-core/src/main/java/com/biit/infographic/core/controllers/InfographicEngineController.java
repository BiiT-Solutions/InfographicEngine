package com.biit.infographic.core.controllers;


import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.engine.InfographicTemplate;
import com.biit.infographic.core.engine.InfographicTemplateAndContent;
import com.biit.infographic.core.engine.Parameter;
import com.biit.infographic.core.engine.ParameterType;
import com.biit.infographic.core.engine.content.DroolsContent;
import com.biit.infographic.core.engine.files.InfographicFileElement;
import com.biit.infographic.core.engine.files.InfographicFolder;
import com.biit.infographic.core.engine.files.TreeNode;
import com.biit.infographic.core.exceptions.ElementDoesNotExistsException;
import com.biit.infographic.core.exceptions.InvalidParameterException;
import com.biit.infographic.core.exceptions.ReportNotReadyException;
import com.biit.infographic.logger.InfographicEngineLogger;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class InfographicEngineController {

    public static final String INFOGRAPHIC_PATH = "/infographics";
    private final DroolsContent droolsContent;

    public InfographicEngineController(DroolsContent droolsContent) {
        this.droolsContent = droolsContent;
    }

    public List<InfographicTemplateAndContent> addContentToTemplates(List<InfographicTemplate> templates,
                                                                     Map<InfographicFileElement, Set<Parameter>> values) {
        final List<InfographicTemplateAndContent> infographics = new ArrayList<>();
        for (InfographicTemplate template : templates) {
            if (template.getTemplate() != null) {
                final Set<Parameter> templateParams = values.get(template.getIndexFile());
                infographics.add(addContentToTemplate(template, templateParams));
            }
        }
        return infographics;
    }

    private InfographicTemplateAndContent addContentToTemplate(InfographicTemplate template, Set<Parameter> templateParams) {
        final InfographicTemplateAndContent infographicTemplateAndContent = new InfographicTemplateAndContent(
                template.getIndexFile() != null ? template.getIndexFile().getCompleteName() : null);
        infographicTemplateAndContent.setTemplate(template.getTemplate());
        final Map<String, String> variables = new HashMap<>();
        if (templateParams != null) {
            for (Parameter param : templateParams) {
                final Map<String, String> attributes = param.getAttributes();
                for (String key : attributes.keySet()) {
                    variables.put("#" + param.getType() + "%" + param.getName() + "%" + key + "#", attributes.get(key));
                }
            }
        }
        infographicTemplateAndContent.setContent(variables);
        return infographicTemplateAndContent;
    }

    private List<TreeNode<String>> getSelectableElements() {
        return new InfographicFolder(null).getSelectableElementsTree(getReportPath());
    }

    public Map<InfographicFileElement, Set<Parameter>> getValues(DroolsSubmittedForm droolsSubmittedForm, Map<InfographicFileElement,
            Set<Parameter>> parameters) throws java.security.InvalidParameterException, ElementDoesNotExistsException, ReportNotReadyException {
        final Map<InfographicFileElement, Set<Parameter>> filledParams = new HashMap<>();
        // Group parameters by type.
        for (InfographicFileElement infographicDefinition : parameters.keySet()) {
            filledParams.put(infographicDefinition, parameters.get(infographicDefinition));
            final Map<ParameterType, Set<Parameter>> parametersByType = groupParametersByType(filledParams.get(infographicDefinition));

            // Update parameters with values.
            droolsContent.setDroolsVariablesValues(parametersByType.get(ParameterType.DROOLS), droolsSubmittedForm);

            // Obtain goals.
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


    private String getTemplateBasePath(DroolsSubmittedForm droolsSubmittedForm) {
        return getReportPath() + File.separator + droolsSubmittedForm.getName() + "_v" + droolsSubmittedForm.getVersion();
    }

    public List<InfographicTemplate> getTemplates(DroolsSubmittedForm droolsSubmittedForm) {
        return new InfographicFolder(null).getTemplatesFromPath(getTemplateBasePath(droolsSubmittedForm), null);
    }

    private List<InfographicTemplate> getTemplates(List<TreeNode<String>> selections) {
        final String reportPath = getReportPath();
        return new InfographicFolder(null).getTemplatesFromPath(reportPath, selections);
    }

    private Map<InfographicFileElement, Set<Parameter>> getParamsFromAllTemplates() {
        final List<InfographicTemplate> templates = getTemplates((List<TreeNode<String>>) null);
        return getParamsFromTemplates(templates);
    }

    public Map<InfographicFileElement, Set<Parameter>> getParamsFromTemplates(List<InfographicTemplate> templates) {
        final Map<InfographicFileElement, Set<Parameter>> params = new HashMap<>();
        for (InfographicTemplate template : templates) {
            final InfographicFileElement templateDef = template.getIndexFile();
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

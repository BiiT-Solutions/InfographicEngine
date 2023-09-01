package com.biit.infographic.core.engine.content;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.form.entity.TreeObject;
import com.biit.infographic.core.controllers.DroolsResultController;
import com.biit.infographic.core.engine.Parameter;
import com.biit.infographic.core.exceptions.ElementDoesNotExistsException;
import com.biit.infographic.core.models.DroolsResultDTO;
import com.biit.infographic.core.models.svg.serialization.ObjectMapperFactory;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class DroolsContent {

    private final DroolsResultController droolsResultController;

    public DroolsContent(DroolsResultController droolsResultController) {
        this.droolsResultController = droolsResultController;
    }

    /**
     * Search the form structure to find any variable value obtained when executing
     * the drools rules.
     *
     * @param parameters requested parameters
     * @throws ElementDoesNotExistsException if not found.
     */
    private void setDroolsVariablesValues(Set<Parameter> parameters, String formName, Integer formVersion,
                                          Long organizationId, String createdBy)
            throws ElementDoesNotExistsException {

        final DroolsResultDTO droolsResultDTO = droolsResultController.findLatest(formName, formVersion, organizationId, createdBy);

        final String droolsAnswer = droolsResultDTO.getForm();
        InfographicEngineLogger.debug(getClass().getName(),
                "Setting drools variables for examination '" + droolsResultDTO.getFormName() + "'. Response from drools:\n " + droolsAnswer);
        if (droolsAnswer == null || droolsAnswer.isEmpty()) {
            InfographicEngineLogger.errorMessage(getClass().getName(), "No content for '{}' with version '{}'.", formName, formVersion);
        } else {
            try {
                final DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
                domFactory.setNamespaceAware(true);
                final DocumentBuilder builder = domFactory.newDocumentBuilder();
                final Document document = builder.parse(new ByteArrayInputStream(droolsAnswer.getBytes(StandardCharsets.UTF_8)));
                final XPath xpathCompiler = XPathFactory.newInstance().newXPath();

                final DroolsSubmittedForm droolsSubmittedForm = ObjectMapperFactory.getObjectMapper().readValue(droolsResultDTO.getForm(), DroolsSubmittedForm.class);

                final LinkedHashSet<TreeObject> formElements = new LinkedHashSet<>();
                // Allow to search on the form root too
                formElements.add(examinationResult.getFormResult());
                formElements.addAll(examinationResult.getFormResult().getAllChildrenInHierarchy());

                for (TreeObject formElement : formElements) {
                    for (Parameter parameter : parameters) {
                        // Search for any variable defined in the parameters
                        for (String attribute : parameter.getAttributes().keySet()) {
                            if (parameter.getName() != null
                                    && (parameter.getName().equalsIgnoreCase(formElement.getName()) || parameter.getName().equals(formElement.getPathName()))) {
                                String path = formElement.getXPath() + "/variables/" + attribute + "/text()";

                                // Search as a variable.
                                String value = getValue(path, document, xpathCompiler);
                                // Not a variable, maybe a question value.
                                if (value == null) {
                                    path = formElement.getXPath() + "/" + attribute + "/text()";
                                    value = getValue(path, document, xpathCompiler);
                                }
                                String attributeValue;
                                if (value != null && value.length() > 0) {
                                    attributeValue = new GsonBuilder().create().toJson(value.trim());
                                    InfographicEngineLogger.info(getClass().getName(), attribute + " " + attributeValue);
                                } else {
                                    attributeValue = "";
                                    InfographicEngineLogger.warning(getClass().getName(), attribute + " has empty value.");
                                }
                                parameter.getAttributes().put(attribute, attributeValue);
                            }
                        }
                    }
                }
            } catch (ParserConfigurationException | XPathExpressionException | IOException | SAXException e) {
                InfographicEngineLogger.errorMessage(this.getClass(), e);
            }
        }
    }
}

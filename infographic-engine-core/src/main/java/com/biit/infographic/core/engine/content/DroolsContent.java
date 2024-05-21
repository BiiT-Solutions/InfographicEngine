package com.biit.infographic.core.engine.content;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.form.submitted.implementation.SubmittedObject;
import com.biit.infographic.core.engine.Parameter;
import com.biit.infographic.core.engine.content.value.Condition;
import com.biit.infographic.core.engine.content.value.ValueCalculator;
import com.biit.infographic.core.exceptions.ElementDoesNotExistsException;
import com.biit.infographic.logger.InfographicEngineLogger;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

@Component
public class DroolsContent {
    private static final DecimalFormat DECIMAL_FORMAT_VALUES = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.ENGLISH));

    private final ValueCalculator valueCalculator;

    public DroolsContent(ValueCalculator valueCalculator) {
        this.valueCalculator = valueCalculator;
    }


    /**
     * Search the form structure to find any variable value obtained when executing
     * the drools rules.
     *
     * @param parameters          requested parameters. Are updated with the drools value
     * @param droolsSubmittedForm the drools form to retrieve the values.
     * @throws ElementDoesNotExistsException if not found.
     */
    public void setDroolsVariablesValues(Set<Parameter> parameters, DroolsSubmittedForm droolsSubmittedForm)
            throws ElementDoesNotExistsException {
        if (droolsSubmittedForm == null) {
            InfographicEngineLogger.errorMessage(getClass().getName(), "No drools content.");
            return;
        }
        final String droolsAnswers = droolsSubmittedForm.generateXML();
        if (droolsAnswers == null || droolsAnswers.isBlank()) {
            InfographicEngineLogger.errorMessage(getClass().getName(), "No drools content.");
            return;
        }
        InfographicEngineLogger.debug(getClass().getName(),
                "Setting drools variables for form '{}' with version '{}'.", droolsSubmittedForm.getName(), droolsSubmittedForm.getVersion());
        try {
            //Parse Drools value XML.
            final DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true);
            final DocumentBuilder builder = domFactory.newDocumentBuilder();
            final Document document = builder.parse(new ByteArrayInputStream(droolsAnswers.getBytes(StandardCharsets.UTF_8)));
            final XPath xpathCompiler = XPathFactory.newInstance().newXPath();

            final LinkedHashSet<SubmittedObject> formElements = new LinkedHashSet<>();
            // Allow searching on the form root too
            formElements.add(droolsSubmittedForm);
            formElements.addAll(droolsSubmittedForm.getAllChildrenInHierarchy());

            if (parameters != null) {
                for (SubmittedObject submittedObject : formElements) {
                    for (Parameter parameter : parameters) {
                        InfographicEngineLogger.debug(this.getClass(), "Processing parameter '#{}%{}%{}#'.",
                                parameter.getType(), parameter.getName(), parameter.getAttributes());
                        // Search for any variable defined in the parameters
                        for (String attribute : parameter.getAttributes().keySet()) {
                            final String element;

                            //Check if attribute has some operators
                            final Condition condition = valueCalculator.getCondition(attribute);
                            if (condition == null) {
                                element = attribute;
                            } else {
                                element = condition.getElement();
                            }

                            if (parameter.getName() != null
                                    && (parameter.getName().equalsIgnoreCase(submittedObject.getTag())
                                    || parameter.getName().equals(submittedObject.getPathName()))) {
                                String path = submittedObject.getXPath() + "/variables/" + element + "/text()";

                                // Search as a variable.
                                String value = getValue(path, document, xpathCompiler);
                                // Not a variable, maybe a question value.
                                if (value == null) {
                                    path = submittedObject.getXPath() + "/" + element + "/text()";
                                    value = getValue(path, document, xpathCompiler);
                                }
                                final String attributeValue;
                                if (value != null && !value.isEmpty()) {
                                    if (condition == null) {
                                        attributeValue = value.trim();
                                        InfographicEngineLogger.info(getClass().getName(), element + " " + attributeValue);
                                    } else {
                                        //If it is a condition, resolve it.
                                        attributeValue = condition.getResult(value);
                                    }

                                } else {
                                    attributeValue = "";
                                    InfographicEngineLogger.warning(getClass().getName(), element + " has empty value.");
                                }
                                parameter.getAttributes().put(attribute, attributeValue);
                            }
                        }
                    }
                }
            }
        } catch (ParserConfigurationException | XPathExpressionException | IOException | SAXException e) {
            InfographicEngineLogger.errorMessage(this.getClass(), e);
        }
    }

    private String getValue(String path, Document document, XPath xpathCompiler) throws XPathExpressionException {
        InfographicEngineLogger.debug(this.getClass().getName(), "Searching variable in '" + path + "'.");
        final XPathExpression formulaExpression = xpathCompiler.compile(path);
        String value = (String) formulaExpression.evaluate(document, XPathConstants.STRING);

        if (value != null && !value.isEmpty()) {
            InfographicEngineLogger.debug(getClass().toString(), "Expression '" + path + "' has a value of '" + value.trim() + "'.");
            // If the value is a number, format it to X decimal position
            try {
                DECIMAL_FORMAT_VALUES.setRoundingMode(RoundingMode.DOWN);
                value = DECIMAL_FORMAT_VALUES.format(Double.parseDouble(value));
            } catch (IllegalArgumentException e) {
                //It is not a number.
            }
            return value;
        }
        return null;
    }
}

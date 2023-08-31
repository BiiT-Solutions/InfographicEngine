package com.biit.infographic.core.files;


import com.biit.infographic.core.exceptions.ElementDoesNotExistsException;
import com.biit.infographic.core.exceptions.ReportNotReadyException;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IInfographicEngine {

    Map<InfographicIndexFile, Set<IParameter>> getValues(Map<InfographicIndexFile, Set<IParameter>> parameters)
            throws InvalidParameterException, ElementDoesNotExistsException, ReportNotReadyException;

    String getReportPath();

    List<Infographic> getInfographicReport(List<TreeNode<String>> selections);

    List<Infographic> getInfographicReport(List<TreeNode<String>> selections, String rootPath);

    List<TreeNode<String>> getSelectableElements();

    List<TreeNode<String>> getSelectableElements(String rootPath);

    Map<InfographicIndexFile, Set<IParameter>> getParamsFromAllTemplates(String rootPath);
}

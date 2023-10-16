package com.biit.infographic.core.engine.files;


import com.biit.infographic.core.engine.InfographicTemplate;
import com.biit.infographic.core.exceptions.MalformedTemplateException;
import com.biit.infographic.core.models.svg.serialization.ObjectMapperFactory;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


/***
 * Generates a tree structure that mirrors the infographic folder structure, but also with the content of each json file.
 */
public class InfographicFolder extends TreeNode<InfographicFileElement> {
    public static final String JSON_EXTENSION = ".json";
    public static final String INDEX_FILE_NAME = "index.json";

    public InfographicFolder(InfographicFileElement infographicFileElement) {
        super(infographicFileElement);
    }

    public List<InfographicTemplate> getAllTemplates(String rootPath) {
        final List<InfographicTemplate> templates = new ArrayList<>();
        final InfographicTemplate template = getTemplate(rootPath);
        if (template != null) {
            templates.add(template);
        }
        for (TreeNode<InfographicFileElement> child : getChildren()) {
            templates.addAll(((InfographicFolder) child).getAllTemplates(FileSearcher.getInfographicPath(rootPath, getDefinition())));
        }
        return templates;
    }

    public List<InfographicTemplate> getSelectedTemplates(String rootPath) {
        final List<InfographicTemplate> templates = new ArrayList<>();
        if (getDefinition().isSelected()) {
            final List<TreeNode<InfographicFileElement>> children = getChildren();
            if (children.isEmpty()) {
                templates.add(getTemplate(rootPath + File.separator));
            } else {
                for (TreeNode<InfographicFileElement> child : children) {
                    templates.addAll(((InfographicFolder) child).getSelectedTemplates(FileSearcher.getInfographicPath(rootPath, getDefinition())));
                }
            }
        }
        return templates;
    }

    /**
     * Reads the index.json on a folder and generates an InfographicTemplate with it.
     *
     * @param path the path of the template.
     * @return the template.
     */
    private InfographicTemplate getTemplate(String path) {
        if (!this.getChildren().isEmpty()) {
            return null;
        } else {
            final InfographicTemplate infographicTemplate = new InfographicTemplate();
            infographicTemplate.setIndexFile(getJsonFile());
            try {
                infographicTemplate.setTemplate(new FileSearcher().readFile(FileSearcher.getInfographicPath(path, getDefinition())));
            } catch (FileNotFoundException | NullPointerException e) {
                InfographicEngineLogger.errorMessage(this.getClass(), e);
            }
            return infographicTemplate;
        }
    }

    private void selectAllElements() {
        getDefinition().setSelected(true);
        for (TreeNode<InfographicFileElement> child : getChildren()) {
            ((InfographicFolder) child).selectAllElements();
        }
    }

    private void selectElements(List<TreeNode<String>> selections) {
        if (selections == null) {
            selectAllElements();
            return;
        }
        for (TreeNode<String> selection : selections) {
            if (getDefinition().getJsonFile().equals(selection.getJsonFile())) {
                getDefinition().setSelected(true);
                for (TreeNode<InfographicFileElement> child : getChildren()) {
                    ((InfographicFolder) child).selectElements(selection.getChildren());
                }
            }
        }
    }

    private void clearSelection() {
        if (getDefinition().isSelectable()) {
            getDefinition().setSelected(false);
        }
        for (TreeNode<InfographicFileElement> child : getChildren()) {
            ((InfographicFolder) child).clearSelection();
        }
    }

    private TreeNode<String> getSelectableElementsTree() {
        if (getDefinition().isSelectable()) {
            final TreeNode<String> tree = new TreeNode<>(getDefinition().getJsonFile());
            for (TreeNode<InfographicFileElement> child : getChildren()) {
                if (((InfographicFolder) child).getSelectableElementsTree() != null) {
                    tree.addChild(((InfographicFolder) child).getSelectableElementsTree());
                }
            }
            return tree;
        }
        return null;
    }

    private InfographicFileElement getDefinition() {
        return getJsonFile();
    }

    public List<InfographicTemplate> getTemplatesFromPath(String rootPath, List<TreeNode<String>> selections) throws MalformedTemplateException {
        final List<InfographicFolder> infographicFolderNodes = getInfographicNodes(rootPath);
        final List<InfographicTemplate> templates = new ArrayList<>();
        for (InfographicFolder node : infographicFolderNodes) {
            node.selectElements(selections);
            templates.addAll(node.getSelectedTemplates(rootPath));
        }
        return templates;
    }

    public List<TreeNode<String>> getSelectableElementsTree(String rootPath) throws MalformedTemplateException {
        final List<InfographicFolder> infographicFolderNodes = getInfographicNodes(rootPath);
        final List<TreeNode<String>> items = new ArrayList<>();
        for (InfographicFolder node : infographicFolderNodes) {
            final TreeNode<String> item = node.getSelectableElementsTree();
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }

    /**
     * Reads the folder index.json, and from there any element or folder that is described.
     *
     * @param path the path to search.
     * @return A list of InfographicTree that each one represents a piece of infographic.
     */
    private List<InfographicFolder> getInfographicNodes(String path) throws MalformedTemplateException {
        if (!path.endsWith(JSON_EXTENSION)) {
            try {
                if (!path.endsWith(File.separator)) {
                    path += File.separator;
                }
                // Parse index.json structure
                InfographicEngineLogger.debug(InfographicFolder.class.getName(),
                        "Searching for '" + path + INDEX_FILE_NAME + "'.");
                final String indexFile = new FileSearcher().readFile(path + INDEX_FILE_NAME);
                if (indexFile == null) {
                    throw new MalformedTemplateException(this.getClass(), "No index file at '" + path + INDEX_FILE_NAME + "'");
                }
                final List<InfographicFileElement> definedFilesOrFolders;
                try {
                    definedFilesOrFolders = ObjectMapperFactory.getObjectMapper().readValue(indexFile, new TypeReference<>() {
                    });
                } catch (IllegalArgumentException | JsonSyntaxException | JsonProcessingException e) {
                    InfographicEngineLogger.info(InfographicFolder.class.getName(), "Malformed json:\n" + indexFile);
                    throw e;
                }

                final List<InfographicFolder> infographicFolders = new ArrayList<>();
                for (InfographicFileElement infographicFileElement : definedFilesOrFolders) {
                    final InfographicFolder node = new InfographicFolder(infographicFileElement);
                    infographicFolders.add(node);
                    if (infographicFileElement.isFolder()) {
                        for (InfographicFolder subTree : getInfographicNodes(FileSearcher.getInfographicPath(path, node.getDefinition()))) {
                            if (subTree != null) {
                                node.addChild(subTree);
                            }
                        }
                    }
                }
                return infographicFolders;
            } catch (FileNotFoundException | NullPointerException | JsonProcessingException e) {
                InfographicEngineLogger.warning(InfographicFolder.class.getName(),
                        "File '" + path + INDEX_FILE_NAME + "' not found or invalid.");
                throw new MalformedTemplateException(this.getClass(), "No index file at '" + path + INDEX_FILE_NAME + "'");
            }
        }
        return new ArrayList<>();
    }

}

package com.biit.infographic.core.engine.files;


import com.biit.infographic.core.engine.InfographicTemplate;
import com.biit.infographic.core.models.svg.serialization.ObjectMapperFactory;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.utils.file.FileReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.JsonSyntaxException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class InfographicFolder extends TreeNode<InfographicIndexFile> {
    public static final String JSON_EXTENSION = ".json";
    public static final String INDEX_FILE_NAME = "index.json";

    public InfographicFolder(InfographicIndexFile def) {
        super(def);
    }

    public String getPath(String root) {
        String path = root + getDefinition().getJsonFile();

        if (getDefinition().isFolder()) {
            path = path + "/";
        } else {
            path = path + "_v" + getDefinition().getJsonVersion() + JSON_EXTENSION;
        }
        return path;
    }

    public List<InfographicTemplate> getAllTemplates(String rootPath) {
        final List<InfographicTemplate> templates = new ArrayList<>();
        final InfographicTemplate template = getTemplate(rootPath);
        if (template != null) {
            templates.add(template);
        }
        for (TreeNode<InfographicIndexFile> child : getChildren()) {
            templates.addAll(((InfographicFolder) child).getAllTemplates(getPath(rootPath)));
        }
        return templates;
    }

    public List<InfographicTemplate> getSelectedTemplates(String rootPath) {
        final List<InfographicTemplate> templates = new ArrayList<>();
        if (getDefinition().isSelected()) {
            final List<TreeNode<InfographicIndexFile>> children = getChildren();
            if (children.isEmpty()) {
                templates.add(getTemplate(rootPath));
            } else {
                for (TreeNode<InfographicIndexFile> child : children) {
                    templates.addAll(((InfographicFolder) child).getSelectedTemplates(getPath(rootPath)));
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
    public InfographicTemplate getTemplate(String path) {
        if (!this.getChildren().isEmpty()) {
            return null;
        } else {
            final InfographicTemplate infographicTemplate = new InfographicTemplate();
            infographicTemplate.setIndexFile(getJsonFile());
            try {
                infographicTemplate.setTemplate(FileReader.readFile(FileReader.getResource(getPath(path))));
            } catch (FileNotFoundException | NullPointerException e) {
                InfographicEngineLogger.errorMessage(this.getClass(), e);
            }
            return infographicTemplate;
        }
    }

    public void selectAllElements() {
        getDefinition().setSelected(true);
        for (TreeNode<InfographicIndexFile> child : getChildren()) {
            ((InfographicFolder) child).selectAllElements();
        }
    }

    public void selectElements(List<TreeNode<String>> selections) {
        if (selections == null) {
            selectAllElements();
            return;
        }
        for (TreeNode<String> selection : selections) {
            if (getDefinition().getJsonFile().equals(selection.getJsonFile())) {
                getDefinition().setSelected(true);
                for (TreeNode<InfographicIndexFile> child : getChildren()) {
                    ((InfographicFolder) child).selectElements(selection.getChildren());
                }
            }
        }
    }

    public void clearSelection() {
        if (getDefinition().isSelectable()) {
            getDefinition().setSelected(false);
        }
        for (TreeNode<InfographicIndexFile> child : getChildren()) {
            ((InfographicFolder) child).clearSelection();
        }
    }

    public TreeNode<String> getSelectableElementsTree() {
        if (getDefinition().isSelectable()) {
            final TreeNode<String> tree = new TreeNode<>(getDefinition().getJsonFile());
            for (TreeNode<InfographicIndexFile> child : getChildren()) {
                if (((InfographicFolder) child).getSelectableElementsTree() != null) {
                    tree.addChild(((InfographicFolder) child).getSelectableElementsTree());
                }
            }
            return tree;
        }
        return null;
    }

    public InfographicIndexFile getDefinition() {
        return getJsonFile();
    }

    public static List<InfographicTemplate> getTemplatesFromPath(String rootPath, List<TreeNode<String>> selections) {
        final List<InfographicFolder> infographicFolderNodes = InfographicFolder.getInfographicNodes(rootPath);
        final List<InfographicTemplate> templates = new ArrayList<>();
        for (InfographicFolder node : infographicFolderNodes) {
            node.selectElements(selections);
            templates.addAll(node.getSelectedTemplates(rootPath));
        }
        return templates;
    }

    public static List<TreeNode<String>> getSelectableElementsTree(String rootPath) {
        final List<InfographicFolder> infographicFolderNodes = InfographicFolder.getInfographicNodes(rootPath);
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
    public static List<InfographicFolder> getInfographicNodes(String path) {
        if (!path.contains(JSON_EXTENSION)) {
            try {
                // Parse index.json structure
                InfographicEngineLogger.debug(InfographicFolder.class.getName(),
                        "Searching for '" + path + INDEX_FILE_NAME + "'.");
                final String indexFile = FileReader
                        .readFile(FileReader.getResource(InfographicFolder.class, path + INDEX_FILE_NAME));
                final List<InfographicIndexFile> definedFilesOrFolders;
                try {
                    definedFilesOrFolders = ObjectMapperFactory.getObjectMapper().readValue(indexFile, new TypeReference<>() {
                    });
                } catch (JsonSyntaxException | JsonProcessingException e) {
                    InfographicEngineLogger.info(InfographicFolder.class.getName(), "Malformed json:\n" + indexFile);
                    throw e;
                }

                final List<InfographicFolder> infographicFolders = new ArrayList<>();
                for (InfographicIndexFile def : definedFilesOrFolders) {
                    final InfographicFolder node = new InfographicFolder(def);
                    infographicFolders.add(node);
                    if (def.isFolder()) {
                        for (InfographicFolder subTree : getInfographicNodes(node.getPath(path))) {
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
            }
        }
        return new ArrayList<>();
    }

}

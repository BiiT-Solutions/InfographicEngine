package com.biit.infographic.core.files;


import com.biit.infographic.core.models.svg.serialization.ObjectMapperFactory;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.utils.file.FileReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.JsonSyntaxException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class InfographicTree extends TreeNode<InfographicIndexFile> {
    private String template;
    public static final String EXTENSION = ".json";
    public static final String INDEX_FILE_NAME = "index.json";

    public InfographicTree(InfographicIndexFile def) {
        super(def);
    }

    public String getPath(String root) {
        String path = root + getDefinition().getJsonFile();

        if (getDefinition().isFolder()) {
            path = path + "/";
        } else {
            path = path + "_v" + getDefinition().getJsonVersion() + EXTENSION;
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
            templates.addAll(((InfographicTree) child).getAllTemplates(getPath(rootPath)));
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
                    templates.addAll(((InfographicTree) child).getSelectedTemplates(getPath(rootPath)));
                }
            }
        }
        return templates;
    }

    public InfographicTemplate getTemplate(String path) {
        if (!this.getChildren().isEmpty()) {
            return null;
        } else {
            if (this.template == null) {
                try {
                    this.template = FileReader.readFile(FileReader.getResource(getPath(path)));
                } catch (FileNotFoundException | NullPointerException e) {
                    InfographicEngineLogger.errorMessage(this.getClass().getName(), e);
                }
            }
            final InfographicTemplate template = new InfographicTemplate();
            template.setIndexFile(getData());
            template.setTemplate(this.template);
            return template;
        }
    }

    public void selectAllElements() {
        getDefinition().setSelected(true);
        for (TreeNode<InfographicIndexFile> child : getChildren()) {
            ((InfographicTree) child).selectAllElements();
        }
    }

    public void selectElements(List<TreeNode<String>> selections) {
        if (selections == null) {
            selectAllElements();
            return;
        }
        for (TreeNode<String> selection : selections) {
            if (getDefinition().getJsonFile().equals(selection.getData())) {
                getDefinition().setSelected(true);
                for (TreeNode<InfographicIndexFile> child : getChildren()) {
                    ((InfographicTree) child).selectElements(selection.getChildren());
                }
            }
        }
    }

    public void clearSelection() {
        if (getDefinition().isSelectable()) {
            getDefinition().setSelected(false);
        }
        for (TreeNode<InfographicIndexFile> child : getChildren()) {
            ((InfographicTree) child).clearSelection();
        }
    }

    public TreeNode<String> getSelectableElementsTree() {
        if (getDefinition().isSelectable()) {
            final TreeNode<String> tree = new TreeNode<>(getDefinition().getJsonFile());
            for (TreeNode<InfographicIndexFile> child : getChildren()) {
                if (((InfographicTree) child).getSelectableElementsTree() != null) {
                    tree.addChild(((InfographicTree) child).getSelectableElementsTree());
                }
            }
            return tree;
        }
        return null;
    }

    public InfographicIndexFile getDefinition() {
        return getData();
    }

    /**
     * Reads the folder index.json, and from then any element or folder that is described.
     *
     * @param path               the path to search.
     * @return A list of InfographicTree that each one represents a piece of infographic.
     */
    public static List<InfographicTree> getInfographicNodes(String path) {
        if (!path.contains(EXTENSION)) {
            try {
                // Parse index.json structure
                InfographicEngineLogger.debug(InfographicTree.class.getName(),
                        "Searching for '" + path + INDEX_FILE_NAME + "'.");
                final String indexFile = FileReader
                        .readFile(FileReader.getResource(InfographicTree.class, path + INDEX_FILE_NAME));
                final List<InfographicIndexFile> definedFilesOrFolders;
                try {
                    definedFilesOrFolders = ObjectMapperFactory.getObjectMapper().readValue(indexFile, new TypeReference<>() {
                    });
                } catch (JsonSyntaxException | JsonProcessingException e) {
                    InfographicEngineLogger.info(InfographicTree.class.getName(), "Malformed json:\n" + indexFile);
                    throw e;
                }

                final List<InfographicTree> infographicTrees = new ArrayList<>();
                for (InfographicIndexFile def : definedFilesOrFolders) {
                    final InfographicTree node = new InfographicTree(def);
                    infographicTrees.add(node);
                    if (def.isFolder()) {
                        for (InfographicTree subTree : getInfographicNodes(node.getPath(path))) {
                            if (subTree != null) {
                                node.addChild(subTree);
                            }
                        }
                    }
                }
                return infographicTrees;
            } catch (FileNotFoundException | NullPointerException | JsonProcessingException e) {
                InfographicEngineLogger.warning(InfographicTree.class.getName(),
                        "File '" + path + INDEX_FILE_NAME + "' not found or invalid.");
            }
        }
        return new ArrayList<>();
    }

}

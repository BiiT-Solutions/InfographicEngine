package com.biit.infographic.core.engine.files;

import com.biit.infographic.logger.InfographicEngineLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TreeNode<T> {
    private final T jsonFile;
    private TreeNode<T> parent;
    private List<TreeNode<T>> children;

    public TreeNode(T jsonFile) {
        this.jsonFile = jsonFile;
        children = new ArrayList<>();
    }

    public void setChildren(List<TreeNode<T>> children) {
        this.children = new ArrayList<>();
        for (TreeNode<T> child : children) {
            if (child != null) {
                addChild(child);
            }
        }
    }

    public void addChild(TreeNode<T> node) {
        if (node != null) {
            node.setParent(this);
            children.add(node);
        } else {
            InfographicEngineLogger.warning(getClass().getName(), "Node is null!");
        }
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public T getJsonFile() {
        return jsonFile;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return toString("");
    }

    private String toString(String prefix) {
        final StringBuilder str = new StringBuilder();
        for (TreeNode<T> node : getChildren()) {
            if (!str.isEmpty()) {
                str.append(", ");
            }
            str.append(prefix).append(node.toString(prefix));
        }
        return (jsonFile != null ? jsonFile.toString() : "") + (!str.isEmpty() ? "[" + str + "]" : "");
    }

    public TreeNode<T> getChild(T jsonFile) {
        if (jsonFile != null) {
            for (TreeNode<T> child : getChildren()) {
                if (Objects.equals(child.getJsonFile(), jsonFile)) {
                    return child;
                }
            }
        }
        return null;
    }

    public List<T> flattenTree() {
        final List<T> flatTree = new ArrayList<>();
        final List<TreeNode<T>> children = getChildren();
        flatTree.add(getJsonFile());
        if (children != null && !children.isEmpty()) {
            for (TreeNode<T> child : getChildren()) {
                flatTree.addAll(child.flattenTree());
            }
        }
        return flatTree;
    }

    public List<T> flattenTreeLeafs() {
        final List<T> flatTree = new ArrayList<>();
        final List<TreeNode<T>> children = getChildren();
        if (children != null && !children.isEmpty()) {
            for (TreeNode<T> child : getChildren()) {
                flatTree.addAll(child.flattenTree());
            }
        } else {
            flatTree.add(getJsonFile());
        }
        return flatTree;
    }

}

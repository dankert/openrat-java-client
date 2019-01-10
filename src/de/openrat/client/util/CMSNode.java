package de.openrat.client.util;

import java.util.ArrayList;
import java.util.List;

public class CMSNode {

    private static final CMSNode EMPTY_NODE = new CMSNode(null, null, new ArrayList<>());

    private String name;
    private String value;
    private List<CMSNode> children;

    public CMSNode(String name, String value, List<CMSNode> children) {
        super();
        this.name = name;
        this.value = value;
        this.children = children;
    }

    public List<CMSNode> getChildren() {

        if (children != null)
            return children;
        else
            return new ArrayList<CMSNode>();
    }

    public String getFirstChildValue(String name) {
        CMSNode node = getFirstChildByName(name);
        if (node != null)
            return node.getValue();
        else
            return null;
    }

    public CMSNode getFirstChildByName(String name) {

        if (children == null)
            return EMPTY_NODE;

        for (CMSNode node : children) {
            if (name.equals(node.getName()))
                return node;
        }

        return EMPTY_NODE;
    }

    public String getValue() {
        return value;
    }

    public boolean isTrue() {
        return Boolean.valueOf(value).booleanValue();
    }

    public int toInt() {
        return NumberUtils.toInt(value);
    }

    public long toLong() {
        return NumberUtils.toLong(value);
    }


    public String getName() {
        return name;
    }

    public boolean isEmpty() {
        return name == null;
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " + getName() + "=" + getValue() + " children:" + children.size() + " "
                + children.toString();
    }
}

package com.zazsona.decorheads.blockmeta.library.node;

public class Node
{
    private Node parent;

    public Node()
    {
        this.parent = null;
    }

    Node(Node parent)
    {
        this.parent = parent;
    }

    public Node getParent()
    {
        return parent;
    }

    void setParent(Node node)
    {
        this.parent = node;
    }
}

package com.zazsona.decorheads.blockmeta.library.node;

public class Node
{
    private Node parent;

    public Node()
    {
        this.parent = null;
    }

    public Node(Node parent)
    {
        this.parent = parent;
    }
}

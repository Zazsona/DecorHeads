package com.zazsona.decorheads.headdata;

public abstract class HeadDecorator implements IHead
{
    private IHead head;

    public HeadDecorator(IHead head)
    {
        this.head = head;
    }

    @Override
    public String getKey()
    {
        return head.getKey();
    }

    @Override
    public String getName()
    {
        return head.getName();
    }

    @Override
    public String getTextureEncoding()
    {
        return head.getTextureEncoding();
    }

    @Override
    public boolean isObtainedThroughDrops()
    {
        return head.isObtainedThroughDrops();
    }

    @Override
    public boolean isObtainedThroughCreation()
    {
        return head.isObtainedThroughCreation();
    }
}

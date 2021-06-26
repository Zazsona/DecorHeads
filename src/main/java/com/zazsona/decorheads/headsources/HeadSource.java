package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.headdata.IHead;

import java.util.HashSet;
import java.util.Set;

public abstract class HeadSource
{
    private IHead head;
    private HeadSourceType headSourceType;

    public HeadSource(IHead head, HeadSourceType headSourceType)
    {
        this.head = head;
        this.headSourceType = headSourceType;
    }

    public IHead getHead()
    {
        return head;
    }

    public HeadSourceType getSourceType()
    {
        return headSourceType;
    }
}

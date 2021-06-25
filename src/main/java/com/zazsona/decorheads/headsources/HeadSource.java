package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.headdata.IHead;

import java.util.HashSet;
import java.util.Set;

public abstract class HeadSource implements IHeadSource
{
    private IHead head;

    public HeadSource(IHead head)
    {
        this.head = head;
    }

    @Override
    public IHead getHead()
    {
        return head;
    }
}

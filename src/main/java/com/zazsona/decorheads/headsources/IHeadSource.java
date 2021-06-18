package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.headdata.IHead;

import java.util.Set;

public interface IHeadSource
{
    public IHead getHead();

    public Set<HeadSourceType> getSourceTypes();
}

package com.zazsona.decorheads.event.head;

import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HeadExplodeEvent extends BlockExplodeEvent implements IHeadEvent
{
    private IHead head;

    public HeadExplodeEvent(@NotNull Block what, @NotNull List<Block> blocks, float yield, IHead head)
    {
        super(what, blocks, yield);
        this.head = head;
    }

    /**
     * Gets the definition of the head that was broken
     * @return
     */
    public IHead getHead()
    {
        return head;
    }
}

package com.zazsona.decorheads.event.head;

import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class HeadBreakEvent extends BlockBreakEvent implements IHeadEvent
{
    private IHead head;

    public HeadBreakEvent(Block theBlock, Player player, IHead head)
    {
        super(theBlock, player);
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

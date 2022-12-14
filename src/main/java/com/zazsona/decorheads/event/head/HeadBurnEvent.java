package com.zazsona.decorheads.event.head;

import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HeadBurnEvent extends BlockBurnEvent implements IHeadEvent
{
    private IHead head;

    public HeadBurnEvent(@NotNull Block block, @Nullable Block ignitingBlock, IHead head)
    {
        super(block, ignitingBlock);
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

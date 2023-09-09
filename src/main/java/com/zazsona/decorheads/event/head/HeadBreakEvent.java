package com.zazsona.decorheads.event.head;

import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public class HeadBreakEvent extends HeadDestroyEvent implements Cancellable
{
    private final Player player;
    private boolean dropHead;

    public HeadBreakEvent(@NotNull Block block, @NotNull IHead head, @NotNull Player player)
    {
        super(block, head);
        this.player = player;
        this.dropHead = true;
    }

    /**
     * Gets the Player that is breaking the head involved in this event.
     * @return The Player that is breaking the head involved in this event
     */
    @NotNull
    public Player getPlayer()
    {
        return player;
    }

    /**
     * Sets whether the head will drop itself.
     * @param dropHead Whether the head will drop itself
     */
    public void setDropHead(boolean dropHead) {
        this.dropHead = dropHead;
    }

    /**
     * Gets whether or not the head will drop itself.
     *
     * @return Whether or not the head will drop itself
     */
    public boolean isDropHead() {
        return this.dropHead;
    }
}

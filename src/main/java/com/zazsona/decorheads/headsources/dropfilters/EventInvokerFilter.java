package com.zazsona.decorheads.headsources.dropfilters;

import com.zazsona.decorheads.headsources.HeadSourceType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

public class EventInvokerFilter extends DropSourceFilter
{
    private EventInvoker invoker;

    public EventInvokerFilter(EventInvoker invoker)
    {
        this.invoker = invoker;
    }

    public EventInvoker getInvoker()
    {
        return invoker;
    }

    private boolean checkPass(Player player)
    {
        if (invoker == EventInvoker.ANY)
            return true;
        else if (player != null && invoker == EventInvoker.PLAYER)
            return true;
        else if (player == null && invoker == EventInvoker.ENVIRONMENT)
            return true;
        else
            return false;
    }

    @Override
    protected boolean passFilter(HeadSourceType sourceType, EntityDeathEvent e)
    {
        LivingEntity entity = e.getEntity();
        return checkPass(entity.getKiller());
    }

    public static EventInvoker matchInvoker(String invokerName)
    {
        try
        {
            if (invokerName == null)
                throw new IllegalArgumentException("invokerName cannot be null.");
            invokerName = invokerName.trim().toUpperCase().replace(" ", "_");
            EventInvoker invokerResult = EventInvoker.valueOf(invokerName);
            return invokerResult;
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }

    }

    public enum EventInvoker
    {
        ANY,
        PLAYER,
        ENVIRONMENT
    }
}

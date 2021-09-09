package com.zazsona.decorheads.headsources.dropfilters;

import com.zazsona.decorheads.headsources.HeadSourceType;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class ToolDropFilter extends DropSourceFilter
{
    private Set<Material> tools = new HashSet<>();

    public ToolDropFilter(Collection<Material> tools)
    {
        if (tools != null)
            this.tools.addAll(tools);
    }

    public Set<Material> getTools()
    {
        return tools;
    }

    private boolean checkPass(Material tool)
    {
        return (tools.size() == 0 || tools.contains(tool));
    }

    @Override
    protected boolean passFilter(HeadSourceType sourceType, BlockBreakEvent e)
    {
        ItemStack tool = e.getPlayer().getInventory().getItemInMainHand();
        return checkPass(tool.getType());
    }

    @Override
    protected boolean passFilter(HeadSourceType sourceType, EntityDeathEvent e)
    {
        Player killer = e.getEntity().getKiller();
        ItemStack tool = (killer != null) ? killer.getInventory().getItemInMainHand() : new ItemStack(Material.AIR);
        return checkPass(tool.getType());
    }
}

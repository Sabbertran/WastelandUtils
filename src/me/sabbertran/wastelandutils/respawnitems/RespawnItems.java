package me.sabbertran.wastelandutils.respawnitems;

import java.util.ArrayList;
import me.sabbertran.wastelandutils.WastelandUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnItems implements Listener
{

    private WastelandUtils main;
    private ArrayList<ItemStack> respawnItems = new ArrayList<ItemStack>();

    public RespawnItems(WastelandUtils wu, ArrayList<String> items)
    {
        this.main = wu;
        
        for(String s : items)
        {
            String[] split = s.split(":");
            this.respawnItems.add(new ItemStack(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
        }
    }

    @EventHandler
    public void onPlayerRespawn(final PlayerRespawnEvent ev)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (ItemStack is : respawnItems)
                {
                    ev.getPlayer().getInventory().addItem(is);
                }
            }
        }.runTaskLater(main, 1L);
    }
}

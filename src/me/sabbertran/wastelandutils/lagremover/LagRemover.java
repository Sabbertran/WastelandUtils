package me.sabbertran.wastelandutils.lagremover;

import me.sabbertran.wastelandutils.WastelandUtils;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class LagRemover
{

    private WastelandUtils main;

    public LagRemover(WastelandUtils wu)
    {
        this.main = wu;

        main.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable()
        {
            public void run()
            {
                killZombies();
            }
        }, 18000L, 18000L);
    }

    private void killZombies()
    {
        main.getServer().broadcastMessage(ChatColor.GOLD + "[LagKiller]" + ChatColor.AQUA + " In 60 Sekunden werden alle Untoten ausgerottet!");

        main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
        {
            public void run()
            {
                World fallout1 = main.getServer().getWorld("Fallout1");

                int count = 0;
                for (Entity e : fallout1.getEntities())
                {
                    if (e.getType().equals(EntityType.ZOMBIE) || e.getType().equals(EntityType.ARROW))
                    {
                        e.remove();
                        count++;
                    }
                }
                main.getLog().info("Removed " + count + " Zombies and Arrows");
                main.getServer().broadcastMessage(ChatColor.GOLD + "[LagKiller]" + ChatColor.AQUA + " Alle Untoten wurden ausgerottet!");
            }
        }, 1200L);
    }
}

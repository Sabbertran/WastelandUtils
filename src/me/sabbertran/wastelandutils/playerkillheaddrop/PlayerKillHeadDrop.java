package me.sabbertran.wastelandutils.playerkillheaddrop;

import java.util.Random;
import me.sabbertran.wastelandutils.WastelandUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerKillHeadDrop implements Listener
{

    private WastelandUtils main;

    public PlayerKillHeadDrop(WastelandUtils wu)
    {
        this.main = wu;
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent ev)
    {
        Player death = ev.getEntity().getPlayer();
        Player killer = ev.getEntity().getKiller();

        if (killer != null)
        {
            int chance = main.getPlayerHeadDropChance();
            Random r = new Random();
            int ran = r.nextInt(100);

            if (ran <= chance)
            {
                ItemStack head = new ItemStack(Material.SKULL_ITEM);
                head.setDurability((short)3);
                SkullMeta meta = (SkullMeta) head.getItemMeta();
                meta.setOwner(death.getName());
                head.setItemMeta(meta);
                ev.getDrops().add(head);
            }
        }

    }
}

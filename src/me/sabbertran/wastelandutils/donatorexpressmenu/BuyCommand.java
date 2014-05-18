package me.sabbertran.wastelandutils.donatorexpressmenu;

import java.util.Arrays;
import me.sabbertran.wastelandutils.WastelandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BuyCommand implements CommandExecutor
{

    private WastelandUtils main;
    private DonatorExpressMenu menu;

    public BuyCommand(DonatorExpressMenu dem, WastelandUtils wu)
    {
        this.main = wu;
        this.menu = dem;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if (sender instanceof Player)
        {
            Player p = (Player) sender;

            ItemStack is = new ItemStack(399);
            ItemMeta ismeta = is.getItemMeta();
            ismeta.setDisplayName("§b" + p.getName());
            ismeta.setLore(Arrays.asList(new String[]
            {
                menu.getTokens(p.getName()) + " Tokens"
            }));
            is.setItemMeta(ismeta);

            Inventory inv = main.getServer().createInventory(null, 27, "§6§lShop");

            for (int i = 0; i < 27; i++)
            {
                inv.setItem(i, menu.getBuyMenu().getItem(i));
            }

            inv.setItem(13, is);

            p.openInventory(inv);
        } else
        {
            sender.sendMessage("[WastelandUtils] You have to be a player to perform this command.");
        }

        return true;
    }
}

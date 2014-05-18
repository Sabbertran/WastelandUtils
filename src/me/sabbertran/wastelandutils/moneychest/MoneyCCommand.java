package me.sabbertran.wastelandutils.moneychest;

import me.sabbertran.wastelandutils.WastelandUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCCommand implements CommandExecutor
{
    
    private WastelandUtils main;
    private MoneyChest mchest;
    
    public MoneyCCommand(WastelandUtils wu, MoneyChest mc)
    {
        this.main = wu;
        this.mchest = mc;
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if (sender instanceof Player)
        {
            Player p = (Player) sender;
            
            Block b = p.getTargetBlock(null, 4);
            if (b.getType().equals(Material.CHEST) || b.getType().equals(Material.TRAPPED_CHEST))
            {
                Chest c = (Chest) b.getState();
                
                int coins = mchest.getCoins(c.getInventory());
                p.sendMessage("There are " + coins + " Coins in this chest.");
            } else
            {
                p.sendMessage("You have to look at a chest to use this command");
            }
        } else
        {
            sender.sendMessage("You have to be a player to use this command.");
        }
        
        
        
        return true;
    }
}

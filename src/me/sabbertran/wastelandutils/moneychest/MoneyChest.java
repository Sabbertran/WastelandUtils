package me.sabbertran.wastelandutils.moneychest;

import me.sabbertran.wastelandutils.WastelandUtils;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MoneyChest
{

    private WastelandUtils main;

    public MoneyChest(WastelandUtils wu)
    {
        this.main = wu;

        main.getCommand("moneyc").setExecutor(new MoneyCCommand(main, this));
    }

    public int getCoins(Inventory inv)
    {
        int count = 0;
        for (ItemStack is : inv.getContents())
        {
            if (is != null)
            {
                if (is.getType() == Material.GOLD_NUGGET)
                {
                    count = count + is.getAmount();
                } else if (is.getType() == Material.GOLD_INGOT)
                {
                    count = count + (is.getAmount() * 9);
                } else if (is.getType() == Material.GOLD_BLOCK)
                {
                    count = count + (is.getAmount() * 81);
                }
            }
        }
        return count;
    }
}

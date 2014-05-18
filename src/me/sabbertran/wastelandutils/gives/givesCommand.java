package me.sabbertran.wastelandutils.gives;

import me.sabbertran.wastelandutils.WastelandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class givesCommand implements CommandExecutor
{

    private WastelandUtils main;

    public givesCommand(WastelandUtils wu)
    {
        this.main = wu;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if (sender.hasPermission("wastelandutils.gives") || sender.isOp())
        {
            Player p = Bukkit.getPlayer(args[0]);
            int id = Integer.parseInt(args[1]);
            ItemStack is = new ItemStack(id);

            for (int i = 2; i < args.length; i++)
            {
                String[] split = args[i].split(":");
                if (split[0].equals("c"))
                {
                    if (is.getItemMeta() instanceof LeatherArmorMeta)
                    {
                        LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();
                        if (split[1].equals("0"))
                        {
                            im.setColor(Color.BLACK);
                        }
                        if (split[1].equals("1"))
                        {
                            im.setColor(Color.BLUE);
                        }
                        if (split[1].equals("2"))
                        {
                            im.setColor(Color.GREEN);
                        }
                        if (split[1].equals("3"))
                        {
                            im.setColor(Color.TEAL);
                        }
                        if (split[1].equals("4"))
                        {
                            im.setColor(Color.MAROON);
                        }
                        if (split[1].equals("5"))
                        {
                            im.setColor(Color.PURPLE);
                        }
                        if (split[1].equals("6"))
                        {
                            im.setColor(Color.ORANGE);
                        }
                        if (split[1].equals("7"))
                        {
                            im.setColor(Color.SILVER);
                        }
                        if (split[1].equals("8"))
                        {
                            im.setColor(Color.GRAY);
                        }
                        if (split[1].equals("9"))
                        {
                            im.setColor(Color.NAVY);
                        }
                        if (split[1].equals("a"))
                        {
                            im.setColor(Color.OLIVE);
                        }
                        if (split[1].equals("b"))
                        {
                            im.setColor(Color.AQUA);
                        }
                        if (split[1].equals("c"))
                        {
                            im.setColor(Color.RED);
                        }
                        if (split[1].equals("d"))
                        {
                            im.setColor(Color.FUCHSIA);
                        }
                        if (split[1].equals("e"))
                        {
                            im.setColor(Color.YELLOW);
                        }
                        if (split[1].equals("f"))
                        {
                            im.setColor(Color.WHITE);
                        }
                        is.setItemMeta(im);
                    } else
                    {
                        sender.sendMessage("Color cannot be applied to this Item");
                    }
                } else if (split[0].equals("e"))
                {
                    for (int j = 1; j < split.length; j++)
                    {
                        String[] ench = split[j].split("-");
                        is.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(ench[0])), Integer.parseInt(ench[1]));
                    }
                }
            }
            p.getInventory().addItem(is);
        }
        return true;
    }
}

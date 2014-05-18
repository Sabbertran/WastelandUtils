package me.sabbertran.wastelandutils.donatorexpressmenu;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.sabbertran.wastelandutils.WastelandUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DonatorExpressMenu implements Listener
{

    private WastelandUtils main;
    private String host;
    private String port;
    private String database;
    private String user;
    private String password;
    private String url;
    private Inventory buyMenu;
    private HashMap<String, String> commands = new HashMap<String, String>();

    public DonatorExpressMenu(WastelandUtils wu)
    {
        this.main = wu;

        this.host = this.main.getDonatorExpressMenuSQL().get(0);
        this.port = this.main.getDonatorExpressMenuSQL().get(1);
        this.database = this.main.getDonatorExpressMenuSQL().get(2);
        this.user = this.main.getDonatorExpressMenuSQL().get(3);
        this.password = this.main.getDonatorExpressMenuSQL().get(4);
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;

        main.getLog().info("[WastelandUtils] DonatorExpressMenu connected");

        createInventory();
        main.getCommand("buy").setExecutor(new BuyCommand(this, main));
    }

    private void createInventory()
    {
        ArrayList<String[]> items = read();

        buyMenu = main.getServer().createInventory(null, 27, "§6§lShop");

        ItemStack temp;
        ItemMeta tempmeta;

        for (String[] item : items)
        {
            temp = new ItemStack(Integer.parseInt(item[1]));
            tempmeta = temp.getItemMeta();
            tempmeta.setDisplayName(item[0]);
            temp.setItemMeta(tempmeta);
            this.commands.put(item[0], item[3]);
            buyMenu.setItem(Integer.parseInt(item[2]), temp);
        }
    }

    public Inventory getBuyMenu()
    {
        return this.buyMenu;
    }

    public ArrayList<String[]> read()
    {
        ArrayList<String[]> tempList = new ArrayList<String[]>();
        try
        {
            FileInputStream fin = new FileInputStream(main.getItemFile());
            InputStreamReader isr = new InputStreamReader(fin);
            BufferedReader read = new BufferedReader(isr);

            String[] temp = null;
            String text;
            while ((text = read.readLine()) != null)
            {
                if (!text.startsWith("#"))
                {
                    if (text.startsWith("- Name: "))
                    {
                        temp = new String[4];
                        temp[0] = text.replace("- Name: ", "");
                    } else if (text.startsWith("  ID: "))
                    {
                        temp[1] = text.replace("  ID: ", "");
                    } else if (text.startsWith("  Position: "))
                    {
                        temp[2] = text.replace("  Position: ", "");
                    } else if (text.startsWith("  Command: "))
                    {
                        temp[3] = text.replace("  Command: ", "");
                        tempList.add(temp);
                    }
                }
            }

        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(DonatorExpressMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(DonatorExpressMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tempList;
    }

    public int getTokens(String name)
    {
        int tokens = 0;
        try
        {
            Statement st = DriverManager.getConnection(url, user, password).createStatement();
            ResultSet rs = st.executeQuery("SELECT tokens FROM dep WHERE username='" + name + "'");
            if (rs.next())
            {
                tokens = rs.getInt("tokens");
            }
            rs.close();
            st.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(DonatorExpressMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tokens;
    }

    public Inventory geBuyMenu()
    {
        return this.buyMenu;
    }

    @EventHandler
    public void InvKlick(InventoryClickEvent ev)
    {
        Player p = (Player) ev.getWhoClicked();

        if (ev.getInventory().getName().equalsIgnoreCase("§6§lShop"))
        {
            if (ev.getCurrentItem() != null && !ev.getCurrentItem().getType().equals(Material.AIR) && !ev.getCurrentItem().getType().equals(Material.NETHER_STAR))
            {
                String name = ev.getCurrentItem().getItemMeta().getDisplayName();
                if (this.commands.containsKey(name))
                {
                    main.getServer().dispatchCommand(p, this.commands.get(name));
                    p.closeInventory();
                }
            }
            ev.setCancelled(true);
        }
    }
}

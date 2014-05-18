package me.sabbertran.wastelandutils.capturepoints;

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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.sabbertran.wastelandutils.WastelandUtils;
import me.sabbertran.wastelandutils.donatorexpressmenu.DonatorExpressMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CapturePoints implements Listener
{

    private WastelandUtils main;
    private ArrayList<CapturePoint> capturePoints;
    private HashMap<Player, CapturePoint> positions;
    private final HashMap<Player, Integer> schedulerIDs;
    private int timeUntilCapture;
    private int chestFillID;

    public CapturePoints(WastelandUtils wu, int captureTime)
    {
        this.main = wu;
        this.capturePoints = readPoints();
        this.positions = new HashMap<Player, CapturePoint>();
        this.schedulerIDs = new HashMap<Player, Integer>();
        this.timeUntilCapture = captureTime;

        main.getCommand("capturepoints").setExecutor(new CapturePointsCommand(main, this));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent ev)
    {
        final Player p = ev.getPlayer();
        boolean atPoint = false;

        for (final CapturePoint point : capturePoints)
        {
            double distance = p.getLocation().distance(point.getLocation());
            if (distance <= point.getRadius())
            {
                atPoint = true;
                if (!positions.containsKey(p))
                {
                    positions.put(p, point);
                    final String playerFaction = getPlayerFaction(p.getName());
                    if (playerFaction != null && !playerFaction.equals(point.getFaction()))
                    {
                        if (point.getPlayersAtPoint().get(playerFaction).size() == 0)
                        {
                            point.getPlayersAtPoint().get(playerFaction).add(p.getName());
                            main.getServer().broadcastMessage(playerFaction + " hat begonnen " + point.getName() + " einzunehmen!");
                            point.setTimeUntilCapture(timeUntilCapture);
                            final int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable()
                            {
                                String factionCapturing = playerFaction;

                                public void run()
                                {
                                    if (point.getMostPlayersFaction().equals(playerFaction))
                                    {
                                        timeUntilCapture = timeUntilCapture - point.getPlayersAtPoint().get(playerFaction).size();

                                        if (timeUntilCapture <= 0)
                                        {
                                            Bukkit.getScheduler().cancelTask(schedulerIDs.get(p));
                                            point.setFaction(point.getMostPlayersFaction());
                                            Bukkit.getServer().broadcastMessage(point.getFaction() + " hat den Punkt " + point.getName() + " eingenommen!");

                                            chestFillID = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable()
                                            {
                                                String fac = playerFaction;

                                                public void run()
                                                {
                                                    if (point.getFaction().equals(fac))
                                                    {
                                                        Chest c = (Chest) point.getLocation().getBlock().getState();
                                                        if (inventoryEmpty(c.getBlockInventory()))
                                                        {
                                                            for (ItemStack is : point.getItems())
                                                            {
                                                                c.getInventory().addItem(is);
                                                            }
                                                        }
                                                    } else
                                                    {
                                                        Bukkit.getScheduler().cancelTask(chestFillID);
                                                    }
                                                }
                                            }, 0L, 72000L);
                                        } else if (timeUntilCapture % 60 == 0)
                                        {
                                            for (Map.Entry<String, ArrayList<String>> entry : point.getPlayersAtPoint().entrySet())
                                            {
                                                for (String p : entry.getValue())
                                                {
                                                    Bukkit.getServer().getPlayer(p).sendMessage("In " + timeUntilCapture / 60 + " Minuten wird der Punkt " + point.getName() + " eingenommen");
                                                }
                                            }
                                        }
                                    }
                                }
                            }, 20L, 20L);
                            schedulerIDs.put(p, id);
                        } else if (playerFaction != null)
                        {
                            point.getPlayersAtPoint().get(playerFaction).add(p.getName());
                        }
                    }
                }
                break;
            }
        }

        if (!atPoint)
        {
            positions.remove(p);
        }
    }

    public ArrayList<CapturePoint> readPoints()
    {
        ArrayList<CapturePoint> points = new ArrayList<CapturePoint>();
        try
        {
            FileInputStream fin = new FileInputStream(main.getCapturePointsFile());
            InputStreamReader isr = new InputStreamReader(fin);
            BufferedReader read = new BufferedReader(isr);

            String name = null;
            Location position = null;
            int radius = -1;
            ArrayList<ItemStack> items = null;
            String text;
            while ((text = read.readLine()) != null)
            {
                if (!text.startsWith("#"))
                {
                    if (text.startsWith("- Name: "))
                    {
                        name = text.replace("- Name: ", "");
                    } else if (text.startsWith("  Position: "))
                    {
                        String pos = text.replace("  Position: ", "");
                        String[] pos_split = pos.split(",");
                        position = new Location(Bukkit.getWorld(pos_split[0]), Integer.parseInt(pos_split[1]), Integer.parseInt(pos_split[2]), Integer.parseInt(pos_split[3]));
                    } else if (text.startsWith("   Radius: "))
                    {
                        radius = Integer.parseInt(text.replace("  Radius: ", ""));
                    } else if (text.startsWith("  Items: "))
                    {
                        items = new ArrayList<ItemStack>();
                        String tempitems = text.replace("  Items: ", "");
                        String[] tempitems_split = tempitems.split(", ");
                        for (String s : tempitems_split)
                        {
                            if (s.startsWith("CS-"))
                            {
                                if (main.getCrackShotAPI() != null)
                                {
                                    String cs_temp = s.replace("CS-", "");
                                    String[] cs_temp_split = cs_temp.split(":");
                                    ItemStack tempitem = main.getCrackShotAPI().generateWeapon(cs_temp_split[0]);
                                    tempitem.setAmount(Integer.parseInt(cs_temp_split[1]));
                                    items.add(tempitem);
                                } else
                                {
                                    main.getLog().info("[CapturePoints] CrackShot is not installed. Item " + s.replace("CS-", "").split(":")[0] + " cannot be rewarded.");
                                }
                            } else
                            {
                                String[] item_split = s.split(":");
                                items.add(new ItemStack(Material.getMaterial(Integer.parseInt(item_split[0])), Integer.parseInt(item_split[1])));
                            }
                        }
                        CapturePoint p = new CapturePoint(name, position, radius, items);
                        points.add(p);
                        name = null;
                        position = null;
                        radius = -1;
                        items = null;
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

        return points;
    }

    private String getPlayerFaction(String name)
    {
        String faction = null;
        try
        {
            String url = "jdbc:mysql://" + main.getCapturePointsSQL().get(0) + ":3306/" + main.getCapturePointsSQL().get(2);
            Statement st = DriverManager.getConnection(url, main.getCapturePointsSQL().get(3), main.getCapturePointsSQL().get(4)).createStatement();
            ResultSet rs1 = st.executeQuery("SELECT tag FROM sc_players where name = '" + name + "'");
            if (rs1.next())
            {
                faction = rs1.getString("tag");
            }
            rs1.close();
            ResultSet rs2 = st.executeQuery("SELECT color_tag FROM sc_clans where tag = '" + faction + "'");
            if (rs2.next())
            {
                faction = rs2.getString("color_tag");
            }
            st.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(CapturePoints.class.getName()).log(Level.SEVERE, null, ex);
        }
        return faction;
    }

    public HashMap<Player, CapturePoint> getPositions()
    {
        return positions;
    }

    private boolean inventoryEmpty(Inventory i)
    {
        for (ItemStack item : i.getContents())
        {
            if (item != null)
            {
                return false;
            }
        }
        return true;
    }
}

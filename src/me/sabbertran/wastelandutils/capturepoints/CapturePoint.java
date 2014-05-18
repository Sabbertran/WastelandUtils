package me.sabbertran.wastelandutils.capturepoints;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class CapturePoint
{

    private String name;
    private Location location;
    private int radius;
    private ArrayList<ItemStack> items;

    private String faction;
    private HashMap<String, ArrayList<String>> playersAtPoint;
    private boolean isTaken;
    private Date captureTime;
    private int timeUntilCapture; //in seconds

    public CapturePoint(String name, Location location, int radius, ArrayList<ItemStack> items)
    {
        this.name = name;
        this.location = location;
        this.radius = radius;
        this.items = items;
        this.isTaken = false;
        this.playersAtPoint = new HashMap<String, ArrayList<String>>();
        this.timeUntilCapture = -1;
    }

    public String getName()
    {
        return name;
    }

    public Location getLocation()
    {
        return location;
    }

    public int getRadius()
    {
        return radius;
    }

    public ArrayList<ItemStack> getItems()
    {
        return items;
    }

    public String getFaction()
    {
        return faction;
    }

    public void setFaction(String faction)
    {
        this.faction = faction;
    }

    public Date getCaptureTime()
    {
        return captureTime;
    }

    public void setCaptureTime(Date capturetime)
    {
        this.captureTime = capturetime;
    }

    public boolean isTaken()
    {
        return this.isTaken;
    }

    public void setTaken(boolean taken)
    {
        this.isTaken = taken;
    }

    public HashMap<String, ArrayList<String>> getPlayersAtPoint()
    {
        return this.playersAtPoint;
    }

    private void setAtPoint(HashMap<String, ArrayList<String>> atPoint)
    {
        this.playersAtPoint = atPoint;
    }

    public String getMostPlayersFaction()
    {
        int mostP = -1;
        String most = null;
        for (Map.Entry<String, ArrayList<String>> entry : playersAtPoint.entrySet())
        {
            if (entry.getValue().size() > mostP)
            {
                most = entry.getKey();
            }
        }
        return most;
    }

    public int getTimeUntilCapture()
    {
        return this.timeUntilCapture;
    }

    public void setTimeUntilCapture(int time)
    {
        this.timeUntilCapture = time;
    }
}

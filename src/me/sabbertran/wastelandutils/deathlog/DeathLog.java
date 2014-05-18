package me.sabbertran.wastelandutils.deathlog;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.sabbertran.wastelandutils.WastelandUtils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathLog implements Listener
{

    private WastelandUtils main;
    private String host;
    private String port;
    private String database;
    private String user;
    private String password;
    private String url;

    public DeathLog(WastelandUtils wu)
    {
        this.main = wu;
        this.main = wu;
        this.host = this.main.getDeathLogSQL().get(0);
        this.port = this.main.getDeathLogSQL().get(1);
        this.database = this.main.getDeathLogSQL().get(2);
        this.user = this.main.getDeathLogSQL().get(3);
        this.password = this.main.getDeathLogSQL().get(4);
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;

        main.getLog().info("[WastelandUtils] DeathLog connected");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent ev)
    {
        Player p = ev.getEntity();
        if (!p.getName().equals("Protector"))
        {
            if (ev.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent)
            {
                EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent) ev.getEntity().getLastDamageCause();
                Player dead = (Player) nEvent.getEntity();
                Entity killer = nEvent.getDamager();

                String way = "none";
                if (killer instanceof Player)
                {
                    Player kill = (Player) killer;
                    if (!kill.getName().equals("Protector"))
                    {
                        way = "player";
                    }
                } else if (killer instanceof Arrow)
                {
                    Arrow ar = (Arrow) killer;
                    Player kill = (Player) ar.getShooter();
                    if (!kill.getName().equals("Protector"))
                    {
                        way = "player";
                    }
                } else if (killer instanceof Zombie)
                {
                    way = "zombie";
                } else
                {
                    way = "natural";
                }

                if (!way.equals("none"))
                {
                    String player = dead.getName();
                    String x = String.valueOf(dead.getLocation().getX());
                    String y = String.valueOf(dead.getLocation().getY());
                    String z = String.valueOf(dead.getLocation().getZ());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    String time = sdf.format(new Date());
                    try
                    {
                        Statement st = DriverManager.getConnection(url, user, password).createStatement();
                        String cmd = "INSERT INTO wh_mc_death (player, x, y, z, time, way) VALUES ('" + player + "', '" + x + "', '" + y + "', '" + z + "', '" + time + "', '" + way + "')";
                        new Thread(new SQLThread(st, cmd)).start();
                    } catch (SQLException ex)
                    {
                        Logger.getLogger(DeathLog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
}

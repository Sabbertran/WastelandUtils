package me.sabbertran.wastelandutils.scoreboardstats;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.sabbertran.wastelandutils.WastelandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreboardStats implements Listener
{

    private WastelandUtils main;
    private String host;
    private String port;
    private String database;
    private String user;
    private String password;
    private String url;
    private ScoreboardManager manager = Bukkit.getScoreboardManager();

    public ScoreboardStats(WastelandUtils wu)
    {
        this.main = wu;
        this.host = this.main.getSBStatsSQL().get(0);
        this.port = this.main.getSBStatsSQL().get(1);
        this.database = this.main.getSBStatsSQL().get(2);
        this.user = this.main.getSBStatsSQL().get(3);
        this.password = this.main.getSBStatsSQL().get(4);
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;
    }

    public void reloadScoreboard(final Player p, final int i)
    {
        main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
        {
            @Override
            public void run()
            {
                int kills = 0;
                int tode = 0;
                int zKills = 0;
                try
                {
                    Statement st = DriverManager.getConnection(url, user, password).createStatement();
                    ResultSet rs = st.executeQuery("SELECT * FROM FalloutStats where name='" + p.getName() + "'");
                    if (rs.next())
                    {
                        kills = rs.getInt("kills");
                        tode = rs.getInt("tode");
                        zKills = rs.getInt("zombiekills");
                    }
                    rs.close();
                    st.close();
                } catch (SQLException ex)
                {
                    Logger.getLogger(ScoreboardStats.class.getName()).log(Level.SEVERE, null, ex);
                }
                double k_d = 0;
                String sK_d = "0";
                if (kills > 0 && tode == 0)
                {
                    k_d = kills;
                } else if (kills > 0 && tode > 0)
                {
                    k_d = kills * 1.0 / tode;
                    sK_d = String.format("%.2f", k_d);
                }


                //Setting up the scoreboard
                Scoreboard board = manager.getNewScoreboard();

                Objective obj = board.registerNewObjective("Stats", "dummy");
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                obj.setDisplayName("Stats");

                Score sKills = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Kills: " + kills));
                sKills.setScore(5);
                Score sTode = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Tode: " + tode));
                sTode.setScore(4);
                Score sKD = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "K/D: " + sK_d));
                sKD.setScore(3);
                Score sZKills = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "ZKills: " + zKills));
                sZKills.setScore(2);
                Score sLink = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + "wh-mc.de/stats"));
                sLink.setScore(1);

                p.setScoreboard(board);
            }
        }, 1L);

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent ev)
    {
        reloadScoreboard(ev.getPlayer(), 0);
    }

    @EventHandler
    public void onKill(PlayerDeathEvent ev)
    {
        if (ev.getEntity().getKiller() instanceof Player)
        {
            final Player killer = ev.getEntity().getKiller();
            final Player dead = ev.getEntity().getPlayer();

            reloadScoreboard(dead, 0);
            reloadScoreboard(killer, 0);
        }
    }

    @EventHandler
    public void ZombieKillEvent(EntityDeathEvent ev)
    {
        if (ev.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent)
        {
            Entity damager = ((EntityDamageByEntityEvent) ev.getEntity().getLastDamageCause()).getDamager();
            if (damager instanceof Player)
            {
                final Player p = (Player) damager;
                if (!p.getName().equals("Protector"))
                {
                    reloadScoreboard(p, 0);
                }
            } else if (damager instanceof Arrow)
            {
                Arrow a = (Arrow) damager;
                Player shooter = (Player) a.getShooter();
                if (!shooter.getName().equals("Protector"))
                {
                    reloadScoreboard(shooter, 0);
                }
            }
        }
    }
}

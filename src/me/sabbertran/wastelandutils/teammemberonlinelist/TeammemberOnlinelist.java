package me.sabbertran.wastelandutils.teammemberonlinelist;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.sabbertran.wastelandutils.WastelandUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TeammemberOnlinelist implements Listener
{

    private WastelandUtils main;
    static final Logger log = Bukkit.getLogger();
    private String host;
    private String port;
    private String database;
    private String user;
    private String password;
    private String url;

    public TeammemberOnlinelist(WastelandUtils wu)
    {
        this.main = wu;

        this.host = this.main.getMemberstatsSQL().get(0);
        this.port = this.main.getMemberstatsSQL().get(1);
        this.database = this.main.getMemberstatsSQL().get(2);
        this.user = this.main.getMemberstatsSQL().get(3);
        this.password = this.main.getMemberstatsSQL().get(4);
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;

        main.getLog().info("[WastelandUtils] TeammemberOnlinelist connected");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent ev)
    {
        Player p = ev.getPlayer();
        try
        {
            Statement st = DriverManager.getConnection(url, user, password).createStatement();
            ResultSet rs = st.executeQuery("SELECT u_name from wh_mc_member where u_name = '" + p.getName() + "'");
            if (rs.next())
            {
                if (main.getMotd().contains("Freebuild"))
                {
                    Statement st1 = DriverManager.getConnection(url, user, password).createStatement();
                    new Thread(new SQLThread(st1, "UPDATE wh_mc_member SET u_fb_online=1 WHERE u_name = '" + p.getName() + "'")).start();
                } else if (main.getMotd().contains("Fallout"))
                {
                    Statement st2 = DriverManager.getConnection(url, user, password).createStatement();
                    new Thread(new SQLThread(st2, "UPDATE wh_mc_member SET u_fo_online=1 WHERE u_name = '" + p.getName() + "'")).start();
                }
            }
            rs.close();
            st.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(TeammemberOnlinelist.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent ev)
    {
        Player p = ev.getPlayer();
        try
        {
            Statement st = DriverManager.getConnection(url, user, password).createStatement();
            ResultSet rs = st.executeQuery("SELECT u_name from wh_mc_member where u_name = '" + p.getName() + "'");
            if (rs.next())
            {
                if (main.getMotd().contains("Freebuild"))
                {
                    Statement st1 = DriverManager.getConnection(url, user, password).createStatement();
                    new Thread(new SQLThread(st1, "UPDATE wh_mc_member SET u_fb_online=0 WHERE u_name = '" + p.getName() + "'")).start();
                } else if (main.getMotd().contains("Fallout"))
                {
                    Statement st2 = DriverManager.getConnection(url, user, password).createStatement();
                    new Thread(new SQLThread(st2, "UPDATE wh_mc_member SET u_fo_online=0 WHERE u_name = '" + p.getName() + "'")).start();
                }
            }
            rs.close();
            st.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(TeammemberOnlinelist.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

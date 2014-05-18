package me.sabbertran.wastelandutils.deathmessageclantags;

import java.sql.DriverManager;
import me.sabbertran.wastelandutils.teammemberonlinelist.*;
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
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathmessageClantags implements Listener
{

    private WastelandUtils main;
    static final Logger log = Bukkit.getLogger();
    private String host;
    private String port;
    private String database;
    private String user;
    private String password;
    private String url;

    public DeathmessageClantags(WastelandUtils wu)
    {
        this.main = wu;

        this.host = this.main.getMessagetagsSQL().get(0);
        this.port = this.main.getMessagetagsSQL().get(1);
        this.database = this.main.getMessagetagsSQL().get(2);
        this.user = this.main.getMessagetagsSQL().get(3);
        this.password = this.main.getMessagetagsSQL().get(4);
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;

        main.getLog().info("[WastelandUtils] DeathmessageClantags connected");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent ev)
    {
        Player death = ev.getEntity().getPlayer();
        Player killer = ev.getEntity().getKiller();

        if (killer != null)
        {

            String killertag = "";
            String deathtag = "";
            try
            {
                Statement st = DriverManager.getConnection(url, user, password).createStatement();
                ResultSet rs1 = st.executeQuery("SELECT tag FROM sc_players where name = '" + killer.getName() + "'");
                if (rs1.next())
                {
                    killertag = rs1.getString("tag");
                }
                rs1.close();
                ResultSet rs2 = st.executeQuery("SELECT color_tag FROM sc_clans where tag = '" + killertag + "'");
                if (rs2.next())
                {
                    killertag = rs2.getString("color_tag");
                }
                rs2.close();
                ResultSet rs3 = st.executeQuery("SELECT tag FROM sc_players where name = '" + death.getName() + "'");
                if (rs3.next())
                {
                    deathtag = rs3.getString("tag");
                }
                rs3.close();
                ResultSet rs4 = st.executeQuery("SELECT color_tag FROM sc_clans where tag = '" + deathtag + "'");
                if (rs4.next())
                {
                    deathtag = rs4.getString("color_tag");
                }
                rs4.close();
                st.close();

            } catch (SQLException ex)
            {
                Logger.getLogger(TeammemberOnlinelist.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

            String newmsg = ev.getDeathMessage();
            if (!killertag.equals(""))
            {
                String newkiller = killer.getName() + "§b [" + killertag + "]§r";
                newmsg = newmsg.replace(killer.getName(), newkiller);
            }
            if (!deathtag.equals(""))
            {
                String newdeath = death.getName() + "§b [" + deathtag + "]§r";
                newmsg = newmsg.replace(death.getName(), newdeath);
            }

            ev.setDeathMessage(newmsg);
        }
    }
}

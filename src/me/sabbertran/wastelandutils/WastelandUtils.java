package me.sabbertran.wastelandutils;

import com.shampaggon.crackshot.CSDirector;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Logger;
import me.sabbertran.wastelandutils.capturepoints.CapturePoints;
import me.sabbertran.wastelandutils.deathlog.DeathLog;
import me.sabbertran.wastelandutils.deathmessageclantags.DeathmessageClantags;
import me.sabbertran.wastelandutils.donatorexpressmenu.DonatorExpressMenu;
import me.sabbertran.wastelandutils.gives.givesCommand;
import me.sabbertran.wastelandutils.lagremover.LagRemover;
import me.sabbertran.wastelandutils.moneychest.MoneyChest;
import me.sabbertran.wastelandutils.playerkillheaddrop.PlayerKillHeadDrop;
import me.sabbertran.wastelandutils.respawnitems.RespawnItems;
import me.sabbertran.wastelandutils.scoreboardstats.ScoreboardStats;
import me.sabbertran.wastelandutils.teammemberonlinelist.TeammemberOnlinelist;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class WastelandUtils extends JavaPlugin implements CommandExecutor
{

    private final Logger log = Bukkit.getLogger();
    private String motd;
    private CSDirector CrackShotAPI;
    private TeammemberOnlinelist memberstats;
    private DeathmessageClantags messagetags;
    private DonatorExpressMenu donatorexpressmenu;
    private PlayerKillHeadDrop playerkillheaddrop;
    private MoneyChest moneychest;
    private LagRemover lagremover;
    private ScoreboardStats sbstats;
    private DeathLog deathlog;
    private RespawnItems respawnitems;
    private givesCommand gives;
    private CapturePoints capturepoints;
    private boolean memberstats_enabled;
    private boolean messagetags_enabled;
    private boolean donatorexpressmenu_enabled;
    private boolean playerkillheaddrop_enabled;
    private boolean moneychest_enabled;
    private boolean lagremover_enabled;
    private boolean sbstats_enabled;
    private boolean deathlog_enabled;
    private boolean respawnitems_enabled;
    private boolean gives_enabled;
    private boolean capturepoints_enabled;
    private ArrayList<String> memberstats_sql;
    private ArrayList<String> messagetags_sql;
    private ArrayList<String> donatorexpressmenu_sql;
    private ArrayList<String> sbstats_sql;
    private ArrayList<String> deathlog_sql;
    private ArrayList<String> capturepoints_sql;
    private ArrayList<String> respawnitems_items;
    private int playerkillheaddrop_percentage;
    private int capturepoints_capturetime;
    private File donatorexpressmenu_itemFile;
    private File capturepoints_pointsFile;

    @Override
    public void onDisable()
    {
        log.info("WastelandUtils disabled!");
    }

    @Override
    public void onEnable()
    {
        this.CrackShotAPI = (CSDirector) Bukkit.getServer().getPluginManager().getPlugin("CrackShot");
        if (CrackShotAPI != null)
        {
            log.info("[CapturePoints] Found CrackShot. You can now reward CrackShot Items using this plugin");
        } else
        {
            log.info("[CapturePoints] Couldn't find CrackShot. Please install it to be able to reward CrackShot Items");
        }

        this.getConfig().addDefault("WastelandUtils.TeamMemberOnlineList.enabled", false);
        this.getConfig().addDefault("WastelandUtils.TeamMemberOnlineList.SQL", new String[]
        {
            "Adress", "Port", "Database", "User", "Password"
        });

        this.getConfig().addDefault("WastelandUtils.DeathMesssageClantags.enabled", false);
        this.getConfig().addDefault("WastelandUtils.DeathMesssageClantags.SQL", new String[]
        {
            "Adress", "Port", "Database", "User", "Password"
        });

        this.getConfig().addDefault("WastelandUtils.DonatorExpressMenu.enabled", false);
        this.getConfig().addDefault("WastelandUtils.DonatorExpressMenu.SQL", new String[]
        {
            "Adress", "Port", "Database", "User", "Password"
        });
        this.getConfig().addDefault("WastelandUtils.PlayerKillHeadDrop.enabled", false);
        this.getConfig().addDefault("WastelandUtils.PlayerKillHeadDrop.Chance", 25);
        this.getConfig().addDefault("WastelandUtils.MoneyChest.enabled", false);
        this.getConfig().addDefault("WastelandUtils.LagRemover.enabled", false);
        this.getConfig().addDefault("WastelandUtils.ScoreboardStats.enabled", false);
        this.getConfig().addDefault("WastelandUtils.ScoreboardStats.SQL", new String[]
        {
            "Adress", "Port", "Database", "User", "Password"
        });
        this.getConfig().addDefault("WastelandUtils.DeathLog.enabled", false);
        this.getConfig().addDefault("WastelandUtils.DeathLog.SQL", new String[]
        {
            "Adress", "Port", "Database", "User", "Password"
        });
        this.getConfig().addDefault("WastelandUtils.RespawnItems.enabled", false);
        this.getConfig().addDefault("WastelandUtils.RespawnItems.items", new String[]
        {
            "391:16"
        });
        this.getConfig().addDefault("WastelandUtils.GiveS.enabled", false);
        this.getConfig().addDefault("WastelandUtils.CapturePoints.enabled", false);
        this.getConfig().addDefault("WastelandUtils.CapturePoints.SQL", new String[]
        {
            "Adress", "Port", "Database", "User", "Password"
        });
        this.getConfig().addDefault("WastelandUtils.CapturePoints.CaptureTime", 3600);

        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        this.memberstats_enabled = this.getConfig().getBoolean("WastelandUtils.TeamMemberOnlineList.enabled");
        this.messagetags_enabled = this.getConfig().getBoolean("WastelandUtils.DeathMesssageClantags.enabled");
        this.playerkillheaddrop_enabled = this.getConfig().getBoolean("WastelandUtils.PlayerKillHeadDrop.enabled");
        this.donatorexpressmenu_enabled = this.getConfig().getBoolean("WastelandUtils.DonatorExpressMenu.enabled");
        this.moneychest_enabled = this.getConfig().getBoolean("WastelandUtils.MoneyChest.enabled");
        this.lagremover_enabled = this.getConfig().getBoolean("WastelandUtils.LagRemover.enabled");
        this.sbstats_enabled = this.getConfig().getBoolean("WastelandUtils.ScoreboardStats.enabled");
        this.deathlog_enabled = this.getConfig().getBoolean("WastelandUtils.DeathLog.enabled");
        this.respawnitems_enabled = this.getConfig().getBoolean("WastelandUtils.RespawnItems.enabled");
        this.gives_enabled = this.getConfig().getBoolean("WastelandUtils.GiveS.enabled");
        this.capturepoints_enabled = this.getConfig().getBoolean("WastelandUtils.CapturePoints.enabled");
        this.memberstats_sql = (ArrayList<String>) this.getConfig().getStringList("WastelandUtils.TeamMemberOnlineList.SQL");
        this.messagetags_sql = (ArrayList<String>) this.getConfig().getStringList("WastelandUtils.DeathMesssageClantags.SQL");
        this.donatorexpressmenu_sql = (ArrayList<String>) this.getConfig().getStringList("WastelandUtils.DonatorExpressMenu.SQL");
        this.sbstats_sql = (ArrayList<String>) this.getConfig().getStringList("WastelandUtils.ScoreboardStats.SQL");
        this.deathlog_sql = (ArrayList<String>) this.getConfig().getStringList("WastelandUtils.DeathLog.SQL");
        this.capturepoints_sql = (ArrayList<String>) this.getConfig().getStringList("WastelandUtils.CapturePoints.SQL");
        this.playerkillheaddrop_percentage = this.getConfig().getInt("WastelandUtils.PlayerKillHeadDrop.Chance");
        this.capturepoints_capturetime = this.getConfig().getInt("WastelandUtils.CapturePoints.CaptureTime");
        this.respawnitems_items = (ArrayList<String>) this.getConfig().getStringList("WastelandUtils.RespawnItems.items");
        this.motd = this.getServer().getMotd();

        donatorexpressmenu_itemFile = new File(getDataFolder(), "donatorexpressmenu.yml");
        capturepoints_pointsFile = new File(getDataFolder(), "capturepoints.yml");
        if (!donatorexpressmenu_itemFile.exists())
        {
            donatorexpressmenu_itemFile.getParentFile().mkdirs();
            copy(getResource("donatorexpressmenu.yml"), donatorexpressmenu_itemFile);
        }
        if (!capturepoints_pointsFile.exists())
        {
            capturepoints_pointsFile.getParentFile().mkdirs();
            copy(getResource("capturepoints.yml"), capturepoints_pointsFile);
        }

        if (this.donatorexpressmenu_enabled)
        {
            donatorexpressmenu_itemFile.getParentFile().mkdirs();
            copy(getResource("donatorexpressmenu.yml"), donatorexpressmenu_itemFile);
        }
        if (this.memberstats_enabled)
        {
            this.memberstats = new TeammemberOnlinelist(this);
            this.getServer().getPluginManager().registerEvents(memberstats, this);
        }
        if (this.messagetags_enabled)
        {
            this.messagetags = new DeathmessageClantags(this);
            this.getServer().getPluginManager().registerEvents(messagetags, this);
        }
        if (this.donatorexpressmenu_enabled)
        {
            this.donatorexpressmenu = new DonatorExpressMenu(this);
            this.getServer().getPluginManager().registerEvents(donatorexpressmenu, this);
        }
        if (this.playerkillheaddrop_enabled)
        {
            this.playerkillheaddrop = new PlayerKillHeadDrop(this);
            this.getServer().getPluginManager().registerEvents(playerkillheaddrop, this);
        }
        if (this.moneychest_enabled)
        {
            this.moneychest = new MoneyChest(this);
        }
        if (this.lagremover_enabled)
        {
            this.lagremover = new LagRemover(this);
        }
        if (this.sbstats_enabled)
        {
            this.sbstats = new ScoreboardStats(this);
            this.getServer().getPluginManager().registerEvents(sbstats, this);
        }
        if (this.deathlog_enabled)
        {
            this.deathlog = new DeathLog(this);
            this.getServer().getPluginManager().registerEvents(deathlog, this);
        }
        if (this.respawnitems_enabled)
        {
            this.respawnitems = new RespawnItems(this, respawnitems_items);
            this.getServer().getPluginManager().registerEvents(respawnitems, this);
        }
        if (this.gives_enabled)
        {
            this.gives = new givesCommand(this);
            getCommand("gives").setExecutor(gives);
        }
        if (this.capturepoints_enabled)
        {
            this.capturepoints = new CapturePoints(this, capturepoints_capturetime);
        }

        log.info("WastelandUtils enabled");
    }

    public Logger getLog()
    {
        return this.log;
    }

    public CSDirector getCrackShotAPI()
    {
        return CrackShotAPI;
    }

    public String getMotd()
    {
        return this.motd;
    }

    public ArrayList<String> getMemberstatsSQL()
    {
        return this.memberstats_sql;
    }

    public ArrayList<String> getMessagetagsSQL()
    {
        return this.messagetags_sql;
    }

    public ArrayList<String> getDonatorExpressMenuSQL()
    {
        return this.donatorexpressmenu_sql;
    }

    public ArrayList<String> getSBStatsSQL()
    {
        return this.sbstats_sql;
    }

    public ArrayList<String> getDeathLogSQL()
    {
        return this.deathlog_sql;
    }

    public ArrayList<String> getCapturePointsSQL()
    {
        return this.capturepoints_sql;
    }

    public File getItemFile()
    {
        return this.donatorexpressmenu_itemFile;
    }

    public File getCapturePointsFile()
    {
        return this.capturepoints_pointsFile;
    }

    public int getPlayerHeadDropChance()
    {
        return this.playerkillheaddrop_percentage;
    }

    private void copy(InputStream in, File file)
    {
        try
        {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0)
            {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

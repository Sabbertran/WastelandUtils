package me.sabbertran.wastelandutils.capturepoints;

import java.util.Map;
import me.sabbertran.wastelandutils.WastelandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CapturePointsCommand implements CommandExecutor
{

    private WastelandUtils main;
    private CapturePoints cp;

    public CapturePointsCommand(WastelandUtils wu, CapturePoints cp)
    {
        this.main = wu;
        this.cp = cp;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if (args[0].equalsIgnoreCase("players"))
        {
            if (cp.getPositions().isEmpty())
            {
                sender.sendMessage("[CapturePoints] No Player is in range of a capturepoint.");
                return true;
            } else
            {
                sender.sendMessage("[CapturePoints] The following players are in range of a capturepoint");
                for (Map.Entry<Player, CapturePoint> entry : cp.getPositions().entrySet())
                {
                    sender.sendMessage("[CapturePoints] " + entry.getKey().getName() + " is in range of " + entry.getValue().getName());
                }
                return true;
            }
        }
        return true;
    }
}

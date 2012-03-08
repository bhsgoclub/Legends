package ShadovvMoon.Legend;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LGBolt implements Runnable
{

    public Legends plugin;
    public Location loc;
    public Player player;
    public LGBolt(Legends p, Location l, Player pl)
    {
        this.plugin = p;
        this.loc = l;
        this.player = pl;
    }
    
    @Override
    public void run()
    {
    	this.player.getWorld().strikeLightning(this.loc);
    }
}

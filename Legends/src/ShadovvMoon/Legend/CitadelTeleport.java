package ShadovvMoon.Legend;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.*;

public class CitadelTeleport implements Runnable
{

    public Legends plugin;
    public Player player;
    public CitadelTeleport(Legends p, Player t)
    {
        this.plugin = p;
        this.player = t;
    }
    
    @Override
    public void run()
    {
        //do your stuff here
    	this.player.teleport(new Location(this.player.getWorld(), 66, 80, 263));
    	
    	plugin.playerConfig(player).setProperty("lastCitadelTiming", String.valueOf(System.currentTimeMillis()));

    }
}

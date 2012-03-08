package ShadovvMoon.Legend;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftExperienceOrb;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.*;

public class LGZapper implements Runnable
{

    public Legends plugin;
    public Block ee;
    public LGPlayerListener sender;
    public Player pe;
    public LGZapper(Legends p, Block e, LGPlayerListener sender, Player pe)
    {
        this.plugin = p;
        this.ee = e;
        this.sender = sender;
        this.pe = pe;
    }
    
    @Override
    public void run()
    {
    	this.sender.zapBlock(this.ee, this.pe, 20);
    }
}

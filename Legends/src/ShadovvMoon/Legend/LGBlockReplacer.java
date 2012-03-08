package ShadovvMoon.Legend;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftExperienceOrb;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.*;

public class LGBlockReplacer implements Runnable
{

    public Legends plugin;
    public Material mat;
    public Block block;
    public LGBlockReplacer(Legends p, Block b)
    {
        this.plugin = p;
        this.block = b;
        this.mat = b.getType();
    }
    
    @Override
    public void run()
    {
    	this.block.setType(this.mat);
    }
}

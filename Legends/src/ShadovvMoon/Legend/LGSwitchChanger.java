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
import org.bukkit.material.Button;
import org.bukkit.scheduler.*;

public class LGSwitchChanger implements Runnable
{

    public Legends plugin;
    public Material mat;
    public Block block;
    public LGSwitchChanger(Legends p, Block b)
    {
        this.plugin = p;
        this.block = b;
        this.mat = b.getType();
    }
    
    @Override
    public void run()
    {

        byte data = this.block.getData();
        this.block.setData((byte) (data & ~0x8));
        

    }
}

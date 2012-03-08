package ShadovvMoon.Legend;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.*;

public class LGRegen implements Runnable
{

	public Block block;
    public Legends plugin;
    public Material m;
    public LGRegen(Block tochange, Material g, Legends p)
    {
        this.block = tochange;
        this.plugin = p;
        this.m = g;
    }
    
    @Override
    public void run()
    {
        //do your stuff here
    	this.block.setType(this.m);
    	
    	//this.player.setExperience(1);
    	//this.player.setLevel(1);
    }
}

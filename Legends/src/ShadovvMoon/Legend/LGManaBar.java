package ShadovvMoon.Legend;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.*;

public class LGManaBar implements Runnable
{

	public Player player;
    public Legends plugin;
    public LGManaBar(Player tochange, Legends p)
    {
        this.player = tochange;
        this.plugin = p;
    }
    
    @Override
    public void run()
    {
        //do your stuff here
    	
    	if (!this.player.isSprinting())
    		this.plugin.gainMana(this.player, 2);
    	
    	//this.player.setExperience(1);
    	//this.player.setLevel(1);
    }
}

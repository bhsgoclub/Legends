package ShadovvMoon.Legend;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftExperienceOrb;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class LGAutoSave implements Runnable
{

    public Legends plugin;
    public LGAutoSave(Legends p)
    {
        this.plugin = p;
    }
    
    @Override
    public void run()
    {
        //do your stuff here
    	this.plugin.getCon().save();
    	this.plugin.getSettle().save();
    	this.plugin.getTelepad().save();
    	this.plugin.getXrayers().save();
    	this.plugin.save();
    	
    	Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+"Saving worlds... Please wait");
    	List <World> world = Bukkit.getServer().getWorlds();
    	
    	
    	
    	int w;
    	for (w=0; w < world.size(); w++)
		{
    		world.get(w).save();
    		
    		List <Player> players =world.get(w).getPlayers();
    		for (Player player: players)
    		{
    			player.saveData();
    			this.plugin.saveData(player.getName());
    		}
		 }
    	
    	Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+"Save complete!");
    	//Bukkit.getServer().broadcastMessage(ChatColor.RED+"[Protip]"+ChatColor.YELLOW+" Post on minecraftforum and more players will join the server! Go to http://tinyURL.com/SkyCr. You may receive a free golden apple from ShadovvMoon for your troubles!");
    }
}

package ShadovvMoon.Legend;


import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftCreeper;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.entity.CraftSpider;
import org.bukkit.craftbukkit.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.*;
import org.bukkit.util.Vector;

public class LGGuard implements Runnable
{

	public Player player;
    public Legends plugin;
    public Monster ai_player;
    public int state;
    
    public LGGuard(Player tochange, Legends p, Monster ai)
    {
        this.player = tochange;
        this.plugin = p;
        this.ai_player = ai;
    }
    
    @Override
    public void run()
    {
        //do your stuff here
    	
    	if (this.ai_player.isDead())
    	{
    		return;
    	}

    	if (this.player.isDead())
    	{
    		this.ai_player.setHealth(0);
    		this.ai_player.damage(1000);
    	}
    	else
    	{
    		this.ai_player.setTarget(this.player);
    	}
 	
    	//Who hurt master? D:
    	
    	
    	

    	
    }
}

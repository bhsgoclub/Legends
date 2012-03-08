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

public class LGAIZombie implements Runnable
{

	public Player player;
    public Legends plugin;
    public Monster ai_player;
    public int state;
    
    public LGAIZombie(Player tochange, Legends p, Monster ai)
    {
        this.player = tochange;
        this.plugin = p;
        this.ai_player = ai;
    }
    
    public void idle()
    {
    	ai_player.setTarget(null);

    	List<Entity>  li = ai_player.getWorld().getEntities();
    	for (Entity e : li)
    	{
    		if (e instanceof LivingEntity)
    		{
    			if (e.getLocation().distance(ai_player.getLocation())>100)
    			{
    				ai_player.setTarget((LivingEntity) e);
    				return;
    			}
    		}
    	}
    	
    	
    }
    
    @Override
    public void run()
    {
        //do your stuff here
    	
    	if (this.ai_player.isDead())
    	{
    		return;
    	}

    	this.state = plugin.playerConfig(player).getInt("PetState", 1);
    	if (this.state == 1)
    	{
    		//Following master
    		ai_player.setTarget((LivingEntity) player);
    		//ai_player.setAngry(false);
    		
    		if (this.ai_player.getLocation().distance(player.getLocation())<5)
    		{
    			//Stop
    			//System.out.println("following player");
    			
    			//ai_player.setAngry(false);
    			idle();
    			
    			
    		}
    		CraftPlayer p = (CraftPlayer)player;
    		//p.set
    		
    		
    		
    		
    		boolean attacking_somebody = false;
    		
    		EntityDamageEvent ev = player.getLastDamageCause();
    		if (ev instanceof EntityDamageByEntityEvent)
    		{
	    		EntityDamageByEntityEvent  e = (EntityDamageByEntityEvent) ev;
	    		Entity last_dmg = e.getDamager();
	    		if (last_dmg != null)
	    		{
	    			if (last_dmg instanceof LivingEntity)
	    			{
			    		if (!last_dmg.isDead() && (last_dmg.getLocation().distance(ai_player.getLocation()) < 6)&&last_dmg.getEntityId() != player.getEntityId())
			    		{
			    			//Check distance between player
			    			if ( (ai_player.getLocation().distance(player.getLocation()) < 10))
			    			{
			    				if (last_dmg.getEntityId() == ai_player.getEntityId())
			    					return;
			    				
			    				attacking_somebody=true;
				    			ai_player.setTarget((LivingEntity) last_dmg);
			    			}
			    		}
	    			}
	    		}
    		}
    		
    		if (!attacking_somebody)
    		{
    			if (p.getWorld().getEntities().contains(plugin.playerConfig(player).getInt("Target", -1)))
        		{
    				Entity last_dmg = p.getWorld().getEntities().get(plugin.playerConfig(player).getInt("Target", -1));
    				if (last_dmg != null)
    	    		{
    	    			if (last_dmg instanceof LivingEntity)
    	    			{
    			    		if (!last_dmg.isDead() && (last_dmg.getLocation().distance(ai_player.getLocation()) < 6)&&last_dmg.getEntityId() != player.getEntityId())
    			    		{
    			    			if (last_dmg.getEntityId() == ai_player.getEntityId())
			    					return;
    			    			
    			    			ai_player.setTarget((LivingEntity) last_dmg);
    			    		}
    	    			}
    	    		}
        		}
    		}
    	}
    	else if (this.state == 2)
    	{
    		//Somebody hit me D:
    		EntityDamageByEntityEvent  e = (EntityDamageByEntityEvent) ai_player.getLastDamageCause();
    		Entity last_dmg = e.getEntity();
    		if (last_dmg != null)
    		{
    			if (last_dmg instanceof LivingEntity)
    			{
    			
		    		if (last_dmg.getEntityId() == this.player.getEntityId())
		    		{
		    			//That's ok
		    			System.out.println("player hit me :(");
		    			
		    			this.state = 1;
		    			idle();
		    		}
		    		else
		    		{
		    			if (last_dmg.isDead())
		    			{
		    				this.state=1;
		    				
		    				idle();
		    				System.out.println("Switching to following");
		    			}
		    			else
		    			{
		    				System.out.println("KILL");
		    				//ai_player.setAngry(true);
		    				ai_player.setTarget((LivingEntity) last_dmg);
		    			}
		    		}
    		
    			}
    		}
    		else
    		{
    			this.state=1;
				ai_player.setTarget(null);
				System.out.println("Switching to following");
    		}
    			
    	}
    	plugin.playerConfig(player).setProperty("PetState", this.state);
 	
    	//Who hurt master? D:
    	
    	/*
    	LivingEntity last_dmg = (LivingEntity) ai_player.getLastDamageCause();
    	if ((last_dmg instanceof CraftZombie||last_dmg instanceof CraftSpider||last_dmg instanceof CraftCreeper||last_dmg instanceof CraftPlayer))
		{
    		ai_player.setTarget((LivingEntity) last_dmg);
    		return;
    	}
    	
    	//Find the nearest target
    	int dist = 3000;
    	Entity closest = null;
    	
    	for (Entity entity : ai_player.getNearbyEntities(30, 30, 30))
    	{
    		if (entity.getEntityId() == player.getEntityId())
				continue;
    		if (!(entity instanceof LivingEntity))
    			continue;
    		
    		int new_dist = (int) ai_player.getLocation().distance(entity.getLocation());
    		if (new_dist < dist)
    		{
    			dist = new_dist;
    			closest = entity;
    		}
    	}
    	
    	
    	boolean has_target = false;
    	if (closest != null)
    	{
    		//System.out.println(entity.getClass().getName());
    		ai_player.setTarget((LivingEntity) closest);
    		has_target = true;
    	}
   */
    	
    	/*

    	if (!has_target)
    	{
    		ai_player.setTarget((LivingEntity) ai_player.getWorld().getEntities().get(0));
    	}
    	*/
    	
    	
    	

    	
    }
}

package ShadovvMoon.Legend;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.server.Container;
import net.minecraft.server.ContainerChest;
import net.minecraft.server.ContainerPlayer;
import net.minecraft.server.EntityFireball;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EntityWolf;
import net.minecraft.server.InventoryLargeChest;
import net.minecraft.server.MobEffect;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet20NamedEntitySpawn;
import net.minecraft.server.TileEntityChest;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.inventory.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInventoryEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;


import org.bukkit.entity.*;


import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.*;

import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.config.Configuration;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import java.util.Timer;

import javax.xml.crypto.Data;

public class LGPlayerListener extends PlayerListener 
{
	 private final Legends plugin;

	    public LGPlayerListener(Legends instance) {
	        plugin = instance;
	    }

	    @Override
	    public void onPlayerJoin(PlayerJoinEvent event)
	    {
	    	
			List<String> str = plugin.getSettle().getKeys("Bans");
			if (str != null)
			{
				int i;
				for (i=0; i < str.size(); i++)
				{
					if (str.get(i).equalsIgnoreCase(event.getPlayer().getName()))
					{
						Bukkit.getServer().broadcastMessage(ChatColor.RED+event.getPlayer().getName()+" was auto-kicked for being banned.");
						event.getPlayer().kickPlayer("being banned. Contact Bhsgoclub on the forum to request an unban.");
					}
				}

			}
			
	    	
	    	LGManaBar mana_bar = new LGManaBar(event.getPlayer(), plugin);
	    	int timerID = event.getPlayer().getServer().getScheduler().scheduleSyncRepeatingTask(plugin, mana_bar, 50, 50);
	    	plugin.playerConfig(event.getPlayer()).setProperty("manaTimer", timerID);
	    			
	    	LGSprinter sprint = new LGSprinter(event.getPlayer(), plugin);
	    	
	    	int staminaBoosts = plugin.playerConfig(event.getPlayer()).getInt("staminaBoost", 0);
			staminaBoosts++;
			
			int stam = plugin.skillLevel(event.getPlayer(), "Sprint");
			int boost = stam*3;
			
			timerID = event.getPlayer().getServer().getScheduler().scheduleSyncRepeatingTask(plugin, sprint, 10*staminaBoosts+boost, 10*staminaBoosts+boost);
			plugin.playerConfig(event.getPlayer()).setProperty( "sprintTimer", timerID);
			
			
	    	Player p = event.getPlayer();
	    	
	    	plugin.loadPlayerData(p.getName());
	    	
	    	p.sendMessage(ChatColor.YELLOW+"Loaded save file for " + p.getName());
	    	
	    	String addr = p.getAddress().toString();
	    	addr = addr.split(":")[0];
	    	
	    	
	    	plugin.playerConfig(p.getName()).setProperty("IP", addr);
	    	
	    	//Look through the online players
	    	Player[] pa= Bukkit.getServer().getOnlinePlayers();
	    	String peeps = "";
	    	
	    	
	  
	    	plugin.playerConfig(p).setProperty("is_sneaking", "false");
	    	p.setSneaking(false);
	    	
	    	 System.out.println(event.getPlayer().getName() + " joined the server");
	    	 
	    	if (!(plugin.playerConfig(p).getString("first_join_new7", "").equalsIgnoreCase("complete")))
	    	{
	    		p.teleport(new Location(p.getWorld(), 395, 11, -356));
	    		
	    		p.sendMessage(ChatColor.YELLOW+"Welcome to SkyCraft.");
	    		plugin.playerConfig(p).setProperty("first_join_new7", "complete");
	    		
	    		plugin.playerConfig(p).setProperty("archery_task",
						"started");
				plugin.playerConfig(p).setProperty("magic_task", "started");
				plugin.playerConfig(p).setProperty("melee_task", "started");
				
	    	}
	    	
	    	if (!(plugin.playerConfig(p).getString("first_join", "").equalsIgnoreCase("complete")))
	    	{
	    		//Tutorial incomplete.
	    		
	    		plugin.playerConfig(p).setProperty("nomad", "true");
	    		plugin.playerConfig(p).setProperty("first_join", "complete");
	    		
	    		System.out.println("New player" + plugin.playerName(p) + "with config " + plugin.playerConfig(p).getString("first_join", ""));
	    		
	    		Bukkit.getServer().broadcastMessage(ChatColor.WHITE+"[o] "+ChatColor.GREEN+"[h][0:0:0] Sarah"+ChatColor.WHITE+": Welcome to SkyCraft " + p.getName() +"!" );
				
	    	}

	    	plugin.setPermissions(event.getPlayer());
	       
	        
	        
	        System.out.println(addr);
	    	
	    	int i;
	    	for (i=0; i < pa.length; i++)
	    	{
	    		if (pa[i] == p)
	    			continue;
	    		
	    		String addr2 = pa[i].getAddress().toString();
	    		addr2 = addr2.split(":")[0];
	    		if (addr2.equalsIgnoreCase(addr))
	    		{
	    			if (peeps.equalsIgnoreCase(""))
	    				peeps = pa[i].getName();
	    			else peeps = peeps + ", " + pa[i].getName();
	    					
	    			
	    		}
	    	}
	    	
	    	if (!peeps.equalsIgnoreCase(""))
	    		Bukkit.broadcastMessage(ChatColor.YELLOW+p.getName()+" is also known as " + peeps);
	    	
	    	
	        
	        String sneak = plugin.playerConfig(p).getString("lightningLogin", "");
    		if (sneak.equalsIgnoreCase("true"))
    		{
    			p.getWorld().strikeLightningEffect(p.getLocation());
    		}
    		
    		WorldGuardPlugin worldGuard = getWorldGuard();
    		RegionManager regionManager = worldGuard.getRegionManager(p.getWorld());
    		
    		Location location = p.getLocation();

    		com.sk89q.worldedit.Vector v = new com.sk89q.worldedit.Vector(location.getX(), location.getY(), location.getZ());
      		
    		
	          //PLAY MUSIC?
	          ProtectedRegion region2 = worldGuard.getRegionManager(p.getWorld()).getRegion("city");
	          if (region2!=null)
	          {
	    		if (region2.contains(v))
	    		{

	    				//PLAY THE MUSIC :D
	    				p.playEffect(new Location(p.getWorld(), 9,63,139), Effect.RECORD_PLAY, 2264);
		
	    		}
	          }
	    		
	    	/*p.sendMessage(ChatColor.GOLD+"NOTICE: We will be doing a map reset at the end of the week.");
	    		p.sendMessage(ChatColor.GOLD+"Factions will be removed we will have set factions each with a perk");
	    		p.sendMessage(ChatColor.GOLD+"Guides if you do not do your job");
	    		p.sendMessage(ChatColor.GOLD+"You WILL BE REMOVED. being a guide is");
	    		p.sendMessage(ChatColor.GOLD+"a honer not just something for fun.");
	    		*/
	    }
	    
		 @Override
		 public void onPlayerRespawn(PlayerRespawnEvent event)
		 {
			// Player p = (Player)event.getPlayer();
			 
			 //int health_level = plugin.skillLevel(p, "Health");
			 //p.setHealth((int)(20/(20/(20+health_level*5))));
		 }

	    @Override
	    public void onPlayerQuit(PlayerQuitEvent event)
	    {
	    	int timerID = plugin.playerConfig(event.getPlayer()).getInt("manaTimer", 0);
	    	event.getPlayer().getServer().getScheduler().cancelTask(timerID);
	    	
	    	int ti2merID = plugin.playerConfig(event.getPlayer()).getInt("sprintTimer", 0);
	    	event.getPlayer().getServer().getScheduler().cancelTask(ti2merID);
	    	
	    	
	    	plugin.playerClose(event.getPlayer().getName());
	    	
	    	//Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+event.getPlayer().getName()+" left skycraft.");
	        
	    }

	    public void sendGlobalBlockChange(Location loc, Material type, byte dat)
	    {
	    	 for (Player player : Bukkit.getServer().getOnlinePlayers())
	         {
	    		 player.sendBlockChange(loc, type, dat);
	         }
	    }

	    @Override
	    public void onPlayerMove(PlayerMoveEvent event)
	    {
	    	
	          Player player = event.getPlayer();
	          
	          //if (player.getName().equalsIgnoreCase("bhsgoclub"))
	        //	  event.setCancelled(true);
	          
	      	WorldGuardPlugin worldGuard = getWorldGuard();
    		RegionManager regionManager = worldGuard.getRegionManager(player.getWorld());
    		
    		Location location = event.getTo();

    		com.sk89q.worldedit.Vector v = new com.sk89q.worldedit.Vector(location.getX(), location.getY(), location.getZ());
    		
    		Location location2 = event.getFrom();

    		com.sk89q.worldedit.Vector v2 = new com.sk89q.worldedit.Vector(location2.getX(), location2.getY(), location2.getZ());
    		
    		
	          //PLAY MUSIC?
	          ProtectedRegion region2 = worldGuard.getRegionManager(player.getWorld()).getRegion("city");
	          if (region2!=null)
	          {
	    		if (region2.contains(v))
	    		{
	    			if (!region2.contains(v2))
		    		{
	    				//PLAY THE MUSIC :D
	    				player.playEffect(new Location(player.getWorld(), 9,63,139), Effect.RECORD_PLAY, 2264);
		    		}
	    		}
	          }
	          
	          if (player.getInventory().getChestplate().getType()==Material.CHAINMAIL_CHESTPLATE)
			  {
	        	  plugin.effectPlayer(player, "regen", 1, 2);
			  }
	          
	          if (player.getInventory().getHelmet().getType() == Material.CHAINMAIL_HELMET)
	          {
	          	//Woo breathe underwter!
	        	  plugin.effectPlayer(player, "waterbreath", 1, 2);
	          }

	          String binoculars = plugin.playerConfig(player).getString("Binoculars", "");
	        	if (!(binoculars.equalsIgnoreCase("true")))
	        	{	  
	          if (player.getInventory().getBoots().getType()==Material.CHAINMAIL_BOOTS)
			    {
	        	  	plugin.effectPlayer(player, "speedup", 4, 1);
	        	  	
	        	  	if (event.getFrom().distance(event.getTo())>0.1)
	        		  {
		        	  	short dur = player.getInventory().getBoots().getDurability();
			        	  
			        	  if (dur > 500)
			        		  player.getInventory().setBoots(null);
			        	  else if (!(player.getInventory().getLeggings().getType()==Material.CHAINMAIL_LEGGINGS))
			        		  player.getInventory().getBoots().setDurability((short) (dur+1));
			        	  
	        		  }
		        	  
			    	//player.setSprinting(true);
	        	//  player.sendMessage("PegasusBoots");
	        	  
	        	  /*
	        	  int boots = Integer.valueOf(plugin.playerConfig(player).getString("pegasusBoots_MOVED", "0"));
	        	  
	        	  if (boots <= 0)
	        	  {
	        		  if (event.getFrom().distance(event.getTo())>0.3)
	        		  {
	        			  //if (plugin.gainMana(player, -5))
	        			  //{
	        			  plugin.playerConfig(player).setProperty("pegasusBoots_MOVED", "1");
		
				        	  org.bukkit.util.Vector eye=player.getEyeLocation().getDirection();
				        	  eye.normalize();
				        	  eye.setY(0);
				        	 
				        	  player.setVelocity(eye);
	        			 // }
				        	  
				        	  short dur = player.getInventory().getBoots().getDurability();
				        	  
				        	  if (dur > 300)
				        		  player.getInventory().setBoots(null);
				        	  else
				        	  player.getInventory().getBoots().setDurability((short) (dur+1));

	        		  }
	        	  }
	        	  else
	        	  {
	        		  boots--;
	        		  plugin.playerConfig(player).setProperty("pegasusBoots_MOVED", String.valueOf(boots));
	        	  }
	        	  
	        	  */
	        	  
			    }
	          else if (player.getInventory().getLeggings().getType()==Material.CHAINMAIL_LEGGINGS)
			    {
	        	  plugin.effectPlayer(player, "speedup", 100, 1);
	        	  	
	        	  	if (event.getFrom().distance(event.getTo())>0.1)
	        		  {
	        	  		
	        	  		int boots = Integer.valueOf(plugin.playerConfig(player).getString("pegasusLEGS_MOVED", "21"));

	        	  		if (boots <= 0)
	  	        	  {
		        	  	short dur = player.getInventory().getLeggings().getDurability();
			        	  
			        	  if (dur > 100)
			        		  player.getInventory().setLeggings(null);
			        	  else
			        	  player.getInventory().getLeggings().setDurability((short) (dur+1));
			        	  
	  	        	  }
	        	  		
			        	  boots--;
		        		  plugin.playerConfig(player).setProperty("pegasusLEGS_MOVED", String.valueOf(boots));
	        		  }
			    }
	        	}
	          
	          if (player.getItemInHand().getType() == Material.TORCH||player.getInventory().getHelmet().getType() == Material.CHAINMAIL_HELMET)
	          {
		          Block at_foot =  player.getWorld().getBlockAt(event.getTo()).getRelative(0, -1, 0);
		          Block at_foot_old =  player.getWorld().getBlockAt(event.getFrom()).getRelative(0, -1, 0);
	
		          if (at_foot.getLocation().distance(at_foot_old.getLocation())>=0.5)
		          {
		        	  if (at_foot.getType() != Material.AIR)
		        	  {
		        		  
		        		 // ((CraftWorld)player.getWorld()).getBlockAt(at_foot.getLocation()).
		        		  byte d = at_foot.getData();
		        		  
		        		  sendGlobalBlockChange(at_foot.getLocation(), Material.GLOWSTONE, at_foot.getData());
		        	  }
		        	  sendGlobalBlockChange(at_foot_old.getLocation(), at_foot_old.getType(), at_foot_old.getData());
		          }
	          }
	          


		    long current_time = System.currentTimeMillis();
		    long previous_time = Long.valueOf(plugin.playerConfig(player).getString("frozenTime", "0"));
		    		
		    if (!plugin.playerConfig(player).getString("frozenTime", "0").equalsIgnoreCase("0"))
		    {
		    	long time_since = current_time-previous_time;
			    if (time_since < 1000)
				{
			    	Block at_foot =  player.getWorld().getBlockAt(event.getTo()).getRelative(0, -1, 0);
			          Block at_foot_old =  player.getWorld().getBlockAt(event.getFrom()).getRelative(0, -1, 0);
		
			    	if (at_foot.getLocation().distance(at_foot_old.getLocation())>=1)
			         {
			    		
			    		event.setCancelled(true);
			         }
			    	
			    	
				}
		    }

		    if (plugin.playerConfig(player).getString("teleporting", "false").equalsIgnoreCase("true"))
		    {
		    	previous_time = Long.valueOf(plugin.playerConfig(player).getString("lastTeleportTiming", "0"));
			    
		    	long time_since = current_time-previous_time;
		    	if (time_since < 10000)
				{
		    		plugin.playerConfig(player).setProperty("teleporting", "false");
		    		
		    		player.sendMessage(ChatColor.YELLOW+"Teleport cancelled.");
		    		Bukkit.getServer().getScheduler().cancelTask(plugin.playerConfig(player).getInt("teleportSchedule", 0));
				}
		    }
		    
		    if (plugin.playerConfig(player).getString("citadel", "false").equalsIgnoreCase("true"))
		    {
		    	previous_time = Long.valueOf(plugin.playerConfig(player).getString("lastCitadelTiming", "0"));
			    
		    	long time_since = current_time-previous_time;
		    	if (time_since < 10000)
				{
		    		plugin.playerConfig(player).setProperty("citadel", "false");
		    		
		    		player.sendMessage(ChatColor.YELLOW+"Teleport cancelled.");
		    		Bukkit.getServer().getScheduler().cancelTask(plugin.playerConfig(player).getInt("citadelSchedule", 0));
				}
		    }
		    
		    /*Player player = (Player) sender;
			
			long sneak = Long.valueOf(plugin.getConfig().getString(player.getName()+".lastTeleportTime", "0"));
    		long time = System.currentTimeMillis();
    		
    		long since = time-sneak;
    		if (since>(1800000))
    		{
    			player.sendMessage(ChatColor.YELLOW+"Teleporting... Don't move for 10 seconds");
    			plugin.getConfig().setProperty(player.getName()+".teleporting", "true");
    		}
    		else
    		{
    			long minutes = (since /1000/60);
    			
    			player.sendMessage(ChatColor.RED+"You cannot teleport for another "+String.valueOf(minutes)+" minutes.");
    		}*/
		    
		    Block at_foot =  player.getWorld().getBlockAt(event.getTo()).getRelative(0, -1, 0);
	        
		    if (at_foot.getTypeId()==21)
		    {
		    	plugin.effectPlayer(player, "speedup", 50, 1);
		    }
		    if (at_foot.getTypeId()==74)
		    {
		    	plugin.effectPlayer(player, "confusion", 50, 50);
		    }
		    if (at_foot.getTypeId()==103)
		    {
		    	plugin.effectPlayer(player, "heal", 50, 10);
		    }
		    if (at_foot.getTypeId()==37)
		    {
		    	plugin.effectPlayer(player, "confusion", 50, 50);
		    }
		    if (at_foot.getTypeId()==38)
		    {
		    	plugin.effectPlayer(player, "poison", 50, 20);
		    }
		    if (at_foot.getTypeId()==80)
		    {
		    	plugin.effectPlayer(player, "jump", 50, 1);
		    }
		   
		   /* if (at_foot.getTypeId()==88)
		    {
		    	plugin.effectPlayer(player, "weakness", 50, 10);
		    }
		    if (at_foot.getTypeId()==88)
		    {
		    	plugin.effectPlayer(player, "hunger", 50, 10);
		    }
		    if (at_foot.getTypeId()==91)
		    {
		    	plugin.effectPlayer(player, "blindness", 50, 20);
		    }
		    if (at_foot.getTypeId()==9)
		    {
		    	plugin.effectPlayer(player, "poison", 50, 20);
		    }
		    if (at_foot.getTypeId()==0)
		    {
		    	plugin.effectPlayer(player, "blindness", 50, 20);
		    }
		    if (at_foot.getTypeId()==12)
		    {
		    	plugin.effectPlayer(player, "hunger", 50, 20);
		    }
		    if (at_foot.getTypeId()==1)
		    {
		    	plugin.effectPlayer(player, "digSlowDown", 50, 20);
		    }
		    if (at_foot.getTypeId()==4)
		    {
		    	plugin.effectPlayer(player, "confusion", 50, 20);
		    }
		    if (at_foot.getTypeId()==2)
		    {
		    	plugin.effectPlayer(player, "jump", 50, 20);
		    }
		    if (at_foot.getTypeId()==3)
		    {
		    	plugin.effectPlayer(player, "damageBoost", 50, 20);
		    }
		    if (at_foot.getTypeId()==5)
		    {
		    	plugin.effectPlayer(player, "heal", 50, 20);
		    }
		    if (at_foot.getTypeId()==17)
		    {
		    	plugin.effectPlayer(player, "confusion", 50, 1);
		    }
		    if (at_foot.getTypeId()==87)
		    {
		    	plugin.effectPlayer(player, "speeddown", 50, 20);
		    }*/
	    }

	    
	    public void cutDownTree(Block main_block, Player p, int transverse)
	    {
	    	if (transverse == 0)
	    		return;
	    	
	    	if (main_block.getType() != Material.LOG && main_block.getType() != Material.LEAVES)
	    	{
	    		if (main_block.getType() == Material.DIRT)
	    			main_block.getRelative(0, 1, 0).setType(Material.SAPLING);
	    		return;
	    	}
	    	
        	((CraftPlayer)p).getHandle().itemInWorldManager.c(main_block.getX(), main_block.getY(), main_block.getZ());
        		
        	transverse-=1;
        	
        	cutDownTree(main_block.getRelative(0, 1, 0), p, transverse);
        	cutDownTree(main_block.getRelative(0, -1, 0), p, transverse);
        	cutDownTree(main_block.getRelative(1, 0, 0), p, transverse);
        	cutDownTree(main_block.getRelative(-1, 0, 0), p, transverse);
        	cutDownTree(main_block.getRelative(0, 0, 1), p, transverse);
        	cutDownTree(main_block.getRelative(0, 0, -1), p, transverse);
	    }
	    
	    
	    public void spawnWolf(Player p)
	    {
	    	Wolf actual_wolf = (Wolf) p.getWorld().spawnCreature(p.getLocation(), CreatureType.WOLF);
        	actual_wolf.setOwner(p);
	    }
	    
	    public void spawnGhast(Player p)
	    {
	    	Wolf actual_wolf = (Wolf) p.getWorld().spawnCreature(p.getLocation(), CreatureType.GHAST);
	    }
	    
	    public void spawnZombie(Player p)
	    {
	    	Wolf actual_wolf = (Wolf) p.getWorld().spawnCreature(p.getLocation(), CreatureType.PIG_ZOMBIE);
	    }
	  
	    public Entity getTarget(Player p)
	    {
	    	
	    	
	    	List<Entity> surrounding = p.getNearbyEntities(10,10,10);
	    	int i;
	    	for (i=0; i < surrounding.size(); i++)
	    	{
	    		Entity thing = surrounding.get(i);
	    		p.getWorld().strikeLightning(thing.getLocation());
	    		p.sendMessage("Entity detected");
	    	}
	    	
			return null;
	    	
	    }
	    
	    
	    @Override
	    public void onPlayerFish(PlayerFishEvent event)
	    {
	    	Player player = event.getPlayer();
	    	if (event.getCaught()!=null)
	    	{

	    	int fish_level = plugin.skillLevel(player, "Fishing");
			if (fish_level > 10)
				fish_level = 10;
			
			int catchChance = (int) (Math.random() * (plugin.skillLevel(player, "Fishing")+1)*3 + 1);
			ItemStack fish1 = new ItemStack(349, 1);
			
			if (catchChance == 1 || catchChance == 2
					|| catchChance == 3 || catchChance == 4) {
				player.sendMessage(ChatColor.YELLOW
						+ "You caught a fish");
				player.getInventory().addItem(fish1);
				plugin.gainExperience(player, "Fishing", 10);
				
				 plugin.monKil("Fish", player);
			}
			if (catchChance == 5) {
				int bigChance = (int) (Math.random() * 5 + 1);
				if (bigChance == 1) {
					EntityLiving e = null;
					CraftPlayer craftPlayer = (CraftPlayer) player;
					CraftWorld craftWorld = (CraftWorld) craftPlayer
							.getWorld();
					Skeleton actual_wolf = (Skeleton) player.getWorld().spawnCreature(player.getLocation(), CreatureType.SKELETON);

					player.sendMessage(ChatColor.RED
							+ "You reel in your line to find its stuck to a skeleton!");
					
					plugin.gainExperience(player, "Fishing", 5);
				} else {
					player.sendMessage(ChatColor.RED
							+ "A skeleton got caught on the line! One of its bones breaks and it sinks into the water...");
					ItemStack sBone = new ItemStack(352, 2);
					ItemStack sBoneMeal = new ItemStack(351, 4,
							(short) 15);
					player.getInventory().addItem(sBone);
					player.getInventory().addItem(sBoneMeal);
					
					plugin.gainExperience(player, "Fishing", 10);
				}
			}
			if (catchChance == 6) {
				ItemStack lBoots = new ItemStack(301, 1, (short) 20);
				player.sendMessage(ChatColor.YELLOW
						+ "You reel in your line to find its stuck to some old boots. There is a fish inside!");
				player.getInventory().addItem(fish1);
				player.getInventory().addItem(lBoots);
				
				plugin.gainExperience(player, "Fishing", 15);
			}
			if (catchChance == 7) {
				ItemStack lHelmet = new ItemStack(298, 1, (short) 20);
				player.sendMessage(ChatColor.YELLOW
						+ "You reel in your line to find its stuck to a sodden hat. There is a fish inside!");
				player.getInventory().addItem(fish1);
				player.getInventory().addItem(lHelmet);
				
				plugin.gainExperience(player, "Fishing", 15);
			}
			if (catchChance == 8 || catchChance == 30) {
				ItemStack dirt = new ItemStack(3, 1);
				player.sendMessage(ChatColor.RED
						+ "Your hook is covered in dirt.");
				player.getInventory().addItem(dirt);
				
				plugin.gainExperience(player, "Fishing", -5);
			}
			if (catchChance == 9) {
				ItemStack bone = new ItemStack(352, 1);
				player.sendMessage(ChatColor.YELLOW
						+ "You caught a fish, which spewed up a dirty bone.");
				player.getInventory().addItem(fish1);
				player.getInventory().addItem(bone);
				
				plugin.gainExperience(player, "Fishing", 18);
			}
			if (catchChance == 10) {
				ItemStack bones = new ItemStack(352, 2);
				player.sendMessage(ChatColor.YELLOW
						+ "You caught a fish, which spewed up two dirty bones.");
				player.getInventory().addItem(fish1);
				player.getInventory().addItem(bones);
				
				plugin.gainExperience(player, "Fishing", 20);
			}
			if (catchChance == 11) {
				int bigChance = (int) (Math.random() * 3 + 1);
				if (bigChance == 1 || bigChance == 2) {
					player.sendMessage(ChatColor.YELLOW
							+ "You caught a fish.");
					player.getInventory().addItem(fish1);
					plugin.gainExperience(player, "Fishing", 10);
				} else {
					player.getInventory().addItem(fish1);
					player.getInventory().addItem(fish1);
					player.getInventory().addItem(fish1);
					player.sendMessage(ChatColor.GOLD
							+ "Whoa! You've caught a big one.");
					plugin.gainExperience(player, "Fishing", 60);
				}
			}
			if (catchChance == 12 || catchChance == 15
					|| catchChance == 29) {
				ItemStack lBoots = new ItemStack(301, 1, (short) 20);
				player.sendMessage(ChatColor.DARK_AQUA
						+ "You reel in your line to find its stuck to some old boots.");
				player.getInventory().addItem(lBoots);
				plugin.gainExperience(player, "Fishing", 15);
			}
			if (catchChance == 13 || catchChance == 14
					|| catchChance == 28) {
				ItemStack lHelmet = new ItemStack(298, 1, (short) 20);
				player.sendMessage(ChatColor.DARK_AQUA
						+ "You reel in your line to find its stuck to a sodden hat.");
				player.getInventory().addItem(lHelmet);
				plugin.gainExperience(player, "Fishing", 15);
			}
			if (catchChance >= 16 && catchChance <= 20
					|| catchChance == 27) {
				player.sendMessage(ChatColor.RED
						+ "You didn't catch anything.");
				plugin.gainExperience(player, "Fishing", -5);
			}
			if (catchChance == 21) {
				int bigChance = (int) (Math.random() * 3 + 1);
				if (bigChance == 1 || bigChance == 2) {
					EntityLiving e = null;
					CraftPlayer craftPlayer = (CraftPlayer) player;
					CraftWorld craftWorld = (CraftWorld) craftPlayer
							.getWorld();

					Squid actual_wolf = (Squid) player.getWorld().spawnCreature(player.getLocation(), CreatureType.SQUID);

					player.sendMessage(ChatColor.YELLOW
							+ "You caught a Squid!");
					plugin.gainExperience(player, "Fishing", 20);
				} else {
					EntityLiving e = null;
					CraftPlayer craftPlayer = (CraftPlayer) player;
					CraftWorld craftWorld = (CraftWorld) craftPlayer
							.getWorld();
					Squid actual_wolf = (Squid) player.getWorld().spawnCreature(player.getLocation(), CreatureType.SQUID);
					player.sendMessage(ChatColor.YELLOW
							+ "You snagged a Squid!");
					player.getInventory().addItem(fish1);
					player.sendMessage(ChatColor.AQUA
							+ "The squid had a fish in its tentacles!");
					plugin.gainExperience(player, "Fishing", 30);
				}
			}
			if (catchChance >= 22 && catchChance <= 25) {
				ItemStack sString = new ItemStack(287, 1);
				player.getInventory().addItem(sString);
				player.sendMessage(ChatColor.AQUA
						+ "You caught some string.");
				plugin.gainExperience(player, "Fishing", 5);
			}
			if (catchChance == 26) {
				int bigChance = (int) (Math.random() * 10 + 1);
				if (bigChance == 1 || bigChance == 2) {
					EntityLiving e = null;
					CraftPlayer craftPlayer = (CraftPlayer) player;
					CraftWorld craftWorld = (CraftWorld) craftPlayer
							.getWorld();
					Pig actual_wolf = (Pig) player.getWorld().spawnCreature(player.getLocation(), CreatureType.PIG);

					player.sendMessage(ChatColor.GREEN
							+ "You saved a pig from drowning!");
					plugin.gainExperience(player, "Fishing", 40);

				} else {
					player.sendMessage(ChatColor.GREEN
							+ "Oh-no! a poor pig is caught on the line... Unfortunatly it falls back into the water...");
					ItemStack sPork = new ItemStack(319, 1);
					player.getInventory().addItem(sPork);
					player.sendMessage(ChatColor.RED
							+ "Well, atleast you got some pork...");
					plugin.gainExperience(player, "Fishing", 50);
				}
			}
			if (catchChance == 31) {
				int bigChance = (int) (Math.random() * 10 + 1);
				if (bigChance >= 1 && bigChance <= 8) {
					player.sendMessage(ChatColor.GREEN
							+ "You found a wooden bowl... weird");
					ItemStack sBowl = new ItemStack(281, 1);
					player.getInventory().addItem(sBowl);
					plugin.gainExperience(player, "Fishing", 10);
				} else {
					player.sendMessage(ChatColor.GOLD
							+ "You found a wooden bowl... Some gold was inside!");
					ItemStack sBowl = new ItemStack(281, 1);
					ItemStack sGold = new ItemStack(14, 1);
					player.getInventory().addItem(sBowl);
					player.getInventory().addItem(sGold);
					plugin.gainExperience(player, "Fishing", 80);
				}
			}
	    	}
		}
	    
	    
	    public void freezeBlock(Block block, int frequency)
	    {
	    	if (frequency <= 0)
	    		return;
	    	
	    	Block freeze = block.getRelative(0, 1, 0);

	    	if (freeze.getType() == Material.SNOW)
	    		return;
	    	
	    	if (block.getType() == Material.ICE)
	    		return;
	    	
	    	if (block.getType()==Material.LONG_GRASS||block.getType()==Material.YELLOW_FLOWER||block.getType()==Material.RED_MUSHROOM||block.getType()==Material.RED_ROSE||block.getType()==Material.BROWN_MUSHROOM)
	    		return; //Cant freeze long grass
	    	
	    	if (block.getType() == Material.WATER)
	    		block.setType(Material.ICE);
	    	else if (block.getType() == Material.AIR)
	    		return;
	    	else if (block.getType() == Material.SNOW)
	    		return;
	    	else if (freeze.getType() == Material.AIR)
	    		freeze.setType(Material.SNOW);
	    	
	    	frequency-=1;
	    	freezeBlock(block.getRelative(-1,0, 0), frequency);
	    	freezeBlock(block.getRelative(-1,0, 1), frequency);
	    	freezeBlock(block.getRelative(1,0,0), frequency);
	    	freezeBlock(block.getRelative(1,0,1), frequency);
	    	
	    }
	    
	    public void freezeSpell(Player p)
	    {
	    	
	    	//Block block = p.getWorld().getBlockAt(p.getLocation()).getRelative(0, -1, 0); 
	    	
	    	
	    	long current_time = System.currentTimeMillis();
	    	long previous_time = Long.valueOf(plugin.playerConfig(p).getString("lastCastTimeFreeze", String.valueOf(0)));
	    		
	    	long time_since = current_time-previous_time;
		    if (p.getInventory().contains(Material.INK_SACK))
		    {
			    	if (time_since > 1000)
					{
			    		
			    		plugin.playerConfig(p).setProperty("lastCastTimeFreeze", String.valueOf(current_time));
							Block block = p.getTargetBlock(null, 2000);
							
							if (plugin.gainMana(p, -20))
					    	{
								freezeBlock(block, 15);
								removeInventoryItems(p.getInventory(), Material.INK_SACK, 1);
					    	}
				    }
				    else
						p.sendMessage(ChatColor.RED+"You can only cast this spell every second.");
		    }
		    else
		    		p.sendMessage(ChatColor.RED+"You require an ink_sack to cast this spell.");
		    
	    	
	    	
	    	

	    	
	    	//Block block2 = block. if (block2.getType() == Material.AIR) block2.setType(Material.SNOW);
	    }
	    
	    public static void removeInventoryItems(Inventory inv, Material type, int amount) {
	        for (ItemStack is : inv.getContents()) {
	            if (is != null && is.getType() == type) {
	                int newamount = is.getAmount() - amount;
	                if (newamount > 0) {
	                    is.setAmount(newamount);
	                    break;
	                } else {
	                	amount -= is.getAmount();
	                    inv.remove(is);
	                	//is.setAmount(0);

	                    if (amount <= 0) return;
	                }
	            }
	        }
	    }
	    
	    public static void removeInventoryItems(Inventory inv, Integer integer, int amount) {
	    	int i= 0;
	        for (ItemStack is : inv.getContents()) {
	            if (is != null && is.getTypeId() == integer) {
	                int newamount = is.getAmount() - amount;
	                if (newamount > 0) {
	                    is.setAmount(newamount);
	                    break;
	                } else {
	                	amount -= is.getAmount();
	                    inv.remove(is);

	                    
	                    
	                    if (amount <= 0) return;
	                }
	            }
	            i++;
	        }
	    }
	    
	    public boolean  slowBlock(Block b, Player p, int dist)
	    {
	    	if (dist == 0)
	    		return false ;

	    	
	    	/*
	    	if (b.getType() == Material.WOOD || b.getType() == Material.LOG|| b.getType() == Material.CHEST || b.getType() == Material.FENCE)
	    		return false;
	    	Block b2 = above_block.getRelative(-1, 0, 0);
	    	if (b2.getType() == Material.WOOD || b2.getType() == Material.LOG || b2.getType() == Material.CHEST || b2.getType() == Material.FENCE)
	    		return false;
	    	b2 = above_block.getRelative(1, 0, 0);
	    	if (b2.getType() == Material.WOOD || b2.getType() == Material.LOG || b2.getType() == Material.CHEST || b2.getType() == Material.FENCE)
	    		return false;
	    	b2 = above_block.getRelative(0, 0, 1);
	    	if (b2.getType() == Material.WOOD || b2.getType() == Material.LOG || b2.getType() == Material.CHEST || b2.getType() == Material.FENCE)
	    		return false;
	    	b2 = above_block.getRelative(0, 0, -1);
	    	if (b2.getType() == Material.WOOD || b2.getType() == Material.LOG || b2.getType() == Material.CHEST || b2.getType() == Material.FENCE)
	    		return false;
	    	*/
	    	

	    	
	    	p.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new LGBlockReplacer2(plugin, b), 60);
	    	
	    	sendGlobalBlockChange(b.getLocation(), Material.SOUL_SAND, b.getData());
	    	//b.setType(Material.SOUL_SAND);
	    	
	    	dist--;
	    	
	    	slowBlock(b.getRelative(-1, 0, 0), p, dist);
	    	slowBlock(b.getRelative(1, 0, 0), p, dist);
	    	slowBlock(b.getRelative(0, 0, -1), p, dist);
	    	slowBlock(b.getRelative(0, 0, 1), p, dist);
	    	
	    	
	    	return true;
	    }

	    
	    public void castSlow(Player p)
	    {
	    	long current_time = System.currentTimeMillis();
	    	long previous_time = Long.valueOf(plugin.playerConfig(p).getString("lastSLOW", String.valueOf(0)));
	    		
	    	long time_since = current_time-previous_time;
		    if (p.getInventory().contains(Material.SOUL_SAND))
		    {
			    	if (time_since > 6000)
					{
			    		if (plugin.gainMana(p, -1))
				    	{
			    			plugin.playerConfig(p).setProperty("lastSLOW", String.valueOf(current_time));
							Block block = p.getTargetBlock(null, 2000);
		
							slowBlock(block, p, 4);

							removeInventoryItems(p.getInventory(), Material.SOUL_SAND, 1);
				    	}
				    }
				    else
						p.sendMessage(ChatColor.RED+"You can only cast this spell every 6 seconds.");
		    }
		    else
		    		p.sendMessage(ChatColor.RED+"You require soulsand to cast this spell.");
	    }
	    
	    

	    public boolean  zapBlock(Block b, Player p, int dist)
	    {
	    	if (dist == 0)
	    		return false ;
	    	
	    	int lvl = plugin.skillLevel((Player)p, "Magic");
	    	
	    	List<Entity> list = p.getNearbyEntities(40, 40, 40);
	    	String houseID = plugin.getSettle().getString(p.getName()+".houseID", "");
	    	boolean found = false;
	    	int i;
	    	for (i=0; i < list.size(); i++)
	    	{
	    		Entity e = list.get(i);
	    		if (e.getLocation().distance(b.getLocation()) <=dist )
	    		{
	    			if (e instanceof Player)
	    			{
		    	    	String houseID2 = plugin.getSettle().getString(((Player) e).getName()+".houseID", "");
		    			if (houseID.equalsIgnoreCase(houseID2))
		    			{
		    				continue;
		    			}
	    			}
	    			
	    			p.getWorld().strikeLightning(e.getLocation());
	    			found = true;
	    		}
	    	}
	    	
	    	if (!found)
	    	{
				p.sendMessage(ChatColor.RED+"You didn't cast the spell within range of your target. Try aiming for your targets feet.");
	    	}
	    	
	    	
	    	return true;
	    }
	    public boolean  burnBlock(Block b, Player p, int dist)
	    {
	    	if (dist == 0)
	    		return false ;
	    	
	    	int lvl = plugin.skillLevel((Player)p, "Magic");
	    	
	    	List<Entity> list = p.getNearbyEntities(40, 40, 40);
	    	
	    	//Shoot a ghastball at the location
	    	//p.getWorld().
	    	
	    	
	    	//EntityFireball fball = new EntityFireball(((CraftWorld) player.getWorld()).getHandle(), playerEntity, lookat.getX(), lookat.getY(), lookat.getZ());
	    	
	    	String houseID = plugin.getSettle().getString(p.getName()+".houseID", "");
	    	
	    	boolean found = false;
	    	int i;
	    	for (i=0; i < list.size(); i++)
	    	{
	    		Entity e = list.get(i);
	    		if (e.getLocation().distance(b.getLocation()) <=dist )
	    		{
	    			if (e instanceof Player)
	    			{
	    				String houseID2 = plugin.getSettle().getString(((Player) e).getName()+".houseID", "");
	    				if (houseID.equalsIgnoreCase(houseID2))
	    				{
	    					continue; // NO DAMAGE!
	    				}
	    			}
	    			
	    			e.setFireTicks(20*(lvl+12));
	    			found = true;
	    		}
	    	}
	    	
	    	
	   
	    	
	    	if (!found)
	    	{
				p.sendMessage(ChatColor.RED+"You didn't cast the spell within range of your target. Try aiming for your targets feet.");
	    	}
	    	
	    	Block above_block = b.getRelative(0, 1, 0);
	    	
	    	/*
	    	if (b.getType() == Material.WOOD || b.getType() == Material.LOG|| b.getType() == Material.CHEST || b.getType() == Material.FENCE)
	    		return false;
	    	Block b2 = above_block.getRelative(-1, 0, 0);
	    	if (b2.getType() == Material.WOOD || b2.getType() == Material.LOG || b2.getType() == Material.CHEST || b2.getType() == Material.FENCE)
	    		return false;
	    	b2 = above_block.getRelative(1, 0, 0);
	    	if (b2.getType() == Material.WOOD || b2.getType() == Material.LOG || b2.getType() == Material.CHEST || b2.getType() == Material.FENCE)
	    		return false;2
	    	b2 = above_block.getRelative(0, 0, 1);
	    	if (b2.getType() == Material.WOOD || b2.getType() == Material.LOG || b2.getType() == Material.CHEST || b2.getType() == Material.FENCE)
	    		return false;
	    	b2 = above_block.getRelative(0, 0, -1);
	    	if (b2.getType() == Material.WOOD || b2.getType() == Material.LOG || b2.getType() == Material.CHEST || b2.getType() == Material.FENCE)
	    		return false;
	    	*/
	    	
	    	/*
	    	if (b.getType() == Material.LONG_GRASS)
	    		b.setType(Material.FIRE);
	    	else if (above_block.getType() == Material.AIR)
	    		above_block.setType(Material.FIRE);
	    	
	    	dist--;
	    	
	    	burnBlock(b.getRelative(-1, 0, 0), p, dist);
	    	burnBlock(b.getRelative(1, 0, 0), p, dist);
	    	burnBlock(b.getRelative(0, 0, -1), p, dist);
	    	burnBlock(b.getRelative(0, 0, 1), p, dist);
	    	*/
	    	
	    	return true;
	    }

	    
	    public void addBlastZone(Player p, Location loc, int seconds, String spell)
	    {
	    	
	    	String coords = String.format("%d %d %d", (int)loc.getX(), (int)loc.getY(), (int)loc.getZ());
        	
	    	String current = String.valueOf(System.currentTimeMillis());
	    	plugin.getCon().setProperty("TempMagic."+current+"|"+plugin.playerName(p)+".Player", plugin.playerName(p));
	    	plugin.getCon().setProperty("TempMagic."+current+"|"+plugin.playerName(p)+".Time", current);
	    	plugin.getCon().setProperty("TempMagic."+current+"|"+plugin.playerName(p)+".Location",coords );
	    	plugin.getCon().setProperty("TempMagic."+current+"|"+plugin.playerName(p)+".Spell",spell );

	    	LGBlastZones mana_bar = new LGBlastZones(plugin, current+"|"+plugin.playerName(p));
	    	int timerID = p.getServer().getScheduler().scheduleSyncDelayedTask(plugin, mana_bar, 20*seconds);
	    }
	    
	  
	    public void castCacti(Player p)
	    {
	    	long current_time = System.currentTimeMillis();
	    	long previous_time = Long.valueOf(plugin.playerConfig(p).getString("lastCactiTime", String.valueOf(0)));
	    		
	    	long time_since = current_time-previous_time;
		    if (p.getInventory().contains(Material.CACTUS))
		    {
			    	if (time_since > 2000)
					{
			    		if (plugin.gainMana(p, -1))
				    	{
			    			plugin.playerConfig(p).setProperty("lastCactiTime", String.valueOf(current_time));
							Block block = p.getTargetBlock(null, 2000);
		
							
							p.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new LGBlockReplacer(plugin, block.getRelative(0, 1, 0)), 30);
							p.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new LGBlockReplacer(plugin, block.getRelative(0, 0, 0)), 40);
							
							p.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new LGBlockReplacer(plugin, block.getRelative(-1, 1, 1)), 30);
							p.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new LGBlockReplacer(plugin, block.getRelative(-1, 0, 1)), 40);
							
							p.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new LGBlockReplacer(plugin, block.getRelative(1, 1, -1)), 30);
							p.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new LGBlockReplacer(plugin, block.getRelative(1, 0, -1)), 40);
							
							
							block.getRelative(0, 0, 0).setType(Material.SAND);
							block.getRelative(0, 1, 0).setType(Material.CACTUS);
							
							block.getRelative(-1, 0, 1).setType(Material.SAND);
							block.getRelative(-1, 1, 1).setType(Material.CACTUS);
							
							block.getRelative(1, 0, -1).setType(Material.SAND);
							block.getRelative(1, 1, -1).setType(Material.CACTUS);

							
							removeInventoryItems(p.getInventory(), Material.CACTUS, 1);
				    	}
				    }
				    else
						p.sendMessage(ChatColor.RED+"You can only cast this spell every 2 seconds.");
		    }
		    else
		    		p.sendMessage(ChatColor.RED+"You require an arrow to cast this spell.");
	    }
	    
	    public void castShadowDarkness(Player p)
	    {
	    	long current_time = System.currentTimeMillis();
	    	long previous_time = Long.valueOf(plugin.playerConfig(p).getString("lastDarkness", String.valueOf(0)));
	    		
	    	long time_since = current_time-previous_time;
		    if (p.getInventory().contains(Material.DIAMOND))
		    {
			    	if (time_since > 40000)
					{
			    		
			    			plugin.playerConfig(p).setProperty("lastDarkness", String.valueOf(current_time));
							Block block = p.getTargetBlock(null, 2000);
							
							int lvl2 = plugin.skillLevel((Player)p, "Magic");
							if (block.getLocation().distance(p.getLocation()) > 10+(lvl2*2))
							{
								p.sendMessage(ChatColor.RED+"That casting location is too far away!");
								return;
							}
							
							
					    	int lvl = plugin.skillLevel((Player)p, "Magic");
					    	
					    	long current_times = p.getWorld().getTime();
					    	p.getWorld().setTime(18000);
					    	p.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new LGTimeChanger(plugin, current_times, p.getWorld()), 100);
							
					    	
					    	addBlastZone(p, block.getLocation(), 10, "darkness of the moon");
							if (plugin.gainMana(p, -100))
						    {
								
								
								
								
						    	List<Entity> list = p.getNearbyEntities(40, 40, 40);
						    	String houseID = plugin.getSettle().getString(p.getName()+".houseID", "");
						    	
						    	boolean found = false;
						    	int i;
						    	for (i=0; i < list.size(); i++)
						    	{
						    		Entity e = list.get(i);
						    		if (e.getLocation().distance(block.getLocation()) <=30 )
						    		{
						    			if (e instanceof LivingEntity)
						    			{
						    				
						    				if (e instanceof Player)
							    			{
						    		    	String houseID2 = plugin.getSettle().getString(((Player) e).getName()+".houseID", "");
						    				if (houseID.equalsIgnoreCase(houseID2))
						    				{
						    				continue;
						    				}
							    			}
						    				((LivingEntity) e).damage(15);
						    				
						    				p.getWorld().playEffect(e.getLocation(), Effect.SMOKE, 8);
											p.getWorld().createExplosion(e.getLocation(), 0);
											
						    			}
						    			
						    			found = true;
						    		}
						    	}
						    	
						    	if (!found)
						    	{
									p.sendMessage(ChatColor.RED+"You didn't cast the spell within range of your target. Try aiming for your targets feet.");
						    	}
						    	
						    	

								
								
								
								
								
								
								
								removeInventoryItems(p.getInventory(), Material.DIAMOND, 1);
						    }
							else
							{
								p.sendMessage(ChatColor.RED+"That block refuses to burn.");
							}
				    }
				    else
						p.sendMessage(ChatColor.RED+"You can only cast this spell every 40 seconds.");
		    }
		    else
		    		p.sendMessage(ChatColor.RED+"You require a diamond to cast this spell.");
	    }
	    
	    public void castUberBolt(Player p)
	    {
	    	long current_time = System.currentTimeMillis();
	    	long previous_time = Long.valueOf(plugin.playerConfig(p).getString("lastUberStorm", String.valueOf(0)));
	    		
	    	long time_since = current_time-previous_time;
		    if (p.getInventory().contains(Material.GOLD_INGOT))
		    {
			    	if (time_since > 20000)
					{
			    		
			    		plugin.playerConfig(p).setProperty("lastUberStorm", String.valueOf(current_time));
							Block block = p.getTargetBlock(null, 2000);
							
							int lvl2 = plugin.skillLevel((Player)p, "Magic");
							if (block.getLocation().distance(p.getLocation()) > 10+(lvl2*2))
							{
								p.sendMessage(ChatColor.RED+"That casting location is too far away!");
								return;
							}
							
					    	int lvl = plugin.skillLevel((Player)p, "Magic");
				
								if (plugin.gainMana(p, -44) && zapBlock(block, p, 20))
						    	{
									p.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new LGZapper(plugin, block, this, p), 0);
									p.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new LGZapper(plugin, block, this, p), 20);
									p.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new LGZapper(plugin, block, this, p), 40);
									p.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new LGZapper(plugin, block, this, p), 60);
									p.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new LGZapper(plugin, block, this, p), 80);
									
									
									addBlastZone(p, block.getLocation(), 10, "wraith of the elder sage");
									removeInventoryItems(p.getInventory(), Material.GOLD_INGOT, 1);
						    	}
	
				    }
				    else
						p.sendMessage(ChatColor.RED+"You can only cast this spell every 20 seconds.");
		    }
		    else
		    		p.sendMessage(ChatColor.RED+"You require a gold ingot to cast this spell.");
	    }
	    
	 /*    public void castBolt(Player p)
	    {
	    	long current_time = System.currentTimeMillis();
	    	long previous_time = Long.valueOf(plugin.getConfig().getString(plugin.playerName(p)+".lastCastTime", String.valueOf(0)));
	    		
	    	long time_since = current_time-previous_time;
		    if (p.getInventory().contains(Material.ARROW))
		    {
			    	if (time_since > 4000)
					{
			    		if (plugin.gainMana(p, -15))
				    	{
			    			
			    			plugin.getConfig().setProperty(plugin.playerName(p)+".lastCastTime", String.valueOf(current_time));
							Block block = p.getTargetBlock(null, 2000);
							
							int lvl2 = plugin.skillLevel((Player)p, "Magic");
							if (block.getLocation().distance(p.getLocation()) > 10+(lvl2*2))
							{
								p.sendMessage(ChatColor.RED+"That casting location is too far away!");
								return;
							}
							
							addBlastZone(p, block.getLocation(), 3, "small bolt");
							p.getWorld().strikeLightning(block.getLocation());
										    		
							removeInventoryItems(p.getInventory(), Material.ARROW, 1);
							
				    	}
				    }
				    else
						p.sendMessage(ChatColor.RED+"You can only cast this spell every 4 seconds.");
		    }
		    else
		    		p.sendMessage(ChatColor.RED+"You require gunpowder to cast this spell.");
	    }
	   */ 
	    
	    
	    public void castLargeFlame(Player p)
	    {
	    	long current_time = System.currentTimeMillis();
	    	long previous_time = Long.valueOf(plugin.playerConfig(p).getString("lastCastTimeArrowLowLARGE", String.valueOf(0)));
	    		
	    	
	    
	    	
	    	
	    	long time_since = current_time-previous_time;
		    if (p.getInventory().contains(Material.COAL))
		    {
			    	if (time_since > 10000)
					{
			    		
			    		plugin.playerConfig(p).setProperty("lastCastTimeArrowLowLARGE", String.valueOf(current_time));
							Block block = p.getTargetBlock(null, 2000);
							
							int lvl2 = plugin.skillLevel((Player)p, "Magic");
							if (block.getLocation().distance(p.getLocation()) > 10+(lvl2))
							{
								p.sendMessage(ChatColor.RED+"That casting location is too far away!");
								return;
							}
							
					    	int lvl = plugin.skillLevel((Player)p, "Magic");
							if (burnBlock(block, p, lvl+5))
							{
								if (plugin.gainMana(p, -24))
						    	{
								 	
									List<Location> smokeLocations = new ArrayList<Location>();
									smokeLocations.add(block.getLocation().clone().add(0, 0, 0));
									smokeLocations.add(block.getLocation().clone().add(0, 1, 0));
									new SmokeUtil().spawnCloudRandom(smokeLocations, (float)lvl+5);
									
									addBlastZone(p, block.getLocation(), 10, "large flame");
									removeInventoryItems(p.getInventory(), Material.COAL, 1);
						    	}
							}
							else
							{
								p.sendMessage(ChatColor.RED+"That block refuses to burn.");
							}
				    }
				    else
						p.sendMessage(ChatColor.RED+"You can only cast this spell every 10 seconds.");
		    }
		    else
		    		p.sendMessage(ChatColor.RED+"You require coal to cast this spell.");
	    }
	    
	    
	    
	    public boolean  slow2Block(Block b, Player p, int dist)
	    {
	    	if (dist == 0)
	    		return false ;
	    	
	    	int lvl = plugin.skillLevel((Player)p, "Magic");
	    	
	    	List<Entity> list = p.getNearbyEntities(40, 40, 40);

	    	boolean found = false;
	    	int i;
	    	for (i=0; i < list.size(); i++)
	    	{
	    		Entity e = list.get(i);
	    		if (e.getLocation().distance(b.getLocation()) <=dist )
	    		{
	    			if (e instanceof EntityLiving)
	    				((EntityLiving) e).addEffect(new MobEffect(2, lvl * 20, 2));
	    			
	    			found = true;
	    		}
	    	}

	    	if (!found)
	    	{
				p.sendMessage(ChatColor.RED+"You didn't cast the spell within range of your target. Try aiming for your targets feet.");
	    	}
	    	
	    	return true;
	    }

	    
	    public void castSlowness(Player p)
	    {
	    	long current_time = System.currentTimeMillis();
	    	long previous_time = Long.valueOf(plugin.playerConfig(p).getString("lastCastSlowness", String.valueOf(0)));
	    		
	    	long time_since = current_time-previous_time;
		    if (p.getInventory().contains(Material.BLAZE_POWDER))
		    {
			    	if (time_since > 20000)
					{
			    		plugin.playerConfig(p).setProperty("lastCastSlowness", String.valueOf(current_time));
							Block block = p.getTargetBlock(null, 2000);
							
							int lvl2 = plugin.skillLevel((Player)p, "Magic");
							if (block.getLocation().distance(p.getLocation()) > 14+(lvl2*2))
							{
								p.sendMessage(ChatColor.RED+"That casting location is too far away!");
								return;
							}
							
					    	int lvl = plugin.skillLevel((Player)p, "Magic");
							if (slow2Block(block, p, lvl+2))
							{
								if (plugin.gainMana(p, -32))
						    	{
									List<Location> smokeLocations = new ArrayList<Location>();
									smokeLocations.add(block.getLocation().clone().add(0, 0, 0));
									smokeLocations.add(block.getLocation().clone().add(0, 1, 0));
									new SmokeUtil().spawnCloudRandom(smokeLocations, (float)lvl+2);
									
									removeInventoryItems(p.getInventory(), Material.BLAZE_ROD, 1);
						    	}
							}
							else
							{
								p.sendMessage(ChatColor.RED+"That block refuses to slow.");
							}
				    }
				    else
						p.sendMessage(ChatColor.RED+"You can only cast this spell every 20 seconds.");
		    }
		    else
		    		p.sendMessage(ChatColor.RED+"You require blaze powder to cast this spell.");
	    }
	    
	    public void castSmallFlame(Player p)
	    {
	    	long current_time = System.currentTimeMillis();
	    	long previous_time = Long.valueOf(plugin.playerConfig(p).getString("lastCastTimeArrowLow", String.valueOf(0)));
	    		
	    	long time_since = current_time-previous_time;
		    if (p.getInventory().contains(Material.LOG))
		    {
			    	if (time_since > 3000)
					{
			    		
			    		plugin.playerConfig(p).setProperty("lastCastTimeArrowLow", String.valueOf(current_time));
							Block block = p.getTargetBlock(null, 2000);
							
							int lvl2 = plugin.skillLevel((Player)p, "Magic");
							if (block.getLocation().distance(p.getLocation()) > 7+(lvl2/2))
							{
								p.sendMessage(ChatColor.RED+"That casting location is too far away!");
								return;
							}
							
					    	int lvl = plugin.skillLevel((Player)p, "Magic");
							if (burnBlock(block, p, lvl+3))
							{
								if (plugin.gainMana(p, -24))
						    	{
								 	
									List<Location> smokeLocations = new ArrayList<Location>();
									smokeLocations.add(block.getLocation().clone().add(0, 0, 0));
									smokeLocations.add(block.getLocation().clone().add(0, 1, 0));
									SmokeUtil.spawnCloudRandom(smokeLocations, (float)lvl+2);
									
									addBlastZone(p, block.getLocation(), 10, "small flame");
									removeInventoryItems(p.getInventory(), Material.LOG, 1);
						    	}
							}
							else
							{
								p.sendMessage(ChatColor.RED+"That block refuses to burn.");
							}
				    }
				    else
						p.sendMessage(ChatColor.RED+"You can only cast this spell every 3 seconds.");
		    }
		    else
		    		p.sendMessage(ChatColor.RED+"You require a log to cast this spell.");
	    }
	    
	    public void castFlameArrow(Player p)
	    {
	    	long current_time = System.currentTimeMillis();
	    	long previous_time = Long.valueOf(plugin.playerConfig(p).getString("lastCastTimeArrow", String.valueOf(0)));
	    		
	    	long time_since = current_time-previous_time;
		    if (p.getInventory().contains(Material.EGG))
		    {
			    	if (time_since > 3000)
					{
			    		if (plugin.gainMana(p, -1))
				    	{
			    			plugin.playerConfig(p).setProperty(plugin.playerName(p)+"lastCastTime", String.valueOf(current_time));
							Block block = p.getTargetBlock(null, 2000);
							
							int lvl2 = plugin.skillLevel((Player)p, "Magic");
							if (block.getLocation().distance(p.getLocation()) > 10+(lvl2*2))
							{
								p.sendMessage(ChatColor.RED+"That casting location is too far away!");
								return;
							}
		
							Zombie zombie = (Zombie) p.getWorld().spawnCreature(block.getLocation(), CreatureType.ZOMBIE);
							
							LGAIZombie mana_bar = new LGAIZombie(p, plugin, zombie);

							int timerID = p.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, mana_bar, 0, 10);
					    	//getConfig().setProperty(playerName(player)+".manaTimer", timerID);


							removeInventoryItems(p.getInventory(), Material.EGG, 1);
				    	}
				    }
				    else
						p.sendMessage(ChatColor.RED+"You can only cast this spell every 1 seconds.");
		    }
		    else
		    		p.sendMessage(ChatColor.RED+"You require an arrow to cast this spell.");
	    }
	    
	    public void castExplosion(Player p)
	    {
	    	long current_time = System.currentTimeMillis();
	    	long previous_time = Long.valueOf(plugin.playerConfig(p).getString("lastExplosionCastTime", String.valueOf(0)));
	    		
	    	long time_since = current_time-previous_time;
		    if (p.getInventory().contains(Material.SULPHUR))
		    {
			    	if (time_since > 3000)
					{
			    		if (plugin.gainMana(p, -32))
				    	{
			    			plugin.playerConfig(p).setProperty(plugin.playerName(p)+"lastExplosionCastTime", String.valueOf(current_time));
							Block block = p.getTargetBlock(null, 2000);
String houseID = plugin.getSettle().getString(p.getName()+".houseID", "");
							int lvl2 = plugin.skillLevel((Player)p, "Magic");
							if (block.getLocation().distance(p.getLocation()) > 10+(lvl2*2))
							{
								p.sendMessage(ChatColor.RED+"That casting location is too far away!");
								return;
							}
							
							
							if (plugin.skillLevel(p, "Magic")>=10)
							{
								p.getWorld().playEffect(block.getLocation(), Effect.SMOKE, 8);
								p.getWorld().createExplosion(block.getLocation(), 0);
								
								
								int lvl = plugin.skillLevel((Player)p, "Magic");
								addBlastZone(p, block.getLocation(), 3, "small explosion");
								
						    	List<Entity> list = p.getNearbyEntities(20, 20, 20);
						    	
						    	boolean found = false;
						    	int i;
						    	for (i=0; i < list.size(); i++)
						    	{
						    		Entity e = list.get(i);
						    		if (e instanceof LivingEntity&&e.getLocation().distance(block.getLocation()) <=20 )
						    		{
						    			if (e instanceof Player)
						    			{
						    	    	String houseID2 = plugin.getSettle().getString(((Player) e).getName()+".houseID", "");
						    			if (houseID.equalsIgnoreCase(houseID2))
						    			{
						    				continue;
						    			}
						    			}
						    			
						    			LivingEntity en = (LivingEntity)e;
						    			en.damage(8, p);
						    			
						    			found = true;
						    		}
						    	}
						    	
						    	if (!found)
						    	{
									p.sendMessage(ChatColor.RED+"You didn't cast the spell within range of your target. Try aiming for your targets feet.");
						    	}
						    	
							}
							else if (plugin.skillLevel(p, "Magic")>=5)
							{
								p.getWorld().playEffect(block.getLocation(), Effect.SMOKE, 6);
								p.getWorld().createExplosion(block.getLocation(), 0);
								
								
								int lvl = plugin.skillLevel((Player)p, "Magic");
								addBlastZone(p, block.getLocation(), 3, "medium explosion");
								
						    	List<Entity> list = p.getNearbyEntities(15, 15, 15);
						    	
						    	boolean found = false;
						    	int i;
						    	for (i=0; i < list.size(); i++)
						    	{
						    		Entity e = list.get(i);
						    		if (e instanceof LivingEntity&&e.getLocation().distance(block.getLocation()) <=15 )
						    		{
						    			if (e instanceof Player)
						    			{
						    			String houseID2 = plugin.getSettle().getString(((Player) e).getName()+".houseID", "");
						    			if (houseID.equalsIgnoreCase(houseID2))
						    			{
						    				continue;
						    			}
						    			}
						    				LivingEntity en = (LivingEntity)e;
						    				en.damage(6, p);
						    				
						    				
						    				
						    			found = true;
						    		}
						    	}
						    	
						    	if (!found)
						    	{
									p.sendMessage(ChatColor.RED+"You didn't cast the spell within range of your target. Try aiming for your targets feet.");
						    	}
						    	
							}
							else if (plugin.skillLevel(p, "Magic")>=2)
							{
								p.getWorld().playEffect(block.getLocation(), Effect.SMOKE, 3);
								p.getWorld().createExplosion(block.getLocation(), 0);
								
								
								int lvl = plugin.skillLevel((Player)p, "Magic");
								addBlastZone(p, block.getLocation(), 3, "small explosion");
								
						    	List<Entity> list = p.getNearbyEntities(8, 8, 8);
						    	
						    	boolean found = false;
						    	int i;
						    	for (i=0; i < list.size(); i++)
						    	{
						    		Entity e = list.get(i);
						    		if (e instanceof LivingEntity&&e.getLocation().distance(block.getLocation()) <=8 )
						    		{
						    			if (e instanceof Player)
						    			{
						    			String houseID2 = plugin.getSettle().getString(((Player) e).getName()+".houseID", "");
						    			if (houseID.equalsIgnoreCase(houseID2))
						    			{
						    				continue;
						    			}
						    			}
						    			
						    				LivingEntity en = (LivingEntity)e;
						    				en.damage(4, p);
						    			found = true;
						    		}
						    	}
						    	
						    	if (!found)
						    	{
									p.sendMessage(ChatColor.RED+"You didn't cast the spell within range of your target. Try aiming for your targets feet.");
						    	}
						    	
								
								
							}
							
							
										    		
							removeInventoryItems(p.getInventory(), Material.SULPHUR, 1);
							
				    	}
				    }
				    else
						p.sendMessage(ChatColor.RED+"You can only cast this spell every 3 seconds.");
		    }
		    else
		    		p.sendMessage(ChatColor.RED+"You require gunpowder to cast this spell.");
	    }
	    
	    public void castTeleport(Player p)
	    {
	    	long current_time = System.currentTimeMillis();
	    	long previous_time = Long.valueOf(plugin.playerConfig(p).getString("lastTeleportCastTime", String.valueOf(0)));
	    		
	    	plugin.playerConfig(p).setProperty("lastTeleportCastTime", String.valueOf(current_time));
			String coords = plugin.playerConfig(p).getString("teleport_coordinates", "");
			
			if (!coords.equalsIgnoreCase(""))
			{
				
	    	long time_since = current_time-previous_time;
		    if (p.getInventory().contains(Material.BOOK))
		    {
			    	if (time_since > 180000)
					{
			    		
			    				
				    		if (plugin.gainMana(p, -44))
					    	{
				    			
				    			
				    				String[] crds = coords.split(" ");
				    				p.teleport(new Location(p.getWorld(), Integer.valueOf(crds[0]), Integer.valueOf(crds[1]), Integer.valueOf(crds[2])));
				    			
								removeInventoryItems(p.getInventory(), Material.BOOK, 1);
								
					    	}

		    				
				    }
				    else
						p.sendMessage(ChatColor.RED+"You can only cast this spell every 3 minutes.");
		    }
		    else
		    		p.sendMessage(ChatColor.RED+"You require a book to cast this spell.");
		    
    		
			}
			else
			{
				p.sendMessage(ChatColor.RED+"You need to set a location using /set before you can teleport!");
			}
	    }
	    
	    public void castBolt(Player p)
	    {
	    	long current_time = System.currentTimeMillis();
	    	long previous_time = Long.valueOf(plugin.playerConfig(p).getString("lastCastTime", String.valueOf(0)));
	    		
	    	long time_since = current_time-previous_time;
		    if (p.getInventory().contains(Material.BONE))
		    {
			    	if (time_since > 4000)
					{
			    		if (plugin.gainMana(p, -16))
				    	{
			    			
			    			plugin.playerConfig(p).setProperty("lastCastTime", String.valueOf(current_time));
							Block block = p.getTargetBlock(null, 2000);
							
							int lvl2 = plugin.skillLevel((Player)p, "Magic");
							if (block.getLocation().distance(p.getLocation()) > 10+(lvl2*2))
							{
								p.sendMessage(ChatColor.RED+"That casting location is too far away!");
								return;
							}
							
							addBlastZone(p, block.getLocation(), 3, "small bolt");
							p.getWorld().strikeLightning(block.getLocation());
										    		
							removeInventoryItems(p.getInventory(), Material.BONE, 1);
							
				    	}
				    }
				    else
						p.sendMessage(ChatColor.RED+"You can only cast this spell every 4 seconds.");
		    }
		    else
		    		p.sendMessage(ChatColor.RED+"You require gunpowder to cast this spell.");
	    }
	    
	    public void castSuperBolt(Player p)
	    {
	    	long current_time = System.currentTimeMillis();
	    	long previous_time = Long.valueOf(plugin.playerConfig(p).getString("lastCastTimeS", String.valueOf(0)));
	    		
	    	long time_since = current_time-previous_time;
		    if (p.getInventory().contains(Material.ENDER_PEARL))
		    {
			    	if (time_since > 10000)
					{
			    		if (plugin.gainMana(p, -40))
				    	{
			    			plugin.playerConfig(p).setProperty("lastCastTimeS", String.valueOf(current_time));
			    			
							Block block = p.getTargetBlock(null, 2000);
							addBlastZone(p, block.getLocation(), 3, "lightning storm");
			    			
							int lvl2 = plugin.skillLevel((Player)p, "Magic");
							if (block.getLocation().distance(p.getLocation()) > 10+(lvl2*2))
							{
								p.sendMessage(ChatColor.RED+"That casting location is too far away!");
								return;
							}
							
							p.getWorld().strikeLightning(block.getLocation());

							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new LGBolt(plugin, block.getLocation(), p ),10);
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new LGBolt(plugin, block.getRelative(-1, 0, 0).getLocation(), p ),30);
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new LGBolt(plugin, block.getRelative(-1, 0, 1).getLocation(), p ),40);
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new LGBolt(plugin, block.getRelative(0, 0, -1).getLocation(), p ),70);
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new LGBolt(plugin, block.getRelative(1, 0, -1).getLocation(), p ),80);
										    		
							removeInventoryItems(p.getInventory(), Material.ENDER_PEARL, 1);
							
				    	}
				    }
				    else
						p.sendMessage(ChatColor.RED+"You can only cast this spell every 10 seconds.");
		    }
		    else
		    		p.sendMessage(ChatColor.RED+"You require ender pearls to cast this spell.");
	    }
	    
	    
	    @Override
	    public void onPlayerChat(PlayerChatEvent event)
	    {
	    	
	    	String filtered_message = event.getMessage().toLowerCase();
	    	filtered_message = filtered_message.replace(" ", "");
	    	filtered_message = filtered_message.replace("!", "");
	    	filtered_message = filtered_message.replace("@", "");
	    	filtered_message = filtered_message.replace("#", "");
	    	filtered_message = filtered_message.replace("$", "s");
	    	filtered_message = filtered_message.replace("%", "");
	    	filtered_message = filtered_message.replace("%", "");
	    	filtered_message = filtered_message.replace("0", "o");
	    	
	    	boolean ignore = false;
	    	if (filtered_message.contains("fuck")){ ignore = true; }
	    	if (filtered_message.contains("fuk")){ ignore = true; }
	    	if (filtered_message.contains("fuc")){ ignore = true; }
	    	if (filtered_message.contains("fvc")){ ignore = true; }
	    	if (filtered_message.contains("nigg")){ ignore = true; }
	    	if (filtered_message.contains("boob")){ ignore = true; }
	    	if (filtered_message.contains("penis")){ ignore = true; }
	    	if (filtered_message.contains("sex")){ ignore = true; }
	    	if (filtered_message.contains("whore")){ ignore = true; }
	    	if (filtered_message.contains("bitch")){ ignore = true; }
	    	if (filtered_message.contains("cock")){ ignore = true; }
	    	if (filtered_message.contains("cunt")){ ignore = true; }
	    	if (filtered_message.contains("vagina")){ ignore = true; }
	    	if (filtered_message.contains("puss")){ ignore = true; }
	    	if (filtered_message.contains("mastur")){ ignore = true; }
	    	if (filtered_message.contains("hooters")){ ignore = true; }
	    	if (filtered_message.contains("viagiana")){ ignore = true; }
	    	if (filtered_message.contains("butthole")){ ignore = true; }
	    	if (filtered_message.contains("buthole")){ ignore = true; }
	    	
	    	if (ignore)
	    	{
	    		event.setCancelled(true);
	    		event.getPlayer().sendMessage(ChatColor.RED+"Your message was not sent as it contains inappropriate language.");
	    		
	    		System.out.println(event.getMessage());
	    		
	    		Bukkit.broadcastMessage(event.getPlayer().getName()+" was kicked for inappropriate language.");
	    		event.getPlayer().kickPlayer("inappropriate language");
	    	}
	    	
	    	if (filtered_message.contains("u mad"))
	    	{
	    		event.setCancelled(true);
	    		
	    		Bukkit.broadcastMessage(event.getPlayer().getName()+" was kicked for trolling.");
	    		event.getPlayer().kickPlayer("trolling");
	    	}
	    	
	    	//event.setMessage(filtered_message);
	    	
	    	String last_msg = plugin.playerConfig(event.getPlayer()).getString("LastChat", "");
	    	String last_msg2 = plugin.playerConfig(event.getPlayer()).getString("LastLastChat", "");
	    	String last_msg3 = plugin.playerConfig(event.getPlayer()).getString("LastLastChatChat", "");
	    	
	    	if (last_msg3.equalsIgnoreCase(event.getMessage())&&last_msg2.equalsIgnoreCase(event.getMessage())&&last_msg.equalsIgnoreCase(event.getMessage()))
	    	{
	    		plugin.playerConfig(event.getPlayer()).setProperty("LastChat", "");
	    		plugin.playerConfig(event.getPlayer()).setProperty("LastLastChat", "");
	    		plugin.playerConfig(event.getPlayer()).setProperty("LastLastChatChat", "");
	    		Bukkit.broadcastMessage(event.getPlayer().getName()+" was kicked for spamming.");
	    		event.getPlayer().kickPlayer("Spamming");
	    	}
	    	
	    	if (last_msg2.equalsIgnoreCase(event.getMessage())&&last_msg.equalsIgnoreCase(event.getMessage()))
	    	{
	    		plugin.playerConfig(event.getPlayer()).setProperty("LastLastChatChat", event.getMessage());
	    	}
	    	
	    	if (last_msg.equalsIgnoreCase(event.getMessage()))
	    	{
	    		plugin.playerConfig(event.getPlayer()).setProperty("LastLastChat", event.getMessage());
	    	}
	    	
	    	plugin.playerConfig(event.getPlayer()).setProperty("LastChat", event.getMessage());
	    	
	    	
	    	
	    	
	    	//BOT CODE. codename SARAH
	    	
	    	String bm = event.getMessage().toLowerCase();
	    	
	    		
	    		boolean is_requesting = false;
	    		if (bm.contains("ask sarah"))
	    		{
	    			botReply("Just type \"help\" to ask me a question.", event.getPlayer());
	    			return;
	    		}
	    		
	    		if (bm.contains("sarah")||bm.startsWith("sarah ")||bm.startsWith("sarah, ")||bm.endsWith(" sarah")||bm.endsWith(", sarah"))
	    			is_requesting = true;
	    		
	    		bm = bm.replace("'", "");
	    		
	    		//Find the meanings of some words
	    		String requested_item = plugin.playerConfig(event.getPlayer()).getString("ChatBot.Action", "");
	    		plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "");
	    		
	    		String statement = "";
	    		String talking_about = "";
	    		
	    		String[] words = bm.split(" ");
	    		
	    		int annoyances = plugin.playerConfig(event.getPlayer()).getInt("ChatBot.Anger", 0);
	    		plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Anger", 0);
	    		
	    		int m;
	    		
	    		String pw = "";
	    		String ppw = "";
	    		String pppw = "";
	    		String ppppw = "";
	    		String pppppw = "";
	    		String ppppppw = "";
	    		
	    		int i;
	    		for (i=0; i < words.length; i++)
	    		{
	    			String wrd = words[i];
	    			if (wrd.equalsIgnoreCase("sarah"))
	    				continue;
	    			
	    			if (is_requesting)
	    			{
		    			if (ppw.contains("call") && pw.contains("me")|| ppw.contains("im") && pw.contains("called"))
		    			{
		    				requested_item = "name";
		    				
		    				//Desired name
		    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Name", wrd);
		    				botReply("Ok, ill call you <name> from now on.:Sure thing, <name>!", event.getPlayer());
		    			}
		    			else if (ppw.contains("how") && pw.contains("are")&& wrd.contains("you"))
		    			{
		    				requested_item = "feeling";
		    				botReply("Im alright ;):Im patiently waiting for some questions:I don't really have emotion:Pretty tired. Answering questions all day lol.", event.getPlayer());
		    			}
		    			else if (wrd.contains("is"))
		    			{
		    				talking_about = pw;
		    				requested_item = "learn";
		    				
		    			}
		    			else if (requested_item.equalsIgnoreCase("learn"))
		    			{
		    				statement = statement + " " + wrd;
		    			}
		    			else if (wrd.contains("hi")||wrd.contains("mornin")||wrd.contains("hey")||wrd.contains("hai")||wrd.contains("sup")||wrd.contains("hello"))
		    			{
		    				requested_item = "greet";
		    				botReply("Hi <name>!:Hey:Hey <name>:Hello", event.getPlayer());
		    			}
		    			else if (wrd.contains("im back"))
		    			{
		    				requested_item = "greet";
		    				botReply("Welcome back :)", event.getPlayer());
		    			}
		    			else if (wrd.contains("cya")||wrd.contains("bye")||wrd.contains("night")||wrd.contains("g2g"))
		    			{
		    				requested_item = "farewell";
		    				botReply("Cya <name>:Bye ;(:See you tomorrow, <name>:;*(", event.getPlayer());
		    			}
		    			else if ((pw.contains("marry")&&wrd.contains("me")))
		    			{
		    				requested_item = "marry";
		    				botReply("Im not allowed to marry anyone D;:I don't think my master would allow that.:Sry <name>, im in love with somebody else.:No, sorry.", event.getPlayer());
		    				
		    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "marry");
		    			}
		    			else if (((pw.contains("your")||pw.contains("ur")) &&(wrd.contains("stupid")||wrd.contains("dumb"))))
		    			{
		    				requested_item = "stupid";
		    				botReply("Sorry ;( I'm still fairly young.:ShadovvMoon and Bhsgoclub has only given me limited inteligence. Sorry ;(:Thats mean ;(:;(:Sry <name>. I can't help it.", event.getPlayer());
		    				
		    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "stupid");
		    			}
		    			else if (wrd.contains("shutup")||wrd.contains("stfu"))
		    			{
		    				requested_item = "shutup";
		    				botReply("Make me, jerk: You leave me alone, i leave you alone. Deal?:You can talk.:Sorry <name> ;(", event.getPlayer());
		    				
		    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "shutup");
		    			}
		    			else if (ppw.contains("who")&& pw.contains("am")&& wrd.contains("I"))
		    			{
		    				requested_item = "shutup";
		    				botReply("Your <name>, silly.:What kind of person doesn't even know their own name. Geez <name>.:<name>:You have asked me to call you <name>.:Hiah <name> ;)", event.getPlayer());
		    				
		    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "shutup");
		    			}
		    			
		    			//Add some useful things
	    			}
	    			
	    			
	    			
	    			
	    			if (wrd.contains("call")&& (pw.contains("gonna")||pw.contains("ganna")||pw.contains("gunna"))&&(ppw.contains("you")||ppw.contains("u"))&&pppw.contains("who"))
	    			{
	    				requested_item = "ghost busters";
	    				botReply("Ghost Busters!", event.getPlayer());
	    				
	    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "");
	    				break;
	    			}
	    			
	    			if (bm.contains("how much wood could a wood chuck chuck if a wood chuck could chuck wood"))
	    			{
	    				requested_item = "ghost busters";
	    				botReply("A woodchuck would chuck as much wood as a woodchuck could chuck if a woodchuck could chuck wood.", event.getPlayer());
	    				
	    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "");
	    				break;
	    			}
	    			
	    			
	    			if ((bm.contains("why")||(pw.contains("your")||pw.contains("ur"))&&wrd.contains("master")) && requested_item.equalsIgnoreCase("marry"))
	    			{
	    				requested_item = "master";
	    				botReply("ShadovvMoon:ShadovvMoon is my master:I was created by ShadovvMoon:Shadovv ;)", event.getPlayer());
	    				
	    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "master");
	    				break;
	    			}
	    			
	    			if (bm.contains("can") && bm.contains("i") && bm.contains("get") && bm.contains("items"))
	    			{
	    				requested_item = "items";
	    				botReply("Sorry, I'm not allowed to spawn items:Shadovv doesnt let me spawn items sry:I cant do that sorry ;(", event.getPlayer());
	    				
	    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "items");
	    				break;
	    			}
	    			
	    			
	    			if (wrd.contains("why") && requested_item.equalsIgnoreCase("items"))
	    			{
	    				requested_item = "tutorial";
	    				botReply("Because bhs hasnt given me permission to spawn items:Because bhs said so", event.getPlayer());
	    				
	    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "");
	    			}
	    			
	    			
	    			if (bm.contains("i") && bm.contains("portals") && bm.contains("get"))
	    			{
	    				requested_item = "donor";
	    				botReply("You get portals for donating ($1 for 2 portals). Go to bit.ly/A67CsN.:Only donors can get portals. Go to bit.ly/A67CsN", event.getPlayer());
	    				
	    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "");
	    				
	    				break;
	    			}
	    			
	    			if ((bm.contains("cant") && bm.contains("i")&& bm.contains("build"))||(bm.contains("cant") && bm.contains("break")&& bm.contains("blocks")))
	    			{
	    				requested_item = "nomad";
	    				if (!plugin.playerConfig(event.getPlayer()).getString("nomad", "").equals("true"))
	    					botReply("You are probably in a no-build zone silly.:Try going to a different location <name>:That zone is probably protected by a faction, or is part of the safezone. ", event.getPlayer());
	    				else
	    					botReply("You are a nomad, so you cant build until you have applied. Go to tinyurl.com/skycraft4:You're a nomad <name>. Apply at tinyurl.com/skycraft4 ;)", event.getPlayer());
	    				
	    				
	    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "");
	    				
	    				break;
	    			}
	    			
	    			
	    			
	    			
	    			if (bm.contains("door") && bm.contains("start") && bm.contains("how"))
	    			{
	    				requested_item = "tutorial";
	    				botReply("To progress to the training room, throw dirt on the pressure plate. You get dirt from the other side of the room by right clicking on a sign.", event.getPlayer());
	    				
	    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "");
	    				
	    				break;
	    			}
	    			
	    			else if (pw.contains("im")&&wrd.contains("new"))
	    			{
	    				requested_item = "";
	    				botReply("Welcome to Skycraft, <name>. If you need help, just ask me.", event.getPlayer());
	    			
	    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "");
	    			}
	    			
	    			
	    			
	    			if (wrd.contains("tutorial") && requested_item.equalsIgnoreCase("stuck"))
	    			{
	    				requested_item = "tutorial";
	    				botReply("To progress to the training room, throw dirt on the pressure plate. You get dirt from the other side of the room by right clicking on a sign.", event.getPlayer());
	    				
	    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "");
	    			}
	    			else if (wrd.contains("dungeon") && requested_item.equalsIgnoreCase("stuck"))
	    			{
	    				requested_item = "dungeon";
	    				botReply("The only way out of a dungeon is by finishing it or suiciding. Would you like me to kill you?", event.getPlayer());
	    				
	    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "suicide");
	    			}
	    			else if (wrd.contains("engineers") && requested_item.equalsIgnoreCase("stuck"))
	    			{
	    				requested_item = "dungeon";
	    				botReply("The only way out of that building is by suiciding. Would you like me to kill you?", event.getPlayer());
	    				
	    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "suicide");
	    			}
	    			else if (wrd.contains("noob") && requested_item.equalsIgnoreCase("stuck"))
	    			{
	    				requested_item = "noobtrap";
	    				botReply("The only way out of a noob trap is suicde. Would you like me to kill you?:Your going to have to suicide. Do you want me to kill you?", event.getPlayer());
	    				
	    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "suicide");
	    			}
	    			else if (requested_item.equalsIgnoreCase("stuck"))
	    			{
	    				requested_item = "unknown";
	    				botReply("I have no information on that location. Usually the only way to escape is by suiciding. Would you like me to kill you?:Idk where that is sry. I can kill you though?", event.getPlayer());
	    				
	    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "suicide");
	    			}
	    			
	    			
	    			else if ((wrd.contains("yes")||wrd.contains("ya")||wrd.contains("ye")||wrd.contains("mhmm")||wrd.contains("yup")||wrd.contains("ok")) && requested_item.equalsIgnoreCase("suicide"))
	    			{
	    				requested_item = "suicide";
	    				botReply("OK. This might hurt a little:This is gonna hurt a bit:You asked for it lol:Ill try to make this painless:OK ;(:Kk, here goes!:This should be painless ;):Ok <name>:You should wake up at your bed, revived and fresh ;):Alright ;(:;(", event.getPlayer());
	    				
	    				event.getPlayer().damage(100);
	    				event.getPlayer().setFireTicks(1000);
	    				
	    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "");
	    			}
	    			else if ((wrd.contains("no")||wrd.contains("na")) && requested_item.equalsIgnoreCase("suicide"))
	    			{
	    				requested_item = "suicide";
	    				botReply("OK. Let me know if you change your mind.:OK ;):Alright ;) Goodluck:Goodluck ;):Kk:ok:ok, ill be here if you change your mind.:Kk. Lemme know if you need anything else.", event.getPlayer());
	    				
	    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "");
	    			}
	    			
	    			
	    			if (pw.contains("im") &&( wrd.contains("stuck")|| wrd.contains("trap")) )
	    			{
	    				requested_item = "stuck";
	    				botReply("Where are you stuck?:How are you stuck?:Where are you stuck? ;(:What's your location?:That sucks. Where are you stuck?", event.getPlayer());
	    				
	    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "stuck");
	    				
	    				return;
	    			}
	    			else if (((pw.contains("help")&&wrd.contains("me"))||bm.equalsIgnoreCase("sarah"))||bm.equalsIgnoreCase("help"))
	    			{
	    				requested_item = "help";
	    				botReply("What's wrong?:What do you need help with?:What do you need?:What's up, <name>?:With what, <name>?:Yes <name>?:What?:Mmm?:Yes <name>?:I'm here to help <name>. What do you need?", event.getPlayer());
	    			
	    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "help");
	    			}
	    			else if (wrd.contains("nothing") && requested_item.equalsIgnoreCase("help"))
	    			{
	    				requested_item = "stuck";
	    				botReply("OK. Call if you need anything:No worries ;):*sigh* back to sleep.:Let me know if you need anything else.:ZzzzZzzzz:Ok:Kk:No worries <name>:Let me know if you need anything ;):Haha, alright ;)", event.getPlayer());
	    				
	    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "stuck");
	    			}
	    			else if ((wrd.contains("teleport")&&bm.contains("me"))||(wrd.contains("tp")&&bm.contains("me")))
	    			{
	    				requested_item = "teleport";
	    				botReply("Don't ask for teleports ;\\:Mods dont like being pestered.:Teleports are not given.:Don't nag mods for teleports ;(:Why do you need a teleport?", event.getPlayer());
	    			
	    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "");
	    			}
	    			else if (pw.contains("kill")&&wrd.contains("me"))
	    			{
	    				requested_item = "suicide";
	    				botReply("Are you sure?:You sure? It won't hurt a bit:You certain?:Um, are you sure?:You sure you want to die?:Can you confirm that for me please?", event.getPlayer());
	    			
	    				plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Action", "suicide");
	    			}
	    			
	    			ppppppw = ppppw;
	    			pppppw = ppppw;
	    			ppppw = pppw;
	    			
	    			pppw = ppw;
	    			ppw = pw;
	    			pw = wrd;
	    			
	    		}
	    		
	    		
	    		if (is_requesting && requested_item.equalsIgnoreCase(""))
	    		{
	    			
	    			
	    			if (annoyances == 2)
	    			{
	    				botReply("<name>, Ask a proper question or I'll kick you.", event.getPlayer());
	    			}
	    			else if (annoyances > 3)
	    			{
	    				event.getPlayer().kickPlayer("Kicked for annoying Sarah");
	    			}
	    		
		    		plugin.playerConfig(event.getPlayer()).setProperty("ChatBot.Anger", annoyances+1);
	    			botReply("<name>, I dont understand what you mean.:What?:Please clarify that statement", event.getPlayer());
	    		}
	    		else if (requested_item.equalsIgnoreCase("learn"))
	    		{
	    			botReply("I'll remember that "+talking_about+" is"+statement+".", event.getPlayer());
	    		}
	    		
	    		plugin.updateName(event.getPlayer());
	    		
	    		
	    		String channel = plugin.playerConfig(event.getPlayer()).getString("CHATCHANNEL", "PUBLIC");
	    		if (channel.equalsIgnoreCase("public"))
	    		{
	    			
	    		}
	    		else if (channel.equalsIgnoreCase("private"))
	    		{
	    			event.getPlayer().sendMessage(ChatColor.YELLOW+"[t] " + ChatColor.WHITE+event.getPlayer().getDisplayName()+ChatColor.YELLOW+": " + event.getMessage());
				      
	    			event.setCancelled(true);
	    			
	    			//send the message to all the players ;)
	    			String houseID = plugin.getSettle().getString(event.getPlayer().getName()+".houseID", "");
	    			java.util.List<String> players = plugin.getSettle().getKeys("Settlements."+houseID+".Players");
		        	if (players != null)
		    		{
		        		for (i=0; i < players.size(); i++)
		        		{
		        			String name = players.get(i);
		        			
		        			
		        			Player p = getPlayer(null, name);
		        			p.sendMessage(ChatColor.YELLOW+"[t] " + ChatColor.WHITE+event.getPlayer().getDisplayName()+ChatColor.YELLOW+": " + event.getMessage());
		        		}
		    		}
		        	
		        	
	    		}
	    	}
	    
		 public Player getPlayer(World w, String name) {

				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
					if (player.getName().equalsIgnoreCase(name))
						return player;
				}

				return null;

			}
	    public void botReply(String message, Player p)
	    {
	    	p.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new LGBot(plugin, message, p), plugin.random_num(20, 10));
	    }
	    
	  
	    @Override
	    public void onPlayerDropItem(PlayerDropItemEvent event)
	    {
	    	if (event.getItemDrop().getItemStack().getTypeId()==36)
	    	{
	    		
	    		Player p = event.getPlayer();
	    		String inven_name = "Backpack";
	    		plugin.dumpChest(p, event.getItemDrop().getLocation());
	    		p.sendMessage(ChatColor.RED+"You items fall out of your backpack as it hits the ground.");
	    		
	    	}
	    }
	    
	    @Override
	    public void onPlayerPickupItem(PlayerPickupItemEvent event)
	    {
	    	int amount = 0;
	    	if (event.getItem().getItemStack().getTypeId()==36)
	    	{
	    		ItemStack[] itms = event.getPlayer().getInventory().getContents();
	    		for (int i =0; i < itms.length; i++)
	    		{
	    			ItemStack s = itms[i];
	    			if (s!=null)
	    			{
		    			if (s.getTypeId() == 36)
		    			{
		    				amount++;
	
		    				if (amount > 1)
		    		    	{
		    		    		event.setCancelled(true);
		    					break;
		    		    	}
		    			}
	    			}
	    		}
	    	}
	    	
	    	
	    }
	    
	    @Override
	    public void onPlayerInteract(PlayerInteractEvent event)
	    {

	        Player p = event.getPlayer();
	        
	        if ((event.getAction() == Action.RIGHT_CLICK_AIR||event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getPlayer().getItemInHand().getTypeId()==36)
	        {
	        	//Right clicked a bed?
	        	plugin.openBackpack(p);
	        }	
	        
	        if (event.hasBlock() && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.BED)
	        {
	        	//Right clicked a bed?
	        	CraftPlayer e = (CraftPlayer)p;
	        }	
	        else if ( (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && (p.getItemInHand().getType()==Material.REDSTONE_WIRE))
	        {
	        	//Right clicked a bed?
	        	
	        	String binoculars = plugin.playerConfig(p).getString("Binoculars", "");
	        	if (binoculars.equalsIgnoreCase("true"))
	        	{	        		
	        		int cancel = plugin.playerConfig(p).getInt("BScheduler", -1);
	        		Bukkit.getServer().getScheduler().cancelTask(cancel);
	        		
	        		plugin.playerConfig(p).setProperty( "Binoculars", "false");
	        	}
	        	else
	        	{
		        	LGBino mana_bar = new LGBino(p, plugin);
					int timerID = p.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, mana_bar, 0, 5);
					plugin.playerConfig(p).setProperty( "BScheduler", timerID);
					plugin.playerConfig(p).setProperty( "Binoculars", "true");
	        	}
	        }
	        else if ( (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && (p.getItemInHand().getTypeId()==2262))
	        {
	        	//Right clicked a bed?
	        	plugin.mirror(event.getPlayer(),0);
	
	        }
	        else if ( (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && (p.getItemInHand().getTypeId()==2258))
	        {
	        	//Right clicked a bed?
	        	plugin.mirror(event.getPlayer(),1);
	
	        }
	        else if ( (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && (p.getItemInHand().getTypeId()==2264))
	        {
	        	//Right clicked a bed?
	        	plugin.mirror(event.getPlayer(),2);
	
	        }
	        else if (event.hasBlock() && event.getAction() == Action.LEFT_CLICK_BLOCK && plugin.playerConfig(p).getString("DisplayCoords","").equalsIgnoreCase("yes"))
	        {
	        	System.out.println("Displaying coordinates.");
	        	plugin.playerConfig(p).setProperty("DisplayCoords", "NO");
		        	String task_id = plugin.playerConfig(p).getString("Task_name", "");
		        	
		        	Block block = event.getClickedBlock();
		        	String coords = String.format("%d %d %d", (int)block.getLocation().getX(), (int)block.getLocation().getY(), (int)block.getLocation().getZ());
		        	
					List<String> tasks = plugin.getTasks().getKeys("Tasks");
					
		        	int g; boolean is_task = false;
		        	
		        	if (tasks != null)
		        	{
			        	for (g=0; g < tasks.size(); g++)
					    {
			        		String coords2 = tasks.get(g);
			        		String task_od = plugin.getTasks().getString("Tasks."+coords2+".set", "");
					    	 
					    	 if (task_od.equalsIgnoreCase(task_id)||coords2.equalsIgnoreCase(coords))
					    	 {
					    		 is_task = true;
					    		 break;
					    	 }
					    }
		        	}
		        	
		        	Player player = event.getPlayer();
		        	
		        	if (is_task)
		        	{
		        		player.sendMessage(ChatColor.RED+"A task with that ID or coordinates already exists");
		        		return;
		        	}

		        	plugin.getTasks().setProperty("Tasks."+coords+".set", task_id);
		        	player.sendMessage(ChatColor.YELLOW+"Task "+task_id+" created!");

		        	return;
	        }
	        else if (event.hasBlock() && event.getAction() == Action.LEFT_CLICK_BLOCK && plugin.playerConfig(p).getString("ShopDisplayCoords","").equalsIgnoreCase("yes"))
	        {

			
	        		plugin.playerConfig(p).setProperty("ShopDisplayCoords", "NO");
		        	
		        	
		        	String selling = plugin.playerConfig(p).getString("Selling", "");
		        	String samount = plugin.playerConfig(p).getString("SAmount", "");
		        	String buying = plugin.playerConfig(p).getString("Buying", "");
		        	String bamount = plugin.playerConfig(p).getString("BAmount", "");

		        	
		        	Block block = event.getClickedBlock();
		        	String coords = String.format("%d %d %d", (int)block.getLocation().getX(), (int)block.getLocation().getY(), (int)block.getLocation().getZ());
		        	
					List<String> tasks = plugin.getTasks().getKeys("Shops");
					int g;
					boolean is_task = false;
					
					if (tasks!=null)
					{
			        	
			        	for (g=0; g < tasks.size(); g++)
					    {
			        		String coords2 = tasks.get(g);
					    	 if (coords2.equalsIgnoreCase(coords))
					    	 {
					    		 String selling2 = plugin.getTasks().getString("Shops."+coords2+".Selling", "");
					    		 if (!selling2.equalsIgnoreCase(""))
					    		 {
					    			is_task = true;
					    			break;
					    		 }
					    	 }
					    }
					}
		        	
		        	Player player = event.getPlayer();
		        	
		        	if (is_task)
		        	{
		        		player.sendMessage(ChatColor.RED+"A shop with those coordinates already exists! ("+coords+")");
		        		return;
		        	}

		        	plugin.getTasks().setProperty("Shops."+coords+".Selling", selling);
		        	plugin.getTasks().setProperty("Shops."+coords+".SAmount", samount);
		        	plugin.getTasks().setProperty("Shops."+coords+".Buying", buying);
		        	plugin.getTasks().setProperty("Shops."+coords+".BAmount", bamount);
		        	
		        	//player.sendMessage(ChatColor.YELLOW+"Task "+task_id+" created!");

		        	return;
	        }
	        Material t =  p.getItemInHand().getType();
	        
		    
		    Material sa = p.getItemInHand().getType();
		    if (sa==Material.STONE_PICKAXE||sa==Material.IRON_PICKAXE||sa==Material.DIAMOND_PICKAXE)
		    {
		    	//Special!
		    	if (event.getAction()==Action.LEFT_CLICK_BLOCK)
		    	{
		    		int mining_lvl = (plugin.skillLevel(p, "Mining"));
		    		int dig_increase = mining_lvl/4;
		    		
		    		if (mining_lvl % 4 >=0.5)
		    		{
		    			dig_increase-=1;
		    		}
		    		
		    		if (mining_lvl >= 4)
		    		{
		    			plugin.effectPlayer(p, "digup", dig_increase, 3);
		    		}
		    	}
		    }
		    
		    if (sa==Material.STONE_SPADE||sa==Material.IRON_SPADE||sa==Material.DIAMOND_SPADE)
		    {
		    	//Special!
		    	if (event.getAction()==Action.LEFT_CLICK_BLOCK)
		    	{
		    		
		    		int mining_lvl = (plugin.skillLevel(p, "Shovelling"));
		    		int dig_increase = mining_lvl/4;
		    		
		    		if (mining_lvl % 4 >=0.5)
		    		{
		    			dig_increase-=1;
		    		}
		    		
		    		if (mining_lvl >= 4)
		    		{
		    			plugin.effectPlayer(p, "digup", dig_increase, 3);
		    		}
		    	}
		    }
		    
		    if (sa==Material.STONE_AXE||sa==Material.IRON_AXE||sa==Material.DIAMOND_AXE)
		    {
		    	//Special!
		    	if (event.getAction()==Action.LEFT_CLICK_BLOCK)
		    	{
		    		int mining_lvl = (plugin.skillLevel(p, "Woodcutting"));
		    		int dig_increase = mining_lvl/4;
		    		
		    		if (mining_lvl % 4 >=0.5)
		    		{
		    			dig_increase-=1;
		    		}
		    		
		    		if (mining_lvl >= 4)
		    		{
		    			plugin.effectPlayer(p, "digup", dig_increase, 3);
		    		}
		    	}
		    }
		    	
    	  	
	        
	        if (event.hasBlock() && event.getAction() == Action.LEFT_CLICK_BLOCK && event.getClickedBlock().getRelative(0, 1,0).getType()==Material.FIRE)
	        {
	        	System.out.println("Putting out fire");
	        	event.setCancelled(true);
	        	return;
	        }
	        
	        if (event.hasBlock() && p.getItemInHand().getType() == Material.DIAMOND_AXE && plugin.skillLevel(p, "Woodcutting") >= 10 && event.getClickedBlock().getType() == Material.LOG && event.getAction() == Action.RIGHT_CLICK_BLOCK)
	        {
	        	//Cut down the entire tree
	        	cutDownTree(event.getClickedBlock(), p, 22);
	        }
	        else if (event.hasBlock() && (event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN)  && event.getAction() == Action.LEFT_CLICK_BLOCK)
	        {
	        	System.out.println("Clicked sign");
	        	Block block = event.getClickedBlock();
	        	
	        	Player player = event.getPlayer();
	        	Sign sign = (Sign)block.getState();
	        	
	        	List<String> tasks = plugin.getTasks().getKeys("Tasks");
	        	String coords = String.format("%d %d %d", (int)block.getLocation().getX(), (int)block.getLocation().getY(), (int)block.getLocation().getZ());
	        	
	        	int g; boolean is_task = false;
	        	if (tasks != null)
	        	{
		        	for (g=0; g < tasks.size(); g++)
				    {
				    	 String coords2 = tasks.get(g);
				    	 if (coords2.equalsIgnoreCase(coords))
				    	 {
				    		 is_task = true;
				    		 break;
				    	 }
				    }
	        	}
	        	if (is_task)
	        	{
	        		
	        		player.sendMessage(ChatColor.YELLOW+"Task: "+plugin.getTasks().getString("Tasks."+coords+".set", "")+". Right click to accept.");
	        	}
	        }
	        else if (event.hasBlock() && (event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN)  && event.getAction() == Action.RIGHT_CLICK_BLOCK)
	        {
	        	
	        		
	        		Sign selected_sign = (Sign) event.getClickedBlock().getState();
	        		String main = selected_sign.getLine(1);
	        		Player player = event.getPlayer();
	        		
	        		String settlement_name = plugin.currentSettlement(event.getClickedBlock().getLocation(), player);
	        	
	        		if ( (!(settlement_name.equalsIgnoreCase(""))) && main.equalsIgnoreCase(ChatColor.YELLOW+"[Leader]")||main.equalsIgnoreCase(ChatColor.GREEN+"[Leader]"))
	        		{
	        			/*String pll = selected_sign.getLine(2)+selected_sign.getLine(3);
	        			if (pll.equalsIgnoreCase(player.getName()))
	        			{
	        				String houseID = plugin.getSettle().getString(player.getName()+".houseID", "");
	        	    		if (houseID.equalsIgnoreCase(settlement_name) || main.equalsIgnoreCase(ChatColor.GREEN+"[Leader]"))
	        	    		{
	        	    			player.sendMessage(ChatColor.YELLOW+"You are now a leader of this this settlement!");
	        	    			
	        	    			plugin.getSettle().setProperty("Settlements."+settlement_name+".Players."+player.getName(), "Leader");

	        	    			//sendGlobalBlockChange(event.getClickedBlock().getLocation(), event.getClickedBlock().getType(), event.getClickedBlock().getData());
    	        				event.getClickedBlock().setType(Material.AIR);
	        	    		}
	        			}*/
	        		}
	        		else if ( (!(settlement_name.equalsIgnoreCase(""))) && main.equalsIgnoreCase(ChatColor.RED+"[Member]")||main.equalsIgnoreCase(ChatColor.GREEN+"[Member]"))
	        		{
	        			/*String pll = selected_sign.getLine(2)+selected_sign.getLine(3);
	        			if (pll.equalsIgnoreCase(player.getName()))
	        			{
	        				String houseID = plugin.getSettle().getString(player.getName()+".houseID", "");
	        	    		if (houseID.equalsIgnoreCase("") || main.equalsIgnoreCase(ChatColor.GREEN+"[Member]"))
	        	    		{
	        	    			String center_loc = plugin.getSettle().getString("Settlements."+settlement_name+".Middle", "");
	    	    	    		
	    	        			if (!center_loc.equalsIgnoreCase(""))
	    	        			{
	    		    				player.sendMessage(ChatColor.YELLOW+"You have joined this settlement!");
	    		    				
	    		    				plugin.getSettle().setProperty("Settlements."+settlement_name+".Players."+player.getName(), "Member");
	    		    				plugin.getSettle().setProperty(player.getName()+".houseID", settlement_name);

	    	        				String[] crds = center_loc.split(" ");
	    	        				Location settlement_loc = new Location(player.getWorld(), Integer.valueOf(crds[0]), Integer.valueOf(crds[1]), Integer.valueOf(crds[2]));
	    	    	    			
	    	        				Block b = player.getWorld().getBlockAt(settlement_loc);
	    	        				Sign the_sign = (Sign)b.getState();
	    	        				
	    	        				String third_line = the_sign.getLine(3);
	    	        				String[] cmd = third_line.split("/");
	    	        				
	    	        				int members = Integer.valueOf(cmd[0]);
	    	        				int required = Integer.valueOf(cmd[1]);
	    	        				
	    	        				members+=1;
	    	        				
	    	        				if (members == required)
	    	        				{
	    	        					
	    	        					String type = plugin.getSettle().getString("Settlements."+settlement_name+".Type", "");

	    	        					if (type.equalsIgnoreCase("[Settlement]"))
	    	        						Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+settlement_name+" has setup a new settlement!");
	    	        					
	    	        	        		else if (type.equalsIgnoreCase("[Colony]"))
	    	        	        			Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+settlement_name+" has setup a new colony!");
	    	        					
	    	        	        		else if (type.equalsIgnoreCase("[Village]"))
	    	        	        			Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+settlement_name+" has setup a new village! A simple palisade wall has been setup around its perimeter.");
	    	        					
	    	        	        		else if (type.equalsIgnoreCase("[Town]"))
	    	        	        			Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+settlement_name+" has setup a new town! A simple palisade wall has been setup around its perimeter.");
	    	        					
	    	        					
	    	        					//Create the palisade
	    	        					
	    	        					
	    	        					the_sign.setLine(3, String.format("%d/%d", required, required));
	    	        					
	    	        					int size = 0;
	    	        	    			
	    	        	        		if (type.equalsIgnoreCase("[Settlement]"))
	    	        	        			size = 5;
	    	        	        		else if (type.equalsIgnoreCase("[Colony]"))
	    	        	            		size = 22;
	    	        	        		else if (type.equalsIgnoreCase("[Village]"))
	    	        	            		size = 37;
	    	        	        		else if (type.equalsIgnoreCase("[Town]"))
	    	        	            		size = 72;
	    	        					
	    	        		    		Block bound1= b.getRelative(size, 0, -size);
	    	        		    		Block bound2= b.getRelative(size, 0, size);
	    	        		    		Block bound3= b.getRelative(-size, 0, -size);
	    	        		    		Block bound4= b.getRelative(-size, 0, size);
	    	        		    		
	    	        		    		
	    	        		    		if (type.equalsIgnoreCase("[Village]")||type.equalsIgnoreCase("[Town]"))
	    	        		    		{
	    	        	        			//CREATE THE PALISADE!
	    	        		    			Block current_block = bound2;
	    	        		    			
	    	        		    			int i;
	    	        		    			for (i=0; i < size*2; i++)
	    	        		    			{
	    	        		    				current_block.setType(Material.WOOD);
	    	        		    				current_block.getRelative(0, 1, 0).setType(Material.WOOD);
	    	        		    				current_block.getRelative(0, 2, 0).setType(Material.WOOD);
	    	        		    				current_block.getRelative(0, 3, 0).setType(Material.WOOD);

	    	        		    				current_block = current_block.getRelative(-1, 0, 0);
	    	        		    				if (current_block == bound1 ||current_block == bound2||current_block == bound3||current_block == bound4)
	    	        		    					break;
	    	        		    			}
	    	        		    			
	    	        		    			current_block = bound2;
	    	        		    			for (i=0; i < size*2; i++)
	    	        		    			{
	    	        		    				current_block.setType(Material.WOOD);
	    	        		    				current_block.getRelative(0, 1, 0).setType(Material.WOOD);
	    	        		    				current_block.getRelative(0, 2, 0).setType(Material.WOOD);
	    	        		    				current_block.getRelative(0, 3, 0).setType(Material.WOOD);

	    	        		    				current_block = current_block.getRelative(0, 0, -1);
	    	        		    				if (current_block == bound1 ||current_block == bound2||current_block == bound3||current_block == bound4)
	    	        		    					break;
	    	        		    			}
	    	        		    			
	    	        		    			current_block = bound3;
	    	        		    			for (i=0; i < size*2; i++)
	    	        		    			{
	    	        		    				current_block.setType(Material.WOOD);
	    	        		    				current_block.getRelative(0, 1, 0).setType(Material.WOOD);
	    	        		    				current_block.getRelative(0, 2, 0).setType(Material.WOOD);
	    	        		    				current_block.getRelative(0, 3, 0).setType(Material.WOOD);

	    	        		    				current_block = current_block.getRelative(1, 0, 0);
	    	        		    				if (current_block == bound1 ||current_block == bound2||current_block == bound3||current_block == bound4)
	    	        		    					break;
	    	        		    			}
	    	        		    			
	    	        		    			current_block = bound3;
	    	        		    			for (i=0; i < size*2; i++)
	    	        		    			{
	    	        		    				current_block.setType(Material.WOOD);
	    	        		    				current_block.getRelative(0, 1, 0).setType(Material.WOOD);
	    	        		    				current_block.getRelative(0, 2, 0).setType(Material.WOOD);
	    	        		    				current_block.getRelative(0, 3, 0).setType(Material.WOOD);

	    	        		    				current_block = current_block.getRelative(0, 0, 1);
	    	        		    				if (current_block == bound1 ||current_block == bound2||current_block == bound3||current_block == bound4)
	    	        		    					break;
	    	        		    			}
	    	        		    			
	    	        		    			
	    	        		    			
	    	        		    		}
	    	        		    		
	    	        					bound1.setType(Material.GLOWSTONE);
	    	        		    		bound2.setType(Material.GLOWSTONE);
	    	        		    		bound3.setType(Material.GLOWSTONE);
	    	        		    		bound4.setType(Material.GLOWSTONE);
	    	        		    		
	    	        		    		BlockVector pt = new BlockVector(bound3.getLocation().getX(), bound3.getLocation().getY()-30, bound3.getLocation().getZ());
	    	        		    		BlockVector pt2 = new BlockVector(bound2.getLocation().getX(), bound2.getLocation().getY()+30, bound2.getLocation().getZ());
	    	        		
	    	        		    		ProtectedCuboidRegion new_protection = new ProtectedCuboidRegion(settlement_name,pt,pt2);
	    	        		    		
	    	        		    		DefaultDomain domain = new DefaultDomain();
	    	        		    		domain.addPlayer(player.getName());

	    	        		    		DefaultDomain mems = new DefaultDomain();

	    	        		    		new_protection.setFlag(new BooleanFlag("use"), true);
	    	        		    		new_protection.setFlag(new BooleanFlag("chest-access"), true);
	
	    	        		    		//new_protection.setFlag(flag, val)
	    	        		    		
	    	        		    		java.util.List<String> players = plugin.getSettle().getKeys("Settlements."+settlement_name+".Players");
	    	        		        	if (players != null)
	    	        		    		{
	    	        		        		int i;
	    	        		        		for (i=0; i < players.size(); i++)
	    	        		        		{
	    	        		        			String name = players.get(i);
	    	        		        			mems.addPlayer(name);
	    	        		        		}
	    	        		    		}
	    	        		    		
	    	        		        	new_protection.setMembers(mems);
	    	        		    		new_protection.setOwners(domain);
	    	        		    		
	    	        		    		WorldGuardPlugin worldGuard = getWorldGuard();
	    	        		    		
	    	        		    		RegionManager regionManager = worldGuard.getRegionManager(player.getWorld());
	    	        		    		regionManager.addRegion(new_protection);
	    	        				}
	    	        				else
	    	        				{
	    	        					if (members > required)
	    	        					{
	    	        						//Add the new member to the build perms
	    	        						WorldGuardPlugin worldGuard = getWorldGuard();
	    		        		    		
	    		        		    		RegionManager regionManager = worldGuard.getRegionManager(player.getWorld());
	    		        		    		ProtectedRegion region = (ProtectedRegion) regionManager.getRegion(settlement_name);

	    		        		    		DefaultDomain mems = region.getMembers();
	    		        		    		mems.addPlayer(player.getName());
	    		        		    		
	    		        		    		region.setMembers(mems);
	    		        		    		
	    	        					}
	    	        						
	    	        					the_sign.setLine(3, String.format("%d/%d", members, required));
	    	        			
	    	        				}
	    	        				
	    	        				sendGlobalBlockChange(event.getClickedBlock().getLocation(), event.getClickedBlock().getType(), event.getClickedBlock().getData());
	    	        				event.getClickedBlock().setType(Material.AIR);
	    	        			}
	    	        			else
	    	        			{
	    	        				player.sendMessage(ChatColor.RED+"The settlement sign appears to be missing.");
	    	        			}
	        	    		}
	        	    		else
	        	    		{
	        	    			player.sendMessage(ChatColor.RED+"You are already part of a settlement! Consult with the other members of your settlement about destroying the existing one.");
	        	    		}
	        			}*/
	        		}
	        		
	            
	        	
	        	
	        	System.out.println(event.getPlayer().getName() + "right clicked a sign.");
	        	Block block = event.getClickedBlock();
	        	
	        	Sign sign = (Sign)block.getState();
	        	
	        	List<String> tasks = plugin.getTasks().getKeys("Tasks");
	        	String coords = String.format("%d %d %d", (int)block.getLocation().getX(), (int)block.getLocation().getY(), (int)block.getLocation().getZ());
	        	
	        	
	        	int g; boolean is_task = false;
	        	
	        	if (tasks != null)
	        	{
	        	for (g=0; g < tasks.size(); g++)
			    {
			    	 String coords2 = tasks.get(g);
			    	 if (coords2.equalsIgnoreCase(coords))
			    	 {
			    		 is_task = true;
			    		 break;
			    	 }
			    }
	        	}
	        	
	        	if (is_task)
	        	{
	        		String set = plugin.getTasks().getString("Tasks."+coords+".set", "");
	        		String player_task = plugin.playerConfig(player).getString(set, "");
	        		
	        		if (player_task.equalsIgnoreCase("finished"))
	        		{
	        			player.sendMessage(ChatColor.RED+"You have already completed this task.");
	        			return;
	        		}
	        		
	        		String player_task2 = plugin.playerConfig(player).getString(set+".Started", "");
	        		
	        		if (player_task2.equalsIgnoreCase("Yes"))
	        		{
	        			player.sendMessage(ChatColor.RED+"You have already accepted this task.");
	        			return;
	        		}
	        		
	        		boolean meets_requirements = true;
	        		List<String> required = plugin.getTasks().getKeys("Tasks."+coords+".Requires");
	            	if (required != null)
	            	{
		        		int i;
		        		for (i=0; i < required.size(); i++)
		        		{
		        			String task_name = required.get(i);
		        			String player_task23 = plugin.playerConfig(player).getString(task_name, "");
			        		
		        			if (!player_task23.equalsIgnoreCase("finished"))
			        		{
		        				player.sendMessage(ChatColor.RED+"You need to complete " + task_name + " before you can attempt this task.");

		        				return;
			        		}
		        		}
	            	}
	        		//plugin.getConfig().setProperty("Tasks."+coords2+".Requires."+type, "Yes");
	        		
	        		
	        		
	        		player.sendMessage(ChatColor.YELLOW+"Your task is to " + plugin.getTasks().getString("Tasks."+coords+".goal"));

	        		List<String> equipment = plugin.getTasks().getKeys("Tasks."+coords+".Equipment");
	            	if (equipment != null)
	            	{
		            	int s;
		            	for (s=0; s < equipment.size(); s++)
		                {
		            		String equipment_id = equipment.get(s);
		                	
		                	int amount = plugin.getTasks().getInt("Tasks."+coords+".Equipment."+equipment_id, 0);
		                	
		                	
		                	
		                	if (equipment_id.substring(1).equalsIgnoreCase("LWC"))
				        	{
				        		int i;
				        		for (i=0; i < Integer.valueOf(amount); i++)
				        		{
				        			plugin.addLWC(player);
				        		}
				        		
				        		player.sendMessage(ChatColor.YELLOW+"You have received "+amount+" new protection.");
				        	}
		                	else
		                	{
		                	
		                		int equip_id = Integer.parseInt(equipment_id.substring(1));
			                	
		                	
			                	ItemStack stack = new ItemStack(equip_id, amount);
			                	player.getInventory().addItem(stack);
			                	
			                	player.sendMessage(ChatColor.GOLD+"You have gained " + String.valueOf(amount) + " " + stack.getType().toString());
			                	
			                	player.updateInventory();
		                	}
		                }
	            	}
	            	
	            	String collectnum = plugin.getTasks().getString("Tasks."+coords+".Aims.Collect.Number", "0");
                	String num = plugin.getTasks().getString("Tasks."+coords+".Aims.Kill.Number", "0");
                	
                	plugin.playerConfig(player).setProperty(set+".Kill", num);
                	plugin.playerConfig(player).setProperty(set+".Collect", collectnum);
                	plugin.playerConfig(player).setProperty(set+".Started", "Yes");
	        	}
	        	else
	        	{
	        		List<String> shops = plugin.getTasks().getKeys("Shops");
		        	coords = String.format("%d %d %d", (int)block.getLocation().getX(), (int)block.getLocation().getY(), (int)block.getLocation().getZ());
		        	
		        	is_task = false;
		        	if (shops != null)
		        	{
			        	for (g=0; g < shops.size(); g++)
					    {
					    	 String coords2 = shops.get(g);
					    	 if (coords2.equalsIgnoreCase(coords))
					    	 {
					    		 is_task = true;
					    		 break;
					    	 }
					    }
		        	}
		        	
		        	if (is_task)
		        	{
		        	
		        	String selling = plugin.getTasks().getString("Shops."+coords+".Selling", "");
		        	String samount = plugin.getTasks().getString("Shops."+coords+".SAmount", "");
		        	String buying = plugin.getTasks().getString("Shops."+coords+".Buying", "");
		        	String bamount = plugin.getTasks().getString("Shops."+coords+".BAmount", "");
		        	
		        	if (selling.equalsIgnoreCase("FreeMinecart"))
		        	{

		        		long current_time = System.currentTimeMillis();
		    	    	long previous_time = Long.valueOf(plugin.playerConfig(p).getString("lastMinecart", String.valueOf(0)));
		    	    		
		    	    	long time_since = current_time-previous_time;
		
		    			if (time_since > 60000)
		    			{
		    
		        			if (player.getInventory().firstEmpty() != -1)
			        		{
		        				plugin.playerConfig(p).setProperty("lastMinecart", String.valueOf(current_time));
			        			player.sendMessage(ChatColor.YELLOW+"Enjoy your new minecart.");

				        		ItemStack newitems = new ItemStack(328, 1);
				        		
				        		
				        		player.getInventory().addItem(newitems);
				        		player.updateInventory();
			        		}
		        			
		        		}
		        		else
		        		{
		        			player.sendMessage(ChatColor.RED+"You can only take one minecart per minute!");
		        			
		        		}
		

		        		return;
		        		
		        	}
		        	
		       
		        	
		        	
		         	
		        	if (buying == null || buying.equalsIgnoreCase(""))
		        	{
		        		player.sendMessage(ChatColor.RED+"There is a problem with this shop.");
		        		return;
		        	}
		        	else if (bamount == null || bamount.equalsIgnoreCase(""))
			        {
		        		player.sendMessage(ChatColor.RED+"There is a problem with this shop.");
		        		return;
			        }
		        	
		        	
		        	
		        	
		        	
		        	
/*
				
				
				
				
				
*/					boolean contains_enough = false;
		        	if (buying.equalsIgnoreCase("c"))
		        	{
		        		
		        		if (Integer.valueOf(bamount) <= 0)
		        		{
			        		long current_time = System.currentTimeMillis();
			    	    	long previous_time = Long.valueOf(plugin.playerConfig(p).getString("lastShopping", String.valueOf(0)));
			    	    		
			    	    	long time_since = current_time-previous_time;
			
			    	    	
			    			if (time_since > 60000)
			    			{
			    				plugin.playerConfig(p).setProperty("lastShopping", String.valueOf(current_time));
			    			}
			        		else
			        		{
			        			player.sendMessage(ChatColor.RED+"You can only use this sign every 30 seconds.");
			        			return;
			        		}
		        		}
		        		
		        		
		        			if (plugin.totalMoney(player) >= Integer.valueOf(bamount))
		        			{
		        				contains_enough = true;
		        			}
		        			else
		        			{
		        				contains_enough = false;
		        			}
		        	}
		        	else if (player.getInventory().contains(Integer.valueOf(buying), Integer.valueOf(bamount)))
		        	{
		        		contains_enough = true;
		        	}
		        	
		        	if (contains_enough)
		        	{
			        	if (selling.equalsIgnoreCase("LWC"))
			        	{
			        		int i;
			        		for (i=0; i < Integer.valueOf(samount); i++)
			        		{
			        			plugin.addLWC(player);
			        		}
			        		removeInventoryItems(p.getInventory(), Integer.valueOf(buying), Integer.valueOf(bamount));
			        		player.updateInventory();
			        		
			        		player.sendMessage(ChatColor.YELLOW+"You have received "+samount+" new protection.");
			        	}
			        	else if (selling.equalsIgnoreCase("Telepad"))
			        	{
			        		int i;
			        		for (i=0; i < Integer.valueOf(samount); i++)
			        		{
			        			int enterances = plugin.playerConfig(p).getInt("telepadExits", 0);
			    				enterances++;
			    				plugin.playerConfig(p).setProperty("telepadExits", enterances);

			    				enterances = plugin.playerConfig(p).getInt("telepadEnterances", 0);
			    				enterances++;
			    				plugin.playerConfig(p).setProperty("telepadEnterances", enterances);
			        		}
			        		removeInventoryItems(p.getInventory(), Integer.valueOf(buying), Integer.valueOf(bamount));
			        		player.updateInventory();
			        		
			        		player.sendMessage(ChatColor.YELLOW+"You have received "+samount+" new telepads. You can now create another entry and another exit.");
			        	}
			        	else if (selling.equalsIgnoreCase("Stamina"))
			        	{
			        		int i;
			        		for (i=0; i < Integer.valueOf(samount); i++)
			        		{
			        			int enterances = plugin.playerConfig(p).getInt("staminaBoost", 0);
			    				enterances++;
			    				plugin.playerConfig(p).setProperty("staminaBoost", enterances);
			        		}
			        		removeInventoryItems(p.getInventory(), Integer.valueOf(buying), Integer.valueOf(bamount));
			        		player.updateInventory();
			        		
			        		player.sendMessage(ChatColor.YELLOW+"You have received "+samount+" more stamina level. You can now run longer and use more magic.");
			        		
			        		int ti2merID = plugin.playerConfig(event.getPlayer()).getInt("sprintTimer", 0);
			    	    	event.getPlayer().getServer().getScheduler().cancelTask(ti2merID);
			    	    	LGSprinter sprint = new LGSprinter(event.getPlayer(), plugin);
			    	    	
			    	    	int stam = plugin.skillLevel(event.getPlayer(), "Sprint");
			    			int boost = stam*3;
			    	    	
			    	    	int staminaBoosts = plugin.playerConfig(event.getPlayer()).getInt("staminaBoost", 0);
			    			staminaBoosts++;
			    			int timerID = event.getPlayer().getServer().getScheduler().scheduleSyncRepeatingTask(plugin, sprint, 10*staminaBoosts+boost, 10*staminaBoosts+boost);
			    			plugin.playerConfig(event.getPlayer()).setProperty( "sprintTimer", timerID);
			    			
			        	}
			        	else if (selling.equalsIgnoreCase("Gate"))
			        	{
			        		p.addAttachment(Bukkit.getPluginManager().getPlugin("CraftBookMechanisms")).setPermission("craftbook.mech.gate", true);
			        		
			        		removeInventoryItems(p.getInventory(), Integer.valueOf(buying), Integer.valueOf(bamount));
			        		player.updateInventory();
			        		
			        		plugin.playerConfig(p).setProperty("gatePerk", "true");
			        		player.sendMessage(ChatColor.YELLOW+"You can now create gates!");
			        	}
			        	else if (selling.equalsIgnoreCase("Bridge"))
			        	{
			        		p.addAttachment(Bukkit.getPluginManager().getPlugin("CraftBookMechanisms")).setPermission("craftbook.mech.bridge", true);
			        		
			        		removeInventoryItems(p.getInventory(), Integer.valueOf(buying), Integer.valueOf(bamount));
			        		player.updateInventory();
			        		
			        		plugin.playerConfig(p).setProperty("bridgePerk", "true");
			        		player.sendMessage(ChatColor.YELLOW+"You can now create bridges!");
			        	}
			        	else if (selling.equalsIgnoreCase("Lift"))
			        	{
			        		p.addAttachment(Bukkit.getPluginManager().getPlugin("CraftBookMechanisms")).setPermission("craftbook.mech.elevator", true);
			        		
			        		removeInventoryItems(p.getInventory(), Integer.valueOf(buying), Integer.valueOf(bamount));
			        		player.updateInventory();
			        		
			        		plugin.playerConfig(p).setProperty("liftPerk", "true");
			        		player.sendMessage(ChatColor.YELLOW+"You can now create lifts!");
			        	}
			        	else if (selling.equalsIgnoreCase("Music"))
			        	{
			        		p.addAttachment(Bukkit.getPluginManager().getPlugin("OcarinaSong")).setPermission("ocarina.time.sign", true);
			        		p.addAttachment(Bukkit.getPluginManager().getPlugin("OcarinaSong")).setPermission("ocarina.zelda.sign", true);
			        		p.addAttachment(Bukkit.getPluginManager().getPlugin("OcarinaSong")).setPermission("ocarina.awakening.sign", true);
			        		
			        		
			        		removeInventoryItems(p.getInventory(), Integer.valueOf(buying), Integer.valueOf(bamount));
			        		player.updateInventory();
			        		
			        		plugin.playerConfig(p).setProperty("musicPerk", "true");
			        		player.sendMessage(ChatColor.YELLOW+"You can now create music detectors! See the demo for more information on creation!");
			        	}
			        	else if (selling.equalsIgnoreCase("WolfDisguise"))
			        	{
			        		p.addAttachment(Bukkit.getPluginManager().getPlugin("MobDisguise")).setPermission("mobdisguise.wolf", true);
			        		removeInventoryItems(p.getInventory(), Integer.valueOf(buying), Integer.valueOf(bamount));
			        		player.updateInventory();
			        		
			        		plugin.playerConfig(p).setProperty("wolfPerk", "true");
			        		player.sendMessage(ChatColor.YELLOW+"You can now disguise as a wolf! Type /md Wolf to change and /md to change back.");
			        	}
			        	else if (selling.equalsIgnoreCase("ZombieDisguise"))
			        	{
			        		p.addAttachment(Bukkit.getPluginManager().getPlugin("MobDisguise")).setPermission("mobdisguise.zombie", true);
			        		removeInventoryItems(p.getInventory(), Integer.valueOf(buying), Integer.valueOf(bamount));
			        		player.updateInventory();
			        		
			        		plugin.playerConfig(p).setProperty("zombiePerk", "true");
			        		player.sendMessage(ChatColor.YELLOW+"You can now disguise as a Zombie! Type /md Zombie to change and /md to change back.");
			        	}
			        	else if (selling.equalsIgnoreCase("SkeletonDisguise"))
			        	{
			        		p.addAttachment(Bukkit.getPluginManager().getPlugin("MobDisguise")).setPermission("mobdisguise.skeleton", true);
			        		removeInventoryItems(p.getInventory(), Integer.valueOf(buying), Integer.valueOf(bamount));
			        		player.updateInventory();
			        		
			        		plugin.playerConfig(p).setProperty("skeletonPerk", "true");
			        		player.sendMessage(ChatColor.YELLOW+"You can now disguise as a Skeleton! Type /md Skeleton to change and /md to change back.");
			        	}
			        	else if (selling.equalsIgnoreCase("c"))
			        	{
			        		plugin.giveMoney(player, Integer.valueOf(samount));
			        		
			        		removeInventoryItems(p.getInventory(), Integer.valueOf(buying), Integer.valueOf(bamount));
			        		player.updateInventory();
			        		
			        	}
			        	else if (plugin.getColors().contains(selling))
			        	{
			        		player.sendMessage(ChatColor.YELLOW+"Your name colour has been changed.");
			        		plugin.playerConfig(p).setProperty("nameColor", plugin.colorFromString(selling).toString());
			        			
			        		removeInventoryItems(p.getInventory(), Integer.valueOf(buying), Integer.valueOf(bamount));
				        	player.updateInventory();
				        	
				        	plugin.updateName(player);
			        	}
			        	else if (selling.equalsIgnoreCase("Spawn"))
			        	{

			        		
			        		
			        		String sneak = plugin.playerConfig(p).getString("citadelSpawn", "");
			        		
			        		if (sneak.equalsIgnoreCase(""))
			        		{
			        			player.sendMessage(ChatColor.YELLOW+"You have gained the citadelSpawn perk! Type /citadel to use it.");
			        			plugin.playerConfig(p).setProperty("citadelSpawn", "true");
			        			
			        			removeInventoryItems(p.getInventory(), Integer.valueOf(buying), Integer.valueOf(bamount));
				        		player.updateInventory();
			        		}
			        		else
			        		{
			        			player.sendMessage(ChatColor.RED+"You already have citadelSpawn! Type /citadel to use it.");
			        		}
			        	}
			        	else if (selling.equalsIgnoreCase("LightningLogin"))
			        	{

			        		
			        		
			        		String sneak = plugin.playerConfig(p).getString("lightningLogin", "");
			        		
			        		if (sneak.equalsIgnoreCase(""))
			        		{
			        			player.sendMessage(ChatColor.YELLOW+"You have gained the lightning login perk! Lightning will strike your location when you login.");
			        			plugin.playerConfig(p).setProperty("lightningLogin", "true");
			        			
			        			removeInventoryItems(p.getInventory(), Integer.valueOf(buying), Integer.valueOf(bamount));
				        		player.updateInventory();
			        		}
			        		else
			        		{
			        			player.sendMessage(ChatColor.RED+"You already have lightning login! Lightning will strike your location when you login.");
			        		}
			        	}
			        	else if (selling.equalsIgnoreCase("Sneak"))
			        	{

			        		
			        		
			        		String sneak = plugin.playerConfig(p).getString("sneak", "");
			        		
			        		if (sneak.equalsIgnoreCase(""))
			        		{
			        			player.sendMessage(ChatColor.YELLOW+"You have gained the sneak perk! Type /sneak to use it.");
			        			plugin.playerConfig(p).setProperty("sneak", "true");
			        			
			        			removeInventoryItems(p.getInventory(), Integer.valueOf(buying), Integer.valueOf(bamount));
				        		player.updateInventory();
			        		}
			        		else
			        		{
			        			player.sendMessage(ChatColor.RED+"You already have sneak! Type /sneak to use it.");
			        		}
			        	}
			        	else if (selling.equalsIgnoreCase("EXPBoost"))
			        	{
			        		long sneak = Long.valueOf(plugin.playerConfig(p).getString("EXPBOOST", "0"));
			        		long time = System.currentTimeMillis();
			        		
			        		long since = time-sneak;
			        		if (since>(60*60*24*1000))
			        		{
			        			player.sendMessage(ChatColor.YELLOW+"You have gained an experience boost!");
			        			plugin.playerConfig(p).setProperty("EXPBOOST", String.valueOf(System.currentTimeMillis()));
			        			
			        			removeInventoryItems(p.getInventory(), Integer.valueOf(buying), Integer.valueOf(bamount));
				        		player.updateInventory();
			        		}
			        		else
			        		{
			        			player.sendMessage(ChatColor.RED+"You already have an exp boost.");
			        		}
			        	}
			        	else
			        	{
			        		int item_id = Integer.valueOf(selling);
			        		ItemStack newitems = new ItemStack(item_id, Integer.valueOf(samount));
			        		
			        		if (player.getInventory().firstEmpty() != -1)
			        		{
				        		player.getInventory().addItem(newitems);
				        		player.updateInventory();
				        		
				        		if (buying.equalsIgnoreCase("c"))
				        		{
				        			plugin.removeMoney(player, Integer.valueOf(bamount));
				        		}
				        		else
				        		{
				        			removeInventoryItems(p.getInventory(), Integer.valueOf(buying), Integer.valueOf(bamount));
				        			player.updateInventory();
				        		}
				        		
				        		player.sendMessage(ChatColor.YELLOW+"You have received "+samount+" "+ newitems.toString());
			        		}
			        		else
			        		{
			        			player.sendMessage(ChatColor.RED+"Your inventory is full! Transaction cancelled.");
			        		}
			        	}
			        		
		        	}
		        	else
		        	{    		
		        		player.sendMessage(ChatColor.RED+"You do not have the required items for this purchase.");
		        	}
		        	}
		        		

		        	
	        	}
	        		
	        }
	        else if (t == Material.AIR && plugin.skillLevel(p, "Paladin") >= 0 && (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK))
	        {
	        	
	        }
	        else if ( p.getItemInHand().getType() == Material.STICK && plugin.skillLevel(p, "Magic") >= 0 && (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK))
	        {
	        	//Cut down the entire tree
	        	int ignore_nomad = 1;
	        	
	        	if (ignore_nomad == 1)
	        	{
	        		
	        	
	        	if (plugin.playerConfig(p).getString("nomad", "").equalsIgnoreCase("true"))
	            {
	        		boolean can_cast = false;
	        		WorldGuardPlugin worldGuard = getWorldGuard();
		    		RegionManager regionManager = worldGuard.getRegionManager(p.getWorld());
		    		
		    		Location location = p.getLocation();

		    		com.sk89q.worldedit.Vector v = new com.sk89q.worldedit.Vector(location.getX(), location.getY(), location.getZ());
		    		
		    		ProtectedRegion region2 = worldGuard.getRegionManager(p.getWorld()).getRegion("city");
		    		if (region2.contains(v)) {
		    		    // do stuff
		    			can_cast = true;
		    		}
		    		
					if (!can_cast)
					{
						 p.sendMessage(ChatColor.RED+"You cannot cast spells outside of the tutorial area or city as a nomad. Post an application on the minecraftforum and we will give you citizenship. ");
						 p.sendMessage(ChatColor.YELLOW+"Go to http://tinyurl.com/skycraft4.");
						 return;
					}

	            }
	        	
	        	}
	        	
	        	String spec = plugin.playerConfig(p).getString("current_spec", "");
	        	
	        	if (spec.equalsIgnoreCase("paladin")||spec.equalsIgnoreCase("idiot"))
	        	{
	        		p.sendMessage(ChatColor.RED+"You cannot use a wand.");
	        		return;
	        	}
	        	
	        	if (plugin.armour(p)<=1)
	        	{
	        		//No Helmet
	        	
	        		//p.getItemInHand().setDurability((short) 100);
	        	
		        	int next_slot;
		        	
		        	if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
		        		next_slot= p.getInventory().getHeldItemSlot()+1;
		        	else
		        		next_slot= p.getInventory().getHeldItemSlot()+2;
		    
		        	
		        		ItemStack the_item = p.getInventory().getItem(next_slot);
		        		
		        		if (the_item.getType() == Material.LOG)
		        		{
		        			if (plugin.skillLevel(p, "Magic") >= 0)
		        				castSmallFlame(p);
		        			else
		        				p.sendMessage(ChatColor.RED+"You need to be atleast level 0 to cast flame.");
		        		}
		        		else if (the_item.getType() == Material.COAL)
		        		{
		        			if (plugin.skillLevel(p, "Magic") >= 1)
		        				castLargeFlame(p);
		        			else
		        				p.sendMessage(ChatColor.RED+"You need to be atleast level 1 to cast large flame.");
		        		}
		        		else if (the_item.getType() == Material.BONE)
		        		{
		        			if (plugin.skillLevel(p, "Magic") >= 2)
		        				castBolt(p);
		        			else
		        				p.sendMessage(ChatColor.RED+"You need to be atleast level 2 to cast Minor Bolt.");
		        		}
		        		else if (the_item.getType() == Material.SULPHUR)
		        		{
		        			if (plugin.skillLevel(p, "Magic") >= 3)
		        				castExplosion(p);
		        			else
		        				p.sendMessage(ChatColor.RED+"You need to be atleast level 3 to cast Minor Explosion.");
		        		}
		        		else if (the_item.getType() == Material.BOOK)
		        		{
		        			if (plugin.skillLevel(p, "Magic") >= 4)
		        				castTeleport(p);
		        			else
		        				p.sendMessage(ChatColor.RED+"You need to be atleast level 4 to cast Teleportation.");
		        		}
		        		else if (the_item.getType() == Material.ENDER_PEARL)
		        		{
		        			if (plugin.skillLevel(p, "Magic") >= 5)
		        				castSuperBolt(p);
		        			else
		        				p.sendMessage(ChatColor.RED+"You need to be atleast level 5 to cast Large Bolt.");
		        		}
		        		else if (the_item.getType() == Material.BLAZE_POWDER)
		        		{
		        			if (plugin.skillLevel(p, "Magic") >= 6)
		        				castSlowness(p);
		        			else
		        				p.sendMessage(ChatColor.RED+"You need to be atleast level 6 to cast slowness.");
		        		}
		        		else if (the_item.getType() == Material.INK_SACK)
		        		{
		        			if (plugin.skillLevel(p, "Magic") >= 7)
		        				freezeSpell(p);
		        			else
		        				p.sendMessage(ChatColor.RED+"You need to be atleast level 7 to cast Terrain freeze.");
		        		}
		        		else if (the_item.getType() == Material.SOUL_SAND)
		        		{
		        			if (plugin.skillLevel(p, "Magic") >= 8)
		        				castSlow(p);
		        			else
		        				p.sendMessage(ChatColor.RED+"You need to be atleast level 8 to cast Slow.");
		        		}
		        		else if (the_item.getType() == Material.GOLD_INGOT)
		        		{
		        			if (plugin.skillLevel(p, "Magic") >= 12)
		        				castUberBolt(p);
		        			else
		        				p.sendMessage(ChatColor.RED+"You need to be atleast a level 12 to cast wraith of the elder sage.");
		        		}
		        		else if (the_item.getType() == Material.DIAMOND)
		        		{
		        			if (plugin.skillLevel(p, "Magic") >= 15)
		        				castShadowDarkness(p);
		        			else
		        				p.sendMessage(ChatColor.RED+"You need to be atleast a level 15 to cast darkness of the moon.");
		        		}
		        		else
		        		{
		        			if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
		        				p.sendMessage(ChatColor.RED+"Place the spell ingredient in the slot directly right of the wand");
		        			else
		        				p.sendMessage(ChatColor.RED+"Place the spell ingredient two slots to the right of your wand");
		        		}
		        		
		        		
		        		
		        		
		        		p.updateInventory();
	        			
	        	}
	        	else
	        	{
	        		p.sendMessage(ChatColor.RED+"You cannot cast spells while wearing armour greater than leather!");
	        	}
	        	//Cast a magic spell
	        }
	        	
	    }
	    
	    
	    

	    @Override
	    public void onItemHeldChange(PlayerItemHeldEvent event)
	    {
	    	

	    	
	          //System.out.println(player.getVelocity().toString());
	    	if (event.getPlayer().getInventory().getItem(event.getPreviousSlot()).getType() == Material.TORCH)
	    	{
	    		Player player = event.getPlayer();
	    		Block at_foot_old =  player.getWorld().getBlockAt(player.getLocation()).getRelative(0, -1, 0);

	    		if (at_foot_old.getType() != Material.AIR)
	    			sendGlobalBlockChange(at_foot_old.getLocation(), at_foot_old.getType(), at_foot_old.getData());
	    	}
	    	else if (event.getPlayer().getInventory().getItem(event.getNewSlot()).getType() == Material.TORCH)
	    	{
	    		Player player = event.getPlayer();
	    		
	    		
	    		Block at_foot =  player.getWorld().getBlockAt(player.getLocation()).getRelative(0, -1, 0);

	    		if (at_foot.getType() != Material.AIR)
	    			sendGlobalBlockChange(at_foot.getLocation(), Material.GLOWSTONE, at_foot.getData());
	        	
	    	}
	    	else if (event.getPlayer().getInventory().getItem(event.getNewSlot()).getType() == Material.SNOW_BALL)
	    	{
	    		event.getPlayer().sendMessage(ChatColor.YELLOW+"Throw this pokeball at a Zombie, Pig Zombie, Creeper, Silverfish, Slime or Spider to capture it!");
	    	}
	    	else if (event.getPlayer().getInventory().getItem(event.getNewSlot()).getType() == Material.CHAINMAIL_BOOTS)
	    	{
	    		event.getPlayer().sendMessage(ChatColor.YELLOW+"Equip these pegasus boots for a speed increase. It only lasts about 30 seconds.");
	    	}
	    	else if (event.getPlayer().getInventory().getItem(event.getNewSlot()).getTypeId()==36)
	    	{
	    		event.getPlayer().sendMessage(ChatColor.YELLOW+"Right click with this backpack to open it.");
	    	}
	    	
	    	
	    	
	    }
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
		 @Override
		 public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
		 {
			 
			 Entity e = event.getRightClicked();
			 if (e instanceof Player)
			 {
				 Player receive = (Player)e;
				 Player sender = event.getPlayer();

				 String player = plugin.playerConfig(receive).getString("LastTrader", "");
				 String player2 = plugin.playerConfig(sender).getString("LastTrader", "");
				 
				//NOW, check whether they are our last trader.
				 if (player2.equalsIgnoreCase(receive.getName()))
				 {
					 //Initialise trade!
					 plugin.playerConfig(receive).setProperty("LastTrader", "");
					 plugin.playerConfig(sender).setProperty("LastTrader", "");
					 
					 receive.sendMessage(ChatColor.LIGHT_PURPLE+"Initialising gift request...");
					 sender.sendMessage(ChatColor.LIGHT_PURPLE+"Initialising gift request...");
					 
					 EntityPlayer entityplayer = ((CraftPlayer) sender).getHandle();
					 EntityPlayer entityplayer2 = ((CraftPlayer) receive).getHandle();

					 String shr = sender.getName().substring(0, 4);
				     String shr2 = receive.getName().substring(0, 4);
								
				     
				     
				     
				     plugin.playerConfig(sender).setProperty("TRADELEFT", sender.getName());
					 plugin.playerConfig(sender).setProperty("TRADERIGHT", receive.getName());
				     
					 plugin.playerConfig(receive).setProperty("TRADELEFT", sender.getName());
					 plugin.playerConfig(receive).setProperty("TRADERIGHT", receive.getName());
				     
				     
				     
				     
					 
					 InventoryLargeChest chest = new InventoryLargeChest("Secure gifts", new TileEntityChest(), new TileEntityChest());
					 //chest.setItem((int)3, new net.minecraft.server.ItemStack((int)3, (int)1, (short)1));
					 //chest.setItem((int)5, new net.minecraft.server.ItemStack((int)3, (int)1, (short)1));
					 
					 entityplayer.a(chest);
					 entityplayer2.a(chest);
				 }
				 else
				 {
					 if (player.equalsIgnoreCase(sender.getName()))
						 sender.sendMessage(ChatColor.RED+receive.getName()+" is already considering your request.");
					 else
					 {
						 plugin.playerConfig(receive).setProperty("LastTrader", sender.getName());
						 
						 receive.sendMessage(ChatColor.LIGHT_PURPLE+sender.getName()+" wants to give you items. Right click them.");
						 sender.sendMessage(ChatColor.LIGHT_PURPLE+"Sending gift request to "+receive.getName());
					 }
				 }
				 

			 }
		 }
		
	    
	    private WorldGuardPlugin getWorldGuard() {
	        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
	     
	        // WorldGuard may not be loaded
	        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	            return null; // Maybe you want throw an exception instead
	        }
	     
	        return (WorldGuardPlugin) plugin;
	    }
	
}

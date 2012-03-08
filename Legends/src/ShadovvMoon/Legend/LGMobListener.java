package ShadovvMoon.Legend;

import java.util.List;

import net.minecraft.server.DamageSource;
import net.minecraft.server.World;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftArrow;
import org.bukkit.craftbukkit.entity.CraftCaveSpider;
import org.bukkit.craftbukkit.entity.CraftGiant;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.craftbukkit.entity.CraftLightningStrike;
import org.bukkit.craftbukkit.entity.CraftPig;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.entity.CraftSnowball;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import com.sk89q.worldedit.Vector;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class LGMobListener extends EntityListener
{
	 private final Legends plugin;
	 public LGMobListener(Legends instance)
	 {
	     plugin = instance;
	 }
	 
	 @Override
	 public void onItemSpawn(ItemSpawnEvent event)
	 {
		 
		 
		 /*
		 if (event.getEntity() instanceof CraftItem)
		 {
			 CraftItem entity = (CraftItem) event.getEntity();
			 System.out.println(entity.getItemStack().getType().toString());
			 
		 }
		 */
		 
		 //if ()
		//	 event.setCancelled(true);
	 }
	 
		
	@Override
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		Entity e = event.getEntity();
		
		if (e instanceof Giant || e instanceof CraftGiant)
		{
			LGGiant mana_bar = new LGGiant(plugin, (CraftGiant)e);
			e.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, mana_bar, 20, 20);
		}
	}
	 
	@Override
	public void onProjectileHit(ProjectileHitEvent event)
	{
		Entity shooter = null;
				
				if (event.getEntity() instanceof Snowball)
					shooter = ((Snowball) event.getEntity()).getShooter();
				else if (event.getEntity() instanceof Arrow)
					shooter = ((Arrow) event.getEntity()).getShooter();
				else if (event.getEntity() instanceof Egg)
					shooter = ((Egg) event.getEntity()).getShooter();
					

		
		if (shooter instanceof Player)
		{
			if (event.getEntity() instanceof Snowball)
			{
				
				
				Player p = (Player)shooter;
				
				
				System.out.println("Snowball!");
				if (p.getName().equalsIgnoreCase("s"))
				{
					//Spawn a zombie lol
					
					//int entity_id = plugin.getConfig().getInt(p.getName()+".PetID", -1);
					//if (entity_id == -1 || p.getWorld().getEntities().get(entity_id).isDead())
					//{
						CaveSpider zombie = (CaveSpider) p.getWorld().spawnCreature(event.getEntity().getLocation().add(0, 1, 0), CreatureType.CAVE_SPIDER);
						LGAIZombie mana_bar = new LGAIZombie(p, plugin, zombie);

						
						int timerID = p.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, mana_bar, 0, 5);
						
						plugin.playerConfig(p).setProperty("Pet", timerID);
						plugin.playerConfig(p).setProperty("PetID", zombie.getEntityId());
						plugin.playerConfig(p).setProperty("PetState", 1);
						
						Bukkit.getServer().broadcastMessage("<"+p.getDisplayName()+"> Cave Spider, I choose you!");
					//}
					//else
					///{
					//	p.sendMessage(ChatColor.RED+"You already have a pet!");
					//}
			    	//getConfig().setProperty(playerName(player)+".manaTimer", timerID);


				}
			}
			else
			{
		    	 
		    	String spec = plugin.playerConfig(((CraftPlayer) shooter)).getString("current_spec", "");
		        	
		        if (spec.equalsIgnoreCase("paladin")||spec.equalsIgnoreCase("idiot")||spec.equalsIgnoreCase("mage"))
		        {
		        	((CraftPlayer) shooter).sendMessage(ChatColor.RED+"You cannot use a bow.");
		        	event.getEntity().remove();
		        	
		        	return;
		        }
				
				int lvl = plugin.skillLevel((Player)shooter, "Archery");
				if (lvl>=10)
				 {
					int rand = plugin.random_num(4, 0);
					if (rand == 0)
					{
						
						
						
						shooter.getWorld().playEffect(event.getEntity().getLocation(), Effect.SMOKE, 8);
						shooter.getWorld().createExplosion(event.getEntity().getLocation(), 0);
						
						
						addBlastZone((Player)shooter, shooter.getLocation(), 3, "arrow explosion");
						
				    	List<Entity> list = shooter.getNearbyEntities(20, 20, 20);
				    	
				    	boolean found = false;
				    	int i;
				    	for (i=0; i < list.size(); i++)
				    	{
				    		Entity e = list.get(i);
				    		if (e instanceof LivingEntity&&e.getLocation().distance(event.getEntity().getLocation()) <=20 )
				    		{
				    			LivingEntity en = (LivingEntity)e;
				    			en.damage(8, shooter);
				    			
				    			found = true;
				    		}
				    	}
						
					}
						
				 }
				
				if (lvl>=5)
				 {
					int rand = plugin.random_num(6, 0);
					if (rand == 0)
						shooter.getWorld().strikeLightning(event.getEntity().getLocation());
				 }
				
			}
		}
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
    
	 @Override
	 public void onEntityDamage(EntityDamageEvent event)
	 {
		 if (event.isCancelled())
			 return;
		 
		 Entity hit = event.getEntity();
		 
		 
		 
		 /*
 		if (hit instanceof LivingEntity)
 		{
 			if (!(hit instanceof Pig))
 			{
	    		WorldGuardPlugin worldGuard = getWorldGuard();
	    		RegionManager regionManager = worldGuard.getRegionManager(hit.getWorld());
	    		
	    		Location location = hit.getLocation();
	    		ProtectedRegion region = worldGuard.getRegionManager(hit.getWorld()).getRegion("tutorial");
	    		
	    		com.sk89q.worldedit.Vector v = new com.sk89q.worldedit.Vector(location.getX(), location.getY(), location.getZ());
	    		
	    		ProtectedRegion region2 = worldGuard.getRegionManager(hit.getWorld()).getRegion("city");
	    		ProtectedRegion region3 = worldGuard.getRegionManager(hit.getWorld()).getRegion("areana");
	    		
	    		
	    		if ((region.contains(v)||region2.contains(v)) && !region3.contains(v))
	    		{
	    		    // do stuff
	    			event.setCancelled(true);
	    			//player.sendMessage(ChatColor.RED+"You cannot attack players or mobs in this area.");
	    		}
 			}
 		}
		*/ 
 		
		 boolean can_cast = true;
  		WorldGuardPlugin worldGuard = getWorldGuard();
	    		RegionManager regionManager = worldGuard.getRegionManager(hit.getWorld());
	    		
	    		Location location = hit.getLocation();
	    		com.sk89q.worldedit.Vector v = new com.sk89q.worldedit.Vector(location.getX(), location.getY(), location.getZ());
	    		

		 if(event instanceof EntityDamageByEntityEvent)
		 {
			 Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
			 
			 
			
			 
			 if (damager instanceof CraftCaveSpider)
			 {
				 event.setDamage(10);
			 }
			 
			 if (damager instanceof CraftLightningStrike)
			 {
				 CraftLightningStrike strike = (CraftLightningStrike)damager;
				 strike.setFireTicks(0);

				 event.setDamage(2);

				 if (hit instanceof Player && !can_cast)
		    		 event.setCancelled(true);
				// System.out.println(damager.getClass().toString());
			 }
			 else if (damager instanceof Player)
		     {
		    	 String spec = plugin.playerConfig(((CraftPlayer) damager)).getString("current_spec", "");
		        	
			        if (spec.equalsIgnoreCase("idiot")||spec.equalsIgnoreCase("mage"))
			        {
			        	((CraftPlayer) damager).sendMessage(ChatColor.RED+"You cannot use melee.");
			        	event.setCancelled(true);
			        	return;
			        }
			        
			       

			     if (!plugin.canUseTool((Player)damager))
			     {
			    	 event.setCancelled(true);
			    	 return;
			     }
			     
			     	

		    		if (can_cast)
		    		{
					     
		    			
		    			 Player player = ((Player) damager);
		    			 plugin.playerConfig(player).setProperty("Target", hit.getEntityId());
			    			
		    			 Material t = player.getItemInHand().getType();
		    			 
		    			 if (t==Material.STONE_SWORD||t==Material.WOOD_SWORD||t==Material.GOLD_SWORD||t==Material.IRON_SWORD||t==Material.DIAMOND_SWORD)
		    			 {
			    			 
						     int lvl = plugin.skillLevel((Player)damager, "Strength");
						     int rand = plugin.random_num(30-(lvl*2), 0);
					    	 if (lvl>20 || (lvl>=2 && rand==0))
				    		 {
				    			 hit.setFireTicks(20*(lvl+2));
				    		 }
					    	 
					    	
					    	 if (lvl>=6)
							 {
								rand = plugin.random_num(23-lvl, 0);
								if (rand == 0)
								{
									player.sendMessage(ChatColor.GREEN+"You leech health from the damage caused by your sword.");
									
									if (player.getHealth()+2<20)
										player.setHealth(player.getHealth()+2);	
									else
										player.setHealth(20);
								}
							 }
					    	 
					    	 
					    	 
					    	 
					    	 if (hit instanceof Player||hit instanceof CraftPlayer)
					    	 {
						    	 CraftPlayer p = (CraftPlayer)hit;
						    	 
						    	 
							     rand = plugin.random_num(20-lvl, 0);
							     if (lvl>20 || (lvl>=4 && rand==0))
					    		 {
							    	//FReeze player
	
							    	 plugin.doEffect(p.getHandle(), 2, 1, 2);
							    	 
							    	 ((Player) damager).sendMessage(ChatColor.YELLOW+"You have slowed "+ p.getName()+"!");
							    	 p.sendMessage(ChatColor.RED+"You have been slowed for 1 second.");
					    		 }
					    	 }
					    	 
					    	 
					    	 if (hit instanceof Player||hit instanceof CraftPlayer)
					    	 {
						    	 CraftPlayer p = (CraftPlayer)hit;
						    	 
						    	 
							     rand = plugin.random_num(30-lvl, 0);
							     if (lvl>20 || (lvl>=7 && rand==0))
					    		 {
							    	//FReeze player
	
							    	 plugin.doEffect(p.getHandle(), 9, 5, 2);
							    	 
							    	 ((Player) damager).sendMessage(ChatColor.YELLOW+"You have concussed "+ p.getName()+"!");
							    	 p.sendMessage(ChatColor.RED+"You have been concussed for 5 second.");
					    		 }
					    	 }
					    	 


					    	 
					    	 if (hit instanceof Player||hit instanceof CraftPlayer)
					    	 {
						    	 CraftPlayer p = (CraftPlayer)hit;
						    	 
						    	 
							     rand = plugin.random_num(20-lvl, 0);
							     if (lvl>20 || (lvl>=8 && rand==0))
					    		 {
							    	//FReeze player
							    	 long current_time = System.currentTimeMillis();
							    	 plugin.playerConfig(p).setProperty("frozenTime", String.valueOf(current_time));
									
								     
							    	 ((Player) damager).sendMessage(ChatColor.YELLOW+"You have stunned "+ p.getName()+"!");
							    	 p.sendMessage(ChatColor.RED+"You have been stunned! You cannot move for 1 second.");
					    		 }
							     
						    	 rand = plugin.random_num(15-lvl, 0);
					    		 if (lvl>20 || (lvl>=3 && rand==0))
					    		 {
					    			 if (p.getFoodLevel()>2)
					    				 p.setFoodLevel(p.getFoodLevel()-2*((lvl+3)/3));
					    			 else
					    				 p.setFoodLevel(0);
					    		 }
					    	 }
				    	 
		    			 }
		    		}
		    		else
		    		{
		    			//System.out.println(damager.getClass().toString());
		    			
		    			if ((hit instanceof Player))
		    			{
			    			Player player = ((Player) damager);
			    			 
			    			//player.sendMessage(ChatColor.RED+"Your security status in this city has decreased.");
			    			player.sendMessage(ChatColor.GOLD+"You are being attacked by Skycraft Citadel Guards!");
			    			
			    			CraftCaveSpider zombie = (CraftCaveSpider) player.getWorld().spawnCreature(player.getLocation(), CreatureType.CAVE_SPIDER);
							zombie.setTarget(player);
							player.damage(10, zombie);
	

							LGGuard mana_bar = new LGGuard(player, plugin, zombie);
							Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, mana_bar, 0, 5);
							
							
			    			//event.setCancelled(true);
			    			//player.sendMessage(ChatColor.RED+"You cannot attack players or mobs in this area.");
		    			}
		    			
		    			
		    		}
		    	 
		     }
			 else if (damager instanceof Snowball)
			 {
				 if (event.getEntity() instanceof Player)
				 {
					 ((CraftPlayer)((((Snowball) damager).getShooter()))).sendMessage(ChatColor.RED+"You cannot capture players!");
					 event.setCancelled(true);
				 }
				 else
				 {
					 
					 
					 String name = "";
					 if (hit instanceof PigZombie)
							name = "Pig Zombie";
					 else if (hit instanceof Zombie)
						 name = "Zombie";
						else  if (hit instanceof Creeper)
							name = "Creeper";
						else  if (hit instanceof Silverfish)
							name = "Silverfish";
						else  if (hit instanceof Slime)
							name = "Slime";
						else  if (hit instanceof Spider)
							name = "Spider";
						else  if (hit instanceof Enderman)
							name = "Enderman";
						else
						{
							((CraftPlayer)((((Snowball) damager).getShooter()))).sendMessage(ChatColor.RED+"You cannot capture this type of creature!");
							 event.setCancelled(true);
							return;
						}
					 
					 Player p = ((Player)((((Snowball) damager).getShooter())));
					 Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+p.getName()+" has captured "+name+"!");
				 

					 p.sendMessage(ChatColor.YELLOW+"Type /pet choose " + name + " to release your pet. It will follow and protect you during attacks.");
					 
					 event.getEntity().remove();
					 
					 
					 int amount = plugin.playerConfig(p).getInt("Pets."+name, 0);
					 plugin.playerConfig(p).setProperty("Pets."+name, amount+1);
													
					 //Add the thing to the players list
				 }
			 }
		     else if (damager instanceof Arrow||damager instanceof CraftArrow)
			 {
		    	 if (event.isCancelled())
		    		return;
		        	
			       /* 
		    	 boolean can_cast = true;
	        		WorldGuardPlugin worldGuard = getWorldGuard();
		    		RegionManager regionManager = worldGuard.getRegionManager(damager.getWorld());
		    		
		    		Location location = damager.getLocation();
		    		ProtectedRegion region = worldGuard.getRegionManager(damager.getWorld()).getRegion("tutorial");
		    		com.sk89q.worldedit.Vector v = new com.sk89q.worldedit.Vector(location.getX(), location.getY(), location.getZ());
		    		if (region.contains(v)) {
		    		    // do stuff
		    			can_cast = false;
		    		}
		    		
		    		ProtectedRegion region2 = worldGuard.getRegionManager(damager.getWorld()).getRegion("city");
		    		if (region2.contains(v)) {
		    		    // do stuff
		    			can_cast = false;
		    		}
		    		
		    		*/
		    		
		    	 
		    	 if (!(((Arrow) damager).getShooter() instanceof Player))
			     {
		    		 return;
			     }
		    	 	event.setDamage(6);
		    	 
		    	 
		    	
		    	 
		    	 Player player = (Player) ((Arrow) damager).getShooter();
		    	 
		    	 
		    	 if (!can_cast)
		    		{
		    			 
		    			if ((hit instanceof Player))
		    			{
			    			 
			    			//player.sendMessage(ChatColor.RED+"Your security status in this city has decreased.");
			    			player.sendMessage(ChatColor.GOLD+"You are being attacked by Skycraft Citadel Guards!");
			    			
			    			CraftCaveSpider zombie = (CraftCaveSpider) player.getWorld().spawnCreature(player.getLocation(), CreatureType.CAVE_SPIDER);
							zombie.setTarget(player);
							player.damage(10, zombie);
	

							LGGuard mana_bar = new LGGuard(player, plugin, zombie);
							Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, mana_bar, 0, 5);
							
							
			    			//event.setCancelled(true);
			    			//player.sendMessage(ChatColor.RED+"You cannot attack players or mobs in this area.");
		    			}
		    			
		    		}
		    	 
		    	 
		    	 if (plugin.armour(player)<=3)
			     {
		
		    	 
				       
			    	 int lvl = plugin.skillLevel((Player)player, "Archery");
						
			    	 int rand = plugin.random_num(15-lvl, 0);
			    	 if (lvl>20 || (lvl>=1 && rand==0))
		    		 {
		    			 hit.setFireTicks(20*(lvl+2));
		    		 }
			    	 
			    	 if (lvl>=8)
					 {
						rand = plugin.random_num(5, 0);
						if (rand == 0)
						{
							player.sendMessage(ChatColor.GREEN+"You leech 2 hearts from the damage caused by your arrow.");
							
							if (player.getHealth()+4<20)
								player.setHealth(player.getHealth()+4);	
							else
								player.setHealth(20);
						}
					 }
			    	 
			    	 if (hit instanceof Player)
				     {
			    		 rand = plugin.random_num(15-lvl, 0);
			    		 
			    		 CraftPlayer p = (CraftPlayer)hit;
			    		 if (lvl>20 || (lvl>=3 && rand==0))
			    		 {
			    			 if (p.getFoodLevel()>2)
			    				 p.setFoodLevel(p.getFoodLevel()-2*((lvl+3)/3));
			    			 else
			    				 p.setFoodLevel(0);
			    		 }
				     }
		    	 
			     }
		    	 else
		    	 {
		    		 player.sendMessage(ChatColor.RED+"Your armour weighs you down and you fail to damage your oppoent.");
		    		 event.setCancelled(true);
		    	 }
			 }
		     else
		     {
		    	 if (hit instanceof Player && !can_cast && !(damager instanceof Monster) )
		    		 event.setCancelled(true);
		     }
			 
			 if (hit instanceof Player && (damager instanceof Monster || damager instanceof Player) && !event.isCancelled())
			 {
				 Player p = (Player)hit;
				 
				 int health_level = plugin.skillLevel(p, "Health");
					
				 if (health_level >= 10)
					 health_level = 10;
				 
				
				 
				 int rand = plugin.random_num(12-health_level, 0);
		    	 if (health_level>20 || (health_level>=1 && rand==0))
	    		 {
	    			 event.setCancelled(true);
	    			 p.sendMessage(ChatColor.GREEN+"You dodge the attack!");
	    		 }
		    	 else  if (damager instanceof Monster)
		    		 plugin.gainExperience(p, "Health", event.getDamage()*5);
		    	 
		    	 
		    	 if (!event.isCancelled())
		    	 {
		    		
		    		 
		    		 
		    		 
		    		 ItemStack[] helmet = p.getInventory().getArmorContents();
		    	    	int exp = 0;
		    	    	
		    	    	int i;
		    			for (i=0; i < helmet.length; i++)
		    			{
		    				ItemStack is = helmet[i];
		    				String tp = is.getType().toString().toLowerCase();
		    				
		    				int level = 0;
		    				if (tp.contains("diamond")) level = 5;
		    				else if (tp.contains("iron")) level = 4;
		    				else if (tp.contains("chain")) level = 3;
		    				else if (tp.contains("gold")) level = 2;
		    				else if (tp.contains("leather")) level = 1;
		    		    	
		    				exp+=level;
		    				
		    			}
		    		 
		    			if (exp > 0)
		    			{
		    				plugin.gainExperience(p, "Defence", exp);
		    			}
		    	 }
		    	 
			 }
			 
		 }
		 else
	     {
	    	 if (hit instanceof Player && !can_cast )
	    		 event.setCancelled(true);
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
	 

	 

	 @Override
	 public void onEntityRegainHealth(EntityRegainHealthEvent event)
	 {
	 }
	 
	 @Override
	 public void onEntityDeath(EntityDeathEvent damage_event)
	 {
		 
		 
		 
		 //if (dead instanceof Player||dead instanceof CraftPlayer)
		 //{
		 	
		 
		 //}
		 
		 if (damage_event.getEntity() instanceof Player)
		 {

	    		Player p = (Player) damage_event.getEntity();
	    		if (p.getInventory().contains(36))
	    		{
	    		
		    		String inven_name = "Backpack";
		    		plugin.dumpChest(p, p.getLocation());
		    		p.sendMessage(ChatColor.RED+"You items fall out of your backpack as it hits the ground.");
	    		}
		 }
		 
		
		 Event event = damage_event.getEntity().getLastDamageCause();
		 if(event instanceof EntityDamageByEntityEvent)
		 {
			 Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
		     Entity dead = damage_event.getEntity();

		     int radius = 20;
		     CraftWorld world = (CraftWorld) dead.getWorld();
		     int playerX = (int)dead.getLocation().getX();
		        int playerY = (int)dead.getLocation().getY();
		        int playerZ = (int)dead.getLocation().getZ();
		        
		        for(int x = (int)playerX-radius;x<playerX+radius;x ++){
		            for(int z = (int)playerZ-radius;z<playerZ+radius;z ++){
		                for(int y = (int)playerY-radius;y<playerY+radius;y ++){
		                    Block bs = ((org.bukkit.World) world).getBlockAt(x,y,z);
		                    if(!(bs.getType() == Material.MOB_SPAWNER))
		                    	continue;
		                   	
		                    if (!(dead instanceof Pig))
		                    	bs.setType(Material.AIR); //Mob spawner!
		                }
		            }
		        }
		     
		     if (damager instanceof Player)
		     {
		         Player player = (Player) damager;
		         
		         int experience = plugin.experienceForEntity(dead);
		         if (experience > 0)
		        	 plugin.gainExperience(player, "Strength", experience);
		         
		         
		         plugin.monsterKilled(dead, player);
		         
	        		
		     }
		     else if (damager instanceof Arrow)
		     {
		    	 Player player = (Player) ((Arrow) damager).getShooter();


		    	 int experience = plugin.experienceForEntity(dead);
		         if (experience > 0)
		        	 plugin.gainExperience(player, "Archery", experience);
		         
		         plugin.monsterKilled(dead, player);

		     }
		     
		     
		 }
		 else
		 {
			 Entity dead = damage_event.getEntity();
			 
			 //System.out.println("Death caused by something else!");
	    	 Player player = plugin.entityInBlastZone(dead);
	    	 
	    	 if (player != null)
	    	 {
	    		 //int experience = experienceForEntity(dead);
	    		 
	    	 }
		 }
	 } 
}

package ShadovvMoon.Legend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.MobEffect;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.entity.*;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.griefcraft.lwc.LWC;
import com.griefcraft.modules.limits.LimitsModule;

import com.iCo6.iConomy;
import com.iCo6.handlers.Money;
import com.nijikokun.register.Register;
import com.nijikokun.register.payment.Method;
import com.nijikokun.register.payment.Method.MethodAccount;
import com.nijikokun.register.payment.Methods;


import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import net.minecraft.server.DataWatcher;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPig;
import net.minecraft.server.EntitySkeleton;
import net.minecraft.server.EntitySquid;
import net.minecraft.server.InventoryLargeChest;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Packet20NamedEntitySpawn;
import net.minecraft.server.Packet24MobSpawn;
import net.minecraft.server.Packet29DestroyEntity;
import net.minecraft.server.TileEntityChest;

import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.event.Event.Type;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import java.net.MalformedURLException;
import java.net.URL;


public class Legends extends JavaPlugin {
	protected static Configuration CONFIG;
	protected static Configuration STATIC_CONFIG;
	protected static Configuration SETTLEMENTS;
	protected static Configuration BLOCK_TIMERS;
	protected static Configuration TELEPAD_DATA;
	protected static Configuration TASK_DATA;
	protected static Configuration XRAYERS;
	
	
	
	public int time = 30;
	public int strength = 10;
	
	protected static HashMap<String, Configuration> Player_Saves;
	public Configuration playerConfig(String player_name)
	{
		Configuration config;
		
		if (player_name == null)
			return null;
		
		config = (Configuration) Player_Saves.get(player_name);
		if (config != null)
		{
			return config;
		}
		else
		{
			return loadPlayerData(player_name);
		}
	}

	public void doEffect(EntityPlayer player, int id, int time, int strength) {
		player.addEffect(new MobEffect(id, time * 20, strength));
		
		
	}
	public void doEffect(EntityPlayer player, int id, float time, int strength) {

		player.addEffect(new MobEffect(id, (int)time * 20, strength));
	}
	
	
	public int armour(Player p)
	{
		ItemStack[] helmet = p.getInventory().getArmorContents();
		int level = 0;
		
		int i;
		for (i=0; i < helmet.length; i++)
		{
			ItemStack is = helmet[i];
			String tp = is.getType().toString().toLowerCase();
			
			if (tp.contains("diamond"))
			{
				level = 5;
			}
			else if (tp.contains("iron"))
			{
				level = 4;
			}
			else if (tp.contains("chain"))
			{
				level = 3;
			}
			else if (tp.contains("gold"))
			{
				level = 2;
			}
			else if (tp.contains("leather"))
			{
				level = 1;
			}
		}
		
		return level;
	}
	
	
	public void searchForApplication(String player_name)
	{
		//Check the last pages of the five pages :)
		
		String start_url = "http://www.minecraftforum.net/topic/639361-18-hardcore-skycraft-magic-settlements-experience-levels-abilities-etc/";
		
		URL url;
		try
		{
			url = new URL(start_url);
			
			InputStream is = url.openStream();
			try
			{
			  /* Now read the retrieved document from the stream. */
			 
				String cnts = is.toString();
				
				
				//LOOK FOR "<link rel='last' href='"
				int index = cnts.indexOf("<link rel=\'last\' href=\'");
				int end_index = cnts.substring(index).indexOf("\'");
				
				String last_page = cnts.substring(index, end_index);
				System.out.println("URL INPUT: " + last_page);
				
				
				
				
				//NOW, load that page :)
				
				try
				{
					url = new URL(last_page);
										
					is = url.openStream();
					try
					{
					  /* Now read the retrieved document from the stream. */
						cnts = is.toString();
						
						
						
						//PARSE AND FIND POSTS :)
						String[] posts = cnts.split("\'post entry-content \'>");
						int i;
						for (i=1; i < posts.length; i++)
						{
							String post_block = posts[i];
							
							int rng = post_block.indexOf("</div>");
							if (rng != -1)
							{
								String post = post_block.substring(0, rng);
							
								//Look for an IGN
								int ign = post_block.indexOf("IGN:");
								if (ign != -1)
								{
									String ign2 = post_block.substring(ign);
									
									int ign3 = post_block.indexOf("<br");
									String ign_name = post_block.substring(ign, ign3);
									
									System.out.println("IGN: " + ign_name);
								}
							}
							else
							{
								System.out.println("Missing div tag.");
							}
							
						}
					}
					finally
					{
					  is.close();
					}
				
				}
				catch (MalformedURLException e)
				{
					// TODO Auto-generated catch block
					System.out.println("URL Request failed.");
				}
				 catch (IOException e)
				{
					// TODO Auto-generated catch block
					System.out.println("URL Input failed.");
				}
				
				
				
				
				
				
				
				
				
				
			}
			finally
			{
			  is.close();
			}
			
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			System.out.println("URL Request failed.");
		}
		 catch (IOException e)
		{
				// TODO Auto-generated catch block
				System.out.println("URL Input failed.");
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public boolean effectPlayer(Player sender, String commandLabel, int amount, int ss)
	{
		if (amount>0)
			strength = amount;
		
		if (ss>0)
			time = ss;
		
		EntityPlayer eplayer = ((CraftPlayer) (Player)sender).getHandle();
		        if (commandLabel.equalsIgnoreCase("speedup")) {
		            doEffect(eplayer, 1, time, strength);
					return true;
		        } else if (commandLabel.equalsIgnoreCase("speeddown")) {
		            doEffect(eplayer, 2, time, strength);
					return true;
		        } else if (commandLabel.equalsIgnoreCase("digup")) {
		            doEffect(eplayer, 3, time, strength);
					return true;
		        } else if (commandLabel.equalsIgnoreCase("digdown")) {
		            doEffect(eplayer, 4, time, strength);
					return true;
		        } else if (commandLabel.equalsIgnoreCase("damageboost")) {
		            doEffect(eplayer, 5, time, strength);
					return true;
		        } else if (commandLabel.equalsIgnoreCase("heal")) {
		            doEffect(eplayer, 6, time, strength);
					return true;
		        } else if (commandLabel.equalsIgnoreCase("harm") ) {
		            doEffect(eplayer, 7, time, strength);
					return true;
		        } else if (commandLabel.equalsIgnoreCase("jump")) {
		            doEffect(eplayer, 8, time, strength);
					return true;
		        } else if (commandLabel.equalsIgnoreCase("confusion")) {
		            doEffect(eplayer, 9, time, strength);
					return true;
		        } else if (commandLabel.equalsIgnoreCase("regen")) {
		            doEffect(eplayer, 10, time, strength);
					return true;
		        } else if (commandLabel.equalsIgnoreCase("resist")) {
		            doEffect(eplayer, 11, time, strength);
					return true;
		        } else if (commandLabel.equalsIgnoreCase("fireresist")) {
		            doEffect(eplayer, 12, time, strength);
					return true;
		        } else if (commandLabel.equalsIgnoreCase("waterbreath") ) {
		            doEffect(eplayer, 13, time, strength);
					return true;
		        } else if (commandLabel.equalsIgnoreCase("invisible")) {
		            doEffect(eplayer, 14, time, strength);
					return true;
		        } else if (commandLabel.equalsIgnoreCase("blindness")) {
		            doEffect(eplayer, 15, time, strength);
					return true;
		        } else if (commandLabel.equalsIgnoreCase("nightvision")) {
		            doEffect(eplayer, 16, time, strength);
					return true;
		        } else if (commandLabel.equalsIgnoreCase("hunger")) {
		            doEffect(eplayer, 17, time, strength);
					return true;
		        } else if (commandLabel.equalsIgnoreCase("weakness")) {
		            doEffect(eplayer, 18, time, strength);
					return true;
		        } else if (commandLabel.equalsIgnoreCase("poison")) {
		            doEffect(eplayer, 19, time, strength);
					return true;
		        }
				return false; 
		
	}

	public Configuration playerConfig(Player player_name)
	{
		return playerConfig(player_name.getName());
	}
	
	public Configuration loadPlayerData(String player_name)
	{
		Configuration playerData = null;
		
		if (player_name == null)
			return null;
		
		Configuration config = (Configuration) Player_Saves.get(player_name);
		config = null;
			if (config == null)
			{
				//The player save is missing! D:
				File statics = new File("Players/"+player_name+ ".yml");
		
				playerData = new Configuration(statics);
				playerData.load();
				
				Player_Saves.put(player_name, playerData); //Add the data to the dictionary!
				System.out.println("Loading save data for " + player_name);
			}
		
		
			return playerData;

	}
	
	public void saveData(String player_name)
	{

		Configuration config = (Configuration) Player_Saves.get(player_name);
		
			if (config != null)
			{
				config.save();
			}
	}
	
	public void playerClose(String player_name)
	{

		Configuration config = (Configuration) Player_Saves.get(player_name);
			if (config != null)
			{
				config.save();
				Player_Saves.remove(config); //Close the data. We shouldn't need it anymore ;)
				
				System.out.println("Removing save data for " + player_name);
			}

	}
	
	
	protected static Buildr_Converter_BlockToDrop BlockConverter;
	protected static Random rand;

	private final LGPlayerListener playerListener = new LGPlayerListener(this);
	private final LGBlockListener blockListener = new LGBlockListener(this);
	private final LGMobListener mobListener = new LGMobListener(this);
	
	/**
	 * Hook parser
	 */
	
	
	
	
	
	

	// NOTE: There should be no need to define a constructor any more for more
	// info on moving from
	// the old constructor see:
	// http://forums.bukkit.org/threads/too-long-constructor.5032/

	public int random_num(int max, int min) {
		return rand.nextInt(max) + min;
	}

	// Common methods
	public String playerName(Player player) {

		String display_name = player.getName();

		
		
		/*
		 * if (display_name.startsWith("[")) { display_name =
		 * display_name.substring(display_name.indexOf("]")+2); }
		 */

		return display_name;
	}

	public boolean canUseTool(Player player) {
		int tool_id = player.getItemInHand().getTypeId();

		List<String> allowed_tools = getStatic().getKeys("Tools");
		String equip_string = String.format("T%d", tool_id);
		if (allowed_tools.contains(equip_string)) {
			String skill = getStatic().getString("Tools." + equip_string);

			int required_level = getStatic().getInt(
					"Skills." + skill + ".Equipment." + equip_string, 0);
			int level = playerConfig(player).getInt(skill + ".level", 0);

			if (level < required_level) {
				player.sendMessage(ChatColor.RED + "You level in " + skill
						+ " does not permit you to use this tool.");
				return false;
			}
		}
		return true;
	}

	public int skillExperience(Player player, String skill_name) {
		int level = playerConfig(player).getInt(skill_name + ".experience", 0);
		return level;
	}
	
	public int skillLevel(Player player, String skill_name) {
		int level = playerConfig(player).getInt(skill_name + ".level", 0);
		return level;
	}

	public boolean gainEnergy(Player player, int mana_amount) {
		String display_name = player.getName();
		int exp = playerConfig(player).getInt("energy", 0);

		int exp_required = 100;
		if (mana_amount > 0 && exp == exp_required) {
			return true;
		}

		if (mana_amount >= 0 && exp < exp_required) {
			if (exp + mana_amount > exp_required)
				exp = exp_required;
			else
				exp += mana_amount;
		} else if (mana_amount <= 0 && exp >= 0) {
			if (exp + mana_amount >= 0)
				exp += mana_amount;
			else 
			{
				player.sendMessage(ChatColor.BLUE
						+ "You have run out of energy.");
				return false;
			}
		}


		int bars = exp / (exp_required / 20);

		String bar = "";
		int i;
		for (i = 0; i < 2 * bars; i++) {
			bar += "|";
		}
		for (i = 0; i < (20 - bars); i++) {
			bar += " ";
		}

		if (verbosePlayer(player))
			player.sendMessage(ChatColor.YELLOW + "Energy: [" + bar + "]");

		playerConfig(display_name).setProperty("energy", exp);

		if (mana_amount < 0)
			player.getWorld().playEffect(player.getLocation(),
					Effect.EXTINGUISH, 0);

		return true;
	}
    public static void removeInventoryItems(Inventory inv, Material type, int amount) {
        for (ItemStack is : inv.getContents()) {
            if (is != null && is.getType() == type) {
                int newamount = is.getAmount() - amount;
                if (newamount > 0) {
                    is.setAmount(newamount);
                    break;
                } else {
                    inv.remove(is);
                    amount = -newamount;
                    if (amount == 0) break;
                }
            }
        }
    }
	
	public boolean gainMana(Player player, int mana_amount)
	{
		/*
		String display_name = player.getName();
		int exp = playerConfig(player).getInt( "mana", 0);

		int exp_required = 100;
		if (mana_amount > 0 && exp == exp_required) {
			return true;
		}

		if (mana_amount >= 0 && exp < exp_required) {
			if (exp + mana_amount > exp_required)
				exp = exp_required;
			else
				exp += mana_amount;
		} else if (mana_amount <= 0 && exp >= 0) {
			if (exp + mana_amount >= 0)
				exp += mana_amount;
			else {
				player.sendMessage(ChatColor.BLUE
						+ "You do not have enough mana to perform this spell.");
				return false;
			}
		}

		int bars = exp / (exp_required / 20);

		String bar = "";
		int i;
		for (i = 0; i < 2 * bars; i++) {
			bar += "|";
		}
		for (i = 0; i < (20 - bars); i++) {
			bar += " ";
		}

		if (verbosePlayer(player))
			player.sendMessage(ChatColor.BLUE + "Mana: [" + bar + "]");

		
		
		playerConfig(player).setProperty( "mana", exp);

		if (mana_amount < 0)
			player.getWorld().playEffect(player.getLocation(),
					Effect.EXTINGUISH, 0);
*/
		
		
		
		int max_hunger = 20;
		
		if (mana_amount < 0)
			mana_amount/=4;

		int lvl = skillLevel((Player)player, "Magic");
    	
		
		if (player.getFoodLevel()+mana_amount >= max_hunger)
			player.setFoodLevel(max_hunger);
		
		
		if (player.getFoodLevel()+mana_amount < 0)
		{
			//Go to the health bar.
			player.setFoodLevel(0);
			
			int more_damage = (player.getFoodLevel()+mana_amount)*-1;
			player.damage(more_damage/2);
		}
		else
		{
			player.setFoodLevel(player.getFoodLevel()+mana_amount);
		}
		
		
		
		
		
		return true;
	}

	public void monKil(String mob_name, Player p)
	{
		List<String> tasks = getTasks().getKeys("Tasks");
		
		if (!mob_name.equalsIgnoreCase("")) {
			int g;
			boolean is_task = false;
			for (g = 0; g < tasks.size(); g++) {
				String coords2 = tasks.get(g);
				String mob_kills = getTasks().getString(
						"Tasks." + coords2 + ".Aims.Kill.Type", "");

				if (!mob_kills.equalsIgnoreCase("")) {
					if (mob_kills.equalsIgnoreCase(mob_name)) {
						// We have killed the mob! Woo!
						String melee_task_overall = playerConfig(p).getString(getTasks().getString(
												"Tasks." + coords2 + ".set"),
								"");

						if (melee_task_overall.equalsIgnoreCase("finished")) {
							// Do nothing

						} else {
							String melee_task = playerConfig(p).getString(getTasks()
													.getString(
															"Tasks." + coords2
																	+ ".set")
											+ ".Kill", "");
							if (melee_task.equalsIgnoreCase("finished")
									|| melee_task.equalsIgnoreCase("")) {
								// Do nothing

							} else {

								int remaining_kills = Integer
										.valueOf(melee_task);
								remaining_kills--;

								if (remaining_kills == 0) {
									p.sendMessage(ChatColor.YELLOW
											+ "You have finished the killing aspect of "
											+ getTasks()
													.getString(
															"Tasks." + coords2
																	+ ".set")
											+ "!");

									String creative_task = playerConfig(p)
											.getString(getTasks()
																	.getString(
																			"Tasks."
																					+ coords2
																					+ ".set")
															+ ".Collect", "");
									if (creative_task
											.equalsIgnoreCase("finished")
											|| Integer.valueOf(creative_task) <= 0) {
										// Do nothing
										playerConfig(p)
												.setProperty( getTasks()
																		.getString(
																				"Tasks."
																						+ coords2
																						+ ".set"),
														"finished");

										// Do nothing
										p.sendMessage(ChatColor.YELLOW
												+ "You have finished "
												+ getTasks().getString(
														"Tasks." + coords2
																+ ".set") + "!");

										List<String> equipment = getTasks()
												.getKeys(
														"Tasks." + coords2
																+ ".Rewards");

										if (equipment != null) {
											int s;
											for (s = 0; s < equipment.size(); s++) {
												String equipment_id = equipment
														.get(s);
												int equip_id = Integer
														.parseInt(equipment_id
																.substring(1));
												int amount = getTasks()
														.getInt("Tasks."
																+ coords2
																+ ".Rewards."
																+ equipment_id,
																0);

												ItemStack stack = new ItemStack(
														equip_id, amount);
												p.getInventory().addItem(stack);
												p.sendMessage(ChatColor.GOLD
														+ "You have gained "
														+ String.valueOf(amount)
														+ " "
														+ stack.getType()
																.toString());
												p.updateInventory();

											}
										}
									} else
										playerConfig(p)
												.setProperty(getTasks()
																		.getString(
																				"Tasks."
																						+ coords2
																						+ ".set")
																+ ".Kill",
														"finished");
								} else {
									p.sendMessage(ChatColor.YELLOW
											+ getTasks()
													.getString(
															"Tasks." + coords2
																	+ ".set")
											+ " task - Remaining kills: "
											+ String.valueOf(remaining_kills));
									playerConfig(p).setProperty(getTasks().getString(
															"Tasks." + coords2
																	+ ".set")
													+ ".Kill",
											String.valueOf(remaining_kills));
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void monsterKilled(Entity e, Player p) {
		
		if (!(e instanceof Player))
		{
			giveMoney(p, moneyForEntity(e));
		}
		
		
		String mob_name = nameForEntity(e);
		monKil(mob_name, p);
	}

	public void gainExperience(Player player, String skill_name, int block_exp)
	{
		if (verbosePlayer(player))
		{
			if (block_exp>0)
				player.sendMessage(ChatColor.GREEN + "You have gained "
					+ String.format("%d", block_exp) + "xp in " + skill_name
					+ "!");
			else 
				player.sendMessage(ChatColor.RED + "You have lost "
					+ String.format("%d", block_exp) + "xp in " + skill_name
					+ ".");
		}
		
        long sneak = Long.valueOf(playerConfig(player).getString("EXPBOOST", "0"));
		long time = System.currentTimeMillis();
		
		long since = time-sneak;
		if (since<(60*60*24*1000))
		{
			if (verbosePlayer(player))
				if (block_exp>0)
					player.sendMessage(ChatColor.YELLOW + "Experience Boost: "+ String.format("%d", (int)(block_exp*0.5)) + "xp in " + skill_name+ "!");
		}
		
		String display_name = playerName(player);

		int exp = playerConfig(player).getInt(skill_name + ".experience", 0);
		int level = playerConfig(player).getInt(skill_name + ".level", 0);

		int base = getStatic().getInt("Skills." + skill_name + ".Base", 0);
		int increment = getStatic().getInt(
				"Skills." + skill_name + ".Increment", 0);

		if (since<(60*60*24*1000))
		{
			exp += (int)(block_exp*0.5);
		}
		
		//Faction boost!!! :D
		Player[] p = Bukkit.getServer().getOnlinePlayers();
		Player pla = null;
		
		int a;
		for (a=0; a < p.length; a++)
		{
			Player pa = p[a];
			if (pa.getName().equalsIgnoreCase(player.getName()))
			{
				pla = pa;
				break;
			}
		}
		
		/*
		if (pla!=null)
		{
			Faction g = ((FPlayer)pla).getFaction();
			int boosts = 0;
			int i;
			
			if (g != null)
			{
				
				Factions d = getFactions();
				List<Entity> surrounding = player.getNearbyEntities(30,30,30);
		    	
		    	for (i=0; i < surrounding.size(); i++)
		    	{
		    		Entity thing = surrounding.get(i);
		    		if (thing instanceof Player || thing instanceof CraftPlayer)
		    		{
		
		    			Player plaa = null;
		    			
		    			for (a=0; a < p.length; a++)
		    			{
		    				Player pa = p[a];
		    				if (pa.getName().equalsIgnoreCase(((Player)thing).getName()))
		    				{
		    					plaa = pa;
		    					break;
		    				}
		    			}
		    			
		    			FPlayer pl = (FPlayer)plaa;
		    			Faction f = pl.getFaction();
		    			
		    			if (f != null && g == f)
		    			{
		    				boosts++;
		    				//EXP BONUS
		    				
		    			}
		    		}
		    	}
		    	
			}
			
			if (boosts > 0)
				if (verbosePlayer(player))
					if (block_exp>0)
						player.sendMessage(ChatColor.YELLOW + "Faction exp increase ("+ String.valueOf(boosts)+" nearby members): "+ String.format("%d", (int)(block_exp*0.2*boosts)) + "xp in " + skill_name+ "!");
		
			
			if (boosts > 0)
			{
				exp += (int)(block_exp*0.2*boosts);
			}
		
		}
		*/
		
		exp += block_exp;

		if (exp <=0)
			exp =0;
		
		int exp_required = base + increment * level;
		int bars = exp / (exp_required / 20);

		int i;
		String bar = "";
		for (i = 0; i < 2 * bars; i++) {
			bar += "|";
		}
		for (i = 0; i < (20 - bars); i++) {
			bar += " ";
		}

		if (verbosePlayer(player))
			player.sendMessage(ChatColor.GREEN + "[" + bar + "]");

		if (exp >= exp_required) {
			level += 1;
			exp = exp - exp_required;

			playerConfig(player).setProperty(skill_name + ".level",
					level);
			player.sendMessage(ChatColor.YELLOW
					+ "Congratulations! You have advanced to level "
					+ String.format("%d", level) + " in " + skill_name + ".");
			
			if (skill_name.equalsIgnoreCase("Sprint"))
			{
				int ti2merID = playerConfig(player).getInt("sprintTimer", 0);
				player.getServer().getScheduler().cancelTask(ti2merID);
    	    	LGSprinter sprint = new LGSprinter(player, this);
    	    	
    	    	int stam = skillLevel(player, "Sprint");
    			int boost = stam*3;
    	    	
    	    	int staminaBoosts = playerConfig(player).getInt("staminaBoost", 0);
    			staminaBoosts++;
    			int timerID = player.getServer().getScheduler().scheduleSyncRepeatingTask(this, sprint, 10*staminaBoosts+boost, 10*staminaBoosts+boost);
    			playerConfig(player).setProperty( "sprintTimer", timerID);
			}

			// Display the appropriate message, if any
			String display_string = getStatic().getString(
					"Skills." + skill_name + ".Messages.L"
							+ String.format("%d", level), "");
			if (!display_string.isEmpty()) {
				player.sendMessage(ChatColor.YELLOW + display_string);
			}

		}

		playerConfig(player).setProperty(skill_name + ".experience", exp);

		updateName(player);
		// getConfig().save(); //TURN THIS OFF LATER
	}
	
	public void saveInventory(Player p, String inven_name)
	{
		Inventory inventory = p.getInventory();
		
		int i;
		for (i=0; i < 36; i++)
		{
			ItemStack itm = inventory.getItem(i);
			
			if (itm != null)
			{
				int durability = itm.getDurability();
				int id = itm.getTypeId();
				int amt = itm.getAmount();
	
				if (amt > 0 && itm != null)
				{
				
					playerConfig(p).setProperty("Inventory."+inven_name+"."+String.valueOf(i)+".D", durability);
					playerConfig(p).setProperty("Inventory."+inven_name+"."+String.valueOf(i)+".ID", id);
					playerConfig(p).setProperty("Inventory."+inven_name+"."+String.valueOf(i)+".AM", amt);
				
				}
			}
		}
		
		p.sendMessage(ChatColor.YELLOW+"Inventory " +inven_name+" saved.");
	}

	public void loadInventory(Player p, String inven_name)
	{
		
		
		List<String> keys = playerConfig(p).getKeys("Inventory."+inven_name);
		p.getInventory().clear();
		
		if (keys != null)
		{
			int i;
			for (i=0; i < keys.size(); i++)
			{
				String key = keys.get(i);
				int k = Integer.valueOf(key);
				
				int durability = playerConfig(p).getInt("Inventory."+inven_name+"."+key+".D", 0);
				int id = playerConfig(p).getInt("Inventory."+inven_name+"."+key+".ID", 0);
				int amount = playerConfig(p).getInt("Inventory."+inven_name+"."+key+".AM", 0);
				
				p.getInventory().setItem(k, new ItemStack(id, amount));
			}
		
		}
		
		p.updateInventory();
		
		p.sendMessage(ChatColor.YELLOW+"Inventory " +inven_name+" loaded.");
		
	}
	
	public void updateName(Player player) {
		String chat_color = playerConfig(player).getString("nameColor", ChatColor.WHITE.toString());

		if (chat_color.equalsIgnoreCase(ChatColor.WHITE.toString()))
		{
			int ignore_nomad = 1;
        	
        	
			if (player.isOp()) {
				chat_color = ChatColor.GOLD.toString();
			} else if (playerConfig(player).getString("nomad", "")
					.equalsIgnoreCase("true")&&ignore_nomad==0) {
				chat_color = ChatColor.GRAY.toString();
			} else if (playerConfig(player).getString("mod", "").equals("true"))
				{
	
					chat_color = ChatColor.YELLOW.toString();
				}
				else if (playerConfig(player).getString("superguide", "")
				
					.equalsIgnoreCase("true")) {
				chat_color = ChatColor.DARK_PURPLE.toString();
			}
			else if (playerConfig(player).getString("guide", "")
					.equalsIgnoreCase("true")) {
				chat_color = ChatColor.DARK_GREEN.toString();
			}
			

		}
		
		int strlvl = skillLevel(player, "Strength");
		int magiclvl = skillLevel(player, "Magic");
		int archerylvl = skillLevel(player, "Archery");

		String spc = playerConfig(player).getString("current_spec", "");
		int spclvl = skillLevel(player, spc);
		
		if (playerConfig(player).getString("mod", "").equals("true"))
			spc = "Mod";
		else if (playerConfig(player).getString("superguide", "").equalsIgnoreCase("true"))
				spc = "Senior Guide";
		
		
		
		if ((playerConfig(player).getString("admin", "false").equals("true")))
		{
			if ((playerConfig(player).getString("ADMIN_MODE", "false").equals("true")))
			{
				spc="Admin";
			}
			else
			{
				spc="";
			}
		}
		
		//plugin.getConfig().setProperty(player.getName()+".spec_lvl", "1");
		String houseID = getSettle().getString(player.getName()+".houseID", "");
		if (houseID.equalsIgnoreCase(""))
			houseID = "h";
		
		if (spc.equalsIgnoreCase(""))
			player.setDisplayName(String.format(chat_color + "[%s][%d|%d|%d] %s"
				+ ChatColor.WHITE, houseID, strlvl, archerylvl, magiclvl,
				playerName(player)));
		else
			player.setDisplayName(String.format(chat_color + "[%s][%s] %s"
					+ ChatColor.WHITE, houseID, spc,
					playerName(player)));
	}

	public void onDisable() {
		// TODO: Place any custom disable code here

		// NOTE: All registered events are automatically unregistered when a
		// plugin is disabled

		// EXAMPLE: Custom code, here we just output some info so we can check
		// all is well

		// CONFIG.save();
		System.out.println("Goodbye world!");
		
		getCon().save();
		getSettle().save();
		getTelepad().save();
		getTasks().save();
		getXrayers().save();
		save();
		
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
    			saveData(player.getName());
    		}
		 }
    	
	}


	public Configuration getStatic() {
		return STATIC_CONFIG;
	}

	public Configuration getSettle() {
		return SETTLEMENTS;
	}
	
	public Configuration getTasks()
	{
		return TASK_DATA;
	}
	
	public Configuration getXrayers()
	{
		return XRAYERS;
	}
	
	public Configuration getTelepad() {
		return TELEPAD_DATA;
	}

	
	public Configuration getBlocks()
	{
		return BLOCK_TIMERS;
	}
	

	public Configuration getCon() {
		return CONFIG;
	}
	
	public String nameForEntity(Entity dead) {
		String name = "";

		if (dead instanceof Player || dead instanceof CraftPlayer)
			name = "Player";
		else if (dead instanceof Ghast || dead instanceof CraftGhast)
			name = "Ghast";
		else if (dead instanceof Wolf || dead instanceof CraftWolf)
			name = "Wolf";
		else if (dead instanceof Creeper || dead instanceof CraftCreeper)
			name = "Creeper";
		else if (dead instanceof Zombie || dead instanceof CraftZombie)
			name = "Zombie";
		else if (dead instanceof Spider || dead instanceof CraftSpider)
			name = "Spider";
		else if (dead instanceof Skeleton || dead instanceof CraftSkeleton)
			name = "Skeleton";
		else if (dead instanceof Chicken || dead instanceof CraftChicken)
			name = "Chicken";
		else if (dead instanceof Pig || dead instanceof CraftPig)
			name = "Pig";
		else if (dead instanceof Sheep || dead instanceof CraftSheep)
			name = "Sheep";
		else if (dead instanceof Cow || dead instanceof CraftCow)
			name = "Cow";
		else if (dead instanceof Squid || dead instanceof CraftSquid)
			name = "Squid";
		else if (dead instanceof Enderman || dead instanceof CraftEnderman)
			name = "Enderman";
		else if (dead instanceof Enderman || dead instanceof CraftEnderman)
			name = "Slime";
		else if (dead instanceof Blaze || dead instanceof CraftBlaze)
			name = "Blaze";
		else if (dead instanceof EnderDragon || dead instanceof CraftEnderDragon)
			name = "Dragon";
		else if (dead instanceof Villager || dead instanceof CraftVillager)
			name = "Villager";
		else if (dead instanceof MushroomCow || dead instanceof CraftMushroomCow)
			name = "Mooshroom";
		
		return name;
	}

	public int experienceForEntity(Entity dead) {
		int experience = 0;

		if (dead instanceof Player || dead instanceof CraftPlayer)
		{
			//Lets calculate this beast!
			int percentage = 10; //You lost 10% in every skill. 
			float lose_amount = (float) (percentage/100.0);
			
			int total_loss = 0;
			
			if (getCon().getString("event", "").equalsIgnoreCase(""))
			{
				
				List<String> skills = getCon().getKeys("Skills");
				if (skills!=null)
				{
					for (int i =0; i < skills.size(); i++)
					{
						String skill = skills.get(i);
						int exp = skillExperience((Player)dead, skill);
					
						total_loss+=lose_amount*exp;
						gainExperience((Player)dead, skill, -1*((int)(lose_amount*exp)));
					}
				}
				
				float total_gain = ((float)(total_loss/20));
				experience = (int)total_gain;
			}
			else
			{
				experience = 1;
			}
			
			
		}
		else if (dead instanceof Ghast || dead instanceof CraftGhast)
			experience = 60;
		else if (dead instanceof Wolf || dead instanceof CraftWolf)
			experience = 62;
		else if (dead instanceof Creeper || dead instanceof CraftCreeper)
			experience = 32;
		else if (dead instanceof Giant || dead instanceof CraftGiant)
			experience = 182;
		else if (dead instanceof Silverfish || dead instanceof CraftSilverfish)
			experience = 12;
		else if (dead instanceof PigZombie || dead instanceof CraftPigZombie)
			experience = 5;
		else if (dead instanceof Zombie || dead instanceof CraftZombie)
			experience = 25;
		else if (dead instanceof Spider || dead instanceof CraftSpider)
			experience = 23;
		else if (dead instanceof Skeleton || dead instanceof CraftSkeleton)
			experience = 35;
		else if (dead instanceof Blaze || dead instanceof CraftBlaze)
			experience = 42;
		else if (dead instanceof EnderDragon || dead instanceof CraftEnderDragon)
			experience = 102;
		else if (dead instanceof Villager || dead instanceof CraftVillager)
			experience = 12;
		else if (dead instanceof MushroomCow || dead instanceof CraftMushroomCow)
			experience = 10;
		else if (dead instanceof Chicken || dead instanceof CraftChicken)
			experience = 6;
		else if (dead instanceof Pig || dead instanceof CraftPig)
			experience = 0;
		else if (dead instanceof Sheep || dead instanceof CraftSheep)
			experience = 5;
		else if (dead instanceof Cow || dead instanceof CraftCow||dead instanceof MushroomCow || dead instanceof CraftMushroomCow)
			experience = 10;
		else if (dead instanceof Squid || dead instanceof CraftSquid)
			experience = 15;
		else if (dead instanceof Enderman || dead instanceof CraftEnderman)
			experience = 52;
		else if (dead instanceof Slime || dead instanceof CraftSlime)
			experience = 19;

		return experience;
	}

	public int moneyForEntity(Entity dead) {
		int experience = 0;

		if (dead instanceof Player || dead instanceof CraftPlayer)
			experience = 0;
		else if (dead instanceof Ghast || dead instanceof CraftGhast)
			experience = 9;
		else if (dead instanceof Wolf || dead instanceof CraftWolf)
			experience = 15;
		else if (dead instanceof Creeper || dead instanceof CraftCreeper)
			experience = 5;
		else if (dead instanceof PigZombie || dead instanceof CraftPigZombie)
			experience = 2;
		else if (dead instanceof Zombie || dead instanceof CraftZombie)
			experience = 3;
		else if (dead instanceof Spider || dead instanceof CraftSpider)
			experience = 3;
		else if (dead instanceof Skeleton || dead instanceof CraftSkeleton)
			experience = 5;
		else if (dead instanceof Chicken || dead instanceof CraftChicken)
			experience = 1;
		else if (dead instanceof Pig || dead instanceof CraftPig)
			experience = 0;
		else if (dead instanceof Sheep || dead instanceof CraftSheep)
			experience = 1;
		else if (dead instanceof Cow || dead instanceof CraftCow)
			experience = 1;
		else if (dead instanceof Squid || dead instanceof CraftSquid)
			experience = 1;
		else if (dead instanceof Enderman || dead instanceof CraftEnderman)
			experience = 6;
		else if (dead instanceof Slime || dead instanceof CraftSlime)
			experience = 2;
		else if (dead instanceof Blaze || dead instanceof CraftBlaze)
			experience = 12;
		else if (dead instanceof EnderDragon || dead instanceof CraftEnderDragon)
			experience = 27;
		else if (dead instanceof Villager || dead instanceof CraftVillager)
			experience = 2;
		else if (dead instanceof MushroomCow || dead instanceof CraftMushroomCow)
			experience = 1;

		return experience;
	}

	
	public Player entityInBlastZone(Entity e) {
		// System.out.println("Death!");
		List<String> loaded_spells = getCon().getKeys("TempMagic");

		if (loaded_spells == null)
			return null;

		int i;
		for (i = 0; i < loaded_spells.size(); i++) {
			String id = loaded_spells.get(i);

			String locs = getCon()
					.getString("TempMagic." + id + ".Location");
			String[] pieces = locs.split(" ");
			Location loc = new Location(e.getWorld(), Float.valueOf(pieces[0]),
					Float.valueOf(pieces[1]), Float.valueOf(pieces[2]));

			if (loc.distance(e.getLocation()) < 15) {
				// Player caused this!

				Player p = null;
				String name = getCon().getString(
						"TempMagic." + id + ".Player");
				for (Player play : Bukkit.getServer().getOnlinePlayers()) {
					if (playerName(play).equalsIgnoreCase(name)) {
						p = play;
						break;
					}
				}

				if (p != null) {
					// Do something with player

					// Gain magic experience!
					int exp = experienceForEntity(e);
					String spell = getCon().getString(
							"TempMagic." + id + ".Spell");

					if (e.getEntityId() != p.getEntityId()) {
						if (e instanceof Player) {
							p.getServer().broadcastMessage(
									ChatColor.RED + ((Player) e).getName()
											+ " was killed by " + p.getName()
											+ " using " + spell);
						}
						else
						{
							
							
							//giveMoney(p, moneyForEntity(e));
							
							
							
							
							
							
						}

						monsterKilled(e, p);
						gainExperience(p, "Magic", exp);
					} else {
						p.getServer().broadcastMessage(
								ChatColor.RED + ((Player) e).getName()
										+ " accidently killed himself using "
										+ spell);
					}

					return p;
				}

				return null;
			}
		}
		return null;
	}
	
	public void giveMoney(Player p, int amount)
	{
		/*Methods.setMethod(Bukkit.getPluginManager()); 
		
		if (Methods.hasMethod())
		{
			MethodAccount m = Methods.getMethod().getAccount(p.getName());
			m.add(amount);
			
			
			if (verbosePlayer(p))
				p.sendMessage(ChatColor.YELLOW+String.format("You have gained %d coin.", amount));
		}*/
	}
	
	public double totalMoney(Player p)
	{
		Methods.setMethod(Bukkit.getPluginManager()); 
		
		if (Methods.hasMethod())
		{
			double m = Methods.getMethod().getAccount(p.getName()).balance();
			return m;
		}
		return 0;
	}

	public void removeMoney(Player p, int amount)
	{
		Methods.setMethod(Bukkit.getPluginManager()); 
		
		if (Methods.hasMethod())
		{
			MethodAccount m = Methods.getMethod().getAccount(p.getName());
			m.subtract(amount);
		}
	}
	
	public boolean verbosePlayer(Player p) {
		String verbose = playerConfig(p)
				.getString("verbose", "on");

		if (verbose.equalsIgnoreCase("on"))
			return true;
		else if (verbose.equalsIgnoreCase("off"))
			return false;

		return false;

	}

	public ArrayList<String> getColors() {
		ArrayList<String> str = new ArrayList<String>();
		str.add("aqua");
		str.add("blue");
		str.add("dark_aqua");
		str.add("dark_blue"); // Add
		str.add("dark_gray");
		str.add("dark_purple");
		str.add("dark_red");
		str.add("light_purple");
		str.add("red");
		str.add("yellow");

		return str;
	}

	public ChatColor colorFromString(String c) {
		if (c.equalsIgnoreCase("aqua"))
			return ChatColor.AQUA;
		else if (c.equalsIgnoreCase("blue"))
			return ChatColor.BLUE;
		else if (c.equalsIgnoreCase("dark_aqua"))
			return ChatColor.DARK_AQUA;
		else if (c.equalsIgnoreCase("dark_blue"))
			return ChatColor.DARK_BLUE;
		else if (c.equalsIgnoreCase("dark_gray"))
			return ChatColor.DARK_GRAY;
		else if (c.equalsIgnoreCase("dark_purple"))
			return ChatColor.DARK_PURPLE;
		else if (c.equalsIgnoreCase("dark_red"))
			return ChatColor.DARK_RED;
		else if (c.equalsIgnoreCase("light_purple"))
			return ChatColor.LIGHT_PURPLE;
		else if (c.equalsIgnoreCase("red"))
			return ChatColor.RED;
		else if (c.equalsIgnoreCase("yellow"))
			return ChatColor.YELLOW;

		return ChatColor.WHITE;
	}
	
	
	
	
	  private HashMap<String, InventoryLargeChest> chests;
	  private File dataFolder;
	    
	
	
	
	
	
	
	

    public void sendGlobalBlockChange(Location loc, Material type, byte dat)
    {
    	 for (Player player : Bukkit.getServer().getOnlinePlayers())
         {
    		 player.sendBlockChange(loc, type, dat);
         }
    }
    
    public Packet20NamedEntitySpawn packetMaker(LivingEntity actual_wolf, String name, String e) {
        Location loc = actual_wolf.getLocation();
        Packet20NamedEntitySpawn packet = new Packet20NamedEntitySpawn();
        packet.a = actual_wolf.getEntityId();
        packet.b = name; //Set the name of the player to the name they want.
        packet.c = (int) loc.getX();
        packet.c = MathHelper.floor(loc.getX() * 32.0D);
        packet.d = MathHelper.floor(loc.getY() * 32.0D);
        packet.e = MathHelper.floor(loc.getZ() * 32.0D);
        packet.f = (byte) ((int) loc.getYaw() * 256.0F / 360.0F);
        packet.g = (byte) ((int) (loc.getPitch() * 256.0F / 360.0F));
        packet.h = Integer.valueOf(e);
        return packet;
        
    }
    
	public void mirror(Player p, int type)
	{
		effectPlayer(p, "confusion", 1, 5);
		Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+p.getName()+"has stared into the magic mirror!");
		
        /*
        //Make everyone pigmen D:
        for (Player p2 : Bukkit.getServer().getOnlinePlayers())
        {
        	if (p2.getName().equalsIgnoreCase(p.getName()))
        		continue;
        	if (p2 == p)
        		continue;
        	
        	Packet24MobSpawn p24 = packetMaker(p2, (byte)57);
        	if (type == 0)
        		((CraftPlayer) p).getHandle().netServerHandler.sendPacket(p24);
        }
		*/
		
		int radius = 8;
		
		//Check for cave.
		int playerX = (int)p.getLocation().getX();
        int playerY = (int)p.getLocation().getY();
        int playerZ = (int)p.getLocation().getZ();
        List<Sign> signs = new ArrayList<Sign>();
        World world = p.getWorld();
        for(int x = (int)playerX-radius;x<playerX+radius;x ++){
            for(int z = (int)playerZ-radius;z<playerZ+radius;z ++){
                for(int y = (int)playerY-15;y<playerY+radius;y ++){
                    Block bs = world.getBlockAt(x,y,z);
                    
                    if (bs.getTypeId()==43||bs.getType()==Material.WOOD)
                    {
                    	if (type == 0)
                    		p.sendBlockChange(bs.getLocation(), Material.NETHER_BRICK, bs.getData());
                    	else if (type == 2)
                    		sendGlobalBlockChange(bs.getLocation(), Material.SNOW_BLOCK, bs.getData());
                    	else
                    		sendGlobalBlockChange(bs.getLocation(), Material.NETHER_BRICK, bs.getData());
                    	
                    		
                    }
                    else if (bs.getTypeId()==1)
                    {
                    	if (type == 0)
                    	p.sendBlockChange(bs.getLocation(), Material.OBSIDIAN, bs.getData());
                    	else if (type == 2)
                    		sendGlobalBlockChange(bs.getLocation(), Material.AIR, bs.getData());
                    	else
                    		sendGlobalBlockChange(bs.getLocation(), Material.OBSIDIAN, bs.getData());
                    }
                    else if (bs.getTypeId()==98)
                    {
                    	if (type == 0)
                    	p.sendBlockChange(bs.getLocation(), Material.NETHERRACK, bs.getData());
                    	else if (type == 2)
                    		sendGlobalBlockChange(bs.getLocation(), Material.ICE, bs.getData());
                    	else
                    		sendGlobalBlockChange(bs.getLocation(), Material.NETHERRACK, bs.getData());
                    }
                    else if (bs.getTypeId()==98)
                    {
                    	if (type == 0)
                    	p.sendBlockChange(bs.getLocation(), Material.NETHERRACK, bs.getData());
                    	else if (type == 2)
                    		sendGlobalBlockChange(bs.getLocation(), Material.GLOWSTONE, bs.getData());
                    	else
                    		sendGlobalBlockChange(bs.getLocation(), Material.NETHERRACK, bs.getData());
                    }
                    else if (bs.getTypeId()==37)
                    {
                    	if (type == 0)
                    	p.sendBlockChange(bs.getLocation(), Material.RED_MUSHROOM, bs.getData());
                    	else if (type == 2)
                    		sendGlobalBlockChange(bs.getLocation(), Material.YELLOW_FLOWER, bs.getData());
                    	else
                    		sendGlobalBlockChange(bs.getLocation(), Material.RED_MUSHROOM, bs.getData());
                    }
                    else if (bs.getTypeId()==53)
                    {
                    	if (type == 0)
                    	p.sendBlockChange(bs.getLocation(), Material.NETHER_BRICK_STAIRS, bs.getData());
                    	else if (type == 2)
                    		sendGlobalBlockChange(bs.getLocation(), Material.BRICK_STAIRS, bs.getData());
                    	else
                    		sendGlobalBlockChange(bs.getLocation(), Material.NETHER_BRICK_STAIRS, bs.getData());
                    }
                    else if (bs.getTypeId()==8)
                    {
                    	if (type == 0)
                    	p.sendBlockChange(bs.getLocation(), Material.LAVA, bs.getData());
                    	else if (type == 2)
                    		sendGlobalBlockChange(bs.getLocation(), Material.WATER, bs.getData());
                    	else
                    		sendGlobalBlockChange(bs.getLocation(), Material.LAVA, bs.getData());
                    }
                    else if (bs.getTypeId()==9)
                    {
                    	if (type == 0)
                    	p.sendBlockChange(bs.getLocation(), Material.LAVA, bs.getData());
                    	else if (type == 2)
                    		sendGlobalBlockChange(bs.getLocation(), Material.WATER, bs.getData());
                    	else
                    		sendGlobalBlockChange(bs.getLocation(), Material.LAVA, bs.getData());
                    }
                    else if (bs.getTypeId()==10)
                    {
                    	if (type == 0)
                    	p.sendBlockChange(bs.getLocation(), Material.LAVA, bs.getData());
                    	else if (type == 2)
                    		sendGlobalBlockChange(bs.getLocation(), Material.WATER, bs.getData());
                    	else
                    		sendGlobalBlockChange(bs.getLocation(), Material.LAVA, bs.getData());
                    }
                    else if (bs.getTypeId()==11)
                    {
                    	if (type == 0)
                    	p.sendBlockChange(bs.getLocation(), Material.LAVA, bs.getData());
                    	else if (type == 2)
                    		sendGlobalBlockChange(bs.getLocation(), Material.WATER, bs.getData());
                    	else
                    		sendGlobalBlockChange(bs.getLocation(), Material.LAVA, bs.getData());
                    }
                    else if (bs.getType()==Material.FENCE||bs.getType()==Material.GLASS||bs.getType()==Material.THIN_GLASS)
                    {
                    	if (type == 0)
                    	p.sendBlockChange(bs.getLocation(), Material.NETHER_FENCE, bs.getData());
                    	else if (type == 2)
                    		sendGlobalBlockChange(bs.getLocation(), Material.GLASS, bs.getData());
                    	else
                    		sendGlobalBlockChange(bs.getLocation(), Material.NETHER_FENCE, bs.getData());
                    }
                    else if (bs.getType()==Material.GRASS)
                    {
                    	if (type == 0)
                    	p.sendBlockChange(bs.getLocation(), Material.MYCEL, bs.getData());
                    	else if (type == 2)
                    		sendGlobalBlockChange(bs.getLocation(), Material.GRASS, bs.getData());
                    	else
                    		sendGlobalBlockChange(bs.getLocation(), Material.MYCEL, bs.getData());
                    }
                    else if (bs.getType()==Material.SAND)
                    {
                    	if (type == 0)
                    	p.sendBlockChange(bs.getLocation(), Material.SOUL_SAND, bs.getData());
                    	else if (type == 2)
                    		sendGlobalBlockChange(bs.getLocation(), Material.SNOW_BLOCK, bs.getData());
                    	else
                    		sendGlobalBlockChange(bs.getLocation(), Material.SOUL_SAND, bs.getData());
                    }
                    else if (bs.getTypeId() == 44)
                    {
                    	if (type == 0)
                    	p.sendBlockChange(bs.getLocation(), Material.FIRE, bs.getData());
                    	else if (type == 2)
                    		sendGlobalBlockChange(bs.getLocation(), Material.DOUBLE_STEP, (byte)1);
                    	else
                    		sendGlobalBlockChange(bs.getLocation(), Material.FIRE, bs.getData());
                    }
                    else if (bs.getTypeId() == 106)
                    {
                    	if (type == 0)
                    	p.sendBlockChange(bs.getLocation(), Material.IRON_FENCE, bs.getData());
                    	else if (type == 2)
                    		sendGlobalBlockChange(bs.getLocation(), Material.AIR, bs.getData());
                    	else
                    		sendGlobalBlockChange(bs.getLocation(), Material.IRON_FENCE, bs.getData());
                    }
                    else if (bs.getType() == Material.LONG_GRASS)
                    {
                    	if (type == 0)
                    	p.sendBlockChange(bs.getLocation(), Material.NETHER_WARTS, bs.getData());
                    	else if (type == 2)
                    		sendGlobalBlockChange(bs.getLocation(), Material.YELLOW_FLOWER, bs.getData());
                    	else
                    		sendGlobalBlockChange(bs.getLocation(), Material.NETHER_WARTS, bs.getData());
                    }
                    else if (bs.getType() == Material.IRON_FENCE)
                    {
                    	if (type == 0)
                    	p.sendBlockChange(bs.getLocation(), Material.AIR, bs.getData());
                    	else if (type == 2)
                    		sendGlobalBlockChange(bs.getLocation(), Material.THIN_GLASS, bs.getData());
                    	else
                    		sendGlobalBlockChange(bs.getLocation(), Material.AIR, bs.getData());
                    }
                    else if (bs.getType() == Material.LOG)
                    {
                    	if (type == 0)
                    	p.sendBlockChange(bs.getLocation(), Material.LOG, (byte) 1);
                    	else if (type == 2)
                    		sendGlobalBlockChange(bs.getLocation(), Material.LOG, (byte)2);
                    	else
                    		sendGlobalBlockChange(bs.getLocation(), Material.LOG, (byte)1);
                    }
                    else if (bs.getType() == Material.LEAVES)
                    {
                    	if (type == 0)
                    	p.sendBlockChange(bs.getLocation(), Material.WOOL, (byte)15);
                    	else if (type == 2)
                    		sendGlobalBlockChange(bs.getLocation(), Material.LEAVES, bs.getData());
                    	else
                    		sendGlobalBlockChange(bs.getLocation(), Material.WOOL, (byte)15);
                    }
                }
            }
            
        }
		
      
        if (type == 0)
        p.playEffect(p.getLocation(), Effect.RECORD_PLAY, 2262);
        else if (type == 2)
        {

            	  for (Player player : Bukkit.getServer().getOnlinePlayers())
                  {
            		  player.playEffect(p.getLocation(), Effect.RECORD_PLAY, 2264);
                  }
                  

        }
        else
        {
        	  for (Player player : Bukkit.getServer().getOnlinePlayers())
              {
        		  player.playEffect(p.getLocation(), Effect.RECORD_PLAY, 2262);
              }
              
        }
        

	}
	public Packet24MobSpawn packetMaker(Player p1, Byte id) {
        DataWatcher tmp = null;
        tmp = new DataWatcher();
        
        Location loc = p1.getLocation();
        Packet24MobSpawn packet = new Packet24MobSpawn();
        packet.a = ((CraftPlayer) p1).getEntityId();
        packet.b = id.byteValue();
        packet.c = MathHelper.floor(loc.getX() * 32.0D);
        packet.d = MathHelper.floor(loc.getY() * 32.0D);
        packet.e = MathHelper.floor(loc.getZ() * 32.0D);
        packet.f = (byte) ((int) loc.getYaw() * 256.0F / 360.0F);
        packet.g = (byte) ((int) (loc.getPitch() * 256.0F / 360.0F));
        Field datawatcher;
        try {
            datawatcher = packet.getClass().getDeclaredField("h");
            datawatcher.setAccessible(true);
            datawatcher.set(packet, tmp);
            datawatcher.setAccessible(false);
        } catch (Exception e) {
            return null;
        } 
        return packet;
    }

	public void setPermissions(Player p)
	{
		PermissionAttachment attack = p.addAttachment(Bukkit.getPluginManager().getPlugin("CraftBookMechanisms"));
		if (playerConfig(p).getString("gatePerk","false").equalsIgnoreCase("true"))
		{
			attack.setPermission("craftbook.mech.gate", true);
		}
		if (playerConfig(p).getString("liftPerk","false").equalsIgnoreCase("true"))
		{
			attack.setPermission("craftbook.mech.elevator", true);
		}
		if (playerConfig(p).getString("bridgePerk","false").equalsIgnoreCase("true"))
		{
			attack.setPermission("craftbook.mech.bridge", true);
		}
		if (playerConfig(p).getString("musicPerk","false").equalsIgnoreCase("true"))
		{
			p.addAttachment(Bukkit.getPluginManager().getPlugin("OcarinaSong")).setPermission("ocarina.time.sign", true);
    		p.addAttachment(Bukkit.getPluginManager().getPlugin("OcarinaSong")).setPermission("ocarina.zelda.sign", true);
    		p.addAttachment(Bukkit.getPluginManager().getPlugin("OcarinaSong")).setPermission("ocarina.awakening.sign", true);
    		
		}
		if (playerConfig(p).getString("wolfPerk","false").equalsIgnoreCase("true"))
		{
			p.addAttachment(Bukkit.getPluginManager().getPlugin("MobDisguise")).setPermission("mobdisguise.wolf", true);
		}
		if (playerConfig(p).getString("skeletonPerk","false").equalsIgnoreCase("true"))
		{
			p.addAttachment(Bukkit.getPluginManager().getPlugin("MobDisguise")).setPermission("mobdisguise.skeleton", true);
		}
		if (playerConfig(p).getString("zombiePerk","false").equalsIgnoreCase("true"))
		{
			p.addAttachment(Bukkit.getPluginManager().getPlugin("MobDisguise")).setPermission("mobdisguise.zombie", true);
		}
		
		attack.setPermission("craftbook.mech.bridge.use", true);
		attack.setPermission("craftbook.mech.elevator.use", true);
	}

	/*
	public SpoutPlugin getSpout()
	{
		SpoutPlugin spoutPlugin = (SpoutPlugin) this.getServer().getPluginManager()
				.getPlugin("Spout");

		return spoutPlugin;
	}

	private void setupSpout()
	{
		SpoutPlugin spoutPlugin = (SpoutPlugin) this.getServer().getPluginManager()
				.getPlugin("Spout");
		if (spoutPlugin != null) 
		{
			//LGSpoutListener spoutListener = new LGSpoutListener(this);

		}
	}
	*/
	
	public InventoryLargeChest getChest(String playerName) {
		InventoryLargeChest chest = chests.get(playerName.toLowerCase());

		if (chest == null)
			chest = addChest(playerName);

		return chest;
	}

	private InventoryLargeChest addChest(String playerName) {
		InventoryLargeChest chest = new InventoryLargeChest("Backpack", new TileEntityChest(), new TileEntityChest());
		chests.put(playerName.toLowerCase(), chest);
		return chest;
	}

	public void removeChest(String playerName)
	{
		chests.remove(playerName.toLowerCase());
	}
	
	

	public void load() {
		chests.clear();

		int loadedChests = 0;

		dataFolder.mkdirs();
		final FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".chest");
			}
		};
		for (File chestFile : dataFolder.listFiles(filter)) {
			try {
				final InventoryLargeChest chest = new InventoryLargeChest("Backpack", new TileEntityChest(), new TileEntityChest());
				final String playerName = chestFile.getName().substring(0, chestFile.getName().length() - 6);
				
				final BufferedReader in = new BufferedReader(new FileReader(chestFile));

				String line;
				int field = 0;
				while ((line = in.readLine()) != null) {
					if (line != "") {
						final String[] parts = line.split(":");
						try {
							int type = Integer.parseInt(parts[0]);
							int amount = Integer.parseInt(parts[1]);
							short damage = Short.parseShort(parts[2]);
							if (type != 0) {
								chest.setItem(field, new net.minecraft.server.ItemStack(type, amount, damage));
							}
						} catch (NumberFormatException e) {
							// ignore
						}
						++field;
					}
				}

				in.close();
				chests.put(playerName.toLowerCase(), chest);

				++loadedChests;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void save()
	{
		int savedChests = 0;

		dataFolder.mkdirs();

		for (String playerName : chests.keySet()) {
			final InventoryLargeChest chest = chests.get(playerName);

			try {
				final File chestFile = new File(dataFolder, playerName + ".chest");
				if (chestFile.exists())
					chestFile.delete();
				chestFile.createNewFile();

				final BufferedWriter out = new BufferedWriter(new FileWriter(chestFile));

				for (net.minecraft.server.ItemStack stack : chest.getContents()) {
					if (stack != null)
						out.write(stack.id + ":" + stack.count + ":" + stack.getData() + "\r\n");
					else
						out.write("0:0:0\r\n");
				}

				out.close();

				savedChests++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public void openBackpack(Player p)
	{
		((CraftPlayer)p).getHandle().a(getChest(p.getName()));
	}
	
	public void dumpChest(Player p, Location i)
	{
		if (chests.containsKey(p.getName().toLowerCase()))
		{
			final InventoryLargeChest chest = chests.get(p.getName().toLowerCase());
			
			for (net.minecraft.server.ItemStack stack : chest.getContents())
			{
				if (stack != null)
					p.getWorld().dropItemNaturally(i, new ItemStack(stack.id, stack.count));
			}
			
			int im = 0;
			for (net.minecraft.server.ItemStack stack : chest.getContents())
			{
				chest.setItem(im, null);
				im++;
			}
			
			chests.remove(p.getName().toLowerCase());
		}
	}
	
	public void onEnable()
	{
		
		this.dataFolder = new File("plugins/Legends/", "chests");
		this.chests = new HashMap<String, InventoryLargeChest>();
		load();
		
		// TODO: Place any custom enable code here including the registration of
		// any events
		Player_Saves = new HashMap<String,Configuration>();

		CONFIG = getConfiguration();

		File statics = new File("settlements_config.yml");

		SETTLEMENTS = new Configuration(statics);
		SETTLEMENTS.load();
		
		
		File td = new File("telepad_data.yml");

		TELEPAD_DATA = new Configuration(td);
		TELEPAD_DATA.load();
		
		File tpd = new File("task_data.yml");

		TASK_DATA = new Configuration(tpd);
		TASK_DATA.load();
		
		File xr = new File("xrayers.yml");

		XRAYERS = new Configuration(xr);
		XRAYERS.load();
		
		//TASK_DATA

		File blx = new File("block_timers.yml");
		BLOCK_TIMERS = new Configuration(blx);
		BLOCK_TIMERS.load();
		
		
		File statics2 = new File("legends_config.yml");
		STATIC_CONFIG = new Configuration(statics2);

		STATIC_CONFIG.load();

		BlockConverter = new Buildr_Converter_BlockToDrop();

		rand = new Random(System.currentTimeMillis());
		

		// Register our events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener,
				Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener,
				Priority.Highest, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener,
				Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_ITEM_HELD, playerListener,
				Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener,
				Priority.Highest, this);
		pm.registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, playerListener,
				Priority.Highest, this);
		pm.registerEvent(Event.Type.PLAYER_PICKUP_ITEM, playerListener,
				Priority.Highest, this);
		pm.registerEvent(Event.Type.PLAYER_DROP_ITEM, playerListener,
				Priority.Highest, this);
		pm.registerEvent(Event.Type.PLAYER_RESPAWN, playerListener,
				Priority.Lowest, this);
		pm.registerEvent(Event.Type.PLAYER_FISH, playerListener,
				Priority.Lowest, this);
		pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener,
				Priority.Lowest, this);
		
		pm.registerEvent(Event.Type.BLOCK_PHYSICS, blockListener,
				Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_CANBUILD, blockListener,
				Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener,
				Priority.Highest, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener,
				Priority.Highest, this);
		pm.registerEvent(Event.Type.SIGN_CHANGE, blockListener, Priority.High,
				this);
		pm.registerEvent(Event.Type.REDSTONE_CHANGE, blockListener,
				Priority.High, this);

		pm.registerEvent(Event.Type.ENTITY_DAMAGE, mobListener,
				Priority.Highest, this);
		pm.registerEvent(Event.Type.ENTITY_DEATH, mobListener, Priority.Normal,
				this);
		pm.registerEvent(Event.Type.ITEM_SPAWN, mobListener, Priority.Normal,
				this);
		pm.registerEvent(Event.Type.PROJECTILE_HIT, mobListener,
				Priority.Normal, this);
		pm.registerEvent(Event.Type.CREATURE_SPAWN, mobListener,
				Priority.Normal, this);
		
		// onItemSpawn
		// Register our commands
		getCommand("level").setExecutor(new LGLevels(this));
		getCommand("levels").setExecutor(new LGLevels(this));
		getCommand("resetPlayerName").setExecutor(new LGLevels(this));
		getCommand("createTask").setExecutor(new LGLevels(this));
		getCommand("verboseon").setExecutor(new LGLevels(this));
		getCommand("verboseoff").setExecutor(new LGLevels(this));
		getCommand("setLevel").setExecutor(new LGLevels(this));
		getCommand("resetPlayer").setExecutor(new LGLevels(this));
		getCommand("sendTutorial").setExecutor(new LGLevels(this));
		getCommand("set").setExecutor(new LGLevels(this));
		getCommand("who").setExecutor(new LGLevels(this));
		getCommand("task").setExecutor(new LGLevels(this));
		getCommand("getLevels").setExecutor(new LGLevels(this));
		getCommand("setFlag").setExecutor(new LGLevels(this));
		getCommand("nomad").setExecutor(new LGLevels(this));
		getCommand("addGuide").setExecutor(new LGLevels(this));
		getCommand("removeGuide").setExecutor(new LGLevels(this));
		getCommand("addMod").setExecutor(new LGLevels(this));
		getCommand("removeMod").setExecutor(new LGLevels(this));
		getCommand("addLWC").setExecutor(new LGLevels(this));
		getCommand("shop").setExecutor(new LGLevels(this));
		getCommand("helmet").setExecutor(new LGLevels(this));
		getCommand("sneak").setExecutor(new LGLevels(this));
		getCommand("tell").setExecutor(new LGLevels(this));
		getCommand("speak").setExecutor(new LGLevels(this));
		getCommand("minestatus").setExecutor(new LGLevels(this));
		getCommand("sarah").setExecutor(new LGLevels(this));
		getCommand("spec").setExecutor(new LGLevels(this));
		getCommand("pet").setExecutor(new LGLevels(this));
		//getCommand("teleport").setExecutor(new LGLevels(this));
		getCommand("citadel").setExecutor(new LGLevels(this));
		getCommand("resetHouse").setExecutor(new LGLevels(this));
		getCommand("event").setExecutor(new LGLevels(this));
		getCommand("ekit").setExecutor(new LGLevels(this));
		getCommand("lvl").setExecutor(new LGLevels(this));
		getCommand("exp").setExecutor(new LGLevels(this));
		getCommand("setEvent").setExecutor(new LGLevels(this));
		getCommand("c").setExecutor(new LGLevels(this));
		getCommand("s").setExecutor(new LGLevels(this));
		getCommand("potion").setExecutor(new LGLevels(this));
		getCommand("drug").setExecutor(new LGLevels(this));
		getCommand("pot").setExecutor(new LGLevels(this));
		getCommand("mirror").setExecutor(new LGLevels(this));
		getCommand("tutorial").setExecutor(new LGLevels(this));
		getCommand("admin").setExecutor(new LGLevels(this));
        getCommand("clone").setExecutor(new LGLevels(this));
		getCommand("town").setExecutor(new SaCommands(this));
		getCommand("setSettlementFlag").setExecutor(new SaCommands(this));
		getCommand("resetSettlement").setExecutor(new SaCommands(this));
		
		getCommand("mkick").setExecutor(new LGLevels(this));
		getCommand("mban").setExecutor(new LGLevels(this));
		getCommand("munban").setExecutor(new LGLevels(this));
		getCommand("mtp").setExecutor(new LGLevels(this));
		getCommand("mspawn").setExecutor(new LGLevels(this));

		// EXAMPLE: Custom code, here we just output some info so we can check
		// all is well
		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println(pdfFile.getName() + " version "
				+ pdfFile.getVersion() + " is enabled!");

		LGAutoSave autosave = new LGAutoSave(this);
		Bukkit.getServer().getScheduler()
				.scheduleSyncRepeatingTask(this, autosave, 6000, 6000);

		LGProtip protip = new LGProtip(this);
		Bukkit.getServer().getScheduler()
				.scheduleSyncRepeatingTask(this, protip, 0, 36000);
		
		
		//LGNPC_Spawner npcspawner = new LGNPC_Spawner(this);
		//Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, npcspawner, 600, 600);

		Bukkit.getServer().broadcastMessage(
				ChatColor.YELLOW + "Saving worlds... Please wait");
		List<World> world = Bukkit.getServer().getWorlds();

		int w;
		for (w = 0; w < world.size(); w++) {
			world.get(w).save();

			
			List<Player> players = world.get(w).getPlayers();

			for (Player player : players) {

				setPermissions(player);
				player.saveData();
				saveData(player.getName());
			}
		}

		Bukkit.getServer()
				.broadcastMessage(ChatColor.YELLOW + "Save complete!");

		// Create mana timers
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			updateName(player);

			LGManaBar mana_bar = new LGManaBar(player, this);
			int timerID = player.getServer().getScheduler().scheduleSyncRepeatingTask(this, mana_bar, 50, 50);
			playerConfig(player).setProperty("manaTimer", timerID);
			
			LGSprinter sprint = new LGSprinter(player, this);
			
			
			int staminaBoosts = playerConfig(player).getInt("staminaBoost", 0);
			staminaBoosts++;

			int stam = skillLevel(player, "Sprint");
			int boost = stam*3;
			
			timerID = player.getServer().getScheduler().scheduleSyncRepeatingTask(this, sprint, 10*staminaBoosts+boost, 10*staminaBoosts+boost);
			
			playerConfig(player).setProperty( "sprintTimer", timerID);
			
		}

		List<String> loaded_spells = getCon().getKeys("TempMagic");

		if (loaded_spells != null) {

			int i;
			for (i = 0; i < loaded_spells.size(); i++) {
				String id = loaded_spells.get(i);
				getCon().removeProperty("TempMagic." + id);
			}

		}

		Bukkit.getServer().broadcastMessage(
				ChatColor.YELLOW + "Loaded: Legends, beta 1.02");
		Bukkit.getServer()
				.broadcastMessage(
						ChatColor.RED
								+ "[Protip]"
								+ ChatColor.YELLOW
								+ " Post on minecraftforum and more players will join the server! Go to http:/tinyurl.com/skycraft4. You may receive a free golden apple from ShadovvMoon for your troubles!");


	}

	public String currentSettlement(Location loc, Player player) {
		java.util.List<String> sett = getSettle().getKeys("Settlements");

		if (sett != null) {
			boolean too_close = false;
			int i;
			for (i = 0; i < sett.size(); i++) {
				String settlement_id = sett.get(i);
				String center_loc = getSettle().getString(
						"Settlements." + settlement_id + ".Middle", "");

				int size = 0;

				String type = getSettle().getString(
						"Settlements." + settlement_id + ".Type", "");
				if (type.equalsIgnoreCase("[Settlement]"))
					size = 5;
				else if (type.equalsIgnoreCase("[Colony]"))
					size = 22;
				else if (type.equalsIgnoreCase("[Village]"))
					size = 37;
				else if (type.equalsIgnoreCase("[Town]"))
					size = 72;

				if (!center_loc.equalsIgnoreCase("")) {
					String[] crds = center_loc.split(" ");
					Location settlement_loc = new Location(player.getWorld(),
							Integer.valueOf(crds[0]), Integer.valueOf(crds[1]),
							Integer.valueOf(crds[2]));
					if (settlement_loc.distance(loc) <= size * 2) {
						return settlement_id;
					}

				}
			}
		}
		return "";
	}

	public void addLWC(Player player)
	{
		LWC the_lwc = LWC.getInstance();
		LimitsModule mod = ((LimitsModule) the_lwc.getModuleLoader().getModule(
				LimitsModule.class));

		int current_limit = playerConfig(player).getInt("lwc_limit",
				0);
		current_limit++;

		mod.set("players." + player.getName() + ".limit", current_limit);
		mod.set("players." + player.getName() + ".chest", current_limit);
		mod.set("players." + player.getName() + ".furnace", current_limit);
		mod.set("players." + player.getName() + ".dispenser", current_limit);
		mod.set("players." + player.getName() + ".wooden_door", current_limit);
		mod.set("players." + player.getName() + ".iron_door", current_limit);

		mod.save();

		playerConfig(player).setProperty("lwc_limit", current_limit);
		
		
	}

	public boolean isDebugging(final Player player) {
		if (debugees.containsKey(player)) {
			return debugees.get(player);
		} else {
			return false;
		}
	}

	public void setDebugging(final Player player, final boolean value) {
		debugees.put(player, value);
	}

	public Buildr_Converter_BlockToDrop blockConverter() {
		// TODO Auto-generated method stub
		return BlockConverter;
	}
	
	
	
	public final RodActivation rodActivation = new RodActivation(this);
	public final CatchFish catchFish = new CatchFish();
	private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
	public final DropFish dropFish = new DropFish();
	
	
	public class CatchDelay {
		Timer timer;

		public CatchDelay(int seconds) {
			timer = new Timer();
			timer.schedule(new RemindTask(), seconds * 1000);
		}

		class RemindTask extends TimerTask {
			@SuppressWarnings("static-access")
			public void run() {

				Player player = rodActivation.fishingPlayer;
				long rodCastTime = rodActivation.fishingTime.get(player);
				
				
				
				
				if (catchFish.isCaughtFish() == true && rodCastTime >= 5000L) {
					
					
				}

				timer.cancel(); // Terminate the timer thread
			}
		}
	}
		
		


}

package ShadovvMoon.Legend;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPigZombie;
import org.bukkit.craftbukkit.entity.CraftPlayer;

import net.minecraft.server.Packet20NamedEntitySpawn;
import net.minecraft.server.Packet24MobSpawn;
import net.minecraft.server.Packet29DestroyEntity;

/*import com.griefcraft.lwc.LWC;
import com.griefcraft.model.LWCPlayer;
import com.griefcraft.model.Protection;
import com.griefcraft.modules.limits.LimitsModule;
import com.griefcraft.util.config.Configuration;*/
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class LGLevels implements CommandExecutor {
	private final Legends plugin;

	public LGLevels(Legends plugin) {
		this.plugin = plugin;
	}

	public Player getPlayer(World w, String name) {

		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (player.getName().equalsIgnoreCase(name))
				return player;
		}

		return null;

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] split) {
		String cmd = command.getLabel();
		if (cmd.equalsIgnoreCase("levels") || cmd.equalsIgnoreCase("level")
				|| cmd.equalsIgnoreCase("getLevels")) {
			if (!(sender instanceof Player)) {
				return false;
			}

			Player player = (Player) sender;
			if (player.isOp() && cmd.equalsIgnoreCase("getLevels")) {
				String player_name = split[0];
				player = getPlayer(player.getWorld(), player_name);
			}

			List<String> skills = plugin.getStatic().getKeys("Skills");
			int g;
			boolean skilled = false;

			for (g = 0; g < skills.size(); g++) {
				String skill_name = skills.get(g);

				int exp = plugin.playerConfig(player).getInt(skill_name
								+ ".experience", 0);
				int level = plugin.playerConfig(player)
						.getInt(skill_name
								+ ".level", 0);

				int base = plugin.getStatic().getInt(
						"Skills." + skill_name + ".Base", 0);
				int increment = plugin.getStatic().getInt(
						"Skills." + skill_name + ".Increment", 0);

				int exp_required = base + increment * level;
				int bars = exp / (exp_required / 20);

				String bar = "";
				int i;
				for (i = 0; i < 2 * bars; i++) {
					bar += "|";
				}
				for (i = 0; i < (20 - bars); i++) {
					bar += " ";
				}

				sender.sendMessage(ChatColor.GREEN + "[" + bar + "]"
						+ ChatColor.YELLOW + " Level "
						+ String.format("%d", level) + " " + skill_name + ".");

			}
		} else if (cmd.equalsIgnoreCase("resetPlayerName")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;

			player.sendMessage(ChatColor.GREEN + "resetting");

			player.setDisplayName("bhsgoclub");
		} else if (cmd.equalsIgnoreCase("createTask")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			plugin.playerConfig(player).setProperty("DisplayCoords", "YES");

			player.sendMessage(ChatColor.GREEN
					+ "Click the block to display coordinates.");

		} else if (cmd.equalsIgnoreCase("verboseon")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			player.sendMessage(ChatColor.YELLOW
					+ "Now displaying experience and mana gains.");
			plugin.playerConfig(player).setProperty("verbose", "on");
		} else if (cmd.equalsIgnoreCase("verboseoff")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;

			player.sendMessage(ChatColor.YELLOW
					+ "No longer displaying experience and mana gains.");
			plugin.playerConfig(player).setProperty("verbose", "off");
		} else if (cmd.equalsIgnoreCase("setLevel")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			if (player.getName().equalsIgnoreCase("bhsgoclub")) {

				String skill_name = split[0];
				String player_name = split[1];
				String level = split[2];

				Player p = getPlayer(player.getWorld(), player_name);

				if (p != null) {
					plugin.playerConfig(p).setProperty(skill_name + ".level",
							Integer.valueOf(level));
				}
			}
		} else if (cmd.equalsIgnoreCase("who")) {
			if (!(sender instanceof Player)) {
				return false;
			}

			String who = "";
			Player player = (Player) sender;

			for (Player p : player.getWorld().getPlayers()) {
				who += ", " + p.getName();
			}

			player.sendMessage(who);

		} else if (cmd.equalsIgnoreCase("resetPlayer")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			if (player.getName().equalsIgnoreCase("bhsgoclub")) {

				String player_name = split[0];
				Player p = getPlayer(player.getWorld(), player_name);

				if (p != null) {
					List<String> skills = plugin.getStatic().getKeys("Skills");
					int g;
					boolean skilled = false;

					p.sendMessage(ChatColor.RED
							+ "Your experience has been wiped.");
					for (g = 0; g < skills.size(); g++) {
						String skill_name = skills.get(g);

						plugin.playerConfig(p).setProperty(skill_name
										+ ".experience", 0);
						plugin.playerConfig(p).setProperty( skill_name
										+ ".level", 0);
					}

					if (p != null) {
						p.getInventory().clear();
						p.updateInventory();
						p.sendMessage(ChatColor.RED
								+ "You have been sent to the tutorial.");


						p.teleport(new Location(player.getWorld(), 670, 20, 41));

						plugin.playerConfig(p).setProperty("archerytask",
								"started");
						plugin.playerConfig(p).setProperty("magictask", "started");
						plugin.playerConfig(p).setProperty("meleetask", "started");
						plugin.playerConfig(p).setProperty("miningtask","started");
						plugin.playerConfig(p).setProperty("fishingtask", "started");
						plugin.playerConfig(p).setProperty("woodcuttingtask", "started");
					}
				}
			}
		} else if (cmd.equalsIgnoreCase("speak")) {
			if (!(sender instanceof Player))
			{
				String msg = split[0];
				for (int i =1; i < split.length; i++)
				{
					String s = split[i];
					if (!s.equalsIgnoreCase(""))
					{
						msg+=(" "+s);
					}
				}
				Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+msg);
				return true;
			}
			
			Player player = (Player) sender;
			if (player.isOp()) {

				String msg = split[0];
				for (int i =1; i < split.length; i++)
				{
					String s = split[i];
					if (!s.equalsIgnoreCase(""))
					{
						msg+=(" "+s);
					}
				}
				Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+msg);
			}
		} else if (cmd.equalsIgnoreCase("minestatus")) {
			if (!(sender instanceof Player))
			{
				String name = split[0];
				
				plugin.giveMoney(getPlayer(null, name), 100);
				Bukkit.getServer().broadcastMessage(ChatColor.RED+"[Notice] "+ChatColor.YELLOW+name+" voted on " + ChatColor.GREEN+"minestatus" + ChatColor.YELLOW+" and received " +ChatColor.GREEN+"100c"+ChatColor.YELLOW+"! Go to "+ChatColor.GREEN+"bit.ly/skyvote"+ChatColor.YELLOW+" and easily gain 100c for yourself!");
				return true;
			}
		}
		else if (cmd.equalsIgnoreCase("sarah")) {
			if (!(sender instanceof Player))
			{
				String msg = split[0];
				for (int i =1; i < split.length; i++)
				{
					String s = split[i];
					if (!s.equalsIgnoreCase(""))
					{
						msg+=(" "+s);
					}
				}
				Bukkit.getServer().broadcastMessage(ChatColor.WHITE+"[o] "+ChatColor.AQUA+"[h][0:0:0] Sarah"+ChatColor.WHITE+": " + msg);
				return true;
			}
			
			Player player = (Player) sender;
			if (player.isOp()) {

				String msg = split[0];
				for (int i =1; i < split.length; i++)
				{
					String s = split[i];
					if (!s.equalsIgnoreCase(""))
					{
						msg+=(" "+s);
					}
				}
				Bukkit.getServer().broadcastMessage(ChatColor.WHITE+"[o] "+ChatColor.GREEN+"[h][0:0:0] Sarah"+ChatColor.WHITE+": " + msg);
			}
		} 
		else if (cmd.equalsIgnoreCase("sendTutorial")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			if (player.isOp()) {

				String player_name = split[0];
				Player p = getPlayer(player.getWorld(), player_name);

				if (p != null) {
					p.getInventory().clear();
					p.updateInventory();
					p.sendMessage(ChatColor.RED
							+ "You have been sent to the tutorial.");

					p.teleport(new Location(player.getWorld(), 670, 20, 41));

					plugin.playerConfig(p).setProperty("archerytask",
							"started");
					plugin.playerConfig(p).setProperty("magictask", "started");
					plugin.playerConfig(p).setProperty("meleetask", "started");
					plugin.playerConfig(p).setProperty("miningtask","started");
					plugin.playerConfig(p).setProperty("fishingtask", "started");
					plugin.playerConfig(p).setProperty("woodcuttingtask", "started");
				}
			}
			
		
		}
		else if (cmd.equalsIgnoreCase("exp"))
		{
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			if (player.isOp())
			{
				player.setExperience(Integer.valueOf(split[0]));
			}
		}
		else if (cmd.equalsIgnoreCase("lvl"))
		{
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			if (player.isOp())
			{
				player.setLevel(Integer.valueOf(split[0]));
			}
		}
		else if (cmd.equalsIgnoreCase("c"))
		{
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			if (player.isOp())
			{
				player.setGameMode(GameMode.CREATIVE);
			}
		}
		else if (cmd.equalsIgnoreCase("s"))
		{
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			if (player.isOp())
			{
				player.setGameMode(GameMode.SURVIVAL);
			}
		}
		else if (cmd.equalsIgnoreCase("admin"))
		{
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			if ((plugin.playerConfig(player).getString("admin", "false").equals("true")))
			{
				if ((plugin.playerConfig(player).getString("ADMIN_MODE", "false").equals("true")))
				{
					plugin.playerConfig(player).setProperty("ADMIN_MODE", "false");
					player.sendMessage(ChatColor.GOLD+"You are no longer in admin mode.");
					
					player.setOp(false);
					player.setGameMode(GameMode.SURVIVAL);
					
					plugin.updateName(player);
					
					plugin.saveInventory(player, "Admin");
					plugin.loadInventory(player, "Normal");
				}
				else
				{
					plugin.playerConfig(player).setProperty("ADMIN_MODE", "true");
					player.sendMessage(ChatColor.GOLD+"You are now in admin mode.");
					
					player.setOp(true);
					player.setGameMode(GameMode.CREATIVE);
					
					plugin.updateName(player);
					
					plugin.saveInventory(player, "Normal");
					plugin.loadInventory(player, "Admin");
				}
			}
		}
			 else if (cmd.equalsIgnoreCase("set")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;

			Location loc = player.getLocation();
			String coords = String.format("%d %d %d", (int) loc.getX(),
					(int) loc.getY(), (int) loc.getZ());

			plugin.playerConfig(player)
					.setProperty("teleport_coordinates",
							coords);
			player.sendMessage(ChatColor.YELLOW
					+ "Your current location has been stored.");
		}
		
		
		
		
			 else if (cmd.equalsIgnoreCase("mkick")||cmd.equalsIgnoreCase("mban")||cmd.equalsIgnoreCase("munban")||cmd.equalsIgnoreCase("mtp")||cmd.equalsIgnoreCase("mspawn"))
				{
				 
				 Player player;
				
					if (!(sender instanceof Player))
					{
						player = null;
					}
					else
					{
						player =  (Player) sender;	
					}
					
					if (player == null || player.isOp() || (plugin.playerConfig(player).getString("mod", "").equals("true")))
					{
						if (cmd.equalsIgnoreCase("mspawn"))
						{
							if (player==null)
								return false;
							
							player.teleport(new Location(player.getWorld(), 9, 61, 113));
							return true;
						}
						
						String player_name = split[0];

						Player p = getPlayer(player.getWorld(), player_name);
						
						if (cmd.equalsIgnoreCase("mban"))
						{
							Bukkit.getServer().broadcastMessage(ChatColor.RED+player_name+" has been banned from the server.");
							if (p != null)
								p.kickPlayer("Contact ShadovvMoon on the forum to request an unban.");
							
							plugin.getSettle().setProperty("Bans."+player_name, "Banned");
							return true;
						}
						else if (cmd.equalsIgnoreCase("munban"))
						{
							Bukkit.getServer().broadcastMessage(ChatColor.GREEN+player_name+" has been unbanned from the server.");
	
							plugin.getSettle().removeProperty("Bans."+player_name);
							
							return true;
						}
						
						//LOAD UP THE PLAYER CONFIG?
						
						if (p != null)
						{
							if (cmd.equalsIgnoreCase("mkick"))
							{
								Bukkit.getServer().broadcastMessage(ChatColor.GREEN+player_name+" was kicked from the server.");
								
								if (split.length>1) p.kickPlayer(split[1]);
								else p.kickPlayer("");
							}
							else if (cmd.equalsIgnoreCase("mtp"))
							{
								if (player==null)
									return false;
								player.sendMessage(ChatColor.RED+"This command is disabled.");
							}
							    	
						}
						else
						{
							player.sendMessage(ChatColor.RED
									+ "That player is not currently online!");
						}
					} 
					else {
						player.sendMessage(ChatColor.RED
								+ "You don't have permission for this command!");
					}

					return true;
				}  
		
		
		
		
		 else if (cmd.equalsIgnoreCase("removeGuide"))
		{
		
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			if (player.isOp() || (plugin.playerConfig(player).getString("superguide", "").equalsIgnoreCase("true"))|| (plugin.playerConfig(player).getString("mod", "").equals("true")))
			{
				String player_name = split[0];

				
				//LOAD UP THE PLAYER CONFIG?
				Player p = getPlayer(player.getWorld(), player_name);
				if (p==null)
				{
					plugin.loadPlayerData(player_name);
				}
				
				if (plugin.playerConfig(player_name).getString("guide", "").equals("true"))
				{
					plugin.playerConfig(player_name).setProperty( "guide",
							"");
					player.sendMessage(ChatColor.YELLOW
							+ "The player is no longer a guide.");

					Bukkit.getServer().broadcastMessage(
							ChatColor.YELLOW + player_name
									+ " is the newest demoted guide in Skycraft!");
					
					if (p!=null)
						plugin.updateName(p);

				}
				else
				{
					player.sendMessage(ChatColor.RED
							+ "That player is not a guide!");
				}
				
				p = getPlayer(player.getWorld(), player_name);
				if (p!=null)
					plugin.updateName(p);
				else
				{
					//CLOSE
					plugin.playerClose(player_name);
				}
			} 
			else {
				player.sendMessage(ChatColor.RED
						+ "You don't have permission for this command!");
			}

			return true;
		}  
		else if (cmd.equalsIgnoreCase("addGuide")) {
		
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			if (player.isOp() || (plugin.playerConfig(player).getString("superguide", "").equalsIgnoreCase("true"))|| (plugin.playerConfig(player).getString("mod", "").equals("true"))) {
				String player_name = split[0];

				
				//LOAD UP THE PLAYER CONFIG?
				Player p = getPlayer(player.getWorld(), player_name);
				if (p==null)
				{
					plugin.loadPlayerData(player_name);
				}
				
				if (!plugin.playerConfig(player_name).getString("nomad", "").equals("true"))
				{
					plugin.playerConfig(player_name).setProperty( "guide",
							"true");
					player.sendMessage(ChatColor.YELLOW
							+ "The player is now a guide.");

					Bukkit.getServer().broadcastMessage(
							ChatColor.YELLOW + player_name
									+ " is the newest guide in Skycraft!");

					if (p!=null)
						plugin.updateName(p);

				}
				else
				{
					player.sendMessage(ChatColor.RED
							+ "That player is not citizen yet!");
				}
				
				p = getPlayer(player.getWorld(), player_name);
				if (p!=null)
					plugin.updateName(p);
				else
				{
					//CLOSE
					plugin.playerClose(player_name);
				}
			} 
			else {
				player.sendMessage(ChatColor.RED
						+ "You don't have permission for this command!");
			}

			return true;
		}  
		else if (cmd.equalsIgnoreCase("removeMod"))
		{
		
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			if (player.isOp() || (plugin.playerConfig(player).getString("supermod", "").equalsIgnoreCase("true")))
			{
				String player_name = split[0];

				
				//LOAD UP THE PLAYER CONFIG?
				Player p = getPlayer(player.getWorld(), player_name);
				if (p==null)
				{
					plugin.loadPlayerData(player_name);
				}
				
				if (plugin.playerConfig(player_name).getString("mod", "").equals("true"))
				{
					plugin.playerConfig(player_name).setProperty( "mod",
							"");
					player.sendMessage(ChatColor.YELLOW
							+ "The player is no longer a moderator.");

					Bukkit.getServer().broadcastMessage(
							ChatColor.YELLOW + player_name
									+ " is the newest demoted moderator in Skycraft!");
					
					if (p!=null)
						plugin.updateName(p);

				}
				else
				{
					player.sendMessage(ChatColor.RED
							+ "That player is not a guide!");
				}
				
				p = getPlayer(player.getWorld(), player_name);
				if (p!=null)
					plugin.updateName(p);
				else
				{
					//CLOSE
					plugin.playerClose(player_name);
				}
			} 
			else {
				player.sendMessage(ChatColor.RED
						+ "You don't have permission for this command!");
			}

			return true;
		}  
		else if (cmd.equalsIgnoreCase("addMod")) {
		
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			if (player.isOp() || (plugin.playerConfig(player).getString("supermod", "").equalsIgnoreCase("true"))) {
				String player_name = split[0];

				
				//LOAD UP THE PLAYER CONFIG?
				Player p = getPlayer(player.getWorld(), player_name);
				if (p==null)
				{
					plugin.loadPlayerData(player_name);
				}
				
				if (!plugin.playerConfig(player_name).getString("nomad", "").equals("true"))
				{
					plugin.playerConfig(player_name).setProperty( "mod",
							"true");
					player.sendMessage(ChatColor.YELLOW
							+ "The player is now a moderator.");

					Bukkit.getServer().broadcastMessage(
							ChatColor.YELLOW + player_name
									+ " is the newest moderator in Skycraft!");

					
					p.sendMessage(ChatColor.YELLOW+"You are now a moderator. The following commands have been unlocked: /mban <name> /munban <name> /mkick <name> <reason> /mtp <name> /admin and /mspawn.");
					
					if (p!=null)
						plugin.updateName(p);

				}
				else
				{
					player.sendMessage(ChatColor.RED
							+ "That player is not citizen yet!");
				}
				
				p = getPlayer(player.getWorld(), player_name);
				if (p!=null)
					plugin.updateName(p);
				else
				{
					//CLOSE
					plugin.playerClose(player_name);
				}
			} 
			else {
				player.sendMessage(ChatColor.RED
						+ "You don't have permission for this command!");
			}

			return true;
		}  
		else if (cmd.equalsIgnoreCase("addLWC")) 
		{
			if (!(sender instanceof Player)) 
			{
				return false;
			}
			Player player = (Player) sender;
			if (player.isOp()) 
			{
				
				
				plugin.addLWC(player);

				
			} 
			else
			{
				player.sendMessage(ChatColor.RED
						+ "You don't have permission for this command!");
			}

			return true;
		} 
		else if (cmd.equalsIgnoreCase("drug")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			if (player.isOp())
			{
				Player p = getPlayer(player.getWorld(), split[0]);
					
				plugin.effectPlayer(p, "confusion", 1, 60);
				Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+player.getName()+" has drugged "+p.getName()+"!");
			}
		}
        else if (cmd.equalsIgnoreCase("clone")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			if (player.isOp())
			{
                    
			}
		}
		else if (cmd.equalsIgnoreCase("pot")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			if (player.isOp())
			{
				int item_id = Integer.valueOf(split[0]);
				
				ItemStack stack = new ItemStack(373, 10, (byte)item_id);
            	player.getInventory().addItem(stack);
				
			}
		}
		else if (cmd.equalsIgnoreCase("mirror")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			if (player.isOp())
			{
				Player p = getPlayer(player.getWorld(), split[0]);
					
				plugin.mirror(p, 0);
			}
		}
		else if (cmd.equalsIgnoreCase("potion")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			if (player.isOp())
			{
				int a = -1;
				if (split[1] != null)
					a = Integer.valueOf(split[1]);
				
				int t = -1;
				if (split[2] != null)
					t = Integer.valueOf(split[2]);
				
				Player p = player;
				if (split.length>2)
					p=getPlayer(p.getWorld(), split[3]);
					
				plugin.effectPlayer(p, split[0], a, t);
			}
		}
	else if (cmd.equalsIgnoreCase("nomad")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			if (plugin.playerConfig(player).getString( "guide", "")
					.equalsIgnoreCase("true")
					|| player.isOp()) {
				String player_name = split[0];
				
				//LOAD UP THE PLAYER CONFIG?
				Player p = getPlayer(player.getWorld(), player_name);
				if (p==null)
				{
					plugin.loadPlayerData(player_name);
				}
					
				if (plugin.playerConfig(player_name).getString("nomad", "")
						.equals("true")) {
					plugin.playerConfig(player_name).setProperty( "nomad",
							"false");
					player.sendMessage(ChatColor.YELLOW
							+ "The player is no longer a nomad.");

					//plugin
					Bukkit.getServer().broadcastMessage(
							ChatColor.YELLOW + "Welcome " + player_name
									+ " as the newest citizen of Skycraft!");
					
					p = getPlayer(player.getWorld(), player_name);
					if (p!=null)
						plugin.updateName(p);
					else
					{
						//CLOSE
						plugin.playerClose(player_name);
					}

				}
				else if (plugin.playerConfig(player_name).getString("nomad", "")
				.equals("false"))
				{
					player.sendMessage(ChatColor.RED
							+ "That player is already a citizen!");
				}
				else
				{
					player.sendMessage(ChatColor.RED
							+ "That player has not joined the game yet.");
				}
			} else {
				player.sendMessage(ChatColor.RED
						+ "You don't have permission for this command!");
			}

			return true;
		} else if (cmd.equalsIgnoreCase("setFlag")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			if (player.isOp()) {
				String player_name = split[1];

				plugin.playerConfig(player_name).setProperty(split[0],
						split[2]);
				player.sendMessage(ChatColor.YELLOW + "Flag saved!");

			} else {
				player.sendMessage(ChatColor.RED
						+ "You don't have permission for this command!");
			}

			return true;
		} else if (cmd.equalsIgnoreCase("shop")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;

			if (player.isOp())
			{
				String cmdd = split[0];
				String sell = split[1];
				String sellamount = split[2];
				String buy = split[3];
				String buyamount = split[4];
				
				if (cmdd.equalsIgnoreCase("new"))
				{
					
					String task_id = split[1];
					if (!task_id.equalsIgnoreCase("")) 
					{
						player.sendMessage(ChatColor.GREEN + "Click the desired sign now.");
						plugin.playerConfig(player).setProperty("ShopDisplayCoords","YES");
						plugin.playerConfig(player).setProperty("Selling",sell);
						plugin.playerConfig(player).setProperty("SAmount",sellamount);
						plugin.playerConfig(player).setProperty("Buying",buy);
						plugin.playerConfig(player).setProperty("BAmount",buyamount);
						
					}
				
				}
			}
		}
		else if (cmd.equalsIgnoreCase("helmet")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;

			if (player.isOp())
			{
				player.getInventory().getHelmet().setType(Material.IRON_HELMET);
				player.getInventory().getHelmet().setTypeId(Integer.valueOf(split[0]));
				player.updateInventory();
			}
		}
		else if (cmd.equalsIgnoreCase("tell")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;


			Player p = getPlayer(player.getWorld(), split[0]);
			if (p!=null)
			{
				String msg = "";
				for (int i =1; i < split.length; i++)
				{
					String s = split[i];
					if (!s.equalsIgnoreCase(""))
					{
						msg+=(" "+s);
					}
				}
						
				p.sendMessage(ChatColor.WHITE+player.getDisplayName()+ChatColor.DARK_RED+"whispers:" + msg);
				player.sendMessage(ChatColor.WHITE+player.getDisplayName()+ChatColor.DARK_RED+":" + msg);
			}
			else
			{
				player.sendMessage(ChatColor.RED+"Cannot find that player!");
			}
			
			
		}
		else if (cmd.equalsIgnoreCase("sneak")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			
			String sneak = plugin.playerConfig(player).getString("sneak", "");
			if (sneak.equalsIgnoreCase("true"))
			{
				
				String sneak2 = plugin.playerConfig(player).getString("is_sneaking", "false");
	    		
				if (sneak2.equalsIgnoreCase("false"))
				{
					player.setSneaking(true);
					player.sendMessage(ChatColor.GOLD+"You are now sneaking");
					plugin.playerConfig(player).setProperty("is_sneaking", "true");
				}
				else if (sneak2.equalsIgnoreCase("true"))
				{
					player.setSneaking(false);
					player.sendMessage(ChatColor.GOLD+"You are no longer sneaking");
					plugin.playerConfig(player).setProperty("is_sneaking", "false");
				}
			}
			else
			{
				player.sendMessage(ChatColor.RED+"You require the sneak perk to do this. Buy it in the spawn tower for only 30 portals!");
			}
		}
		else if (cmd.equalsIgnoreCase("teleport"))
		{
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			
			long sneak = Long.valueOf(plugin.playerConfig(player).getString("lastTeleportTiming", "0"));
    		long time = System.currentTimeMillis();
    		
    		long since = time-sneak;
    		if (since>(1800000))
    		{
    			//long sneak = Long.valueOf(plugin.getConfig().getString(player.getName()+".lastTeleportTime", "0"));
    			//plugin.playerConfig(player).setProperty("lastTeleportTiming", String.valueOf(System.currentTimeMillis()));

    			player.sendMessage(ChatColor.YELLOW+"Teleporting... Don't move for 10 seconds");
    			plugin.playerConfig(player).setProperty("teleporting", "true");
    			
    			HomeTeleport mana_bar = new HomeTeleport(plugin, player);
    	    	int timerID = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, mana_bar, 20*10);
    	    	
    	    	plugin.playerConfig(player).setProperty("teleportSchedule", timerID);
    		}
    		else
    		{
    			long minutes = (1800000-(since)) /1000/60;
    			
    			player.sendMessage(ChatColor.RED+"You cannot teleport for another "+String.valueOf(minutes)+" minutes.");
    		}
		}
		else if (cmd.equalsIgnoreCase("citadel"))
		{
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			
			if (plugin.playerConfig(player).getString("citadelSpawn", "").equalsIgnoreCase("true"))
			{
				
				
				long sneak = Long.valueOf(plugin.playerConfig(player).getString("lastCitadelTiming", "0"));
	    		long time = System.currentTimeMillis();
	    		
	    		long since = time-sneak;
	    		if (since>(1800000))
	    		{
	    			//long sneak = Long.valueOf(plugin.getConfig().getString(player.getName()+".lastTeleportTime", "0"));
	    			//plugin.playerConfig(player).setProperty("lastTeleportTiming", String.valueOf(System.currentTimeMillis()));
	
	    			player.sendMessage(ChatColor.YELLOW+"Teleporting... Don't move for 10 seconds");
	    			plugin.playerConfig(player).setProperty("citadel", "true");
	    			
	    			CitadelTeleport mana_bar = new CitadelTeleport(plugin, player);
	    	    	int timerID = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, mana_bar, 20*10);
	    	    	
	    	    	plugin.playerConfig(player).setProperty("citadelSchedule", timerID);
	    		}
	    		else
	    		{
	    			long minutes = (1800000-(since)) /1000/60;
	    			
	    			player.sendMessage(ChatColor.RED+"You cannot teleport for another "+String.valueOf(minutes)+" minutes.");
	    		}
    		
			}
			else
			{
				player.sendMessage(ChatColor.RED+"You don't have the citadel teleport perk! Buy it at the citadel tower.");
			}
		}
		else if (cmd.equalsIgnoreCase("tutorial"))
		{
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			if (plugin.playerConfig(player).getString("guide", "").equalsIgnoreCase("true"))
			{
				long sneak = Long.valueOf(plugin.playerConfig(player).getString("lastGuideTeleportTime", "0"));
	    		long time = System.currentTimeMillis();
	    		
	    		long since = time-sneak;
	    		if (since>(600000))
	    		{
	    			//long sneak = Long.valueOf(plugin.getConfig().getString(player.getName()+".lastTeleportTime", "0"));
	    			plugin.playerConfig(player).setProperty("lastGuideTeleportTime", String.valueOf(System.currentTimeMillis()));
	
	    			player.sendMessage(ChatColor.YELLOW+"Teleporting...");
	    			player.teleport(new Location(player.getWorld(), 21, 66, 111));
	    		}
	    		else
	    		{
	    			long minutes = (600000-(since)) /1000/60;
	    			
	    			player.sendMessage(ChatColor.RED+"You cannot guide teleport for another "+String.valueOf(minutes)+" minutes.");
	    		}
	    		
			}
			else
    		{
    			player.sendMessage(ChatColor.RED+"You are not a guide");
    		}
		}
		else if (cmd.equalsIgnoreCase("pet"))
		{
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			
			if (split[0].equalsIgnoreCase("list"))
			{
				player.sendMessage(ChatColor.YELLOW+"Current pets:");
				
				List<String> pets = plugin.playerConfig(player).getKeys("Pets");
				if (pets != null)
				{
					for (int i=0; i < pets.size();i++)
					{
						//Pet type = 
						String pet = pets.get(i);
						int amount = plugin.playerConfig(player).getInt("Pets."+pet, 0);
						
						player.sendMessage(ChatColor.YELLOW+pet+": "+String.valueOf(amount));
					}
				}
			}
			else if (split[0].equalsIgnoreCase("resetHouse"))
			{
				if (!(sender instanceof Player)) {
					return false;
				}
				Player player2 = (Player) sender;
				if (player2.isOp())
				{
					plugin.getSettle().removeProperty(split[0]);
				}
				else
					return false;
			}
			else if (split[0].equalsIgnoreCase("choose"))
			{
				String pet_name = split[1];
				
				List<String> pets = plugin.playerConfig(player).getKeys("Pets");
				if (pets != null)
				{
						
					if (pet_name.equalsIgnoreCase("Zombie"))
						pet_name = "Zombie";
					else if (pet_name.equalsIgnoreCase("Pig Zombie"))
						pet_name = "Pig Zombie";
					else if (pet_name.equalsIgnoreCase("Cave Spider"))
						pet_name = "Cave Spider";
					else if (pet_name.equalsIgnoreCase("Creeper"))
						pet_name = "Creeper";
					else if (pet_name.equalsIgnoreCase("Silverfish"))
						pet_name = "Silverfish";
					else if (pet_name.equalsIgnoreCase("Slime"))
						pet_name = "Slime";
					else if (pet_name.equalsIgnoreCase("Spider"))
						pet_name = "Spider";
					else if (pet_name.equalsIgnoreCase("Enderman"))
						pet_name = "Enderman";
					else
					{
						player.sendMessage(ChatColor.RED+"You don't have any "+pet_name+"s!");
						return false;
					}
					
						int amount = plugin.playerConfig(player).getInt("Pets."+pet_name, 0);
						if (amount > 0)
						{
							plugin.playerConfig(player).setProperty("Pets."+pet_name, amount-1);
							
							//Spawn new pet.
							CreatureType creature = CreatureType.CHICKEN;
							
							if (pet_name.equalsIgnoreCase("Zombie"))
								creature = CreatureType.ZOMBIE;
							else if (pet_name.equalsIgnoreCase("Pig Zombie"))
								creature = CreatureType.PIG_ZOMBIE;
							else if (pet_name.equalsIgnoreCase("Cave Spider"))
								creature = CreatureType.CAVE_SPIDER;
							else if (pet_name.equalsIgnoreCase("Creeper"))
								creature = CreatureType.CREEPER;
							else if (pet_name.equalsIgnoreCase("Silverfish"))
								creature = CreatureType.SILVERFISH;
							else if (pet_name.equalsIgnoreCase("Slime"))
								creature = CreatureType.SLIME;
							else if (pet_name.equalsIgnoreCase("Spider"))
								creature = CreatureType.SPIDER;
							else if (pet_name.equalsIgnoreCase("Enderman"))
								creature = CreatureType.ENDERMAN;
							
							Monster zombie = (Monster) player.getWorld().spawnCreature(player.getLocation().add(0, 1, 0), creature);
							LGAIZombie mana_bar = new LGAIZombie(player, plugin, zombie);

							int timerID = player.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, mana_bar, 0, 5);
							
							plugin.playerConfig(player).setProperty("Pet", timerID);
							plugin.playerConfig(player).setProperty("PetID", zombie.getEntityId());
							plugin.playerConfig(player).setProperty("PetState", 1);
							
							Bukkit.getServer().broadcastMessage("<"+player.getDisplayName()+"> "+pet_name+", I choose you!");
							
							int item_id = 332;
			        		ItemStack newitems = new ItemStack(item_id, 1);
			        		
				        	player.getInventory().addItem(newitems); //FREE POKEBALL
						}
						else
						{
							player.sendMessage(ChatColor.RED+"You don't have any "+pet_name+"s!");
						}
				}
				else
				{
					player.sendMessage(ChatColor.RED+"You don't have any pets!");
				}
			}

		}
		else if (cmd.equalsIgnoreCase("event"))
		{
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
		
				if (!plugin.getCon().getString("event", "").equalsIgnoreCase(""))
				{
					//Event!
					
					String center_loc = plugin.getCon().getString("event", "0 0 0");
					
					String[] crds = center_loc.split(" ");
    				Location settlement_loc = new Location(player.getWorld(), Integer.valueOf(crds[0]), Integer.valueOf(crds[1]), Integer.valueOf(crds[2]));
	    			
					player.teleport(settlement_loc);
					player.sendMessage(ChatColor.YELLOW+"You have been teleported to the event.");
				}
				else
				{
					player.sendMessage(ChatColor.RED+"No events are currently running.");
				}
		}
		else if (cmd.equalsIgnoreCase("ekit"))
		{
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
		
				if (!plugin.getCon().getString("event", "").equalsIgnoreCase(""))
				{
					//Event!
				
					
					
					long current_time = System.currentTimeMillis();
			    	long previous_time = Long.valueOf(plugin.playerConfig(player).getString("lastEvenetkit", String.valueOf(0)));
			    		
			    	long time_since = current_time-previous_time;
			    	
			    	if (time_since > 1000*30)
			    	{
			    		plugin.playerConfig(player).setProperty("lastEvenetkit", String.valueOf(current_time));
		    			
						
						player.getInventory().clear();
	
						player.sendMessage(ChatColor.YELLOW+"Your inventory has been cleared and replaced with an event kit.");
						player.sendMessage(ChatColor.GOLD+"You have gained 1 BOW, 32 ARROW, 1 STICK, 20 LOG, 1 WOODEN SWORD, 1 TORCH");
						
						ItemStack bow = new ItemStack(Material.BOW, 1);
						player.getInventory().addItem(bow);
						
						bow = new ItemStack(Material.ARROW, 32);
						player.getInventory().addItem(bow);
						
						bow = new ItemStack(Material.STICK, 1);
						player.getInventory().addItem(bow);
	
						bow = new ItemStack(Material.LOG, 20);
						player.getInventory().addItem(bow);
						
						bow = new ItemStack(Material.WOOD_SWORD, 1);
						player.getInventory().addItem(bow);
						
						bow = new ItemStack(Material.TORCH, 1);
						player.getInventory().addItem(bow);
						
						player.updateInventory();
					
			    	}
			    	else
			    	{
			    		player.sendMessage(ChatColor.RED+"You can only get an event kit every 30 seconds.");
			    	}
					
					
					
				}
				else
				{
					player.sendMessage(ChatColor.RED+"No events are currently running.");
				}
		}
		else if (cmd.equalsIgnoreCase("setEvent"))
		{
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			
			
			if (player.isOp())
			{
				if (!plugin.getCon().getString("event", "").equalsIgnoreCase(""))
				{
					//Event!
					
					//STOP THE EVENT
					plugin.getCon().setProperty("event", "");
					player.getServer().broadcastMessage(ChatColor.YELLOW+"The current event has ended. You will now lose experience on death.");
				}
				else
				{
					//START A NEW EVENT
					player.getServer().broadcastMessage(ChatColor.YELLOW+"A new event has started! Type /event to be instantly teleported to the location and /ekit to replace your inventory with an event toolkit. You do not lose experience on death during events.");
				
					String coords = String.format("%d %d %d", (int)player.getLocation().getX(), (int)player.getLocation().getY(), (int)player.getLocation().getZ());
					plugin.getCon().setProperty("event", coords);
					
					
				}
			}
		}
		else if (cmd.equalsIgnoreCase("spec")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;
			
			
			if (player.isOp())
			{
				if (player.getName().equalsIgnoreCase("shadovvMoon"))
				{
					String classn = split[0];
					if (classn.equalsIgnoreCase("paladin")||classn.equalsIgnoreCase("Mage"))
					{
						plugin.playerConfig(player).setProperty("current_spec", split[0]);
						Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+player.getName()+" has specialised into a " + split[0]);
						
						if (classn.equalsIgnoreCase("paladin"))
						{
							player.sendMessage(ChatColor.YELLOW+"You have unlocked pegasus boots! Jump whilst sprinting to go faster.");
							player.sendMessage(ChatColor.YELLOW+"You can no longer use a wand.");
							player.sendMessage(ChatColor.YELLOW+"You can no longer use a bow.");
						}
						else if (classn.equalsIgnoreCase("mage"))
						{
							player.sendMessage(ChatColor.YELLOW+"You have learnt wraith of the elder sage! Cast this spell using your hands and a gold ingot as the ingredient.");
							player.sendMessage(ChatColor.YELLOW+"You can no longer use a bow.");
							player.sendMessage(ChatColor.YELLOW+"You can no longer use melee.");
						}
						
						plugin.updateName(player);
						
					}
					else
					{
						player.sendMessage(ChatColor.RED+"That class doesn't exist.");
					}
				}
				else
				{
					plugin.playerConfig(player).setProperty("current_spec", "Idiot");
					Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+player.getName()+" has specialised into a " + "n idiot!");

					String classn = "idiot";
					
					if (classn.equalsIgnoreCase("idiot"))
					{
						player.sendMessage(ChatColor.YELLOW+"You can no longer use melee.");
						player.sendMessage(ChatColor.YELLOW+"You can no longer use a wand.");
						player.sendMessage(ChatColor.YELLOW+"You can no longer use a bow.");
					}
					
					plugin.updateName(player);
					return true;
				}
			}
			else
			{
				player.sendMessage(ChatColor.RED+"You don't have permission for this command.");
			}
		}
		
	
			else if (cmd.equalsIgnoreCase("task")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player player = (Player) sender;

			if (player.isOp()) {
				if (split[0].equalsIgnoreCase("new")) {
					String task_id = split[1];
					if (!task_id.equalsIgnoreCase("")) {
						player.sendMessage(ChatColor.GREEN
								+ "Click the desired sign now.");
						plugin.playerConfig(player).setProperty("DisplayCoords",
								"YES");
						plugin.playerConfig(player).setProperty("Task_name",
								task_id);
					}
				} else if (split[0].equalsIgnoreCase("reward")) {
					String task_id = split[1];
					if (!task_id.equalsIgnoreCase("")) {

						List<String> tasks = plugin.getTasks()
								.getKeys("Tasks");
						int g;
						boolean is_task = false;

						String coords2 = "";
						if (tasks != null)
						{
							
						
						for (g = 0; g < tasks.size(); g++) {
							coords2 = tasks.get(g);
							String task_od = plugin.getTasks().getString(
									"Tasks." + coords2 + ".set", "");

							if (task_od.equalsIgnoreCase(task_id)) {
								is_task = true;
								break;
							}
						}

						}
						
						if (is_task) {
							String equipment = split[2];
							String quantity = split[3];

							if (equipment.equalsIgnoreCase("")) {
								player.sendMessage(ChatColor.RED
										+ "You haven't entered an reward item number.");
								return false;
							}

							if (quantity.equalsIgnoreCase("")) {
								player.sendMessage(ChatColor.RED
										+ "You haven't entered a quantity number.");
								return false;
							}

							// All good :)
							String item_id;
							if (equipment.equalsIgnoreCase("LWC"))
				        	{
								item_id = "E"+equipment;
				        	}
							else
							 item_id = String.format("E%d",
									Integer.valueOf(equipment));
							Integer qty = Integer.valueOf(quantity);

							plugin.getTasks().setProperty(
									"Tasks." + coords2 + ".Rewards." + item_id,
									qty);
							player.sendMessage(ChatColor.YELLOW
									+ "Reward added to task.");
						} else {
							player.sendMessage(ChatColor.RED
									+ "That task doesn't exist.");
						}

					}
				} else if (split[0].equalsIgnoreCase("delete")) {
					String task_id = split[1];
					if (!task_id.equalsIgnoreCase("")) {

						List<String> tasks = plugin.getTasks()
								.getKeys("Tasks");
						int g;
						boolean is_task = false;

						String coords2 = "";
						if (tasks != null)
						{
							
						for (g = 0; g < tasks.size(); g++) {
							coords2 = tasks.get(g);
							String task_od = plugin.getTasks().getString(
									"Tasks." + coords2 + ".set", "");

							if (task_od.equalsIgnoreCase(task_id)) {
								is_task = true;
								break;
							}
						}}

						if (is_task) {
							String task_od = plugin.getTasks().getString(
									"Tasks." + coords2 + ".set", "");
							plugin.getTasks().removeProperty(
									"Tasks." + coords2);
							player.sendMessage(ChatColor.YELLOW + "Task "
									+ task_od
									+ " has been deleted successfully.");
						} else {
							player.sendMessage(ChatColor.RED
									+ "That task doesn't exist.");
						}

					}
				} else if (split[0].equalsIgnoreCase("equipment")) {
					String task_id = split[1];
					if (!task_id.equalsIgnoreCase("")) {

						List<String> tasks = plugin.getTasks()
								.getKeys("Tasks");
						int g;
						boolean is_task = false;

						
						String coords2 = "";
						
						if (tasks != null)
						{
							
						for (g = 0; g < tasks.size(); g++) {
							coords2 = tasks.get(g);
							String task_od = plugin.getTasks().getString(
									"Tasks." + coords2 + ".set", "");

							if (task_od.equalsIgnoreCase(task_id)) {
								is_task = true;
								break;
							}
						}
						}

						if (is_task) {
							String equipment = split[2];
							String quantity = split[3];

							if (equipment.equalsIgnoreCase("")) {
								player.sendMessage(ChatColor.RED
										+ "You haven't entered an equipment item number.");
								return false;
							}

							if (quantity.equalsIgnoreCase("")) {
								player.sendMessage(ChatColor.RED
										+ "You haven't entered a quantity number.");
								return false;
							}

							// All good :)
							String item_id;
							if (equipment.equalsIgnoreCase("LWC"))
				        	{
								item_id = "E"+equipment;
				        	}
							else
							 item_id = String.format("E%d",
									Integer.valueOf(equipment));
							Integer qty = Integer.valueOf(quantity);

							plugin.getTasks().setProperty(
									"Tasks." + coords2 + ".Equipment."
											+ item_id, qty);

							player.sendMessage(ChatColor.YELLOW
									+ "Equipment added to task.");
						} else {
							player.sendMessage(ChatColor.RED
									+ "That task doesn't exist.");
						}

					}
				} else if (split[0].equalsIgnoreCase("goal")) {
					String task_id = split[1];
					if (!task_id.equalsIgnoreCase("")) {

						List<String> tasks = plugin.getTasks()
								.getKeys("Tasks");
						int g;
						boolean is_task = false;

						String coords2 = "";
						
						if (tasks != null)
						{
							
								
						for (g = 0; g < tasks.size(); g++) {
							coords2 = tasks.get(g);
							String task_od = plugin.getTasks().getString(
									"Tasks." + coords2 + ".set", "");

							if (task_od.equalsIgnoreCase(task_id)) {
								is_task = true;
								break;
							}
						}
						}
						if (is_task) {
							String equipment = split[2];
							equipment = equipment.replaceAll("_", " ");
							if (equipment.equalsIgnoreCase("")) {
								player.sendMessage(ChatColor.RED
										+ "You haven't entered a goal string.");
								return false;
							}

							plugin.getTasks().setProperty(
									"Tasks." + coords2 + ".goal", equipment);

							player.sendMessage(ChatColor.YELLOW
									+ "Goal saved to task.");
						} else {
							player.sendMessage(ChatColor.RED
									+ "That task doesn't exist.");
						}

					}
				} else if (split[0].equalsIgnoreCase("aim")) {
					String task_id = split[1];
					if (!task_id.equalsIgnoreCase("")) {

						List<String> tasks = plugin.getTasks()
								.getKeys("Tasks");
						int g;
						boolean is_task = false;

						String coords2 = "";
						
						if (tasks != null)
						{
							
						for (g = 0; g < tasks.size(); g++) {
							coords2 = tasks.get(g);
							String task_od = plugin.getTasks().getString(
									"Tasks." + coords2 + ".set", "");

							if (task_od.equalsIgnoreCase(task_id)) {
								is_task = true;
								break;
							}
						}
						}

						if (is_task) {
							String type = split[2];
							if (!(type.equalsIgnoreCase("kill") || type
									.equalsIgnoreCase("collect"))) {
								player.sendMessage(ChatColor.RED
										+ "You haven't entered a type string [kill|collect].");
								return false;
							}

							String quantity = split[4];
							if (quantity.equalsIgnoreCase("")) {
								player.sendMessage(ChatColor.RED
										+ "You haven't entered a quantity number.");
								return false;
							}

							String num = "";
							if (type.equalsIgnoreCase("collect")) {
								String item_id = split[3];
								int id = Integer.valueOf(item_id);

								plugin.getTasks().setProperty(
										"Tasks." + coords2
												+ ".Aims.Collect.Type", id);
								plugin.getTasks().setProperty(
										"Tasks." + coords2
												+ ".Aims.Collect.Number",
										quantity);
								player.sendMessage(ChatColor.YELLOW
										+ "Aim added to task.");

							} else if (type.equalsIgnoreCase("kill")) {
								String mobtype = split[3];
								if (mobtype.equalsIgnoreCase("Player")
										|| mobtype.equalsIgnoreCase("Skeleton")
										|| mobtype.equalsIgnoreCase("Squid")
										|| mobtype.equalsIgnoreCase("Enderman")
										|| mobtype.equalsIgnoreCase("Zombie")
										|| mobtype.equalsIgnoreCase("Sheep")
										|| mobtype.equalsIgnoreCase("Pig")
										|| mobtype.equalsIgnoreCase("Spider")
										|| mobtype.equalsIgnoreCase("Creeper")
										|| mobtype.equalsIgnoreCase("Fish")
										|| mobtype
												.equalsIgnoreCase("PigZombie")
										|| mobtype.equalsIgnoreCase("Ghast")) {
									num = mobtype;
									plugin.getTasks().setProperty(
											"Tasks." + coords2
													+ ".Aims.Kill.Type",
											mobtype);
									plugin.getTasks().setProperty(
											"Tasks." + coords2
													+ ".Aims.Kill.Number",
											quantity);
									player.sendMessage(ChatColor.YELLOW
											+ "Aim added to task.");
								} else {
									player.sendMessage(ChatColor.RED
											+ "Invalid mob name (Can't use chickens or cows)");
									return true;
								}
							}
						} else {
							player.sendMessage(ChatColor.RED
									+ "That task doesn't exist.");
						}

					}
				} else if (split[0].equalsIgnoreCase("requires")) {
					String task_id = split[1];
					if (!task_id.equalsIgnoreCase("")) {

						List<String> tasks = plugin.getTasks()
								.getKeys("Tasks");
						int g;
						boolean is_task = false;

						String coords2 = "";
						
						if (tasks != null)
						{
							
						for (g = 0; g < tasks.size(); g++) {
							coords2 = tasks.get(g);
							String task_od = plugin.getTasks().getString(
									"Tasks." + coords2 + ".set", "");

							if (task_od.equalsIgnoreCase(task_id)) {
								is_task = true;
								break;
							}
						}
						}

						if (is_task) {
							String type = split[2];
							plugin.getTasks().setProperty(
									"Tasks." + coords2 + ".Requires." + type,
									"Yes");
							player.sendMessage(ChatColor.YELLOW
									+ "Task requirement saved.");
						} else {
							player.sendMessage(ChatColor.RED
									+ "That task doesn't exist.");
						}

					}
				}
			}
		}

		return true;
	}
	



}

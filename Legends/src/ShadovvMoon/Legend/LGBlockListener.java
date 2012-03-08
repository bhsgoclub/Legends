package ShadovvMoon.Legend;





import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.server.BlockButton;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.*;
import org.bukkit.craftbukkit.entity.CraftPlayer;

import org.bukkit.material.Button;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.config.Configuration;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
public class LGBlockListener extends BlockListener
{
	private final Legends plugin;

    public LGBlockListener(final Legends plugin)
    {
        this.plugin = plugin;
    }
    
    @Override
    public void onSignChange(SignChangeEvent event)
    {
    	String main = event.getLine(1);
    	

    	String[] lines = event.getLines();
    	
    	int i = 0;
    	for (String line : lines)
    	{
    		line = line.replaceAll("&y", ""+ChatColor.YELLOW);
    		line = line.replaceAll("&b", ""+ChatColor.BLUE);
    		line = line.replaceAll("&g", ""+ChatColor.GREEN);
    		line = line.replaceAll("&r", ""+ChatColor.RED);
    		
    		event.setLine(i, line);
    		i+=1;
    	}
    	

		ArrayList<String> str = plugin.getColors();
		
		
		
    	if (str.contains(lines[0]))
    	{
    		
    		event.setLine(1, (plugin.colorFromString(lines[0]).toString())+lines[1]);
    		event.setLine(0, "");
    	}
    	
    	Player player = event.getPlayer();
    	
    	int size = 5;
    	
    	if ((main.equalsIgnoreCase("&y[Settlement]")||main.equalsIgnoreCase(ChatColor.YELLOW+"[Settlement]"))&&!player.isOp())
    	{
    		event.getBlock().setType(Material.AIR);
    		player.sendMessage(ChatColor.RED+"You can't do that!");
    	}
    	else if ((main.equalsIgnoreCase("&y[Telepad]")||main.equalsIgnoreCase(ChatColor.YELLOW+"[Telepad]"))&&!player.isOp())
    	{
    		event.getBlock().setType(Material.AIR);
    		player.sendMessage(ChatColor.RED+"You can't do that!");
    	}
    	else if ((main.equalsIgnoreCase("&y[TE]")||main.equalsIgnoreCase(ChatColor.YELLOW+"[TE]"))&&!player.isOp())
    	{
    		event.getBlock().setType(Material.AIR);
    		player.sendMessage(ChatColor.RED+"You can't do that!");
    	}
    	else if ((main.equalsIgnoreCase("&y[Village]")||main.equalsIgnoreCase(ChatColor.YELLOW+"[Village]"))&&!player.isOp())
    	{
    		event.getBlock().setType(Material.AIR);
    		player.sendMessage(ChatColor.RED+"You can't do that!");
    	}
    	else if ((main.equalsIgnoreCase("&y[Colony]")||main.equalsIgnoreCase(ChatColor.YELLOW+"[Colony]"))&&!player.isOp())
    	{
    		event.getBlock().setType(Material.AIR);
    		player.sendMessage(ChatColor.RED+"You can't do that!");
    	}
    	else if ((main.equalsIgnoreCase("&y[Town]")||main.equalsIgnoreCase(ChatColor.YELLOW+"[Town]"))&&!player.isOp())
    	{
    		event.getBlock().setType(Material.AIR);
    		player.sendMessage(ChatColor.RED+"You can't do that!");
    	}
    	else if ((main.equalsIgnoreCase("&r[Member]")||main.equalsIgnoreCase(ChatColor.RED+"[Member]")||main.equalsIgnoreCase(ChatColor.GREEN+"[Member]"))&&!player.isOp())
    	{
    		event.getBlock().setType(Material.AIR);
    		player.sendMessage(ChatColor.RED+"You can't do that!");
    	}
    	else if ((main.equalsIgnoreCase("&r[Leader]")||main.equalsIgnoreCase(ChatColor.RED+"[Leader]")||main.equalsIgnoreCase(ChatColor.GREEN+"[Leader]"))&&!player.isOp())
    	{
    		event.getBlock().setType(Material.AIR);
    		player.sendMessage(ChatColor.RED+"You can't do that!");
    	}
    	
    	if (main.equalsIgnoreCase("[Telepad]"))
    	{
    		List<String> telepads = plugin.getTelepad().getKeys("Telepads");
        	
	    		String channel = event.getLine(2);
	    		if (!channel.equalsIgnoreCase(""))
	    		{
	    				//Ooh, we has a new telepad O.o
	    			boolean can_create = true;
	    			if (telepads!=null)
	    			{
	    				if (telepads.contains(channel))
	    				{
	    					String p = plugin.getTelepad().getString("Telepads."+channel+".Player", "");
	    					if (!p.equalsIgnoreCase(player.getName()))
	    					{
	    						can_create = false;
	    					}
	    				}
	    			}
	    			int enterances = plugin.playerConfig(player).getInt("telepadEnterances", 0);
	    			
	    			if (enterances <= 0)
	    			{
	    				player.sendMessage(ChatColor.RED+"You cannot create another teleporter enterance! Buy more from the spawn tower.");
	    				return;
	    			}
	    			
	    			if (can_create)
	    			{
	    				Location loc = event.getBlock().getLocation();
	    				String coords = String.format("%d %d %d", (int)loc.getX(), (int)loc.getY(), (int)loc.getZ());
	    				
	    				plugin.getTelepad().setProperty("Telepads."+channel+".Player", event.getPlayer().getName());
	    				plugin.getTelepad().setProperty("Telepads."+channel+".Enterance", coords);
	    				
	    				player.sendMessage(ChatColor.YELLOW+"You have created a new telepad enterance! Create an exit by placing [Telepad Exit] on the second line of a sign, and the channel ID on the third line.");
	    				event.setLine(1, ChatColor.YELLOW+"[Telepad]");
	    				

	    				
	    				enterances--;
	    				plugin.playerConfig(player).setProperty("telepadEnterances", enterances);
	    			}
	    			else
	    			{
	    				player.sendMessage(ChatColor.RED+"You do not have permission to create a telepad with that channel ID.");
	    			}
	    		}
	    		else
	    		{
	    			player.sendMessage(ChatColor.RED+"You didn't enter a channel ID!");
	    		}
        	
    	}
    	else if (main.equalsIgnoreCase("[NPC]"))
    	{
    		List<String> NPCS = plugin.getTelepad().getKeys("NPCS");
        	

    			if (event.getPlayer().isOp())
    			{
    				Location loc = event.getBlock().getLocation();
    				
    				String ia = String.format("%d %d %d", (int)loc.getX(), (int)loc.getY(), (int)loc.getZ());
    	        	
    				//String ia = String.valueOf((int)loc.getX())+" "+String.valueOf((int)loc.getY())+" "+String.valueOf((int)loc.getZ());
    				
    				
    				
    				plugin.getTelepad().setProperty("NPCS."+ia+".Name", event.getLine(2));
    				plugin.getTelepad().setProperty("NPCS."+ia+".Type", event.getLine(3));
    				plugin.getTelepad().setProperty("NPCS."+ia+".Equipment", event.getLine(0));
    				
    				event.getPlayer().sendMessage(ChatColor.YELLOW+"NPC spawner created. "+ ia);
    				
    			}
    			else
    			{
    				event.setCancelled(true);
    				return;
    		
			}
    	}
    	else if (main.equalsIgnoreCase("[Telepad Exit]"))
    	{
    		List<String> telepads = plugin.getTelepad().getKeys("Telepads");
        	
    		String channel = event.getLine(2);
    		if (!channel.equalsIgnoreCase(""))
    		{
    				//Ooh, we has a new telepad O.o
    			
    			boolean can_create = true;
    			if (telepads!=null)
    			{
    				if (telepads.contains(channel))
    				{
    					String p = plugin.getTelepad().getString("Telepads."+channel+".Player", "");
    					if (!p.equalsIgnoreCase(player.getName()))
    					{
    						can_create = false;
    					}
    				}
    			}
    				
    			int enterances = plugin.playerConfig(player).getInt("telepadExits", 0);
    			if (enterances <= 0)
    			{
    				player.sendMessage(ChatColor.RED+"You cannot create another teleporter exit! Buy more from the spawn tower.");
    				return;
    			}
    			
    			if (can_create)
    			{
    				Location loc = event.getBlock().getLocation();
    				String coords = String.format("%d %d %d", (int)loc.getX(), (int)loc.getY(), (int)loc.getZ());
    				
    				plugin.getTelepad().setProperty("Telepads."+channel+".Player", event.getPlayer().getName());
    				plugin.getTelepad().setProperty("Telepads."+channel+".Exit", coords);
    				
    				player.sendMessage(ChatColor.YELLOW+"You have created a new telepad exit! Create an enterance by placing [Telepad] on the second line of a sign, and the channel ID on the third line.");
    				event.setLine(1, ChatColor.YELLOW+"[TE]");
    				
    				
    				enterances--;
    				plugin.playerConfig(player).setProperty("telepadExits", enterances);

    			}
    			else
    			{
    				player.sendMessage(ChatColor.RED+"You do not have permission to create a telepad with that channel ID.");
    			}
    		}
    		else
    		{
    			player.sendMessage(ChatColor.RED+"You didn't enter a channel ID!");
    		}
        	
    	}
    	else if (main.equalsIgnoreCase("[Village]")||main.equalsIgnoreCase("[Colony]")||main.equalsIgnoreCase("[Town]")||(main.equalsIgnoreCase(ChatColor.GREEN+"[Village]")&&player.isOp()))
    	{
    		
    		
    		
    		
    		
    		
    		String houseID = plugin.getSettle().getString(player.getName()+".houseID", "");
    		
    		java.util.List<String> sett = plugin.getSettle().getKeys("Settlements");
    		
    		if (!plugin.currentSettlement(event.getBlock().getLocation(), player).equalsIgnoreCase(""))
    		{
    			player.sendMessage(ChatColor.RED+"Your settlement is too close to another exiting one. Remove the existing one, then try again.");
    			return;
    		}
    		
    		if (event.getBlock().getRelative(0, -1, 0).getType()==Material.SAND||event.getBlock().getRelative(0, -1, 0).getType()==Material.GRAVEL)
    		{
    			player.sendMessage(ChatColor.RED+"Your settlement sign is on sand or gravel. Place it on a more stable block.");
    			return;
    		}
    		
    		if (houseID.equalsIgnoreCase(""))
    		{
    			String village_name = event.getLine(2);
    			
    			if (village_name.equalsIgnoreCase(""))
    			{
    				player.sendMessage(ChatColor.RED+"You need to enter a village name on the line under the type.");
	    			return;
    			}
    			
    			if (plugin.getSettle().getKeys(village_name)==null)
    			{
    				
	    			
    				

    	    		//CHECK FOR COSTS
    	    		if (main.equalsIgnoreCase("[Village]"))
    	    		{
    		    		if (plugin.totalMoney(player) >= Integer.valueOf(10000))
    		    		{
    		    			plugin.removeMoney(player, Integer.valueOf(10000));
    		    		}
    		    		else
    		    		{
    		    			player.sendMessage(ChatColor.RED+"You require an additional " + String.valueOf(10000-plugin.totalMoney(player)) +"c to setup a village");
    		    			event.setCancelled(true);
    		    			
    		    			return;
    		    		}
    	    		}
    	    		else if (main.equalsIgnoreCase("[Colony]"))
    	    		{
    		    		if (plugin.totalMoney(player) >= Integer.valueOf(3000))
    		    		{
    		    			plugin.removeMoney(player, Integer.valueOf(3000));
    		    		}
    		    		else
    		    		{
    		    			player.sendMessage(ChatColor.RED+"You require an additional " + String.valueOf(3000-plugin.totalMoney(player)) +"c to setup a colony");
    		    			event.setCancelled(true);
    		    			
    		    			return;
    		    		}
    	    		}
    	    		else if (main.equalsIgnoreCase("[Town]"))
    	    		{
    		    		if (plugin.totalMoney(player) >= Integer.valueOf(20000))
    		    		{
    		    			plugin.removeMoney(player, Integer.valueOf(20000));
    		    		}
    		    		else
    		    		{
    		    			player.sendMessage(ChatColor.RED+"You require an additional " + String.valueOf(20000-plugin.totalMoney(player)) +"c to setup a town");
    		    			event.setCancelled(true);
    		    			
    		    			return;
    		    		}
    	    		}
    	    		
    	    		
    				
    				
    				
					if (main.equalsIgnoreCase("[Settlement]"))
					{
						    		event.setLine(1, ChatColor.YELLOW+"[Settlement]");
									event.setLine(3, "1/3");
					}
					else if (main.equalsIgnoreCase("[Village]") || (main.equalsIgnoreCase(ChatColor.GREEN+"[Village]")))
					{
						size = 37;
						event.setLine(1, ChatColor.YELLOW+"[Village]");
						
						if (player.isOp())
							event.setLine(3, "1/2");
						else
							event.setLine(3, "1/11");
					}
					else if (main.equalsIgnoreCase("[Town]"))
					{
						size = 72;
						event.setLine(1, ChatColor.YELLOW+"[Town]");
						event.setLine(3, "1/21");
					}
					else if (main.equalsIgnoreCase("[Colony]"))
					{
						size = 22;
						event.setLine(1, ChatColor.YELLOW+"[Colony]");
						event.setLine(3, "1/6");
					}
		    		
		    		
		    		if (main.equalsIgnoreCase("[Settlement]"))
		    		{
		    			player.sendMessage(ChatColor.YELLOW+"You have created a new settlement (11x11).");
		    		}
		    		else if (main.equalsIgnoreCase("[Village]"))
		    		{
		    			player.sendMessage(ChatColor.YELLOW+"You have created a new village (75x75).");
		    		}
		    		else if (main.equalsIgnoreCase("[Town]"))
		    		{
		    			player.sendMessage(ChatColor.YELLOW+"You have created a new town (145x145).");
		    		}
		    		else if (main.equalsIgnoreCase("[Colony]"))
		    		{
		    			player.sendMessage(ChatColor.YELLOW+"You have created a new colony (45x45).");
		    		}
		    		
		    		//event.setLine(2, plugin.playerName(player));
		    		
		    		
		    		
		    		plugin.getSettle().setProperty(player.getName()+".houseID", village_name);
		    		
		    		Block settlement_block = event.getBlock();
		
		    		Block bound1= settlement_block.getRelative(size, 0, -size);
		    		Block bound2= settlement_block.getRelative(size, 0, size);
		    		Block bound3= settlement_block.getRelative(-size, 0, -size);
		    		Block bound4= settlement_block.getRelative(-size, 0, size);
		    		
		    		Location loc = event.getBlock().getLocation();
		    		String coords = String.format("%d %d %d", (int)loc.getX(), (int)loc.getY(), (int)loc.getZ());
		    		
		    		plugin.getSettle().setProperty("Settlements."+village_name+".Middle", coords);
		    		plugin.getSettle().setProperty("Settlements."+village_name+".Players."+player.getName(), "Owner");
		    		plugin.getSettle().setProperty("Settlements."+village_name+".Type", main);
		
		    		/*
		    		bound1.setType(Material.GLOWSTONE);
		    		bound2.setType(Material.GLOWSTONE);
		    		bound3.setType(Material.GLOWSTONE);
		    		bound4.setType(Material.GLOWSTONE);
		    		
		    		BlockVector pt = new BlockVector(bound3.getLocation().getX(), bound3.getLocation().getY()-30, bound3.getLocation().getZ());
		    		BlockVector pt2 = new BlockVector(bound2.getLocation().getX(), bound2.getLocation().getY()+30, bound2.getLocation().getZ());
		
		    		ProtectedCuboidRegion new_protection = new ProtectedCuboidRegion(plugin.playerName(player),pt,pt2);
		    		
		    		DefaultDomain domain = new DefaultDomain();
		    		domain.addPlayer(player.getName());
		    		
		    		new_protection.setOwners(domain);
		    		
		    		WorldGuardPlugin worldGuard = getWorldGuard();
		    		
		    		RegionManager regionManager = worldGuard.getRegionManager(player.getWorld());
		    		regionManager.addRegion(new_protection);
		    		*/
		    		
		    		if (main.equalsIgnoreCase("[Settlement]"))
		    		{
		    			player.sendMessage(ChatColor.YELLOW+"You need to add another two people to the settlement before it is protected. Type /town invite <name> to add people.");
		        		
		    		}
		    		else if (main.equalsIgnoreCase("[Village]"))
		    		{
		    			player.sendMessage(ChatColor.YELLOW+"You need to add another ten people to the village before it is protected. Type /town invite <name> to add people.");
		        		
		    		}
		    		else if (main.equalsIgnoreCase("[Town]"))
		    		{
		    			player.sendMessage(ChatColor.YELLOW+"You need to add another twenty people to the town before it is protected. Type /town invite <name> to add people.");
		        		
		    		}
		    		else if (main.equalsIgnoreCase("[Colony]"))
		    		{
		    			player.sendMessage(ChatColor.YELLOW+"You need to add another five people to the colony before it is protected. Type /town invite <name> to add people.");
		        		
		    		}
    			}
		    		else
		    		{
		    			player.sendMessage(ChatColor.RED+"A settlement with the name " + village_name + " already exists.");
		    			return;
		    		}
	    		}
    		else
    		{
    			player.sendMessage(ChatColor.RED+"You are already part of a settlement! Consult with the other members of your settlement about destroying the existing one.");
    		}
    		
    	}
    	else if (main.equalsIgnoreCase("[Member]"))
    	{
    		String p = event.getLine(2)+event.getLine(3);
    		if (!p.equalsIgnoreCase(""))
    		{
    			String houseID = plugin.getSettle().getString(p+".houseID", "");
    			if (!houseID.equalsIgnoreCase(""))
    			{
    				player.sendMessage(ChatColor.RED+"That player is already part of a settlement!");
    			}
    			else
    			{
    				String settlement = plugin.currentSettlement(event.getBlock().getLocation(), player);
    				String type = plugin.getSettle().getString("Settlements."+settlement+".Players."+player.getName(), "");
    				if (type.equalsIgnoreCase("Owner")||type.equalsIgnoreCase("Leader"))
    				{
    					event.setLine(1, ChatColor.RED+"[Member]");
        				player.sendMessage(ChatColor.YELLOW+"Settlement member added! To activate this sign, the new member must right click this sign.");
    				}
    				else
    				{
    					player.sendMessage(ChatColor.RED+"You don't have permission to add members to this settlement.");
    				}
    			}
    		}
    		else
    		{
    			player.sendMessage(ChatColor.RED+"You didn't enter a player name on the third line!");
    		}
    	}
    	else if (main.equalsIgnoreCase("[Leader]"))
    	{
    		String p = event.getLine(2)+event.getLine(3);
    		if (!p.equalsIgnoreCase(""))
    		{
    			String settlement = plugin.currentSettlement(event.getBlock().getLocation(), player);
    			
    			String houseID = plugin.getSettle().getString(p+".houseID", "");
    			if (!houseID.equalsIgnoreCase(settlement))
    			{
    				player.sendMessage(ChatColor.RED+"That player isn't part of this settlement!");
    			}
    			else
    			{
    				String type = plugin.getSettle().getString("Settlements."+settlement+".Players."+player.getName(), "");

    				if (type.equalsIgnoreCase("Owner")||type.equalsIgnoreCase("Leader"))
    				{
    					event.setLine(1, ChatColor.RED+"[Leader]");
        				player.sendMessage(ChatColor.YELLOW+"Settlement leader added! To activate this sign, the new leader must right click this sign.");
    				}
    				else
    				{
    					player.sendMessage(ChatColor.RED+"You don't have permission to add leaders to this settlement.");
    				}
    			}
    		}
    		else
    		{
    			player.sendMessage(ChatColor.RED+"You didn't enter a player name on the third line!");
    		}
    	}
    }
    
    @Override
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block block = event.getBlock();

        if ((block.getType() == Material.SAND) || (block.getType() == Material.GRAVEL)) {
            Block above = block.getFace(BlockFace.UP);
            if (above.getType() == Material.IRON_BLOCK) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onBlockCanBuild(BlockCanBuildEvent event) {
        Material mat = event.getMaterial();

        if (mat.equals(Material.CACTUS)) {
            event.setBuildable(true);
        }
    }
    
    @Override
    public void onBlockPlace(BlockPlaceEvent event)
    {
    	
    	boolean can_cast = false;
		WorldGuardPlugin worldGuard = getWorldGuard();
		RegionManager regionManager = worldGuard.getRegionManager(event.getPlayer().getWorld());
		
		Location location = event.getPlayer().getLocation();
		com.sk89q.worldedit.Vector v = new com.sk89q.worldedit.Vector(location.getX(), location.getY(), location.getZ());
		ApplicableRegionSet region2 =  worldGuard.getRegionManager(event.getPlayer().getWorld()).getApplicableRegions(v);
		
		Iterator<ProtectedRegion> r = region2.iterator();

		boolean is_regenerating = false;
		while (r.hasNext())
		{
			ProtectedRegion re = r.next();
			if (re.getId().contains("regenzone"))
			{
				is_regenerating = true;
				break;
			}
		}
    	
		if (is_regenerating)
		{
			event.getPlayer().sendMessage(ChatColor.RED+"You cannot build in a regenerating region.");
			event.setCancelled(true);
		}
    	
        Player player = event.getPlayer();

        Material Block = event.getBlock().getType();
        /*if (player!=null && (Block==Material.DIAMOND_ORE||Block==Material.IRON_ORE||Block==Material.GOLD_ORE||Block==Material.PORTAL))
        {
        	event.setCancelled(true);
        	player.sendMessage(ChatColor.RED+"You cannot place that type of block.");
        }*/
        
        
        String coords = String.format("%d %d %d", (int)event.getBlock().getLocation().getX(), (int)event.getBlock().getLocation().getY(), (int)event.getBlock().getLocation().getZ());
        plugin.getBlocks().setProperty(coords, String.valueOf(System.currentTimeMillis()));
		
//getBlocks()
        
        
        
        
        int ignore_nomad = 1;
    	
    	if (ignore_nomad == 1)
    	{
        if (plugin.playerConfig(player).getString("nomad", "").equalsIgnoreCase("true"))
        {
        	player.sendMessage(ChatColor.RED+"You cannot place blocks as a nomad. Post an application on the minecraftforum and we will give you citizenship.");
        	player.sendMessage(ChatColor.YELLOW+"Go to http://tinyurl.com/skycraft4.");
        	event.setCancelled(true);
        	return;
        }
    	}
        
    }
    
    public void spadeBlock(Block main_block, Player p)
    {
    	if (main_block.getType() != Material.DIRT && main_block.getType() != Material.GRASS)
    		return;
    	
    	((CraftPlayer)p).getHandle().itemInWorldManager.c(main_block.getX(), main_block.getY(), main_block.getZ());
    }
    
    public void superSpade(Block main_block, Player p)
    {
  
    	main_block.getRelative(-1, 0, 0).setData((byte) 90);
    	main_block.getRelative(1, 0, 0).setData((byte) 90);
    	main_block.getRelative(0, 0, 1).setData((byte) 90);
    	main_block.getRelative(0, 0, -1).setData((byte) 90);
    	main_block.getRelative(1, 0, 1).setData((byte) 90);
    	main_block.getRelative(1, 0, -1).setData((byte) 90);
    	main_block.getRelative(-1, 0, 1).setData((byte) 90);
    	main_block.getRelative(-1, 0, -1).setData((byte) 90);
    	
    	spadeBlock(main_block.getRelative(-1, 0, 0), p);
    	spadeBlock(main_block.getRelative(1, 0, 0), p);
    	spadeBlock(main_block.getRelative(0, 0, 1), p);
    	spadeBlock(main_block.getRelative(0, 0, -1), p);
    	
    	spadeBlock(main_block.getRelative(1, 0, 1), p);
    	spadeBlock(main_block.getRelative(1, 0, -1), p);
    	spadeBlock(main_block.getRelative(-1, 0, 1), p);
    	spadeBlock(main_block.getRelative(-1, 0, -1), p);
    }
    
    @Override
    public void onBlockRedstoneChange(BlockRedstoneEvent event)
    {
    	
    	
    	int current = event.getNewCurrent();
    	
    	if (current != 1)
    		return;
    	
    	if (event.getBlock().getType()!=Material.STONE_PLATE)
    		return;
    	
    	Location loc2 = event.getBlock().getLocation();
    	int radius = 4;
		
    	
    	
		//Check for cave.
		int playerX = (int)loc2.getX();
        int playerY = (int)loc2.getY();
        int playerZ = (int)loc2.getZ();
        List<Sign> signs = new ArrayList<Sign>();
        World world = event.getBlock().getWorld();
        for(int x = (int)playerX-radius;x<playerX+radius;x ++){
            for(int z = (int)playerZ-radius;z<playerZ+radius;z ++){
                for(int y = (int)playerY-radius;y<playerY+radius;y ++){
                    Block bs = world.getBlockAt(x,y,z);
                    if(!(bs.getType() == Material.WALL_SIGN))continue;
                    
                    //A sign!!! Is it a task sign
                    
                    
        	    	boolean player_found = false;
        	    	Player player = null;
        	    	
        	    	for ( Player p : Bukkit.getServer().getOnlinePlayers())
        	        {
        	    		if (p.getWorld() == event.getBlock().getLocation().getWorld())
        	    		{
        		    		if (p.getLocation().distance(event.getBlock().getLocation()) <= 1.5)
        		    		{
        		    			//We has found player
        		    			player_found = true;
        		    			player=p;
        		    			break;
        		    		}
        	    		}
        	        }
        	    	
                    
        	    	if (player_found)
        	    	{
        	    	
                    Block block = bs;
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
    	                    //ACTIVATE ANY NEARBY BUTTONS
    	                    
    	        		     
                            for(x = (int)playerX-radius;x<playerX+radius;x ++){
                                for(z = (int)playerZ-radius;z<playerZ+radius;z ++){
                                    for(y = (int)playerY-radius;y<playerY+radius;y ++){
                                        Block bs2 = world.getBlockAt(x,y,z);
                                        if(!(bs2.getType() == Material.STONE_BUTTON)) continue;
                                        
                   
                                        byte data = bs2.getData();
                                        bs2.setData((byte) (data | 0x8));
                                        
                                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new LGSwitchChanger(plugin, bs2), 20*5);
                            	    	
                                    }
                                }
                            }
                        
                        
                            
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
    		        				
    		                    	 
    		                        for(x = (int)playerX-radius;x<playerX+radius;x ++){
    		                            for(z = (int)playerZ-radius;z<playerZ+radius;z ++){
    		                                for(y = (int)playerY-radius;y<playerY+radius;y ++){
    		                                    Block bs2 = world.getBlockAt(x,y,z);
    		                                    if(!(bs2.getType() == Material.STONE_BUTTON)) continue;
    		                                    
    		               
    		                                    byte data = bs2.getData();
    		                                    bs2.setData((byte) (data & ~0x8));
    	
    		                                }
    		                            }
    		                        }
    		                        
    		        				
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
                    
                    
                    
                    
                    //ACTIVATE ANY NEARBY BUTTONS
                    
     
                    	 
                        for(x = (int)playerX-radius;x<playerX+radius;x ++){
                            for(z = (int)playerZ-radius;z<playerZ+radius;z ++){
                                for(y = (int)playerY-radius;y<playerY+radius;y ++){
                                    Block bs2 = world.getBlockAt(x,y,z);
                                    if(!(bs2.getType() == Material.STONE_BUTTON)) continue;
                                    
               
                                    byte data = bs2.getData();
                                    bs2.setData((byte) (data | 0x8));
                                    
                                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new LGSwitchChanger(plugin, bs2), 20*5);
                        	    	
                                }
                            }
                        }
                    
                    
                    
                }
        	    	}
            }
        }
        }
    	//Look for nearby signs
    	
    	
    	if (current == 1)
    	{
	    	Location loc = event.getBlock().getLocation();
	    	
	    	boolean player_found = false;
	    	Player player = null;
	    	
	    	for ( Player p : Bukkit.getServer().getOnlinePlayers())
	        {
	    		if (p.getWorld() == loc.getWorld())
	    		{
		    		if (p.getLocation().distance(loc) <= 1.5)
		    		{
		    			//We has found player
		    			player_found = true;
		    			player=p;
		    			break;
		    		}
	    		}
	        }
	    	
	    	if (player_found)
	    	{
	    		//Do something
	    		if (player.getLocation().distance(new Location(player.getWorld(), 407, 12, -346)) <= 1.5)
	    		{
	    			String archery_task = plugin.playerConfig(player).getString("archery_task", "");
	    			String melee_task = plugin.playerConfig(player).getString("melee_task", "");
	    			String magic_task = plugin.playerConfig(player).getString("magic_task", "");
			         
	    			
	    			if (archery_task.equalsIgnoreCase("finished"))
	    				if (magic_task.equalsIgnoreCase("finished"))
	    					if (melee_task.equalsIgnoreCase("finished"))
	    					{
	    						player.sendMessage(ChatColor.YELLOW+"You have completed basic training!");
	    						
	    						player.teleport(new Location(player.getWorld(), 399, 54, -350));
	    						player.getWorld().setSpawnLocation(399, 54, -350);
	    						
	    						player.getInventory().clear();

	    						player.sendMessage(ChatColor.YELLOW+"Be warned. Players are unable to attack you whilst inside the glowstone border (Look up at the sky), but can kill you outside of it.");
	    						player.sendMessage(ChatColor.GOLD+"You have gained 5 LOG, 1 WOODEN SWORD, 1 TORCH, 5 BREAD, 1 WOODEN PICKAXE, 1 WOODEN SPADE, 1 WOODEN AXE");
	    						
	    						ItemStack bow;
	    						
	    						bow = new ItemStack(Material.LOG, 5);
	    						player.getInventory().addItem(bow);
	    						
	    						bow = new ItemStack(Material.WOOD_SWORD, 1);
	    						player.getInventory().addItem(bow);
	    						
	    						bow = new ItemStack(Material.TORCH, 1);
	    						player.getInventory().addItem(bow);
	    						
	    						bow = new ItemStack(Material.BREAD, 5);
	    						player.getInventory().addItem(bow);
	    						
	    						bow = new ItemStack(Material.WOOD_PICKAXE, 1);
	    						player.getInventory().addItem(bow);
	    						
	    						bow = new ItemStack(Material.WOOD_SPADE, 1);
	    						player.getInventory().addItem(bow);
	    						
	    						bow = new ItemStack(Material.WOOD_AXE, 1);
	    						player.getInventory().addItem(bow);
	    						
	    						
	    						player.updateInventory();
	    						
	    					}
	    			
	    		    			else
	    		    				player.sendMessage(ChatColor.RED+"You haven't completed the melee task!");
	    	    			else
	    	    				player.sendMessage(ChatColor.RED+"You haven't completed the magic task!");
	    			else
	    				player.sendMessage(ChatColor.RED+"You haven't completed the archery task!");
	    		}
	    		else 
	    		{
	    			Block below = event.getBlock().getRelative(0,-2, 0);
	    			if (below.getType()==Material.SIGN_POST)
	    			{
	    				Sign the_sign = (Sign)below.getState();
	    				String special = the_sign.getLine(1);
	    				String channel = the_sign.getLine(2);
	    				
	    				if (special.equalsIgnoreCase(ChatColor.YELLOW+"[Telepad]"))
	    				{
	    					//Player just stood on a telepad!
	    					System.out.println(player.getName()+" just stood on a telepad "+channel);
	    					
	    					List<String> telepads = plugin.getTelepad().getKeys("Telepads");
	    		    			if (telepads!=null)
	    		    			{
	    		    				if (telepads.contains(channel))
	    		    				{
	    		    					String p = plugin.getTelepad().getString("Telepads."+channel+".Exit", "");
	    		    					if (p.equalsIgnoreCase(""))
	    		    					{
	    		    						player.sendMessage(ChatColor.RED+"This telepad ("+channel+") does not have an exit.");
	    		    					}
	    		    					else
	    		    					{
	    		    						String[] st = p.split(" ");
	    		    						Block sig = player.getWorld().getBlockAt(Integer.valueOf(st[0]), Integer.valueOf(st[1]), Integer.valueOf(st[2]));
	    		    						if (sig.getType()==Material.SIGN_POST)
	    		    		    			{
	    		    							Sign the_sign2 = (Sign)sig.getState();
	    		    		    				String special2 = the_sign2.getLine(1);
	    		    		    				String channel2 = the_sign2.getLine(2);
	    		    		    				
	    		    		    				if (special2.equals(ChatColor.YELLOW+"[TE]")&& channel2.equals(channel) )
	    		    		    				{
	    		    		    					player.teleport(sig.getLocation().add(0, 1, 0));
	    		    		    					player.sendMessage(ChatColor.YELLOW+"You have been teleported.");
	    		    		    				}
	    		    		    				else
		    		    						{
		    		    							player.sendMessage(ChatColor.RED+"This telepad does not have a physical exit.");
		    		    						}
	    		    		    			}
	    		    					}
	    		    				}
	    		    			}
	    					//
	    					
	    					
	    				}
	    			}

	    			
	    	
	    			
	    			
	    			
	    			
	    		}

	    		
	    	}
    	
        }
    }
    
    public void blockDestroyed(Block e, Player p, int gain)
	 {
		List<String> tasks = plugin.getTasks().getKeys("Tasks");
		int mob_name= e.getTypeId();
	     	int g; boolean is_task = false;
	     	for (g=0; g < tasks.size(); g++)
	     	{
	     		String coords2 = tasks.get(g);
	     		int block_type = plugin.getTasks().getInt("Tasks."+coords2+".Aims.Collect.Type", -1);
			    	 
	     		//p.sendMessage(String.format("Aim:%d Mined: %d (DEBUG)", block_type, mob_name));
	     			if (block_type == mob_name)
	     			{
	     				//We have killed the mob! Woo!
	     				String melee_task_overall = plugin.playerConfig(p).getString(plugin.getTasks().getString("Tasks."+coords2+".set"), "");
	     				 if (melee_task_overall.equalsIgnoreCase("finished"))
		   		         {
		   		        	 //Do nothing
		   		         }
		   		         else
		   		         {
		   		        	String melee_task = plugin.playerConfig(p).getString(plugin.getTasks().getString("Tasks."+coords2+".set")+".Collect", "");
		   		        	if (melee_task.equalsIgnoreCase("finished")||melee_task.equalsIgnoreCase(""))
			   		         {
			   		        	 //Do nothing
			   		         }
			   		         else
			   		         {
			   		        	 int remaining_kills = Integer.valueOf(melee_task);
			   		        	 remaining_kills+=gain;
			   		        	 
			   		        	 if (remaining_kills <= 0)
			   		        	 {
			   		        		p.sendMessage(ChatColor.YELLOW+"You have finished the collection aspect of "+plugin.getTasks().getString("Tasks."+coords2+".set")+"!");
			   		        		
			   		        		 String creative_task = plugin.playerConfig(p).getString(plugin.getTasks().getString("Tasks."+coords2+".set")+".Kill", "");
					   		         if (creative_task.equalsIgnoreCase("finished")||Integer.valueOf(creative_task)<=0)
					   		         {
					   		        	plugin.playerConfig(p).setProperty(plugin.getTasks().getString("Tasks."+coords2+".set"), "finished");
					   		        	
					   		        	 //Do nothing
					   		        	p.sendMessage(ChatColor.YELLOW+"You have finished "+plugin.getTasks().getString("Tasks."+coords2+".set")+"!");
					   		        	
					   		        	List<String> equipment = plugin.getTasks().getKeys("Tasks."+coords2+".Rewards");
						            	
					   		        	if (equipment!=null)
					   		        	{
							            	int s;
							            	for (s=0; s < equipment.size(); s++)
							                {
							            		String equipment_id = equipment.get(s);
							                	int equip_id = Integer.parseInt(equipment_id.substring(1));
							                	int amount = plugin.getTasks().getInt("Tasks."+coords2+".Rewards."+equipment_id, 0);
							                	
							                	ItemStack stack = new ItemStack(equip_id, amount);
							                	p.getInventory().addItem(stack);
							                	p.sendMessage(ChatColor.GOLD+"You have gained " + String.valueOf(amount) + " " + stack.getType().toString());
							                	p.updateInventory();
							                	
							                }
					   		        	}
					   		         }
					   		         else
					   		         {
					   		        	plugin.getTasks().setProperty(plugin.playerName(p)+"."+plugin.getTasks().getString("Tasks."+coords2+".set")+".Collect", "finished");
					   		         }
			   		        	 }
			   		        	 else
			   		        	 {
			   		        		 p.sendMessage(ChatColor.YELLOW+plugin.getTasks().getString("Tasks."+coords2+".set")+" task - Remaining blocks: " + String.valueOf(remaining_kills));
			   		        		plugin.playerConfig(p).setProperty(plugin.getTasks().getString("Tasks."+coords2+".set")+".Collect", String.valueOf(remaining_kills));
			   		        	 }
			   		         }
		   		         }
	     			}
	     		
			 }

	 }
    @Override
    public void onBlockBreak(BlockBreakEvent event)
    {
    	
    	//Check for an auto-regen zone
    	
    	boolean can_cast = false;
		WorldGuardPlugin worldGuard = getWorldGuard();
		RegionManager regionManager = worldGuard.getRegionManager(event.getPlayer().getWorld());
		
		Location location = event.getPlayer().getLocation();
		com.sk89q.worldedit.Vector v = new com.sk89q.worldedit.Vector(location.getX(), location.getY(), location.getZ());
		ApplicableRegionSet region2 =  worldGuard.getRegionManager(event.getPlayer().getWorld()).getApplicableRegions(v);
		
		Iterator<ProtectedRegion> r = region2.iterator();

		boolean is_regenerating = false;
		while (r.hasNext())
		{
			ProtectedRegion re = r.next();
			if (re.getId().contains("regen"))
			{
				is_regenerating = true;
				break;
			}
		}
    	
    	
        Player player = event.getPlayer();
        Block block = event.getBlock();
        
        if (block.getType()==Material.FIRE)
        {
        	event.setCancelled(true);
        	return;
        }
        
        if (event.isCancelled())
        	return;
        
        
    	if (is_regenerating)
    	{
    		//Create a block scheduler
    		LGRegen sprint = new LGRegen(event.getBlock(), event.getBlock().getType(), plugin);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, sprint, 600);
		}
    	
    	
        
        
        
        
        
        
        
    	int ignore_nomad = 1;
    	
    	if (ignore_nomad == 1)
    	{
        if (!is_regenerating && plugin.playerConfig(player).getString("nomad", "").equalsIgnoreCase("true"))
        {
        	player.sendMessage(ChatColor.RED+"You cannot break blocks as a nomad. Post an application on the minecraftforum and we will give you citizenship.");
        	player.sendMessage(ChatColor.YELLOW+"Go to http://tinyurl.com/skycraft4.");
        	event.setCancelled(true);
        	return;
        }
    	}
    	
        if (block.getType()==Material.SNOW)
        {
        	event.setCancelled(true);
        	block.setType(Material.AIR);
        }
      	
        
        Block b = event.getBlock();
        if (b.getType() != Material.SIGN_POST)
    	{
        	Block b2 = event.getBlock().getRelative(0, 1, 0);
        	if (b2.getType() == Material.SIGN_POST)
        	{
        		b=b2;
        	}
    	}
        
    	if (b.getType() == Material.SIGN_POST)
    	{
    		Sign the_sign = (Sign)b.getState();
    		
    		
  
				
				if (the_sign.getLine(1).equalsIgnoreCase(ChatColor.YELLOW+"[Telepad]"))
				{
					//Player just stood on a telepad!
					System.out.println(player.getName()+" just broke a telepad enterance");
					
						List<String> telepads = plugin.getTelepad().getKeys("Telepads");
		    			if (telepads!=null)
		    			{
		    				if (telepads.contains(the_sign.getLine(2)))
		    				{
		    					plugin.getTelepad().setProperty("Telepads."+the_sign.getLine(2)+".Enterance", "");
		    					
		    					String p = plugin.getTelepad().getString("Telepads."+the_sign.getLine(2)+".Player", "");
		    					if (!p.equalsIgnoreCase(""))
		    					{
		    						int enterances = plugin.playerConfig(p).getInt("telepadEnterances", 0);
		    						enterances++;
		    						plugin.playerConfig(p).setProperty("telepadEnterances", enterances);
		    					}
		    				}
		    			}
					//
				}
				else if (the_sign.getLine(1).equalsIgnoreCase(ChatColor.YELLOW+"[TE]"))
				{
					//Player just stood on a telepad!
					System.out.println(player.getName()+" just broke a telepad exit");
					
						List<String> telepads = plugin.getTelepad().getKeys("Telepads");
		    			if (telepads!=null)
		    			{
		    				if (telepads.contains(the_sign.getLine(2)))
		    				{
		    					plugin.getTelepad().setProperty("Telepads."+the_sign.getLine(2)+".Exit", "");
		    					
		    					String p = plugin.getTelepad().getString("Telepads."+the_sign.getLine(2)+".Player", "");
		    					if (!p.equalsIgnoreCase(""))
		    					{
		    						int enterances = plugin.playerConfig(p).getInt("telepadExits", 0);
		    						enterances++;
		    						plugin.playerConfig(p).setProperty("telepadExits", enterances);
		    					}
		    				}
		    			}
					//
				}

		    			
    		if (the_sign.getLine(1).equalsIgnoreCase(ChatColor.YELLOW+"[Settlement]")||the_sign.getLine(1).equalsIgnoreCase(ChatColor.YELLOW+"[Colony]")||the_sign.getLine(1).equalsIgnoreCase(ChatColor.YELLOW+"[Village]")||the_sign.getLine(1).equalsIgnoreCase(ChatColor.YELLOW+"[Town]"))
    		{
				String type = plugin.getSettle().getString("Settlements."+the_sign.getLine(2)+".Players."+player.getName(), "");
				if (type.equalsIgnoreCase("Owner"))
				{
					
	
    				

    	    		//CHECK FOR COSTS
    	    		if (the_sign.getLine(1).equalsIgnoreCase(ChatColor.YELLOW+"[Village]"))
    	    		{
    	    			plugin.giveMoney(player, Integer.valueOf(5000));
    	    		}
    	    		else if (the_sign.getLine(1).equalsIgnoreCase(ChatColor.YELLOW+"[Colony]"))
    	    		{
    	    			plugin.giveMoney(player, Integer.valueOf(2000));
    	    		}
    	    		else if (the_sign.getLine(1).equalsIgnoreCase(ChatColor.YELLOW+"[Town]"))
    	    		{
    	    			plugin.giveMoney(player, Integer.valueOf(15000));
    	    		}
    	    		
    	    		
    	    		
    			
	    		
	    		regionManager = worldGuard.getRegionManager(event.getBlock().getWorld());
	    		ProtectedRegion region =regionManager.getRegion(the_sign.getLine(2));
	    		if (region != null)
	    		{
	    			regionManager.removeRegion(the_sign.getLine(2));
	    		}
	    		
	    		java.util.List<String> players = plugin.getSettle().getKeys("Settlements."+the_sign.getLine(2)+".Players");
	        	if (players != null)
	    		{
	        		int i;
	        		for (i=0; i < players.size(); i++)
	        		{
	        			String name = players.get(i);
	        			plugin.getSettle().setProperty(name+".houseID", "");
	        		}
	    		}
	        	
	    		plugin.getSettle().removeProperty("Settlements."+the_sign.getLine(2));

	        	event.getPlayer().sendMessage(ChatColor.YELLOW+"Settlement deleted.");
	        	
				}
				else
				{
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED+"You cannot destroy this settlement. Only leaders or the owner can.");
				}
    		}
    	}
    	
    	blockDestroyed(event.getBlock(), event.getPlayer(), -1);
      	
        if (!plugin.canUseTool(player))
        {
        	event.setCancelled(true);
        	return;
        }
        

    	long current_time = System.currentTimeMillis();
 
    	String coords = String.format("%d %d %d", (int)event.getBlock().getLocation().getX(), (int)event.getBlock().getLocation().getY(), (int)event.getBlock().getLocation().getZ());
    	
    	
    	long previous_time = Long.valueOf(plugin.getBlocks().getString(coords, "0"));
    	long time_since = current_time-previous_time;
    	
        if (time_since > (1000*60*20))
        {
	        List<String> allowed_tools = plugin.getStatic().getKeys("Blocks");
	        String equip_string = String.format("B%d", block.getTypeId());
	        if (allowed_tools.contains(equip_string))
	    	{
	        	String skill = plugin.getStatic().getString("Blocks."+equip_string);
	        	int block_exp = plugin.getStatic().getInt("Skills." + skill + ".Blocks." + equip_string, 0);
	    		plugin.gainExperience(player, skill, block_exp);
	    		
	    		int radius = 3;
	    		int air = 0;
	    		
	    		//Check for cave.
	    		int playerX = (int)player.getLocation().getX();
	            int playerY = (int)player.getLocation().getY();
	            int playerZ = (int)player.getLocation().getZ();
	            List<Sign> signs = new ArrayList<Sign>();
	            World world = player.getWorld();
	            for(int x = (int)playerX-radius;x<playerX+radius;x ++){
	                for(int z = (int)playerZ-radius;z<playerZ+radius;z ++){
	                    for(int y = (int)playerY-radius;y<playerY+radius;y ++){
	                        Block bs = world.getBlockAt(x,y,z);
	                        if(!(bs.getType() == Material.AIR))continue;
	                        
	                        //Another air block!
	                        air++;
	                    }
	                }
	            }
	            
	            /*
	            if (player.isOp())
	            {
	            	if (air < 20)
	            		player.sendMessage(ChatColor.YELLOW+"Air: " + String.valueOf(air));
	            	else 
	            		player.sendMessage(ChatColor.YELLOW+"You are in a cave.");
	            }
	            */
	    		
	    		int light = (int)player.getWorld().getBlockAt(player.getLocation()).getLightLevel();
	    		
	    		
	    		if (light < 1)
	    		{
	    			
	    			
	    			int m = plugin.random_num(2, 1);	    			
	    			
	    			if (m == 1)
	    			{
	    				player.sendMessage(ChatColor.RED+"You fail to break the block as it is too dark.");
	    				event.setCancelled(true);
	    			}
	    		}
	    		
	    		if (light <= 1 && air < 20)
	    		{
	    			String coordss = String.format("%d %d %d", (int)player.getLocation().getX(), (int)player.getLocation().getY(), (int)player.getLocation().getZ());
		    		plugin.playerConfig(player).setProperty("Blocks4."+String.valueOf(event.getBlock().getTypeId()), plugin.playerConfig(player).getInt("Blocks4."+String.valueOf(event.getBlock().getTypeId()), 0)+1);
		    		
		    		//GET THE PERCENTAGES
		    		int diamonds = plugin.playerConfig(player).getInt("Blocks4.56", 0);
		    		int iron = plugin.playerConfig(player).getInt("Blocks4.15", 0);
		    		int gold = plugin.playerConfig(player).getInt("Blocks4.14", 0);
		    		int coal = plugin.playerConfig(player).getInt("Blocks4.16", 0);
		    		int stone = plugin.playerConfig(player).getInt("Blocks4.1", 0);
		    		
		    		int total_mined = diamonds+iron+gold+coal+stone;
		    		
		    		float diamond_percent = (float) ((diamonds*1.0)/(total_mined));
		    		float iron_percent = (float) ((iron*1.0)/(total_mined));
		    		float gold_percent = (float) ((gold*1.0)/(total_mined));
		    		float coal_percent = (float) ((coal*1.0)/(total_mined));
		    		float stone_percent = (float) ((stone*1.0)/(total_mined));
		    		
		    		boolean is_xraying = false;
		    		
		    		//The stone percentage should be > 0.9 SO make it > 0.8 on the safe side
		    		if (stone_percent < 0.8) { is_xraying = true; }
		    		else if (iron_percent > 0.3) { is_xraying = true; }
		    		else if (gold_percent > 0.2) { is_xraying = true; }
		    		else if (coal_percent > 0.4) { is_xraying = true; }
		    		else if (diamond_percent > 0.1) { is_xraying = true; }
		    		
		    		if (is_xraying && total_mined > 100)
		    		{
		    			//XRAYER!!! 
		    			String coord = String.format("%d %d %d", (int)player.getLocation().getX(), (int)player.getLocation().getY(), (int)player.getLocation().getZ());
			        	
		    			plugin.getXrayers().setProperty(player.getName(), coord);
		    			//Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+player.getName() +" appears to be x-raying. An admin will investigate further when they are online.");
		    		}
	    		}
	    		
	    		//10/(9166+98+392+59+10+10+47)
	    	}
        }
        else
        {
        	if (plugin.verbosePlayer(player))
        	{
        		player.sendMessage(ChatColor.RED+"That block was placed too recently to get experience from it.");
        	}
        }
        
        
        String coords2 = String.format("%d %d %d", (int)event.getBlock().getLocation().getX(), (int)event.getBlock().getLocation().getY(), (int)event.getBlock().getLocation().getZ());
        plugin.getBlocks().setProperty(coords2, "0");
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

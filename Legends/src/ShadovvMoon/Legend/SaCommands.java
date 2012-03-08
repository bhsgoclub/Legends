package ShadovvMoon.Legend;

import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.bukkit.commands.RegionCommands;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

 
public class SaCommands implements CommandExecutor 
{
	 private final Legends plugin;

	 public SaCommands(Legends plugin)
	 {
	       this.plugin = plugin;
	 }
	 
	 @Override
	 public boolean onCommand(CommandSender sender, Command command, String label, String[] split)
	 {
		 String cmd = command.getLabel();
		 if (cmd.equalsIgnoreCase("town"))
		 {
			 if (split.length == 0 ||  (split[0].equalsIgnoreCase("help")))
			 {
				 if (!(sender instanceof Player))
					 return false;
				 
				 Player player = (Player)sender;
				 player.sendMessage(ChatColor.RED+"Town Usage");
				 player.sendMessage(ChatColor.YELLOW+"/town list " + ChatColor.WHITE+"List the members in your town."); //DONE
				 player.sendMessage(ChatColor.YELLOW+"/town leave " + ChatColor.WHITE+"Leave your current town."); //DONE
				 player.sendMessage(ChatColor.YELLOW+"/town create " + ChatColor.WHITE+"Instructions for town creation."); //DONE
				 player.sendMessage(ChatColor.YELLOW+"/town size " + ChatColor.WHITE+"Information regarding the sizes of towns."); //DONE
				 player.sendMessage(ChatColor.YELLOW+"/town invite <name>" + ChatColor.WHITE+"Invite a member to your town"); //DONE
				 player.sendMessage(ChatColor.YELLOW+"/town accept <name> " + ChatColor.WHITE+"Join a town"); //DONE i suppose
				 player.sendMessage(ChatColor.YELLOW+"/town kick <name> " + ChatColor.WHITE+"Kick a member from your town"); //Buggy... i think?
				 player.sendMessage(ChatColor.YELLOW+"/town op <name> " + ChatColor.WHITE+"Add member as a leader of your town"); //DONE :0
				 player.sendMessage(ChatColor.YELLOW+"/town deop <name> " + ChatColor.WHITE+"Remove member as a leader of your town"); //Done i suppose
				 player.sendMessage(ChatColor.YELLOW+"/town chat public " + ChatColor.WHITE+"Switch to public chat"); //DONE :0
				 player.sendMessage(ChatColor.YELLOW+"/town chat private" + ChatColor.WHITE+"Switch to private chat"); //Done i suppose
				 
				 return true;
			 }
			 
			 
			 if (split[0].equalsIgnoreCase("chat"))
			 {
				 if (!(sender instanceof Player))
					 return false;
				 
				 Player player = (Player)sender;
				 
				 if (split.length>1)
				 {
					String channel = split[1];
					if (channel.equalsIgnoreCase("public"))
					{
						player.sendMessage(ChatColor.YELLOW+"Switched to public chat.");
						plugin.playerConfig(player).setProperty("CHATCHANNEL", "PUBLIC");
					}
					else if (channel.equalsIgnoreCase("private"))
					{
						player.sendMessage(ChatColor.YELLOW+"Switched to town chat.");
						plugin.playerConfig(player).setProperty("CHATCHANNEL", "PRIVATE");
					}
					else
					{
						player.sendMessage(ChatColor.YELLOW+"/town chat public " + ChatColor.WHITE+"Switch to public chat"); //DONE :0
						 player.sendMessage(ChatColor.YELLOW+"/town chat private" + ChatColor.WHITE+"Switch to private chat"); //Done i suppose
					}
				 }
				 else
				 {
					 player.sendMessage(ChatColor.YELLOW+"/town chat public " + ChatColor.WHITE+"Switch to public chat"); //DONE :0
					 player.sendMessage(ChatColor.YELLOW+"/town chat private" + ChatColor.WHITE+"Switch to private chat"); //Done i suppose
				 }
				 
				 return true;
			 }
			 
			 if (split[0].equalsIgnoreCase("list"))
			 {
				 if (!(sender instanceof Player))
						return false;
				 
				String player_name = sender.getName();
				 
				String houseID = plugin.getSettle().getString(player_name+".houseID", "");
		    	if (houseID.equalsIgnoreCase(""))
		    	{
		    		sender.sendMessage(ChatColor.RED+"You are not in a settlement!");
		    	}
		    	else
		    	{
		    		java.util.List<String> players2 = plugin.getSettle().getKeys("Settlements."+houseID+".Players");
		        	if (players2 != null)
		    		{
		        		int i;
		        		for (i=0; i < players2.size(); i++)
		        		{
		        			String name = players2.get(i);
		        			
		        			if (getPlayer(null, name)!=null)
		        				sender.sendMessage(ChatColor.GREEN+name+ChatColor.WHITE+":" + plugin.getSettle().getString("Settlements."+houseID+".Players."+name, ""));
		        			else
		        				sender.sendMessage(ChatColor.RED+name+ChatColor.WHITE+":" + plugin.getSettle().getString("Settlements."+houseID+".Players."+name, ""));
		        		}
		    		}
		        	
		    	}
			 }
			 else if (split[0].equalsIgnoreCase("walls"))
			 {
				 
				 if (!(sender instanceof Player))
						return false;
				 
				String player_name = sender.getName();
				 
				String houseID = plugin.getSettle().getString(player_name+".houseID", "");
		    	if (houseID.equalsIgnoreCase(""))
		    	{
		    		sender.sendMessage(ChatColor.RED+"You are not in a settlement!");
		    	}
		    	else
		    	{
		    		//They are in a settlement
		    		Player player = (Player)sender;
		    		
		    		String type = plugin.getSettle().getString("Settlements."+houseID+".Players."+player.getName(), "");
					if ((type.equalsIgnoreCase("Owner")||type.equalsIgnoreCase("Leader"))&&plugin.getSettle().getString("Settlements."+houseID+".Active", "").equalsIgnoreCase("YES"))
					{
						//Oke, make em a leader ;)
						//Good. Add the town to their invite list :)

		    		String center_loc = plugin.getSettle().getString("Settlements."+houseID+".Middle", "");
		    		
				 String[] crds = center_loc.split(" ");
 				Location settlement_loc = new Location(player.getWorld(), Integer.valueOf(crds[0]), Integer.valueOf(crds[1]), Integer.valueOf(crds[2]));
	    			
 				Block b = player.getWorld().getBlockAt(settlement_loc);
 				Sign the_sign = (Sign)b.getState();
 				
 				type = the_sign.getLine(1);
 				
					int size = 0;
	     		if (type.contains("[Settlement]"))
	     			size = 5;
	     		else if (type.contains("[Colony]"))
	         		size = 22;
	     		else if (type.contains("[Village]"))
	         		size = 37;
	     		else if (type.contains("[Town]"))
	         		size = 72;
				 
				 	Block bound1= b.getRelative(size, 0, -size);
		    		Block bound2= b.getRelative(size, 0, size);
		    		Block bound3= b.getRelative(-size, 0, -size);
		    		Block bound4= b.getRelative(-size, 0, size);
		    		
		    		
		    		if (type.contains("[Village]")||type.contains("[Town]")||type.contains("[Colony]"))
		    		{
	        			//CREATE THE PALISADE!
		    			Block current_block = bound2;
		    			
		    			int i;
		    			for (i=0; i < size*2; i++)
		    			{
		    				current_block.setTypeId(43);
		    				current_block.getRelative(0, 1, 0).setTypeId(43);
		    				current_block.getRelative(0, 2, 0).setTypeId(43);
		    				current_block.getRelative(0, 3, 0).setTypeId(43);
		    				current_block.getRelative(0, 4, 0).setTypeId(43);
		    				current_block.getRelative(0, 5, 0).setTypeId(43);
		    				current_block.getRelative(0, 6, 0).setTypeId(43);
		    				current_block.getRelative(0, 7, 0).setTypeId(43);
		    				current_block.getRelative(0, 8, 0).setTypeId(43);
		    				current_block.getRelative(0, 9, 0).setTypeId(43);
		    				
		    				current_block.getRelative(0, -1, 0).setTypeId(43);
		    				current_block.getRelative(0, -2, 0).setTypeId(43);
		    				current_block.getRelative(0, -3, 0).setTypeId(43);
		    				
		    				
		    				current_block = current_block.getRelative(-1, 0, 0);
		    				if (current_block == bound1 ||current_block == bound2||current_block == bound3||current_block == bound4)
		    					break;
		    			}
		    			
		    			current_block = bound2;
		    			for (i=0; i < size*2; i++)
		    			{
		    				current_block.setTypeId(43);
		    				current_block.getRelative(0, 1, 0).setTypeId(43);
		    				current_block.getRelative(0, 2, 0).setTypeId(43);
		    				current_block.getRelative(0, 3, 0).setTypeId(43);
		    				current_block.getRelative(0, 4, 0).setTypeId(43);
		    				current_block.getRelative(0, 5, 0).setTypeId(43);
		    				current_block.getRelative(0, 6, 0).setTypeId(43);
		    				current_block.getRelative(0, 7, 0).setTypeId(43);
		    				current_block.getRelative(0, 8, 0).setTypeId(43);
		    				current_block.getRelative(0, 9, 0).setTypeId(43);
		    				
		    				current_block.getRelative(0, -1, 0).setTypeId(43);
		    				current_block.getRelative(0, -2, 0).setTypeId(43);
		    				current_block.getRelative(0, -3, 0).setTypeId(43);
		    				current_block = current_block.getRelative(0, 0, -1);
		    				if (current_block == bound1 ||current_block == bound2||current_block == bound3||current_block == bound4)
		    					break;
		    			}
		    			
		    			current_block = bound3;
		    			for (i=0; i < size*2; i++)
		    			{
		    				current_block.setTypeId(43);
		    				current_block.getRelative(0, 1, 0).setTypeId(43);
		    				current_block.getRelative(0, 2, 0).setTypeId(43);
		    				current_block.getRelative(0, 3, 0).setTypeId(43);
		    				current_block.getRelative(0, 4, 0).setTypeId(43);
		    				current_block.getRelative(0, 5, 0).setTypeId(43);
		    				current_block.getRelative(0, 6, 0).setTypeId(43);
		    				current_block.getRelative(0, 7, 0).setTypeId(43);
		    				current_block.getRelative(0, 8, 0).setTypeId(43);
		    				current_block.getRelative(0, 9, 0).setTypeId(43);
		    				
		    				current_block.getRelative(0, -1, 0).setTypeId(43);
		    				current_block.getRelative(0, -2, 0).setTypeId(43);
		    				current_block.getRelative(0, -3, 0).setTypeId(43);
		    				current_block = current_block.getRelative(1, 0, 0);
		    				if (current_block == bound1 ||current_block == bound2||current_block == bound3||current_block == bound4)
		    					break;
		    			}
		    			
		    			current_block = bound3;
		    			for (i=0; i < size*2; i++)
		    			{
		    				current_block.setTypeId(43);
		    				current_block.getRelative(0, 1, 0).setTypeId(43);
		    				current_block.getRelative(0, 2, 0).setTypeId(43);
		    				current_block.getRelative(0, 3, 0).setTypeId(43);
		    				current_block.getRelative(0, 4, 0).setTypeId(43);
		    				current_block.getRelative(0, 5, 0).setTypeId(43);
		    				current_block.getRelative(0, 6, 0).setTypeId(43);
		    				current_block.getRelative(0, 7, 0).setTypeId(43);
		    				current_block.getRelative(0, 8, 0).setTypeId(43);
		    				current_block.getRelative(0, 9, 0).setTypeId(43);
		    				
		    				current_block.getRelative(0, -1, 0).setTypeId(43);
		    				current_block.getRelative(0, -2, 0).setTypeId(43);
		    				current_block.getRelative(0, -3, 0).setTypeId(43);
		    				
		    				current_block = current_block.getRelative(0, 0, 1);
		    				if (current_block == bound1 ||current_block == bound2||current_block == bound3||current_block == bound4)
		    					break;
		    			}
		    			
		    			
		    			
		    			}
					}
					else
					{
						player.sendMessage(ChatColor.RED+"You do not have permission for this command.");
					}
		    	}
		    	
			 }
			 else if (split[0].equalsIgnoreCase("op"))
			 {
				 if (!(sender instanceof Player))
						return false;
				 
				String player_name = sender.getName();
				 
				String houseID = plugin.getSettle().getString(player_name+".houseID", "");
		    	if (houseID.equalsIgnoreCase(""))
		    	{
		    		sender.sendMessage(ChatColor.RED+"You are not in a settlement!");
		    	}
		    	else
		    	{
		    		//They are in a settlement
		    		Player player = (Player)sender;
		    		
		    		String type = plugin.getSettle().getString("Settlements."+houseID+".Players."+player.getName(), "");
					if (type.equalsIgnoreCase("Owner"))
					{
						//Oke, make em a leader ;)
						//Good. Add the town to their invite list :)
						if (split.length > 1)
						{
							String invited = split[1];
							String player_name2 = invited;
							
							
							if (split[1].equalsIgnoreCase(player.getName()))
							{
								player.sendMessage(ChatColor.RED+"You cannot add yourself as a leader of this town.");
								return true;
							}
							 
							String houseID_KICK = plugin.getSettle().getString(player_name2+".houseID", "");
					    	if (!houseID_KICK.equalsIgnoreCase(houseID))
					    	{
					    		sender.sendMessage(ChatColor.RED+"The specified player is not in your town.");
					    	}
					    	else
					    	{
								String pll = invited;
			        			if (pll.equalsIgnoreCase(invited))
			        			{
			        				houseID_KICK = plugin.getSettle().getString(invited+".houseID", "");
			        	    		if (houseID.equalsIgnoreCase(houseID) )
			        	    		{
			        	    			
			        	    			Player g = getPlayer(player.getWorld(), invited);
										if (g != null)
										{
											g.sendMessage(ChatColor.YELLOW+"You are now a leader of this this settlement!");
										}
										
										
										java.util.List<String> players2 = plugin.getSettle().getKeys("Settlements."+houseID+".Players");
			        		        	if (players2 != null)
			        		    		{
			        		        		int i;
			        		        		for (i=0; i < players2.size(); i++)
			        		        		{
			        		        			String name = players2.get(i);
			        		        			
			        		        			Player m = getPlayer(player.getWorld(), name);
			        		        			if (m!=null)
			        		        			{
			        		        				m.sendMessage(ChatColor.GREEN+invited+" has been promoted to a leader of " + houseID + ".");
			        		        			}
			        		        			

			        		        		}
			        		    		}

			        	    			plugin.getSettle().setProperty("Settlements."+houseID+".Players."+invited, "Leader");
			        	    		}
			        			}
					    	}
						}
						else
						{
							player.sendMessage(ChatColor.YELLOW+"/town op <name> " + ChatColor.WHITE+"Add member as a leader of your town");
						}
						
					}
					else
					{
						sender.sendMessage(ChatColor.RED+"You are not the owner of this town.");
					}
		    	}
			 }
			 else if (split[0].equalsIgnoreCase("deop"))
			 {
				 if (!(sender instanceof Player))
						return false;
				 
				String player_name = sender.getName();
				 
				String houseID = plugin.getSettle().getString(player_name+".houseID", "");
		    	if (houseID.equalsIgnoreCase(""))
		    	{
		    		sender.sendMessage(ChatColor.RED+"You are not in a settlement!");
		    	}
		    	else
		    	{
		    		//They are in a settlement
		    		Player player = (Player)sender;
		    		
		    		String type = plugin.getSettle().getString("Settlements."+houseID+".Players."+player.getName(), "");
					if (type.equalsIgnoreCase("Owner"))
					{
						//Oke, make em a leader ;)
						//Good. Add the town to their invite list :)
						if (split.length > 1)
						{
							String invited = split[1];
							String player_name2 = invited;
							
							
							if (split[1].equalsIgnoreCase(player.getName()))
							{
								player.sendMessage(ChatColor.RED+"You cannot remove yourself as a leader of this town.");
								return true;
							}
							 
							String houseID_KICK = plugin.getSettle().getString(player_name2+".houseID", "");
					    	if (!houseID_KICK.equalsIgnoreCase(houseID))
					    	{
					    		sender.sendMessage(ChatColor.RED+"The specified player is not in your town.");
					    	}
					    	else
					    	{
								String pll = invited;
								
								String type2 = plugin.getSettle().getString("Settlements."+houseID+".Players."+invited, "");
								if (!type2.equalsIgnoreCase("Leader"))
								{
									sender.sendMessage(ChatColor.RED+"That player is not an op in your town!");
									return true;
								}
								
			        			if (pll.equalsIgnoreCase(invited))
			        			{
			        				houseID_KICK = plugin.getSettle().getString(invited+".houseID", "");
			        	    		if (houseID.equalsIgnoreCase(houseID) )
			        	    		{
			        	    			
			        	    			Player g = getPlayer(player.getWorld(), invited);
										if (g != null)
										{
											g.sendMessage(ChatColor.YELLOW+"You are no longer a leader of this this settlement!");
										}
										
										java.util.List<String> players2 = plugin.getSettle().getKeys("Settlements."+houseID+".Players");
			        		        	if (players2 != null)
			        		    		{
			        		        		int i;
			        		        		for (i=0; i < players2.size(); i++)
			        		        		{
			        		        			String name = players2.get(i);
			        		        			
			        		        			Player m = getPlayer(player.getWorld(), name);
			        		        			if (m!=null)
			        		        			{
			        		        				m.sendMessage(ChatColor.RED+invited+" was fired from their position as a leader of " + houseID + ".");
			        		        			}
			        		        			

			        		        		}
			        		    		}
			        		        	
			        	    			
			        	    			plugin.getSettle().setProperty("Settlements."+houseID+".Players."+invited, "Member");
			        	    		}
			        			}
					    	}
						}
						else
						{
							player.sendMessage(ChatColor.YELLOW+"/town deop <name> " + ChatColor.WHITE+"Remove member as a leader of your town");
						}
						
					}
					else
					{
						sender.sendMessage(ChatColor.RED+"You are not the owner of this town.");
					}
		    	}
			 }
			 
			 
			 if (split[0].equalsIgnoreCase("invite"))
			 {
				 //Add this town to the persons invite list
				 //What town is inviting who?
				 
				 
				 
				 if (!(sender instanceof Player))
						return false;
				 
				String player_name = sender.getName();
				 
				String houseID = plugin.getSettle().getString(player_name+".houseID", "");
		    	if (houseID.equalsIgnoreCase(""))
		    	{
		    		sender.sendMessage(ChatColor.RED+"You are not in a settlement!");
		    	}
		    	else
		    	{
		    		//They are in a settlement
		    		Player player = (Player)sender;
		    		
		    		String type = plugin.getSettle().getString("Settlements."+houseID+".Players."+player.getName(), "");
					if (type.equalsIgnoreCase("Owner")||type.equalsIgnoreCase("Leader"))
					{
						//Good. Add the town to their invite list :)
						if (split.length > 1)
						{
							String invited = split[1];
							Player g = getPlayer(player.getWorld(), invited);
							if (g != null)
							{
								List<String> invitations = plugin.getSettle().getKeys(g.getName()+".invitations");
								if (invitations != null)
								{
									int a; boolean contains = false;
									for (a = 0; a < invitations.size(); a++)
									{
										if (invitations.get(a).equalsIgnoreCase(houseID))
										{
											houseID = invitations.get(a); //FIX THE CASE SENSITIVE SHIT
											contains = true;
											break;
										}
									}
									
									if (contains)
									{
										//Already invited derp!
										player.sendMessage(ChatColor.RED+"That player already has an invitation to join " + houseID);
										return true;
									}
								}
								
								//Mmk accept em
								g.sendMessage(ChatColor.GREEN+player.getName()+" has invited you to join " + houseID);
								g.sendMessage(ChatColor.GREEN+"Type /town accept " + houseID + " to accept this invitation.");
								
								sender.sendMessage(ChatColor.GREEN+"You have invited " + g.getName()+" to join " + houseID);
								
								plugin.getSettle().setProperty(g.getName()+".invitations."+houseID, "true");
							}
							else
								player.sendMessage(ChatColor.RED+"That player is not currently online.");

						}
						else
						{
							player.sendMessage(ChatColor.YELLOW+"/town invite <name>" + ChatColor.WHITE+"Invite a member to your town");
							return true;
						}
						
					}
					else
					{
						sender.sendMessage(ChatColor.RED+"You are not a leader in your town.");
					}
		    	}
				 
				 
				 
				 
				 
				 
				 
			 }
			 else if (split[0].equalsIgnoreCase("kick"))
			 {
				 //Add this town to the persons invite list
				 //What town is inviting who?
				 
				 
				 
				 if (!(sender instanceof Player))
						return false;
				 
				String player_name = sender.getName();
				 
				String houseID = plugin.getSettle().getString(player_name+".houseID", "");
		    	if (houseID.equalsIgnoreCase(""))
		    	{
		    		sender.sendMessage(ChatColor.RED+"You are not in a settlement!");
		    	}
		    	else
		    	{
		    		//They are in a settlement
		    		Player player = (Player)sender;
		    		
		    		String type = plugin.getSettle().getString("Settlements."+houseID+".Players."+player.getName(), "");
					if (type.equalsIgnoreCase("Owner")||type.equalsIgnoreCase("Leader"))
					{
						//Good. Add the town to their invite list :)
						if (split.length > 1)
						{
							String invited = split[1];
							

							String player_name2 = invited;
							 
							String houseID_KICK = plugin.getSettle().getString(player_name2+".houseID", "");
					    	if (!houseID_KICK.equalsIgnoreCase(houseID))
					    	{
					    		sender.sendMessage(ChatColor.RED+"The specified player is not in your town.");
					    	}
					    	else
					    	{
					    		
					    		
					    		
					    		
					    		//They are in a settlement
					    		type = plugin.getSettle().getString("Settlements."+houseID+".Players."+invited, "");
								if (type.equalsIgnoreCase("Owner"))
								{
									player.sendMessage(ChatColor.RED+"You cannot kick the owner of this settlement!");
									return true;
								}
				
								Player g = getPlayer(player.getWorld(), invited);
								if (g != null)
								{
									g.sendMessage(ChatColor.RED+"You have been kicked from your town.");
								}
								
								
								java.util.List<String> players2 = plugin.getSettle().getKeys("Settlements."+houseID+".Players");
	        		        	if (players2 != null)
	        		    		{
	        		        		int i;
	        		        		for (i=0; i < players2.size(); i++)
	        		        		{
	        		        			String name = players2.get(i);
	        		        			
	        		        			Player m = getPlayer(player.getWorld(), name);
	        		        			if (m!=null)
	        		        			{
	        		        				m.sendMessage(ChatColor.RED+invited+" was kicked from " + houseID + ".");
	        		        			}
	        		        			

	        		        		}
	        		    		}
								
								
					    		String settlement_name = houseID;
					    		
					    		String center_loc = plugin.getSettle().getString("Settlements."+settlement_name+".Middle", "");
								String[] crds = center_loc.split(" ");
								
								Location settlement_loc = new Location(((Player) sender).getWorld(), Integer.valueOf(crds[0]), Integer.valueOf(crds[1]), Integer.valueOf(crds[2]));
				    			
								Block b = player.getWorld().getBlockAt(settlement_loc);
								Sign the_sign = (Sign)b.getState();
								
								String third_line = the_sign.getLine(3);
								String[] thecmd = third_line.split("/");
								
								int members = Integer.valueOf(thecmd[0]);
								int required = Integer.valueOf(thecmd[1]);
								
								if ((members > (required-2))||plugin.getSettle().getString("Settlements."+the_sign.getLine(2)+".Active", "").equalsIgnoreCase(""))
								{
									//Free to leave this settlement
									members--;
				
									//remove the new member to the build perms
									WorldGuardPlugin worldGuard = getWorldGuard();
						    		
						    		RegionManager regionManager = worldGuard.getRegionManager(player.getWorld());
						    		ProtectedRegion region = (ProtectedRegion) regionManager.getRegion(settlement_name);
						    		ProtectedRegion region2 = (ProtectedRegion) regionManager.getRegion(settlement_name+"_nopvp");
									
						    		if (region==null)
		        		    			region = (ProtectedRegion) regionManager.getRegion(invited.toLowerCase());
		        		    		if (region2==null)
		        		    			region2 = (ProtectedRegion) regionManager.getRegion((invited+"_nopvp").toLowerCase());
		        		    		
						    		
						    		if (region != null)
						    		{
							    		DefaultDomain mems = region.getMembers();
							    		mems.removePlayer(invited);
							    		
							    		region.setMembers(mems);
							    		region2.setMembers(mems);
						    		}
						    		
						    		plugin.getSettle().setProperty(invited+".houseID", "");
						    		plugin.getSettle().removeProperty("Settlements."+settlement_name+".Players."+invited);
						    		
									the_sign.setLine(3, String.format("%d/%d", members, required));
									the_sign.update();
									player.sendMessage(ChatColor.YELLOW+"The player has been kicked from the settlement.");
									
								}
								else
								{
									
									//*sigh* destroy the settlement
									
									
									
									WorldGuardPlugin worldGuard = getWorldGuard();
						    		
						    		RegionManager regionManager = worldGuard.getRegionManager(b.getWorld());
						    		ProtectedRegion region =regionManager.getRegion(the_sign.getLine(2));
						    		ProtectedRegion region2 =regionManager.getRegion(the_sign.getLine(2)+"_nopvp");
						    		if (region != null)
						    		{
						    			regionManager.removeRegion(the_sign.getLine(2));
						    		}
						    		if (region2 != null)
						    		{
						    			regionManager.removeRegion(the_sign.getLine(2)+"_nopvp");
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
						        	
						        	plugin.getSettle().setProperty(the_sign.getLine(2)+".houseID", "");
						    		plugin.getSettle().removeProperty("Settlements."+the_sign.getLine(2));
				
						        	//event.getPlayer().sendMessage(ChatColor.YELLOW+"Settlement deleted.");
						    		Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+settlement_name+" has been deleted due to lack of members.");
						    		try 
    	        		    		{
										regionManager.save();
									}
    	        		    		catch (IOException e)
    	        		    		{

									}
									
									//player.sendMessage(ChatColor.RED+"You cannot leave this settlement as it would be destroyed. Consult with the other members of your settlement about breaking the [Settlement] sign.");
								}
					    		
					    	}
					    	
					    	
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							

						}
						else
						{
							 player.sendMessage(ChatColor.YELLOW+"/town kick <name> " + ChatColor.WHITE+"Kick a member from your town");
							return true;
						}
						
					}
					else
					{
						sender.sendMessage(ChatColor.RED+"You are not a leader in your town.");
					}
		    	}
				 
				 
				 
				 
				 
				 
				 
			 }
			 else  if (split[0].equalsIgnoreCase("accept")||split[0].equalsIgnoreCase("join"))
			 {
				 if (!(sender instanceof Player))
						return false;
				 
				String player_name = sender.getName();
				 
				String houseID = plugin.getSettle().getString(player_name+".houseID", "");
		    	if (houseID.equalsIgnoreCase(""))
		    	{
		    		Player player = (Player)sender;
		    		
		    		//Lets find what their after
		    		if (split.length > 1)
					{
		    			String invited = split[1];
		    			List<String> invitations = plugin.getSettle().getKeys(player_name+".invitations");
						
		    			if (invitations != null)
						{
		    				int a; boolean contains = false;
							for (a = 0; a < invitations.size(); a++)
							{
								if (invitations.get(a).equalsIgnoreCase(invited))
								{
									invited = invitations.get(a); //FIX THE CASE SENSITIVE SHIT
									contains = true;
									break;
								}
							}
							
							if (contains)
							{
								//ACCEPT EM!
								
								
								
								
								plugin.getSettle().removeProperty(player_name+".invitations."+invited);
								
								
								
								
								
								
								
								String pll = player.getName();
			        			if (pll.equalsIgnoreCase(player.getName()))
			        			{
			        	    		if (houseID.equalsIgnoreCase(""))
			        	    		{
			        	    			String center_loc = plugin.getSettle().getString("Settlements."+invited+".Middle", "");
			    	    	    		
			    	        			if (!center_loc.equalsIgnoreCase(""))
			    	        			{
			    		    				player.sendMessage(ChatColor.YELLOW+"You have joined this settlement!");
			    		    				
			    		    				plugin.getSettle().setProperty("Settlements."+invited+".Players."+player.getName(), "Member");
			    		    				plugin.getSettle().setProperty(player.getName()+".houseID", invited);

			    	        				String[] crds = center_loc.split(" ");
			    	        				Location settlement_loc = new Location(player.getWorld(), Integer.valueOf(crds[0]), Integer.valueOf(crds[1]), Integer.valueOf(crds[2]));
			    	    	    			
			    	        				Block b = player.getWorld().getBlockAt(settlement_loc);
			    	        				Sign the_sign = (Sign)b.getState();
			    	        				
			    	        				String third_line = the_sign.getLine(3);
			    	        				String[] cmd1 = third_line.split("/");
			    	        				
			    	        				int members = Integer.valueOf(cmd1[0]);
			    	        				int required = Integer.valueOf(cmd1[1]);
			    	        				
			    	        				members+=1;
			    	        				
			    	        				java.util.List<String> players = plugin.getSettle().getKeys("Settlements."+invited+".Players");
		    	        		        	if (players != null)
		    	        		    		{
		    	        		        		int i;
		    	        		        		for (i=0; i < players.size(); i++)
		    	        		        		{
		    	        		        			String name = players.get(i);
		    	        		        			
		    	        		        			Player m = getPlayer(player.getWorld(), name);
		    	        		        			if (m!=null)
		    	        		        			{
		    	        		        				m.sendMessage(ChatColor.GREEN+player.getName()+" has joined " + invited + "!");
		    	        		        			}
		    	        		        			

		    	        		        		}
		    	        		    		}
			    	        				
			    	        				
			    	        				if (members == required)
			    	        				{
			    	        					
			    	        					String type = plugin.getSettle().getString("Settlements."+invited+".Type", "");
			    	        					plugin.getSettle().setProperty("Settlements."+invited+".Active", "YES");
			    	        					
			    	        					if (type.equalsIgnoreCase("[Settlement]"))
			    	        						Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+invited+" has setup a new settlement!");
			    	        					
			    	        	        		else if (type.equalsIgnoreCase("[Colony]"))
			    	        	        			Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+invited+" has setup a new colony! A no-pvp area has been created and a simple palisade wall has been setup around its perimeter.");
			    	        					
			    	        	        		else if (type.equalsIgnoreCase("[Village]"))
			    	        	        			Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+invited+" has setup a new village! A no-pvp area has been created and a simple palisade wall has been setup around its perimeter.");
			    	        					
			    	        	        		else if (type.equalsIgnoreCase("[Town]"))
			    	        	        			Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+invited+" has setup a new town! A no-pvp area has been created and a simple palisade wall has been setup around its perimeter.");
			    	        					
			    	        					
			    	        					//Create the palisade
			    	        					
			    	        					
			    	        					the_sign.setLine(3, String.format("%d/%d", required, required));
			    	        					the_sign.update();
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
			    	        		    		
			    	        		    		
			    	        		    		if (type.equalsIgnoreCase("[Village]")||type.equalsIgnoreCase("[Town]")||type.equalsIgnoreCase("[Colony]"))
			    	        		    		{
			    	        	        			//CREATE THE PALISADE!
			    	        		    			Block current_block = bound2;
			    	        		    			
			    	        		    			int i;
			    	        		    			for (i=0; i < size*2; i++)
			    	        		    			{
			    	        		    				current_block.setTypeId(43);
			    	        		    				current_block.getRelative(0, 1, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 2, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 3, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 4, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 5, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 6, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 7, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 8, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 9, 0).setTypeId(43);
			    	        		    				
			    	        		    				current_block.getRelative(0, -1, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, -2, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, -3, 0).setTypeId(43);
			    	        		    				
			    	        		    				
			    	        		    				current_block = current_block.getRelative(-1, 0, 0);
			    	        		    				if (current_block == bound1 ||current_block == bound2||current_block == bound3||current_block == bound4)
			    	        		    					break;
			    	        		    			}
			    	        		    			
			    	        		    			current_block = bound2;
			    	        		    			for (i=0; i < size*2; i++)
			    	        		    			{
			    	        		    				current_block.setTypeId(43);
			    	        		    				current_block.getRelative(0, 1, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 2, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 3, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 4, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 5, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 6, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 7, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 8, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 9, 0).setTypeId(43);
			    	        		    				
			    	        		    				current_block.getRelative(0, -1, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, -2, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, -3, 0).setTypeId(43);
			    	        		    				current_block = current_block.getRelative(0, 0, -1);
			    	        		    				if (current_block == bound1 ||current_block == bound2||current_block == bound3||current_block == bound4)
			    	        		    					break;
			    	        		    			}
			    	        		    			
			    	        		    			current_block = bound3;
			    	        		    			for (i=0; i < size*2; i++)
			    	        		    			{
			    	        		    				current_block.setTypeId(43);
			    	        		    				current_block.getRelative(0, 1, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 2, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 3, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 4, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 5, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 6, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 7, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 8, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 9, 0).setTypeId(43);
			    	        		    				
			    	        		    				current_block.getRelative(0, -1, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, -2, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, -3, 0).setTypeId(43);
			    	        		    				current_block = current_block.getRelative(1, 0, 0);
			    	        		    				if (current_block == bound1 ||current_block == bound2||current_block == bound3||current_block == bound4)
			    	        		    					break;
			    	        		    			}
			    	        		    			
			    	        		    			current_block = bound3;
			    	        		    			for (i=0; i < size*2; i++)
			    	        		    			{
			    	        		    				current_block.setTypeId(43);
			    	        		    				current_block.getRelative(0, 1, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 2, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 3, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 4, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 5, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 6, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 7, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 8, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, 9, 0).setTypeId(43);
			    	        		    				
			    	        		    				current_block.getRelative(0, -1, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, -2, 0).setTypeId(43);
			    	        		    				current_block.getRelative(0, -3, 0).setTypeId(43);
			    	        		    				
			    	        		    				current_block = current_block.getRelative(0, 0, 1);
			    	        		    				if (current_block == bound1 ||current_block == bound2||current_block == bound3||current_block == bound4)
			    	        		    					break;
			    	        		    			}
			    	        		    			
			    	        		    			
			    	        		    			
			    	        		    		}
			    	        		    		
			    	        					bound1.setType(Material.GLOWSTONE);
			    	        		    		bound2.setType(Material.GLOWSTONE);
			    	        		    		bound3.setType(Material.GLOWSTONE);
			    	        		    		bound4.setType(Material.GLOWSTONE);
			    	        		    		
			    	        		    		BlockVector pt = new BlockVector(bound3.getLocation().getX(), 0, bound3.getLocation().getZ());
			    	        		    		BlockVector pt2 = new BlockVector(bound2.getLocation().getX(), 128, bound2.getLocation().getZ());
			    	        		
			    	        		    		int nopvpsize = 15;
			    	        		    		Block bound11= b.getRelative(nopvpsize, 0, -nopvpsize);
			    	        		    		Block bound12= b.getRelative(nopvpsize, 0, nopvpsize);
			    	        		    		Block bound13= b.getRelative(-nopvpsize, 0, -nopvpsize);
			    	        		    		Block bound14= b.getRelative(-nopvpsize, 0, nopvpsize);
			    	        		    		
			    	        		    		BlockVector p2t = new BlockVector(bound13.getLocation().getX(), 0, bound13.getLocation().getZ());
			    	        		    		BlockVector pt22 = new BlockVector(bound12.getLocation().getX(), 128, bound12.getLocation().getZ());
			    	        		
			    	        		    		
			    	        		    		ProtectedCuboidRegion new_protection = new ProtectedCuboidRegion(invited,pt,pt2);
			    	        		    		ProtectedCuboidRegion new_protection_nopvp = new ProtectedCuboidRegion(invited+"_nopvp",p2t,pt22);
			    	        		    		
			    	        		    		WorldGuardPlugin worldGuard = getWorldGuard();
			    	        		    		RegionManager regionManager = worldGuard.getRegionManager(player.getWorld());
			    	        		    		if (new_protection==null)
		    		        		    			new_protection = (ProtectedCuboidRegion) regionManager.getRegion(invited.toLowerCase());
		    		        		    		if (new_protection_nopvp==null)
		    		        		    			new_protection_nopvp = (ProtectedCuboidRegion) regionManager.getRegion((invited+"_nopvp").toLowerCase());
		    		        		    		
			    	        		    		
			    	        		    		DefaultDomain domain = new DefaultDomain();

			    	        		    		

			    	        		    		DefaultDomain mems = new DefaultDomain();
			    	        		    		
			    	        		    		RegionCommands r = new RegionCommands();

			    	        		    		new_protection.setFlag(DefaultFlag.PVP, State.ALLOW);
			    	        		    		new_protection.setFlag(DefaultFlag.CHEST_ACCESS, State.ALLOW);
			    	        		    		
			    	        		    		new_protection_nopvp.setFlag(DefaultFlag.PVP, State.DENY);
			    	        		    		new_protection_nopvp.setFlag(DefaultFlag.CHEST_ACCESS, State.ALLOW);
			    	        		    		
			    	        		    		new_protection_nopvp.setPriority(1000);
			    	        		    		
			    	        		    		players = plugin.getSettle().getKeys("Settlements."+invited+".Players");
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
			    	        		    		
			    	        		    		new_protection_nopvp.setMembers(mems);
			    	        		    		new_protection_nopvp.setOwners(domain);
			    	        		    		
			    	        		    		
			    	        		    		
			    	        		    		
			    	        		    		regionManager.addRegion(new_protection);
			    	        		    		regionManager.addRegion(new_protection_nopvp);
			    	        		    		
			    	        		    		
			    	        		    		try 
			    	        		    		{
													regionManager.save();
												}
			    	        		    		catch (IOException e)
			    	        		    		{
			
												}
			    	        				}
			    	        				else
			    	        				{
			    	        					if (members > required)
			    	        					{
			    	        						//Add the new member to the build perms
			    	        						WorldGuardPlugin worldGuard = getWorldGuard();
			    		        		    		
			    		        		    		RegionManager regionManager = worldGuard.getRegionManager(player.getWorld());
			    		        		    		
			    		        		    		ProtectedRegion region = (ProtectedRegion) regionManager.getRegion(invited);
			    		        		    		ProtectedRegion region2 = (ProtectedRegion) regionManager.getRegion(invited+"_nopvp");

			    		        		    		if (region==null)
			    		        		    			region = (ProtectedRegion) regionManager.getRegion(invited.toLowerCase());
			    		        		    		if (region2==null)
			    		        		    			region2 = (ProtectedRegion) regionManager.getRegion((invited+"_nopvp").toLowerCase());
			    		        		    		
			    		        		    		DefaultDomain mems = region.getMembers();
			    		        		    		mems.addPlayer(sender.getName());
			    		        		    		
			    		        		    		region.setMembers(mems);
			    		        		    		region2.setMembers(mems);
			    	        					}
			    	        						
			    	        					the_sign.setLine(3, String.format("%d/%d", members, required));
			    	        					the_sign.update();
			    	        				}
			    	        				
			    	        				
			    	        				WorldGuardPlugin worldGuard = getWorldGuard();
			    	        		  		RegionManager regionManager = worldGuard.getRegionManager(player.getWorld());
								    		ProtectedRegion region = (ProtectedRegion) regionManager.getRegion(houseID);
								    		ProtectedRegion region2 = (ProtectedRegion) regionManager.getRegion(houseID+"_nopvp");
											
								    		if (region==null)
				        		    			region = (ProtectedRegion) regionManager.getRegion(invited.toLowerCase());
				        		    		if (region2==null)
				        		    			region2 = (ProtectedRegion) regionManager.getRegion((invited+"_nopvp").toLowerCase());
				        		    		
								    		
								    		if (region != null)
								    		{
									    		DefaultDomain mems = region.getMembers();
									    		mems.addPlayer(player.getName());
									    		region.setMembers(mems);
								    		}
								    		if (region2 != null)
								    		{
									    		DefaultDomain mems = region2.getMembers();
									    		mems.addPlayer(player.getName());
									    		region2.setMembers(mems);
								    		}
								    		
								    		
								    		
								    		
			    	        				
			    	        				//sendGlobalBlockChange(event.getClickedBlock().getLocation(), event.getClickedBlock().getType(), event.getClickedBlock().getData());
			    	        				//event.getClickedBlock().setType(Material.AIR);
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
			        			}
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
							}
							else
							{
								player.sendMessage(ChatColor.RED+"You haven't been invited to " + invited + ".");
							}
						}
		    			else
		    			{
		    				player.sendMessage(ChatColor.RED+"You haven't been invited to any towns.");
		    			}
		    
					}
					else
					{
						player.sendMessage(ChatColor.YELLOW+"/town accept <name> " + ChatColor.WHITE+"Join a town");
						return true;
					}
		    	}
		    	else
		    	{
		    		sender.sendMessage(ChatColor.RED+"You are already in a settlement! Type /town leave before joining a new town.");
		    	}
			 }
			 else if (split[0].equalsIgnoreCase("leave"))
			 {
										 if (!(sender instanceof Player))
												return false;
										 
										 
										 
										 
										 
										String player_name = sender.getName();
										 
										String houseID = plugin.getSettle().getString(player_name+".houseID", "");
								    	if (houseID.equalsIgnoreCase(""))
								    	{
								    		sender.sendMessage(ChatColor.RED+"You are not in a settlement!");
								    	}
								    	else
								    	{
								    		
								    		//They are in a settlement
								    		Player player = (Player)sender;
								    		
								    		String type = plugin.getSettle().getString("Settlements."+houseID+".Players."+player.getName(), "");
											if (type.equalsIgnoreCase("Owner"))
											{
												player.sendMessage(ChatColor.RED+"You can't leave your own settlement!");
												return false;
											}
							
								    		String settlement_name = houseID;
								    		
								    		String center_loc = plugin.getSettle().getString("Settlements."+settlement_name+".Middle", "");
											String[] crds = center_loc.split(" ");
											
											Location settlement_loc = new Location(((Player) sender).getWorld(), Integer.valueOf(crds[0]), Integer.valueOf(crds[1]), Integer.valueOf(crds[2]));
							    			
											Block b = player.getWorld().getBlockAt(settlement_loc);
											Sign the_sign = (Sign)b.getState();
											
											String third_line = the_sign.getLine(3);
											String[] thecmd = third_line.split("/");
											
											int members = Integer.valueOf(thecmd[0]);
											int required = Integer.valueOf(thecmd[1]);
											
											if ((members > (required-4))||plugin.getSettle().getString("Settlements."+the_sign.getLine(2)+".Active", "").equalsIgnoreCase(""))
											{
												//Free to leave this settlement
												members--;
							
												//remove the new member to the build perms
												WorldGuardPlugin worldGuard = getWorldGuard();
									    		
									    		RegionManager regionManager = worldGuard.getRegionManager(player.getWorld());
									    		ProtectedRegion region = (ProtectedRegion) regionManager.getRegion(settlement_name);
									    		ProtectedRegion region2 = (ProtectedRegion) regionManager.getRegion(settlement_name+"_nopvp");
												
									    		if (region==null)
		    		        		    			region = (ProtectedRegion) regionManager.getRegion(settlement_name.toLowerCase());
		    		        		    		if (region2==null)
		    		        		    			region2 = (ProtectedRegion) regionManager.getRegion((settlement_name+"_nopvp").toLowerCase());
		    		        		    		
									    		
									    		if (region != null)
									    		{
										    		DefaultDomain mems = region.getMembers();
										    		mems.removePlayer(player.getName());
										    		
										    		region.setMembers(mems);
										    		region2.setMembers(mems);
									    		}
									    		
									    		plugin.getSettle().setProperty(player.getName()+".houseID", "");
									    		plugin.getSettle().removeProperty("Settlements."+settlement_name+".Players."+player.getName());
									    		
												the_sign.setLine(3, String.format("%d/%d", members, required));
												the_sign.update();
												
												player.sendMessage(ChatColor.YELLOW+"You have left the settlement.");
												
												
												
												try 
			    	        		    		{
													regionManager.save();
												}
			    	        		    		catch (IOException e)
			    	        		    		{
			
												}
												
											}
											else
											{
												
												//*sigh* destroy the settlement
												
												
												
												WorldGuardPlugin worldGuard = getWorldGuard();
									    		
									    		RegionManager regionManager = worldGuard.getRegionManager(b.getWorld());
									    		ProtectedRegion region =regionManager.getRegion(the_sign.getLine(2));
									    		ProtectedRegion region2 =regionManager.getRegion(the_sign.getLine(2)+"_nopvp");
									    		
									    		
									    		
									    		
									    		if (region==null)
		    		        		    			region = (ProtectedRegion) regionManager.getRegion(settlement_name.toLowerCase());
		    		        		    		if (region2==null)
		    		        		    			region2 = (ProtectedRegion) regionManager.getRegion((settlement_name+"_nopvp").toLowerCase());
		    		        		    		
									    		
									    		
									    		
									    		if (region != null)
									    		{
									    			regionManager.removeRegion(the_sign.getLine(2));
									    			
									    			if (region2 != null)
									    				regionManager.removeRegion(the_sign.getLine(2)+"_nopvp");
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
									        	
									        	plugin.getSettle().setProperty(the_sign.getLine(2)+".houseID", "");
									    		plugin.getSettle().removeProperty("Settlements."+the_sign.getLine(2));
							
									        	//event.getPlayer().sendMessage(ChatColor.YELLOW+"Settlement deleted.");
									    		Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+settlement_name+" has been deleted due to lack of members.");

									    		try 
			    	        		    		{
													regionManager.save();
												}
			    	        		    		catch (IOException e)
			    	        		    		{
			
												}
												
												//player.sendMessage(ChatColor.RED+"You cannot leave this settlement as it would be destroyed. Consult with the other members of your settlement about breaking the [Settlement] sign.");
											}
								    		
								    	}
								    	
								    	
								    	
			 }
			 else if (split[0].equalsIgnoreCase("create"))
			 {
				 if (!(sender instanceof Player))
					 return false;
				 
				 Player player = (Player)sender;
				 player.sendMessage(ChatColor.RED+"");
				 player.sendMessage(ChatColor.RED+"Town creation:");
				 player.sendMessage(ChatColor.YELLOW+"To create a town, place "+ChatColor.GREEN+"[Colony]"+ChatColor.YELLOW+", "+ChatColor.GREEN+"[Village]"+ChatColor.YELLOW+" or "+ChatColor.GREEN+"[Town]"+ChatColor.YELLOW+" on the second line of a sign. Place the desired name of your town on the third line. You will require 3000c (+6 people), 10000c (+11 people) or 20000c (+21 people) for each type respectively.");
				 
				 
				
			 }
			 else if (split[0].equalsIgnoreCase("size"))
			 {
				 if (!(sender instanceof Player))
					 return false;
				 
				 Player player = (Player)sender;
				 player.sendMessage(ChatColor.RED+"Town Sizes:");
				 player.sendMessage(ChatColor.YELLOW+"Colony:"+ChatColor.WHITE+" 45x45 (3000c) 6 people");
				 player.sendMessage(ChatColor.YELLOW+"Village:"+ChatColor.WHITE+" 75x75 (10000c) 11 people");
				 player.sendMessage(ChatColor.YELLOW+"Town:"+ChatColor.WHITE+" 145x145 (20000c) 21 people");
			 }

		 }
		 else if (cmd.equalsIgnoreCase("setSettlementFlag"))
		 {
			 
			 if (!(sender instanceof Player))
				 return false;
			 
			 Player player = (Player)sender;
			 plugin.getSettle().setProperty(split[0], split[1]);
			 
			 
		 }
		 else if (cmd.equalsIgnoreCase("resetSettlement"))
		 {
			 
			 if (!(sender instanceof Player))
				 return false;
			 
			 Player player = (Player)sender;
			 Player p = getPlayer(null, split[0]);
			 if (p!=null)
			 {
				 if (player.isOp())
				 {
					 plugin.getSettle().setProperty(p.getName()+".houseID", "");
				 }
			 }
			 else
			 {
				 player.sendMessage(ChatColor.RED+"That player isn't online.");
			 }
			 
			 
		 }
		return true;
		 
	 }
	 
	 private WorldGuardPlugin getWorldGuard() {
	        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
	     
	        // WorldGuard may not be loaded
	        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	            return null; // Maybe you want throw an exception instead
	        }
	     
	        return (WorldGuardPlugin) plugin;
	    }
	 public Player getPlayer(World w, String name) {

			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				if (player.getName().equalsIgnoreCase(name))
					return player;
			}

			return null;

		}
	 
}



package ShadovvMoon.Legend;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.*;

public class LGSprinter implements Runnable
{

	public Player player;
    public Legends plugin;
    public LGSprinter(Player tochange, Legends p)
    {
        this.player = tochange;
        this.plugin = p;
    }
    
    public void cantWear(ItemStack i, Player p, String a)
    {
    	
    	p.getInventory().addItem(i);
    	
    	if (a.equalsIgnoreCase("Helmet"))
    	{
    		p.getInventory().setHelmet(null);
    	}
    	
    	if (a.equalsIgnoreCase("Chest"))
    	{
    		p.getInventory().setChestplate(null);
    	}
    	
    	if (a.equalsIgnoreCase("Legs"))
    	{
    		p.getInventory().setLeggings(null);
    	}
    	
    	if (a.equalsIgnoreCase("Boots"))
    	{
    		p.getInventory().setBoots(null);
    	}
    	
    	p.sendMessage(ChatColor.RED+"Your level in Defence is too low to wear a "+ i.getType().toString());
    }
    
    @Override
    public void run()
    {
        //do your stuff here
    	if (this.player.isSprinting())
    	{
    		this.plugin.gainMana(this.player, -4);
    		
    		int stam = plugin.skillLevel(this.player, "Sprint");
    		plugin.gainExperience(this.player, "Sprint", 2);
    	}
    }
} 
/*    	//this.player.setExperience(1);
    	//this.player.setLevel(1);
    	
    	
    	
    	//Check their armour values
    	Player p = this.player;
    	
    	
			ItemStack is = p.getInventory().getHelmet();
			String tp = is.getType().toString().toLowerCase();
			
			int level = 0;
			if (tp.contains("diamond")) level = 5;
			else if (tp.contains("iron")) level = 4;
			else if (tp.contains("chain")) level = 3;
			else if (tp.contains("gold")) level = 2;
			else if (tp.contains("leather")) level = 1;
	    	
	    	int def = plugin.skillExperience(p, "Defence");
	    	if (def <= 0)
	    	{
	    		if (level > 1)
	    		{
	    			cantWear(is, p, "Helmet");
	    		}
	    	}
	    	else if (def <= 0)
	    	{
	    		if (level > 2)
	    		{
	    			cantWear(is, p, "Helmet");
	    		}
	    	}
	    	else if (def <= 0)
	    	{
	    		if (level > 3)
	    		{
	    			cantWear(is, p, "Helmet");
	    		}
	    	}
	    	else if (def <= 0)
	    	{
	    		if (level > 4)
	    		{
	    			cantWear(is, p, "Helmet");
	    		}
	    	}
	    	else if (def <= 0)
	    		if (level > 5)
	    		{
	    			cantWear(is, p, "Helmet");
	    		}
	    	
	    	
	    	 is = p.getInventory().getChestplate();
				tp = is.getType().toString().toLowerCase();
				
				 level = 0;
				if (tp.contains("diamond")) level = 5;
				else if (tp.contains("iron")) level = 4;
				else if (tp.contains("chain")) level = 3;
				else if (tp.contains("gold")) level = 2;
				else if (tp.contains("leather")) level = 1;
				
				if (def <= 0)
		    	{
		    		if (level > 1)
		    		{
		    			cantWear(is, p, "Chest");
		    		}
		    	}
		    	else if (def <= 0)
		    	{
		    		if (level > 2)
		    		{
		    			cantWear(is, p, "Chest");
		    		}
		    	}
		    	else if (def <= 3)
		    	{
		    		if (level > 3)
		    		{
		    			cantWear(is, p, "Chest");
		    		}
		    	}
		    	else if (def <= 5)
		    	{
		    		if (level > 4)
		    		{
		    			cantWear(is, p, "Chest");
		    		}
		    	}
		    	else if (def <= 10)
		    		if (level > 5)
		    		{
		    			cantWear(is, p, "Chest");
		    		}
		    	
		    	
		    	 is = p.getInventory().getLeggings();
					tp = is.getType().toString().toLowerCase();
					
					 level = 0;
					if (tp.contains("diamond")) level = 5;
					else if (tp.contains("iron")) level = 4;
					else if (tp.contains("chain")) level = 3;
					else if (tp.contains("gold")) level = 2;
					else if (tp.contains("leather")) level = 1;

					if (def <= 0)
			    	{
			    		if (level > 1)
			    		{
			    			cantWear(is, p, "Legs");
			    		}
			    	}
			    	else if (def <= 1)
			    	{
			    		if (level > 2)
			    		{
			    			cantWear(is, p, "Legs");
			    		}
			    	}
			    	else if (def <= 3)
			    	{
			    		if (level > 3)
			    		{
			    			cantWear(is, p, "Legs");
			    		}
			    	}
			    	else if (def <= 5)
			    	{
			    		if (level > 4)
			    		{
			    			cantWear(is, p, "Legs");
			    		}
			    	}
			    	else if (def <= 10)
			    		if (level > 5)
			    		{
			    			cantWear(is, p, "Legs");
			    		}

		

			    	 is = p.getInventory().getBoots();
						tp = is.getType().toString().toLowerCase();
						
						 level = 0;
						if (tp.contains("diamond")) level = 5;
						else if (tp.contains("iron")) level = 4;
						else if (tp.contains("chain")) level = 3;
						else if (tp.contains("gold")) level = 2;
						else if (tp.contains("leather")) level = 1;

						if (def <= 0)
				    	{
				    		if (level > 1)
				    		{
				    			cantWear(is, p, "Boots");
				    		}
				    	}
				    	else if (def <= 1)
				    	{
				    		if (level > 2)
				    		{
				    			cantWear(is, p, "Boots");
				    		}
				    	}
				    	else if (def <= 3)
				    	{
				    		if (level > 3)
				    		{
				    			cantWear(is, p, "Boots");
				    		}
				    	}
				    	else if (def <= 5)
				    	{
				    		if (level > 4)
				    		{
				    			cantWear(is, p, "Boots");
				    		}
				    	}
				    	else if (def <= 10)
				    		if (level > 5)
				    		{
				    			cantWear(is, p, "Boots");
				    		}
			
    }
}
*/
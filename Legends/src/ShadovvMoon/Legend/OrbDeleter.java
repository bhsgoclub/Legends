package ShadovvMoon.Legend;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftExperienceOrb;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.*;

public class OrbDeleter implements Runnable
{

    public Legends plugin;
    public OrbDeleter(Legends p)
    {
        this.plugin = p;
    }
    
    @Override
    public void run()
    {
    	
    	List <World> world = Bukkit.getServer().getWorlds();
    	
    	int w;
    	for (w=0; w < world.size(); w++)
		{
    		
	    	List<Entity> entities = world.get(w).getEntities();
			 int o;
			 for (o=0; o < entities.size(); o++)
			 {
				 Entity entity = entities.get(o);
				 
				 if (entity instanceof CraftExperienceOrb)
				 {
					// entity.remove();
					 entity.setFireTicks(300);
					 entity.remove();
					 
				 }
			 }
		 }
    }
}

package ShadovvMoon.Legend;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class RodActivation extends PlayerListener {

	private Legends plugin;

	public RodActivation(Legends instance) {
		plugin = instance;
	}

	/*
	 * The proceeding method was written by Yurij Of Sweden A valued member of
	 * the Bukkit Forum.
	 * 
	 * further tweaked by me, Spoonikle. With help from FullWall.
	 */

	private static HashMap<Player, Long> firstUse = new HashMap<Player, Long>();
	static HashMap<Player, Long> fishingTime = new HashMap<Player, Long>();
	private static HashMap<Player, Long> missUse = new HashMap<Player, Long>();
	static Player fishingPlayer;

	public void onPlayerInteract(PlayerInteractEvent event) {

		if (!(event.getAction().equals(Action.RIGHT_CLICK_AIR))) {
			return;
		}
		Player player = event.getPlayer();
		Material fishingRod = event.getItem().getType();

		if (fishingRod == Material.FISHING_ROD) {

			if (missUse.containsKey(player)) {
				missUse.remove(player);
			} else {
				if (firstUse.containsKey(player)) {
					Long first = firstUse.get(player); // Retrieve the first
														// time
					Long second = System.currentTimeMillis(); // Retrieve the
																// second
																// time

					Long diff = second - first; // Gets the difference in
												// milliseconds
					fishingPlayer = player;
					fishingTime.put(player, diff);
					plugin.new CatchDelay(2);
					firstUse.remove(player); // Remove the players firstUse data

				} else {
					if (player.getTargetBlock(null, 20).getTypeId() == 8
							|| player.getTargetBlock(null, 20).getTypeId() == 9) {
						firstUse.put(player, System.currentTimeMillis());
					} else {
						missUse.put(player, System.currentTimeMillis());
					}
				}
			}
		}
	}

}
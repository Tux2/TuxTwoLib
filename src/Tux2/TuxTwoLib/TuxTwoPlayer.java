package Tux2.TuxTwoLib;

import java.io.File;

import net.minecraft.server.v1_4_5.EntityPlayer;
import net.minecraft.server.v1_4_5.ItemInWorldManager;
import net.minecraft.server.v1_4_5.MinecraftServer;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_4_5.CraftServer;
import org.bukkit.entity.Player;

public class TuxTwoPlayer {
	
	/**
	 * Returns an offline player that can be manipulated exactly like an online player.
	 * @param player Playername to retrieve.
	 * @return The player object. Null if we can't find the player's data.
	 */
	public static Player getOfflinePlayer(String player) {
		Player pplayer = null;
		try {
			//See if the player has data files

			// Find the player folder
			File playerfolder = new File(Bukkit.getWorlds().get(0).getWorldFolder(), "players");

			// Find player name
			for (File playerfile : playerfolder.listFiles()) {
				String filename = playerfile.getName();
				String playername = filename.substring(0, filename.length() - 4);

				if(playername.trim().equalsIgnoreCase(player)) {
					//This player plays on the server!
					MinecraftServer server = ((CraftServer)Bukkit.getServer()).getServer();
					EntityPlayer entity = new EntityPlayer(server, server.getWorldServer(0), playername, new ItemInWorldManager(server.getWorldServer(0)));
					Player target = (entity == null) ? null : (Player) entity.getBukkitEntity();
					if(target != null) {
						target.loadData();
						return target;
					}
				}
			}
		}
		catch(Exception e) {
			return null;
		}
		return pplayer;
	}

}

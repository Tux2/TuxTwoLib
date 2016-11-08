package Tux2.TuxTwoLib;

import java.io.File;

import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.MinecraftServer;
import net.minecraft.server.v1_10_R1.PlayerInteractManager;
import net.minecraft.server.v1_10_R1.World;
import com.mojang.authlib.GameProfile;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.entity.Player;

public class TuxTwoPlayer {
	
	/**
	 * Returns an offline player that can be manipulated exactly like an online player.
	 * @param player The player to retrieve.
	 * @return The player object. Null if we can't find the player's data.
	 */
	public static Player getOfflinePlayer(OfflinePlayer player) {
		Player pplayer = null;
		
		try {
			//See if the player has data files

			// Find the player folder
			File playerfolder = new File(Bukkit.getWorlds().get(0).getWorldFolder(), "playerdata");

			// Find player name
			for (File playerfile : playerfolder.listFiles()) {
				String filename = playerfile.getName();
				String playername = filename.substring(0, filename.length() - 4);

				if(playername.trim().equalsIgnoreCase(player.getUniqueId().toString())) {
					//This player plays on the server!
					MinecraftServer server = ((CraftServer)Bukkit.getServer()).getServer();
					EntityPlayer entity = new EntityPlayer(server, server.getWorldServer(0), new GameProfile(player.getUniqueId(), player.getName()), new PlayerInteractManager((World)server.getWorldServer(0)));
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
	
	/**
	 * Returns an offline player that can be manipulated exactly like an online player.
	 * @param player The player to retrieve.
	 * @return The player object. Null if we can't find the player's data.
	 */
	@Deprecated
	public static Player getOfflinePlayer(String player) {
		OfflinePlayer oplayer = Bukkit.getOfflinePlayer(player);
		return getOfflinePlayer(oplayer);
	}

}

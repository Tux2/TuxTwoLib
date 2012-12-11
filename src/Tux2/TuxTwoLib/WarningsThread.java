package Tux2.TuxTwoLib;

import java.util.LinkedList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class WarningsThread implements Runnable {
	
	LinkedList<Player> players = new LinkedList<Player>();
	TuxTwoLib plugin;
	
	public WarningsThread(TuxTwoLib plugin) {
		this.plugin = plugin;
	}
	
	public void addPlayer(Player player) {
		players.add(player);
	}

	@Override
	public void run() {
		for(Player player : players) {
			if(player.isOnline()) {
				if(plugin.incompatiblemcversion && plugin.hasupdate) {
					player.sendMessage(ChatColor.RED + "WARNING! A Minecraft version update has forced an update of the TuxTwoLib to version " + plugin.newversion + ". Please restart your server.");
				}else if(plugin.hasupdate) {
					player.sendMessage(ChatColor.RED + "Your version of TuxTwoLib has been updated to version " + plugin.newversion + "! Please restart the server.");
				}else if(plugin.incompatiblemcversion && !plugin.newversion.equals("")) {
					player.sendMessage(ChatColor.DARK_RED + "Your version of TuxTwoLib is incompatible with this version of Craftbukkit! Update to version " + plugin.newversion + " immediately!");
				}else if(plugin.incompatiblemcversion) {
					player.sendMessage(ChatColor.DARK_RED + "Your version of TuxTwoLib is incompatible with this version of Craftbukkit! Update immediately!");
				}else if(plugin.updatefailed) {
					player.sendMessage(ChatColor.RED + "TuxTwoLib failed to update automatically! Please update to version " + plugin.newversion + " manually.");
				}else if(!plugin.newversion.equals("")) {
					player.sendMessage(ChatColor.RED + "Your version of TuxTwoLib is out of date! Please update to version " + plugin.newversion);
				}
			}
		}
		players.clear();
	}

}

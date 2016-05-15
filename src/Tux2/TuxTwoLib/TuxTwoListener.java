package Tux2.TuxTwoLib;

import java.lang.reflect.Field;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class TuxTwoListener implements Listener {

	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		if(player instanceof CraftHumanEntity) {
			CraftHumanEntity cplayer = (CraftHumanEntity)player;
			try {
				Field f = CraftHumanEntity.class.getDeclaredField("inventory");
				f.setAccessible(true);
				f.set(cplayer, new TuxTwoInventoryPlayer((CraftInventoryPlayer)f.get(cplayer)));
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}

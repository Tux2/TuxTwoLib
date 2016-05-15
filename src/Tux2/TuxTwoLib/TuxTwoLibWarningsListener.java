package Tux2.TuxTwoLib;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class TuxTwoLibWarningsListener implements Listener {

    TuxTwoLib plugin;

    public TuxTwoLibWarningsListener(TuxTwoLib plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (event.getPlayer().hasPermission("tuxtwolib.notices")) {
            if (this.plugin.warnings == null) {
                this.plugin.warnings = new WarningsThread(this.plugin);
                this.plugin.warnings.addPlayer(event.getPlayer());
                this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, this.plugin.warnings, 4);
            }
        }
    }
}

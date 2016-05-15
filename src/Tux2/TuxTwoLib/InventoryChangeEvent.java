package Tux2.TuxTwoLib;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class InventoryChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    int slot = -1;
    boolean armor = false;
    ItemStack[] items = null;
    Player player;

    public InventoryChangeEvent(Player player, ItemStack... items) {
        this.items = items;
        this.player = player;
    }

    public InventoryChangeEvent(Player player, int slot, ItemStack... items) {
        this.items = items;
        this.slot = slot;
        this.player = player;
    }

    public InventoryChangeEvent(Player player, int slot, boolean armor, ItemStack... items) {
        this.items = items;
        this.slot = slot;
        this.armor = armor;
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public int getSlot() {
        return this.slot;
    }

    public boolean isArmor() {
        return this.armor;
    }

    public ItemStack[] getItems() {
        return this.items;
    }

    public Player getPlayer() {
        return this.player;
    }

}

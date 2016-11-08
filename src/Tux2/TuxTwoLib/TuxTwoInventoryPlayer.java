package Tux2.TuxTwoLib;

import java.util.HashMap;

import net.minecraft.server.v1_10_R1.PlayerInventory;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TuxTwoInventoryPlayer extends CraftInventoryPlayer {
	
	public TuxTwoInventoryPlayer(PlayerInventory inventory) {
		super(inventory);
	}
	
	public TuxTwoInventoryPlayer(CraftInventoryPlayer inventory) {
		super(inventory.getInventory());
	}

	@Override
	public void setArmorContents(ItemStack[] items) {
		super.setArmorContents(items);
		InventoryChangeEvent eventcall = new InventoryChangeEvent((Player)getHolder(), 0, true, items);
		Bukkit.getServer().getPluginManager().callEvent(eventcall);
	}

	@Override
	public void setBoots(ItemStack boots) {
		super.setBoots(boots);
		InventoryChangeEvent eventcall = new InventoryChangeEvent((Player)getHolder(), 0, true, boots);
		Bukkit.getServer().getPluginManager().callEvent(eventcall);
	}

	@Override
	public void setChestplate(ItemStack chestplate) {
		super.setChestplate(chestplate);
		InventoryChangeEvent eventcall = new InventoryChangeEvent((Player)getHolder(), 2, true, chestplate);
		Bukkit.getServer().getPluginManager().callEvent(eventcall);
	}

	@Override
	public void setHelmet(ItemStack helmet) {
		super.setHelmet(helmet);
		InventoryChangeEvent eventcall = new InventoryChangeEvent((Player)getHolder(), 3, true, helmet);
		Bukkit.getServer().getPluginManager().callEvent(eventcall);
	}

	@Override
	public void setLeggings(ItemStack leggings) {
		super.setLeggings(leggings);
		InventoryChangeEvent eventcall = new InventoryChangeEvent((Player)getHolder(), 1, true, leggings);
		Bukkit.getServer().getPluginManager().callEvent(eventcall);
	}

	@Override
	public HashMap<Integer, ItemStack> addItem(ItemStack... items) {
		InventoryChangeEvent eventcall = new InventoryChangeEvent((Player)getHolder(), items);
		Bukkit.getServer().getPluginManager().callEvent(eventcall);
		return super.addItem(items);
	}

	@Override
	public void setContents(ItemStack[] items) {
		super.setContents(items);
		InventoryChangeEvent eventcall = new InventoryChangeEvent((Player)getHolder(), 0, items);
		Bukkit.getServer().getPluginManager().callEvent(eventcall);
	}

	@Override
	public void setItem(int index, ItemStack item) {
		super.setItem(index, item);
		InventoryChangeEvent eventcall = new InventoryChangeEvent((Player)getHolder(), index, item);
		Bukkit.getServer().getPluginManager().callEvent(eventcall);
	}

}

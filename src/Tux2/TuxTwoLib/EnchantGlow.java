package Tux2.TuxTwoLib;

import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;

import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * Class, that allows adding glow effect to item.
 */
public class EnchantGlow {
	
	/**
	 * Adds the glow.
	 * 
	 * @param item
	 *            the item
	 * @return the item stack
	 */
	public static ItemStack addGlow(ItemStack item) {
		net.minecraft.server.ItemStack nmsStack = null;
		CraftItemStack craftStack = null;
		if (item instanceof CraftItemStack) {
			craftStack = (CraftItemStack) item;
			nmsStack = craftStack.getHandle();
		}
		else {
			craftStack = new CraftItemStack(item);
			nmsStack = craftStack.getHandle();
		}
		NBTTagCompound tag = null;
		if (!nmsStack.hasTag()) {
			tag = new NBTTagCompound();
			nmsStack.setTag(tag);
		}
		if (tag == null) tag = nmsStack.getTag();
		NBTTagList ench = new NBTTagList();
		tag.set("ench", ench);
		nmsStack.setTag(tag);
		return craftStack;
	}
	
	/**
	 * Removes the glow.
	 * 
	 * @param item
	 *            the item
	 * @return the item stack
	 */
	public static ItemStack removeGlow(ItemStack item) {
		net.minecraft.server.ItemStack nmsStack = null;
		CraftItemStack craftStack = null;
		if (item instanceof CraftItemStack) {
			craftStack = (CraftItemStack) item;
			nmsStack = craftStack.getHandle();
		}
		else {
			craftStack = new CraftItemStack(item);
			nmsStack = craftStack.getHandle();
		}
		NBTTagCompound tag = null;
		if (!nmsStack.hasTag()) return item;
		tag = nmsStack.getTag();
		tag.remove("ench");
		nmsStack.setTag(tag);
		return craftStack;
	}
}

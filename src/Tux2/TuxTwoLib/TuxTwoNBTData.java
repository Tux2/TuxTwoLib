package Tux2.TuxTwoLib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;

import org.bukkit.craftbukkit.inventory.CraftItemStack;

public class TuxTwoNBTData {
	
	/**
	 * Returns a base64 encoded string of the NBT data in the item stack.
	 * @param item the stack to extract the nbt data from.
	 * @return base64 encoded nbt data if it exists. null if no nbt data.
	 */
	public static String readOutNBTString(org.bukkit.inventory.ItemStack item) {
		CraftItemStack stack = null;
		if(item instanceof CraftItemStack) {
			stack = (CraftItemStack)item;
		}else if(item instanceof org.bukkit.inventory.ItemStack) {
			return null;
		}
		ItemStack newitem = stack.getHandle();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		if(newitem == null || newitem.tag == null) {
			return null;
		}
		NBTBase.a(newitem.getTag(), dos);
		return javax.xml.bind.DatatypeConverter.printBase64Binary(bos.toByteArray());
	}
	
	/**
	 * Adds NBT data from a previously base64 encoded NBT string.
	 * @param item The item to attach the NBT code to.
	 * @param base64encoded The base64 encoded NBT data.
	 * @return The item with NBT code attached.
	 */
	public static org.bukkit.inventory.ItemStack readInNBTString(org.bukkit.inventory.ItemStack item, String base64encoded) {
		byte[] unserialized = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64encoded);
		CraftItemStack stack = null;
		if(item instanceof CraftItemStack) {
			stack = (CraftItemStack)item;
		}else if(item instanceof org.bukkit.inventory.ItemStack) {
			stack = new CraftItemStack(item);
		}
		ItemStack newitem = stack.getHandle();
		ByteArrayInputStream bais = new ByteArrayInputStream(unserialized);
		DataInputStream dis = new DataInputStream(bais);
		newitem.setTag((NBTTagCompound) NBTBase.b(dis));
		return stack;
	}

}

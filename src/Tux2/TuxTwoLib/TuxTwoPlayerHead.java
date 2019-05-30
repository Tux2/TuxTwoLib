package Tux2.TuxTwoLib;

import java.util.UUID;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import Tux2.TuxTwoLib.attributes.Attributes;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;

public class TuxTwoPlayerHead {
	
	public static ItemStack getHead(ItemStack is, NMSHeadData head) {
		return getHead(is, head.getId(), head.getTexture());
	}
	
	public static ItemStack getHead(ItemStack is, UUID id , String texture) {
		net.minecraft.server.v1_12_R1.ItemStack stack = CraftItemStack.asNMSCopy(is);
		NBTTagCompound tag = stack.getTag();
		if(tag == null) {
			tag = new NBTTagCompound();
		}
		NBTTagCompound skullowner = new NBTTagCompound();
		skullowner.setString("Id", id.toString());
		NBTTagCompound properties = new NBTTagCompound();
		NBTTagList textures = new NBTTagList();
		NBTTagCompound ntexture = new NBTTagCompound();
		ntexture.setString("Value", texture);
		textures.add(ntexture);
		properties.set("textures", textures);
		skullowner.set("Properties", properties);
		tag.set("SkullOwner", skullowner);
		stack.setTag(tag);
		return CraftItemStack.asCraftMirror(stack);
	}
	
	public static NMSHeadData getHeadData(ItemStack is) {
		try{
			net.minecraft.server.v1_12_R1.ItemStack mcis = Attributes.getMinecraftItemStack(is);
			if(mcis == null) {
				return null;
			}
			NBTTagCompound tag = mcis.getTag();
			if(tag == null) {
				return null;
			}
			NBTTagCompound skullowner = tag.getCompound("SkullOwner");
			if(skullowner == null) {
				return null;
			}
			try {
				UUID id = UUID.fromString(skullowner.getString("Id"));
				NBTTagCompound properties = skullowner.getCompound("Properties");
				if(properties != null) {
					NBTTagList textures = properties.getList("textures", 10);
					if(textures != null && textures.size() > 0) {
						NBTTagCompound ntexture = textures.get(0);
						if(ntexture != null) {
							String texture = ntexture.getString("Value");
							return new NMSHeadData(id, texture);
						}
					}
				}
			}catch(IllegalArgumentException e) {
				return null;
			}
			return null;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

}

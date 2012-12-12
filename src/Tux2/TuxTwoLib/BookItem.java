package Tux2.TuxTwoLib;

import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagString;

import org.bukkit.craftbukkit.inventory.CraftItemStack;

public class BookItem {
	
	private net.minecraft.server.ItemStack item = null;
	private CraftItemStack stack = null;
	
	/**
	 * Create a book item out of an ItemStack
	 * @param item the book.
	 */
	public BookItem(org.bukkit.inventory.ItemStack item) {
		if(item instanceof CraftItemStack) {
			stack = (CraftItemStack)item;
			this.item = stack.getHandle();
		}else if(item instanceof org.bukkit.inventory.ItemStack) {
			stack = new CraftItemStack(item);
			this.item = stack.getHandle();
		}
	}
	
	/**
	 * Returns the pages of the book as a String array.
	 * @return the pages of the book. Null if no pages.
	 */
	public String[] getPages() {
		NBTTagCompound tags = item.getTag();
		if(tags == null) {
			return null;
		}
		NBTTagList pages = tags.getList("pages");
		String[] pagestrings = new String[pages.size()];
		for(int i = 0; i < pages.size(); i++) {
			pagestrings[i] = pages.get(i).toString();
		}
		return pagestrings;
	}
	
	/**
	 * Gets the author of the book.
	 * @return author of the book. Null if author hasn't been set.
	 */
	public String getAuthor() {
		NBTTagCompound tags = item.getTag();
		if(tags == null) {
			return null;
		}
		String author = tags.getString("author");
		return author;
	}
	
	/**
	 * 
	 * @return Title of the book. Null if title hasn't been set.
	 */
	public String getTitle() {
		NBTTagCompound tags = item.getTag();
		if(tags == null) {
			return null;
		}
		String title = tags.getString("title");
		return title;
	}
	
	/**
	 * Sets the pages in a book.
	 * @param newpages The pages of the book.
	 */
	public void setPages(String[] newpages) {
		NBTTagCompound tags = item.tag;
        if (tags == null) {
            tags = item.tag = new NBTTagCompound();
        }
    	NBTTagList pages = new NBTTagList("pages");
    	//we don't want to throw any errors if the book is blank!
    	if(newpages.length == 0) {
    		pages.add(new NBTTagString("1", ""));
    	}else {
        	for(int i = 0; i < newpages.length; i++) {
        		pages.add(new NBTTagString("" + i + "", newpages[i]));
        	}
    	}
    	tags.set("pages", pages);
	}
	
	/**
	 * Add pages to the end of the book.
	 * @param newpages pages to add.
	 */
	public void addPages(String[] newpages) {
		NBTTagCompound tags = item.tag;
        if (tags == null) {
            tags = item.tag = new NBTTagCompound();
        }
        NBTTagList pages;
        if(getPages() == null) {
        	pages = new NBTTagList("pages");
        }else {
        	pages = tags.getList("pages");
        }
    	//we don't want to throw any errors if the book is blank!
    	if(newpages.length == 0 && pages.size() == 0) {
    		pages.add(new NBTTagString("1", ""));
    	}else {
        	for(int i = 0; i < newpages.length; i++) {
        		pages.add(new NBTTagString("" + pages.size() + "", newpages[i]));
        	}
    	}
    	tags.set("pages", pages);
	}
	
	/**
	 * Sets the author of the book.
	 * @param author
	 */
	public void setAuthor(String author) {
		NBTTagCompound tags = item.tag;
        if (tags == null) {
            tags = item.tag = new NBTTagCompound();
        }
    	if(author != null && !author.equals("")) {
        	tags.setString("author", author);
    	}
	}
	
	/**
	 * Sets the title of the book.
	 * @param title
	 */
	public void setTitle(String title) {
		NBTTagCompound tags = item.tag;
        if (tags == null) {
            tags = item.tag = new NBTTagCompound();
        }
    	if(title != null && !title.equals("")) {
        	tags.setString("title", title);
    	}
	}
	
	/**
	 * Returns the completed book item.
	 * @return the completed book. Ready for insertion into the game.
	 */
	public org.bukkit.inventory.ItemStack getItemStack() {
		return stack;
	}

}

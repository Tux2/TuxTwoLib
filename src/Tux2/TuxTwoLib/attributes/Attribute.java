package Tux2.TuxTwoLib.attributes;

import net.minecraft.server.v1_10_R1.NBTTagCompound;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

import com.google.common.base.Preconditions;

/**
 * Represents a single Attribute that can be applied to ItemStacks.
 * @author Ruud Verbeek
 * @see AttributeList, AttributeType, Operation
 */

public class Attribute {

	String type;
	int operation = -1;
	double amount = 0;
	UUID uuid;
	
	/**
	 * Constructs an Attribute with the given {@link AttributeType}, {@link Operation}, amount and {@link UUID}.
	 * @param type The type of this Attribute.
	 * @param operation The operation this Attribute uses to apply its amount to the final value.
	 * @param amount The amount of this Attribute.
	 * @param uuid This Attribute's {@link UUID}. Must be unique.
	 */
	
	public Attribute(String type, int operation, double amount, UUID uuid){
		this.type=type;
		this.operation=operation;
		this.amount=amount;
		this.uuid=uuid;
	}
	
	/**
	 * Constructs an Attribute using the given {@link AttributeType}, amount and {@link UUID}. {@link Operation#ADD_NUMBER} will be used as {@link Operation}.
	 * @param type The type of this Attribute.
	 * @param amount The amount of this Attribute.
	 * @param uuid This Attribute's {@link UUID}. Must be unique.
	 */
	
	public Attribute(String type, double amount, UUID uuid){
		this(type,Operation.ADD_NUMBER.id,amount,uuid);
	}
	
	/**
	 * Constructs an Attribute using the given {@link AttributeType}, {@link Operation} and amount. A random {@link UUID} will be used.
	 * @param type The type of this Attribute.
	 * @param operation The operation this Attribute uses to apply its amount to the final value.
	 * @param amount The amount of this Attribute
	 */
	public Attribute(String type, int operation, double amount){
		this(type,operation,amount,UUID.randomUUID());
	}
	
	/**
	 * Constructs an Attribute using the give {@link AttributeType} and amount. {@link Operation#ADD_NUMBER} will be used as {@link Operation} and a random {@link UUID} will be used.
	 * @param type The type of this Attribute.
	 * @param amount The amount of this Attribute.
	 */
	
	public Attribute(String type, double amount){
		this(type,Operation.ADD_NUMBER.id,amount,UUID.randomUUID());
	}
	
	/**
	 * Constructs an Attribute with {@link Operation#ADD_NUMBER} as {@link Operation}, 0.0 as amount and a random {@link UUID}. The {@link AttributeType} must still be set using {@link #setType}.
	 */
	
	public Attribute(){
		this(null,Operation.ADD_NUMBER.id,0.0,UUID.randomUUID());
	}
	
	/**
	 * Sets the {@link AttributeType} of this Attribute.
	 * @param type The {@link AttributeType}
	 * @return This Attribute
	 */
	public Attribute setType(String type){
		this.type=type;
		return this;
	}

	/**
	 * Sets the {@link Operation} of this Attribute.
	 * @param operation The {@link Operation}
	 * @return This Attribute
	 */
	public Attribute setOperation(Operation operation){
		this.operation=operation.id;
		return this;
	}

	/**
	 * Sets the {@link Operation} of this Attribute.
	 * @param operation The {@link Operation}
	 * @return This Attribute
	 */
	public Attribute setOperation(int operation){
		this.operation=operation;
		return this;
	}

	/**
	 * Sets the amount of this Attribute.
	 * @param amount The amount
	 * @return This Attribute
	 */
	public Attribute setAmount(double amount){
		this.amount=amount;
		return this;
	}

	/**
	 * Sets the {@link UUID} of this Attribute.
	 * @param uuid The {@link UUID}
	 * @return This Attribute
	 */
	public Attribute setUUID(UUID uuid){
		this.uuid=uuid;
		return this;
	}
	
	/**
	 * Returns this Attribute's {@link AttributeType}
	 * @return The {@link AttributeType}
	 */
	public String getType(){
		return type;
	}

	/**
	 * Returns this Attribute's {@link Operation}
	 * @return The {@link Operation}
	 */
	public int getOperation(){
		return operation;
	}

	/**
	 * Returns this Attribute's amount
	 * @return The amount
	 */
	public double getAmount(){
		return amount;
	}

	/**
	 * Returns this Attribute's {@link UUID}
	 * @return The {@link UUID}
	 */
	public UUID getUUID(){
		return uuid;
	}
	
	NBTTagCompound write() throws InstantiationException, IllegalAccessException{
		Preconditions.checkNotNull(type,"Type cannot be null.");
		if(operation== -1) {
			operation=Operation.ADD_NUMBER.id;
		}
		if(uuid==null) {
			uuid=UUID.randomUUID();
		}
		
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("AttributeName",type);
		tag.setString("Name",type);
		tag.setInt("Operation",operation);
		tag.setDouble("Amount",amount);
		tag.setLong("UUIDMost",uuid.getMostSignificantBits());
		tag.setLong("UUIDLeast",uuid.getLeastSignificantBits());
		return tag;
	}
	
	/**
	 * Returns whether or not this attribute is equal to o.
	 */
	
	public boolean equals(Object o){
		if(!(o instanceof Attribute))
			return false;
		Attribute a = (Attribute)o;
		return(uuid.equals(a.uuid) || (type==a.type && operation==a.operation && amount==a.amount));
	}
	
	static Attribute fromTag(NBTTagCompound c) throws IllegalArgumentException{
		Attribute a = new Attribute();
		if(c.hasKey("AttributeName")) {
			a.setType(c.getString("AttributeName"));
		}else {
			throw new IllegalArgumentException("No AttributeName specified.");
		}
		if(c.hasKey("Operation")) {
			a.setOperation(Operation.fromID(c.getInt("Operation")));
		}else {
			 throw new IllegalArgumentException("No Operation specified.");
		}
		if(c.hasKey("Amount")) {
			a.setAmount(c.getDouble("Amount"));
		}else {
			throw new IllegalArgumentException("No Amount specified.");
		}
		if(c.hasKey("UUIDMost") && c.hasKey("UUIDLeast")) {
			a.setUUID(new UUID(c.getLong("UUIDLeast"),c.getLong("UUIDMost")));
		}else {
			a.setUUID(UUID.randomUUID());
		}
		return a;
	}
	
	/**
	 * Shortcut to {@link Attributes#apply}.
	 * @param is ItemStack to apply this attribute to
	 * @param replace Whether or not to replace existing attributes on the ItemStack
	 * @return the new ItemStack containing this Attribute
	 */
	
	public ItemStack apply(ItemStack is, boolean replace){
		return Attributes.apply(is, this, replace);
	}
	
}

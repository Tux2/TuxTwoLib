package Tux2.TuxTwoLib;

import java.util.UUID;

public class NMSHeadData {
	
	UUID id = null;
	String texture = null;
	
	public NMSHeadData(UUID id, String texture) {
		this.id = id;
		this.texture = texture;
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getTexture() {
		return texture;
	}

}

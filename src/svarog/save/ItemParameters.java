package svarog.save;

public class ItemParameters {
	private int itemGlobalID = -1;
	private int itemTileID = -1;
	private int itemPropability=1;
	
	public ItemParameters(int itemGlobalID, int itemTileID) {
		this.setItemGlobalID(itemGlobalID);
		this.setItemTileID(itemTileID);
	}
	
	public ItemParameters(int itemGlobalID, int itemTileID, int itemPropability) {
		this.setItemGlobalID(itemGlobalID);
		this.setItemTileID(itemTileID);
		this.setItemPropability(itemPropability);
	}
	
	public int getItemTileID() {
		return itemTileID;
	}
	public void setItemTileID(int itemTileID) {
		this.itemTileID = itemTileID;
	}
	public int getItemGlobalID() {
		return itemGlobalID;
	}
	public void setItemGlobalID(int itemGlobalID) {
		this.itemGlobalID = itemGlobalID;
	}

	public int getItemPropability() {
		return itemPropability;
	}

	public void setItemPropability(int itemPropability) {
		this.itemPropability = itemPropability;
	}
	
	
}

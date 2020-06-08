package svarog.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import svarog.entity.Player;
import svarog.objects.Item;

public class TileSheet {
	private List<Group> tileGroups;
	
	private int requestDeleteItem;
	
	public static enum tileGroupType {
		inventory,
		other
	}
	
	public int inventorySize;
	
	public TileSheet() {
		this.tileGroups = new ArrayList<Group>();
		this.inventorySize = 0;
		requestDeleteItem(-1);
	}
	
	public void addTileGroup(Group group, tileGroupType type) {
		group.getGroupSize();
		
		if(type == tileGroupType.inventory) {
			inventorySize += group.getObjects().size();
		}
		
		tileGroups.add(group);
	}
	
	List<Group> getTileGroupsList() {
		return tileGroups;
	}
	
	/// For saves, data etc ///
	public Tile getTile(int tileId) {
		for(Group group : tileGroups) {
			for(GuiObject tile : group.getObjects()) {
				if(((Tile)tile).getTileId() == tileId)
					return (Tile)tile;
			}
		}
		
		return null;
	}
	
	/// For mouse interaction etc ////
	public Tile getTileByObjectId(int id) {
		for(Group group : tileGroups) {
			for(GuiObject tile : group.getObjects()) {
				if(tile.getId() == id)
					return (Tile)tile;
			}
		}
		
		return null;
	}
	
	public void putItemFirstEmpty(Item item, Player player) {
		boolean finished = false;
		
		Collections.sort(tileGroups);
		
		for(Group group : tileGroups) {
			Collections.sort(group.getObjects());
			
			for(GuiObject tile : group.getObjects()) {
				if(tile instanceof Tile) {
					Tile temp = (Tile)tile;
					if(temp.getPuttableItemTypes().size() > 1) {
						if(temp.getPuttedItem() == null) {
							try {
								temp.putItem(item, player);
								finished = true;
								break;
							} catch (Exception e) {}
						}		
					}
				}
			}
			if(finished)
				break;
		}
	}
	
	void putItemFirstEmpty(Item item) {
		boolean finished = false;
		
		for(Group group : tileGroups) {
			for(GuiObject tile : group.getObjects()) {
				if(tile instanceof Tile) {
					Tile temp = (Tile)tile;
					if(temp.getPuttableItemTypes().size() > 1) {
						if(temp.getPuttedItem() == null) {
							try {
								temp.putItem(item);
								finished = true;
								break;
							} catch (Exception e) {}
						}		
					}
				}
			}
			if(finished)
				break;
		}
	}
	
	public int size() {
		return inventorySize;
	}
	
	public Tile clickedTile() {
		for(Group group : tileGroups) {
			for(GuiObject tile : group.getTextureObjectList()) {
				if(tile.getId() == GuiRenderer.getClickedTileId()) {
					Tile temp = (Tile)tile;
					return temp;
				}
			}
		}
		
		return null;
	}
	
	public void removeItem(Tile tile) {
		tile.removePuttedItem();
		requestDeleteItem = -1;
	}
	
	public void cancelRemoveItem() {
		requestDeleteItem = -1;
	}

	public int itemToDelete() {
		return requestDeleteItem;
	}

	void requestDeleteItem(int requestDeleteItem) {
		this.requestDeleteItem = requestDeleteItem;
	}
}

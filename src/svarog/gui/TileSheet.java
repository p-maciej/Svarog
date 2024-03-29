package svarog.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import svarog.entity.Player;
import svarog.objects.Item;

public class TileSheet implements GroupProperties {
	private List<Group> tileGroups;
	
	private int requestDeleteItem;
	
	public TileSheet() {
		this.tileGroups = new ArrayList<Group>();
		requestDeleteItem(-1);
	}
	
	public void addTileGroup(Group group, groupType type) {
		group.getGroupSize();
		group.setType(type);	
		
		tileGroups.add(group);
	}
	
	public void addTileGroup(Group group) {
		group.getGroupSize();	
		
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
			if(group.getType() != groupType.swap) {
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
	}
	
	public int size() {
		int size = 0;
		for(Group group : tileGroups) {
			if(group.getType() == groupType.inventory)
				size += group.getObjects().size();
		}
		
		return size;
	}
	
	public int emptyTilesAmount() {
		int size = 0;
		for(Group group : tileGroups) {
			if(group.getType() == groupType.inventory) {
				for(GuiObject object : group.getObjects()) {
					if(object instanceof Tile) {
						if(((Tile)object).getPuttedItem() == null)
							size++;
					}
				}
			}
		}
		
		return size;
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

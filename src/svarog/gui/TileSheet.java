package svarog.gui;

import java.util.ArrayList;
import java.util.List;

public class TileSheet {
	private List<Group> tileGroups;
	
	public TileSheet() {
		this.tileGroups = new ArrayList<Group>();
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
			for(TextureObject tile : group.getObjects()) {
				if(((Tile)tile).getTileId() == tileId)
					return (Tile)tile;
			}
		}
		
		return null;
	}
	
	/// For mouse interaction etc ////
	public Tile getTileByObjectId(int id) {
		for(Group group : tileGroups) {
			for(TextureObject tile : group.getObjects()) {
				if(tile.getId() == id)
					return (Tile)tile;
			}
		}
		
		return null;
	}
}

package svarog.gui;

import java.util.ArrayList;
import java.util.List;

public class TileSheet {
	private List<Tile> tiles;
	private List<Group> tileGroups;
	
	public TileSheet() {
		this.tiles = new ArrayList<Tile>();
		this.tileGroups = new ArrayList<Group>();
	}
	
	public void addTile(Tile tile) {
		tiles.add(tile);
	}
	
	public void addTileGroup(Group group) {
		group.getGroupSize();
		tileGroups.add(group);
	}
	
	List<Tile> getTilesList() {
		return tiles;
	}
	
	List<Group> getTileGroupsList() {
		return tileGroups;
	}
	
	public Tile getTile(int id) {
		for(Tile tile : tiles) {
			if(tile.getId() == id)
				return tile;
		}
		
		for(Group group : tileGroups) {
			for(TextureObject tile : group.getObjects()) {
				if(tile.getId() == id)
					return (Tile)tile;
			}
		}
		
		return null;
	}
}

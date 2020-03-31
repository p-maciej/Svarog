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
}

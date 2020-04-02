package svarog.gui;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import svarog.gui.GuiRenderer.stickTo;
import svarog.objects.Item;
import svarog.render.Texture;

public class Tile extends TextureObject {
	private byte tileType;
	
	private List<Integer> puttableItemTypes;
	
	private Item puttedItem;
	
	private int tileId;
	
	public Tile(int tileId, Texture texture, byte tileType, Vector2f position) {
		super(texture, position);
		super.setOverable(true);
		this.setTileId(tileId);
		setPuttableItemTypes(new ArrayList<Integer>());
		
		this.setTileType(tileType);
	}
	
	public Tile(int tileId, Texture texture, byte tileType, int X, int Y) {
		super(texture, X, Y);
		super.setOverable(true);
		this.setTileId(tileId);
		setPuttableItemTypes(new ArrayList<Integer>());
		
		this.setTileType(tileType);
	}
	
	public Tile(int tileId, Texture texture, byte tileType, stickTo stickTo) {
		super(texture, stickTo);
		super.setOverable(true);
		this.setTileId(tileId);
		setPuttableItemTypes(new ArrayList<Integer>());
		
		this.setTileType(tileType);
	}

	public int getTileType() {
		return tileType;
	}
	
	public void setTileType(byte tileType) {
		this.tileType = tileType;
	}

	public void setPuttableItemTypes(List<Integer> puttableItemTypes) {
		this.puttableItemTypes = puttableItemTypes;
	}
	
	public void putItem(Item object) throws Exception {
		boolean hasBeenPutted = false;
		for(Integer type : puttableItemTypes) {
			if(type == object.getItemType()) {
				object.setPosition(this.getTransform().getPosition().x, this.getTransform().getPosition().y);
				this.puttedItem = object;
				hasBeenPutted = true;
				break;
			}
		}
		
		if(hasBeenPutted == false) {
			throw new Exception("Cannot put item in this tile");
		}
	}

	public Item getPuttedItem() {
		return puttedItem;
	}
	
	void removePuttedItem() {
		puttedItem = null;
	}

	public int getTileId() {
		return tileId;
	}

	public void setTileId(int tileId) {
		this.tileId = tileId;
	}
}

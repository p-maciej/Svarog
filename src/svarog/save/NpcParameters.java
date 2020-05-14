package svarog.save;

import java.util.ArrayList;
import java.util.List;

public class NpcParameters {
	private int globalNpcID=-1;
	private String texturePath = null;
	private int posX=0;
	private int posY=0;
	private boolean fullBoundingBox = false;
	private String name = null;

	private String interactionsFile = null;
	private List<ItemParameters> items = new ArrayList<>();
	
	public NpcParameters(int globalNpcID, String texturePath, int posX, int posY, boolean fullBoundingBox, String name,
			String interactionsPath, List<ItemParameters> items) {
		super();
		this.globalNpcID = globalNpcID;
		this.texturePath = texturePath;
		this.posX = posX;
		this.posY = posY;
		this.fullBoundingBox = fullBoundingBox;
		this.name = name;
		this.interactionsFile = interactionsPath;
		this.items = items;
	}
	
	public String getInteractionsPath() {
		return interactionsFile;
	}
	public void setInteractionsPath(String interactionsPath) {
		this.interactionsFile = interactionsPath;
	}
	public List<ItemParameters> getItems() {
		return items;
	}
	public void setItems(List<ItemParameters> items) {
		this.items = items;
	}
	public int getGlobalNpcID() {
		return globalNpcID;
	}
	public void setGlobalNpcID(int globalNpcID) {
		this.globalNpcID = globalNpcID;
	}
	public String getTexturePath() {
		return texturePath;
	}
	public void setTexturePath(String texturePath) {
		this.texturePath = texturePath;
	}
	public int getPosX() {
		return posX;
	}
	public void setPosX(int posX) {
		this.posX = posX;
	}
	public int getPosY() {
		return posY;
	}
	public void setPosY(int posY) {
		this.posY = posY;
	}
	public boolean isFullBoundingBox() {
		return fullBoundingBox;
	}
	public void setFullBoundingBox(boolean fullBoundingBox) {
		this.fullBoundingBox = fullBoundingBox;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}

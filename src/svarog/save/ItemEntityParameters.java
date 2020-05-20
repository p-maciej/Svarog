package svarog.save;

import java.util.ArrayList;
import java.util.List;

public class ItemEntityParameters {
	private int entityItemTypeID = -1;
	private String texturePath;
	private int posX=0;
	private int posY=0;
	private boolean fullBoundingBox;
	private String name;
	private int respownInSec=-1;
	
	List<ItemParameters> itemParam=new ArrayList<>();
	
	public ItemEntityParameters(int entityItemTypeID, String texturePath, int posX, int posY, boolean fullBoundingBox,
			String name, int respownInSec, List<ItemParameters> itemParam) {
		super();
		this.entityItemTypeID = entityItemTypeID;
		this.texturePath = texturePath;
		this.posX = posX;
		this.posY = posY;
		this.fullBoundingBox = fullBoundingBox;
		this.name = name;
		this.respownInSec = respownInSec;
		this.itemParam = itemParam;
	}

	public int getEntityItemTypeID() {
		return entityItemTypeID;
	}

	public void setEntityItemTypeID(int entityItemTypeID) {
		this.entityItemTypeID = entityItemTypeID;
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

	public int getRespownInSec() {
		return respownInSec;
	}

	public void setRespownInSec(int respownInSec) {
		this.respownInSec = respownInSec;
	}

	public List<ItemParameters> getItemParam() {
		return itemParam;
	}

	public void setItemParam(List<ItemParameters> itemParam) {
		this.itemParam = itemParam;
	}
	
	
}

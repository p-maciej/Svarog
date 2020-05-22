package svarog.save;

import java.util.ArrayList;
import java.util.List;

public class EntityItemParameters {
	private int entityItemTypeID = -1;
	private String texturePath;
	private int posX=0;
	private int posY=0;
	private int scaleX=1;
	private int scaleY=1;
	private boolean fullBoundingBox = true;
	private String name;
	private int respownInSec=-1;
	
	List<ItemParameters> itemParam=new ArrayList<>();
	
	public EntityItemParameters(int entityItemTypeID, String texturePath, boolean fullBoundingBox,
			String name, int respownInSec, List<ItemParameters> itemParam) {
		super();
		this.entityItemTypeID = entityItemTypeID;
		this.texturePath = texturePath;
		this.fullBoundingBox = fullBoundingBox;
		this.name = name;
		this.respownInSec = respownInSec;
		this.itemParam = itemParam;
	}
	
	
	public EntityItemParameters(int entityItemTypeID, String texturePath, int scaleX, int scaleY, boolean fullBoundingBox,
			String name, int respownInSec, List<ItemParameters> itemParam) {
		super();
		this.entityItemTypeID = entityItemTypeID;
		this.texturePath = texturePath;
		this.fullBoundingBox = fullBoundingBox;
		this.name = name;
		this.respownInSec = respownInSec;
		this.itemParam = itemParam;
		this.setScaleX(scaleX);
		this.setScaleY(scaleY);
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

	public int getScaleY() {
		return scaleY;
	}

	public void setScaleY(int scaleY) {
		this.scaleY = scaleY;
	}

	public int getScaleX() {
		return scaleX;
	}

	public void setScaleX(int scaleX) {
		this.scaleX = scaleX;
	}
	
	
}

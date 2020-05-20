package svarog.save;

public class EntityHolder {

	private String type;
	private int id;
	private int typeID;
	private String name;
	private int posX;
	private int posY;
	private int scaleX;
	private int scaleY;
	private boolean isClickable;
	
	
	//npc
	public EntityHolder(String type, int id, int typeID, String name, int posX, int posY, int scaleX, int scaleY,
			boolean isClickable) {
		super();
		this.type = type;
		this.id = id;
		this.typeID = typeID;
		this.name = name;
		this.posX = posX;
		this.posY = posY;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.isClickable = isClickable;
	}
	//enemy/entityItem
	public EntityHolder(String type, int id, int typeID, String name, int posX, int posY) {
		super();
		this.type = type;
		this.id = id;
		this.typeID = typeID;
		this.name = name;
		this.posX = posX;
		this.posY = posY;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTypeID() {
		return typeID;
	}
	public void setTypeID(int typeID) {
		this.typeID = typeID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int getScaleX() {
		return scaleX;
	}
	public void setScaleX(int scaleX) {
		this.scaleX = scaleX;
	}
	public int getScaleY() {
		return scaleY;
	}
	public void setScaleY(int scaleY) {
		this.scaleY = scaleY;
	}
	public boolean isClickable() {
		return isClickable;
	}
	public void setClickable(boolean isClickable) {
		this.isClickable = isClickable;
	}
	
	
}

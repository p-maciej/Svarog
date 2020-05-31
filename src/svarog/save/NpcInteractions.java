package svarog.save;

public class NpcInteractions {
	private String file = "";
	private int isUsed=0;
	private int npcGlobalID=-1;
	
	public NpcInteractions(String file, int isUsed, int npcGlobalID) {
		super();
		this.file = file;
		this.isUsed = isUsed;
		this.npcGlobalID = npcGlobalID;
	}
	
	public NpcInteractions(int isUsed, int npcGlobalID) {
		super();
		this.isUsed = isUsed;
		this.npcGlobalID = npcGlobalID;
	}
	
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public int getNpcGlobalID() {
		return npcGlobalID;
	}
	public void setNpcGlobalID(int npcGlobalID) {
		this.npcGlobalID = npcGlobalID;
	}
	public int getIsUsed() {
		return isUsed;
	}
	public void setIsUsed(int isUsed) {
		this.isUsed = isUsed;
	}
	
}

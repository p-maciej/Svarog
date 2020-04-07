package svarog.interactions;

//TODO
//I will rebuild code here, some of it sucks, but give me 48h from 9:55 07.04.2020 ~JG

public class Task {
	private int taskID;
	private static int globalTaskID;
	
	private String title;
	private String description;
	private int toKill;
	private int toCollect;
	private int killID;
	private int collectID;
	
	public Task() {
		setTaskID(-1);
		setTitle("title");
		setDescription("description");
		setToKill(0);
		setToCollect(0);
		setKillID(-1);
		setCollectID(-1);
		globalTaskID++;
	}
	
	public Task(int taskID, String title, String description, int toKill, int toCollect, int killID, int collectID) {
		this.setTaskID(taskID);
		this.title = title;
		this.description = description;
		this.toKill = toKill;
		this.toCollect = toCollect;
		this.killID = killID;
		this.collectID = collectID;
		globalTaskID++;
	}
	
	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	public int getGlobalTaskID() {
		return globalTaskID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getToKill() {
		return toKill;
	}

	public void setToKill(int toKill) {
		this.toKill = toKill;
	}

	public int getToCollect() {
		return toCollect;
	}

	public void setToCollect(int toCollect) {
		this.toCollect = toCollect;
	}

	public int getKillID() {
		return killID;
	}

	public void setKillID(int killID) {
		this.killID = killID;
	}

	public int getCollectID() {
		return collectID;
	}

	public void setCollectID(int collectID) {
		this.collectID = collectID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}

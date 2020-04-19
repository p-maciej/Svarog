package svarog.interactions;

public class Task {
	
	public enum doState{
		kill,
		collect
	}
	
	private int taskID;
	private static int globalTaskID;
	
	doState state;

	private String title;
	private String description;
	private int toDo;
	private int doItemID;
	
	public Task() {
		setTaskID(-1);
		setTitle("title");
		setDescription("description");
		setToDo(0);
		setDoItemID(0);
		setState(doState.kill);
		globalTaskID++;
	}
	
	public Task(int taskID, String title, String description, int toDo, int doItemID, doState state) {
		this.setTaskID(taskID);
		this.title = title;
		this.description = description;
		this.toDo = toDo;
		this.doItemID = doItemID;
		this.state = state;
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

	public int getToDo() {
		return toDo;
	}

	public void setToDo(int toDo) {
		this.toDo = toDo;
	}

	public int getDoItemID() {
		return doItemID;
	}

	public void setDoItemID(int doItemID) {
		this.doItemID = doItemID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public doState getState() {
		return state;
	}

	public void setState(doState state) {
		this.state = state;
	}
}

package svarog.interactions;

import java.util.ArrayList;
import java.util.List;

public class Quest {
	
	private int questID;
	private List<Task> tasks = new ArrayList<Task>();;
	
	Quest(int questID){
		this.questID = questID;
	}
	
	Quest(int questID, int taskID, String title, String description, int toKill, int toCollect, int killID, int collectID){
		this.questID = questID;
		tasks.add(new Task(taskID, title, description, toKill, toCollect, killID, collectID));
	}
	
	public int getQuestID() {
		return questID;
	}

	public void setQuestID(int questID) {
		this.questID = questID;
	}

	public void addTask(int taskID, String title, String description, int toKill, int toCollect, int killID, int collectID) {
		tasks.add(new Task(taskID, title, description, toKill, toCollect, killID, collectID));
	}
	
	public Task getTaskAt(int i) {
		return tasks.get(i);
	}
	
	public List<Task> getTasks() {
		return this.tasks;
	}
}

package svarog.interactions;

import java.util.ArrayList;
import java.util.List;

import svarog.interactions.Task.doState;

public class Quest {
	
	private int questID;
	private List<Task> tasks;
	
	Quest(int questID){
		this.setQuestID(questID);
		this.setTask(new ArrayList<Task>());
	}
	
	Quest(int questID, List<Task> tasks){
		this.setQuestID(questID);
		this.setTask(tasks);
	}
	
	Quest(int questID, int taskID, String title, String description, int toDo, int doItemID, doState state){
		this.questID = questID;
		tasks.add(new Task(taskID, title, description, toDo, doItemID, state));
	}
	
	public int getQuestID() {
		return questID;
	}

	public void setQuestID(int questID) {
		this.questID = questID;
	}

	public void addTask(int taskID, String title, String description, int toDo, int doItemID, doState state) {
		tasks.add(new Task(taskID, title, description, toDo, doItemID, state));
	}
	
	public void setTask(List<Task>tasks) {
		this.tasks = tasks;
	}
	
	public Task getTaskAt(int i) {
		return tasks.get(i);
	}
	
	public List<Task> getTasks() {
		return this.tasks;
	}
}

package svarog.interactions;


import java.util.ArrayList;
import java.util.List;

public class Quest {

    private int questID;
    private List<Task> tasks;
    private boolean isEndedQuest = false; 

    public boolean isEndedQuest() {
    	setEndedQuest();
		return isEndedQuest;
	}

	private void setEndedQuest() {
		for(Task t: tasks) {
			if(t.getIsEnded()==false) {
				this.isEndedQuest = false;
				return;
			}
		}
		this.isEndedQuest = true;
	}

	private String title;
    private String description;

    public Quest(int questID, String title, String description){
        this.setQuestID(questID);
        this.setTasks(new ArrayList<Task>());
        this.setTitle(title);
        this.setDescription(description);
    }

    public Quest(int questID, String title, String description, List<Task> tasks){
        this.setQuestID(questID);
        this.setTasks(tasks);
        this.setTitle(title);
        this.setDescription(description);
    }

    public int getQuestID() {
        return questID;
    }

    public void setQuestID(int questID) {
        this.questID = questID;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Task getTaskAt(int i) {
        return tasks.get(i);
    }

    public List<Task> getTasks() {
        return this.tasks;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
package svarog.interactions;


import java.util.ArrayList;
import java.util.List;

import svarog.entity.Player;
import svarog.gui.GuiRenderer;
import svarog.objects.Item;
import svarog.save.ItemParameters;
import svarog.save.Save;

public class Quest {

    private int questID;
    private List<Task> tasks;
    private boolean isEndedQuest = false;
    private List<Item> rewardItem = new ArrayList<>();
    private int rewardMoney=0;
    private boolean isRewardedYet = false;

	public boolean isEndedQuest() {
    	setEndedQuest();
		return isEndedQuest;
	}
    
    public void sendReward(Player player, GuiRenderer guiRenderer) {
    	if(!isRewardedYet && isEndedQuest) {
    		for(Item i:rewardItem) {
    			player.addItemToInventoryWithGUIupdate(i, guiRenderer);
    		}
    		player.addMoney(rewardMoney);
    	}
    	isRewardedYet = true;
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
    
    public Quest(int questID, String title, String description, List<Task> tasks, List<ItemParameters> rewardItem, int rewardMoney){
        this.setQuestID(questID);
        this.setTasks(tasks);
        this.setTitle(title);
        this.setDescription(description);
		for(ItemParameters i: rewardItem) {
			this.rewardItem.add(new Item(Save.getItemById(i.getItemGlobalID())));
		}
        this.setRewardMoney(rewardMoney);
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

	public void setEndedQuest(boolean isEndedQuest) {
		this.isEndedQuest = isEndedQuest;
	}

	public int getRewardMoney() {
		return rewardMoney;
	}

	public void setRewardMoney(int rewardMoney) {
		this.rewardMoney = rewardMoney;
	}

	public List<Item> getRewardItem() {
		return rewardItem;
	}

	public void setRewardItem(List<Item> rewardItem) {
		this.rewardItem = rewardItem;
	}
    public boolean isRewardedYet() {
		return isRewardedYet;
	}

	public void setRewardedYet(boolean isRewardedYet) {
		this.isRewardedYet = isRewardedYet;
	}
}
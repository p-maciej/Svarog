package svarog.interactions;


import java.util.ArrayList;
import java.util.List;

import svarog.entity.Player;
import svarog.gui.GuiRenderer;
import svarog.objects.Item;
import svarog.save.ItemParameters;
import svarog.save.NpcInteractions;
import svarog.save.Save;
import svarog.world.World;

public class Quest {

    private int questID;
    private List<Task> tasks;
    private boolean isEndedQuest = false;
    private List<Item> rewardItem = new ArrayList<>();
    private int rewardMoney=0;
    private boolean isRewardedYet = false;
    
    //obs³uga podmiany kolejnego dialogu XDD
    private int idNpc=-1;
    private boolean isLast=true;
    private String nextInteraction;

	public boolean isEndedQuest() {
    	setEndedQuest();
		return isEndedQuest;
	}
    
    public void sendReward(Player player, GuiRenderer guiRenderer, World world) {
    	if(!isRewardedYet && isEndedQuest) {
    		for(Item i:rewardItem) {
    			player.addItemToInventoryWithGUIupdate(i, guiRenderer);
    		}
    		if(!isLast) {
    			world.getNpcByNpcId(idNpc).setInteractions(new Interactions(nextInteraction));
    			Save.addNpcInteractions(new NpcInteractions(nextInteraction, 0, idNpc));
    			//System.out.println("quest");
    			//System.out.println(nextInteraction + " "+ 0 + " "+ idNpc);
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
    
    public Quest(int questID, String title, String description, List<Task> tasks, List<ItemParameters> rewardItem, int rewardMoney, boolean isLast, int idNpc){
        this.setQuestID(questID);
        this.setTasks(tasks);
        this.setTitle(title);
        this.setDescription(description);
		for(ItemParameters i: rewardItem) {
			this.rewardItem.add(new Item(Save.getItemById(i.getItemGlobalID())));
		}
        this.setRewardMoney(rewardMoney);
        this.isLast = isLast;
        this.idNpc = idNpc;
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

	public String getNextInteraction() {
		return nextInteraction;
	}

	public void setNextInteraction(String nextInteraction) {
		this.nextInteraction = nextInteraction;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public int getIdNpc() {
		return idNpc;
	}

	public void setIdNpc(int idNpc) {
		this.idNpc = idNpc;
	}
}
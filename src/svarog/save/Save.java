package svarog.save;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import svarog.entity.Player;
import svarog.game.WorldLoader;
import svarog.interactions.Quest;
import svarog.interactions.Task;
import svarog.interactions.Task.doState;
import svarog.objects.Item;
import svarog.objects.ItemInfo;
import svarog.objects.ItemProperties.ItemType;
import svarog.render.Texture;
import svarog.world.World;

public class Save {
	
	//WARNING IT IS USED ONLY FOR HELP READING PLAYER FROM FILE, IT ISN'T INTENDED TO CHANGING IT EVERY TIME
	private static PlayerParameters playerParam = new PlayerParameters();
	
	public Save(String filename, Player player, World currentWorld) {
		SaveAs(filename, player, currentWorld);
	}
	
	public static void ReadFrom(String filename, Player player) {
		File file;
		try {
	        file = new File("resources/saves/" + filename);
	        Scanner reader = new Scanner(file);
	        //System.out.println("I am here");
			WorldLoader.setNextFrameLoadWorld(Integer.parseInt(reader.nextLine()));
			//System.out.println("I am here");
			//playerParam.setCurrentWorldId(WorldLoader.getNextFrameLoadWorld());
			playerParam.setPositionX(Integer.parseInt(reader.nextLine()));
			playerParam.setPositionY(Integer.parseInt(reader.nextLine()));
			playerParam.setPlayerID(Integer.parseInt(reader.nextLine()));//4
			playerParam.setTexturesPath(reader.nextLine());
			playerParam.setFileName(reader.nextLine());//6
			playerParam.setFullBoundingBox(Boolean.valueOf(reader.nextLine()));
			playerParam.setMovementLocked(Boolean.valueOf(reader.nextLine()));//8
			playerParam.setHP(Integer.parseInt(reader.nextLine()));
			playerParam.setMaxHP(Integer.parseInt(reader.nextLine()));//10
			playerParam.setXp(Integer.parseInt(reader.nextLine()));
			playerParam.setMinAttack(Integer.parseInt(reader.nextLine()));//12
			playerParam.setMaxAttack(Integer.parseInt(reader.nextLine()));
			playerParam.setMoney(Integer.parseInt(reader.nextLine()));//14
			playerParam.setPlayerName(reader.nextLine());
			//System.out.println("I am here");
			List<Item> tempItem = new ArrayList<>();
			//System.out.println(reader.nextLine());
			int iterator = Integer.parseInt(reader.nextLine());//16
			for(int i=0;i<iterator;i++) {
				Item item = new Item(new Texture(reader.nextLine()),new ItemInfo(Integer.parseInt(reader.nextLine()),
						Integer.parseInt(reader.nextLine()), Integer.parseInt(reader.nextLine()), Integer.parseInt(reader.nextLine()),
						Integer.parseInt(reader.nextLine()), reader.nextLine(), reader.nextLine(), ItemType.valueOf(reader.nextLine())));
				item.getItemInfo().setTileID(Integer.parseInt(reader.nextLine()));
				tempItem.add(item);
			}
			playerParam.setItems(tempItem);
			List<Quest> quests = new ArrayList<>();
			iterator = Integer.parseInt(reader.nextLine());
			for(int i=0;i<iterator;i++) {
				List<Task> tasks = new ArrayList<>();
				for(int j=0;j<Integer.parseInt(reader.nextLine());j++) {
					Task task = new Task(Integer.parseInt(reader.nextLine()), Integer.parseInt(reader.nextLine()),
							Integer.parseInt(reader.nextLine()), doState.valueOf(reader.nextLine()));
					task.setEnded(Boolean.valueOf(reader.nextLine()));
					task.setHowMuchIsDone(Integer.parseInt(reader.nextLine()));
					tasks.add(task);
				}
				Quest quest = new Quest(Integer.parseInt(reader.nextLine()),
						reader.nextLine(),
						reader.nextLine(), tasks);
				quest.setEndedQuest(Boolean.valueOf(reader.nextLine()));
				quests.add(quest);
			}
			playerParam.setQuests(quests);
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("BUGS");
			e.printStackTrace();
		}
		
	}
	
	public static void SaveAs(String filename, Player player, World currentWorld) {
        PrintWriter save;
		try {
			save = new PrintWriter("resources/saves/" + filename);
			save.println(currentWorld.getId());
			save.println(player.getPositionX());
			save.println(player.getPositionY());
			save.println(player.getId());//4
			save.println(player.getTexturesPath());
			save.println(player.getFileName());//6
			save.println(player.getFullBoundingBox());
			save.println(player.isMovementLocked());//8
			save.println(player.getHP().GetHP());
			save.println(player.getHP().getMaxHP());//10
			save.println(player.getXP().GetXP());
			save.println(player.getMinAttack());//12
			save.println(player.getMaxAttack());
			save.println(player.getMoney());//14
			save.println(player.getName());
			save.println(player.getInventory().getItems().size());//16
			for(Item item: player.getInventory().getItems()) {
				save.println(item.getTexture().getFilename());
				save.println(item.getItemInfo().getGlobalID());
				save.println(item.getItemInfo().getLocalID());
				save.println(item.getItemInfo().getHpRegeneration());
				save.println(item.getItemInfo().getAttackBonus());
				save.println(item.getItemInfo().getLvlRequired());
				save.println(item.getItemInfo().getName());
				save.println(item.getItemInfo().getDescription());
				save.println(item.getItemInfo().getItemType());
				save.println(item.getItemInfo().getTileID());
			}
			save.println(player.getQuests().size());
			for(Quest q:player.getQuests()) {
				save.println(q.getTasks().size());
				for(Task t:q.getTasks()){
					save.println(t.getTaskID());
					save.println(t.getToDo());
					save.println(t.getDoItemID());
					save.println(t.getState());
					save.println(t.getIsEnded());
					save.println(t.getHowMuchIsDone());
				}
				save.println(q.getQuestID());
				save.println(q.getTitle());
				save.println(q.getDescription());
				save.println(q.isEndedQuest());
			}
			save.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static PlayerParameters getPlayerParam() {
		return playerParam;
	}

	public static void setPlayerParam(PlayerParameters playerParam) {
		Save.playerParam = playerParam;
	}
	
}

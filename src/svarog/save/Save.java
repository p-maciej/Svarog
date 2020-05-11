package svarog.save;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import svarog.entity.Player;
import svarog.game.WorldLoader;
import svarog.interactions.Quest;
import svarog.interactions.Task;
import svarog.objects.Item;
import svarog.world.World;

public class Save {
	
	public Save(String filename, Player player, World currentWorld) {
		SaveAs(filename, player, currentWorld);
	}
	
	public static void ReadFrom(String filename, Player player) {
		
		try {
	        Scanner reader;
	        reader = new Scanner(filename);
	        WorldLoader.setNextFrameLoadWorld(Integer.parseInt(reader.nextLine()));
			player.setPosition(Integer.parseInt(reader.nextLine()),Integer.parseInt(reader.nextLine()));
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public static void SaveAs(String filename, Player player, World currentWorld) {
        PrintWriter save;
		try {
			save = new PrintWriter("resources/saves/" + filename);
			save.println(currentWorld.getId());
			save.println(player.getPositionX());
			save.println(player.getPositionY());
			save.println(player.getTexturesPath());
			save.println(player.getFileName());
			save.println(player.isMovementLocked());
			save.println(player.getHP().GetHP());
			save.println(player.getHP().getMaxHP());
			save.println(player.getXP().GetXP());
			save.println(player.getMinAttack());
			save.println(player.getMaxAttack());
			save.println(player.getMoney());
			save.println(player.getInventory().getItems().size());
			for(Item item: player.getInventory().getItems()) {
				save.println(item.getItemInfo().getTileID());
				save.println(item.getItemInfo().getGlobalID());
				save.println(item.getItemInfo().getLocalID());
				save.println(item.getItemInfo().getHpRegeneration());
				save.println(item.getItemInfo().getAttackBonus());
				save.println(item.getItemInfo().getLvlRequired());
				save.println(item.getItemInfo().getName());
				save.println(item.getItemInfo().getDescription());
				save.println(item.getItemInfo().getItemType());
			}
			save.println(player.getQuests().size());
			for(Quest q:player.getQuests()) {
				save.println(q.getQuestID());
				save.println(q.isEndedQuest());
				save.println(q.getTasks().size());
				for(Task t:q.getTasks()){
					save.println(t.getTaskID());
					save.println(t.getGlobalTaskID());
					save.println(t.getState());
					save.println(t.getHowMuchIsDone());
					save.println(t.getToDo());
					save.println(t.getDoItemID());
					save.println(t.getIsEnded());
				}
			}
			save.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}

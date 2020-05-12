package svarog.save;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	private static List<Item> items = new ArrayList<Item>();
	
	public Save(String filename, Player player, World currentWorld) {
		SaveAs(filename, player, currentWorld);
	}
	
	public static void ReadItems() {
		try {
			File inputFile = new File("resources/items/items");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("item");
			//System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				//System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					Item item;

					item = new Item(new Texture(eElement.getElementsByTagName("TextureFileName").item(0).getTextContent()),
							new ItemInfo(Integer.parseInt(eElement.getElementsByTagName("globalID").item(0).getTextContent()),
									Integer.parseInt(eElement.getElementsByTagName("defense").item(0).getTextContent()),
									Integer.parseInt(eElement.getElementsByTagName("hpRegeneration").item(0).getTextContent()),
									Integer.parseInt(eElement.getElementsByTagName("attackBonus").item(0).getTextContent()),
									Integer.parseInt(eElement.getElementsByTagName("lvlRequired").item(0).getTextContent()),
									eElement.getElementsByTagName("name").item(0).getTextContent(),
									eElement.getElementsByTagName("description").item(0).getTextContent(), 
									ItemType.valueOf(eElement.getElementsByTagName("itemType").item(0).getTextContent())
								));
					items.add(item);
				}
			}
		} catch (Exception e) {
			System.out.println("ReadItems :)");
			e.printStackTrace();
		}
	}
	
	public static void ReadFrom(String filename, Player player) {
		ReadItems();
		File file;
		try {
	        file = new File("resources/saves/" + filename);
	        Scanner reader = new Scanner(file);
			WorldLoader.setNextFrameLoadWorld(Integer.parseInt(reader.nextLine()));
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
			List<Item> tempItem = new ArrayList<>();
			int iterator = Integer.parseInt(reader.nextLine());//16
			for(int i=0;i<iterator;i++) {
				//System.out.println(Integer.parseInt(reader.nextLine()));
				Item item = getItemById(Integer.parseInt(reader.nextLine()));
				item.getItemInfo().setTileID(Integer.parseInt(reader.nextLine()));
				tempItem.add(item);
			}
			playerParam.setItems(tempItem);
			List<Quest> quests = new ArrayList<>();
			iterator = Integer.parseInt(reader.nextLine());
			for(int i=0;i<iterator;i++) {
				List<Task> tasks = new ArrayList<>();
				int inter = Integer.parseInt(reader.nextLine());
				for(int j=0;j<inter;j++) {
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
			System.out.println("BUGS ReadFrom");
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
				save.println(item.getItemInfo().getGlobalID());
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
	
	private static Item getItemById(int id) {
		for(Item elem : items) {
			if(elem.getItemInfo().getGlobalID() == id) 
				return elem;
		}
		return null;
	}
	
}

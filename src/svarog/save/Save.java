package svarog.save;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
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
	private static List<EnemyParameters> enemies = new ArrayList<EnemyParameters>();
	private static List<NpcParameters> npcs = new ArrayList<NpcParameters>();
	private static List<EntityItemParameters> entityItemParam = new ArrayList<>();

	
	public Save(String filename, Player player, World currentWorld) {
		SaveAs(filename, player, currentWorld);
	}
	
	public static void ReadItems() {
		try {
			File inputFile = new File("resources/gameContent/items");
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
									ItemType.valueOf(eElement.getElementsByTagName("itemType").item(0).getTextContent()),
									Integer.parseInt(eElement.getElementsByTagName("prize").item(0).getTextContent())
								));
					items.add(item);
				}
			}
		} catch (Exception e) {
			System.out.println("ReadItems :)");
			e.printStackTrace();
		}
	}
	
	public static void ReadEntityItems() {
		try {
			File inputFile = new File("resources/gameContent/entityItem");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("entityItem");
			//System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				//System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					
					List<ItemParameters> itemParam = new ArrayList<>();
					for(int i=0;i<Integer.parseInt(eElement.getElementsByTagName("howManyItems").item(0).getTextContent());i++) {
						itemParam.add(new ItemParameters(Integer.parseInt(eElement.getElementsByTagName("itemGlobalID").item(i).getTextContent()),
								Integer.parseInt(eElement.getElementsByTagName("itemTileID").item(i).getTextContent())
								));
					}
					
					EntityItemParameters entityItemParameters;

					entityItemParameters = new EntityItemParameters(
							Integer.parseInt(eElement.getElementsByTagName("entityItemTypeID").item(0).getTextContent()),
							eElement.getElementsByTagName("texturePath").item(0).getTextContent(),
							Integer.parseInt(eElement.getElementsByTagName("scaleX").item(0).getTextContent()),
							Integer.parseInt(eElement.getElementsByTagName("scaleY").item(0).getTextContent()),
							Boolean.valueOf(eElement.getElementsByTagName("fullBoundingBox").item(0).getTextContent()),
							eElement.getElementsByTagName("name").item(0).getTextContent(),
							Integer.parseInt(eElement.getElementsByTagName("respownInSec").item(0).getTextContent()),
							itemParam);
					
					entityItemParam.add(entityItemParameters);
				}
			}
		} catch (Exception e) {
			System.out.println("ReadEntityItems :)");
			e.printStackTrace();
		}
	}
	
	public static List<EntityHolder> ReadWorldEntities(String filename) {
		List<EntityHolder> entityHolder = new ArrayList<>();
		try {
			File inputFile = new File("resources/gameContent/"+filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("entity");
			//System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				//System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					
					
					EntityHolder entityHold;
					
					String type = eElement.getElementsByTagName("type").item(0).getTextContent();
					if(type.equals("npc")) {
						entityHold = new EntityHolder(type,
								Integer.parseInt(eElement.getElementsByTagName("id").item(0).getTextContent()),
								Integer.parseInt(eElement.getElementsByTagName("typeID").item(0).getTextContent()),
								eElement.getElementsByTagName("name").item(0).getTextContent(),
								Integer.parseInt(eElement.getElementsByTagName("posX").item(0).getTextContent()),
								Integer.parseInt(eElement.getElementsByTagName("posY").item(0).getTextContent()),
								Integer.parseInt(eElement.getElementsByTagName("scaleX").item(0).getTextContent()),
								Integer.parseInt(eElement.getElementsByTagName("scaleY").item(0).getTextContent()),
								Boolean.valueOf(eElement.getElementsByTagName("isClickable").item(0).getTextContent())
								);
					}else {
						entityHold = new EntityHolder(type,
								Integer.parseInt(eElement.getElementsByTagName("id").item(0).getTextContent()),
								Integer.parseInt(eElement.getElementsByTagName("typeID").item(0).getTextContent()),
								eElement.getElementsByTagName("name").item(0).getTextContent(),
								Integer.parseInt(eElement.getElementsByTagName("posX").item(0).getTextContent()),
								Integer.parseInt(eElement.getElementsByTagName("posY").item(0).getTextContent())
								);
					}

					entityHolder.add(entityHold);
					
				}
			}
		} catch (Exception e) {
			System.out.println("ReadWorldEntities :)");
			e.printStackTrace();
		}
		return entityHolder;
	}
	
	public static void ReadEnemies() {
		try {
			File inputFile = new File("resources/gameContent/enemies");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("enemy");
			//System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				//System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					/////////////////////////////////////////////////////////////////////////////////////////
					//ItemType.valueOf(eElement.getElementsByTagName("itemType").item(0).getTextContent());
					//eElement.getElementsByTagName("description").item(0).getTextContent();
					//Integer.parseInt(eElement.getElementsByTagName("attackBonus").item(0).getTextContent());
					//Boolean.valueOf(eElement.getElementsByTagName("attackBonus").item(0).getTextContent());
					/////////////////////////////////////////////////////////////////////////////////////////
					List<ItemParameters> itemParam = new ArrayList<>();
					for(int i=0;i<Integer.parseInt(eElement.getElementsByTagName("howManyItems").item(0).getTextContent());i++) {
						itemParam.add(new ItemParameters(Integer.parseInt(eElement.getElementsByTagName("itemGlobalID").item(i).getTextContent()),
								Integer.parseInt(eElement.getElementsByTagName("itemTileID").item(i).getTextContent()),
								Integer.parseInt(eElement.getElementsByTagName("itemPropability").item(i).getTextContent())
								));
					}
					EnemyParameters enemyParam = new EnemyParameters(itemParam,
							Integer.parseInt(eElement.getElementsByTagName("globalID").item(0).getTextContent()),
							eElement.getElementsByTagName("texture").item(0).getTextContent(),
							Integer.parseInt(eElement.getElementsByTagName("posX").item(0).getTextContent()),
							Integer.parseInt(eElement.getElementsByTagName("posY").item(0).getTextContent()),
							Boolean.valueOf(eElement.getElementsByTagName("fullBoundingBox").item(0).getTextContent()),
							Integer.parseInt(eElement.getElementsByTagName("minAttack").item(0).getTextContent()),
							Integer.parseInt(eElement.getElementsByTagName("maxAttack").item(0).getTextContent()),
							Integer.parseInt(eElement.getElementsByTagName("xpForKilling").item(0).getTextContent()),
							Integer.parseInt(eElement.getElementsByTagName("hp").item(0).getTextContent()),
							Integer.parseInt(eElement.getElementsByTagName("reward").item(0).getTextContent()),
							Integer.parseInt(eElement.getElementsByTagName("respownInSeconds").item(0).getTextContent()),
							Integer.parseInt(eElement.getElementsByTagName("level").item(0).getTextContent()),
							eElement.getElementsByTagName("name").item(0).getTextContent());


					enemies.add(enemyParam);
				}
			}
		} catch (Exception e) {
			System.out.println("ReadEnemies :)");
			e.printStackTrace();
		}
	}
	
	public static void ReadNpc() {
		try {
			File inputFile = new File("resources/gameContent/NPCs");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("npc");
			//System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				//System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					/////////////////////////////////////////////////////////////////////////////////////////
					//ItemType.valueOf(eElement.getElementsByTagName("itemType").item(0).getTextContent());
					//eElement.getElementsByTagName("description").item(0).getTextContent();
					//Integer.parseInt(eElement.getElementsByTagName("attackBonus").item(0).getTextContent());
					//Boolean.valueOf(eElement.getElementsByTagName("attackBonus").item(0).getTextContent());
					/////////////////////////////////////////////////////////////////////////////////////////
					List<ItemParameters> itemParam = new ArrayList<>();
					for(int i=0;i<Integer.parseInt(eElement.getElementsByTagName("howManyItems").item(0).getTextContent());i++) {
						itemParam.add(new ItemParameters(Integer.parseInt(eElement.getElementsByTagName("itemGlobalID").item(i).getTextContent()),
								Integer.parseInt(eElement.getElementsByTagName("itemTileID").item(i).getTextContent())));
					}
					NpcParameters npcParameters = new NpcParameters(
							Integer.parseInt(eElement.getElementsByTagName("globalID").item(0).getTextContent()),
							eElement.getElementsByTagName("texturePath").item(0).getTextContent(),
							Integer.parseInt(eElement.getElementsByTagName("posX").item(0).getTextContent()),
							Integer.parseInt(eElement.getElementsByTagName("posY").item(0).getTextContent()),
							Boolean.valueOf(eElement.getElementsByTagName("fullBoundingBox").item(0).getTextContent()),
							eElement.getElementsByTagName("name").item(0).getTextContent(),
							eElement.getElementsByTagName("interactionsFile").item(0).getTextContent(),
							itemParam);


					npcs.add(npcParameters);
				}
			}
		} catch (Exception e) {
			System.out.println("ReadNPCs :)");
			e.printStackTrace();
		}
	}
	
	
	public static void ReadFrom(String filename) {
		ReadItems();
		ReadEntityItems();
		ReadEnemies();
		ReadNpc();
		
		try {
			File inputFile = new File("resources/saves/" + filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("save");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);


				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					
					WorldLoader.setNextFrameLoadWorld(Integer.parseInt(eElement.getElementsByTagName("worldID").item(0).getTextContent()));
					playerParam.setPositionX(Integer.parseInt(eElement.getElementsByTagName("getPositionX").item(0).getTextContent()));
					playerParam.setPositionY(Integer.parseInt(eElement.getElementsByTagName("getPositionY").item(0).getTextContent()));
					playerParam.setPlayerID(Integer.parseInt(eElement.getElementsByTagName("getId").item(0).getTextContent()));//4
					playerParam.setTexturesPath(eElement.getElementsByTagName("getTexturesPath").item(0).getTextContent());
					playerParam.setFileName(eElement.getElementsByTagName("getFileName").item(0).getTextContent());//6
					playerParam.setFullBoundingBox(Boolean.valueOf(eElement.getElementsByTagName("getFullBoundingBox").item(0).getTextContent()));
					playerParam.setMovementLocked(Boolean.valueOf(eElement.getElementsByTagName("isMovementLocked").item(0).getTextContent()));//8
					playerParam.setHP(Integer.parseInt(eElement.getElementsByTagName("getHP").item(0).getTextContent()));
					playerParam.setMaxHP(Integer.parseInt(eElement.getElementsByTagName("getMaxHP").item(0).getTextContent()));//10
					playerParam.setXp(Integer.parseInt(eElement.getElementsByTagName("getXP").item(0).getTextContent()));
					playerParam.setMinAttack(Integer.parseInt(eElement.getElementsByTagName("getMinAttack").item(0).getTextContent()));//12
					playerParam.setMaxAttack(Integer.parseInt(eElement.getElementsByTagName("getMaxAttack").item(0).getTextContent()));
					playerParam.setMoney(Integer.parseInt(eElement.getElementsByTagName("getMoney").item(0).getTextContent()));//14
					playerParam.setPlayerName(eElement.getElementsByTagName("getName").item(0).getTextContent());
					List<Item> tempItem = new ArrayList<>();
					int iterator = Integer.parseInt(eElement.getElementsByTagName("howManyItems").item(0).getTextContent());//16
					for(int i=0;i<iterator;i++) {

						int ID = Integer.parseInt(eElement.getElementsByTagName("getGlobalID").item(i).getTextContent());
						Item temporary = getItemById(ID);
						Item item = new Item(new Texture(temporary.getTexture().getFilename()),
								new ItemInfo(temporary.getItemInfo().getGlobalID(),
										temporary.getItemInfo().getDefense(),
										temporary.getItemInfo().getHpRegeneration(),
										temporary.getItemInfo().getAttackBonus(),
										temporary.getItemInfo().getLvlRequired(),
										temporary.getItemInfo().getName(),
										temporary.getItemInfo().getDescription(),
										temporary.getItemInfo().getItemType(),
										temporary.getItemInfo().getPrize()));
						item.getItemInfo().setTileID(Integer.parseInt(eElement.getElementsByTagName("getTileID").item(i).getTextContent()));
						tempItem.add(item);
					}
					playerParam.setItems(tempItem);
					List<Quest> quests = new ArrayList<>();
					/////////////////////////////////////////////////////////////////////////////////////////
					//ItemType.valueOf(eElement.getElementsByTagName("itemType").item(0).getTextContent());
					//eElement.getElementsByTagName("description").item(0).getTextContent();
					//Integer.parseInt(eElement.getElementsByTagName("attackBonus").item(0).getTextContent());
					//Boolean.valueOf(eElement.getElementsByTagName("attackBonus").item(0).getTextContent());
					/////////////////////////////////////////////////////////////////////////////////////////
					iterator = Integer.parseInt(eElement.getElementsByTagName("howManyQuests").item(0).getTextContent());
					for(int i=0;i<iterator;i++) {
						List<Task> tasks = new ArrayList<>();
						int inter = Integer.parseInt(eElement.getElementsByTagName("howManyTasks").item(0).getTextContent());
						for(int j=0;j<inter;j++) {
							Task task = new Task(Integer.parseInt(eElement.getElementsByTagName("getTaskID").item(0).getTextContent()),
									Integer.parseInt(eElement.getElementsByTagName("getToDo").item(0).getTextContent()),
									Integer.parseInt(eElement.getElementsByTagName("getDoItemID").item(0).getTextContent()),
									doState.valueOf(eElement.getElementsByTagName("getState").item(0).getTextContent())
									);
							task.setEnded(Boolean.valueOf(eElement.getElementsByTagName("getIsEnded").item(0).getTextContent()));
							task.setHowMuchIsDone(Integer.parseInt(eElement.getElementsByTagName("getHowMuchIsDone").item(0).getTextContent()));
							tasks.add(task);
						}
						Quest quest = new Quest(Integer.parseInt(eElement.getElementsByTagName("getQuestID").item(0).getTextContent()),
								eElement.getElementsByTagName("getTitle").item(0).getTextContent(),
								eElement.getElementsByTagName("getDescription").item(0).getTextContent(),
								tasks);
						quest.setEndedQuest(Boolean.valueOf(eElement.getElementsByTagName("isEndedQuest").item(0).getTextContent()));
						quests.add(quest);
					}
					playerParam.setQuests(quests);

				}
			}
		} catch (Exception e) {
			System.out.println("ReadFromFile :)");
			e.printStackTrace();
		}
	}
	
	public static void SaveAs(String filename, Player player, World currentWorld) {
		
		 try {
			 
	            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
	 
	            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
	 
	            Document document = documentBuilder.newDocument();
	 
	            // root element
	            Element root = document.createElement("class");
	            document.appendChild(root);
	 
	            // employee element
	            Element save = document.createElement("save");
	 
	            root.appendChild(save);
	 
	            // set an attribute to staff element
	            Attr attr = document.createAttribute("id");
	            attr.setValue("0");
	            save.setAttributeNode(attr);
	 
	            //you can also use staff.setAttribute("id", "1") for this
	 
	            Element worldID = document.createElement("worldID");
	            worldID.appendChild(document.createTextNode(Integer.toString(currentWorld.getId())));
	            save.appendChild(worldID);
	 
	            Element getPositionX = document.createElement("getPositionX");
	            getPositionX.appendChild(document.createTextNode(Integer.toString(player.getPositionX())));
	            save.appendChild(getPositionX);
	 
	            Element getPositionY = document.createElement("getPositionY");
	            getPositionY.appendChild(document.createTextNode(Integer.toString(player.getPositionY())));
	            save.appendChild(getPositionY);
	 
	            Element getId = document.createElement("getId");
	            getId.appendChild(document.createTextNode(Integer.toString(player.getId())));
	            save.appendChild(getId);
	            
	            Element getTexturesPath = document.createElement("getTexturesPath");
	            getTexturesPath.appendChild(document.createTextNode(player.getTexturesPath()));
	            save.appendChild(getTexturesPath);
	            
	            Element getFileName = document.createElement("getFileName");
	            getFileName.appendChild(document.createTextNode(player.getFileName()));
	            save.appendChild(getFileName);
	            
	            Element getFullBoundingBox = document.createElement("getFullBoundingBox");
	            getFullBoundingBox.appendChild(document.createTextNode(Boolean.toString(player.getFullBoundingBox())));
	            save.appendChild(getFullBoundingBox);
	            
	            Element isMovementLocked = document.createElement("isMovementLocked");
	            isMovementLocked.appendChild(document.createTextNode(Boolean.toString(player.isMovementLocked())));
	            save.appendChild(isMovementLocked);
	            
	            Element getHP = document.createElement("getHP");
	            getHP.appendChild(document.createTextNode(Integer.toString(player.getHP().GetHP())));
	            save.appendChild(getHP);
	            
	            Element getMaxHP = document.createElement("getMaxHP");
	            getMaxHP.appendChild(document.createTextNode(Integer.toString(player.getHP().getMaxHP())));
	            save.appendChild(getMaxHP);
	            
	            Element getXP = document.createElement("getXP");
	            getXP.appendChild(document.createTextNode(Integer.toString(player.getXP().GetXP())));
	            save.appendChild(getXP);
	            
	            Element getMinAttack = document.createElement("getMinAttack");
	            getMinAttack.appendChild(document.createTextNode(Integer.toString(player.getMinAttack())));
	            save.appendChild(getMinAttack);
	            
	            Element getMaxAttack = document.createElement("getMaxAttack");
	            getMaxAttack.appendChild(document.createTextNode(Integer.toString(player.getMaxAttack())));
	            save.appendChild(getMaxAttack);
	            
	            Element getMoney = document.createElement("getMoney");
	            getMoney.appendChild(document.createTextNode(Integer.toString(player.getMoney())));
	            save.appendChild(getMoney);
	            
	            Element getName = document.createElement("getName");
	            getName.appendChild(document.createTextNode(player.getName()));
	            save.appendChild(getName);
	            
	            Element howManyItems = document.createElement("howManyItems");
	            howManyItems.appendChild(document.createTextNode(Integer.toString(player.getInventory().getItems().size())));
	            save.appendChild(howManyItems);
	            
				for(Item item: player.getInventory().getItems()) {
		            Element getGlobalID = document.createElement("getGlobalID");
		            getGlobalID.appendChild(document.createTextNode(Integer.toString(item.getItemInfo().getGlobalID())));
		            save.appendChild(getGlobalID);
		            
		            Element getTileID = document.createElement("getTileID");
		            getTileID.appendChild(document.createTextNode(Integer.toString(item.getItemInfo().getTileID())));
		            save.appendChild(getTileID);
				}

	            Element howManyQuests = document.createElement("howManyQuests");
	            howManyQuests.appendChild(document.createTextNode(Integer.toString(player.getQuests().size())));
	            save.appendChild(howManyQuests);

				for(Quest q:player.getQuests()) {
					Element howManyTasks = document.createElement("howManyTasks");
					howManyTasks.appendChild(document.createTextNode(Integer.toString(q.getTasks().size())));
		            save.appendChild(howManyTasks);

					for(Task t:q.getTasks()){
			            Element getTaskID = document.createElement("getTaskID");
			            getTaskID.appendChild(document.createTextNode(Integer.toString(t.getTaskID())));
			            save.appendChild(getTaskID);

			            Element getToDo = document.createElement("getToDo");
			            getToDo.appendChild(document.createTextNode(Integer.toString(t.getToDo())));
			            save.appendChild(getToDo);

			            Element getDoItemID = document.createElement("getDoItemID");
			            getDoItemID.appendChild(document.createTextNode(Integer.toString(t.getDoItemID())));
			            save.appendChild(getDoItemID);

			            Element getState = document.createElement("getState");
			            getState.appendChild(document.createTextNode(t.getState().toString()));
			            save.appendChild(getState);

			            Element getIsEnded = document.createElement("getIsEnded");
			            getIsEnded.appendChild(document.createTextNode(Boolean.toString(t.getIsEnded())));
			            save.appendChild(getIsEnded);

			            Element getHowMuchIsDone = document.createElement("getHowMuchIsDone");
			            getHowMuchIsDone.appendChild(document.createTextNode(Integer.toString(t.getHowMuchIsDone())));
			            save.appendChild(getHowMuchIsDone);

					}
					Element getQuestID = document.createElement("getQuestID");
					getQuestID.appendChild(document.createTextNode(Integer.toString(q.getQuestID())));
		            save.appendChild(getQuestID);

					Element getTitle = document.createElement("getTitle");
					getTitle.appendChild(document.createTextNode(q.getTitle()));
		            save.appendChild(getTitle);

					Element getDescription = document.createElement("getDescription");
					getDescription.appendChild(document.createTextNode(q.getDescription()));
		            save.appendChild(getDescription);

					Element isEndedQuest = document.createElement("isEndedQuest");
					isEndedQuest.appendChild(document.createTextNode(Boolean.toString(q.isEndedQuest())));
		            save.appendChild(isEndedQuest);
				}

	            // create the xml file
	            //transform the DOM Object to an XML File
	            TransformerFactory transformerFactory = TransformerFactory.newInstance();
	            Transformer transformer = transformerFactory.newTransformer();
	            DOMSource domSource = new DOMSource(document);
	            StreamResult streamResult = new StreamResult(new File("resources/saves/" + filename));
	 
	            // If you use
	            // StreamResult result = new StreamResult(System.out);
	            // the output will be pushed to the standard output ...
	            // You can use that for debugging 
	            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	            transformer.transform(domSource, streamResult);
	 
	            //System.out.println("Done creating XML File");
	 
	        } catch (ParserConfigurationException pce) {
	            pce.printStackTrace();
	        } catch (TransformerException tfe) {
	            tfe.printStackTrace();
	        }

	}

	public static PlayerParameters getPlayerParam() {
		return playerParam;
	}

	public static void setPlayerParam(PlayerParameters playerParam) {
		Save.playerParam = playerParam;
	}
	
	public static Item getItemById(int id) {
		for(Item elem : items) {
			if(elem.getItemInfo().getGlobalID() == id) 
				return elem;
		}
		return null;
	}
	
	public static EnemyParameters getEnemyById(int id) {
		for(EnemyParameters elem : enemies) {
			if(elem.getGlobalEnemyID() == id) 
				return elem;
		}
		return null;
	}
	
	public static NpcParameters getNpcsByID(int id) {
		for(NpcParameters elem : npcs) {
			if(elem.getGlobalNpcID() == id) 
				return elem;
		}
		return null;
	}
	public static EntityItemParameters getEntityItemParameters(int id) {
		for(EntityItemParameters elem : entityItemParam) {
			if(elem.getEntityItemTypeID() == id)
				return elem;
		}
		return null;
	}
	
	public static List<EnemyParameters> getEnemies() {
		return enemies;
	}

	public static void setEnemies(List<EnemyParameters> enemies) {
		Save.enemies = enemies;
	}

	public static List<NpcParameters> getNpcs() {
		return npcs;
	}

	public static void setNpcs(List<NpcParameters> npcs) {
		Save.npcs = npcs;
	}
	
	public static List<Item> getItems() {
		return items;
	}

	public static void setItems(List<Item> items) {
		Save.items = items;
	}

	public static List<EntityItemParameters> getEntityItemParam() {
		return entityItemParam;
	}

	public static void setEntityItemParam(List<EntityItemParameters> entityItemParam) {
		Save.entityItemParam = entityItemParam;
	}

}

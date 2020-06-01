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

import svarog.entity.Entity;
import svarog.entity.NPC;
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
import svarog.world.World.EntityRespawn;

public class Save {
	
	//WARNING IT IS USED ONLY FOR HELP READING PLAYER FROM FILE, IT ISN'T INTENDED TO CHANGING IT EVERY TIME
	private static PlayerParameters playerParam = new PlayerParameters();
	private static List<Item> items = new ArrayList<Item>();
	private static List<EnemyParameters> enemies = new ArrayList<EnemyParameters>();
	private static List<NpcParameters> npcs = new ArrayList<NpcParameters>();
	private static List<EntityItemParameters> entityItemParam = new ArrayList<>();
	private static List<NpcInteractions> npcInteractions = new ArrayList<>();
	
	private static int isOldGame = 1;
	
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
					//playerParam.setPlayerID(Integer.parseInt(eElement.getElementsByTagName("getId").item(0).getTextContent()));//4
					//playerParam.setTexturesPath(eElement.getElementsByTagName("getTexturesPath").item(0).getTextContent());
					//playerParam.setFileName(eElement.getElementsByTagName("getFileName").item(0).getTextContent());//6
					//playerParam.setFullBoundingBox(Boolean.valueOf(eElement.getElementsByTagName("getFullBoundingBox").item(0).getTextContent()));
					//playerParam.setMovementLocked(Boolean.valueOf(eElement.getElementsByTagName("isMovementLocked").item(0).getTextContent()));//8
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
					int iterator2 = Integer.parseInt(eElement.getElementsByTagName("howManyQuests").item(0).getTextContent());
					int taskKeeper=0;
					System.out.println(iterator2);
					for(int i=0;i<iterator2;i++) {
						List<Task> tasks = new ArrayList<>();
						int inter = Integer.parseInt(eElement.getElementsByTagName("howManyTasks").item(i).getTextContent());
						
						for(int j=0;j<inter;j++) {
							System.out.println(inter + " " + taskKeeper);
							Task task = new Task(Integer.parseInt(eElement.getElementsByTagName("getTaskID").item(taskKeeper).getTextContent()),
									Integer.parseInt(eElement.getElementsByTagName("getToDo").item(taskKeeper).getTextContent()),
									Integer.parseInt(eElement.getElementsByTagName("getDoItemID").item(taskKeeper).getTextContent()),
									doState.valueOf(eElement.getElementsByTagName("getState").item(taskKeeper).getTextContent())
									);
							task.setEnded(Boolean.valueOf(eElement.getElementsByTagName("getIsEnded").item(taskKeeper).getTextContent()));
							task.setHowMuchIsDone(Integer.parseInt(eElement.getElementsByTagName("getHowMuchIsDone").item(taskKeeper).getTextContent()));
							tasks.add(task);
							taskKeeper++;
						}
						List<ItemParameters> itemQuests = new ArrayList<ItemParameters>();
						int ite = Integer.parseInt(eElement.getElementsByTagName("rewardHowManyItems").item(i).getTextContent());
						for(int j =0; j< ite;j++) {
							itemQuests.add(new ItemParameters(Integer.parseInt(eElement.getElementsByTagName("itemGlobalID").item(j).getTextContent()),
    								Integer.parseInt(eElement.getElementsByTagName("itemTileID").item(j).getTextContent())
    								));
                        }
						boolean isLast = Boolean.parseBoolean(eElement.getElementsByTagName("isLast").item(i).getTextContent());
						Quest quest = new Quest(Integer.parseInt(eElement.getElementsByTagName("getQuestID").item(i).getTextContent()),
								eElement.getElementsByTagName("getTitle").item(i).getTextContent(),
								eElement.getElementsByTagName("getDescription").item(i).getTextContent(),
								tasks,
								itemQuests,
								Integer.parseInt(eElement.getElementsByTagName("rewardMoney").item(i).getTextContent()),
								isLast,
								Integer.parseInt(eElement.getElementsByTagName("idNpc").item(i).getTextContent()));
						if(!isLast) {
							//System.out.println(isLast);
							String abc = eElement.getElementsByTagName("nextInteraction").item(0).getTextContent();
							//System.out.println(abc);
                        	quest.setNextInteraction(abc);
                        }
						quest.setEndedQuest(Boolean.valueOf(eElement.getElementsByTagName("isEndedQuest").item(i).getTextContent()));
						quests.add(quest);
					}
					playerParam.setQuests(quests);
					int tempowyInt = Integer.parseInt(eElement.getElementsByTagName("numberOfInteractionsGone").item(0).getTextContent());
					for(int h=0;h<tempowyInt;h++) {
						NpcInteractions npcInteraction = new NpcInteractions(
								eElement.getElementsByTagName("file").item(0).getTextContent(),
								Integer.parseInt(eElement.getElementsByTagName("isUsed").item(0).getTextContent()),
								Integer.parseInt(eElement.getElementsByTagName("npcGlobalID").item(0).getTextContent()));
						npcInteractions.add(npcInteraction);
					}

					
				}
			}
		} catch (Exception e) {
			System.out.println("ReadFromFile :)");
			e.printStackTrace();
		}
	}
	
	public static void ResetEntityRespowns() {
		//NUMBER OF WORLDS GOES HERE
		for(int i=1;i<=7;i++) {
			try {
				 
	            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
	 
	            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
	 
	            Document document = documentBuilder.newDocument();
	 
	            // root element
	            Element root = document.createElement("class");
	            document.appendChild(root);
	 
	            // create the xml file
	            //transform the DOM Object to an XML File
	            TransformerFactory transformerFactory = TransformerFactory.newInstance();
	            Transformer transformer = transformerFactory.newTransformer();
	            DOMSource domSource = new DOMSource(document);
	            StreamResult streamResult = new StreamResult(new File("resources/saves/entitiesRespown" + Integer.toString(i)));
	 
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
	}
	
	public static List<EntityRespawn> ReadEntityRespown(int worldID, List<Entity> entities, World currentWorld) {
		List<EntityRespawn> entityRespawn = new ArrayList<>();
		if(isOldGame==1) {
			try {
				File inputFile = new File("resources/saves/entitiesRespown"+worldID);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(inputFile);
				doc.getDocumentElement().normalize();
				//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
				NodeList nList = doc.getElementsByTagName("entitiesRespown");
				//System.out.println("----------------------------");
	
				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);
					//System.out.println("\nCurrent Element :" + nNode.getNodeName());
	
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						
						int index = Integer.parseInt(eElement.getElementsByTagName("getID").item(0).getTextContent());
						long timerStart = Long.parseLong(eElement.getElementsByTagName("getTimerStart").item(0).getTextContent());
						
						for(Entity i:entities) {
							if(i.getId()==index) {
								entityRespawn.add(currentWorld.new EntityRespawn((Entity)i, timerStart));
								break;
							}
						}
					}
				}
			} catch (Exception e) {
				System.out.println("ReadEntityRespown :)");
				e.printStackTrace();
			}
		}
		setIsOldGame(1);
		return entityRespawn;
	}
	
	public static void SaveWorldEntityRespown(World currentWorld) {
		try {
			 
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
 
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
 
            Document document = documentBuilder.newDocument();
 
            // root element
            Element root = document.createElement("class");
            document.appendChild(root);
 
            for(int i=0;i<currentWorld.getEntitiesToRespawn().size();i++) {
	            // employee element
	            Element save = document.createElement("entitiesRespown");
	 
	            root.appendChild(save);
	 
	            // set an attribute to staff element
	            Attr attr = document.createAttribute("id");
	            attr.setValue(Integer.toString(i));
	            save.setAttributeNode(attr);
	 
	            //you can also use staff.setAttribute("id", "1") for this
	 
	            //Element worldID = document.createElement("worldID");
	            //worldID.appendChild(document.createTextNode(Integer.toString(currentWorld.getId())));
	            //save.appendChild(worldID);
	 
	            
	            Element getPositionX = document.createElement("getID");
	            getPositionX.appendChild(document.createTextNode(Integer.toString(currentWorld.getEntitiesToRespawn().get(i).getEntity().getId())));
	            save.appendChild(getPositionX);
	 
	            Element getPositionY = document.createElement("getTimerStart");
	            getPositionY.appendChild(document.createTextNode(Long.toString(currentWorld.getEntitiesToRespawn().get(i).getTimerStart())));
	            save.appendChild(getPositionY);
            
            }
            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("resources/saves/entitiesRespown" + currentWorld.getId()));
 
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
	            getPositionY.appendChild(document.createTextNode(Integer.toString(player.getPositionY()+1)));
	            save.appendChild(getPositionY);
	 
//	            Element getId = document.createElement("getId");
//	            getId.appendChild(document.createTextNode(Integer.toString(player.getId())));
//	            save.appendChild(getId);
//	            
//	            Element getTexturesPath = document.createElement("getTexturesPath");
//	            getTexturesPath.appendChild(document.createTextNode(player.getTexturesPath()));
//	            save.appendChild(getTexturesPath);
//	            
//	            Element getFileName = document.createElement("getFileName");
//	            getFileName.appendChild(document.createTextNode(player.getFileName()));
//	            save.appendChild(getFileName);
//	            
//	            Element getFullBoundingBox = document.createElement("getFullBoundingBox");
//	            getFullBoundingBox.appendChild(document.createTextNode(Boolean.toString(player.getFullBoundingBox())));
//	            save.appendChild(getFullBoundingBox);
//	            
//	            Element isMovementLocked = document.createElement("isMovementLocked");
//	            isMovementLocked.appendChild(document.createTextNode(Boolean.toString(player.isMovementLocked())));
//	            save.appendChild(isMovementLocked);
	            
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
					
					Element rewardMoney = document.createElement("rewardMoney");
					rewardMoney.appendChild(document.createTextNode(Integer.toString(q.getRewardMoney())));
		            save.appendChild(rewardMoney);
		            
					Element rewardHowManyItems = document.createElement("rewardHowManyItems");
					rewardHowManyItems.appendChild(document.createTextNode(Integer.toString(q.getRewardItem().size())));
		            save.appendChild(rewardHowManyItems);
		            
		            for(Item i : q.getRewardItem()) {
		            	Element itemGlobalID = document.createElement("itemGlobalID");
		            	itemGlobalID.appendChild(document.createTextNode(Integer.toString(i.getItemInfo().getGlobalID())));
			            save.appendChild(itemGlobalID);
			            
		            	Element itemTileID = document.createElement("itemTileID");
		            	itemTileID.appendChild(document.createTextNode(Integer.toString(-1)));
			            save.appendChild(itemTileID);
		            }
					
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
		            
					Element idNpc = document.createElement("idNpc");
					idNpc.appendChild(document.createTextNode(Integer.toString(q.getIdNpc())));
		            save.appendChild(idNpc);
		            
					Element isLast = document.createElement("isLast");
					isLast.appendChild(document.createTextNode(Boolean.toString(q.isLast())));
		            save.appendChild(isLast);

		            if(!q.isLast()) {
		            	Element nextInteraction = document.createElement("nextInteraction");
		            	nextInteraction.appendChild(document.createTextNode(q.getNextInteraction()));
			            save.appendChild(nextInteraction);
		            }
		            
					Element numberOfInteractionsGone = document.createElement("numberOfInteractionsGone");
					numberOfInteractionsGone.appendChild(document.createTextNode(Integer.toString(npcInteractions.size())));
		            save.appendChild(numberOfInteractionsGone);
		            
		            //System.out.println(Integer.toString(npcInteractions.size()));
		            
				}
	            for(int i =0;i<npcInteractions.size();i++) {
					Element npcGlobalID = document.createElement("npcGlobalID");
					npcGlobalID.appendChild(document.createTextNode(Integer.toString(npcInteractions.get(i).getNpcGlobalID())));
		            save.appendChild(npcGlobalID);
		            
					Element isUsed = document.createElement("isUsed");
					isUsed.appendChild(document.createTextNode(Integer.toString(npcInteractions.get(i).getIsUsed())));
		            save.appendChild(isUsed);
		            
					Element file = document.createElement("file");
					file.appendChild(document.createTextNode(npcInteractions.get(i).getFile()));
		            save.appendChild(file);
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
	
	public static void addNpcInteractions(NpcInteractions npcInteract) {
		int temp = 0;
		//System.out.println("Hello there");
		for(NpcInteractions i: npcInteractions) {
			if(i.getNpcGlobalID()==npcInteract.getNpcGlobalID()) {
				i.setIsUsed(npcInteract.getIsUsed());
				if(i.getIsUsed()==0) {
					i.setFile(npcInteract.getFile());
					temp++;
					break;
				}

			}
		}
		if(temp==0) {
			if(npcInteract.getIsUsed()==0) {
				npcInteractions.add(new NpcInteractions(
						npcInteract.getFile(),
						npcInteract.getIsUsed(),
						npcInteract.getNpcGlobalID()));
			}else {
				npcInteractions.add(new NpcInteractions(
						npcInteract.getIsUsed(),
						npcInteract.getNpcGlobalID()));
			}
		}
		//System.out.println(temp);
	}
	
	public static void UpdateInteractions(ArrayList<NPC> NPCs) {
		for(NPC npc : NPCs) {
			for(NpcInteractions npcInter: npcInteractions) {
				if(npc.getGlobalNpcID()==npcInter.getNpcGlobalID()) {
					npc.getInteractions().setIsUsed(npcInter.getIsUsed());
					npc.getInteractions().setFile(npcInter.getFile());
				}
			}
		}
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

	public static List<NpcInteractions> getNpcInteractions() {
		return npcInteractions;
	}

	public static void setNpcInteractions(List<NpcInteractions> npcInteractions) {
		Save.npcInteractions = npcInteractions;
	}

	public static int getIsOldGame() {
		return isOldGame;
	}

	public static void setIsOldGame(int isOldGame) {
		Save.isOldGame = isOldGame;
	}
}

package svarog.interactions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import svarog.entity.Player;
import svarog.game.Main;
import svarog.gui.Answer;
import svarog.gui.Dialog;

import svarog.gui.GuiRenderer;
import svarog.interactions.Task.doState;
import svarog.io.Window;
import svarog.render.Camera;
import svarog.world.WorldRenderer;

public class Interactions {
	private List<Dialog> dialogs = new ArrayList<>();
	private List<Quest> quests = new ArrayList<>();
	private Dialog dialog;
	private boolean isEnded = true;
	
	public boolean isEnded() {
		return isEnded;
	}

	public void setEnded(boolean isEnded) {
		this.isEnded = isEnded;
	}

	private static final String path = "resources/quests/";
	
	public Interactions(String file) {
		Reader(file);
	}
	
	public void setNew(String file) {
		dialogs.clear();
		quests.clear();
		Reader(file);
	}
	
	public void Reader(String file) {
		try {
			File inputFile = new File(path + file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("dialog");
			//System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				//System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					ArrayList<Answer> answers = new ArrayList<>();
					ArrayList<Task> tasks = new ArrayList<>();

					for(int i = 0; i < Integer.parseInt(eElement.getElementsByTagName("ans").item(0).getTextContent());i++){
						answers.add(new Answer(Integer.parseInt(eElement.getElementsByTagName("id").item(i).getTextContent()),
								eElement.getElementsByTagName("answer").item(i).getTextContent(),
								Integer.parseInt(eElement.getElementsByTagName("leadsTo").item(i).getTextContent())));
					}
                    if( Integer.parseInt(eElement.getElementsByTagName("q").item(0).getTextContent())!=0) {
                        for(int i =0; i< Integer.parseInt(eElement.getElementsByTagName("t").item(0).getTextContent());i++) {
                            tasks.add(new Task(Integer.parseInt(eElement.getElementsByTagName("taskID").item(i).getTextContent()),
                                    Integer.parseInt(eElement.getElementsByTagName("toDo").item(i).getTextContent()),
                                    Integer.parseInt(eElement.getElementsByTagName("doItemID").item(i).getTextContent()),
                                    Task.doState.valueOf(eElement.getElementsByTagName("state").item(i).getTextContent())
                            ));
                        }
                    }

                    if(eElement.getElementsByTagName("questID").item(0) != null) {
                        quests.add(new Quest(Integer.parseInt(eElement.getElementsByTagName("questID").item(0).getTextContent()),
                                eElement.getElementsByTagName("title").item(0).getTextContent(),
                                eElement.getElementsByTagName("description").item(0).getTextContent(),
                                tasks));
                    }
					
                    if(eElement.getElementsByTagName("questID").item(0) != null) {
                    	dialogs.add(new Dialog(Integer.parseInt(eElement.getAttribute("id")),
							eElement.getElementsByTagName("content").item(0).getTextContent(),
							answers,
							Integer.parseInt(eElement.getElementsByTagName("questID").item(0).getTextContent())));
                    }else {
                    	dialogs.add(new Dialog(Integer.parseInt(eElement.getAttribute("id")),
							eElement.getElementsByTagName("content").item(0).getTextContent(),
							answers));
                    }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void ChceckInteractions(WorldRenderer currentWorld, Camera camera, Window window, GuiRenderer guiRenderer, Player player, int NPCid) {
		if(isEnded || dialog == null) {
			dialog = dialogs.get(0);
			isEnded = false;
		}
		for(int i=0; i < currentWorld.getWorld().numberOfEntities() - 1 ; i++) {
			if(/* currentWorld.isOverEntity(currentWorld.getWorld().getEntity(i), camera, window) && */ window.getInput().isMouseButtonPressed(0)) {
				if(/*currentWorld.getWorld().getEntity(i) instanceof NPC && currentWorld.getWorld().getEntity(i).getId() == 6 &&*/ !guiRenderer.isDialogOpen()) {
					guiRenderer.showDialog(dialog);
				}
			}
		}
		
		if(dialog.clickedAnswer() != null) {
			interactionsHelper(currentWorld, guiRenderer, player, NPCid);
		}

	}
	public void interactionsHelper(WorldRenderer currentWorld, GuiRenderer guiRenderer, Player player, int NPCid) {
		for(int i = 0; i < dialog.getAnswers().size();i++) {
			if(dialog.clickedAnswer() != null) {
				if(dialog.clickedAnswer().getId() == i) {
					
					if(dialog.clickedAnswer().getLeadsTo() == -1) {
						isEnded = true;
						for(Quest q1:player.getQuests()) {
							for(Task t1:q1.getTasks()) {
								if(t1.getState() == doState.talk) {
									if(t1.getDoItemID() == NPCid) {
										t1.increaseHowMuchIsDone();
									}
								}
							}
						}
						guiRenderer.closeDialog();
						Main.talkingNPCid = -1;
						break;
					}
					guiRenderer.closeDialog();
					dialog = new Dialog(dialogs.get(dialog.clickedAnswer().getLeadsTo()).getId(),
							dialogs.get(dialog.clickedAnswer().getLeadsTo()).getContent(),
							dialogs.get(dialog.clickedAnswer().getLeadsTo()).getAnswers(),
							dialogs.get(dialog.clickedAnswer().getLeadsTo()).getQuestID()
							);
					guiRenderer.showDialog(dialog);
					if(dialog.getQuestID()!=-1) {
						player.addNewQuestNoRepeating(getQuestByID(dialog.getQuestID()));
					}
				}
			}
		}
	}

	
    public List<Quest> getQuests() {
        return quests;
    }
    
    public Quest getQuestByID(int id) {
    	for(Quest i :quests) {
    		if(i.getQuestID() == id) {
    			return i;
    		}
    	}
    		return null;
    }
	
	public Dialog getDialogAt(int i) {
		return dialogs.get(i);
	}
	
	public Quest getQuestAt(int i) {
		return quests.get(i);
	}
	
	
}

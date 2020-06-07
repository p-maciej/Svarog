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
import svarog.gui.Answer;
import svarog.gui.Dialog;

import svarog.gui.GuiRenderer;
import svarog.interactions.Task.doState;
import svarog.io.Window;
import svarog.language.LanguageLoader;
import svarog.render.Camera;
import svarog.save.ItemParameters;
import svarog.save.NpcInteractions;
import svarog.save.Save;
import svarog.world.WorldRenderer;

public class Interactions {
	private List<Dialog> dialogs = new ArrayList<>();
	public List<Dialog> getDialogs() {
		return dialogs;
	}

	private List<Quest> quests = new ArrayList<>();
	private Dialog dialog;
	private boolean isEnded = true;
	private int isTrade;
	//nowe elementy dla aktualnego stanu Interactions
	private String file;
	private int isUsed=1; //czy nie zosta³ u¿yty
	private int isQuestSend=0; //local variable to set isUsed
	
	private static int talkingNPCid = -1;
	
	public boolean isEnded() {
		return isEnded;
	}

	public void setEnded(boolean isEnded) {
		this.isEnded = isEnded;
	}

	private static final String path = "resources/quests/";
	
	public Interactions() {
		isUsed=1;
	}
	
	public Interactions(String file) {
		this.file = file;
		Reader(file);
		isUsed=0;
	}
	
	public void clearInteractions() {
		//dialogs.clear();
		quests.clear();
	}
	
	public void setNew(String file) {
		clearInteractions();
		this.file = file;
		isUsed=0;
		Reader(file);
	}
	
	public void Reader(String file) {
		this.dialogs.clear();
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
					ArrayList<ItemParameters> itemParameters = new ArrayList<ItemParameters>();

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
                        for(int i =0; i< Integer.parseInt(eElement.getElementsByTagName("rewardHowManyItems").item(0).getTextContent());i++) {
                        	itemParameters.add(new ItemParameters(Integer.parseInt(eElement.getElementsByTagName("itemGlobalID").item(i).getTextContent()),
    								Integer.parseInt(eElement.getElementsByTagName("itemTileID").item(i).getTextContent())
    								));
                        }
                    }

                    if(eElement.getElementsByTagName("questID").item(0) != null) {
                    	boolean isLast = Boolean.parseBoolean(eElement.getElementsByTagName("isLast").item(0).getTextContent());
                        quests.add(new Quest(Integer.parseInt(eElement.getElementsByTagName("questID").item(0).getTextContent()),
                                eElement.getElementsByTagName("title").item(0).getTextContent(),
                                eElement.getElementsByTagName("description").item(0).getTextContent(),
                                tasks,
                                itemParameters,
                                Integer.parseInt(eElement.getElementsByTagName("rewardMoney").item(0).getTextContent()),
                                isLast,
                                Integer.parseInt(eElement.getElementsByTagName("idNpc").item(0).getTextContent())));
                        if(!isLast) {
                        	quests.get(quests.size()-1).setNextInteraction(eElement.getElementsByTagName("nextInteraction").item(0).getTextContent());
                        }
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
	
	public void ChceckInteractions(WorldRenderer currentWorld, Camera camera, Window window, GuiRenderer guiRenderer, Player player, int NPCid, LanguageLoader language) {
		
		if((isEnded || dialog == null)  && isUsed ==0  && !dialogs.isEmpty()) {
			dialog = dialogs.get(0);
			for(int i =0;i<dialogs.get(0).getAnswers().size();i++) {

					if(dialogs.get(0).getAnswers().get(i).getLeadsTo()==-2) {
						isTrade = 1;
						break;
					}
				}
			isEnded = false;
		}
		
		if((isEnded || dialog == null) && isUsed ==1 && !dialogs.isEmpty()) {
			for(int i =0;i<dialogs.get(0).getAnswers().size();i++) {
				if(dialogs.get(0).getAnswers().get(i).getLeadsTo()==-2) {
					
					List<Answer> temp = new ArrayList<Answer>();
					Answer tem = dialogs.get(0).getAnswers().get(i);
					tem.setId(0);
					temp.add(tem);
					dialog = new Dialog(0, dialogs.get(0).getContent(), temp);
					isEnded = false;
					isTrade = 1;
					break;
				}
			}
		}

		if(window.getInput().isMouseButtonReleased(0) && !guiRenderer.isDialogOpen() && isUsed == 0) {
			guiRenderer.showDialog(dialog, language);
			
			//if(!quests.isEmpty() && quests.get(0).getIdNpc()!=NPCid) {
				//Save.addNpcInteractions(new NpcInteractions(quests.get(0).getNextInteraction(), 1, quests.get(0).getIdNpc()));
				//System.out.println(quests.get(0).getTitle());
				//System.out.println(Save.getNpcsByID(NPCid).getName()+ " "+quests.get(0).getNextInteraction()+" interaction 1");

			//}
		}
		if(isUsed == 0 && dialog.clickedAnswer() != null) {
			interactionsHelper(currentWorld, guiRenderer, player, NPCid, language);
		}

		if(window.getInput().isMouseButtonReleased(0) && !guiRenderer.isDialogOpen() && isUsed == 1 && isTrade == 1) {
			guiRenderer.showDialog(dialog, language);
		}

		if(isUsed == 1 && dialog.clickedAnswer() != null) {
			interactionsHelper(currentWorld, guiRenderer, player, NPCid, language);
		}

	}
	public int getIsQuestSend() {
		return isQuestSend;
	}

	public void setIsQuestSend(int isQuestSend) {
		this.isQuestSend = isQuestSend;
	}

	public void interactionsHelper(WorldRenderer currentWorld, GuiRenderer guiRenderer, Player player, int NPCid, LanguageLoader language) {

		for(int i = 0; i < dialog.getAnswers().size();i++) {
			Answer answerT = dialog.clickedAnswer();

				if(answerT!=null && answerT.getId() == i) {
					///////////////////////////////////////////////////////////////////////////////////////////// TRADE
					if( isTrade == 1 && answerT.getLeadsTo()==-2) {
						isEnded = true;
						
						guiRenderer.closeDialog();
						setTalkingNPCid(-1);
						
						player.tradeWithPlayer(currentWorld, guiRenderer, NPCid);
						
						System.out.println("you trade with: "+ currentWorld.getWorld().getNpcByNpcId(NPCid).getName());
						
						break;
					}////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					if(answerT.getLeadsTo() == -1) {
						isEnded = true;
						//TALK TO TASK TALK TASK
						for(Quest q1:player.getQuests()) {
							for(Task t1:q1.getTasks()) {
								if(t1.getState() == doState.talk) {
									if(t1.getDoItemID() == NPCid) {
										t1.increaseHowMuchIsDone();
									}
								}
							}
							if(q1.isEndedQuest() && !q1.isRewardedYet()) {
								System.out.println("Zjeb");
								q1.sendReward(player, guiRenderer, currentWorld.getWorld(), isUsed);
								guiRenderer.getStatsContainer().updatePlayerInventory(guiRenderer, player);
							}
						}
						guiRenderer.closeDialog();
						setTalkingNPCid(-1);
						
						if(quests.isEmpty()) {
							isUsed = 1;
							Save.addNpcInteractions(new NpcInteractions(file, isUsed, NPCid));
							System.out.println(Save.getNpcsByID(NPCid).getName()+" "+isUsed + " "+file+" interaction 1");
							Save.UpdateInteractions(currentWorld.getWorld().getNPCs());
						}
						if(isQuestSend==1) {
							isUsed = 1;
							Save.addNpcInteractions(new NpcInteractions(file, isUsed, NPCid));
							System.out.println(Save.getNpcsByID(NPCid).getName()+ " "+isUsed + " "+file+" interaction 2"); //wy³¹cza questy okok
							Save.UpdateInteractions(currentWorld.getWorld().getNPCs());
							for(Quest q :quests) {
								for(Task t: q.getTasks()) {
									if(t.getState()==doState.find) {
										
										Save.addNpcInteractions(new NpcInteractions(q.getNextInteraction(), 0, q.getIdNpc()));
										System.out.println(Save.getNpcsByID(q.getIdNpc()).getName()+" "+0 + " "+file+" interaction 3");
										Save.UpdateInteractions(currentWorld.getWorld().getNPCs());
										break;
									}
								}
							}
							isQuestSend = 0;
						}
						break;
					}
					if(answerT.getLeadsTo()>=0) {
					guiRenderer.closeDialog();
					dialog = new Dialog(dialogs.get(answerT.getLeadsTo()).getId(),
							dialogs.get(answerT.getLeadsTo()).getContent(),
							dialogs.get(answerT.getLeadsTo()).getAnswers(),
							dialogs.get(answerT.getLeadsTo()).getQuestID()
							);
					guiRenderer.showDialog(dialog, language);
					if(dialog.getQuestID()!=-1) {
						player.addNewQuestNoRepeating(getQuestByID(dialog.getQuestID()));
						this.isQuestSend = 1;
					}}
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

	public static int getTalkingNPCid() {
		return talkingNPCid;
	}

	public static void setTalkingNPCid(int talkingNPCid) {
		Interactions.talkingNPCid = talkingNPCid;
	}

	public int getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(int isUsed) {
		this.isUsed = isUsed;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}	
}

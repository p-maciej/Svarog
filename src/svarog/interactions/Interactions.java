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

import svarog.entity.NPC;
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
	
	private static final String path = "resources/quests/";
	
	public Interactions(String file) {
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

					//System.out.println("dialog ID no : " + eElement.getAttribute("id"));
					//System.out.println("First Name : " + eElement.getElementsByTagName("content").item(0).getTextContent());
					for(int i = 0; i < Integer.parseInt(eElement.getElementsByTagName("ans").item(0).getTextContent());i++){
						answers.add(new Answer(Integer.parseInt(eElement.getElementsByTagName("id").item(i).getTextContent()),
								eElement.getElementsByTagName("answer").item(i).getTextContent(),
								Integer.parseInt(eElement.getElementsByTagName("leadsTo").item(i).getTextContent())));
						//System.out.println(eElement.getElementsByTagName("answer").item(i).getTextContent());
						//System.out.println(eElement.getElementsByTagName("id").item(i).getTextContent());
						//System.out.println(eElement.getElementsByTagName("leadsTo").item(i).getTextContent());
					}
					if( Integer.parseInt(eElement.getElementsByTagName("q").item(0).getTextContent())!=0) {
						for(int i =0; i< Integer.parseInt(eElement.getElementsByTagName("t").item(0).getTextContent());i++) {
							tasks.add(new Task(Integer.parseInt(eElement.getElementsByTagName("taskID").item(i).getTextContent()),
									eElement.getElementsByTagName("title").item(i).getTextContent(),
									eElement.getElementsByTagName("description").item(i).getTextContent(),
									Integer.parseInt(eElement.getElementsByTagName("toDo").item(i).getTextContent()),
									Integer.parseInt(eElement.getElementsByTagName("doItemID").item(i).getTextContent()),
									doState.valueOf(eElement.getElementsByTagName("state").item(i).getTextContent())
									));
						}
					}
					/*Quest temporaryGuy = new Quest(Integer.parseInt(eElement.getElementsByTagName("questID").item(0).getTextContent()),
							Integer.parseInt(eElement.getElementsByTagName("taskID").item(0).getTextContent()),
							eElement.getElementsByTagName("title").item(0).getTextContent(),
							eElement.getElementsByTagName("description").item(0).getTextContent(),
							Integer.parseInt(eElement.getElementsByTagName("toKill").item(0).getTextContent()),
							Integer.parseInt(eElement.getElementsByTagName("toCollect").item(0).getTextContent()),
							Integer.parseInt(eElement.getElementsByTagName("killID").item(0).getTextContent()),
							Integer.parseInt(eElement.getElementsByTagName("collectID").item(0).getTextContent()));
					
					quest.add(temporaryGuy);*/
					
					dialogs.add(new Dialog(Integer.parseInt(eElement.getAttribute("id")),
							eElement.getElementsByTagName("content").item(0).getTextContent(),
							answers));
					if(eElement.getElementsByTagName("questID").item(0) != null) {
						quests.add(new Quest(Integer.parseInt(eElement.getElementsByTagName("questID").item(0).getTextContent()),
								tasks));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void ChceckInteractions(WorldRenderer currentWorld, Camera camera, Window window, GuiRenderer guiRenderer) {
		//Dialog dialog = null;
		//int answer2 = 0;
		for(int i=0; i < currentWorld.getWorld().numberOfEntities() - 1 ; i++) {
			if(currentWorld.isOverEntity(currentWorld.getWorld().getEntity(i), camera, window) && window.getInput().isMouseButtonPressed(0)) {
				if(currentWorld.getWorld().getEntity(i) instanceof NPC && currentWorld.getWorld().getEntity(i).getId() == 4 && !guiRenderer.isDialogOpen()) {
					Main.dialog = dialogs.get(0);
					guiRenderer.showDialog(Main.dialog);
					Main.ans1 = 1;
				}
			}
		}
		//System.out.println(Main.ans1);
		if(Main.ans1 != 0) {
			if(Main.dialog.clickedAnswer() != null) {
				if(Main.dialog.clickedAnswer().getId() == 0) {
					guiRenderer.closeDialog();
					//Main.ans1=0;
					Main.dialog1 = new Dialog(dialogs.get(1).getId());
					Main.dialog1.setContent(dialogs.get(1).getContent());
					Main.dialog1.setAnswers(dialogs.get(1).getAnswers());
					guiRenderer.showDialog(Main.dialog1);
					Main.ans1=2;
				}
			}
		}
		if(Main.ans1 != 0) {
			if(Main.dialog.clickedAnswer() != null) {
				if(Main.dialog.clickedAnswer().getId() == 1) {
					guiRenderer.closeDialog();
					//Main.ans1=0;
					Main.dialog1 = dialogs.get(2);
					guiRenderer.showDialog(Main.dialog1);
					Main.ans1=2;
				}
			}
		}
		if(Main.ans1 == 2) {
			if(Main.dialog1.clickedAnswer() != null) {
				if(Main.dialog1.clickedAnswer().getId() == 0) {
					guiRenderer.closeDialog();
				}
			}
		}
	}
	
	public Dialog getDialogAt(int i) {
		return dialogs.get(i);
	}
	
	public Quest getQuestAt(int i) {
		return quests.get(i);
	}
	
}

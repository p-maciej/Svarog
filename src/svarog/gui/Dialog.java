package svarog.gui;

import java.util.ArrayList;
import java.util.List;

public class Dialog {
	private String content;
	private List<Answer> answers;
	private int id;
	private int questID=-1;

	public Dialog(int dialogId) {
		this.setId(dialogId);
		this.setContent("");
		this.setAnswers(new ArrayList<Answer>());
	}
	
	public Dialog(int dialogId, String content, List<Answer> answers) {
		this.setId(dialogId);
		this.setContent(content);
		this.setAnswers(answers);
	}
	
	public Dialog(int dialogId, String content, List<Answer> answers, int questID) {
		this.setId(dialogId);
		this.setContent(content);
		this.setAnswers(answers);
		this.setQuestID(questID);
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public List<Answer> getAnswers() {
		return answers;
	}
	
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	} 
	
	public Answer clickedAnswer() {
		for(Answer answer : answers) {
			if(answer.getObjectId() == GuiRenderer.getClickedObjectId()) {
				return answer;
			}
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getQuestID() {
		return questID;
	}

	public void setQuestID(int questID) {
		this.questID = questID;
	}
}

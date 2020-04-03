package svarog.gui;

import java.util.ArrayList;
import java.util.List;

public class Dialog {
	private String content;
	private List<Answer> answers;
	
	public Dialog() {
		this.setContent("");
		this.setAnswers(new ArrayList<Answer>());
	}
	
	public Dialog(String content, List<Answer> answers) {
		this.setContent(content);
		this.setAnswers(answers);
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
		for(Answer answer : answers)
			if(answer.getObjectId() == GuiRenderer.getClickedObjectId())
				return answer;
		
		return null;
	}
}

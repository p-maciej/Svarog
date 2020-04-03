package svarog.gui;

public class Answer {
	private int id;
	private String content;
	private int leadsTo;
	
	public Answer() {
		this.setId(-1);
		this.setContent("");
		this.setLeadsTo(-1);
	}
	
	public Answer(int id, String content, int leadsTo) {
		this.setId(id);
		this.setContent(content);
		this.setLeadsTo(leadsTo);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getLeadsTo() {
		return leadsTo;
	}

	public void setLeadsTo(int leadsTo) {
		this.leadsTo = leadsTo;
	}
}

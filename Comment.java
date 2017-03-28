package modelPOJO;

import java.sql.Date;

public class Comment {
	String text;
	int ownerId;
	Date date;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getOwner() {
		return ownerId;
	}
	public void setOwner(int ownerId) {
		this.ownerId = ownerId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}

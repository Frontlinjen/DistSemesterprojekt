package DatabaseController;

import java.io.Serializable;
import java.sql.Date;

import modelPOJO.Comment;

public class CommentDTO implements Serializable{
	String text;
	String ownerId;
	Date date;
	int ID;
	int taskID;
	
	public CommentDTO(){
	}
	
	public static CommentDTO fromModel(Comment comment){
		CommentDTO dto = new CommentDTO();
		dto.setDate(comment.getDate());
		dto.setOwnerId(comment.getOwner());
		dto.setText(comment.getText());
		dto.setID(comment.getID());
		dto.setTaskID(comment.getTaskID());
		return dto;
	}
	
	public Comment getModel(){
		Comment comment = new Comment();
		comment.setDate(this.getDate());
		comment.setOwner(this.getOwnerId());
		comment.setText(this.getText());
		comment.setID(this.getID());
		comment.setTaskID(this.getTaskID());
		return comment;
	}

	public String getText() {
		return text;
	}

	public CommentDTO setText(String text) {
		this.text = text;
		return this;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public CommentDTO setOwnerId(String ownerId) {
		this.ownerId = ownerId;
		return this;
	}

	public Date getDate() {
		return date;
	}

	public CommentDTO setDate(Date date) {
		this.date = date;
		return this;
	}
	
	public int getID() {
		return ID;
	}

	public CommentDTO setID(int ID) {
		this.ID = ID;
		return this;
	}

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}	
}

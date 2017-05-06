package DatabaseController;

import java.io.Serializable;
import java.sql.Date;

import modelPOJO.Comment;

public class CommentDTO implements Serializable{
	String message;
	String Commenter;
	Date submitDate;
	int CommentID;
	int TaskID;
	
	public CommentDTO(){
	}
	
	public static CommentDTO fromModel(Comment comment){
		CommentDTO dto = new CommentDTO();
		dto.setSubmitDate(comment.getSubmitDate());
		dto.setCommenter(comment.getCommenter());
		dto.setMessage(comment.getMessage());
		dto.setCommentID(comment.getCommentID());
		dto.setTaskID(comment.getTaskID());
		return dto;
	}
	
	public Comment getModel(){
		Comment comment = new Comment();
		comment.setSubmitDate(this.getSubmitDate());
		comment.setCommenter(this.getCommenter());
		comment.setMessage(this.getMessage());
		comment.setCommentID(this.getCommentID());
		comment.setTaskID(this.getTaskID());
		return comment;
	}

	public String getMessage() {
		return message;
	}

	public CommentDTO setMessage(String text) {
		this.message = text;
		return this;
	}

	public String getCommenter() {
		return Commenter;
	}

	public CommentDTO setCommenter(String ownerId) {
		this.Commenter = ownerId;
		return this;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public CommentDTO setSubmitDate(Date date) {
		this.submitDate = date;
		return this;
	}
	
	public int getCommentID() {
		return TaskID;
	}

	public CommentDTO setCommentID(int ID) {
		this.CommentID = ID;
		return this;
	}

	public int getTaskID() {
		return TaskID;
	}

	public void setTaskID(int taskID) {
		this.TaskID = taskID;
	}	
}

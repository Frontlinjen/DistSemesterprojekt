package DatabaseController;

import java.io.Serializable;
import java.sql.Date;

import modelPOJO.Comment;

public class CommentDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCommenter() {
		return Commenter;
	}

	public void setCommenter(String commenter) {
		Commenter = commenter;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public int getCommentID() {
		return CommentID;
	}

	public void setCommentID(int commentID) {
		CommentID = commentID;
	}

	public int getTaskID() {
		return TaskID;
	}

	public void setTaskID(int taskID) {
		TaskID = taskID;
	}

}

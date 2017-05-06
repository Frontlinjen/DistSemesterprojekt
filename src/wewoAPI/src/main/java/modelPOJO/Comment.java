package modelPOJO;

import java.sql.Date;

public class Comment {
	String message;
	String Commenter;
	Date submitDate;
	int CommentID;
	int TaskID;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String text) {
		this.message = text;
	}
	public String getCommenter() {
		return Commenter;
	}
	public void setCommenter(String ownerId) {
		this.Commenter = ownerId;
	}
	public Date getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(Date date) {
		this.submitDate = date;
	}
	public int getCommentID() {
		return CommentID;
	}
	public void setCommentID(int ID) {
		this.CommentID = ID;
	}
	public int getTaskID() {
		return TaskID;
	}
	public void setTaskID(int taskID) {
		this.TaskID = taskID;
	}
	
	
}

package DatabaseController;

import java.io.Serializable;
import java.sql.Date;

import modelPOJO.Comment;

public class CommentDTO implements Serializable{
	String text;
	int ownerId;
	Date date;
	
	public CommentDTO(){
	}
	
	static CommentDTO fromModel(Comment comment){
		CommentDTO dto = new CommentDTO();
		dto.setDate(comment.getDate());
		dto.setOwnerId(comment.getOwner());
		dto.setText(comment.getText());
		return dto;
	}
	
	Comment getModel(){
		Comment comment = new Comment();
		comment.setDate(this.getDate());
		comment.setOwner(this.getOwnerId());
		comment.setText(this.getText());
		return comment;
	}

	public String getText() {
		return text;
	}

	public CommentDTO setText(String text) {
		this.text = text;
		return this;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public CommentDTO setOwnerId(int ownerId) {
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
}

package wewoAPI;

import com.amazonaws.services.lambda.runtime.Context;

import modelPOJO.IDObject;
import modelPOJO.JsonList;

class Comment{
	String posterID;
	String message;
	long timestamp;
	public String getPosterID() {
		return posterID;
	}
	public void setPosterID(String posterID) {
		this.posterID = posterID;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}

public class CommentsController {
	public JsonList<Comment> GetComments(IDObject taskID, Context context)
	{
		return null;	
	}
	
	public void CreateComment(Comment comment, Context context)
	{
		
	}
	
}

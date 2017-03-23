package wewoAPI;

import com.amazonaws.services.lambda.runtime.Context;

import modelPOJO.IDObject;
import modelPOJO.JsonList;

class RatingObject{
	String ratee;
	String message;
	int rating;
	public String getRatee() {
		return ratee;
	}
	public void setRatee(String ratee) {
		this.ratee = ratee;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
}

public class RatingController {
	public void CreateRating(RatingObject rating, Context context)
	{
		return;
	}
	public JsonList<RatingObject> GetRatings(IDObject ratee){
		return null;
	}
}

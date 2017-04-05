package modelPOJO;

public class Rating {

	int rating;
	String rateeID, message;
	
	
	public String getRateeID() {
		return rateeID;
	}
	public void setRateeID(String rateeID) {
		this.rateeID = rateeID;
	}
	
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}

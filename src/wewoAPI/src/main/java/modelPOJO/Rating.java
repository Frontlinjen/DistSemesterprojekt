package modelPOJO;

public class Rating {

	int rating, raterID, rateeID;
	String message;
	
	public int getRaterID() {
		return raterID;
	}
	public void setRaterID(int raterID) {
		this.raterID = raterID;
	}
	public int getRateeID() {
		return rateeID;
	}
	public void setRateeID(int rateeID) {
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

package modelPOJO;

public class Rating {

	int rating, ratingID;
	String rateeID, message;
	
	public int getRatingID() {
		return ratingID;
	}
	public void setRatingID(int ratingID) {
		this.ratingID = ratingID;
	}
	
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

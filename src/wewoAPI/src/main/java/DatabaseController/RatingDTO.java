package DatabaseController;

import java.io.Serializable;

public class RatingDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	int raterID, rateeID, rating;
	String message;
	
	public RatingDTO(){
		
	}
	
	public RatingDTO(RatingDTO rate){
		this.raterID = rate.raterID;
		this.rateeID = rate.rateeID;
		this.rating = rate.rating;
		this.message = rate.message;
	}
	
	public int getRaterID(){return raterID;}
	public RatingDTO setRaterID(int raterID){this.raterID = raterID; return this;}
	public int getRateeID(){return rateeID;}
	public RatingDTO setRateeID(int rateeID){this.rateeID = rateeID; return this;}
	public int getRating(){return rating;}
	public RatingDTO setRating(int rating){this.rating = rating; return this;}
	public String getMessage(){return message;}
	public RatingDTO setMessage(String message){this.message = message; return this;}
}
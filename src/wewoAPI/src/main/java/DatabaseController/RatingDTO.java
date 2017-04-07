package DatabaseController;

import java.io.Serializable;

import modelPOJO.Rating;

public class RatingDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	int rating, ratingID;
	String raterID, rateeID, message;
	
	public RatingDTO(){
		
	}
	
	public RatingDTO(RatingDTO rate){
		this.raterID = rate.raterID;
		this.rateeID = rate.rateeID;
		this.rating = rate.rating;
		this.message = rate.message;
		this.ratingID = rate.ratingID;
	}
	
	public static RatingDTO fromModel(Rating rate){
		RatingDTO dto = new RatingDTO();
		dto.setRating(rate.getRating());
		dto.setRateeID(rate.getRateeID());
		dto.setMessage(rate.getMessage());
		dto.setRatingID(rate.getRatingID());
		return dto;
	}
	
	public Rating getModel(){
		Rating rate = new Rating();
		rate.setRating(this.getRating());;
		rate.setRateeID(this.getRateeID());
		rate.setMessage(this.getMessage());
		rate.setRatingID(this.getRatingID());
		return rate;
	}
	
	public String getRaterID(){return raterID;}
	public RatingDTO setRaterID(String raterID){this.raterID = raterID; return this;}
	public String getRateeID(){return rateeID;}
	public RatingDTO setRateeID(String rateeID){this.rateeID = rateeID; return this;}
	public int getRating(){return rating;}
	public RatingDTO setRating(int rating){this.rating = rating; return this;}
	public String getMessage(){return message;}
	public RatingDTO setMessage(String message){this.message = message; return this;}
	public int getRatingID(){return rating;}
	public RatingDTO setRatingID(int ratingID){this.ratingID = rating; return this;}
}
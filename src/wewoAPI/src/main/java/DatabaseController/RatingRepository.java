package DatabaseController;

public interface RatingRepository {
	
	String lookUpRater(String ratee, int ratingID) throws DALException;
	RatingDTO getRating(String rater, String ratee) throws DALException;
	boolean createRating(RatingDTO rate) throws DALException;
}

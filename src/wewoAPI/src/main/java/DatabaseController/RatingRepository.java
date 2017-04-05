package DatabaseController;

public interface RatingRepository {
	
	int lookUpRater(String ratee, int ratingID);
	RatingDTO getRating(String rater, String ratee) throws DALException;
	boolean createRating(RatingDTO rate) throws DALException;
}

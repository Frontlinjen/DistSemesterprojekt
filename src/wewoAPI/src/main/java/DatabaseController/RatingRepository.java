package DatabaseController;

public interface RatingRepository {
	RatingDTO getRating(int ratingID, String ratee) throws DALException;
	boolean createRating(RatingDTO rate) throws DALException;
}

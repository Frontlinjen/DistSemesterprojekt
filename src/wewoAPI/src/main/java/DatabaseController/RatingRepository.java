package DatabaseController;

public interface RatingRepository {

	RatingDTO getRating(RatingDTO rate) throws DALException;
	boolean createRating(RatingDTO rate) throws DALException;
}

package DatabaseController;

import java.util.List;

public interface RatingRepository {
	boolean createRating(RatingDTO rate) throws DALException;
	RatingDTO getRating(int ratingID, String ratee) throws DALException;
	List<RatingDTO> getLastRatings(String ratee) throws DALException;
	boolean hasRelation(String ratee, String rater) throws DALException;
}

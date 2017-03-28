package DatabaseController;
public class MySQLRatingRepository implements RatingRepository{

	private final String GET_RATING = "SELECT * FROM UserRatings WHERE raterID = ? AND rateeID = ?;";
	private final String CREATE_RATING = "INSERT INTO UserRatings(rating, raterID, rateeID, message) "
									   + "VALUES (?, ?, ?, ?);";

	public RatingDTO getRating(RatingDTO rate) throws DALException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean createRating(RatingDTO rate) throws DALException {
		// TODO Auto-generated method stub
		return false;
	}
	

}

package DatabaseController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLRatingRepository implements RatingRepository{

	private final String GET_RATER = "SELECT raterID FROM UserRatings WHERE rateeID = ? AND ratingID = ?";
	private final String GET_RATING = "SELECT * FROM UserRatings WHERE raterID = ? AND rateeID = ?;";
	private final String CREATE_RATING = "INSERT INTO UserRatings(rating, raterID, rateeID, message) "
									   + "VALUES (?, ?, ?, ?);";

	public MySQLRatingRepository() throws DALException{
		DatabaseConnector.RegisterStatement("GET_RATING", GET_RATING);
		DatabaseConnector.RegisterStatement("CREATE_RATING", CREATE_RATING);
		DatabaseConnector.RegisterStatement("GET_RATER", GET_RATER);
	}
	
	/*public String lookUpRater(String rateeID, int ratingID) throws DALException{
		PreparedStatement statement;
		try{
			statement = DatabaseConnector.getPreparedStatement(GET_RATER);
			statement.setString(1, rateeID);
			statement.setInt(2, ratingID);
			
			ResultSet rs = statement.executeQuery();
			
			if(!rs.first())
				throw new DALException("Kan ikke finde en rating med med rateeID " + rateeID + " og ratingID " + ratingID);
			return rs.getString("raterID");
		}catch(SQLException e){
			throw new DALException(e);
	}
		
	}*/
	
	public RatingDTO getRating(int ratingID, String rateeID) throws DALException {
		PreparedStatement statement;
		try {
			statement = DatabaseConnector.getPreparedStatement("GET_RATING");
			statement.setInt(1, ratingID);
			statement.setString(2, rateeID);
		

			ResultSet rs = statement.executeQuery();

			if (!rs.first()) 
				throw new DALException("Rating med rateeID " + rateeID + " og/eller ratingID " + ratingID + " findes ikke");
			
			return generate(rs);	
		}
		catch (SQLException e) {
			throw new DALException(e); 
		}
	}

	private RatingDTO generate(ResultSet rs) throws SQLException{
		RatingDTO rate = new RatingDTO();
		rate.setRating(rs.getInt("Rating")).setRaterID(rs.getString("RaterID"))
		.setRateeID(rs.getString("RateeID")).setMessage(rs.getString("Message"));
		return rate;
	}
	
	public boolean createRating(RatingDTO rate) throws DALException {
		try {
			PreparedStatement statement = DatabaseConnector.getPreparedStatement("CREATE_RATING");
			statement.setFloat(1, rate.rating);
			statement.setString(2, rate.raterID);
			statement.setString(3, rate.rateeID);
			statement.setString(4, rate.message);
			
			//Ved ikke helt hvordan jeg skal lave denne del...
			ResultSet rs = DatabaseConnector.doQuery("SELECT LAST_INSERT_ID();");
			rs.first();
			int ID = rs.getInt("last_insert_id()");
			rate.ratingID = ID;
			System.out.println("Rating created with ratingID:" + rate.ratingID);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Rating didn't get created");
			e.printStackTrace();
		}
		return false;
	}
	

}

package DatabaseController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLRatingRepository implements RatingRepository{

	private final String CAN_RATE = "SELECT COUNT(TaskID) FROM Appliers, Tasks WHERE Appliers.status = 'Closed' AND ((Tasks.creatorID = ? AND Appliers.ApplierID = ?) OR (Tasks.creatorID = ? AND Appliers.ApplierID = ?))";
	private final String GET_RATER = "SELECT raterID FROM UserRatings WHERE rateeID = ? AND ratingID = ?";
	private final String GET_RATING = "SELECT * FROM UserRatings WHERE raterID = ? AND rateeID = ?;";
	private final String CREATE_RATING = "INSERT INTO UserRatings(rating, raterID, rateeID, message) "
									   + "VALUES (?, ?, ?, ?);";
	private final String CREATE_RATING_MAPPING = "INSERT INTO UserRatingMapper(raterID, rateeID) VALUES (?, ?)";
	private final String GET_LATEST_RATINGS = "select * from UserRatingMapper where UserRatingMapper.rateeID = ? ORDER BY UserRatingMapper.RatingID DESC LIMIT 3;";

	public MySQLRatingRepository() throws DALException{
		DatabaseConnector.RegisterStatement("GET_RATING", GET_RATING);
		DatabaseConnector.RegisterStatement("CREATE_RATING", CREATE_RATING);
		DatabaseConnector.RegisterStatement("GET_RATER", GET_RATER);
		DatabaseConnector.RegisterStatement("CAN_RATE", CAN_RATE);
		DatabaseConnector.RegisterStatement("CREATE_RATING_MAPPING", CREATE_RATING_MAPPING);
		DatabaseConnector.RegisterStatement("GET_LATEST_RATINGS", GET_LATEST_RATINGS);
	}
	
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
			PreparedStatement ratingStatement = DatabaseConnector.getPreparedStatement("CREATE_RATING");
			PreparedStatement mappingStatement = DatabaseConnector.getPreparedStatement("CREATE_RATING_MAPPING");
			DatabaseConnector.StartTransaction();
			ratingStatement.setFloat(1, rate.rating);
			ratingStatement.setString(2, rate.raterID);
			ratingStatement.setString(3, rate.rateeID);
			ratingStatement.setString(4, rate.message);
			ratingStatement.executeUpdate();
			mappingStatement.setString(1, rate.raterID);
			mappingStatement.setString(2, rate.rateeID);
			mappingStatement.executeUpdate();
			DatabaseConnector.EndTransaction();
			
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
			throw new DALException(e);
		}
	}

	public List<RatingDTO> getLastRatings(String ratee) throws DALException {
		PreparedStatement statement = null;
		try{
			statement = DatabaseConnector.getPreparedStatement("GET_LATEST_RATINGS");
			statement.setString(1, ratee);
			ResultSet set = statement.executeQuery();
			
			List<RatingDTO> ratings = new ArrayList<RatingDTO>();
			while(set.next()){
				ratings.add(generate(set));
			}
			return ratings;
		}
		catch(SQLException e){
			throw new DALException(e);
		}
	}

	public boolean hasRelation(String ratee, String rater) throws DALException {
		PreparedStatement statement = null;
		try{
			statement = DatabaseConnector.getPreparedStatement("CAN_RATE");
			statement.setString(1, ratee);
			statement.setString(2, rater);
			
			statement.setString(3, rater);
			statement.setString(4, ratee);
			
			ResultSet rs = statement.executeQuery();
			if(rs.first()){
				int amount = rs.getInt(1);
				return amount > 0;
			}
		}
		catch(SQLException e){
			throw new DALException(e);
		}
		return false;
	}
	

}

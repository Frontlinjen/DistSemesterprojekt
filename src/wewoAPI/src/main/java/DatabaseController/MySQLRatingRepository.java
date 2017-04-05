package DatabaseController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MySQLRatingRepository implements RatingRepository{

	private final String GET_RATING = "SELECT * FROM UserRatings WHERE raterID = ? AND rateeID = ?;";
	private final String CREATE_RATING = "INSERT INTO UserRatings(rating, raterID, rateeID, message) "
									   + "VALUES (?, ?, ?, ?);";


	public MySQLRatingRepository(){
		DatabaseConnector.RegisterStatement("GET_TASK", GET_RATING);
		DatabaseConnector.RegisterStatement("CREATE_TASK", CREATE_RATING);
	}
	
	public String lookUpRater(String rateeID, int ratingID){
		try{
			return rater;
		}
		catch(SQLException e){
			
		}
		return null;
		
	}
	
	public RatingDTO getRating(String raterID, String rateeID) throws DALException {
		PreparedStatement statement;
		try {
			statement = DatabaseConnector.getPreparedStatement("GET_RATING");
			statement.setString(1, raterID);
			statement.setString(2, rateeID);
		

			ResultSet rs = statement.executeQuery();

			if (!rs.first()) 
				throw new DALException("Rating med rateeID " + rateeID + " og/eller raterID " + raterID + " findes ikke");
			
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
			statement.setInt(1, rate.rating);
			statement.setString(2, rate.raterID);
			statement.setString(3, rate.rateeID);
			statement.setString(4, rate.message);
			
			int res = statement.executeUpdate();

			//Ved ikke helt hvordan jeg skal lave denne del...
			/*ResultSet rs = DatabaseConnector.doQuery("SELECT LAST_INSERT_ID();");
			rs.first();
			int ID = rs.getInt("last_insert_id()");
			rate.id = ID;*/
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	

}

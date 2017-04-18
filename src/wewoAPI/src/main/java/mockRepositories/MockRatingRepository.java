package mockRepositories;

import java.awt.RenderingHints.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import DatabaseController.DALException;
import DatabaseController.RatingDTO;
import DatabaseController.RatingRepository;

public class MockRatingRepository implements RatingRepository{
	class primary_key{
		
		public String raterID;
		public String rateeID;
		
		@Override
		public int hashCode() {
			return (raterID + rateeID).hashCode();
		}	
	}
	private HashMap<primary_key, RatingDTO> database1 = new HashMap<primary_key, RatingDTO>();
	private List<String> database2 = new ArrayList<String>();
	
	public String lookUpRater(String ratee, int ratingID) throws DALException {
		
		return database2.get(ratingID);
	}

	public RatingDTO getRating(String rater, String ratee) throws DALException {
		primary_key key = new primary_key();
		key.rateeID = ratee;
		key.raterID = rater;
		return database1.get(key);
	}

	public boolean createRating(RatingDTO rate) throws DALException {
		try{
		    database2.add(rate.toString());
			return true;
		}catch(Exception e){
			return false;
		}
	}

}

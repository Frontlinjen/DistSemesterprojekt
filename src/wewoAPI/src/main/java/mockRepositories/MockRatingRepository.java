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
		public String toString() {
			return (raterID + rateeID);
		}
	}
	
	//weak_key raterID + rateeID
	private HashMap<String, RatingDTO> database1 = new HashMap<String, RatingDTO>();
	//primary key ratingID
	private List<String> database2 = new ArrayList<String>();


	public RatingDTO getRating(int ratingID, String ratee) throws DALException {
		
		if(ratingID > database2.size())
		{
			return null;
		}
		
		String rater = database2.get(ratingID);
		
		primary_key key = new primary_key();
		
		key.rateeID = ratee;
		key.raterID = rater;
		return database1.get(key.toString());
	}

	public boolean createRating(RatingDTO rate) throws DALException {
		try{
			
		    database2.add(rate.getRaterID());
		    rate.setRatingID(database2.size()-1);
		    primary_key key = new primary_key();
		    key.raterID = rate.getRaterID();
		    key.rateeID = rate.getRateeID();
		    database1.put(key.toString(), rate);
			return true;
		}catch(Exception e){
			return false;
		}
	}

}

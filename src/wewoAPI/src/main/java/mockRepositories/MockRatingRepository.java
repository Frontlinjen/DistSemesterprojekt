package mockRepositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import DatabaseController.DALException;
import DatabaseController.RatingDTO;
import DatabaseController.RatingRepository;

public class MockRatingRepository implements RatingRepository{
	class primary_key{
		
		public String raterID;
		public String rateeID;
		
		@Override
		public String toString() {
			return (rateeID + raterID);
		}
	}
	
	//weak_key raterID + rateeID
	private HashMap<String, RatingDTO> database1 = new HashMap<String, RatingDTO>();
	//primary key ratingID
	private List<String> database2 = new ArrayList<String>();

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
	
	public List<RatingDTO> getLastRatings(String ratee) throws DALException{
		List<RatingDTO> container = new ArrayList<RatingDTO>();
		Iterator<Entry<String, RatingDTO>> it = database1.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, RatingDTO> pair = (Map.Entry<String, RatingDTO>)it.next();
	        if(((String)pair.getKey()).startsWith(ratee)){
	        	container.add((RatingDTO)pair.getValue());
	        	if(container.size() == 3){
	        		return container;
	        	}
	        }
	    }
		return container;
	}
	
	public boolean hasRelation(String ratee, String rater) throws DALException{
		if(ratee.equals("rateeIDTest") && rater.equals("Test2"))
			return true;
		else
			return false;
		
	}

}

package wewoAPI;

import com.amazonaws.services.lambda.runtime.Context;

import modelPOJO.IDObject;
import modelPOJO.JsonList;
import modelPOJO.Rating;
import DatabaseController.RatingDTO;
import DatabaseController.RatingRepository;
import exceptions.UnauthorizedException;
import DatabaseController.DALException;
import DatabaseController.MySQLRatingRepository;

public class RatingController{
	RatingRepository repository;
	
	public RatingController()
	{
		repository = new MySQLRatingRepository();
	}
	
	public RatingController(RatingRepository repository)
	{
		this.repository = repository;
	}
	
	public IDObject createRating(Rating rate, Context context) throws UnauthorizedException
	{
		if(context.getIdentity() == null || context.getIdentity().getIdentityId().isEmpty())
		{
			throw new UnauthorizedException(null);
		}
		
		IDObject newRating = new IDObject();
		
		RatingRepository dao = new MySQLRatingRepository();
		rate.setMessage(context.getIdentity().getIdentityId());
		RatingDTO dto = RatingDTO.fromModel(rate);
		
		try {
			dao.createRating(dto);
			newRating.setID(rate.getRateeID());
			return newRating;
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newRating;
	}
	
	public Rating getRating(IDObject rating, Context context)
	{
		RatingRepository dao = new MySQLRatingRepository();
		RatingDTO dto;
		try {
			dto = dao.getRating();
			Rating rate = dto.getModel();
			
			return rate;
			
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
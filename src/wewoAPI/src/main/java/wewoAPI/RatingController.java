package wewoAPI;

import com.amazonaws.services.lambda.runtime.Context;

import modelPOJO.RatingIDObject;
import modelPOJO.Rating;
import DatabaseController.RatingDTO;
import DatabaseController.RatingRepository;
import exceptions.UnauthorizedException;
import DatabaseController.DALException;
import DatabaseController.MySQLRatingRepository;

public class RatingController{
	RatingRepository repository;
	
	public RatingController() throws DALException
	{
		repository = new MySQLRatingRepository();
	}
	
	public RatingController(RatingRepository repository)
	{
		this.repository = repository;
	}
	
	public RatingIDObject createRating(Rating rate, Context context) throws UnauthorizedException, DALException
	{
		if(context.getIdentity() == null || context.getIdentity().getIdentityId().isEmpty())
		{
			throw new UnauthorizedException(null);
		}
		
		RatingIDObject newRating = new RatingIDObject();
		
		RatingRepository dao = new MySQLRatingRepository();
		rate.setMessage(context.getIdentity().getIdentityId());
		RatingDTO dto = RatingDTO.fromModel(rate);
		
		try {
			dao.createRating(dto);
			newRating.setRateeID(rate.getRateeID());
			return newRating;
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newRating;
	}
	
	public Rating getRating(RatingIDObject rating, Context context) throws DALException
	{
		RatingRepository dao = new MySQLRatingRepository();
		RatingDTO dto;
		try {
			dto = dao.getRating(rating.getRaterID(), rating.getRateeID());
			Rating rate = dto.getModel();
			
			return rate;
			
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
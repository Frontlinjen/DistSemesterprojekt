package wewoAPI;

import com.amazonaws.services.lambda.runtime.Context;

import java.io.InputStream;
import java.io.OutputStream;
import modelPOJO.Rating;
import DatabaseController.RatingDTO;
import DatabaseController.RatingRepository;
import exceptions.InternalServerErrorException;
import DatabaseController.DALException;
import DatabaseController.MySQLRatingRepository;

public class RatingController extends ControllerBase{
	RatingRepository repository;

	public RatingController() throws DALException
	{
		repository = new MySQLRatingRepository();
	}

	public RatingController(RatingRepository repository)
	{
		this.repository = repository;
	}

	public void createRating(InputStream in, OutputStream out, Context context) throws InternalServerErrorException
	{
		try{
			if(!verifyLogin(context)){
				raiseError(out, 401, "Not logged in");
			}
			StartRequest(in);
			Rating rate = request.getObject(Rating.class);
			if(rate == null){
				raiseError(out, 400, "Invalid Rating Object");
			}
			RatingDTO dto = RatingDTO.fromModel(rate);
			dto.setRaterID(context.getIdentity().getIdentityId());

			try{
				repository.createRating(dto);
				response.addResponseObject("RatingID", dto.getRatingID());
				response.setStatusCode(200);
				FinishRequest(out);
			} catch (DALException e){
				raiseError(out, 503, "Database unavailable");
				return;
			}
		}
		catch(Exception e)
		{
			raiseError(out, 500, "(╯°□°）╯︵ ┻━┻");
			return;
		}
	}

	public void getRating(InputStream in, OutputStream out, Context context) throws InternalServerErrorException
	{
		try{
			StartRequest(in);
			String raterID = request.getObject("RaterID", String.class);
			RatingDTO dto;
			dto = repository.getRating(raterID, context.getIdentity().getIdentityId());
			if(dto==null){
				raiseError(out, 404, "No rating was found using raterID " + raterID);
				return;
			}
		
			Rating rate = dto.getModel();
			response.addResponseObject("Rating", rate);
			response.setStatusCode(200);
			FinishRequest(out);
		} catch (DALException e){
			raiseError(out, 503, "Database unavailable");
			return;
		}
	}
	
	public void lookUpRater(InputStream in, OutputStream out, Context context) throws InternalServerErrorException
	{
		try{
			StartRequest(in);
			int ratingID = request.getObject("RatingID",Integer.class);
			String rater;
			rater = repository.lookUpRater(context.getIdentity().getIdentityId(), ratingID);
			if(rater==null){
				raiseError(out, 404, "No rater was found with ratingID " + ratingID);
				return;
			}
			
			
			response.addResponseObject("Rater", rater);
			response.setStatusCode(200);
			FinishRequest(out);
		} catch (DALException e) {
			raiseError(out, 503, "Database unavailable");
		}
	}
}
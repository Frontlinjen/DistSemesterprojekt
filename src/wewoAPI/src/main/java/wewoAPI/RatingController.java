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
				return;
			}
			StartRequest(in);
			//GET user/{userID}/ratings/{ratingsID}
			String rateeID = request.getPath("rateeID");
			Rating rate = request.getObject(Rating.class);
			if(rate == null){
				raiseError(out, 400, "Invalid Rating Object");
				return;
			}
			
			RatingDTO dto = RatingDTO.fromModel(rate);
			dto.setRateeID(rateeID);
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
			Rating rate = request.getObject(Rating.class);
			String raterID = request.getPath("raterID");
			String rateeID = request.getPath("rateeID");
			String foo = request.getPath("ratingID");
			int ratingID = Integer.parseInt(foo);
			RatingDTO dto;
			
			dto = repository.getRating(ratingID, rateeID);
			if(dto==null){
				raiseError(out, 404, "No rating was found using raterID " + raterID);
				return;
			}
		
			rate = dto.getModel();
			response.addResponseObject("message", rate.message);
			response.addResponseObject("rating", rate.rating);
			response.addResponseObject("ratingObject", rate);
			response.setStatusCode(200);
			FinishRequest(out);
		} catch (DALException e){
			raiseError(out, 503, "Database unavailable");
			return;
		}
	}
	
	/*public void lookUpRater(InputStream in, OutputStream out, Context context) throws InternalServerErrorException
	{
		try{
			StartRequest(in);
			int ratingID = request.getObject("RatingID",Integer.class);
			RatingDTO dto;
			dto = repository.getRating(ratingID, context.getIdentity().getIdentityId());
			if(dto==null){
				raiseError(out, 404, "No rater was found with ratingID " + ratingID);
				return;
			}
			
			
			response.addResponseObject("Rater", dto.getRaterID());
			response.setStatusCode(200);
			FinishRequest(out);
		} catch (DALException e) {
			raiseError(out, 503, "Database unavailable");
		}
	}*/
}
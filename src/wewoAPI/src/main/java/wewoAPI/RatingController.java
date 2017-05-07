package wewoAPI;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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
			context.getIdentity().getIdentityId();
			if(!verifyLogin(context)){
				raiseError(out, 401, "Not logged in");
				return;
			}
			StartRequest(in);
			//GET user/{userID}/ratings/{ratingsID}
			String rateeID = request.getPath("rateeID");
			Rating rate = null;
			try{
				rate = request.getObject(Rating.class);
			}catch(UnrecognizedPropertyException ex){
				raiseError(out, 400, "" + ex.getPropertyName());
				return;
			}
			if(rate == null){
				raiseError(out, 400, "No message body recieved");
				return;
			}
			
			RatingDTO dto = RatingDTO.fromModel(rate);
			if(!repository.hasRelation(rateeID, context.getIdentity().getIdentityId())){
				raiseError(out, 401, "No relation between target person and you");
				return;
			}
			
			dto.setRateeID(rateeID);
			dto.setRaterID(context.getIdentity().getIdentityId());

			try{
				repository.createRating(dto);
				response.addResponseObject("RatingID", dto.getRatingID());
				response.setStatusCode(201);
				response.addHeader("Created", "users/" + dto.getRateeID() + "/ratings/" + dto.getRatingID());
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
			if(!verifyLogin(context)){
				raiseError(out, 401, "Not logged in");
				return;
			}
			StartRequest(in);
			Rating rate = null;
			try{
				rate = request.getObject(Rating.class);
			}catch(UnrecognizedPropertyException ex){
				raiseError(out, 400, "" + ex.getPropertyName());
				return;
			}
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
	
	public void getLastRatings(InputStream in, OutputStream out, Context context) throws InternalServerErrorException
	{
		try{
			StartRequest(in);
			List<Rating> rate = new ArrayList<Rating>();
			String rateeID = request.getPath("rateeID");
			List<RatingDTO> dto;
			
			dto = repository.getLastRatings(rateeID);
			if(dto==null){
				raiseError(out, 404, "No rating was found using rateeID " + rateeID);
				return;
			}
			for(int i = 0; dto.size()>i; i++){
				rate.add(dto.get(i).getModel());
				//rate.set(i, dto.get(i).getModel());
			}
			response.addResponseObject("Ratings", rate);
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
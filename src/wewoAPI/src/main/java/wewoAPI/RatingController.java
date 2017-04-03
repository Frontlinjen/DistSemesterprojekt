package wewoAPI;

import com.amazonaws.services.lambda.runtime.Context;

import modelPOJO.IDObject;
import modelPOJO.JsonList;
import modelPOJO.Rating;
import modelPOJO.Task;
import DatabaseController.MySQLTaskRespository;
import DatabaseController.RatingDTO;
import DatabaseController.RatingRepository;
import DatabaseController.TaskDTO;
import DatabaseController.TaskRespository;
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
			throw new UnauthorizedException();
		}
		
		IDObject newRatingID = new IDObject();
		
		RatingRepository dao = new MySQLRatingRepository();
		rate.setRaterID(context.getIdentity().getIdentityId());
		RatingDTO dto = RatingDTO(rate);
		
		try {
			dao.createRating(dto);
			newRatingID.setID(dto.getId());
			return newTaskID;
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newTaskID;
	}
	
	public Task getRating(IDObject id, Context context)
	{
		TaskRespository dao = new MySQLTaskRespository();
		TaskDTO dto;
		try {
			dto = dao.getTask(id.getID());
			Task task = dto.getModel();
			
			return task;
			
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
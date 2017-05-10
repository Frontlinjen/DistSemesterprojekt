package wewoAPI;

import exceptions.InternalServerErrorException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;

import DatabaseController.DALException;
import DatabaseController.MySQLTagsRepository;
import DatabaseController.TagsDTO;
import DatabaseController.TagsRepository;

public class TagsController extends ControllerBase{
	TagsRepository repository;

	public TagsController() throws InternalServerErrorException
	{
		try{
			repository = new MySQLTagsRepository();
		}
		catch(DALException e) {
			e.printStackTrace();
			throw new InternalServerErrorException("Failed to connect to database");
		}
	}

	public TagsController(TagsRepository repository)
	{
		this.repository = repository;
	}
	
	public void getTags(InputStream in, OutputStream out, Context context) throws InternalServerErrorException{
		HashMap<String, Integer> tags = new HashMap<String, Integer>();
		try{
			StartRequest(in);
			tags = repository.getTags();
			if(tags == null){
				raiseError(out, 400, "No tags exists.");
				return;
			}
			response.addResponseObject("Tags", tags);
			response.addResponseObject("tasks", "/tasks");
			response.setStatusCode(200);
			FinishRequest(out);
			return;
		}
		catch(Exception e){
			raiseError(out, 503, "Database unavailable");
		}
		return;
	}
}

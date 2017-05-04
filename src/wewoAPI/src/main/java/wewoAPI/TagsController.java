package wewoAPI;

import exceptions.InternalServerErrorException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;

import DatabaseController.DALException;
import DatabaseController.MySQLTagsRepository;
import DatabaseController.TagsDTO;
import DatabaseController.TagsRepository;

public class TagsController extends ControllerBase{
	TagsRepository repository;

	public TagsController() throws DALException
	{
		repository = new MySQLTagsRepository();
	}

	public TagsController(TagsRepository repository)
	{
		this.repository = repository;
	}
	
	public void getTags(InputStream in, OutputStream out, Context context) throws InternalServerErrorException{
		try{
			StartRequest(in);
			List<TagsDTO> tags = new ArrayList<TagsDTO>();
			
			response.addResponseObject("Tags", tags);
			response.setStatusCode(200);
			FinishRequest(out);
		}
		catch(Exception e){
			raiseError(out, 503, "Database unavailable");
			return;
		}
	}
}

package wewoAPI;
import com.amazonaws.services.lambda.runtime.Context;

import exceptions.UnauthorizedException;
import modelPOJO.Application;
import modelPOJO.IDObject;
import modelPOJO.JsonList;
import modelPOJO.ResponseObject;
import DatabaseController.ApplicationRepository;
import DatabaseController.DALException;
import DatabaseController.MySQLApplicationRepository;

public class ApplicationController {
	
	MySQLApplicationRepository repository;
	
	
	public ApplicationController()
	{
		repository = new MySQLApplicationRepository();
	}
	
	public ApplicationController(MySQLApplicationRepository repository)
	{
		this.repository = repository;
	}
		
	public JsonList<String> GetApplicants(IDObject taskid, Context context)
	{
		return null;
	}
	
	public void PostApplications(Application app, Context context) throws UnauthorizedException, NumberFormatException, DALException
	{
		
		
		if(context.getIdentity() == null || context.getIdentity().getIdentityId().isEmpty())
		{
			throw new UnauthorizedException("Invalid login");
		}
		if(repository.getApplication(Integer.parseInt(app.getApplierid())) != null) {
			
		}
		
	}
	
	public 	ResponseObject<String> GetApplication(IDObject object, Context context)
	{
		return null;
	}

}
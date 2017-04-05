package wewoAPI;
import com.amazonaws.services.lambda.runtime.Context;

import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import modelPOJO.Application;
import modelPOJO.IDObject;
import modelPOJO.JsonList;
import modelPOJO.ResponseObject;
import DatabaseController.ApplicationDTO;
import DatabaseController.ApplicationRepository;
import DatabaseController.DALException;
import DatabaseController.MySQLApplicationRepository;
import DatabaseController.MySQLTaskRepository;
import DatabaseController.TaskDTO;

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
	
	private void verifyLogin(Context context) throws UnauthorizedException
	{
		if(context.getIdentity() == null || context.getIdentity().getIdentityId().isEmpty())
		{
			throw new UnauthorizedException("Invalid login");
		}	
	}
		
	public JsonList<String> GetApplicants(IDObject taskid, Context context) throws UnauthorizedException, DALException
	{
		MySQLTaskRepository tas = new MySQLTaskRepository();
		
		verifyLogin(context);
		if(tas.getTask(taskid.getID()).getCreatorId() != context.getIdentity().getIdentityId()){
			throw new UnauthorizedException("Du har ikke adgang til dette.");
		}
		try {
			tas.getTaskList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void PostApplications(Application app, Context context) throws UnauthorizedException, NumberFormatException, DALException, BadRequestException
	{
		ApplicationDTO dto = ApplicationDTO.fromModel(app);
//		IDObject newApplierID = new IDObject();
		
		verifyLogin(context);
		
		if(repository.getApplication(Integer.parseInt(app.getApplierid())) != null) {
			throw new BadRequestException("Du har allerede en application på denne task.");
		}
		
		dto.setApplierid(context.getIdentity().getIdentityId());
		
		try {
			Application application = new Application();
			application.setTaskid(dto.getTaskid());
			application.setApplierid(dto.getApplierid());
			application.setApplicationMessage(dto.getApplicationMessage());
			repository.createApplication(dto);
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		return newApplierID;
		
		
	}
	
	public 	ResponseObject<String> GetApplication(IDObject object, Context context)
	{
		return null;
	}

}
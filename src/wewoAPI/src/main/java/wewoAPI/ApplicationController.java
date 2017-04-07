package wewoAPI;
import com.amazonaws.services.lambda.runtime.Context;

import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import modelPOJO.Application;
import modelPOJO.IDObject;
import modelPOJO.JsonList;
import modelPOJO.Task;
import DatabaseController.ApplicationDTO;
import DatabaseController.DALException;
import DatabaseController.MySQLApplicationRepository;
import DatabaseController.MySQLTaskRepository;

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
		
	public JsonList<String> GetApplicants(IDObject taskid, Context context) throws UnauthorizedException
	{
		MySQLTaskRepository tas = new MySQLTaskRepository();
		MySQLApplicationRepository app = new MySQLApplicationRepository();
		
		verifyLogin(context);
		try {
			if(tas.getTask(taskid.getID()).getCreatorId() != context.getIdentity().getIdentityId()){
				throw new UnauthorizedException("Du har ikke adgang til dette.");
			}
			
		} catch (DALException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			JsonList<String> applications = new JsonList<String>();
			applications.setElements(app.getApplicationList(taskid.getID()));
//			for (int i = 0; i < app.getApplicationList(taskid.getID()).size(); i++) {
//				applications.getElements().addAll(i, app.getApplicationList(taskid.getID()));
//			}
			return applications;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		
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
	
	public 	Application GetApplication(Task task, Application application, Context context) throws UnauthorizedException
	{
		ApplicationDTO dto = new ApplicationDTO();
		MySQLTaskRepository tas = new MySQLTaskRepository();
		MySQLApplicationRepository app = new MySQLApplicationRepository();
		
		verifyLogin(context);
		try {
			if(tas.getTask(task.getID()).getCreatorId() != context.getIdentity().getIdentityId() ||
			   app.getApplication(Integer.parseInt(application.getTaskid())).getApplierid() != context.getIdentity().getIdentityId() )
			
			application.setApplicationMessage(dto.getApplicationMessage());
			application.setApplierid(dto.getApplierid());
			application.setTaskid(dto.getTaskid());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return application;
	}

}
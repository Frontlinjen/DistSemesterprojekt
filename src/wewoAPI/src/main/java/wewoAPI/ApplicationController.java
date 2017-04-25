package wewoAPI;
import java.io.InputStream;
import java.io.OutputStream;

import javax.print.attribute.standard.Finishings;

import com.amazonaws.services.lambda.runtime.Context;

import exceptions.*;
import modelPOJO.Application;
import modelPOJO.IDObject;
import modelPOJO.JsonList;
import modelPOJO.Task;
import DatabaseController.ApplicationDTO;
import DatabaseController.ApplicationRepository;
import DatabaseController.DALException;
import DatabaseController.MySQLApplicationRepository;
import DatabaseController.MySQLTaskRepository;
import DatabaseController.TaskDTO;
import wewoAPI.ControllerBase;

public class ApplicationController extends ControllerBase{
	
	ApplicationRepository repository;	
	
	public ApplicationController() throws InternalServerErrorException
	{
		try {
			repository = new MySQLApplicationRepository(); 
		} catch(DALException e) {
			e.printStackTrace();
			throw new InternalServerErrorException("Failed to connect to database");
		}
	
	}
	
	public ApplicationController(ApplicationRepository repository)
	{
		this.repository = repository;
	}
		
	public JsonList<String> GetApplicants(IDObject taskid, Context context) throws UnauthorizedException, DALException
	{
		MySQLTaskRepository tas = new MySQLTaskRepository();
		MySQLApplicationRepository app = new MySQLApplicationRepository();
		
		verifyLogin(context);
		try {
			if(tas.getTask(taskid.getID()).getCreatorId() != context.getIdentity().getIdentityId()){
				throw new UnauthorizedException("Du har ikke adgang til dette.");
			}
			
		} catch (DALException e1) {
			e1.printStackTrace();
		}
		try {
			JsonList<String> applications = new JsonList<String>();
			applications.setElements(app.getApplicationList(taskid.getID()));
			return applications;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	public void PostApplications(InputStream in, OutputStream out, Context context) throws InternalServerErrorException
	{
		
		try{
			if(!verifyLogin(context)){
				raiseError(out, 401, "Not logged in");
				return;
			}
			StartRequest(in);
			Application app = request.getObject(Application.class);
			if(app == null){
				raiseError(out, 400, "Invalid Application Object");
				return;
			}
			ApplicationDTO dto = ApplicationDTO.fromModel(app);
			dto.setApplierid(context.getIdentity().getIdentityId());
			
			try {
				repository.createApplication(dto);
				response.addResponseObject("ApplicationID", dto.getApplierid());
				response.setStatusCode(200);
				FinishRequest(out);
				return;
			} catch (DALException e) {
				raiseError(out, 504, "Database unavailable");
				return;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			raiseError(out, 500, "(╯°□°）╯︵ ┻━┻");
			return;
		}		
	}
	
	public void GetApplication(InputStream in, OutputStream out, Context context) throws InternalServerErrorException
	{
		
		try {
			StartRequest(in);
			
			int applierID;
			try{
				applierID = Integer.parseInt(request.getPath("applierID"));			
			}
			catch(NumberFormatException neg){
				raiseError(out, 400, "No ApplierID specified on path");
				return;
			}
			ApplicationDTO dto;
			dto = repository.getApplication(applierID);
			if(dto==null)
			{
				raiseError(out, 404, "No application was found using ID " + applierID);
				return;
			}
			
			Application app = dto.getModel();
			response.addResponseObject("Application", app);
			response.setStatusCode(200);
			FinishRequest(out);
		}catch (DALException e) {
			raiseError(out, 503, "Database unavailable");
			return;
		}
	}

	public void updateApplication(InputStream in, OutputStream out, Context context) throws InternalServerErrorException
	{
		if(!verifyLogin(context)){
			raiseError(out, 401, "Not logged in");
			return;
		}
		
		try {
			StartRequest(in);
			Application app = request.getObject(Application.class);
			int applierID;
			try{
				applierID = Integer.parseInt(request.getPath("applierID"));
			}
			catch(Exception e)
			{
				raiseError(out, 400, "No applierID specified");
				return;
			}
			app.setApplierid(applierID);
			ApplicationDTO dto = repository.getApplication(app.getTaskid());
	
			if(dto == null)
			{
				raiseError(out, 404, "No application was found using ID " + app.getApplierid());
				return;
			}
			if(dto.getApplierid().equals(context.getIdentity().getIdentityId())){
				dto = ApplicationDTO.fromModel(app);
				repository.updateApplication(dto);
				response.setStatusCode(200);
				FinishRequest(out);
				return;
			}
			else
			{
				raiseError(out, 403, "User does not own that application");
				return;
			}
		} catch (DALException e) {
			raiseError(out, 503, "Database unavailable");
			return;
		}
	}
	
	public void deleteApplication(InputStream in, OutputStream out, Context context) throws InternalServerErrorException
	{
		if(!verifyLogin(context)){
			raiseError(out, 401, "Not logged in");
		}	
		try {
			StartRequest(in);
			int applierID;
			try{
				applierID = Integer.parseInt(request.getPath("applierID"));
			}
			catch(NumberFormatException eng)
			{
				raiseError(out, 400, "No applier specified");
				return;
			}
			ApplicationDTO app = repository.getApplication(applierID);
			if(app == null)
			{
				raiseError(out, 404, "No such application");
				return;
			}
			
			if(app.getApplierid().equals(context.getIdentity().getIdentityId())){
				repository.deleteApplication(applierID);
				response.setStatusCode(200);
				FinishRequest(out);
				return;
			}
			else
			{
				raiseError(out, 403, "User does not own that application");
				return;
			}
		} catch (DALException e) {
			raiseError(out, 503, "Database unavailable");
		}
	}
}
package wewoAPI;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.print.attribute.standard.Finishings;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

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
import DatabaseController.TaskRespository;
import wewoAPI.ControllerBase;

public class ApplicationController extends ControllerBase{
	
	ApplicationRepository repository;
	TaskRespository taskRepository;
	
	public ApplicationController() throws InternalServerErrorException
	{
		try {
			repository = new MySQLApplicationRepository();
			taskRepository = new MySQLTaskRepository();
		} catch(DALException e) {
			e.printStackTrace();
			throw new InternalServerErrorException("Failed to connect to database");
		}
	
	}
	
	public ApplicationController(ApplicationRepository repository, TaskRespository taskRep)
	{
		this.repository = repository;
		this.taskRepository = taskRep;
	}
		
	public void getApplicants(InputStream in, OutputStream out, Context context) throws UnauthorizedException, DALException, InternalServerErrorException
	{
					
		try {
			StartRequest(in, context);
			if(userID == null){
				raiseError(out, 401, "Not logged in");
				return;
			}
			
			String taskStr = request.getPath("taskID");
			int taskid;
			
				try{
					taskid = Integer.parseInt(taskStr);
				}catch(NumberFormatException e){
					raiseError(out, 400, "Invalid taskID or applierID specified");					
					return;
				}
				
			TaskDTO task = taskRepository.getTask(taskid);
			if(task == null){
				raiseError(out, 400, "Task does not exists");
				return;
			}
			if(!task.getCreatorId().equals(userID)){
				raiseError(out, 401, "Du har ikke adgang til dette.");
				return;
			}
			else{
				try {
					List<String> applicants = repository.getApplicationList(taskid);
					response.addResponseObject("applicants", applicants);
					response.addResponseObject("task", "tasks/"+task.getId());
					response.setStatusCode(200);
					FinishRequest(out);
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} catch (DALException e1) {
			e1.printStackTrace();
		}
		return;
		
		
	}
	
	public void createApplications(InputStream in, OutputStream out, Context context) throws InternalServerErrorException
	{
		
		try{
			StartRequest(in, context);
			if(userID == null){
				raiseError(out, 401, "Not logged in");
				return;
			}
			
			String taskStr = request.getPath("taskID");
			if(taskStr == null || taskStr.isEmpty()){
				raiseError(out, 400, "No taskID specified");
				return;
			}
			int taskId;
			try{
				taskId = Integer.parseInt(taskStr);
			}catch(NumberFormatException e){
				raiseError(out, 400, "Invalid taskID specified");
				return;
			}
			Application app = null;
			try{
				app = request.getObject(Application.class);
			}catch(UnrecognizedPropertyException ex){
				raiseError(out, 400, "" + ex.getPropertyName());
				return;
			}
			if(app == null){
				raiseError(out, 400, "No message body recieved");
				return;
			}
			ApplicationDTO dto = ApplicationDTO.fromModel(app);
			dto.setTaskid(taskId);
			dto.setApplierid(userID);
			
			TaskDTO taskDTO = new TaskDTO();
			
			try {
				repository.createApplication(dto);
				response.setStatusCode(201);
				response.addHeader("Created", "/tasks/"+dto.getTaskid()+"/applicants/"+dto.getApplierid());
				response.addResponseObject("task", "tasks/applicants/"+taskDTO.getId());
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
	
	public void getApplication(InputStream in, OutputStream out, Context context) throws InternalServerErrorException, UnauthorizedException, DALException
	{
		
		try {
			String applierID;
			int taskID;
			
			StartRequest(in, context);
			if(userID == null){
				raiseError(out, 401, "Not logged in");
				return;
			}
			try{
				taskID = Integer.parseInt(request.getPath("taskID"));
				applierID = request.getPath("applierID");			
			}
			catch(NumberFormatException neg){
				raiseError(out, 400, "Invalid path id");
				return;
			}
			ApplicationDTO dto;
			dto = repository.getApplication(applierID, taskID);
			if(dto==null)
			{
				raiseError(out, 403, "Application does not exist.");
				return;
			}
			try{
				TaskDTO task = taskRepository.getTask(taskID);
				if(task == null){
					raiseError(out, 400, "Task does not exists");
					return;
				}
				if(!task.getCreatorId().equals(userID) && !applierID.equals(userID)){
					raiseError(out, 401, "Du har ikke adgang til dette.");
					return;
				}
				Application app = dto.getModel();
				response.addResponseObject("Application", app);
				response.setStatusCode(200);
				FinishRequest(out);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}catch (DALException e) {
			raiseError(out, 503, "Database unavailable");
			return;
		}
	}

	public void updateApplication(InputStream in, OutputStream out, Context context) throws InternalServerErrorException
	{
		try {
			StartRequest(in, context);
			if(userID == null){
				raiseError(out, 401, "Not logged in");
				return;
			}
			
			Application app = null;
			try{
				app = request.getObject(Application.class);
			}catch(UnrecognizedPropertyException ex){
				raiseError(out, 400, "" + ex.getPropertyName());
				return;
			}
			int taskID;
			String applierID;
			try{
				taskID = Integer.parseInt(request.getPath("taskID"));
				applierID = request.getPath("applierID");
			}
			catch(Exception e)
			{
				raiseError(out, 400, "No applierID specified");
				return;
			}
			app.setApplierId(userID);
			app.setTaskId(taskID);
			ApplicationDTO dto = repository.getApplication(userID, taskID);
	
			if(dto == null)
			{
				raiseError(out, 404, "No application was found using ID " + app.getApplierId());
				return;
			}
			if(dto.getApplierid().equals(userID)){
				dto = ApplicationDTO.fromModel(app);
				if(dto.getApplierid().equals(app.getApplierId()) && dto.getTaskid() == app.getTaskId()){
					repository.updateApplication(dto);
					response.setStatusCode(200);
					FinishRequest(out);
					return;
				} else{
					dto.setApplicationMessage(app.getApplicationMessage());
					dto.setApplierid(applierID);
					dto.setTaskid(taskID);
					repository.updateApplication(dto);
					response.setStatusCode(200);
					FinishRequest(out);
					return;
				}
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
		try {
			
			StartRequest(in, context);
			if(userID == null){
				raiseError(out, 401, "Not logged in");
			}
			
			String applierID;
			int taskID;
			try{
				applierID = request.getPath("applierID");
				taskID = Integer.parseInt(request.getPath("taskID"));
			}
			catch(NumberFormatException eng)
			{
				raiseError(out, 400, "No applier specified");
				return;
			}
			ApplicationDTO app = repository.getApplication(applierID, taskID);
			if(app == null)
			{
				raiseError(out, 404, "No such application");
				return;
			}
			
			if(app.getApplierid().equals(userID)){
				repository.deleteApplication(applierID, taskID);
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
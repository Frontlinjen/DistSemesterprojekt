package wewoAPI;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;

import DatabaseController.DALException;
import DatabaseController.MySQLTaskRepository;
import DatabaseController.TaskRespository;
import DatabaseController.TaskDTO;
import exceptions.*;
import modelPOJO.IDObject;
import modelPOJO.Task;
import modelPOJO.FindDataObject;;

public class TaskController extends ControllerBase{
	TaskRespository repository;
	
	public TaskController() throws InternalServerErrorException
	{
		try {
			repository = new MySQLTaskRepository();
		} catch (DALException e) {
			e.printStackTrace();
			throw new InternalServerErrorException("Failed to connect to database");
		}
	}
	
	public TaskController(TaskRespository repository)
	{
		this.repository = repository;
	}
		 
	public void createTask(InputStream in, OutputStream out, Context context) throws InternalServerErrorException
	{
		try{
			if(!verifyLogin(context)){
				raiseError(out, 401, "Not logged in");
				return;
			}
			StartRequest(in);
			Task task = request.getObject(Task.class);
			if(task == null){
				raiseError(out, 400, "Invalid Task Object\n Got: " + request.getObject(String.class));
				return;
			}
			TaskDTO dto = TaskDTO.fromModel(task);
			dto.setCreatorId(context.getIdentity().getIdentityId());

			HttpRequestFactory factory = new NetHttpTransport().createRequestFactory();
			
			GenericUrl url = new GenericUrl("https://dawa.aws.dk/adresser");
			url.put("postnr", dto.getZipaddress());
			url.put("vejnavn", dto.getStreet());
			HttpResponse addressLookupResponse = null;
			try{
				HttpRequest addressLookup = factory.buildGetRequest(url);
				addressLookupResponse  = addressLookup.execute();
			}catch(Exception e){
				e.printStackTrace();
				//Ignore the error and accept the address regardless
			}
			if(addressLookupResponse != null && addressLookupResponse.isSuccessStatusCode() && addressLookupResponse.getContent() != null){
				InputStream stream = null;
				try{
					stream = addressLookupResponse.getContent();
					ObjectMapper mapper = new ObjectMapper();
					JsonNode n = mapper.readTree(stream);
					if(n.isArray() && !n.elements().hasNext()){ //If format changes and we do not recieve an array, then we shouldn't disallow the user to create the task
						raiseError(out, 400, "Invalid address specified");
						return;
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally{
					if(stream != null)
						stream.close();
				}
			}
			
			try {
				repository.createTask(dto);
				response.addResponseObject("TaskID", dto.getId());
				response.setStatusCode(201);
				response.addHeader("Created", "/tasks/"+dto.getId());
				FinishRequest(out);
				return;
			} 
			catch(DALException.ForeignKeyException e){
				raiseError(out, 400, "Invalid tag specified");
				return;
			}
			catch (DALException e) {
				raiseError(out, 503, "Database unavailable");
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
	
	//See: https://github.com/Hjorthen/Bubble/blob/master/AuthTest/Repositories/TaskRepository.cs#L74
	public void findTasks(InputStream in, OutputStream out, Context context) throws InternalServerErrorException
	{
		try{
			StartRequest(in);
			String tags = request.getQuery("tags");
			if(tags == null){
				raiseError(out, 400, "No tags specified");
				return;
			}
				
				String[] tag = tags.split("\\+");
				List<Integer> tagIds = new ArrayList<Integer>(tag.length);
				
				for (String tagStr : tag) {
					try{
						tagIds.add(Integer.parseInt(tagStr));
					}
					catch(java.lang.NumberFormatException e){
						//Ignore invalid ids
					}
				}
				if(tagIds.size()==0){
					raiseError(out, 400, "No valid tagIds");
					return;
				}
				List<TaskDTO> tasks = repository.queryTasks(tagIds);
				if(tasks == null || tasks.isEmpty())
				{
					raiseError(out, 404, "No tasks were found");
					return;
				}
				List<Task> models = new ArrayList<Task>(tasks.size());
				for (int i = 0; i < tasks.size(); i++) {
					models.add(tasks.get(i).getModel());
				}
				response.addResponseObject("Results", models);
				response.setStatusCode(200);
				FinishRequest(out);
				return;
		}catch (DALException e) {
			raiseError(out, 503, "Database unavailable");
			return;
		}
	}
	
	
	public void getTask(InputStream in, OutputStream out, Context context) throws InternalServerErrorException
	{
		
		try {
			StartRequest(in);
			
			int taskID;
			try{
				taskID = Integer.parseInt(request.getPath("taskID"));			
			}
			catch(NumberFormatException neg){
				raiseError(out, 400, "No taskID specified on path");
				return;
			}
			TaskDTO dto;
			dto = repository.getTask(taskID);
			if(dto==null)
			{
				raiseError(out, 404, "No task was found using ID " + taskID);
				return;
			}
			
			Task task = dto.getModel();
			response.addResponseObject("Task", task);
			response.setStatusCode(200);
			FinishRequest(out);
		}catch (DALException e) {
			raiseError(out, 503, "Database unavailable");
			return;
		}
	}
	
	//PUT /task/{ID}
	public void updateTask(InputStream in, OutputStream out, Context context) throws InternalServerErrorException
	{
		if(!verifyLogin(context)){
			raiseError(out, 401, "Not logged in");
			return;
		}
		
		try {
			StartRequest(in);
			Task task = request.getObject(Task.class);
			int taskID;
			try{
				taskID = Integer.parseInt(request.getPath("taskID"));
			}
			catch(Exception e)
			{
				raiseError(out, 400, "No taskID specified");
				return;
			}
			task.setID(taskID);
			TaskDTO dto = repository.getTask(task.getID());
			if(dto == null)
			{
				raiseError(out, 404, "No task was found using ID " + task.getID());
				return;
			}
			if(dto.getCreatorId().equals(context.getIdentity().getIdentityId())){
				dto = TaskDTO.fromModel(task);
				repository.updateTask(dto);
				response.setStatusCode(200);
				FinishRequest(out);
				return;
			}
			else
			{
				raiseError(out, 403, "User does not own that task");
				return;
			}
		} catch (DALException e) {
			raiseError(out, 503, "Database unavailable");
			return;
		}
	}
	
	public void deleteTask(InputStream in, OutputStream out, Context context) throws InternalServerErrorException
	{
		if(!verifyLogin(context)){
			raiseError(out, 401, "Not logged in");
		}	
		try {
			StartRequest(in);
			int taskId;
			try{
				taskId = Integer.parseInt(request.getPath("taskID"));
			}
			catch(NumberFormatException eng)
			{
				raiseError(out, 400, "No taskID specified");
				return;
			}
			TaskDTO task = repository.getTask(taskId);
			if(task == null)
			{
				raiseError(out, 404, "No such task");
				return;
			}
			
			if(task.getCreatorId().equals(context.getIdentity().getIdentityId())){
				repository.deleteTask(taskId);
				response.setStatusCode(200);
				FinishRequest(out);
				return;
			}
			else
			{
				raiseError(out, 403, "User does not own that task");
				return;
			}
		} catch (DALException e) {
			raiseError(out, 503, "Database unavailable");
		}
	}
}

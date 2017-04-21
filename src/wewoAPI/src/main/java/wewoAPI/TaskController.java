package wewoAPI;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;

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
	
	public TaskController()
	{
		repository = new MySQLTaskRepository();
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
			}
			StartRequest(in);
			Task task = request.getObject(Task.class);
			if(task == null){
				raiseError(out, 400, "Invalid Task Object");
			}
			TaskDTO dto = TaskDTO.fromModel(task);
			dto.setCreatorId(context.getIdentity().getIdentityId());

			try {
				repository.createTask(dto);
				response.addResponseObject("TaskID", dto.getId());
				response.setStatusCode(200);
				FinishRequest(out);
			} catch (DALException e) {
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
	
	//See: https://github.com/Hjorthen/Bubble/blob/master/AuthTest/Repositories/TaskRepository.cs#L74
	public List<Task> findTasks(FindDataObject findData, Context context)
	{
		
		return null;
	}
	
	
	public void getTask(InputStream in, OutputStream out, Context context) throws InternalServerErrorException
	{
		
		try {
			StartRequest(in);
			int taskID = request.getObject("TaskID", Integer.class);
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
		}
		
		try {
			StartRequest(in);
			Task task = request.getObject(Task.class);
			TaskDTO dto = repository.getTask(task.getID());
			if(dto == null)
			{
				raiseError(out, 404, "No task was found using ID " + task.getID());
				return;
			}
			if(dto.getCreatorId().equals(context.getIdentity().getIdentityId())){
				
				Date date = new Date(System.currentTimeMillis());
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
			int taskId = request.getObject("TaskID", Integer.class);
			TaskDTO task = repository.getTask(taskId);
			if(task == null)
			{
				raiseError(out, 404, "No such task");
				return;
			}
			
			if(task.getCreatorId().equals(context.getIdentity().getIdentityId())){
				repository.deleteTask(taskId);
				response.setStatusCode(200);
				return;
			}
			else
			{
				raiseError(out, 401, "User does not own that task");
				return;
			}
		} catch (DALException e) {
			raiseError(out, 503, "Database unavailable");
		}
	}
}

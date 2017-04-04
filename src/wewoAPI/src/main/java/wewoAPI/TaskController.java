package wewoAPI;

import java.sql.Date;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;

import DAOException.DALException;
import DatabaseController.MySQLTaskRespository;
import DatabaseController.TaskRespository;
import DatabaseController.TaskDTO;
import exceptions.*;
import modelPOJO.IDObject;
import modelPOJO.Task;
import modelPOJO.FindDataObject;;

public class TaskController {
	TaskRespository repository;
	
	public TaskController()
	{
		repository = new MySQLTaskRespository();
	}
	
	public TaskController(TaskRespository repository)
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
	 
	public IDObject createTask(Task task, Context context) throws UnauthorizedException
	{
		verifyLogin(context);
		
		IDObject newTaskID = new IDObject();
		
		TaskRespository dao = new MySQLTaskRespository();
		task.setCreatorid(context.getIdentity().getIdentityId());
		TaskDTO dto = TaskDTO.fromModel(task);
		
		try {
			dao.createTask(dto);
			newTaskID.setID(dto.getId());
			return newTaskID;
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newTaskID;
	}
	
	//See: https://github.com/Hjorthen/Bubble/blob/master/AuthTest/Repositories/TaskRepository.cs#L74
	public List<Task> findTasks(FindDataObject findData, Context context)
	{
		
		return null;
	}
	
	
	public Task getTask(IDObject id, Context context) throws NotFoundException
	{
		TaskRespository dao = new MySQLTaskRespository();
		TaskDTO dto;
		try {
			dto = dao.getTask(id.getID());
			Task task = dto.getModel();
			if(task==null)
				throw new NotFoundException("No such task");
			return task;
		
		}catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//PUT /task/{ID}
	public void updateTask(Task task, Context context) throws NotFoundException, ForbiddenException, UnauthorizedException
	{
		verifyLogin(context);
		TaskRespository dao = new MySQLTaskRespository();
		
		try {
			TaskDTO dto = dao.getTask(task.getID());
			if(dto == null)
			{
				throw new NotFoundException("No such task");
			}
			if(dto.getCreatorId().equals(context.getIdentity())){
				
				Date date = new Date(System.currentTimeMillis());
				dto = new TaskDTO()
						.setTitle(task.getTitle())
						.setDescription(task.getDescription())
						.setPrice(task.getPrice())
						.setEct(task.getETC())
						.setSupplies(task.isSupplies() ? 1 : 0)
						.setUrgent(task.isUrgent() ? 1 : 0)
						.setStreet(task.getStreet())
						.setZipaddress(task.getZipaddress());
				dao.updateTask(dto);
			}
			else
			{
				throw new ForbiddenException("Insuffecient access rights");
			}
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int deleteTask(IDObject id, Context context) throws ForbiddenException, NotFoundException, UnauthorizedException
	{
		verifyLogin(context);
		TaskRespository dao = new MySQLTaskRespository();
		
		try {
			TaskDTO task = dao.getTask(id.getID());
			if(task == null)
				throw new NotFoundException("The specified task were not found");
			
			if(task.getCreatorId().equals(context.getIdentity())){
				dao.deleteTask(id.getID());
			}
			else
			{
				throw new ForbiddenException("Insuffecient access rights");
			}
		} catch (DALException e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
}

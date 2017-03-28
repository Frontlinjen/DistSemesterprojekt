package wewoAPI;

import java.sql.Date;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;

import DatabaseController.DALException;
import DatabaseController.MySQLTaskRespository;
import DatabaseController.TaskRespository;
import DatabaseController.TaskDTO;
import exceptions.UnauthorizedException;
import modelPOJO.IDObject;
import modelPOJO.Task;
import modelPOJO.FindDataObject;;

public class TaskController {
	//Initialize database connection
	 
	public IDObject createTask(Task task, Context context) throws UnauthorizedException
	{
		if(context.getIdentity() == null || context.getIdentity().getIdentityId().isEmpty())
		{
			throw new UnauthorizedException();
		}
		
		IDObject newTaskID = new IDObject();
		
		TaskRespository dao = new MySQLTaskRespository();
		Date date = new Date(System.currentTimeMillis());
		TaskDTO dto = new TaskDTO(
				newTaskID.getID(),
				task.getTitle(),
				task.getDescription(),
				task.getPrice(),
				task.getETC(),
				task.isSupplies(),
				task.isUrgent(),
				task.getViews(), 
				task.getStreet(),
				task.getZipaddress(),
				date,
				date,
				context.getIdentity().getIdentityId()
				);
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
	
	
	public Task getTask(IDObject id, Context context)
	{
		TaskRespository dao = new MySQLTaskRespository();
		TaskDTO dto;
		try {
			dto = dao.getTask(id.getID());
			Task task = new Task();
					//dto.getTags() //Mangler tags fra DTO
					
			task.setCreatorid(dto.getCreatorId());
			task.setDescription(dto.getDescription());
			task.setETC(dto.getEct());
			task.setID(dto.getId());
			task.setPrice(dto.getPrice());
			task.setStreet(dto.getStreet());
			task.setSupplies(dto.getSupplies());
			task.setTitle(dto.getTitle());
			task.setUrgent(dto.getUrgent());
			
			
			return task;
			
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//PUT /task/{ID}
	public void updateTask(Task task, Context context)
	{
		
		TaskRespository dao = new MySQLTaskRespository();
		
		try {
			TaskDTO dto = dao.getTask(task.getID());
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
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int deleteTask(IDObject id, Context context)
	{
		TaskRespository dao = new MySQLTaskRespository();
		
		try {
			TaskDTO task = dao.getTask(id.getID());
			if(task.getCreatorId().equals(context.getIdentity())){
				dao.deleteTask(id.getID());
			}
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}
		return 0;
	}
}

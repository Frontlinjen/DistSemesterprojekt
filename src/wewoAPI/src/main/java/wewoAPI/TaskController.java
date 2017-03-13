package wewoAPI;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;
import java.util.List;
import java.util.Random;

import javax.security.auth.callback.Callback;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.util.JSONPObject;

import DatabaseController.DALException;
import DatabaseController.MySQLTaskDAO;
import DatabaseController.TaskDAO;
import DatabaseController.TaskDTO;
import modelPOJO.IDObject;
import modelPOJO.Task;
import modelPOJO.FindDataObject;;

public class TaskController {
	//Initialize database connection
	 
	public IDObject createTask(Task task, Context context)
	{
		Random random = new Random();
		int ID = random.nextInt(16);
		StringBuilder taskID = new StringBuilder();
		for (int i = 0; i < ID; i++) {
			taskID.append((char)(65+random.nextInt(20)));
		}
		IDObject newTaskID = new IDObject();
		newTaskID.setID(taskID.toString());
		
		TaskDAO dao = new MySQLTaskDAO();
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
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newTaskID;
	}
	
	//See: https://github.com/Hjorthen/Bubble/blob/master/AuthTest/Repositories/TaskRepository.cs#L74
	public List<Task> findTasks(FindDataObject findData, Context context)
	{
		TaskDAO dao = new MySQLTaskDAO();
		List<TaskDTO> DTOtasks;
		try {
			DTOtasks = dao.getTaskList();
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		List<Task> tasks = new ArrayList<Task>();
		for(int i = findData.getIndex(); i < findData.getMax(); i++){
			TaskDTO DTOTask = DTOtasks.get(i); //Check om har tag
			Task task = new Task();
			task.setCreatorid(DTOTask.getCreatorId());
			task.setDescription(DTOTask.getDescription());
			task.setETC(DTOTask.getEct());
			task.setID(DTOTask.getId());
			task.setPrice(DTOTask.getPrice());
			task.setStreet(DTOTask.getStreet());
			task.setSupplies(DTOTask.getSupplies());
			//task.setTags(Arrays.asList(tags)); //Forkert?
			task.setTitle(DTOTask.getTitle());
			task.setUrgent(DTOTask.getUrgent());
			task.setViews(DTOTask.getViews());
			task.setZipaddress(DTOTask.getZipaddress());
			tasks.add(task);
		}
		return tasks;
	}
	
	public Task getTask(IDObject id, Context context)
	{
		TaskDAO dao = new MySQLTaskDAO();
		TaskDTO dto;
		try {
			dto = dao.getTask(id.getID());
			Task task = new Task(
					dto.getId(),
					dto.getTitle(),
					dto.getDescription(),
					dto.getPrice(),
					dto.getEct(),
					dto.getSupplies(),
					dto.getUrgent(),
					dto.getViews(), 
					dto.getStreet(),
					dto.getZipaddress(),
					dto.getCreatorId()
					//dto.getTags() //Mangler tags fra DTO
					
			);
			
			return task;
			
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//PUT /task/{ID}
	public int updateTask(Task task, Context context)
	{
		
		TaskDAO dao = new MySQLTaskDAO();
		Date date = new Date(System.currentTimeMillis());
		TaskDTO dto = new TaskDTO(
				task.getID(),
				task.getTitle(),
				task.getDescription(),
				task.getPrice(),
				task.getETC(),
				task.isSupplies(),
				task.isUrgent(),
				task.getViews(), 
				task.getStreet(),
				task.getZipaddress(),
				date,//Erstat med gammel dato
				date,
				context.getIdentity().getIdentityId()
				);
		try {
			dto = (TaskDTO) context.getIdentity();
			if(dto.getCreatorId().equals(context.getIdentity())){
				dao.updateTask(dto);
			}
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}
		return 0;
	}
	
	public int deleteTask(IDObject id, Context context)
	{
		TaskDAO dao = new MySQLTaskDAO();
		
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

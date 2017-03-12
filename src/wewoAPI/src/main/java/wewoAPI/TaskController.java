package wewoAPI;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.amazonaws.services.lambda.runtime.Context;

import modelPOJO.IDObject;
import modelPOJO.Task;

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
		return newTaskID;
	}
	
	//See: https://github.com/Hjorthen/Bubble/blob/master/AuthTest/Repositories/TaskRepository.cs#L74
	public List<Task> findTasks(int[] tags, int index, int max, Context context)
	{
		return null;
	}
	
	public Task getTask(IDObject id)
	{
		Task task = new Task();
		task.setCreatorid(id.getID());
		task.setPrice(22);
		task.setTitle("Test");
		task.setTags(Arrays.asList(2,4,5,1));
		return task;
	}
	
	//PUT /task/{ID}
	public int updateTask(String str, Task task, Context context)
	{
		return 0;
	}
	
	public int deleteTask(String str, Context context)
	{
		return 0;
	}
	
	
	
	
}

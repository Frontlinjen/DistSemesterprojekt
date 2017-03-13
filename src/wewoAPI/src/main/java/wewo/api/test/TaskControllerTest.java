package wewo.api.test;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import wewoAPI.TaskController;
import modelPOJO.*;

public class TaskControllerTest {

	public static void main (String[]args){
		List<Integer> tags = new ArrayList<Integer>();
		tags.add(5);
		tags.add(6);
		tags.add(3);
		
		TaskController TC = new TaskController();
		Task task = new Task ();
		//task.setID("ID");
		task.setCreatorid("creatorid");
		task.setDescription("description");
		task.setETC(10);
		task.setPrice(200);
		task.setStreet("street");
		task.setSupplies(1);
		task.setTags(tags);
		task.setTitle("title");
		task.setUrgent(1);
		IDObject ID = new IDObject();
		ID.setID(7);
		FindDataObject FD = new FindDataObject();
		
		
		
		FD.setTags(tags);
		FD.setIndex(4);
		FD.setMax(10);
		ContextTest cont = new ContextTest();
		
		
		//TC.createTask(task, cont);
		
		TC.getTask(ID, cont);
		
		//TC.findTasks(FD, cont);
		
		//TC.updateTask(task, cont);
		
		//TC.deleteTask(ID, cont);
		
	}
}

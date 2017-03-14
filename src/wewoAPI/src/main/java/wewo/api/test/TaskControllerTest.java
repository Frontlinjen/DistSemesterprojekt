package wewo.api.test;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import wewoAPI.TaskController;
import modelPOJO.*;

public class TaskControllerTest {

	public static void main (String[]args){
		List<Integer> tags = new ArrayList<Integer>();
		tags.add(2);
		tags.add(4);
		
		TaskController TC = new TaskController();
		Task task = new Task ();
		//task.setID("ID");
		task.setCreatorid("fk3");
		task.setDescription("test2");
		task.setETC(10);
		task.setPrice(200);
		task.setStreet("anker engelundsvej");
		task.setSupplies(1);
		task.setTags(tags);
		task.setTitle("Test");
		task.setUrgent(1);
		IDObject ID = new IDObject();
		ID.setID(7);
		FindDataObject FD = new FindDataObject();
		
		
		
		FD.setTags(tags);
		ContextTest cont = new ContextTest();
		
		
		//TC.createTask(task, cont);
		Task t = TC.getTask(ID, cont);
		
		t.getTags();
		//TC.findTasks(FD, cont);
		
		//TC.updateTask(task, cont);
		
		//TC.deleteTask(ID, cont);
		
	}
}

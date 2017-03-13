package wewo.api.test;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import wewoAPI.TaskController;
import modelPOJO.*;

public class TaskControllerTest {

	public static void main (String[]args){
		TaskController TC = new TaskController();
		Task task = new Task ("ID", "Title", "Description", 10, 3, true, true, 10, "Street", 2000, "creatorID");
		IDObject ID = new IDObject();
		ID.setID("ID");
		FindDataObject FD = new FindDataObject();
		List<Integer> tags = new ArrayList<Integer>();
		tags.add(5);
		tags.add(6);
		tags.add(3);
		
		
		FD.setTags(tags);
		FD.setIndex(4);
		FD.setMax(10);
		ContextTest cont = new ContextTest();
		
		
		TC.createTask(task, cont);
		
		TC.getTask(ID, cont);
		
		TC.findTasks(FD, cont);
		
		TC.updateTask(task, cont);
		
		TC.deleteTask(ID, cont);
		
	}
}

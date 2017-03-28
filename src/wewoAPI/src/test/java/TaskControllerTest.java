import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import exceptions.UnauthorizedException;
import mockRepositories.MockTaskRepository;
import modelPOJO.IDObject;
import modelPOJO.Task;
import wewo.api.test.ContextTest;
import wewoAPI.TaskController;

public class TaskControllerTest {
	TaskController controller;
	ContextTest context;
	int dataCounter = 0;
	@Before
	public void setUp() throws Exception {
		controller = new TaskController(new MockTaskRepository());
		context = new ContextTest("Jeiner");
		controller.createTask(generateTestData(), context);
	}

	private Task generateTestData()
	{
		Task task = new Task();
		task.setDescription("Test" + ++dataCounter);
		task.setTitle("Title" + dataCounter);
		task.setETC(30);
		task.setPrice(55);
		task.setStreet("Nowhere");
		return task;
	}
	
	
	@Test
	public void createTask() {
		Task task = generateTestData();
		try {
			task.setID(-1);
			task.setCreatorid("Nobody");
			IDObject id = controller.createTask(task, context);
			assertNotNull(id);
			assertTrue(id.getID() > 0);
			
			Task newTask = controller.getTask(id, context);
			assertEquals(newTask.getCreatorid(), context.getIdentity().getIdentityId());
			
		} catch (UnauthorizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Test
	public void createTaskWithoutValidLogin(){
		Task task = generateTestData();
		context.setIdentity("");
		try {
			controller.createTask(task, context);
			assertTrue(false); //Exception should always be thrown
		} catch (UnauthorizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void createTaskWithoutLogin(){
		Task task = generateTestData();
		context.clearIdentity();
		try {
			controller.createTask(task, context);
			assertTrue(false); //Exception should always be thrown
		} catch (UnauthorizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getNonexistingTask(){
		IDObject id = new IDObject();
		id.setID(666);
		Task task = controller.getTask(id, context);
		assertNull(task);
	}
	
	@Test
	public void deleteTask(){
		IDObject id = new IDObject();
		id.setID(0);
		controller.deleteTask(id, context);
	}
	
	@Test
	public void deleteTaskCheckPermissions(){
		try {
			IDObject id = new IDObject();
			id.setID(0);
			context.setIdentity("Jeiner22");
			controller.deleteTask(id, context);
			assertTrue(false);
		} catch (UnauthorizedException e) {
			
		}
	}
	
	@Test
	public void deleteNonexistingTask(){
		try {
			IDObject id = new IDObject();
			id.setID(666);
			controller.deleteTask(id, context);
		} catch (UnauthorizedException e) {
			
		}
	}
	
	@Test
	public void updateTask(){
		Task task = generateTestData();
		try{
			task.setID(0);
			task.setCreatorid("Nobody");
			task.setTitle("Update");
			controller.updateTask(task, context);
			
			IDObject id = new IDObject();
			id.setID(0);
			Task getTask = controller.getTask(id, context);
			assertNotNull(getTask);
			assertNotEquals(task.getCreatorid(), getTask.getCreatorid());
			assertEquals(task.getTitle(), getTask.getTitle());
		}
		catch(UnauthorizedException e)
		{
			
		}
	}
	
	@Test
	public void updateWrongPermissionsTask(){
		Task task = generateTestData();
		context.setIdentity("Alice");
		try{
			task.setID(0);
			task.setCreatorid("Nobody");
			task.setTitle("Update");
			controller.updateTask(task, context);
			assertTrue(false);
		}
		catch(UnauthorizedException e)
		{
			
		}
	}
	@Test
	public void updateNoPermissionsTask(){
		Task task = generateTestData();
		context.clearIdentity();
		try{
			task.setID(0);
			task.setCreatorid("Nobody");
			task.setTitle("Update");
			controller.updateTask(task, context);
			assertTrue(false);
		}
		catch(UnauthorizedException e)
		{
			
		}
	}
	

}

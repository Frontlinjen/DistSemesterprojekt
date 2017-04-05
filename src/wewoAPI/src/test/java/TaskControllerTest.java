import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import exceptions.ForbiddenException;
import exceptions.NotFoundException;
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
		task.setDescription("Test" + dataCounter++);
		task.setTitle("Title" + dataCounter);
		task.setETC(30);
		task.setPrice(55);
		task.setStreet("Nowhere");
		task.setCreatorid("TestAcc666");
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
			assertTrue(id.getID() >= 0);
			
			Task newTask;
			try {
				newTask = controller.getTask(id, context);
				assertEquals(newTask.getCreatorid(), context.getIdentity().getIdentityId());
			} catch (NotFoundException e) {
				fail("Task was not created");
			}
			
		} catch (UnauthorizedException e) {
			fail("User was not authorized");
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
		}
	}
	
	@Test
	public void createTaskWithoutLogin(){
		Task task = generateTestData();
		context.clearIdentity();
		try {
			controller.createTask(task, context);
			fail("Exception should have been thrown");
		} catch (UnauthorizedException e) {
			
		}
	}
	
	@Test
	public void getNonexistingTask(){
		IDObject id = new IDObject();
		id.setID(666);
		Task task;
		try {
			task = controller.getTask(id, context);
			fail("Task should not be found!");
		} catch (NotFoundException e) {
		}
	}
	
	@Test
	public void deleteTask(){
		IDObject id = new IDObject();
		id.setID(0);
		try {
			controller.deleteTask(id, context);
		} catch (ForbiddenException e) {
			fail("No access");
		} catch (NotFoundException e) {
			
		} catch (UnauthorizedException e) {
			fail("Not logged in");
		}
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
			fail("Not logged in");
		} catch (ForbiddenException e) {
			
		} catch (NotFoundException e) {
			fail("No such task found");
		}
	}
	
	@Test
	public void deleteNonexistingTask(){
		try {
			IDObject id = new IDObject();
			id.setID(666);
			controller.deleteTask(id, context);
			assertTrue(false); //Should return NotFoundException
		} catch (NotFoundException e) {
			
		}
		catch(UnauthorizedException e){
			fail(e.getMessage());
		} catch (ForbiddenException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void updateTask(){
		try{
			IDObject id = new IDObject();
			id.setID(0);
			Task task = controller.getTask(id, context);
			context.setIdentity(task.getCreatorid());
			task.setTitle("Update");
			controller.updateTask(task, context);
			Task getTask = controller.getTask(id, context);
			assertNotNull(getTask);
			assertNotEquals(task.getCreatorid(), getTask.getCreatorid());
			assertEquals(task.getTitle(), getTask.getTitle());
		}
		catch(UnauthorizedException e)
		{
			fail(e.getMessage());
		} catch (NotFoundException e) {
			fail(e.getMessage());
		} catch (ForbiddenException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void updateNonExistingTask(){
		try{
			Task task = generateTestData();
			task.setCreatorid(context.getIdentity().getIdentityId());
			context.setIdentity(task.getCreatorid());
			task.setTitle("Update");
			task.setID(1394234);
			controller.updateTask(task, context);
			fail("Should have caused NotFoundException");
			
		}
		catch(UnauthorizedException e)
		{
			fail(e.getMessage());
		} catch (NotFoundException e) {

		} catch (ForbiddenException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void updateWrongPermissionsTask(){
		context.setIdentity("Alice");
		try{
			IDObject id = new IDObject();
			id.setID(0);
			Task task = controller.getTask(id, context);
			task.setTitle("Update");
			controller.updateTask(task, context);
			fail("Excepton should have been thrown");
		}
		catch(UnauthorizedException e)
		{
			fail(e.getMessage());
		} catch (NotFoundException e) {
			fail(e.getMessage());
		} catch (ForbiddenException e) {
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
		} catch (NotFoundException e) {
			fail(e.getMessage());
		} catch (ForbiddenException e) {
			fail(e.getMessage());
		}
	}
	

}

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import exceptions.ForbiddenException;
import exceptions.InternalServerErrorException;
import exceptions.NotFoundException;
import exceptions.UnauthorizedException;
import mockRepositories.MockTaskRepository;
import modelPOJO.IDObject;
import modelPOJO.Rating;
import modelPOJO.Task;
import wewoAPI.ControllerBase;
import wewoAPI.TaskController;

public class TaskControllerTest {
	TaskController controller;
	ContextTest context;
	ObjectMapper mapper;
	ByteArrayOutputStream out;
	static int dataCounter = 0;
	@Before
	public void setUp() throws Exception {
		controller = new TaskController(/*new MockTaskRepository()*/);
		context = new ContextTest("Jeiner");
		mapper = new ObjectMapper();
		out = new ByteArrayOutputStream();
	}

	
	public static Task generateTestData()
	{
		Task task = new Task();
		task.setDescription("Test" + dataCounter++);
		task.setTitle("Title" + dataCounter);
		task.setETC(30);
		task.setPrice(55);
		task.setZipaddress(2000);
		task.setStreet("Allegade");
		task.setCreatorid("TestAcc666");
		List<Integer> l = new ArrayList<Integer>();
		l.add(1);
		l.add(2);
		l.add(4);
		task.setTags(l);
		return task;
	}
	
	@Test
	public void createTask() throws InternalServerErrorException, IOException {
		Task task = generateTestData();

		task.setID(-1);
		task.setCreatorid("Nobody");



		RequestDataMock request = new RequestDataMock();
		request.setBody(mapper.writeValueAsString(task));

		controller.createTask(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		Integer taskID = response.getBody("TaskID", Integer.class);
		assertNotNull(taskID);
		assertEquals(response.getResponseCode(), 201);
		assertTrue(taskID >= 0);

		Task newTask;
		out.reset();
		request.addPath("taskID", taskID.toString());
		controller.getTask(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 200);

		newTask = response.getBody("Task", Task.class);
		assertEquals(task.getTitle(), newTask.getTitle());
		assertEquals(task.getPrice(), newTask.getPrice());
		assertEquals(task.getDescription(), newTask.getDescription());
		assertEquals(task.getStreet(), newTask.getStreet());
		assertEquals(task.getZipaddress(), newTask.getZipaddress());
		assertTrue(task.getTags().contains(4));
		assertNotNull(newTask);
		assertEquals(newTask.getCreatorid(), context.getIdentity().getIdentityId());
		out.reset();
	}
	
	@Test
	public void getTaskAlphabeticalId() throws IOException, InternalServerErrorException{
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", "Tags");
		controller.getTask(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(400, response.getResponseCode());
	}
	
	
	@Test
	public void createTaskNoBody()  throws InternalServerErrorException, IOException {
		RequestDataMock request = new RequestDataMock();
		controller.createTask(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 400);
		System.out.println(response.getBody());
	}
	@Test
	public void createTaskNonExistingAddress() throws InternalServerErrorException, IOException {
		Task task = generateTestData();

		task.setID(-1);
		task.setCreatorid("Nobody");
		task.setZipaddress(10);
		task.setStreet("Not a street");
		
		
		RequestDataMock request = new RequestDataMock();
		request.setBody(mapper.writeValueAsString(task));

		controller.createTask(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 400);
	}
	@Test
	public void createTaskUsingNonexistingTags() throws InternalServerErrorException, IOException {
		Task task = generateTestData();

		task.setID(-1);
		task.setCreatorid("Nobody");
		List<Integer> tags = new ArrayList<Integer>();
		tags.add(Integer.MAX_VALUE);
		tags.add(Integer.MAX_VALUE - 1);
		tags.add(Integer.MAX_VALUE - 2);
		task.setTags(tags);


		RequestDataMock request = new RequestDataMock();
		request.setBody(mapper.writeValueAsString(task));

		controller.createTask(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(400, response.getResponseCode());
		out.reset();
	}
	
	@Test
	public void createTaskWithoutValidLogin() throws InternalServerErrorException, IOException{
		Task task = generateTestData();
		RequestDataMock request = new RequestDataMock();
		request.setBody(mapper.writeValueAsString(task));
		context.setIdentity("");
		
		controller.createTask(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 401);
	}
	
	
	@Test
	public void createTaskWithoutLogin() throws InternalServerErrorException, IOException{
		Task task = generateTestData();
		context.clearIdentity();
		RequestDataMock request = new RequestDataMock();
		request.setBody(mapper.writeValueAsString(task));
		context.clearIdentity();
		
		controller.createTask(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 401);
	}
	
	@Test
	public void createTaskInvalidObject() throws InternalServerErrorException, IOException{
		RequestDataMock request = new RequestDataMock();
		request.setBody(mapper.writeValueAsString(new Rating()));
		controller.createTask(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(400, response.getResponseCode());
		String errString = response.getBody("error", String.class);
		System.out.println(errString);
		assertTrue(!errString.contains("null"));
	}
	
	@Test
	public void getNonexistingTask() throws InternalServerErrorException, IOException{
		createTask();
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", Integer.toString(9001));
		controller.getTask(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 404);
	}
	
	@Test
	public void deleteTask()  throws InternalServerErrorException, IOException{
		//createTask();
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", "12");
		controller.deleteTask(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 200);
		
		out.reset();
		controller.getTask(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 404);
	}
	
	@Test
	public void deleteTaskCheckPermissions()  throws InternalServerErrorException, IOException{
		createTask();
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", "0");
		context.setIdentity("Jeiner22");
		controller.deleteTask(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 403);
	}
	
	@Test
	public void deleteNonexistingTask()   throws InternalServerErrorException, IOException{
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", "5050050");
		controller.deleteTask(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(404, response.getResponseCode());
	}
	
	@Test
	public void updateTaskIgnoreCreatorAndTaskIDField()   throws InternalServerErrorException, IOException{
		createTask();
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", "0");
		Task newData = generateTestData();
		newData.setCreatorid("TotallyNotACreator");
		newData.setID(50505);
		request.setBody(mapper.writeValueAsString(newData));
		controller.updateTask(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
		
		out.reset();
		request.setBody("");
		
		controller.getTask(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 200);
		Task newTask = response.getBody("Task", Task.class);
		assertEquals(newTask.getTitle(), newData.getTitle());
		assertNotEquals(newTask.getCreatorid(), newData.getCreatorid());
		assertNotEquals(newTask.getID(), newData.getID());
	}
	@Test
	public void updateNonExistingTask()   throws InternalServerErrorException, IOException{
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", "9000");
		Task newData = generateTestData();
		request.setBody(mapper.writeValueAsString(newData));
		controller.updateTask(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 404);
	}
	
	@Test
	public void updateWrongPermissionsTask()   throws InternalServerErrorException, IOException{
		createTask();
		
		context.setIdentity("Alice");
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", "0");
		Task newData = generateTestData();
		request.setBody(mapper.writeValueAsString(newData));
		controller.updateTask(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(403, response.getResponseCode());
	}
	@Test
	public void updateNoPermissionsTask()    throws InternalServerErrorException, IOException{
		context.clearIdentity();
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", "0");
		Task newData = generateTestData();
		request.setBody(mapper.writeValueAsString(newData));
		controller.updateTask(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 401);
	}
	
	@Test
	public void queryTagsTest() throws InternalServerErrorException, IOException {
		createTask();
		
		RequestDataMock request = new RequestDataMock();
		request.addQuery("tags", "4 22");
		
		controller.findTasks(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		//List<Task> tasks = new ArrayList<Task>();
		assertEquals(200, response.getResponseCode());
		List<Task> tasks = response.getBody("Results", ArrayList.class);
		assertEquals(1, tasks.size());
		
		out.reset();
	}
	
	@Test
	public void queryEmptyTagsTest() throws InternalServerErrorException, IOException {
		createTask();
		
		RequestDataMock request = new RequestDataMock();
		
		controller.findTasks(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		//List<Task> tasks = new ArrayList<Task>();
		assertEquals(400, response.getResponseCode());
		out.reset();
	}
	
	@Test
	public void queryInvalidTagsTest() throws InternalServerErrorException, IOException {
		createTask();
		
		RequestDataMock request = new RequestDataMock();
		request.addQuery("tags", "ilvalid+tasks");
		controller.findTasks(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		//List<Task> tasks = new ArrayList<Task>();
		assertEquals(400, response.getResponseCode());
		out.reset();
	}
	
	@Test
	public void queryNonExistingTagsTest() throws InternalServerErrorException, IOException {
		createTask();
		
		RequestDataMock request = new RequestDataMock();
		request.addQuery("tags", "9000000");
		controller.findTasks(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		//List<Task> tasks = new ArrayList<Task>();
		assertEquals(404, response.getResponseCode());
		out.reset();
	}
	

	@Test
	public void queryTagsTestMultipleResults() throws InternalServerErrorException, IOException {
		createTask();
		createTask();
		createTask();
		
		RequestDataMock request = new RequestDataMock();
		request.addQuery("tags", "4");
		controller.findTasks(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		//List<Task> tasks = new ArrayList<Task>();
		assertEquals(200, response.getResponseCode());
		List<Task> tasks = response.getBody("Results", ArrayList.class);
		assertEquals(3, tasks.size());
		out.reset();
	}


}

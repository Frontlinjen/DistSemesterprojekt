

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import DatabaseController.DALException;
import DatabaseController.TaskDTO;
import exceptions.InternalServerErrorException;
import exceptions.UnauthorizedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;

import mockRepositories.MockApplicationRepository;
import mockRepositories.MockTaskRepository;
import modelPOJO.*;
import wewoAPI.ApplicationController;

public class ApplicationControllerTest {
	ApplicationController controller;
	ContextTest context;
	ObjectMapper mapper;
	ByteArrayOutputStream out;
	
	int dataCounter = 0;
	@Before
	public void setUp() throws Exception {
		MockTaskRepository taskRepo = new MockTaskRepository();
		TaskDTO taskdto = TaskDTO.fromModel(TaskControllerTest.generateTestData());
		taskdto.setCreatorId("Ib");
		taskRepo.createTask(taskdto);
		controller = new ApplicationController(new MockApplicationRepository(), taskRepo);
		context = new ContextTest("Tim");
		mapper = new ObjectMapper();
		out = new ByteArrayOutputStream();
	}

	private Application generateTestData()
	{
		Application app = new Application();
		app.setApplicationMessage("blabla");
		return app;
	}
	
	
	@Test
	public void createApplication() throws IOException, InternalServerErrorException, UnauthorizedException, DALException{
		Application app = generateTestData();
		
		app.setApplierId("Boris");
		
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", Integer.toString(0));
		request.setBody(mapper.writeValueAsString(app));
		System.out.println(new String(request.getContent()));

		controller.createApplications(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());		
		Application newApp;
		out.reset();
		
		request.addPath("taskID", "0");
		request.addPath("applierID", context.getIdentity().getIdentityId());
		
		controller.getApplication(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
		
		newApp = response.getBody("Application", Application.class);
		assertNotNull(newApp);
		assertEquals(app.getApplicationMessage(), newApp.getApplicationMessage());
		assertEquals(context.getIdentity().getIdentityId(), newApp.getApplierId());
		assertEquals(0, newApp.getTaskId());
		out.reset();
	}
	
	@Test
	public void createApplicationWithoutValidLogin() throws InternalServerErrorException, IOException{
		Application app = generateTestData();
		RequestDataMock request = new RequestDataMock();
		request.setBody(mapper.writeValueAsString(app));
		context.setIdentity("");
		
		controller.createApplications(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 401);
	}
	
	@Test
	public void createApplicationWithoutLogin() throws InternalServerErrorException, IOException{
		Application app = generateTestData();
		context.clearIdentity();
		RequestDataMock request = new RequestDataMock();
		request.setBody(mapper.writeValueAsString(app));
		context.clearIdentity();
		
		controller.createApplications(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 401);
	}
	
	@Test
	public void getNonexistingTask() throws InternalServerErrorException, IOException, UnauthorizedException, DALException{
		
		createApplication();
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", "2");
		try {
			controller.getApplication(new ByteArrayInputStream(request.getContent()), out, context);
		} catch (UnauthorizedException e) {
			e.printStackTrace();
		} catch (DALException e) {
			e.printStackTrace();
		}
		ResponseData response = new ResponseData(out);
		assertEquals(403, response.getResponseCode());
	}
	
	@Test
	public void deleteApplication()  throws InternalServerErrorException, IOException, UnauthorizedException, DALException{
		Application app = generateTestData();
		
		app.setApplierId("Boris");
		context.setIdentity("Boris");
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", Integer.toString(0));
		request.setBody(mapper.writeValueAsString(app));
		System.out.println(new String(request.getContent()));

		controller.createApplications(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
		
		controller.deleteApplication(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
		
		out.reset();
		try {
			controller.getApplication(new ByteArrayInputStream(request.getContent()), out, context);
		} catch (UnauthorizedException e) {
			e.printStackTrace();
		} catch (DALException e) {
			e.printStackTrace();
		}
		response = new ResponseData(out);
		assertEquals(403, response.getResponseCode());
	}

	@Test
	public void deleteApplicaitonCheckPermissions()  throws InternalServerErrorException, IOException, UnauthorizedException, DALException{
		Application app = generateTestData();
		
		app.setApplierId("Boris");
		context.setIdentity("Boris");
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", Integer.toString(0));
		request.setBody(mapper.writeValueAsString(app));
		System.out.println(new String(request.getContent()));

		controller.createApplications(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
		out.reset();
		
		context.setIdentity("Tims");
		request.addPath("applierID", "Boris");
		controller.deleteApplication(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		assertEquals(403, response.getResponseCode());
	}

	@Test
	public void deleteNonexistingApplication() throws InternalServerErrorException, IOException{
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", "50");
		controller.deleteApplication(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(404, response.getResponseCode());
	}
	
	@Test
	public void updateApplicationIgnoreIDField()   throws InternalServerErrorException, IOException, UnauthorizedException, DALException{
		Application app = generateTestData();
		
		app.setApplierId("Boris");
		context.setIdentity("Boris");
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", Integer.toString(0));
		request.addPath("applierID", "Boris");
		request.setBody(mapper.writeValueAsString(app));
		System.out.println(new String(request.getContent()));

		controller.createApplications(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
		out.reset();
		app.setApplierId("CUNT!");
		app.setTaskId(99);
		app.setApplicationMessage("Meh");
		request.setBody(mapper.writeValueAsString(app));
		controller.updateApplication(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
		
		out.reset();
		request.setBody("");
		
		try {
			controller.getApplication(new ByteArrayInputStream(request.getContent()), out, context);
		} catch (UnauthorizedException e) {
			e.printStackTrace();
		} catch (DALException e) {
			e.printStackTrace();
		}
		response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
		response.getBody("Application", Application.class);
		Application newApp = response.getBody("Application", Application.class);
		assertEquals("Boris", newApp.getApplierId());
		assertEquals("Meh", newApp.getApplicationMessage());
		assertEquals(0, newApp.getTaskId());
	}
	
	@Test
	public void updateNonExistingTask()   throws InternalServerErrorException, IOException{
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", "5");
		Application newData = generateTestData();
		request.setBody(mapper.writeValueAsString(newData));
		controller.updateApplication(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 404);
	}
	
	@Test
	public void updateWrongPermissionsTask()   throws InternalServerErrorException, IOException, UnauthorizedException, DALException{
		createApplication();
		
		context.setIdentity("Tim");
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", "5");
		Application newData = generateTestData();
		request.setBody(mapper.writeValueAsString(newData));
		controller.updateApplication(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(404, response.getResponseCode());
	}
	
	@Test
	public void updateNoPermissionsTask()    throws InternalServerErrorException, IOException{
		context.clearIdentity();
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", "5");
		Application newData = generateTestData();
		request.setBody(mapper.writeValueAsString(newData));
		controller.updateApplication(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 401);
	}
	
	@Test
	public void seeApplicationListWithoutPermission() throws InternalServerErrorException, IOException, UnauthorizedException, DALException {
		Application app = generateTestData();
		Application app1 = generateTestData();
		
		app.setApplierId("Boris");
		app1.setApplierId("Tim");
		context.setIdentity("Boris");
		RequestDataMock request = new RequestDataMock();
		RequestDataMock re = new RequestDataMock();
		request.addPath("taskID", Integer.toString(0));
		request.addPath("applierID", "Boris");
		request.setBody(mapper.writeValueAsString(app));
		System.out.println(new String(request.getContent()));

		controller.createApplications(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
		out.reset();
		context.setIdentity("Tim");
		re.addPath("taskID", Integer.toString(0));
		re.addPath("applierID", "Tim");
		re.setBody(mapper.writeValueAsString(app1));
		System.out.println(new String(re.getContent()));
		
		controller.createApplications(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
		out.reset();
		
		context.setIdentity("Ib");
		controller.getApplicants(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		ArrayList<String> sl = new ArrayList<String>();
		sl.add("Boris");
		sl.add("Tim");
		assertEquals(sl, response.getBody("applicants", ArrayList.class));
		out.reset();
		context.setIdentity("Hacker");
		controller.getApplicants(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		assertEquals(401, response.getResponseCode());
		out.reset();
	}

}

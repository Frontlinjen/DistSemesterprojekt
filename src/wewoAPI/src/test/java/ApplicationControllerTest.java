

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
	ContextTest con;
	ContextTest c;
	ObjectMapper mapper;
	ByteArrayOutputStream out;
	
	int dataCounter = 0;
	@Before
	public void setUp() throws Exception {
		controller = new ApplicationController();
		context = new ContextTest("Boris");
		con = new ContextTest("Tim");
		c = new ContextTest("Ib");
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
	
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", Integer.toString(1));
		request.addPath("applierID", context.getIdentity().getIdentityId());
		request.setBody(mapper.writeValueAsString(app));
		System.out.println(new String(request.getContent()));

		controller.createApplications(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());		
		Application newApp;
		out.reset();
		
		request.addPath("taskID", "1");
		request.addPath("applierID", context.getIdentity().getIdentityId());
		
		controller.getApplication(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
		
		newApp = response.getBody("Application", Application.class);
		assertNotNull(newApp);
		assertEquals(app.getApplicationMessage(), newApp.getApplicationMessage());
		assertEquals(context.getIdentity().getIdentityId(), newApp.getApplierId());
		assertEquals(1, newApp.getTaskId());
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
	public void getNonexistingApplication() throws InternalServerErrorException, IOException, UnauthorizedException, DALException{
		
		createApplication();
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", "9867");
		request.addPath("applierID", "Boris");
		try {
			controller.getApplication(new ByteArrayInputStream(request.getContent()), out, c);
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
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", Integer.toString(1));
		request.setBody(mapper.writeValueAsString(app));
		System.out.println(new String(request.getContent()));

		controller.createApplications(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
		out.reset();
		
		request.addPath("applierID", "Boris");
		controller.deleteApplication(new ByteArrayInputStream(request.getContent()), out, con);
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
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", Integer.toString(1));
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
		request.addPath("applierID", context.getIdentity().getIdentityId());
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
		assertEquals(1, newApp.getTaskId());
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

		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", "1");
		request.addPath("applierID", "Boris");
		Application newData = generateTestData();
		request.setBody(mapper.writeValueAsString(newData));
		controller.updateApplication(new ByteArrayInputStream(request.getContent()), out, con);
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
		RequestDataMock request = new RequestDataMock();
		RequestDataMock re = new RequestDataMock();
		RequestDataMock r = new RequestDataMock();
		request.addPath("taskID", Integer.toString(1));
		request.setBody(mapper.writeValueAsString(app));
		System.out.println(new String(request.getContent()));

		controller.createApplications(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
		out.reset();
		re.addPath("taskID", Integer.toString(1));
		re.setBody(mapper.writeValueAsString(app1));
		System.out.println(new String(re.getContent()));
		
		controller.createApplications(new ByteArrayInputStream(re.getContent()), out, con);
		response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
		out.reset();
		
		r.addPath("taskID", Integer.toString(1));
		System.out.println(new String(r.getContent()));
		
		controller.getApplicants(new ByteArrayInputStream(r.getContent()), out, c);
		response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
		
		ArrayList<String> sl = new ArrayList<String>();
		sl.add("2");
		sl.add("1");
		sl.add("Boris");
		sl.add("Boris");
		sl.add("Boris");
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
	
	@Test
	public void createApplicationInvalidObject() throws InternalServerErrorException, IOException{
		RequestDataMock request = new RequestDataMock();
		request.setBody(mapper.writeValueAsString(new Rating()));
		controller.createApplications(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(400, response.getResponseCode());
		String errString = response.getBody("error", String.class);
		System.out.println(errString);
		assertTrue(!errString.contains("null"));
	}

}

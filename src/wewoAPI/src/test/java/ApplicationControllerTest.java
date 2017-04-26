

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import exceptions.InternalServerErrorException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import mockRepositories.MockApplicationRepository;
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
		controller = new ApplicationController(new MockApplicationRepository());
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
	public void createApplication() throws IOException, InternalServerErrorException{
		Application app = generateTestData();
		
		app.setApplierId("Boris");
		
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", Integer.toString(5));
		request.setBody(mapper.writeValueAsString(app));
		System.out.println(new String(request.getContent()));

		controller.PostApplications(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());		
		Application newApp;
		out.reset();
		request.addPath("taskID", "5");
		request.addPath("applierID", context.getIdentity().getIdentityId());
		
		
		controller.GetApplication(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
		
		newApp = response.getBody("Application", Application.class);
		assertNotNull(newApp);
		assertEquals(app.getApplicationMessage(), newApp.getApplicationMessage());
		assertEquals(context.getIdentity().getIdentityId(), newApp.getApplierId());
		assertEquals(5, newApp.getTaskId());
		out.reset();
	}
	
	@Test
	public void createApplicationWithoutValidLogin() throws InternalServerErrorException, IOException{
		Application app = generateTestData();
		RequestDataMock request = new RequestDataMock();
		request.setBody(mapper.writeValueAsString(app));
		context.setIdentity("");
		
		controller.PostApplications(new ByteArrayInputStream(request.getContent()), out, context);
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
		
		controller.PostApplications(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 401);
	}
	
	@Test
	public void getNonexistingTask() throws InternalServerErrorException, IOException{
		
		createApplication();
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", "2");
		controller.GetApplication(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 404);
	}
	
	@Test
	public void deleteApplication()  throws InternalServerErrorException, IOException{
		createApplication();
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", "5");
		controller.deleteApplication(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 200);
		
		out.reset();
		controller.GetApplication(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 404);
	}

	@Test
	public void deleteApplicaitonCheckPermissions()  throws InternalServerErrorException, IOException{
		createApplication();
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", "5");
		context.setIdentity("Tims");
		controller.deleteApplication(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 403);
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
	public void updateApplicationIgnoreIDField()   throws InternalServerErrorException, IOException{
		createApplication();
		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", "5");
		Application newData = generateTestData();
		newData.setApplierId("CUNT!");
		newData.setTaskId(99);
		newData.setApplicationMessage("Meh");
		request.setBody(mapper.writeValueAsString(newData));
		controller.updateApplication(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
		
		out.reset();
		request.setBody("");
		
		controller.GetApplication(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 200);
		Application newApp = response.getBody("Application", Application.class);
		assertEquals(newApp.getApplierId(), newData.getApplierId());
		assertEquals(newApp.getApplicationMessage(), newData.getApplicationMessage());
		assertEquals(newApp.getTaskId(), newData.getTaskId());
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
	public void updateWrongPermissionsTask()   throws InternalServerErrorException, IOException{
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
	

}

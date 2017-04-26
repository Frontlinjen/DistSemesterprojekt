

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
		context = new ContextTest("TIM");
		mapper = new ObjectMapper();
		out = new ByteArrayOutputStream();
	}

	private Application generateTestData()
	{
		Application app = new Application();
		app.setApplicationMessage("blabla");
		app.setApplierID("TOM");
		app.setTaskid(1);
		return app;
	}
	
	
	@Test
	public void createApplication() throws IOException, InternalServerErrorException{
		Application app = generateTestData();
		
		app.setApplierID("BORIS!");
		app.setApplicationMessage("BlaBla");
		
		RequestDataMock request = new RequestDataMock();
		request.setBody(mapper.writeValueAsString(app));
		
		controller.PostApplications(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		String applierID = response.getBody("applierID", String.class);
		assertNotNull(applierID);
		assertEquals(response.getResponseCode(), 200);
		assertTrue(applierID == "BORIS!");
		
		Application newApp;
		out.reset();
		request.addPath("applierID", applierID);
		controller.GetApplication(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 200);
		
		newApp = response.getBody("Application", Application.class);
		assertEquals(app.getApplicationMessage(), newApp.getApplicationMessage());
		assertEquals(app.getApplierid(), newApp.getApplierid());
		assertEquals(app.getTaskid(), newApp.getTaskid());
		assertNotNull(newApp);
		assertEquals(newApp.getApplierid(), context.getIdentity().getIdentityId());
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
		request.addPath("applierID", "SHITFUCK!");
		controller.GetApplication(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 404);
	}
	
	@Test
	public void deleteApplication()  throws InternalServerErrorException, IOException{
		createApplication();
		RequestDataMock request = new RequestDataMock();
		request.addPath("applierID", "ØV");
		controller.deleteApplication(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 200);
		
		out.reset();
		controller.GetApplication(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 404);
	}

	@Test
	public void deleteTaskCheckPermissions()  throws InternalServerErrorException, IOException{
		createApplication();
		RequestDataMock request = new RequestDataMock();
		request.addPath("applierID", "");
		context.setIdentity("TIM");
		controller.deleteApplication(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 403);
	}

	@Test
	public void deleteNonexistingApplication() throws InternalServerErrorException, IOException{
		RequestDataMock request = new RequestDataMock();
		request.addPath("applierID", "5050050");
		controller.deleteApplication(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(404, response.getResponseCode());
	}
	
	@Test
	public void updateTaskIgnoreCreatorAndTaskIDField()   throws InternalServerErrorException, IOException{
		createApplication();
		RequestDataMock request = new RequestDataMock();
		request.addPath("applierID", "0");
		Application newData = generateTestData();
		newData.setApplierID("CUNT!");
		newData.setTaskid(99);
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
		assertEquals(newApp.getApplierid(), newData.getApplierid());
		assertEquals(newApp.getApplicationMessage(), newData.getApplicationMessage());
		assertEquals(newApp.getTaskid(), newData.getTaskid());
	}
	
	@Test
	public void updateNonExistingTask()   throws InternalServerErrorException, IOException{
		RequestDataMock request = new RequestDataMock();
		request.addPath("applierID", "POUL");
		Application newData = generateTestData();
		request.setBody(mapper.writeValueAsString(newData));
		controller.updateApplication(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 404);
	}
	
	@Test
	public void updateWrongPermissionsTask()   throws InternalServerErrorException, IOException{
		createApplication();
		
		context.setIdentity("TOBI");
		RequestDataMock request = new RequestDataMock();
		request.addPath("applierID", "BRIAN");
		Application newData = generateTestData();
		request.setBody(mapper.writeValueAsString(newData));
		controller.updateApplication(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(403, response.getResponseCode());
	}
	
	@Test
	public void updateNoPermissionsTask()    throws InternalServerErrorException, IOException{
		context.clearIdentity();
		RequestDataMock request = new RequestDataMock();
		request.addPath("applierID", "IVAN");
		Application newData = generateTestData();
		request.setBody(mapper.writeValueAsString(newData));
		controller.updateApplication(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 401);
	}
	

}

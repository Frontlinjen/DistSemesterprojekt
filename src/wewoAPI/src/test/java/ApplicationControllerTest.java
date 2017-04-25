

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.Test;

import mockRepositories.MockApplicationRepository;
import modelPOJO.*;
import wewoAPI.ApplicationController;

public class ApplicationControllerTest {
	ApplicationController controller;
	ContextTest context;
	int dataCounter = 0;
	@Before
	public void setUp() throws Exception {
		controller = new ApplicationController(new MockApplicationRepository());
		context = new ContextTest("TIM!");
	//	controller.PostApplications(generateTestData(), context);
		
	}

	private Application generateTestData()
	{
		Application app = new Application();
		app.setApplicationMessage("blabla");
		app.setApplierid(1);
		app.setTaskid(1);
		return app;
	}
	
	@Test
	public void createApplication(){
		Application app = generateTestData();
		try {
			//IDObject id = controller.PostApplications(app, context);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}

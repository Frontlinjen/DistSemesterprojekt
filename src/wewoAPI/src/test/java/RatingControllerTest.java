import modelPOJO.IDObject;
import modelPOJO.Rating;
import wewo.api.test.ContextTest;
import wewoAPI.RatingController;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import exceptions.UnauthorizedException;



public class RatingControllerTest {
	RatingController controller;
	ContextTest context;
	
	@Before
	public void setUp() throws Exception {
		context = new ContextTest("Test1");
		controller.createRating(generateTestData(), context);
	}
	
	private Rating generateTestData()
	{
		Rating rate = new Rating();
		rate.setRateeID("rateeIDTest");
		rate.setRating(100);
		rate.setMessage("This is a test");
		return rate;
	}
	
	@Test
	public void createRating() throws UnauthorizedException{
		Rating rate = generateTestData();
		IDObject id = controller.createRating(rate, context);
	}
	
	@Test
	public void getRating(){
		
	}
	
}

import modelPOJO.Rating;
import modelPOJO.RatingIDObject;
import wewo.api.test.ContextTest;
import wewoAPI.RatingController;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import DatabaseController.DALException;
import exceptions.UnauthorizedException;
import mockRepositories.MockRatingRepository;



public class RatingControllerTest {
	RatingController controller;
	ContextTest context;
	
	@Before
	public void setUp() throws Exception {
		controller = new RatingController(new MockRatingRepository());
		context = new ContextTest("Test1");
		controller.createRating(generateTestData(), context);
	}
	
	private Rating generateTestData()
	{
		Rating rate = new Rating();
		rate.setRateeID("rateeIDTest");
		rate.setRatingID(-1);
		rate.setRating(5);
		rate.setMessage("This is a test");
		return rate;
	}
	
	@Test
	public void createRating() throws UnauthorizedException, DALException{
		Rating rate = generateTestData();
		RatingIDObject id = controller.createRating(rate, context);
		id.setRaterID("Senad");
		assertEquals(id.getRaterID(), context.getIdentity().getIdentityId());
		
	}
	
	@Test
	public void getRating(){
		
	}
	
	public void lookUpRater(){
		
	}
	
}

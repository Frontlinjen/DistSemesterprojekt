import modelPOJO.Rating;
import modelPOJO.Task;
import wewoAPI.RatingController;


import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import exceptions.InternalServerErrorException;
import mockRepositories.MockRatingRepository;



public class RatingControllerTest {
	RatingController controller;
	ContextTest context;
	ObjectMapper mapper;
	ByteArrayInputStream in;
	ByteArrayOutputStream out;
	
	@Before
	public void setUp() throws Exception {
		controller = new RatingController(new MockRatingRepository());
		context = new ContextTest("Test1");
		mapper = new ObjectMapper();
		out = new ByteArrayOutputStream();
	}
	
	private Rating generateTestData()
	{
		Rating rate = new Rating();
		rate.rating = 9;
		rate.message = "TestMessage";
		return rate;
	}
	
	@Test
	public void createRating() throws InternalServerErrorException, IOException{
		Rating rate = generateTestData();
		
		context.setIdentity(null);
		RequestDataMock request = new RequestDataMock();
		request.setBody(mapper.writeValueAsString(rate));
		request.addPath("rateeID", "rateeIDTest");
		controller.createRating(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 200);
		int id = response.getBody("RatingID", Integer.class);
		assertTrue(id >= 0);
		out.reset();
		request.addPath("raterID", context.getIdentity().getIdentityId());
		request.addPath("ratingID", Integer.toString(id));
		controller.getRating(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		String testmsg = response.getBody("message", String.class);
		String testrating = response.getBody("rating", String.class);
		assertEquals(response.getResponseCode(), 200);
		assertEquals(testmsg, "TestMessage");
		assertEquals(testrating, "9.0");
		out.reset();
		
	}
	
	@Test
	public void getLastRatings() throws InternalServerErrorException, IOException{
		Rating rate = generateTestData();
		
		RequestDataMock request = new RequestDataMock();
		request.setBody(mapper.writeValueAsString(rate));
		request.addPath("rateeID", "rateeIDTest");
		
		createRating();
		context.setIdentity("test2");
		createRating();
		context.setIdentity("test3");
		createRating();
		
		controller.getLastRatings(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 200);
		
	}
	
	@Test
	public void createRatingInvalidObject() throws InternalServerErrorException, IOException{
		RequestDataMock request = new RequestDataMock();
		request.setBody(mapper.writeValueAsString(new Task()));
		controller.createRating(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(400, response.getResponseCode());
		String errString = response.getBody("error", String.class);
		System.out.println(errString);
		assertTrue(!errString.contains("null"));
	}
}

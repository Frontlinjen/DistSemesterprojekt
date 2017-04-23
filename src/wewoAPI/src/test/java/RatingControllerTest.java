import modelPOJO.Rating;
import modelPOJO.RatingIDObject;
import modelPOJO.Task;
import wewoAPI.RatingController;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DatabaseController.DALException;
import DatabaseController.RatingDTO;
import exceptions.InternalServerErrorException;
import exceptions.UnauthorizedException;
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
		rate.setRaterID("testCreator");
		rate.setRateeID("rateeIDTest");
		rate.setRating(5);
		rate.setMessage("This is a test");
		return rate;
	}
	
	@Test
	public void createRating() throws InternalServerErrorException, IOException{
		Rating rate = generateTestData();
		rate.setRatingID(-1);
		rate.setRaterID("Senad");
		RequestDataMock request = new RequestDataMock();
		request.setBody(mapper.writeValueAsString(rate));
		controller.createRating(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		Integer ratingID = response.getBody("RatingID", Integer.class);
		assertNotNull(ratingID);
		assertEquals(response.getResponseCode(), 200);
		assertTrue(ratingID >= 0);
		
		Rating newRating;
		out.reset();
		request.addPath("ratingID", ratingID.toString());
		controller.createRating(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 200);
		
		newRating = response.getBody("Rating", Rating.class);
		assertNotNull(newRating);
		assertEquals(newRating.getRaterID(), context.getIdentity().getIdentityId());
		out.reset();
		/*
		Rating id = controller.getRating(in, out, context);
		id.setRaterID("Senad");
		assertEquals(id.getRaterID(), context.getIdentity().getIdentityId());
		*/
	}
	
	@Test
	public void getRating() throws InternalServerErrorException, IOException{
	

	}
	
	public void lookUpRater(){
		
	}
	
}

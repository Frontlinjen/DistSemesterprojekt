import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import DatabaseController.MySQLTagsRepository;
import exceptions.InternalServerErrorException;
import wewoAPI.TagsController;

public class TagsControllerTest {
	TagsController controller;
	ContextTest context;
	ObjectMapper mapper;
	ByteArrayInputStream in;
	ByteArrayOutputStream out;
	
	@Before
	public void setUp() throws Exception {
		controller = new TagsController(new MySQLTagsRepository());
		context = new ContextTest("Ib");
		mapper = new ObjectMapper();
		out = new ByteArrayOutputStream();
	}
	
	@Test
	public void getTagsTest() throws InternalServerErrorException, IOException{
		RequestDataMock request = new RequestDataMock();
		controller.getTags(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
		
		System.out.println(response.getBody("Tags", HashMap.class));
	}
}


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Before;

import com.fasterxml.jackson.databind.ObjectMapper;

import mockRepositories.MockTagsRepository;
import wewoAPI.TagsController;

public class TagsControllerTest {
	TagsController controller;
	ContextTest context;
	ObjectMapper mapper;
	ByteArrayInputStream in;
	ByteArrayOutputStream out;
	
	@Before
	public void setUp() throws Exception {
		controller = new TagsController(new MockTagsRepository());
		context = new ContextTest("Test1");
		mapper = new ObjectMapper();
		out = new ByteArrayOutputStream();
	}
}


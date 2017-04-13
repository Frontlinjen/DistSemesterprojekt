import static org.junit.Assert.*;

import java.sql.Date;

import org.junit.Before;
import org.junit.Test;

import mockRepositories.MockCommentsRepository;
import mockRepositories.MockTaskRepository;
import modelPOJO.Comment;
import wewo.api.test.ContextTest;
import wewoAPI.CommentsController;

public class CommentsControllerTest {//Tests kan ikke se CommentsController?? Hænger meget sammen med tasks
	
	CommentsController controller;
	ContextTest context;

	@Before
	public void setUp() throws Exception {
		controller = new CommentsController(new MockCommentsRepository());
		context = new ContextTest("TestUser");
		controller.createTask(generateTestData(), context);
		
	}
	
	private Comment generateTestData()
	{
		Comment comment = new Comment();
		comment.setText("10/10 would test again");
		comment.setOwner("666");
		comment.setDate(new Date(5));
		comment.setTaskID(420);
		comment.setID(360);
		return comment;
	}
	

}

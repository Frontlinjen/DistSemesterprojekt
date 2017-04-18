import static org.junit.Assert.*;

import java.sql.Date;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import exceptions.NotFoundException;
import exceptions.UnauthorizedException;
import mockRepositories.MockCommentsRepository;
import mockRepositories.MockTaskRepository;
import modelPOJO.Comment;
import modelPOJO.DoubleIDObject;
import modelPOJO.IDObject;
import modelPOJO.Task;
import wewo.api.test.ContextTest;
import wewoAPI.CommentsController;

public class CommentsControllerTest {//Tests kan ikke se CommentsController?? Hænger meget sammen med tasks
	
	CommentsController controller;
	Context context;

	@Before
	public void setUp() throws Exception {
		controller = new CommentsController(new MockCommentsRepository());
		context = new ContextTest("TestUser");
		controller.createComment(generateTestData(), context);
		
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
	
	@Test
	private void createComment(){
		Comment comment = generateTestData();
		try {
			IDObject id = controller.createComment(comment, context);
			assertNotNull(id);
			assertTrue(id.getID() >= 0);
			assertTrue(id.getID() == comment.getID());
			
			Comment newComment;
			DoubleIDObject dido = new DoubleIDObject();
			dido.setFirstID(comment.getTaskID());
			dido.setSecondID(comment.getID());
			try {
				newComment = controller.getComment(dido, context);
				assertEquals(newComment.getOwner(), context.getIdentity().getIdentityId());
				assertEquals(comment, newComment);
			} catch (NotFoundException e) {
				fail("Task was not created");
			}
			
		} catch (UnauthorizedException e) {
			fail("User was not authorized");
			e.printStackTrace();
		}
	}

}

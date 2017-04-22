import static org.junit.Assert.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
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
	public void createComment(){
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
				assertTrue(newComment.getOwner().equals(context.getIdentity().getIdentityId()));
				assertTrue(comment.equals(newComment));
			} catch (Exception e) { //Replace with NotFoundException
				fail("Task was not created");
			}
			
		} catch (UnauthorizedException e) {
			fail("User was not authorized");
			e.printStackTrace();
		}
	}
	
	@Test
	public void getComment(){
		Comment comment = generateTestData();
		Comment comment2 = generateTestData();
		comment2.setID(22);
		try {
			IDObject id = controller.createComment(comment, context);	
			controller.createComment(comment2, context);
			DoubleIDObject dido = new DoubleIDObject();
			dido.setFirstID(comment.getTaskID());
			dido.setSecondID(comment.getID());
			assertTrue(controller.getComment(dido, context).equals(comment));
			IDObject ido = new IDObject();
			ido.setID(comment.getTaskID());
			List<Comment> rList = new ArrayList<Comment>();
			rList.add(comment);
			rList.add(comment2);
			assertTrue(((List<Comment>)controller.getCommentList(ido, context)).equals(rList));
		} catch (NotFoundException e) {
			fail("Task was not created");
		} catch (UnauthorizedException e) {
			fail("User was not authorized");
			e.printStackTrace();
		}
	}
	
	@Test
	public void updateComment(){
		Comment comment = generateTestData();
		Comment comment2 = generateTestData();
		comment2.setText("blah");
		try {
			IDObject id = controller.createComment(comment, context);
			DoubleIDObject dido = new DoubleIDObject();
			dido.setFirstID(comment.getTaskID());
			dido.setSecondID(comment.getID());
			Comment reComment1 = controller.getComment(dido, context);
			controller.updateComment(comment2, context);
			DoubleIDObject dido2 = new DoubleIDObject();
			dido2.setFirstID(comment2.getTaskID());
			dido2.setSecondID(comment2.getID());
			Comment reComment2 = controller.getComment(dido2, context);
			assertTrue(comment.equals(reComment1));
			assertTrue(comment2.equals(reComment2));
			assertFalse(reComment1.equals(reComment2));
			assertTrue(reComment2.getText().equals("blah"));
		} catch (NotFoundException e) {
			fail("Task was not created");
		} catch (UnauthorizedException e) {
			fail("User was not authorized");
			e.printStackTrace();
		}
	}
	
	@Test
	public void deleteComment(){
		Comment comment = generateTestData();
		try {
			controller.createComment(comment, context);
			controller.deleteComment(comment, context);
			DoubleIDObject dido = new DoubleIDObject();
			controller.getComment(dido, context);
			fail("Task was found");
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (UnauthorizedException e) {
			fail("User was not authorized");
			e.printStackTrace();
		}
	}
}

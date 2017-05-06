import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import exceptions.InternalServerErrorException;
import mockRepositories.MockCommentsRepository;
import modelPOJO.Comment;
import wewoAPI.CommentsController;

public class CommentsControllerTest {
	CommentsController controller;
	ContextTest context;
	ObjectMapper mapper;
	ByteArrayOutputStream out;
	int dataCounter = 0;
	
	@Before
	public void setUp() throws Exception {
		controller = new CommentsController(new MockCommentsRepository());
		context = new ContextTest("Jeiner");
		mapper = new ObjectMapper();
		out = new ByteArrayOutputStream();
	}

	private Comment generateTestData()
	{
		Comment comment = new Comment();
		comment.setText("test");
		comment.setDate(new Date(1));
		return comment;
	}
	
	@Test
	public void createComment() throws InternalServerErrorException, IOException {
		Comment comment = generateTestData();

		comment.setID(0);
		comment.setTaskID(5);
		comment.setOwner("B�llemis");

		RequestDataMock request = new RequestDataMock();
		request.setBody(mapper.writeValueAsString(comment));
		request.addPath("taskID", "5");
		request.addPath("commentID", "0");
		controller.createComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		Integer commentID = response.getBody("CommentID", Integer.class);
	//	Integer taskID = response.getBody("taskID", Integer.class);
		assertNotNull(commentID);
		assertEquals(200, response.getResponseCode());
		assertTrue(commentID >= 0);

		Comment newComment;
		out.reset();
		request.addPath("commentID", commentID.toString());
		request.addPath("taskID", Integer.toString(5));
		controller.getComment(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());

		newComment = response.getBody("Comment", Comment.class);
		assertNotNull(newComment);
		//System.out.println(newComment.getOwner() + " " + context.getIdentity().getIdentityId());
		assertEquals(newComment.getOwner(), context.getIdentity().getIdentityId());
		out.reset();
	}
	
	@Test
	public void createCommentWithoutValidLogin() throws InternalServerErrorException, IOException{
		Comment comment = generateTestData();
		RequestDataMock request = new RequestDataMock();
		request.setBody(mapper.writeValueAsString(comment));
		request.addPath("taskID", "5");
		request.addPath("commentID", "0");
		context.setIdentity("");
		
		controller.createComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 401);
	}
	
	@Test
	public void createCommentWithoutLogin() throws InternalServerErrorException, IOException{
		Comment comment = generateTestData();
		context.clearIdentity();
		RequestDataMock request = new RequestDataMock();
		request.setBody(mapper.writeValueAsString(comment));
		request.addPath("taskID", "5");
		request.addPath("commentID", "0");
		context.clearIdentity();
		
		controller.createComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 401);
	}
	
	@Test
	public void getNonexistingComment() throws InternalServerErrorException, IOException{
		createComment();
		RequestDataMock request = new RequestDataMock();
		request.addPath("commentID", Integer.toString(9001));
		request.addPath("taskID", "5");
		controller.getComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 404);
	}
	
	@Test
	public void deleteComment()  throws InternalServerErrorException, IOException{
		createComment();
		RequestDataMock request = new RequestDataMock();
		request.addPath("commentID", "0");
		request.addPath("taskID", "5");
		controller.deleteComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 200);
		
		out.reset();
		controller.getComment(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 404);
	}
	
	@Test
	public void deleteCommentCheckPermissions()  throws InternalServerErrorException, IOException{
		createComment();
		RequestDataMock request = new RequestDataMock();
		request.addPath("commentID", "0");
		request.addPath("taskID", "5");
		context.setIdentity("Jeiner22");
		controller.deleteComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 403);
	}
	
	@Test
	public void deleteNonexistingComment()   throws InternalServerErrorException, IOException{
		RequestDataMock request = new RequestDataMock();
		request.addPath("commentID", "912038");
		request.addPath("taskID", "5");
		controller.deleteComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(404, response.getResponseCode());
	}
	
	@Test
	public void updateNonExistingComment()   throws InternalServerErrorException, IOException{
		RequestDataMock request = new RequestDataMock();
		request.addPath("commentID", "9000");
		request.addPath("taskID", "5");
		Comment newData = generateTestData();
		request.setBody(mapper.writeValueAsString(newData));
		controller.updateComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 404);
	}
	
	@Test
	public void updateComment()   throws InternalServerErrorException, IOException{
		createComment();
		RequestDataMock request = new RequestDataMock();
		Comment newData = generateTestData();
		request.setBody(mapper.writeValueAsString(newData));
		request.addPath("commentID", "0");
		request.addPath("taskID", "5");
		controller.updateComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
	}
	
	@Test
	public void updateWrongPermissionsComment()   throws InternalServerErrorException, IOException{
		createComment();
		
		context.setIdentity("Alice");
		RequestDataMock request = new RequestDataMock();
		request.addPath("commentID", "0");
		request.addPath("taskID", "5");
		Comment newData = generateTestData();
		request.setBody(mapper.writeValueAsString(newData));
		controller.updateComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(403, response.getResponseCode());
	}
	
	@Test
	public void updateNoPermissionsComment()    throws InternalServerErrorException, IOException{
		context.clearIdentity();
		RequestDataMock request = new RequestDataMock();
		request.addPath("commentID", "0");
		request.addPath("taskID", "5");
		Comment newData = generateTestData();
		request.setBody(mapper.writeValueAsString(newData));
		controller.updateComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 401);
	}
}

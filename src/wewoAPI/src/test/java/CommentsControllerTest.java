import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import DatabaseController.MySQLCommentRepository;
import exceptions.InternalServerErrorException;
import mockRepositories.MockCommentsRepository;
import modelPOJO.Comment;
import modelPOJO.Rating;
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
		context = new ContextTest("Boris");
		mapper = new ObjectMapper();
		out = new ByteArrayOutputStream();
	}

	private Comment generateTestData()
	{
		Comment comment = new Comment();
		comment.setMessage("test");
		return comment;
	}
	
	@Test
	public void createComment() throws InternalServerErrorException, IOException {
		Comment comment = generateTestData();

		RequestDataMock request = new RequestDataMock();
		request.addPath("taskID", "0");
		request.setBody(mapper.writeValueAsString(comment));
		System.out.println(new String(request.getContent()));
		
		controller.createComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		comment.setCommenter(response.getBody("Commenter", String.class));
		comment.setCommentID(response.getBody("CommentID", Integer.class));
		comment.setTaskID(response.getBody("TaskID", Integer.class));
		assertEquals(201, response.getResponseCode());
		Comment newCom;
		out.reset();
		request.addPath("taskID", "0");
		request.addPath("commentID", Integer.toString(comment.getCommentID()));

		controller.getComment(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());

		newCom = response.getBody("Comment", Comment.class);
		assertNotNull(newCom);
		assertEquals(comment.getMessage(), newCom.getMessage());
		assertEquals(comment.getCommenter(), newCom.getCommenter());
		out.reset();
	}
	
	@Test
	public void createCommentWithoutValidLogin() throws InternalServerErrorException, IOException{
		Comment comment = generateTestData();
		RequestDataMock request = new RequestDataMock();
		request.setBody(mapper.writeValueAsString(comment));
		request.addPath("taskID", "1");
		context.setIdentity("");
		
		controller.createComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(401, response.getResponseCode());
	}
	
	@Test
	public void createCommentWithoutLogin() throws InternalServerErrorException, IOException{
		Comment comment = generateTestData();
		RequestDataMock request = new RequestDataMock();
		request.setBody(mapper.writeValueAsString(comment));
		request.addPath("taskID", "1");
		context.clearIdentity();
		
		controller.createComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(401, response.getResponseCode());
	}
	
	@Test
	public void getNonexistingComment() throws InternalServerErrorException, IOException{
		createComment();
		RequestDataMock request = new RequestDataMock();
		request.addPath("commentID", "10");
		request.addPath("taskID", "2");
		controller.getComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(404, response.getResponseCode());
	}
	
	@Test
	public void deleteComment()  throws InternalServerErrorException, IOException{
		
		createComment();
		RequestDataMock request = new RequestDataMock();
		request.addPath("commentID", "0");
		request.addPath("taskID", "0");
		controller.deleteComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
		
		out.reset();
		controller.getComment(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		assertEquals(404, response.getResponseCode());
	}
	
	@Test
	public void deleteCommentCheckPermissions()  throws InternalServerErrorException, IOException{
		createComment();
		RequestDataMock request = new RequestDataMock();
		request.addPath("commentID", "0");
		request.addPath("taskID", "0");
		context.setIdentity("Jeiner22");
		controller.deleteComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(403, response.getResponseCode());
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
		assertEquals(404, response.getResponseCode());
	}
	
	@Test
	public void updateComment()   throws InternalServerErrorException, IOException{
		createComment();
		RequestDataMock request = new RequestDataMock();
		Comment newData = generateTestData();
		request.setBody(mapper.writeValueAsString(newData));
		request.addPath("commentID", "0");
		request.addPath("taskID", "0");
		request.addPath("message", "UpdatedMessage3");
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
		request.addPath("taskID", "0");
		Comment newData = generateTestData();
		request.setBody(mapper.writeValueAsString(newData));
		controller.updateComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(403, response.getResponseCode());
	}
	
	@Test
	public void updateNoPermissionsComment()    throws InternalServerErrorException, IOException{
		createComment();
		context.clearIdentity();
		RequestDataMock request = new RequestDataMock();
		request.addPath("commentID", "0");
		request.addPath("taskID", "0");
		Comment newData = generateTestData();
		request.setBody(mapper.writeValueAsString(newData));
		controller.updateComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(401, response.getResponseCode());
	}
	
	@Test
	public void createCommentInvalidObject() throws InternalServerErrorException, IOException{
		RequestDataMock request = new RequestDataMock();
		request.setBody(mapper.writeValueAsString(new Rating()));
		controller.createComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(400, response.getResponseCode());
		String errString = response.getBody("error", String.class);
		System.out.println(errString);
		assertTrue(!errString.contains("null"));
	}
}

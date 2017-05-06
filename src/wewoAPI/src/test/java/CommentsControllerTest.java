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
import wewoAPI.CommentsController;

public class CommentsControllerTest {
	CommentsController controller;
	ContextTest context;
	ObjectMapper mapper;
	ByteArrayOutputStream out;
	int dataCounter = 0;
	
	@Before
	public void setUp() throws Exception {
		controller = new CommentsController(new MySQLCommentRepository());
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
		request.addPath("TaskID", Integer.toString(1));
		request.addPath("CommentID", Integer.toString(6));
		request.setBody(mapper.writeValueAsString(comment));
		System.out.println(new String(request.getContent()));
		
		controller.createComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
		Comment newCom;
		out.reset();
	//	Integer taskID = response.getBody("taskID", Integer.class);
		request.addPath("TaskID", "1");
		request.addPath("CommentID", "6");

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
		request.addPath("TaskID", "1");
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
		request.addPath("TaskID", "1");
		context.clearIdentity();
		
		controller.createComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 401);
	}
	
	@Test
	public void getNonexistingComment() throws InternalServerErrorException, IOException{
		createComment();
		RequestDataMock request = new RequestDataMock();
		request.addPath("CommentID", Integer.toString(9001));
		request.addPath("TaskID", "1");
		controller.getComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 404);
	}
	
	@Test
	public void deleteComment()  throws InternalServerErrorException, IOException{
		
		createComment();
		RequestDataMock request = new RequestDataMock();
		request.addPath("CommentID", "0");
		request.addPath("TaskID", "1");
		controller.deleteComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
		
		out.reset();
		controller.getComment(new ByteArrayInputStream(request.getContent()), out, context);
		response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 404);
	}
	
	@Test
	public void deleteCommentCheckPermissions()  throws InternalServerErrorException, IOException{
		createComment();
		RequestDataMock request = new RequestDataMock();
		request.addPath("CommentID", "0");
		request.addPath("TaskID", "1");
		context.setIdentity("Jeiner22");
		controller.deleteComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 403);
	}
	
	@Test
	public void deleteNonexistingComment()   throws InternalServerErrorException, IOException{
		RequestDataMock request = new RequestDataMock();
		request.addPath("CommentID", "912038");
		request.addPath("TaskID", "5");
		controller.deleteComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(404, response.getResponseCode());
	}
	
	@Test
	public void updateNonExistingComment()   throws InternalServerErrorException, IOException{
		RequestDataMock request = new RequestDataMock();
		request.addPath("CommentID", "9000");
		request.addPath("TaskID", "5");
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
		request.addPath("CommentID", "0");
		request.addPath("TaskID", "1");
		controller.updateComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(200, response.getResponseCode());
	}
	
	@Test
	public void updateWrongPermissionsComment()   throws InternalServerErrorException, IOException{
		context.setIdentity("Alice");
		RequestDataMock request = new RequestDataMock();
		request.addPath("CommentID", "1");
		request.addPath("TaskID", "3");
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
		request.addPath("CommentID", "0");
		request.addPath("TaskID", "1");
		Comment newData = generateTestData();
		request.setBody(mapper.writeValueAsString(newData));
		controller.updateComment(new ByteArrayInputStream(request.getContent()), out, context);
		ResponseData response = new ResponseData(out);
		assertEquals(response.getResponseCode(), 401);
	}
}

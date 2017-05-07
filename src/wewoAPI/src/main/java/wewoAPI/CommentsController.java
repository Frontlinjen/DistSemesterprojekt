package wewoAPI;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;

import DatabaseController.AccountRepository;
import DatabaseController.CommentDTO;
import DatabaseController.CommentRepository;
import DatabaseController.DALException;
import DatabaseController.MySQLAccountRepository;
import DatabaseController.MySQLCommentRepository;
import DatabaseController.DALException;
import DatabaseController.DALException.ForeignKeyException;
import DatabaseController.MySQLTaskRepository;
import DatabaseController.TaskDTO;
import DatabaseController.TaskRespository;
import exceptions.BadRequestException;
import exceptions.InternalServerErrorException;
import exceptions.NotFoundException;
import exceptions.UnauthorizedException;
import modelPOJO.Application;
import modelPOJO.Comment;
import modelPOJO.DoubleIDObject;
import modelPOJO.FindDataObject;
import modelPOJO.IDObject;
import modelPOJO.JsonList;
import modelPOJO.Task;

public class CommentsController extends ControllerBase{
	CommentRepository repository;
	TaskRespository taskRepo;
	AccountRepository accountRepo;
	
	public CommentsController() throws InternalServerErrorException
	{
		try {
			repository = new MySQLCommentRepository();
		} catch (DALException e) {
			e.printStackTrace();
			throw new InternalServerErrorException("");
		}
	}
	
	public CommentsController(CommentRepository repository)
	{
		this.repository = repository;
	}
	
	public void createComment(InputStream in, OutputStream out, Context context) throws InternalServerErrorException{
		try{
			if(!verifyLogin(context)){
				raiseError(out, 401, "Not logged in");
				return;
			}
			
			StartRequest(in);
			
			String taskStr = request.getPath("TaskID");
			if(taskStr == null || taskStr.isEmpty()){
				raiseError(out, 400, "No taskID specified");
				return;
			}
			int taskId;
			try{
				taskId = Integer.parseInt(taskStr);
			}catch(NumberFormatException e){
				raiseError(out, 400, "Invalid taskID specified");
				return;
			}
			Comment com = request.getObject(Comment.class);
			if(com == null){
				raiseError(out, 400, "Invalid Comment Object");
				return;
			}
			CommentDTO dto = CommentDTO.fromModel(com);
			dto.setTaskID(taskId);
			dto.setCommenter(context.getIdentity().getIdentityId());
			
			try {
				repository.createComment(dto);
				response.addResponseObject("CommentID", dto.getCommentID());
				response.addResponseObject("Commenter", dto.getCommenter());
				response.addResponseObject("TaskID", dto.getTaskID());
				response.setStatusCode(201);
				response.addHeader("Created", "/tasks/"+dto.getTaskID()+"/comments/"+dto.getCommentID());
				FinishRequest(out);
				return;
			} catch (DALException e) {
				raiseError(out, 503, "Database unavailable");
				return;
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
			raiseError(out, 500, "(â•¯Â°â–¡Â°ï¼‰â•¯ï¸µ â”»â”�â”»");
			return;
		}	
	}
	
	public void getCommentList(InputStream in, OutputStream out, Context context) throws InternalServerErrorException{
		StartRequest(in);
		
		String taskStr = request.getPath("TaskID");
		int taskid;
		try{
			taskid = Integer.parseInt(taskStr);
		}catch(NumberFormatException e){
			raiseError(out, 400, "Invalid taskID or commentID specified");					
			return;
		}
		try {
			HashMap<String, String> comments = repository.getCommentList(taskid);
			response.addResponseObject("Comments", comments);
			response.setStatusCode(200);
			FinishRequest(out);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getComment(InputStream in, OutputStream out, Context context) throws InternalServerErrorException
	{
		
		try {
			StartRequest(in);
			
			int commentID;
			int taskID;
			try{
				commentID = Integer.parseInt(request.getPath("CommentID"));		
				taskID = Integer.parseInt(request.getPath("TaskID"));
			}
			catch(NumberFormatException neg){
				raiseError(out, 400, "No commentID specified on path");
				return;
			}
			CommentDTO dto;
			dto = repository.getComment(taskID, commentID);
			if(dto==null)
			{
				raiseError(out, 404, "No comment was found using ID " + commentID);
				return;
			}
			
			Comment comment = dto.getModel();
			response.addResponseObject("Comment", comment);
			response.setStatusCode(200);
			FinishRequest(out);
		}catch (DALException e) {
			raiseError(out, 503, "Database unavailable");
			return;
		}
	}
	
	public void updateComment(InputStream in, OutputStream out, Context context) throws InternalServerErrorException
	{
		if(!verifyLogin(context)){
			raiseError(out, 401, "Not logged in");
		}
		
		try {
			StartRequest(in);
			Comment comment = request.getObject(Comment.class);
			int commentID;
			int taskID;
			try{
				commentID = Integer.parseInt(request.getPath("CommentID"));
				taskID = Integer.parseInt(request.getPath("TaskID"));
			}
			catch(Exception e)
			{
				raiseError(out, 400, "No commentID specified");
				return;
			}
			comment.setCommenter(context.getIdentity().getIdentityId());
			comment.setCommentID(commentID);
			comment.setTaskID(taskID);
			CommentDTO dto = repository.getComment(taskID, commentID);
			if(dto == null)
			{
				raiseError(out, 404, "No comment was found using ID " + commentID);
				return;
			}
			if(dto.getCommenter().equals(context.getIdentity().getIdentityId())){
				dto = CommentDTO.fromModel(comment);
				if(dto.getCommentID() == comment.getCommentID() && dto.getTaskID() == comment.getTaskID()){
					repository.updateComment(dto);
					response.setStatusCode(200);
					FinishRequest(out);
					return;
				} 
				else 
				{
					dto.setCommentID(commentID);
					dto.setTaskID(taskID);
					repository.updateComment(dto);
					response.setStatusCode(200);
					FinishRequest(out);
					return;
				} 
			}
			else 
			{
				raiseError(out, 403, "User does not own that comment");
				return;
			
			}
		} catch(DALException e) {
			raiseError(out, 503, "Database unavailable");
			return;
		}
	}
	
	public void deleteComment(InputStream in, OutputStream out, Context context) throws InternalServerErrorException
	{
		if(!verifyLogin(context)){
			raiseError(out, 401, "Not logged in");
		}	
		try {
			StartRequest(in);
			int commentId;
			int taskId;
			try{
				commentId = Integer.parseInt(request.getPath("commentID"));
				taskId = Integer.parseInt(request.getPath("taskID"));
			}
			catch(NumberFormatException eng)
			{
				raiseError(out, 400, "No comment found");
				return;
			}
			CommentDTO comment = repository.getComment(taskId, commentId);
			if(comment == null)
			{
				raiseError(out, 404, "No such comment");
				return;
			}
			
			if(comment.getCommenter().equals(context.getIdentity().getIdentityId())){
				repository.deleteComment(taskId, commentId);
				response.setStatusCode(200);
				FinishRequest(out);
				return;
			}
			else
			{
				raiseError(out, 403, "User does not own that task");
				return;
			}
		} catch (DALException e) {
			raiseError(out, 503, "Database unavailable");
		}
	}
}

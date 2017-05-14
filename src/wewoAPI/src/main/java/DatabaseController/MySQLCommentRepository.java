package DatabaseController;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MySQLCommentRepository implements CommentRepository{
	private final String GET_COMMENT = "SELECT * FROM Comments WHERE TaskID = ? AND CommentID = ?;";
	private final String GET_COMMENT_LIST = "SELECT * FROM Comments WHERE TaskID = ?;";
	private final String DELETE_COMMENT = "DELETE FROM Comments WHERE TaskID = ? AND CommentID = ?;";
	private final String CREATE_COMMENT = "INSERT INTO Comments(TaskID, Commenter, message, submitDate) VALUES (?, ?, ?, CURDATE());";
	private final String UPDATE_COMMENT = "UPDATE Comments SET message = ?, submitDate = CURDATE() WHERE TaskID = ? AND CommentID = ?;";
	
	public MySQLCommentRepository() throws DALException{
		DatabaseConnector.RegisterStatement("GET_COMMENT", GET_COMMENT);
		DatabaseConnector.RegisterStatement("CREATE_COMMENT", CREATE_COMMENT);
		DatabaseConnector.RegisterStatement("UPDATE_COMMENT", UPDATE_COMMENT);
		DatabaseConnector.RegisterStatement("DELETE_COMMENT", DELETE_COMMENT);
		DatabaseConnector.RegisterStatement("GET_COMMENT_LIST", GET_COMMENT_LIST);
	}
	
	
	public CommentDTO getComment(int taskID, int commentID) throws DALException {
		PreparedStatement statement;
		try {
			statement = DatabaseConnector.getPreparedStatement("GET_COMMENT");
			statement.setInt(1, taskID);
			statement.setInt(2, commentID);

			ResultSet rs = statement.executeQuery();

			if (!rs.first()) {
				return null;
			}
			return generate(rs);
			
		}
		catch (SQLException e) {throw new DALException(e); }
	}

	public HashMap<String, String> getCommentList(int taskId) throws DALException {
		HashMap<String, String> commentList = new HashMap<String, String>();
		try
		{
			PreparedStatement statement = DatabaseConnector.getPreparedStatement("GET_COMMENT_LIST");
			statement.setInt(1, taskId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) 
			{
				commentList.put(rs.getString(1), rs.getString(2));
			}
		} 
		catch (SQLException e) { throw new DALException(e.getMessage()); }
		return commentList;
	}

	//(commentId, taskId, ownerId, text, date)
	public int createComment(CommentDTO com) throws DALException {
		try {
			//"INSERT INTO Comments(TaskID, Commenter, message, submitDate) VALUES (?, ?, ?, CURDATE());";
			PreparedStatement statement = DatabaseConnector.getPreparedStatement("CREATE_COMMENT");
			statement.setInt(1, com.getTaskID());
			statement.setString(2, com.getCommenter());
			statement.setString(3, com.getMessage());

			int res = statement.executeUpdate();
			ResultSet rs = DatabaseConnector.doQuery("SELECT LAST_INSERT_ID();");
			
			rs.first();
			int ID = rs.getInt("last_insert_id()");
			
			com.CommentID = ID;
			return res;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	//(commentId, taskId, ownerId, text, date)
	public int updateComment(CommentDTO com) throws DALException {
		try{
			PreparedStatement statement = DatabaseConnector.getPreparedStatement("UPDATE_COMMENT");
			statement.setString(1, com.getMessage());
			statement.setInt(2, com.getTaskID());
			statement.setInt(3, com.getCommentID());
			return statement.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}

	public int deleteComment(int taskId, int commentId) throws DALException {
		try{
			PreparedStatement statement = DatabaseConnector.getPreparedStatement("DELETE_COMMENT");
			statement.setInt(1, taskId);
			statement.setInt(2, commentId);
			return statement.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}
	
	private CommentDTO generate(ResultSet rs) throws SQLException
	{
		CommentDTO comment = new CommentDTO();
		comment.setMessage(rs.getString("message"));
		comment.setSubmitDate(rs.getDate("submitDate"));
		comment.setCommenter(rs.getString("Commenter"));
		comment.setTaskID(rs.getInt("TaskID"));
		comment.setCommentID(rs.getInt("CommentID"));
		return comment;
	}

}

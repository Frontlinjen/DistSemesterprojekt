package DatabaseController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class MySQLTaskRepository implements TaskRespository{
	private final String GET_TASK = "SELECT * FROM Tasks WHERE id = ?;";
	private final String CREATE_TASK = "INSERT INTO Tasks(creatorID, title, description, price, ECT, supplies, urgent,"
									 + "street, zipcode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
	private final String CREATE_TASK_TAGS = "INSERT INTO TaskTags(TagID, TaskID) VALUES (?, (select LAST_INSERT_ID()));";
	private final String UPDATE_TASK = "UPDATE ansat SET  ID = '?', title =  '?', description = '?', price = '?', ECT = '?', supplies " +
									   "= '?', urgent = '?', street = '?', zipcode = '?' WHERE ID = '?';";
	
	public MySQLTaskRepository() throws DALException{
		DatabaseConnector.RegisterStatement("GET_TASK", GET_TASK);
		DatabaseConnector.RegisterStatement("CREATE_TASK", CREATE_TASK);
		DatabaseConnector.RegisterStatement("UPDATE_TASK", UPDATE_TASK);
		DatabaseConnector.RegisterStatement("CREATE_TASK_TAGS", CREATE_TASK_TAGS); 
	}
	public TaskDTO getTask(int id) throws DALException {
		PreparedStatement statement;
		try {
			statement = DatabaseConnector.getPreparedStatement("GET_TASK");
			statement.setInt(1, id);

			ResultSet rs = statement.executeQuery();

			if (!rs.first()) 
				return null;
			
			return generate(rs);
			
		}
		catch (SQLException e) {throw new DALException(e); }
	}

	private TaskDTO generate(ResultSet rs) throws SQLException
	{
		TaskDTO task = new TaskDTO();
		task.setId(rs.getInt("ID")).setTitle(rs.getString("title")).setPrice(rs.getInt("price"))
		.setDescription(rs.getString("description")).setSupplies(rs.getBoolean("supplies") ? 1 : 0).setUrgent(rs.getBoolean("urgent") ? 1 : 0)
		.setViews(rs.getInt("views")).setStreet(rs.getString("street")).setZipaddress(rs.getInt("zipcode"))
		.setCreated(rs.getDate("created")).setEdited(rs.getDate("edited")).setCreatorId(rs.getString("creatorID"));
		return task;
	}
	
	public List<TaskDTO> getTaskList() throws DALException {
		List<TaskDTO> list = new ArrayList<TaskDTO>();
		ResultSet rs = DatabaseConnector.doQuery("SELECT * FROM Tasks;");
		try
		{
			while (rs.next()) 
			{
				TaskDTO task = generate(rs);
				list.add(task);
			}
		} 
		catch (SQLException e) { throw new DALException(e.getMessage()); }
		return list;
	}

	public int createTask(TaskDTO tas) throws DALException {
		try { 
			PreparedStatement taskStatement = DatabaseConnector.getPreparedStatement("CREATE_TASK");
			taskStatement.setString(1, tas.creatorid);
			taskStatement.setString(2, tas.title);
			taskStatement.setString(3, tas.description);
			taskStatement.setInt(4,  tas.price);
			taskStatement.setInt(5, tas.ect);
			taskStatement.setInt(6, tas.supplies);
			taskStatement.setInt(7, tas.urgent);
			taskStatement.setString(8, tas.street);
			taskStatement.setInt(9, tas.zipaddress);
			HashSet<Integer> set = new HashSet<Integer>();
			for (int i = 0; i < tas.tags.size(); i++) {
				set.add(tas.tags.get(i));
			}
			PreparedStatement addTagStatement = DatabaseConnector.getPreparedStatement("CREATE_TASK_TAGS");
			//We are ready to execute
			DatabaseConnector.StartTransaction();
			int taskResult = taskStatement.executeUpdate();
			for (Integer tag : set) {
				addTagStatement.setInt(1, tag);
				//addTagStatement.setInt(2, ID);
				addTagStatement.addBatch();
			}
			int[] tagResults = addTagStatement.executeBatch();
			DatabaseConnector.EndTransaction();
			
			int error = 0;
			for (int i = 0; i < tagResults.length; i++) {
				error |= tagResults[i];
			}
			error |= taskResult;
			
			ResultSet rs = DatabaseConnector.doQuery("SELECT LAST_INSERT_ID();");
			
			rs.first();
			int ID = rs.getInt("last_insert_id()");
			
			tas.id = ID;
			return error;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DALException(e);
		}
	}

	public int updateTask(TaskDTO tas) throws DALException {
		try{
			PreparedStatement statement = DatabaseConnector.getPreparedStatement("UPDATE_TASK");
			statement.setString(1, tas.title);
			statement.setString(2, tas.description);
			statement.setInt(3,  tas.price);
			statement.setInt(4, tas.ect);
			statement.setInt(5, tas.supplies);
			statement.setInt(6, tas.urgent);
			statement.setString(7, tas.street);
			statement.setInt(8, tas.zipaddress);
			statement.setInt(9, tas.id);
			return statement.executeUpdate();
		
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}

	public int deleteTask(int id) throws DALException {
		return DatabaseConnector.doUpdate("DELETE FROM Tasks WHERE ID = " + id + ";");
	}

	public List<TaskDTO> queryTasks(List<Integer> tags) {
		int START = 0;
		int END = 25;
	
		StringBuilder sb = new StringBuilder();
		for (Integer integer : tags) {
			sb.append(tags + ", ");
		}
		String TAGS = sb.toString();
		try{
			
		
		//Potential risk of SQL Injection
		ResultSet rs  = DatabaseConnector.doQuery("select * from Tasks INNER JOIN from TaskTags " + 
		"(select TaskID from TaskTags, Tags where Tags.ID in (" + TAGS  + ") GROUP BY TaskID)" + 
		" AS TaskID ON Tasks.ID = TaskIDs.TaskID LIMIT " + START + ", " + END + ";");
		while(rs.next())
		{
			
		}
		}
		catch(SQLException e)
		{
			
		}
		catch(DALException e)
		{
			
		}
		return null;
	}
}
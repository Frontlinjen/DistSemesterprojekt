package DatabaseController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MySQLTaskRespository implements TaskRespository{
	private final String GET_TASK = "SELECT * FROM Tasks WHERE id = ?;";
	private final String CREATE_TASK = "INSERT INTO Tasks(title, description, price, ECT, supplies, urgent"
									 + "street, zipcode) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
	private final String UPDATE_TASK = "UPDATE ansat SET  ID = '?', title =  '?', description = '?', price = '?', ECT = '?', supplies " +
									   "= '?', urgent = '?', street = '?', zipcode = '?' WHERE ID = '?';";
	
	
	public MySQLTaskRespository(){
		DatabaseConnector.RegisterStatement("GET_TASK", GET_TASK);
		DatabaseConnector.RegisterStatement("CREATE_TASK", CREATE_TASK);
		DatabaseConnector.RegisterStatement("UPDATE_TASK", UPDATE_TASK);
		
	}
	public TaskDTO getTask(int id) throws DALException {
		PreparedStatement statement;
		try {
			statement = DatabaseConnector.getPreparedStatement("GET_TASK");
			statement.setInt(1, id);

			ResultSet rs = statement.executeQuery();

			if (!rs.first()) 
				throw new DALException("Task med id " + id + " findes ikke");
			
			return generate(rs);
			
		}
		catch (SQLException e) {throw new DALException(e); }
	}

	private TaskDTO generate(ResultSet rs) throws SQLException
	{
		TaskDTO task = new TaskDTO();
		task.setId(rs.getInt("ID")).setTitle(rs.getString("title"))
		.setDescription(rs.getString("description")).setTitle("title")
		.setSupplies(rs.getBoolean("supplies") ? 1 : 0).setUrgent(rs.getBoolean("urgent") ? 1 : 0)
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
			PreparedStatement statement = DatabaseConnector.getPreparedStatement("CREATE_TASK");
			statement.setString(1, tas.title);
			statement.setString(2, tas.description);
			statement.setInt(3,  tas.price);
			statement.setInt(4, tas.ect);
			statement.setInt(5, tas.supplies);
			statement.setInt(6, tas.urgent);
			statement.setString(7, tas.street);
			statement.setInt(8, tas.zipaddress);


			int res = statement.executeUpdate();


			ResultSet rs = DatabaseConnector.doQuery("SELECT LAST_INSERT_ID();");
			rs.first();
			int ID = rs.getInt("last_insert_id()");
			tas.id = ID;
			return res;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
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

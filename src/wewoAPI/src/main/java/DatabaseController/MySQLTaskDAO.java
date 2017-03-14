package DatabaseController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLTaskDAO implements TaskDAO{

	public TaskDTO getTask(int id) throws DALException {
		ResultSet rs = DatabaseConnector.doQuery("SELECT * FROM Tasks WHERE id = " + id + ";");
	    try {
	    	if (!rs.first()) throw new DALException("Task med id " + id + " findes ikke");
	    	return new TaskDTO (rs.getInt("ID"), rs.getString("title"), 
					rs.getString("description"), rs.getInt("price"), rs.getInt("ECT"),
					rs.getBoolean("supplies"), rs.getBoolean("urgent"), rs.getInt("views"),
					rs.getString("street"), rs.getInt("zipcode"), rs.getDate("created"),
					rs.getDate("edited"), rs.getString("creatorID"));
	    }
	    catch (SQLException e) {throw new DALException(e); }
	}

	public List<TaskDTO> getTaskList() throws DALException {
		List<TaskDTO> list = new ArrayList<TaskDTO>();
		ResultSet rs = DatabaseConnector.doQuery("SELECT * FROM Tasks;");
		try
		{
			while (rs.next()) 
			{
				list.add(new TaskDTO(rs.getInt("ID"), rs.getString("title"), 
						rs.getString("description"), rs.getInt("price"), rs.getInt("ECT"),
						rs.getBoolean("supplies"), rs.getBoolean("urgent"), rs.getInt("views"),
						rs.getString("street"), rs.getInt("zipcode"), rs.getDate("created"),
						rs.getDate("edited"), rs.getString("creatorID")));
			}
		}
		catch (SQLException e) { throw new DALException(e.getMessage()); }
		return list;
	}

	public int createTask(TaskDTO tas) throws DALException {
		int res = DatabaseConnector.doUpdate(
				"INSERT INTO Tasks(ID, title, description, price, ECT, supplies, urgent, views, "
				+ "street, zipcode, created, edited, creatorID) VALUES " +
				"(" + tas.getId() + ", '" + tas.getTitle() + "', '" + tas.getDescription() + "', '" + 
				tas.getPrice() + "', '" + tas.getEct() + "', '" + tas.getSupplies() + "', '" +
				tas.getUrgent() + "', '" + tas.getViews() + "', '" + tas.getStreet() + "', '" +
				tas.getZipaddress() + "', '" + tas.getCreated() + "', '" + tas.getEdited() +
				"', '" + tas.getCreatorId() + "');");
		
		try {
			ResultSet rs = DatabaseConnector.doQuery("SELECT LAST_INSERT_ID();");
			rs.first();
			int ID = rs.getInt("last_insert_id()");
			tas.id = ID;
			return res;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public int updateTask(TaskDTO tas) throws DALException {
		return DatabaseConnector.doUpdate(
				"UPDATE ansat SET  ID = '" + tas.getId() + "', title =  '" + tas.getTitle() + 
				"', description = '" + tas.getDescription() + "', price = " + tas.getPrice() +
				"', ECT = '" + tas.getEct() + "', supplies = '" + tas.getSupplies() + "', urgent = '" +
				tas.getUrgent() + "', views = '" + tas.getViews() + "', street = '" + tas.getStreet() +
				"', zipcode = '" + tas.getZipaddress() + "', created = '" + tas.getCreated() +
				"', edited = '" + tas.getEdited() + "', creatorID = '" + tas.getCreatorId() +
				" WHERE ID = '" + tas.getId() + "';"
		);
	}

	public int deleteTask(int id) throws DALException {
		return DatabaseConnector.doUpdate("DELETE FROM Tasks WHERE ID = " + id + ";");
	}

}

package DatabaseController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLTaskTagsDAO implements TaskTagsDAO{

	public List<TaskTagsDTO> getTaskTagsList(int ID) throws DALException {
		List<TaskTagsDTO> list = new ArrayList<TaskTagsDTO>();
		ResultSet rs = DatabaseConnector.doQuery("SELECT * FROM Tasks;");
		try
		{
			while (rs.next()) 
			{
				list.add(new TaskTagsDTO(rs.getInt("TagID"), rs.getInt("TaskID")));
			}
		}
		catch (SQLException e) { throw new DALException(e.getMessage()); }
		return list;
	}

	public List<TaskTagsDTO> getTagsTasksList(List<Integer> tags) throws DALException {
		String s = null;
		for(int i = 0; i < tags.size(); i++){
			if(s == null && i != tags.size()-1){
				s = tags.get(i) + ", ";
			}
			else if(s == null && i == tags.size()-1){
				s = tags.get(i) + "";
			}
			else if (s != null && i != tags.size()-1){
				s = s + tags.get(i) + ", ";
			}
			else {
				s = s + tags.get(i) + "";
			}
		}
		
		List<TaskTagsDTO> list = new ArrayList<TaskTagsDTO>();
		ResultSet rs = DatabaseConnector.doQuery("select * from Tasks INNER JOIN (select "
				+ "TaskID from TaskTags, Tags where Tags.ID in (" + s + ") GROUP BY TaskID);"
				//+ "AS TaskIDs ON Tasks.ID = TaskIDs.TaskID LIMIT @begin, @end;\",\");"
				//+ "new MySqlParameter(\"values\", String.Join(\",\", tags)), "
				//+ "new MySqlParameter(\"begin\", index*max+1), new MySqlParameter(\"end\", "
				//+ "index*max+ max));"
				);
		try
		{
			while (rs.next()) 
			{
				list.add(new TaskTagsDTO(rs.getInt("TagID"), rs.getInt("TaskID")));
			}
		}
		catch (SQLException e) { throw new DALException(e.getMessage()); }
		System.out.println(list);
		return list;
		
	}

	public int createTaskTags(TaskTagsDTO tas) throws DALException {
		return DatabaseConnector.doUpdate(
		"UPDATE ansat SET  TagID = '" + tas.getTagID() + "', TaskID =  '" + tas.getTaskID() +
		"';");
	}

	public int deleteTaskTags(int TagID, int TaskID) throws DALException {
		return DatabaseConnector.doUpdate("DELETE FROM TaskTags WHERE TagID = " + TagID + " AND " +
				"TaskID = " + TaskID + ";");
	}

}

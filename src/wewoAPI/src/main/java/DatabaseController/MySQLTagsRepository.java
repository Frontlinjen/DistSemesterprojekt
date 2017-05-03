package DatabaseController;

import java.util.List;

public class MySQLTagsRepository implements TagsRepository{

	private final String GET_TAGS = "SELECT * FROM Tags;";
	
	public MySQLTagsRepository() throws DALException{
		DatabaseConnector.RegisterStatement("GET_TAGS", GET_TAGS);
	}
	
	public List<TagsDTO> getTags() throws DALException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}

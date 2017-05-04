package mockRepositories;

import java.util.HashMap;

import DatabaseController.DALException;
import DatabaseController.TagsRepository;


public class MockTagsRepository implements TagsRepository {
	private HashMap<String, Integer> database = new HashMap<String, Integer>();	

	public HashMap<String, Integer> getTags() throws DALException {
		return database;
	}
}

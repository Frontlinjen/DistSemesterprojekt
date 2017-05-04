package mockRepositories;

import java.util.ArrayList;
import java.util.List;

import DatabaseController.DALException;
import DatabaseController.TagsDTO;
import DatabaseController.TagsRepository;


public class MockTagsRepository implements TagsRepository {
	private List<TagsDTO> database = new ArrayList<TagsDTO>();	

	public List<TagsDTO> getTags() throws DALException {
		return database;
	}
}

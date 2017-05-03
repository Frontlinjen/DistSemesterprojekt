package DatabaseController;

import java.util.List;

public interface TagsRepository {
	List<TagsDTO> getTags() throws DALException;

}

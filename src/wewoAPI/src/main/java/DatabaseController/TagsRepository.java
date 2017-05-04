package DatabaseController;

import java.util.HashMap;

public interface TagsRepository {
	HashMap<String, Integer> getTags() throws DALException;
}

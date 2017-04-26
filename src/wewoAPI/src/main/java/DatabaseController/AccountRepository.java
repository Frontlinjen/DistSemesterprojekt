package DatabaseController;

import java.util.List;

public interface AccountRepository {

	AccountDTO getAccount(String string) throws DALException;
	List<AccountDTO> queryAccount(List<Integer> tags) throws DALException;
	List<AccountDTO> getAccountList() throws DALException;
	int createAccount(AccountDTO acc) throws DALException;
	int updateAccount(AccountDTO acc) throws DALException;
	int deleteAccount(int id) throws DALException;
	
}

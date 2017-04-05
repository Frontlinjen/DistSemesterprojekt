package wewoAPI;

import java.sql.Date;

import com.amazonaws.services.lambda.runtime.Context;

import DatabaseController.AccountDTO;
import DatabaseController.AccountRepository;
import DatabaseController.DALException;
import DatabaseController.MySQLAccountRepository;
import DatabaseController.MySQLTaskRespository;
import DatabaseController.TaskDTO;
import DatabaseController.TaskRespository;
import modelPOJO.Account;
import modelPOJO.IDObject;
import modelPOJO.JsonList;
import modelPOJO.Task;

public class AccountController {
	public Account getAccount(IDObject userID, Context context)
	{
		AccountRepository dao = new MySQLAccountRepository();
		AccountDTO dto;
		try {
			dto = dao.getAccount(userID.getID());
			Account account = new Account();
				
			account.setName(dto.getName());
			account.setEmail(dto.getEmail());
			account.setPhone(dto.getPhone());
			account.setSex(dto.isSex());
			account.setAboutMe(dto.getAboutMe());
			account.setCompetences(dto.getCompetences());
			
			return account;
			
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	void updateAccount(Account account, Context context)
	{
		AccountRepository dao = new MySQLAccountRepository();
		try{
			AccountDTO dto = dao.getAccount(account.getuserID());
			dto = new AccountDTO()
					.setName(account.getName())
					.setEmail(account.getEmail())
					.setPhone(account.getPhone())
					.setSex(account.isSex())
					.setAboutMe(account.getAboutMe())
					.setCompetences(account.getCompetences());
			dao.updateAccount(dto);
		} catch (DALException e){
			e.printStackTrace();
		}
	}
	JsonList<IDObject> getApplicants(IDObject taskid, Context context)
	{
		return null;
	}
	JsonList<IDObject> getCreatedTasks(IDObject userID, Context context) {
		return null;
	}
	void createAccount(Account account, Context context){
		IDObject newUserID = new IDObject();
		
		AccountRepository dao = new MySQLAccountRepository();
		AccountDTO dto = new AccountDTO(
				newUserID.getID(),
				account.getName(),
				account.getEmail(),
				account.getPhone(),
				account.isSex(),
				account.getAboutMe(),
				account.getCompetences()
		);
		
		try {
			dao.createAccount(dto);
			newUserID.setID(dto.getUserID());
		} catch (DALException e) {
			e.printStackTrace();
		}
	}
}


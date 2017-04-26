package wewoAPI;

import com.amazonaws.services.lambda.runtime.Context;

import DatabaseController.AccountDTO;
import DatabaseController.AccountRepository;
import DatabaseController.DALException;
import DatabaseController.MySQLAccountRepository;
import exceptions.InternalServerErrorException;
import modelPOJO.Account;
import modelPOJO.IDObject;
import modelPOJO.JsonList;
import modelPOJO.UserIDObject;

public class AccountController {
	private AccountRepository dao;
	
	public AccountController() throws InternalServerErrorException{
		try {
			dao =  new MySQLAccountRepository();
		} catch (DALException e) {
			throw new InternalServerErrorException("");
		}
	}
	public Account getAccount(IDObject userid, Context context)
	{
		AccountDTO dto;
		try {
			dto = dao.getAccount("/* TODO: APPLY ACTUAL ID */");
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
	
	JsonList<IDObject> getCompetences(IDObject userid, Context context)
	{
		Account acc = getAccount(userid, context);
		return (JsonList<IDObject>) acc.getCompetences();
	}
	
	public void createAccount(Account account, Context context){
		UserIDObject newUserID = new UserIDObject();
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


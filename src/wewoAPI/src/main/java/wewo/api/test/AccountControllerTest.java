package wewo.api.test;

import java.util.ArrayList;
import java.util.List;

import modelPOJO.Account;
import modelPOJO.FindDataObject;
import modelPOJO.IDObject;
import modelPOJO.Task;
import wewoAPI.AccountController;
import wewoAPI.TaskController;

public class AccountControllerTest {
	public static void main (String[]args){
		List<String> competences = new ArrayList<String>();
		competences.add("Børnepasning");
		competences.add("Slå græs");
		
		AccountController AC = new AccountController();
		Account account = new Account();
		//task.setID("ID");
		account.setName("Test Et");
		account.setEmail("test@test.dk");
		account.setPhone("12345678");
		account.setSex(true);
		account.setAboutMe("Hej");
		account.setCompetences(competences);
		IDObject ID = new IDObject();
		ID.setID(7);
		FindDataObject FD = new FindDataObject();
		
		
		ContextTest cont = new ContextTest();
		
		
		//System.out.println(AC.createAccount(account, cont).getID());
		Account a = AC.getAccount(ID, cont);
		
		a.getCompetences();
		//AC.findAccounts(FD, cont);
		
		//AC.updateAccount(task, cont);
		
		//AC.deleteAccount(ID, cont);
	}
}

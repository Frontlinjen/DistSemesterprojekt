package DatabaseController;

import java.io.Serializable;
import java.util.List;

import modelPOJO.Account;

public class AccountDTO implements Serializable{
	
	int userID;
	String name;
	String email;
	String phone;
	boolean sex;
	String aboutMe;
	List<String> competences;
	
	public AccountDTO(int userID, String name, String email, String phone, boolean sex, String aboutMe, List<String> competences){
		this.userID = userID;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.sex = sex;
		this.aboutMe = aboutMe;
		this.competences = competences;
	}
	
	public AccountDTO(AccountDTO acc){
		this.userID = acc.getUserID();
		this.name = acc.getName();
		this.email = acc.getEmail();
		this.phone = acc.getPhone();
		this.sex = acc.isSex();
		this.aboutMe = acc.getAboutMe();
		this.competences = acc.getCompetences();
	}
	
	static AccountDTO fromModel(Account acc){
		AccountDTO dto = new AccountDTO();
		dto.setUserID(acc.getuserID());
		dto.setName(acc.getName());
		dto.setEmail(acc.getEmail());
		dto.setPhone(acc.getPhone());
		dto.setSex(acc.isSex());
		dto.setAboutMe(acc.getAboutMe());
		dto.setCompetences(acc.getCompetences());
		return dto;
	}
	
	Account getModel(){
		Account acc = new Account();
		acc.setUserID(this.getUserID());
		acc.setName(this.getName());
		acc.setEmail(this.getEmail());
		acc.setPhone(this.getPhone());
		acc.setSex(this.isSex());
		acc.setAboutMe(this.getAboutMe());
		acc.setCompetences(this.getCompetences());
		return acc;
	}

	public int getUserID() {
		return userID;
	}

	public AccountDTO setUserID(int userID) {
		this.userID = userID;
		return this;
	}

	public String getName() {
		return name;
	}

	public AccountDTO setName(String name) {
		this.name = name;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public AccountDTO setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public AccountDTO setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public boolean isSex() {
		return sex;
	}

	public AccountDTO setSex(boolean sex) {
		this.sex = sex;
		return this;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public AccountDTO setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
		return this;
	}

	public List<String> getCompetences() {
		return competences;
	}

	public AccountDTO setCompetences(List<String> competences) {
		this.competences = competences;
		return this;
	}
	
}

package modelPOJO;
import java.util.List;

public class Account {
	
	public String getuserID() {
		return userID;
	}
	public void setuserID(String userID) {
		this.userID = userID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public boolean isSex() {
		return sex;
	}
	public void setSex(boolean sex) {
		this.sex = sex;
	}
	public String getAboutMe() {
		return aboutMe;
	}
	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}
	public List<String> getCompetences() {
		return competences;
	}
	public void setCompetences(List<String> competences) {
		this.competences = competences;
	}
	String userID;
	String name;
	String email;
	String phone;
	boolean sex;
	String aboutMe;
	List<String> competences;
    
}

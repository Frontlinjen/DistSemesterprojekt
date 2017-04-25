package modelPOJO;

public class Application {
	String applicationMessage;
	int applierid, taskID;
	
	public int getApplierid(){
		return applierid;
	}
	public void setApplierid(int applierID) {
		this.applierid = applierID;
	}
	
	public int getTaskid() {
		return taskID;
	}
	public void setTaskid(int taskid) {
		this.taskID = taskid;
	}
	public String getApplicationMessage() {
		return applicationMessage;
	}
	public void setApplicationMessage(String applicationMessage) {
		this.applicationMessage = applicationMessage;
	}
}

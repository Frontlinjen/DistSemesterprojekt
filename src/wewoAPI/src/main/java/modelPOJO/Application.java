package modelPOJO;

public class Application {
	String applicationMessage, applierID;
	int taskID;
	
	public String getApplierid(){
		return applierID;
	}
	public void setApplierID(String applierID) {
		this.applierID = applierID;
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

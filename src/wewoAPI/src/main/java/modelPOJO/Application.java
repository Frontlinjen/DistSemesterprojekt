package modelPOJO;
//POST api/tasks/{ID}/applications
//Body { taskID = 22; }
public class Application {
	private String applicationMessage, applierID;
	private int taskID;
	
	public String getApplierId(){
		return applierID;
	}
	public void setApplierId(String applierID) {
		this.applierID = applierID;
	}
	
	public int getTaskId() {
		return taskID;
	}
	public void setTaskId(int taskID) {
		this.taskID = taskID;
	}
	public String getApplicationMessage() {
		return applicationMessage;
	}
	public void setApplicationMessage(String applicationMessage) {
		this.applicationMessage = applicationMessage;
	}
}

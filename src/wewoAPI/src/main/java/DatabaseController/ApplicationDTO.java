package DatabaseController;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import modelPOJO.Application;

public class ApplicationDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	String taskid, applicationMessage, applierid;

	public ApplicationDTO(){
		
	}
	
	static ApplicationDTO fromModel(Application app)
	{
		ApplicationDTO dto = new ApplicationDTO();
		dto.setTaskid(app.getTaskid());
		dto.setApplicationMessage(app.getApplicationMessage());
		return dto;
	}
	
	Application getModel(){
		Application app = new Application();
		app.setApplicationMessage(this.getApplicationMessage());
		app.setTaskid(this.taskid);
		return app;
	}
	
	public String getApplierid() {
		return applierid;
	}
	
	public void setApplierid(String applierid) {
		this.applierid = applierid;
	}
	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getApplicationMessage() {
		return applicationMessage;
	}

	public void setApplicationMessage(String applicationMessage) {
		this.applicationMessage = applicationMessage;
	}

}
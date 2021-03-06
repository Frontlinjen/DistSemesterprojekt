package DatabaseController;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import modelPOJO.Application;

public class ApplicationDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	int taskid;

	String applicationMessage, applierID;

	public ApplicationDTO(){
		
	}
	
	public ApplicationDTO(ApplicationDTO dto){
		this.taskid = dto.taskid;
		this.applierID = dto.applierID;
		this.applicationMessage = dto.applicationMessage;
	}
	
	public static ApplicationDTO fromModel(Application app)
	{
		ApplicationDTO dto = new ApplicationDTO();
		dto.setApplierid(app.getApplierId());
		dto.setTaskid(app.getTaskId());
		dto.setApplicationMessage(app.getApplicationMessage());
		return dto;
	}
	
	public Application getModel(){
		Application app = new Application();
		app.setApplicationMessage(this.getApplicationMessage());
		app.setTaskId(this.taskid);
		app.setApplierId(this.getApplierid());
		return app;
	}
	
	public String getApplierid() {
		return applierID;
	}
	
	public ApplicationDTO setApplierid(String applierID) {
		this.applierID = applierID;
		return this;
	}
	
	public int getTaskid() {
		return taskid;
	}

	public ApplicationDTO setTaskid(int i) {
		this.taskid = i;
		return this;
	}

	public String getApplicationMessage() {
		return applicationMessage;
	}

	public ApplicationDTO setApplicationMessage(String applicationMessage) {
		this.applicationMessage = applicationMessage;
		return this;
	}

}
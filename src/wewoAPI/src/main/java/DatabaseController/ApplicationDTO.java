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
	
	public ApplicationDTO(ApplicationDTO dto){
		this.taskid = dto.taskid;
		this.applierid = dto.applierid;
		this.applicationMessage = dto.applicationMessage;
	}
	
	public static ApplicationDTO fromModel(Application app)
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
	
	public ApplicationDTO setApplierid(String applierid) {
		this.applierid = applierid;
		return this;
	}
	
	public String getTaskid() {
		return taskid;
	}

	public ApplicationDTO setTaskid(String taskid) {
		this.taskid = taskid;
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
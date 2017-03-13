package DatabaseController;

import java.io.Serializable;

public class TaskTagsDTO implements Serializable{
	
	int TagID;
	String TaskID;
	
	public TaskTagsDTO(){
		
	}
	
	public TaskTagsDTO(int tagid, String taskid){
		this.TagID = tagid;
		this.TaskID = taskid;
	}
	
	public TaskTagsDTO(TaskTagsDTO tas){
		this.TagID = tas.getTagID();
		this.TaskID = tas.getTaskID();
	}
	
	public int getTagID(){return TagID;}
	public void setTagID(int TagID){this.TagID = TagID;}
	public String getTaskID(){return TaskID;}
	public void setTagID(String TaskID){this.TaskID = TaskID;}

}

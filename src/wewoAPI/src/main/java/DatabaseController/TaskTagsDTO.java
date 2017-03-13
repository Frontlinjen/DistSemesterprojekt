package DatabaseController;

import java.io.Serializable;

public class TaskTagsDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int TagID, TaskID;

	public TaskTagsDTO(int tagid, int taskid){
		this.TagID = tagid;
		this.TaskID = taskid;
	}
	
	public TaskTagsDTO(TaskTagsDTO tas){
		this.TagID = tas.getTagID();
		this.TaskID = tas.getTaskID();
	}
	
	public int getTagID(){
		return TagID;
		}
	public void setTagID(int TagID){
		this.TagID = TagID;
		}
	public int getTaskID(){
		return TaskID;
		}
	public void setTaskID(int TaskID){
		this.TaskID = TaskID;
		}

}

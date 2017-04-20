	package DatabaseController;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import modelPOJO.Task;

public class TaskDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	String  title, description, street, creatorid;
	int id, price, ect, views, zipaddress;
	int supplies, urgent;
	Date created, edited;
	List<Integer> tags;
	
	public TaskDTO(){
		
	}
	public TaskDTO(TaskDTO tas){
		this.id = tas.getId();
		this.title = tas.getTitle();
		this.description = tas.getDescription();
		this.price = tas.getPrice();
		this.ect = tas.getEct();
		this.supplies = tas.getSupplies();
		this.urgent = tas.getUrgent();
		this.views = tas.getViews();
		this.street = tas.getStreet();
		this.zipaddress = tas.getZipaddress();
		this.created = tas.getCreated();
		this.edited = tas.getEdited();
		this.creatorid = tas.getCreatorId();
	}
	
	//Should never contain fields that the user can't set
	public static TaskDTO fromModel(Task task)
	{
		TaskDTO dto = new TaskDTO();
		//dto.setCreatorId(task.getCreatorid());
		dto.setDescription(task.getDescription());
		dto.setEct(task.getETC());
		dto.setPrice(task.getPrice());
		dto.setStreet(task.getStreet());
		dto.setSupplies(task.isSupplies());
		dto.setTags(task.getTags());
		dto.setTitle(task.getTitle());
		dto.setUrgent(task.isUrgent());
		return dto;
	}
	
	public Task getModel(){
		Task task = new Task();
		task.setCreatorid(this.getCreatorId());
		task.setDescription(this.getDescription());
		task.setETC(this.getEct());
		task.setPrice(this.getPrice());
		task.setStreet(this.getStreet());
		task.setSupplies(this.getSupplies());
		task.setTags(this.getTags());
		task.setTitle(this.getTitle());
		task.setUrgent(this.getUrgent());
		return task;
	}
	
	
	public int getId(){return id;}
	public TaskDTO setId(int id){this.id = id; return this;}
	public String getTitle(){return title;}
	public TaskDTO setTitle(String title){this.title = title; return this;}
	public String getDescription(){return description;}
	public TaskDTO setDescription(String description){this.description = description; return this;}
	public int getPrice(){return price;}
	public TaskDTO setPrice(int price){this.price = price; return this;}
	public int getEct(){return ect;}
	public TaskDTO setEct(int ect){this.ect = ect; return this;}
	public int getSupplies(){return supplies;}
	public TaskDTO setSupplies(int supplies){this.supplies = supplies; return this;}
	public int getUrgent(){return urgent;}
	public TaskDTO setUrgent(int urgent){this.urgent = urgent; return this;}
	public int getViews(){return views;}
	public TaskDTO setViews(int views){this.views = views; return this;}
	public String getStreet(){return street;}
	public TaskDTO setStreet(String street){this.street = street; return this;}
	public int getZipaddress(){return zipaddress;}
	public TaskDTO setZipaddress(int zipaddress){this.zipaddress = zipaddress; return this;}
	public Date getCreated(){return created;}
	public TaskDTO setCreated(Date created){this.created = created; return this;}
	public Date getEdited(){return edited;}
	public TaskDTO setEdited(Date edited){this.edited = edited; return this;}
	public String getCreatorId(){return creatorid;}
	public TaskDTO setCreatorId(String creatorid){this.creatorid = creatorid; return this;}
	public void setTags(List<Integer> tags) { this.tags = tags; }
	public List<Integer> getTags() { return tags; }
}

	package DatabaseController;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

public class TaskDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	String  title, description, street, creatorid;
	int id, price, ect, views, zipaddress;
	int supplies, urgent;
	Date created, edited;
	
	public TaskDTO(){
		
	}
	
	public TaskDTO(int id, String title, String description, int price, int ect, 
			boolean supplies, boolean urgent, int views, String street, int zipaddress, Date created, 
			Date edited, String creatorid)
	{	
		this.id = id;
		this.title = title;
		this.description = description;
		this.price = price;
		this.ect = ect;
		this.supplies = supplies ? 1 : 0;
		this.urgent = urgent ? 1 : 0;
		this.views = views;
		this.street = street;
		this.zipaddress = zipaddress;
		this.created = created;
		this.edited = edited;
		this.creatorid = creatorid;
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
	
	public int getId(){return id;}
	public void setId(int id){this.id = id;}
	public String getTitle(){return title;}
	public void setTitle(String title){this.title = title;}
	public String getDescription(){return description;}
	public void setDescription(String description){this.description = description;}
	public int getPrice(){return price;}
	public void setPrice(int price){this.price = price;}
	public int getEct(){return ect;}
	public void setEct(int ect){this.ect = ect;}
	public int getSupplies(){return supplies;}
	public void setSupplies(int supplies){this.supplies = supplies;}
	public int getUrgent(){return urgent;}
	public void setUrgent(int urgent){this.urgent = urgent;}
	public int getViews(){return views;}
	public void setViews(int views){this.views = views;}
	public String getStreet(){return street;}
	public void setStreet(String street){this.street = street;}
	public int getZipaddress(){return zipaddress;}
	public void setZipaddress(int zipaddress){this.zipaddress = zipaddress;}
	public Date getCreated(){return created;}
	public void setCreated(Date created){this.created = created;}
	public Date getEdited(){return edited;}
	public void setEdited(Date edited){this.edited = edited;}
	public String getCreatorId(){return creatorid;}
	public void setCreatorId(String creatorid){this.creatorid = creatorid;}
}

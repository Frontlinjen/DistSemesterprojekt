package modelPOJO;

import java.util.List;

public class Task {
	public List<Integer> getTags() {
		return tags;
	}
	public void setTags(List<Integer> tags) {
		this.tags = tags;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getETC() {
		return ETC;
	}
	public void setETC(int eTC) {
		ETC = eTC;
	}
	public boolean isSupplies() {
		if(supplies == 1)
			return true;
		else
			return false;
	}
	public void setSupplies(int supplies) {
		if(supplies == 1)
			this.supplies = 1;
		else
			this.supplies = 0;
	}
	public boolean isUrgent() {
		if(urgent == 1 )
			return true;
		else
			return false;
	}
	public void setUrgent(int urgent) {
		if(urgent == 1)
			this.urgent = 1;
		else
			this.urgent = 0;
	}
	public int getViews() {
		return views;
	}
	public void setViews(int views) {
		this.views = views;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public int getZipaddress() {
		return zipaddress;
	}
	public void setZipaddress(int zipaddress) {
		this.zipaddress = zipaddress;
	}
	public String getCreatorid() {
		return creatorid;
	}
	public void setCreatorid(String creatorid) {
		this.creatorid = creatorid;
	}
	int ID;
	String title;
	String description;
	int price;
	int ETC;
	int supplies;
	int urgent;
	int views;
	String street;
	int zipaddress;
	String creatorid;
	List<Integer> tags;
}

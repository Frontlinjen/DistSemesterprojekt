package modelPOJO;

import java.util.List;

public class Task {
	public List<Integer> getTags() {
		return tags;
	}
	public void setTags(List<Integer> tags) {
		this.tags = tags;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
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
		return supplies;
	}
	public void setSupplies(boolean supplies) {
		this.supplies = supplies;
	}
	public boolean isUrgent() {
		return urgent;
	}
	public void setUrgent(boolean urgent) {
		this.urgent = urgent;
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
	String ID;
	String title;
	String description;
	int price;
	int ETC;
	boolean supplies;
	boolean urgent;
	int views;
	String street;
	int zipaddress;
	String creatorid;
	List<Integer> tags;
}

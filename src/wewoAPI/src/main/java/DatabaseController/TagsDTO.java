package DatabaseController;

import java.io.Serializable;

public class TagsDTO implements Serializable{

	int ID, parentTagID;
	String name;
	
	public TagsDTO(){
		
	}
	
	public TagsDTO(int ID, String name, int parentTagID){
		this.ID = ID;
		this.name = name;
		this.parentTagID = parentTagID;
	}
	
	public TagsDTO(TagsDTO tag){
		this.ID = tag.getID();
		this.name = tag.getName();
		this.parentTagID = tag.getParentTagID();
	}
	
	public int getID(){return ID;}
	public void setID(int ID){this.ID = ID;}
	public String getName(){return name;}
	public void setName(String name){this.name = name;}
	public int getParentTagID(){return parentTagID;}
	public void setParentTagID(int parentTagID){this.parentTagID = parentTagID;}
	
}

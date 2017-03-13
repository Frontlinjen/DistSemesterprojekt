package modelPOJO;

import java.util.List;

public class FindDataObject {

	List<Integer> tags;
	int index, max;
	
	
	public List<Integer> getTags() {
		return tags;
	}
	public void setTags(List<Integer> tags) {
		this.tags = tags;
	}
	
	public int getIndex(){
		return index;
	}
	
	public void setIndex(int index){
		this.index = index;
	}
	
	public int getMax(){
		return max;
	}
	
	public void setMax(int max){
		this.max = max;
	}
	
}

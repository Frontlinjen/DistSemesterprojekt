package modelPOJO;

public class IDWrapper<T, I> {
	I ID;
	T result;
	
	
	public I getID() {
		return ID;
	}
	public void setID(I iD) {
		ID = iD;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}	
}

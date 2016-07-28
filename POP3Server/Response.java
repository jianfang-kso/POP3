
public interface Response {
	public void setMessage(String msg, boolean status);
	
	// Always override Object default toString method
	public String toString();
}

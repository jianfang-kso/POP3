public class SingleLineResponse implements Response {
	// Maximum characters available for a response
	public static final int MAX_CHAR = 512;
	
	private static final String OK = "+OK ";
	private static final String ERR = "-ERR ";
	
	private StringBuilder message;
	
	public SingleLineResponse() {
		this.message = new StringBuilder();
	};
	
	public SingleLineResponse(String msg, boolean status) {
		this.setMessage(msg, status);
	}
	
	@Override
	public void setMessage(String msg, boolean status) {
		// Initialize message
		this.message = new StringBuilder();
		
		if (status)
			this.message.append(SingleLineResponse.OK);
		else
			this.message.append(SingleLineResponse.ERR);
		
		this.message.append(msg);
	}
	
	@Override
	public String toString() {
		// Ensure maximum response length
		if (this.message.length() >= SingleLineResponse.MAX_CHAR)
			this.message.delete(SingleLineResponse.MAX_CHAR - 1, this.message.length());
		
		// Add CRLF to end of every response
		this.message.append("\r\n");
		
		return this.message.toString();
	}
}


public class MultiLineResponse implements Response {	
	private static final String OK = "+OK ";
	private static final String ERR = "-ERR ";
	
	// Every multi-line message has a single line response sent first
	private SingleLineResponse slr = new SingleLineResponse();
	private StringBuilder message = new StringBuilder();
	
	public MultiLineResponse() {}
	
	public MultiLineResponse(String singleMessage, String msg, boolean status) {
		this.slr.setMessage(singleMessage, status);
		this.setMessage(msg, status);
	}
	
	@Override
	public void setMessage(String message, boolean status) {
		// Status ignored as it's set in the SLR
		
		// Replace all occurrences of newline with CRLF pair
		this.message.append(message.replaceAll("\\r\\n|\\r|\\n", "\r\n"));

	}
	
	@Override
	public String toString() {
		// End multi-line response with "CRLF pair . CRLF pair"
		this.message.append("\r\n.\r\n");
		
		// Return resultant string together with single line response
		return slr.toString() + this.message.toString();
	}
}

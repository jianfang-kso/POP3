
import java.io.UnsupportedEncodingException;

public class Mail {
	private String uid;
	private int mailID;
	private String text;
	private int octet = 0;
	
	public Mail(String uid, int mid, String message) {
		this.uid = uid;
		this.mailID = mid;
		this.text = message;
		try {
			byte[] byteArray = message.getBytes("ASCII");
			this.octet = byteArray.length;
		} catch (UnsupportedEncodingException e) {
			System.out.println("ASCII not supported");
			e.printStackTrace();
		}
		
	}
	
	public String getUniqueID() {
		return this.uid;
	}
	
	public int getMailID() {
		return this.mailID;
	}
	
	public String getText() {
		return this.text;
	}
	
	public int getOctetCount() {
		return this.octet;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if ( ! (o instanceof Mail)) return false;
		
		Mail m = (Mail) o;
		return m.getUniqueID().equals(this.uid);
	}
}

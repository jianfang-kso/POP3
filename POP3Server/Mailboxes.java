
import java.util.concurrent.ConcurrentHashMap;


public class Mailboxes {
	private static Mailboxes mb = new Mailboxes();
	private Model model = Database.getInstance();
	
	private ConcurrentHashMap<String, Mailbox> mailboxes = new ConcurrentHashMap<>();
	
	public static Mailboxes getInstance() {
		return Mailboxes.mb;
	}
	
	// Setup mailbox only if user is newly logged in
	public void setupMailbox(String username) {
		if ( ! this.mailboxes.containsKey(username)) {
			// Initialize new mailbox for username
			Mailbox mailbox = new Mailbox(username);
			
			// Get mails from model
			Mail[] mails = this.model.getMail(username);
			
			// Add all mails to mailbox
			for (int i = 0; i < mails.length; i++)
				mailbox.addMail(mails[i]);
			
			// Add mailbox to list of mailboxes
			this.mailboxes.put(username, mailbox);
		}
	}
	
	public Mailbox getMailbox(String username) {
		Mailbox mb = null;
		
		try {
			mb = this.mailboxes.get(username);
		} catch (NullPointerException e) {
			System.out.println("Mailbox not set up for user: " + username);
			e.printStackTrace();
		}
		
		return mb;
	}
}


import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Mailbox {
	private String username;
	private LinkedHashMap<Mail, Boolean> mails = new LinkedHashMap<>();
	
	public Mailbox(String username) {
		this.username = username;
	}
	
	// Add mail to mailbox
	public synchronized void addMail(Mail m) {
		this.mails.put(m, true);
	}
	
	// Delete mail with mail ID
	public synchronized void deleteMail(int mid) throws MailNotFoundException {
		boolean deleted = false;
		Iterator<Entry<Mail, Boolean>> itr = this.iterator();
		while (itr.hasNext()) {
			Entry<Mail, Boolean> mb = itr.next();
			Mail m = mb.getKey();
			int mailID = mb.getKey().getMailID();
			
			// Check mail ID and mail cannot be deleted beforehand
			if (mid == mailID && mb.getValue() == true) {
				// Simply replacing the value as false to mark as deleted
				this.mails.put(m, false);
				deleted = true;
				break;
			}
		}
		
		if ( ! deleted ) throw new MailNotFoundException();
	}
	
	// Delete mail with Mail as key
	public synchronized void deleteMail(Mail key) {
		this.mails.remove(key);
	}
	
	// Check if mail exists with the mail ID
	public boolean mailExists(int mid) {
		// Check if mail ID exists within mailbox
		boolean exists = false;
		Iterator<Entry<Mail, Boolean>> itr = this.iterator();
		while (itr.hasNext()) {
			Entry<Mail, Boolean> mb = itr.next();
			if (mb.getKey().getMailID() == mid && mb.getValue() == true) {
				exists = true;
				break;
			}
		}
		
		return exists;
	}
	
	// Get mail with mail ID
	public Mail getMail(int mid) throws MailNotFoundException {
		if ( ! this.mailExists(mid)) {
			throw new MailNotFoundException();
		} else {
			
			Mail returnedMail = null;
			Iterator<Entry<Mail, Boolean>> itr = this.iterator();
			
			while(itr.hasNext()) {
				Entry<Mail, Boolean> mb = itr.next();
				Mail m = mb.getKey();
				
				if ( ! mb.getValue()) {
					continue;  // Message has been deleted, skip
				} else {
					if (mid == m.getMailID()) {
						returnedMail = mb.getKey();
					}
				}
			}
			assert (returnedMail != null);
			return returnedMail;
		}
	}

	// Get total number of mails
	public int getNumberOfMails() {
		Iterator<Entry<Mail, Boolean>> itr = this.iterator();
		int sum = 0;
		
		while (itr.hasNext()) {
			Entry<Mail, Boolean> mb = itr.next();
			if ( ! mb.getValue()) {
				// Mail marked as deleted, skip
				continue;
			} else {
				// Mail not deleted, count
				sum++;
			}
		}
		
		return sum;
	}
	
	// Get the total number of octets in mailbox
	public int getSumOfOctets() {
		Iterator<Entry<Mail, Boolean>> itr = this.iterator();
		int sum = 0;
		
		while (itr.hasNext()) {
			Entry<Mail, Boolean> mb = itr.next();
			if ( ! mb.getValue()) {
				// Mail marked as deleted, skip
				continue;
			} else {
				// Mail not deleted, count
				sum += mb.getKey().getOctetCount();
			}
		}
		
		return sum;
	}

	// Reset mails marked as deleted (mainly for RSET command)
	public void resetDeletedMails() {
		Iterator<Entry<Mail, Boolean>> itr = this.iterator();
		while (itr.hasNext()) {
			Entry<Mail, Boolean> mb = itr.next();
			Mail m = mb.getKey();
			this.mails.put(m, true);
		}
	}
	
	// Return the iterator over mailbox
	public Iterator<Entry<Mail, Boolean>> iterator() {
		return this.mails.entrySet().iterator();
	}
}

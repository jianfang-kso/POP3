
import java.util.Iterator;
import java.util.Map.Entry;


public class LISTCommand implements Command {
	private State s;
	private Request req;
	private Response res = new SingleLineResponse();
	private Mailboxes mailboxes = Mailboxes.getInstance();
	
	// Argument needed
	Integer mailID;
	
	public LISTCommand(State inputState, Request inputRequest) {
		this.req = inputRequest;
		this.s = inputState;
	}
	
	@Override
	public void execute() {
		
		if (this.s.getState() != State.TRANSACTION) {
			
			// Not authorized
			this.res.setMessage("use USER command to log in first", false);
			
		} else {
			
			// User authorized, ensure number and format of arguments
			if ( ! this.isSatisfied()) {
				
				// Command not satisfied
				this.res.setMessage("argument provided is invalid", false);
				
			} else {
				
				// Get user's mailbox
				Mailbox userMailbox = this.mailboxes.getMailbox(this.s.getUsername());
				
				// List all mail details or specific mail
				// List all mail details if mailID = null
				
				/*
				 * 
				 * Try to construct example response:
				 * +OK 2 messages (320 octets)
				 * 1 120
				 * 2 200
				 * 
				 * .
				 * 
				 * */
				
				if (this.mailID == null) {
					StringBuilder singleline = new StringBuilder();
					StringBuilder multiline = new StringBuilder();
					
					Iterator<Entry<Mail, Boolean>> itr = userMailbox.iterator();
					while (itr.hasNext()) {
						Entry<Mail, Boolean> mb = itr.next();
						Mail m = mb.getKey();
						int index, octet;
						if ( ! mb.getValue()) {
							// Message deleted, skip
							continue;
						} else {
							index = m.getMailID();
							octet = m.getOctetCount();
							
							multiline.append(index + " " + octet + "\n");
						}
					}
					
					singleline.append(userMailbox.getNumberOfMails() + " messages (" + userMailbox.getSumOfOctets() + " octets)");
					
					// Redefine response to a multi-line response
					this.res = new MultiLineResponse(singleline.toString(), multiline.toString(), true);
					
				} else {
					
					// List specific mail detail
					try {
						Mail m = userMailbox.getMail(this.mailID);
						this.res.setMessage(this.mailID + " " + m.getOctetCount(), true);
					} catch (MailNotFoundException e) {
						// Mail ID specified does not exist
						this.res.setMessage("no such message, only " + userMailbox.getNumberOfMails() + " messages in maildrop", false);
					}
				}
			}
		}
	}
	@Override
	public boolean isSatisfied() {
		int count = this.req.getCountofArgs();
		boolean satisfied = (count <= 1);
		
		if (satisfied && count == 1) {
			
			// Parse argument given
			try {
				this.mailID = Integer.parseInt(this.req.getArgsIndexOf(0), 10);
			} catch (NumberFormatException e) {
				// Ensure mailID is invalid
				this.mailID = null;
				satisfied = false;
			}
		}
		
		return satisfied;
	}

	@Override
	public String getResponseMessage() {
		return this.res.toString();
	}

}

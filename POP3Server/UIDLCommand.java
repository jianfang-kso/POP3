
import java.util.Iterator;
import java.util.Map.Entry;


public class UIDLCommand implements Command {
	private State s;
	private Request req;
	private Response res = new SingleLineResponse();
	private Mailboxes mailboxes = Mailboxes.getInstance();
	
	// Argument needed
	Integer mailID = null;
	
	public UIDLCommand(State inputState, Request inputRequest) {
		this.req = inputRequest;
		this.s = inputState;
	}

	@Override
	public void execute() {
		if (this.s.getState() != State.TRANSACTION) {
			
			// User not logged in
			this.res.setMessage("use USER command to log in first", false);
			
		} else {
			
			// User logged in, check satisfiability
			if ( ! this.isSatisfied()) {
				
				// Argument invalid
				this.res.setMessage("UIDL command expects a message number", false);
				
			} else {

				// User's mailbox
				Mailbox userMailbox = this.mailboxes.getMailbox(this.s.getUsername());
				
				if (this.mailID == null) {
					
					// List all unique IDs in mailbox
					StringBuilder multi = new StringBuilder();
					
					Iterator<Entry<Mail, Boolean>> itr = userMailbox.iterator();
					while (itr.hasNext()) {
						Entry<Mail, Boolean> mb = itr.next();
						if (mb.getValue()) {
							Mail m = mb.getKey();
							multi.append(m.getMailID() + " " + m.getUniqueID() + "\n");
						}
					}
					
					this.res = new MultiLineResponse("", multi.toString(), true);
					
				} else {
					
					// Try to get unique ID
					try {
						Mail m = userMailbox.getMail(this.mailID);
						this.res.setMessage(this.mailID + " " + m.getUniqueID(), true);
					} catch (MailNotFoundException e) {
						// Cannot find such mail
						this.res.setMessage("no such message", false);
					}
				}
			}
		}
	}

	@Override
	public boolean isSatisfied() {
		boolean satisfied = this.req.getCountofArgs() <= 1;
		
		if (satisfied && this.req.getCountofArgs() == 1) {
			try {
				this.mailID = Integer.parseInt(this.req.getArgsIndexOf(0), 10);
			} catch (NumberFormatException e) {
				// Argument provided is invalid
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

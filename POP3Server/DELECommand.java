

public class DELECommand implements Command {
	private State s;
	private Request req;
	private Response res = new SingleLineResponse();
	private Mailboxes mailboxes = Mailboxes.getInstance();
	
	// Argument needed
	Integer mailID = null;
	
	public DELECommand(State inputState, Request inputRequest) {
		this.req = inputRequest;
		this.s = inputState;
	}

	@Override
	public void execute() {
		if (this.s.getState() != State.TRANSACTION) {
			
			// Bad request, not logged in
			this.res.setMessage("use USER command to log in first", false);
			
		} else {
			
			if ( ! this.isSatisfied()) {
				
				// Argument not satisfied
				this.res.setMessage("DELE command expects an integer of mail to delete", false);
				
			} else {
				if (this.mailID == null) {
					
					// Argument invalid
					this.res.setMessage("argument provided is invalid", false);
					
				} else {
					
					// Try to delete message
					Mailbox userMailbox = this.mailboxes.getMailbox(this.s.getUsername());
					try {
						// Try to delete mail
						userMailbox.deleteMail(this.mailID);
						
						// Mail marked as deleted
						this.res.setMessage("mail deleted", true);
					} catch (MailNotFoundException e) {
						// Mail cannot be marked as deleted
						this.res.setMessage("cannot delete mail", false);
					}
				}
			}
		}
	}

	@Override
	public boolean isSatisfied() {
		boolean satisfied = this.req.getCountofArgs() == 1;
		
		if (satisfied) {
			try {
				this.mailID = Integer.parseInt(this.req.getArgsIndexOf(0), 10);
			} catch (NumberFormatException e) {
				// Ensure that mailID is null as argument cannot be parsed
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

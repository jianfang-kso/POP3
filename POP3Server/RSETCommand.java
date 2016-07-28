

public class RSETCommand implements Command {
	private State s;
	private Request req;
	private Response res = new SingleLineResponse();
	private Mailboxes mailboxes = Mailboxes.getInstance();
	
	// Argument needed
	Integer mailID = null;
	
	public RSETCommand(State inputState, Request inputRequest) {
		this.req = inputRequest;
		this.s = inputState;
	}
	
	@Override
	public void execute() {
		if (this.s.getState() != State.TRANSACTION) {
			
			// Not logged in
			this.res.setMessage("use USER command to log in first", false);
			
		} else {
			
			// Logged in, reset all deleted mails
			if ( ! this.isSatisfied()) {
				
				this.res.setMessage("RSET command expects no arguments", false);
				
			} else {
				
				// Reset all mails
				Mailbox userMailbox = this.mailboxes.getMailbox(this.s.getUsername());
				userMailbox.resetDeletedMails();
				this.res.setMessage("maildrop has " + userMailbox.getNumberOfMails() + " messages ("
						+ userMailbox.getSumOfOctets() + " octets)", true);
			}
		}
	}

	@Override
	public boolean isSatisfied() {
		return (this.req.getCountofArgs() == 0);
	}

	@Override
	public String getResponseMessage() {
		return this.res.toString();
	}

}

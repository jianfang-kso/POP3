

public class STATCommand implements Command {
	private State s;
	private Request req;
	private Response res = new SingleLineResponse();
	private Mailboxes mailboxes = Mailboxes.getInstance();
	
	public STATCommand(State inputState, Request inputRequest) {
		this.req = inputRequest;
		this.s = inputState;
	}

	@Override
	public void execute() {
		if (this.s.getState() != State.TRANSACTION) {
			
			// Bad request
			this.res.setMessage("use USER command to log in first", false);
			
		} else {
			
			if ( ! isSatisfied()) {
				
				// Command not satisfied
				this.res.setMessage("STAT command expects no arguments", false);
				
			} else {

				// Calculate count of mails and sum of octet
				Mailbox userMailbox = this.mailboxes.getMailbox(this.s.getUsername());

				// List all mails
				int count = userMailbox.getNumberOfMails();
				int sumOfOctet = userMailbox.getSumOfOctets();
				
				// Set response
				this.res.setMessage(count + " " + sumOfOctet, true);
			}		
		}
	}


	@Override
	public boolean isSatisfied() {
		return ! (this.req.getCountofArgs() > 0);
	}


	@Override
	public String getResponseMessage() {
		return this.res.toString();
	}
}

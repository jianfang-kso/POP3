

public class RETRCommand implements Command {
	private State s;
	private Request req;
	private Response res = new SingleLineResponse();
	private Mailboxes mailboxes = Mailboxes.getInstance();
	
	// Argument needed
	Integer mailID = null;
	
	public RETRCommand(State inputState, Request inputRequest) {
		this.req = inputRequest;
		this.s = inputState;
	}

	@Override
	public void execute() {
		if (this.s.getState() != State.TRANSACTION) {
			
			// Not logged in
			this.res.setMessage("use USER command to log in first", false);
		} else {
			
			// User logged in, check satisfiability
			if ( ! this.isSatisfied()) {
				
				// Arguments provided are not satisfying
				this.res.setMessage("argument provided is invalid", false);
				
			} else {
				
				// Try to retrieve mail's contents
				Mailbox userMailbox = this.mailboxes.getMailbox(this.s.getUsername());
				try {
					Mail m = userMailbox.getMail(this.mailID);
					
					StringBuilder single = new StringBuilder();
					single.append(m.getOctetCount() + " octets");
					
					this.res = new MultiLineResponse(single.toString(), m.getText(), true);
					
				} catch (MailNotFoundException e) {
					this.res.setMessage("no such message", false);
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

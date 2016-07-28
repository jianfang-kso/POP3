

public class TOPCommand implements Command {
	private State s;
	private Request req;
	private Response res = new SingleLineResponse();
	private Mailboxes mailboxes = Mailboxes.getInstance();
	
	// Argument needed
	Integer mailID = null;
	Integer numberOfLines = null;
	
	public TOPCommand(State inputState, Request inputRequest) {
		this.req = inputRequest;
		this.s = inputState;
	}

	@Override
	public void execute() {
		if (this.s.getState() != State.TRANSACTION) {
			
			// User not logged in
			this.res.setMessage("use USER command to log in first", false);
			
		} else {
			
			// Check satisfiability
			if ( ! this.isSatisfied()) {
				
				// Argument invalid
				this.res.setMessage("TOP command expects a message number and a line count", false);
				
			} else {
				
				// Write top few lines of message
				Mailbox userMailbox = this.mailboxes.getMailbox(this.s.getUsername());
				try {
					Mail m = userMailbox.getMail(this.mailID);
					
					// The multiline response
					StringBuilder multi = new StringBuilder();
					
					String[] lines = m.getText().split("\n");
					int numberOfLinesInMail = lines.length;
					
					if (this.numberOfLines <= numberOfLinesInMail) {
						
						// Print lesser lines
						for (int i = 0; i < this.numberOfLines; i++) {
							multi.append(lines[i] + "\n");
						}
						
					} else {
						
						// Print all lines
						multi.append(m.getText());
						
					}
					
					// Output multi-line response
					this.res = new MultiLineResponse("", multi.toString(), true);
					
				} catch (MailNotFoundException e) {
					this.res.setMessage("mail not found in mailbox", false);
				}
			}
		}
	}

	@Override
	public boolean isSatisfied() {
		boolean satisfied = (this.req.getCountofArgs() == 2);
		
		if (satisfied) {
			try {
				this.mailID = Integer.parseInt(this.req.getArgsIndexOf(0), 10);
				this.numberOfLines = Integer.parseInt(this.req.getArgsIndexOf(1), 10);
				satisfied = satisfied && (numberOfLines >= 0);
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

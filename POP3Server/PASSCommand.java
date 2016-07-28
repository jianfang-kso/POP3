

public class PASSCommand implements Command {
	private State s;
	private Request req;
	private Response res = new SingleLineResponse();
	private Model m = Database.getInstance();
	private Mailboxes mailboxes = Mailboxes.getInstance();
	
	// Argument needed
	String password;
	
	public PASSCommand(State inputState, Request inputRequest) {
		this.req = inputRequest;
		this.s = inputState;
	}

	@Override
	public void execute() {
		if (this.s.getState() != State.AUTHORIZATION) {
			
			this.res.setMessage("user has already logged in", false);
			
		} else {
			
			// Check command satisfiability
			if ( ! this.isSatisfied()) {
				
				// Password not given
				this.res.setMessage("PASS command expects a password", false);
				
			} else {
			
				// Get the password and try to match with user's password
				String username = this.s.getUsername();
				
				if ( ! this.s.getHistory().equals("USER") || username.isEmpty()) {
					
					// Username is not provided
					this.res.setMessage("use USER command to first provide username before PASS to log in", false);
					
				} else {
					
					// Consecutive USER and PASS commands are used, try to login
					this.password = this.req.getRawInput();
					String match = this.m.getPassword(username);
					
					if ( ! this.password.equals(match)) {
						
						// Password does not match for the username
						this.res.setMessage("password does not match", false);
						
					} else {
						
						// Successful login
						
						// Setup mailbox in mailboxes
						this.mailboxes.setupMailbox(username);
						
						// Switch to TRANSACTION state
						this.s.setState(State.TRANSACTION);
						this.res.setMessage("Maildrop locked and ready", true);
						
					}
				}
			}
		}
	}

	@Override
	public String getResponseMessage() {
		return this.res.toString();
	}

	@Override
	public boolean isSatisfied() {
		this.password = this.req.getArgsIndexOf(0);
		return ! this.password.isEmpty();
	}
}

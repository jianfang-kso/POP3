

public class USERCommand implements Command {
	private State s;
	private Request req;
	private SingleLineResponse res = new SingleLineResponse();
	
	String username;
	
	private Model m = Database.getInstance();
	
	public USERCommand(State inputState, Request inputRequest) {
		this.req = inputRequest;
		this.s = inputState;
	}
	
	@Override
	public void execute() {
		int state = s.getState();
		if (state != State.AUTHORIZATION) {
			
			// (i)    User has been authorized, or,
			// (ii)   User has logged out
			if (state == State.TRANSACTION)
				this.res.setMessage("user has already logged in", false);
			else if (state == State.UPDATE)
				this.res.setMessage("updating maildrop", false);
			else
				this.res.setMessage("critical error state not recognized", false);
			
		} else {
			
			if ( ! this.isSatisfied()) {
				
				// Command not satisfied
				this.res.setMessage("USER command expects a username", false);
				
			} else {
				
				// User wants to log in
				// Authenticate user
				this.username = this.req.getArgsIndexOf(0);
				
				// Reset username in state as USER command has been passed
				this.s.setUsername(this.username);
				
				if (this.m.isUser(this.username)) {
					
					// Remember username in state
					this.s.setUsername(this.username);
					
					// Generate response message
					this.res.setMessage("username \"" + this.username + "\" is submitted", true);
					
				} else {
					
					// Non-existent username
					this.res.setMessage("username \"" + this.username + "\" is not found", false);
					
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
		// Ensure that parameter exists
		String username = this.req.getArgsIndexOf(0);
		return ! username.isEmpty();
	}
}

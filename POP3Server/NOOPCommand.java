

public class NOOPCommand implements Command {
	private State s;
	private Request req;
	private Response res = new SingleLineResponse();
	
	
	public NOOPCommand(State inputState, Request inputRequest) {
		this.req = inputRequest;
		this.s = inputState;
	}
	
	@Override
	public void execute() {
		if (this.s.getState() != State.TRANSACTION) {
			
			// Cannot use NOOP command in other states
			this.res.setMessage("command can only be used after logging in", false);
			
		} else {
			
			// Do nothing
			this.res.setMessage("", true);
			
		}
	}

	@Override
	public boolean isSatisfied() {
		return true;
	}

	@Override
	public String getResponseMessage() {
		return this.res.toString();
	}

}

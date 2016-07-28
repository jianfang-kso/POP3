

public class BadCommand implements Command {
	private State s;
	private Response res = new SingleLineResponse();
	
	/**
	 * Returns a bad response if command specified by user is invalid.
	 * @param inputState The state of the user's session
	 */
	public BadCommand(State inputState) {
		this.s = inputState;
	}
	
	/* (non-Javadoc)
	 */
	@Override
	public void execute() {
		this.res.setMessage("Command not recognized", false);
	}

	/* (non-Javadoc)
	 */
	@Override
	public String getResponseMessage() {
		return this.res.toString();
	}

	/* (non-Javadoc)
	 */
	@Override
	public boolean isSatisfied() {
		// Bad command is always satisfied
		return true;
	}
	
}

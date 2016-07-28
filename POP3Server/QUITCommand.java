

import java.util.Iterator;
import java.util.Map.Entry;


public class QUITCommand implements Command {
	private State s;
	private Request req;
	private Response res = new SingleLineResponse();
	private Mailboxes mbs = Mailboxes.getInstance();
	private Model m = Database.getInstance();
	
	public QUITCommand(State inputState, Request inputRequest) {
		this.req = inputRequest;
		this.s = inputState;
	}

	@Override
	public void execute() {
		switch (this.s.getState()) {
			
		case State.AUTHORIZATION:
			// Reset state
			this.s.resetUsername();
			
			// Quit in authorization state
			this.res.setMessage("Terminating connection", true);
			
			// Terminate connection
			Thread.currentThread().interrupt();
			break;
				
		case State.TRANSACTION:
			// Quit from TRANSACTION to UPDATE status
			this.s.setState(State.UPDATE);
			
			// Re-execute quit command in UPDATE state
			this.execute();
			break;
			
		case State.UPDATE:
			// Perform database transactions and close TCP connection
			Mailbox mb = this.mbs.getMailbox(this.s.getUsername());
			Iterator<Entry<Mail, Boolean>> itr = mb.iterator();
			while (itr.hasNext()) {
				Entry<Mail, Boolean> entry = itr.next();
				if ( ! entry.getValue()) {	// Mail is deleted as it's marked as false
					// Delete mail in database
					this.m.deleteMail(entry.getKey().getMailID());
					
					// Delete from mailbox to maintain consistency
					mb.deleteMail(entry.getKey());
				}
			}
			
			// Give OK response before quitting
			this.res.setMessage("Signing off", true);
			
			// Interrupt thread to terminate connection
			Thread.currentThread().interrupt();
			break;
			
		default:
			// Unrecognized state, critical failure
			System.out.println("Failed to recognize state. Critical failure. System exiting...");
			System.exit(1);
			break;
			
		}
	}

	@Override
	public String getResponseMessage() {
		return this.res.toString();
	}

	@Override
	public boolean isSatisfied() {
		// Is always satisfied
		return true;
	}

}

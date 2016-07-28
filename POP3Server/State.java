/**
 * @author William
 *
 */
public class State {
	// The representation of different states
	public static final int AUTHORIZATION = 1;
	public static final int TRANSACTION = 2;
	public static final int UPDATE = 3;
	
	// Current status
	private int status;
	
	// Individual user's state (properties that need to be remembered)
	private String history = "";
	private String username = "";
	
	public State() {
		// New connection, always set to authorize first
		this.status = State.AUTHORIZATION;
	}
	
	/**
	 * Get the state which the user is currently in.
	 * @return Returns the state of the current user (AUTHORIZATION, TRANSACTION or UPDATE).
	 */
	public int getState() {
		return this.status;
	}
	
	/**
	 * Set the state of the user. Used when after performing certain commands such as logging in
	 * (AUTHORIZATION -> TRANSACTION) or quitting (TRANSACTION -> UPDATE).
	 * @param newStatus Set the current state to user given state.
	 */
	public void setState(int newStatus) {
		this.status = newStatus;
	}
	
	/**
	 * Used during USER command. State needs to remember the username passed in before PASS command is used.
	 * @param user The username to be remembered.
	 */
	public void setUsername(String user) {
		// Replace all space occurrences with nothing
		user.replace(" ", "");
		this.username = user;
	}
	
	/**
	 * Used during PASS command. Gets the username that was passed by USER command in order to perform authorisation.
	 * @return
	 */
	public String getUsername() {
		assert ( ! this.username.isEmpty());
		return this.username;
	}
	
	/**
	 * Sets the previous command ran. Useful in certain contexts when two commands are given and the latter command can
	 * only succeed if and only if it is run directly after the former command.
	 * @param h Previous command ran.
	 */
	public void setHistory(String h) {
		this.history = h;
	}
	
	/**
	 * Gets the previous command ran.
	 * @return The previous command ran.
	 */
	public String getHistory() {
		return this.history;
	}
	
	/**
	 * Resets the username in the state.
	 */
	public void resetUsername() {
		this.username = "";
	}
}

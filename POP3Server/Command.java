public interface Command {
	/**
	 * Executes the command specified by the user. Commands that are implemented:
	 * <ul>
	 * 	 	<li>DELE</li>
	 *  	<li>LIST</li>
	 *  	<li>NOOP</li>
	 *  	<li>PASS</li>
	 *  	<li>QUIT</li>
	 *  	<li>RETR</li>
	 *  	<li>RSET</li>
	 *  	<li>STAT</li>
	 *  	<li>TOP</li>
	 *  	<li>UIDL</li>
	 *  	<li>USER</li>
	 * </ul>
	 */
	public void execute();
	public boolean isSatisfied();
	public String getResponseMessage();
}



public interface Model {
	
	// Check if user is in database
	public boolean isUser(String username);
	
	// Get the user ID
	public int getUserID(String username);
	
	// Return the password of given username
	public String getPassword(String username);
	
	// Return the list of mails of given username
	public Mail[] getMail(String username);
	
	// Delete mail
	public void deleteMail(int mailID);
}

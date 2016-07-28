
public class Request {
	private String command;
	private String[] argv;
	private String rawInput;
	
	public Request(String input) {
		// Trim input to deal with leading and trailing whitespaces
		input = input.trim();
		
		// Split input according to spaces
		String[] tmp = input.split(" ");
		
		// Read first index
		// Command is case-insensitive
		this.command = tmp[0].toUpperCase();
		
		// Set argument array length
		this.argv = new String[tmp.length - 1];
		
		// Copy to argument array
		for (int i = 1; i < tmp.length; i++)
			this.argv[i - 1] = tmp[i];
		
		// Construct raw input
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < tmp.length; i++) {
			sb.append(tmp[i] + " ");
		}
		this.rawInput = sb.toString().trim();
	}
	
	// Return the command parsed
	public String getCommand() {
		return this.command;
	}
	
	// Return the specific index of the argument array
	public String getArgsIndexOf(int index) {
		if (index < argv.length)
			return argv[index];
		else
			return "";
	}
	
	// Get the length of argument array
	public int getCountofArgs() {
		return argv.length;
	}
	
	// Get the raw arguments given by user (excluding the command section)
	public String getRawInput() {
		return this.rawInput;
	}
}

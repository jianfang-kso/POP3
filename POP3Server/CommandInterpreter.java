

/*
Test username: test
Test password: password
*/

/*
Command design pattern is used because the use case here is perfectly suitable for the pattern.
Every request by a user is wrapped in an object and the invoker, in this case, CommandInterpreter
looks for the appropriate objects (the different *Command classes)invokes the different commands 
based on the request.
*/

public class CommandInterpreter {
	private State s;
	private Request req;
	private Command cmd;
	
	public CommandInterpreter() {
		this.s = new State();
	}
	
	public void executeCommand() {
		this.cmd.execute();
	}

	public String handleInput(String input) {
		// Every single call to this method is a new request
		this.req = new Request(input);
		
		// Parse the command through the Request object
		String command = this.req.getCommand();

		// Identify command to be executed
		switch (command) {
		
		case "USER":
			this.cmd = new USERCommand(this.s, this.req);
			break;
			
		case "PASS":
			this.cmd = new PASSCommand(this.s, this.req);
			break;
		
		case "STAT":
			this.cmd = new STATCommand(this.s, this.req);
			break;
			
		case "LIST":
			this.cmd = new LISTCommand(this.s, this.req);
			break;
			
		case "RETR":
			this.cmd = new RETRCommand(this.s, this.req);
			break;
			
		case "DELE":
			this.cmd = new DELECommand(this.s, this.req);
			break;
			
		case "RSET":
			this.cmd = new RSETCommand(this.s, this.req);
			break;
			
		case "TOP":
			this.cmd = new TOPCommand(this.s, this.req);
			break;
			
		case "UIDL":
			this.cmd = new UIDLCommand(this.s, this.req);
			break;
			
		case "NOOP":
			this.cmd = new NOOPCommand(this.s, this.req);
			break;
			
		case "QUIT":
			this.cmd = new QUITCommand(this.s, this.req);
			break;
			
		default:
			this.cmd = new BadCommand(this.s);
			break;
			
		}
		
		// Execute command no matter satifiability (checked in each individual command)
		this.executeCommand();
		
		// Set previous command ran
		this.s.setHistory(command);
		
		// Return server's response message
		return this.cmd.getResponseMessage();
	}
}

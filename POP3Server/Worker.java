
import java.net.Socket;
import java.io.*;

public class Worker implements Runnable {
	private Socket conn;
	private BufferedReader inReader;
	private BufferedWriter outWriter;

	private CommandInterpreter ci = new CommandInterpreter();
	
	public Worker(Socket sck) {
		this.conn = sck;
	}

	@Override
	public void run() {
		try {
			this.establishConnection();
			this.sendReadySignal();
			this.communicate();
		} catch (IOException e) {
			System.out.println("Failed to establish connection with client" + this.conn.getInetAddress().getAddress().toString());
		}
	}
	
	private void establishConnection() throws IOException {
		// Establish I/O stream with client
		this.inReader = new BufferedReader(new InputStreamReader(this.conn.getInputStream()));
		this.outWriter = new BufferedWriter(new OutputStreamWriter(this.conn.getOutputStream()));
	}
	
	private void sendReadySignal() {
		this.sendData(new SingleLineResponse("POP3Server ready", true).toString());
	}
	
	private void communicate() {
		try {
			String req;
			while (true) {
				try {
					if ((req = this.inReader.readLine()) != null) {
						String res = this.ci.handleInput(req);
						this.sendData(res);
					}
				} catch (IOException e) {
					// Connection lost, try to terminate connection and end thread
					break;
				}
				
				if (Thread.currentThread().isInterrupted()) {
					// Interrupted exception issued from Command Interpreter
					// QUIT command issued, terminate connection below
					break;
				}
			}
		} finally {
			this.terminateConnection();
		}
	}
	
	private void terminateConnection() {
		try {
			this.inReader.close();
			this.outWriter.close();
			this.conn.close();
		} catch (IOException | NullPointerException e) {
			// Expected exceptions, ignore
			// Doesn't matter anyway as connection needs to be terminated
		}
	}
	
	private void sendData(String msg) {
		try {
			this.outWriter.write(msg);
			this.outWriter.flush();
		} catch (IOException e) {
			// Connection is lost, interrupt the thread to stop the thread
			Thread.currentThread().interrupt();
		}
	}
}

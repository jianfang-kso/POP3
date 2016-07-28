
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class POP3Server {
	// RFC specifies port 110 but we choose to use 8080 here
//	private static final int port = 110;
	private static final int port = 8080;
	
	// Wait for 2 seconds before force shutdown of executor service
	private static final int SHUTDOWN_BUFFER_TIME = 2;
	
	// Let executor service manipulate thread as this is safer
	private final ExecutorService executor = Executors.newCachedThreadPool();
	private ServerSocket server = null;

	public static void main(String[] args) {
		POP3Server server = new POP3Server();
		server.run();
	}
	
	public void run() {
		// First add shutdown hook to ensure clean shutdown
		this.addShutdownHook();
		
		System.out.println("Starting server...");
		try {
			this.server = new ServerSocket(POP3Server.port);
			System.out.println("Listening to port " + POP3Server.port + "...");
			
			// Start listening to socket
			while (true) {
				try {
					Socket conn = server.accept();
					this.executor.execute(new Worker(conn));
				} catch (EOFException e) {
					System.out.println("Server terminated");
				}
			}
		} catch (IOException e) {
			System.out.println("Error trying to start server");
			e.printStackTrace();
		}
	}
	
	private void addShutdownHook() {
		// Shutdown the executor service
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// Try to shutdown executor service
				POP3Server.this.executor.shutdown();
				try {
					// Force shutdown executor service
					if ( ! POP3Server.this.executor.awaitTermination(POP3Server.SHUTDOWN_BUFFER_TIME, TimeUnit.SECONDS))
						POP3Server.this.executor.shutdownNow();
				} catch (InterruptedException e) {
					System.out.println("Interrupted while forcing shutdown on executor service");
				}
				
				// Shutdown database connection
				Database.getInstance().terminate();
			}
		});
	}
}


import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;

import java.awt.GridLayout;
import java.awt.Dimension;

import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextPane;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JScrollPane;
import javax.swing.JTextField;


public class UserInterface extends JFrame implements Runnable {
	private Socket client = null;
	private BufferedReader br = null;
	private PrintWriter pw = null;
	private boolean connected = false;
	
	ExecutorService es = null;
	
	private JTextArea output;
	private JTextPane input;
	
	private JButton userCmdBtn;
	private JButton passCmdBtn;
	private JButton quitCmdBtn;
	private JButton statCmdBtn;
	private JButton listCmdBtn;
	private JButton retrCmdBtn;
	private JButton deleCmdBtn;
	private JButton noopCmdBtn;
	private JButton rsetCmdBtn;
	private JButton topCmdBtn;
	private JButton uidlCmdBtn;
	private JScrollPane scrollPane;
	private JTextField hostTextField;
	private JTextField portTextField;
	private JButton btnConnect;
	private JButton btnDisconnect;
	
	public static void main(String[] args) {
		UserInterface ui = new UserInterface();
		ui.setVisible(true);
	}

	public UserInterface() {
		setTitle("POP3 Server");
		setMaximumSize(new Dimension(800, 500));
		setMinimumSize(new Dimension(800, 500));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JPanel commandPanel = new JPanel();
		commandPanel.setPreferredSize(new Dimension(50, 32767));
		commandPanel.setBounds(6, 6, 117, 472);
		getContentPane().add(commandPanel);
		commandPanel.setLayout(new GridLayout(6, 1, 0, 0));
		
		userCmdBtn = new JButton("USER");
		userCmdBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UserInterface.this.insertCommand(UserInterface.this.getUserCmdBtn());
			}
		});
		commandPanel.add(userCmdBtn);
		
		passCmdBtn = new JButton("PASS");
		passCmdBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UserInterface.this.insertCommand(UserInterface.this.getPassCmdBtn());
			}
		});
		commandPanel.add(passCmdBtn);
		
		quitCmdBtn = new JButton("QUIT");
		quitCmdBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UserInterface.this.insertCommand(UserInterface.this.getQuitCmdBtn());
			}
		});
		commandPanel.add(quitCmdBtn);
		
		statCmdBtn = new JButton("STAT");
		statCmdBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UserInterface.this.insertCommand(UserInterface.this.getStatCmdBtn());
			}
		});
		commandPanel.add(statCmdBtn);
		
		listCmdBtn = new JButton("LIST");
		listCmdBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UserInterface.this.insertCommand(UserInterface.this.getListCmdBtn());
			}
		});
		commandPanel.add(listCmdBtn);
		
		retrCmdBtn = new JButton("RETR");
		retrCmdBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UserInterface.this.insertCommand(UserInterface.this.getRetrCmdBtn());
			}
		});
		commandPanel.add(retrCmdBtn);
		
		deleCmdBtn = new JButton("DELE");
		deleCmdBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UserInterface.this.insertCommand(UserInterface.this.getDeleCmdBtn());
			}
		});
		commandPanel.add(deleCmdBtn);
		
		noopCmdBtn = new JButton("NOOP");
		noopCmdBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UserInterface.this.insertCommand(UserInterface.this.getNoopCmdBtn());
			}
		});
		commandPanel.add(noopCmdBtn);
		
		rsetCmdBtn = new JButton("RSET");
		rsetCmdBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UserInterface.this.insertCommand(UserInterface.this.getRsetCmdBtn());
			}
		});
		commandPanel.add(rsetCmdBtn);
		
		topCmdBtn = new JButton("TOP");
		topCmdBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UserInterface.this.insertCommand(UserInterface.this.getTopCmdBtn());
			}
		});
		commandPanel.add(topCmdBtn);
		
		uidlCmdBtn = new JButton("UIDL");
		uidlCmdBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UserInterface.this.insertCommand(UserInterface.this.getUidlCmdBtn());
			}
		});
		commandPanel.add(uidlCmdBtn);
		
		JPanel displayPanel = new JPanel();
		displayPanel.setBounds(135, 49, 659, 429);
		getContentPane().add(displayPanel);
		GridBagLayout gbl_displayPanel = new GridBagLayout();
		gbl_displayPanel.columnWidths = new int[] {80, 582};
		gbl_displayPanel.rowHeights = new int[] {397, 10};
		gbl_displayPanel.columnWeights = new double[]{0.0, 1.0};
		gbl_displayPanel.rowWeights = new double[]{1.0, 1.0};
		displayPanel.setLayout(gbl_displayPanel);
		
		JLabel consoleLbl = new JLabel("Console");
		GridBagConstraints gbc_consoleLbl = new GridBagConstraints();
		gbc_consoleLbl.insets = new Insets(0, 0, 5, 5);
		gbc_consoleLbl.gridx = 0;
		gbc_consoleLbl.gridy = 0;
		displayPanel.add(consoleLbl, gbc_consoleLbl);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 0;
		displayPanel.add(scrollPane, gbc_scrollPane);
		
		JTextArea outputTextArea = new JTextArea();
		scrollPane.setViewportView(outputTextArea);
		outputTextArea.setEditable(false);
		
		// Add output display as attribute to be manipulated
		this.output = outputTextArea;
		
		JLabel cmdLbl = new JLabel("Command");
		GridBagConstraints gbc_cmdLbl = new GridBagConstraints();
		gbc_cmdLbl.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmdLbl.insets = new Insets(0, 0, 0, 5);
		gbc_cmdLbl.gridx = 0;
		gbc_cmdLbl.gridy = 1;
		displayPanel.add(cmdLbl, gbc_cmdLbl);
		
		JTextPane cmdTextArea = new JTextPane();		
		
		// Add input display as attribute to be manipulated
		this.input = cmdTextArea;
		
		// Submit command on Enter key released
		cmdTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// If user pressed enter, submit command
					String input = UserInterface.this.input.getText();
					
					// Remove newline from user's input
					input = UserInterface.this.filterCRLF(input);
					
					// Show user's command
					UserInterface.this.appendToOutput(input);
					
					// Send data to server
					UserInterface.this.sendData(input);
					
					// Reset command text area
					UserInterface.this.input.setText("");
				}
			}
		});
		GridBagConstraints gbc_cmdTextArea = new GridBagConstraints();
		gbc_cmdTextArea.fill = GridBagConstraints.BOTH;
		gbc_cmdTextArea.gridx = 1;
		gbc_cmdTextArea.gridy = 1;
		displayPanel.add(cmdTextArea, gbc_cmdTextArea);
		
		
		JLabel lblHost = new JLabel("Host");
		lblHost.setBounds(135, 21, 30, 16);
		getContentPane().add(lblHost);
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(432, 21, 25, 16);
		getContentPane().add(lblPort);
		
		btnConnect = new JButton("Connect");
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if ( ! UserInterface.this.connected)
					// Connect to server if connect button pressed
					UserInterface.this.connectToServer(UserInterface.this.hostTextField.getText(), UserInterface.this.portTextField.getText());
			}
		});
		btnConnect.setBounds(568, 16, 117, 29);
		getContentPane().add(btnConnect);
		
		hostTextField = new JTextField();
		hostTextField.setText("localhost");
		hostTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if ( ! UserInterface.this.connected)
						// Connect to server if Enter key pressed
						UserInterface.this.connectToServer(UserInterface.this.hostTextField.getText(), UserInterface.this.portTextField.getText());
				}
			}
		});
		hostTextField.setBounds(177, 15, 243, 28);
		getContentPane().add(hostTextField);
		hostTextField.setColumns(10);
		
		portTextField = new JTextField();
		portTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if ( ! UserInterface.this.connected)
						// Connect to server if Enter key pressed
						UserInterface.this.connectToServer(UserInterface.this.hostTextField.getText(), UserInterface.this.portTextField.getText());
				}
			}
		});
		portTextField.setText("8080");
		portTextField.setBounds(469, 15, 96, 28);
		getContentPane().add(portTextField);
		portTextField.setColumns(10);
		
		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Disconnect from server if disconnect button pressed
				UserInterface.this.closeConnection();
				UserInterface.this.setConnectivityEnabled(true);
			}
		});
		btnDisconnect.setBounds(677, 16, 117, 29);
		getContentPane().add(btnDisconnect);
	}
	
	// Server manipulation
	private void connectToServer(String host, String port) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				UserInterface.this.closeConnection();
			}
		});
		
		try {
			this.appendToOutput("Connecting...");
			int portNumber = Integer.parseInt(port);
			this.client = new Socket();
			this.client.connect(new InetSocketAddress(InetAddress.getByName(host), portNumber), 10000);
			this.establishConnection();
			
			// Start worker thread for client
			this.es = Executors.newFixedThreadPool(1);
			this.es.execute(this);
			
			this.appendToOutput("Connected.");
		} catch (NumberFormatException e) {
			this.appendToOutput("Invalid port number");
		} catch (IOException e) {
			this.appendToOutput("Error occurred trying to connect to host: " + host + " on port: " + port);
		}
	}
	
	@Override
	public void run() {
		// Listen to server
		String res;
		while (true) {
			try {
				if ((res = this.br.readLine().trim()) != null) {
					this.appendToOutput(res);
				}
			} catch (IOException e) {
				// Connection has already been closed
				// Enable connectivity
				this.appendToOutput("Connection has been closed.");
				break;
			} catch (NullPointerException e) {
				// Connection has been closed prematurely
				// Can be because of QUIT command
				this.appendToOutput("Connection has been closed.");
				break;
			}
		}
		
		// Connection disconnected, enable user to reconnect
		this.setConnectivityEnabled(true);
	}
	
	private void establishConnection() {
		try {
			System.out.println("Trying to establish connection...");
			this.br = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
			this.pw = new PrintWriter(this.client.getOutputStream());
			System.out.println("Connection established");
			
			// Disable connect function
			this.setConnectivityEnabled(false);
			
		} catch (IOException e) {
			this.appendToOutput("Failed to establish connection with host");
		}
	}
	
	private void sendData(String data) {
		try {
			this.pw.println(data);
			this.pw.flush();
		} catch (NullPointerException e) {
			// Connection has already been closed
			// Do nothing
		}
	}
	
	private void closeConnection() {
		// No connection to disconnect
		if ( ! this.connected) return;
		
		try {
			this.appendToOutput("Disconnecting...");
			this.client.close();
			this.br.close();
			this.pw.close();
		} catch (IOException e) {
			System.out.println("Failed to close connection sockets");
		} catch (NullPointerException e) {
			// Connection has not been established and functions of connection are used
			// Do nothing, ignore
		} finally {
			// Must shut down executor service now, no point in listening to the server anymore
			this.es.shutdownNow();
			this.appendToOutput("Disconnected.");
		}
	}
	
	// UI manipulation
	private synchronized void appendToOutput(String s) {
		String previousText;
		if (this.output.getText() == null)
			previousText = "";
		else
			previousText = this.output.getText();
		
		this.output.setText(previousText + s + "\n");
	}
	
	private String filterCRLF(String input) {
		return input.replaceAll("\\r|\\n", "");
	}
	
	private void insertCommand(JButton btn) {
		String cmd = btn.getText();
		this.input.setText(cmd + " ");
		
		// For UX purpose, focus on textpane after button is clicked
		this.input.requestFocusInWindow();
	}
	
	private void setConnectivityEnabled(boolean b) {
		this.connected = ! b;
		this.getConnectBtn().setEnabled(b);
		this.getHostTextField().setEnabled(b);
		this.getPortTextField().setEnabled(b);
	}
	
	protected JButton getUserCmdBtn() {
		return userCmdBtn;
	}
	protected JButton getPassCmdBtn() {
		return passCmdBtn;
	}
	protected JButton getQuitCmdBtn() {
		return quitCmdBtn;
	}
	protected JButton getStatCmdBtn() {
		return statCmdBtn;
	}
	protected JButton getListCmdBtn() {
		return listCmdBtn;
	}
	protected JButton getRetrCmdBtn() {
		return retrCmdBtn;
	}
	protected JButton getDeleCmdBtn() {
		return deleCmdBtn;
	}
	protected JButton getNoopCmdBtn() {
		return noopCmdBtn;
	}
	protected JButton getRsetCmdBtn() {
		return rsetCmdBtn;
	}
	protected JButton getTopCmdBtn() {
		return topCmdBtn;
	}
	protected JButton getUidlCmdBtn() {
		return uidlCmdBtn;
	}
	protected JButton getConnectBtn() {
		return btnConnect;
	}
	protected JButton getDisconnectBtn() {
		return btnDisconnect;
	}
	protected JTextField getHostTextField() {
		return hostTextField;
	}
	protected JTextField getPortTextField() {
		return portTextField;
	}
}

package avdeev.cubiscan;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Cubiscan {

	private ServerSocket socket;
	private String cubIp;
	private int cubPort;
	private int cubTimeout;
	
		
	public Cubiscan(int serverPort, String cubIp, int cubPort, int cubTimeout) {
				
		this.cubIp      = cubIp;
		this.cubPort    = cubPort;
		this.cubTimeout = cubTimeout;
				
		try {
			socket = new ServerSocket(serverPort);
			System.out.println("Server start on port " + serverPort);
		} catch (IOException e) {			
			e.printStackTrace();			
			System.exit(-1);			
		}	    
	}	
	
	
	public void start() {
						
		Socket client;
		
		while(true) {
			
			try {				
				client = socket.accept();
				System.out.println("Request accepted...");
				ResponseThread req = new ResponseThread(client, cubIp, cubPort, cubTimeout);
				req.start();												
				
			} catch (IOException e) {				
				e.printStackTrace();				
				System.exit(-1);				
			}
		}
	}
}

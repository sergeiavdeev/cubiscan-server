package avdeev.cubiscan;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Cubiscan {

	private ServerSocket socket;
	private String cubIp;
	private int cubPort;
	
		
	public Cubiscan(int serverPort, String cubIp, int cubPort) {
				
		this.cubIp = cubIp;
		this.cubPort = cubPort;		
				
		try {
			socket = new ServerSocket(serverPort);
		} catch (IOException e) {			
			e.printStackTrace();			
			System.exit(-1);			
		}	    
	}	
	
	
	public void start() {
		
		System.out.print("Server start!");
		
		Socket client;
		
		while(true) {
			
			try {				
				client = socket.accept();
				
				ResponseThread req = new ResponseThread(client, cubIp, cubPort);
				req.start();												
				
			} catch (IOException e) {				
				e.printStackTrace();				
				System.exit(-1);				
			}
		}
	}
}

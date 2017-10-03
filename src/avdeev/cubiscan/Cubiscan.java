package avdeev.cubiscan;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Cubiscan {

	private ServerSocket socket;
	private String ip;
		
	public Cubiscan(String ip, int port) {
		// TODO Auto-generated constructor stub
		
		this.ip = ip;
				
		try {
			socket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Cold not create socket!");
			System.exit(-1);
			return;
		}	    
	}
	
	public void start() {
		
		System.out.print("Server start!");
		
		Socket client;
		
		while(true) {
			
			try {				
				client = socket.accept();
				
				System.out.println("Client connected!");
				System.out.println(client.getInetAddress());
				
				RequestThread req = new RequestThread(client);
				req.start();												
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("No accept!");
				System.exit(-1);
				return;
			}
		}
	}
}

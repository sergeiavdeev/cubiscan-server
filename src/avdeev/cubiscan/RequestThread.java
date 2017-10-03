package avdeev.cubiscan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class RequestThread extends Thread {

	private BufferedReader input;
	private PrintWriter output;
	private Socket client;
	
	public RequestThread(Socket s) throws IOException {
		
		client = s;
		
		input = new BufferedReader(new InputStreamReader(client.getInputStream()));
		output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);				
	}
	
	@Override
	public void run() {
	
		super.run();
		
		output.println("Hi!");
		
		try {						
			input.close();
			output.close();
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}			
	}
			
}

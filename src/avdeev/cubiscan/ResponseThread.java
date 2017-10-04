package avdeev.cubiscan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ResponseThread extends Thread {

	private BufferedReader input;
	private PrintWriter output;
	
	private BufferedReader cubInput;
	private PrintWriter cubOutput;
	
	private Socket client;
	private Cargo cargo;
	private Socket cubSocket;
	private char [] command;
	private List<String> errors;
	
	public ResponseThread(Socket s, String cubIp, int cubPort) {
		
		client = s;
		cargo = new Cargo();
		
		errors = new ArrayList<String>();
		
		command = new char [] {0x02, 0x4D, 0x03, 0x0D, 0x0A};
		
		try {			
			input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8)), true);
			cubSocket = new Socket(cubIp, cubPort);
			cubOutput = new PrintWriter(cubSocket.getOutputStream(), true);
			cubInput = new BufferedReader(new InputStreamReader(cubSocket.getInputStream()));
		} catch (IOException e) {
			
			errors.add(e.getMessage());
			e.printStackTrace();
		}				
	}
	
	
	@Override
	public void run() {
	
		super.run();
		
		if (errors.size() == 0) {
					
			cargo.setProps(0, 0, 0, 0, 0, 0);
			cubOutput.write(command);
			cubOutput.flush();
			
			String userInput;
			
			try {
				userInput = cubInput.readLine();				
				
				cubOutput.close();
				cubInput.close();
				cubSocket.close();
				
				byte [] result = userInput.getBytes();
				if (result[2] == 0x41) { //Измерили
					cargo.setProps(
							Float.parseFloat(userInput.substring(12, 17)), 
							Float.parseFloat(userInput.substring(19, 24)), 
							Float.parseFloat(userInput.substring(26, 31)), 
							Float.parseFloat(userInput.substring(35, 41)), 
							Float.parseFloat(userInput.substring(43, 49)), 
							Float.parseFloat(userInput.substring(43, 49))
							);				
				}
				else {				
					errors.add("Ошибка измерения");
				}
				
			} catch (IOException e) {				
				errors.add(e.getMessage());	
				e.printStackTrace();						
			}
		}
				
		String response = cargo.getJSON(errors);
		
		output.println("HTTP/1.1 200 OK");
		output.println("Server: Cubiscan Server 1.0");
		output.println("Content-Type: application/json; charset=utf-8");
		output.println("Date: " + new Date().toString());
		output.println("Content-Length: " + response.getBytes(StandardCharsets.UTF_8).length);	
		output.println("X-Powered-By: Cubiscan Server 1.0");
		output.println("");
		output.println(response);
								
		try {						
			input.close();
			output.close();
			client.close();
		} catch (IOException e) {			
			e.printStackTrace();			
		}			
	}			
}

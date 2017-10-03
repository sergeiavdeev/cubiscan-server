package avdeev.cubiscan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;

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
		
		JSONObject j = new JSONObject();
		j.put("id", 1589);
		j.put("name", "Москва!");
		j.put("descr", "Столица России!");
		
		String response = j.toString();
		
		output.println("HTTP/1.1 200 OK");
		output.println("Server: nginx/1.2.1");
		output.println("Content-Type: application/json; charset=utf-8");
		output.println("Content-Length: " + response.getBytes().length);
		output.println("Connection: keep-alive");
		output.println("Accept-Ranges: bytes");
		output.println("");
		output.println(response.toString());
								
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

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

import org.json.JSONObject;


public class ResponseThread extends Thread {

	private float length;
	private float width;
	private float height;
	private float weight;
	private float dimWeight;
	private float volWeight;
	
	private BufferedReader input;
	private PrintWriter output;
	
	private BufferedReader cubInput;
	private PrintWriter cubOutput;
	
	private Socket client;	
	private Socket cubSocket;
	private char [] command;
	private List<String> errors;
	
	public ResponseThread(Socket s, String cubIp, int cubPort) {
		
		client = s;
		
		length = 0;
		width  = 0;
		height = 0;
		weight = 0;
		
		dimWeight = 0;
		volWeight = 0;
		
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
			errors.add("Ошибка соединения с cubiscan!");
			e.printStackTrace();
		} 
	}
	
	
	@Override
	public void run() {
	
		super.run();
		
		long time = 0;
		
		if (errors.size() == 0) {
			Date startDate = new Date();
			cubOutput.write(command);
			cubOutput.flush();
			
			String userInput;
			
			try {				
				userInput = cubInput.readLine();				
				
				Date endDate = new Date();
				
				time = endDate.getTime() - startDate.getTime();
				
				cubOutput.close();
				cubInput.close();
				cubSocket.close();
				
				byte [] result = userInput.getBytes();
				if (result[2] == 0x41) { //Измерили
					
					length = getFloat(userInput.substring(12, 17)); 
					width =  getFloat(userInput.substring(19, 24)); 
					height = getFloat(userInput.substring(26, 31)); 
					weight = getFloat(userInput.substring(35, 41)); 
					
					dimWeight = getFloat(userInput.substring(43, 49)); 
					volWeight = getFloat(userInput.substring(43, 49));
									
				}
				else {				
					errors.add("Ошибка измерения");
				}
				
			} catch (IOException e) {				
				errors.add(e.getMessage());	
				e.printStackTrace();						
			}			
		}
		
		JSONObject result = new JSONObject();		
		
		boolean isError = false;
		
		if(errors.size() > 0) {
			isError = true;
		}
		
		JSONObject data = new JSONObject();
		data.put("length", length);
		data.put("width", width);
		data.put("height", height);
		data.put("weight", weight);
		data.put("dimWeight", dimWeight);
		data.put("volWeight", volWeight);
		data.put("requestTime", time);
		
		result.put("isError", isError);
		result.put("errors", errors);
		result.put("data", data);
		
		String response = result.toString();
		
		output.println("HTTP/1.1 200 OK");
		output.println("Server: Cubiscan Server 1.0");
		output.println("Content-Type: application/json; charset=utf-8");
		output.println("Date: " + new Date().toString());
		output.println("Content-Length: " + response.getBytes(StandardCharsets.UTF_8).length);	
		output.println("X-Powered-By: Cubiscan Server 1.0");
		output.println("");
		output.println(response);
								
		try {						
			if(input != null)input.close();
			if(output != null)output.close();
			if(client != null)client.close();
		} catch (IOException e) {			
			e.printStackTrace();			
		}			
	}		
	
	private float getFloat(String value) {
		
		float result = 0f;
		
		try {
			result = Float.parseFloat(value);
		} catch (NumberFormatException e) {
			
			e.printStackTrace();
		}
		
		return result;
	}
}

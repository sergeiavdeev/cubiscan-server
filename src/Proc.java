import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import avdeev.cubiscan.Cubiscan;

public class Proc {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Properties props = new Properties();
		FileInputStream propsFile;
						
		try {
			propsFile = new FileInputStream("config.ini");
			props.load(propsFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		int port = Integer.parseInt(props.getProperty("port"));
		String ip = props.getProperty("ip");
		
		Cubiscan cubiscan = new Cubiscan(ip, port);
		cubiscan.start();
	}
}

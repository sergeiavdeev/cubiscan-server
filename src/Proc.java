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
		
		int port = Integer.parseInt(props.getProperty("cubiscan.port"));
		String ip = props.getProperty("cubiscan.ip");
		int serverPort = Integer.parseInt(props.getProperty("server.port"));
				
		Cubiscan cubiscan = new Cubiscan(serverPort, ip, port);		
		cubiscan.start();
	}
}

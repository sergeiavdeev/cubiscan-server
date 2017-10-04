package avdeev.cubiscan;


import java.util.List;
import org.json.JSONObject;

public class Cargo {
	
	private float length;
	private float width;
	private float height;
	private float weight;
	private float dimWeight;
	private float volWeight;
				
	public Cargo() {
		
		length = 0;
		width  = 0;
		height = 0;
		
		dimWeight = 0;
		volWeight = 0;
	}
	
	public String getJSON(List<String> errors) {
		
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
		
		result.put("isError", isError);
		result.put("errors", errors);
		result.put("data", data);
		
		return result.toString();
	}
	
	public void setProps(float l, float w, float h, float wg, float dwg, float vwg) {
		
		length = l;
		width = w;
		height = h;
		weight = wg;
		dimWeight = dwg;
		volWeight = vwg;
	}
}

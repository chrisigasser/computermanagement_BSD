package at.Objects;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Housing {
	
	private int id;
	private String name;
	public Housing(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public JSONObject toJson() {
		JSONObject me = new JSONObject();
		me.put("type", "Housing");
		me.put("id", id);
		me.put("name", name);
		
		return me;
	}
	
	public JSONObject toJSON(JSONArray myRooms) {
		JSONObject me = toJson();
		me.put("rooms", myRooms);
		
		return me;
	}
	
	

}

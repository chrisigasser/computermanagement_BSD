package at.Objects;

import org.json.simple.JSONObject;

public class Room {
	private int id;
	private String name;
	private Housing myHousing;
	public Room(int id, String name, Housing myHousing) {
		super();
		this.id = id;
		this.name = name;
		this.myHousing = myHousing;
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
	public Housing getMyHousing() {
		return myHousing;
	}
	public void setMyHousing(Housing myHousing) {
		this.myHousing = myHousing;
	}
	
	public JSONObject toJson() {
		JSONObject me = new JSONObject();
		me.put("type", "room");
		me.put("id", id);
		me.put("name", name);
		
		return me;
	}
	
	public JSONObject toJson(int housingID) {
		JSONObject me = toJson();
		me.put("housing", housingID);
		return me;
	}

}

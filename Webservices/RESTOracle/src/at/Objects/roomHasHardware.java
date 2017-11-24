package at.Objects;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class roomHasHardware {
	private Room myRoom = null;
	private Hardware myType = null;
	private String name = null;
	private String desc = null;
	private int id;
	
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Room getMyRoom() {
		return myRoom;
	}
	public void setMyRoom(Room myRoom) {
		this.myRoom = myRoom;
	}
	public Hardware getMyType() {
		return myType;
	}
	public void setMyType(Hardware myType) {
		this.myType = myType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public roomHasHardware(Room myRoom, Hardware myType, String name, int id, String desc) {
		super();
		this.myRoom = myRoom;
		this.myType = myType;
		this.name = name;
		this.id = id;
		this.desc = desc;
	}
	
	/**
	 * Returns JSONObject containing name, id, roomID, hardwareID, hardwareName, HardwareLogo, HardwareDesc, desc
	 * @return JSONObject
	 */
	public JSONObject toJSONWithAllInfos() {
		JSONObject me = toJSON();
		me.put("hname", myType.getName());
		me.put("hlogo", myType.getLogo());
		me.put("hdesc", myType.getDesc());
		
		return me;
	}
	
	
	/**
	 * Returns JSONObject containing name, id, roomID, hardwareID, desc
	 * @return JSONObject
	 */
	public JSONObject toJSON() {
		JSONObject me = toJSONwithoutHardwareOrRoom();
		me.put("hardware", myType.getId());
		me.put("room", myRoom.getId());
		return me;
	}
	
	/**
	 * Returns JSONObject containing name, id, hardwareID, desc
	 * @return JSONObject
	 */
	public JSONObject toJSONwithoutRoom() {
		JSONObject me = toJSONwithoutHardwareOrRoom();
		me.put("hardware", myType.getId());
		return me;

	}
	
	/**
	 * Returns JSONObject containing name, id, roomID, desc
	 * @return JSONObject
	 */
	public JSONObject toJSONwithoutHardware() {
		JSONObject me = toJSONwithoutHardwareOrRoom();
		me.put("room", myRoom.getId());
		return me;
	}
	
	/**
	 * Returns JSONObject containing name, id, desc
	 * @return JSONObject
	 */
	public JSONObject toJSONwithoutHardwareOrRoom() {
		JSONObject me = new JSONObject();
		me.put("type", "roomHasHardware");
		me.put("id", id);
		me.put("name", name);
		me.put("desc", desc);
		
		return me;
	}
	
}

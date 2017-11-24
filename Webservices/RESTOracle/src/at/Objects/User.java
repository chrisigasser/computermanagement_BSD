package at.Objects;

import org.json.simple.JSONObject;

public class User {
	private String uname;
	private String udesc;
	private int id;
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUdesc() {
		return udesc;
	}
	public void setUdesc(String udesc) {
		this.udesc = udesc;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User(String uname, String udesc, int id) {
		super();
		this.uname = uname;
		this.udesc = udesc;
		this.id = id;
	}
	
	public JSONObject toJson() {
		JSONObject me = new JSONObject();
		me.put("type", "User");
		me.put("id", id);
		me.put("uname", uname);
		me.put("desc", udesc);
		
		return me;
	}
	
}

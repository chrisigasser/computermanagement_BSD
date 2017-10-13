package at.Objects;

import org.json.simple.JSONObject;

public class Hardware {
	private int id;
	private String name;
	private String logo;
	
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
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public Hardware(int id, String name, String logo) {
		super();
		this.id = id;
		this.name = name;
		this.logo = logo;
	}
	
	public JSONObject toJson() {
		JSONObject me = new JSONObject();
		me.put("id", id);
		me.put("name", name);
		me.put("logo", logo);
		
		return me;
	}

}

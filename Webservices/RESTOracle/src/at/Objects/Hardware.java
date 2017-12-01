package at.Objects;

import org.json.simple.JSONObject;

public class Hardware {
	private int id;
	private String name;
	private String logo;
	private String Desc;
	
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

	
	public String getDesc() {
		return Desc;
	}
	public void setDesc(String desc) {
		Desc = desc;
	}
	
	
	public Hardware(int id, String name, String logo, String desc) {
		super();
		this.id = id;
		this.name = name;
		this.logo = logo;
		Desc = desc;
	}
	
	public JSONObject toJson() {
		JSONObject me = new JSONObject();
		me.put("type", "Hardware");
		me.put("id", id);
		me.put("name", name);
		me.put("logo", logo);
		me.put("desc", Desc);
		
		return me;
	}

}

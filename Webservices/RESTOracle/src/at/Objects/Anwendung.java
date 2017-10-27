package at.Objects;

import org.json.simple.JSONObject;

public class Anwendung {
	private int id;
	private String name;
	private String adesc;
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
	public String getAdesc() {
		return adesc;
	}
	public void setAdesc(String adesc) {
		this.adesc = adesc;
	}
	public Anwendung(int id, String name, String adesc) {
		super();
		this.id = id;
		this.name = name;
		this.adesc = adesc;
	}
	
	public JSONObject toJson() {
		JSONObject me = new JSONObject();
		me.put("type", "Application");
		me.put("name", name);
		me.put("desc", adesc);
		
		return me;
	}
}

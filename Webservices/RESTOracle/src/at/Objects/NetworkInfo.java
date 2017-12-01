package at.Objects;

import org.json.simple.JSONObject;

public class NetworkInfo {
	private roomHasHardware part = null;
	private boolean isDHCP = false;
	private String addInfo = null;
	public roomHasHardware getPart() {
		return part;
	}
	public void setPart(roomHasHardware part) {
		this.part = part;
	}
	public boolean isDHCP() {
		return isDHCP;
	}
	public void setDHCP(boolean isDHCP) {
		this.isDHCP = isDHCP;
	}
	public String getAddInfo() {
		return addInfo;
	}
	public void setAddInfo(String addInfo) {
		this.addInfo = addInfo;
	}
	public NetworkInfo(roomHasHardware part, boolean isDHCP, String addInfo) {
		super();
		this.part = part;
		this.isDHCP = isDHCP;
		this.addInfo = addInfo;
	}
	
	/**
	 * returns JSONObject containing isDHCP and furtherInfo
	 * @return JSONObject
	 */
	public JSONObject toJsonWithoutPart() {
		JSONObject me = new JSONObject();
		me.put("type", "NetworkInfo");
		me.put("isDHCP", isDHCP);
		me.put("furtherInfo", addInfo);
		
		return me;
	}
	
	/**
	 * returns JSONObject containing isDHCP and furtherInfo and Part
	 * @return JSONObject
	 */
	public JSONObject toJson() {
		JSONObject me = toJsonWithoutPart();
		me.put("partID", part.getId());
		
		return me;
	}
}

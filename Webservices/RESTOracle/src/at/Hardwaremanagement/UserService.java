package at.Hardwaremanagement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.RowSet;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET; 
import javax.ws.rs.Path; 
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import at.Database.ConnectionFactory;
import at.Objects.Hardware;

@Path("/UserService")

public class UserService {
	//http://localhost:8080/RESTOracle/rest/UserService/<PATH>
	//ResultSet beginnt mit z�hlen bei 1!!!!
	
	@GET 
	@Path("/hardware")
	@Produces(MediaType.APPLICATION_JSON) 
	public String getAllHardware() {
		try {
			ArrayList<Hardware> all = _getAllHardware();
			
			JSONArray allJSON = new JSONArray();
			for(Hardware h : all) {
				allJSON.add(h.toJson());
			}
			return allJSON.toJSONString();
		} catch (SQLException e) {
			return "SQLException";
		}
	}
	
	@GET
	@Path("/hardware/{hid}")
	@Produces(MediaType.APPLICATION_JSON) 
	public String getSingleHardware(@PathParam("hid") int hid) {
		try {
			ArrayList<Hardware> all = _getAllHardware();
			
			for(Hardware h : all) {
				if(h.getId() == hid)
				return h.toJson().toJSONString();
			}
		} catch (SQLException e) {
			return "SQLException";
		}
		return "{}";
	}
	
	private ArrayList<Hardware> _getAllHardware() throws SQLException {
		ArrayList<Hardware> all = new ArrayList<Hardware>();
		Connection conn = ConnectionFactory.get();
		
		Statement stmt = ConnectionFactory.get().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet res = stmt.executeQuery("select id, name, logo from hardware");
		while (res.next()) {
			all.add(new Hardware(res.getInt(1), res.getString(2), res.getString(3)));
		}
		res.close();
		stmt.close();
		conn.close();
		
		return all;
	}

}

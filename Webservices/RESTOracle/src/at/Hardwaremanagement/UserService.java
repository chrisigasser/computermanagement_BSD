package at.Hardwaremanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.RowSet;
import javax.ws.rs.PathParam;
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
import at.Objects.Housing;
import at.Objects.Room;
import at.Objects.roomHasHardware;

@Path("/UserService")

public class UserService {
	//http://192.168.194.150:8080/RESTOracle/rest/UserService/<PATH>
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
	
	@GET
	@Path("/housing")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllHousings() {
		try {
			ArrayList<Housing> all = _getAllHousing();
			
			JSONArray allJSON = new JSONArray();
			for(Housing h : all) {
				allJSON.add(h.toJson());
			}
			return allJSON.toJSONString();
		} catch (SQLException e) {
			return "SQLException";
		}
	}
	
	@GET
	@Path("/housing/{houseid}")
	@Produces(MediaType.APPLICATION_JSON) 
	public String getSingleHousing(@PathParam("houseid") int hid) {
		try {
			Connection conn = ConnectionFactory.get();
			PreparedStatement prepStmt = conn.prepareStatement("select h.id as houseID, h.name as houseName, r.id as roomID, r.Name as roomName from housing h JOIN room r ON h.id = r.HOUSING where h.id = ?");
			prepStmt.setInt(1, hid);
			ResultSet rs = prepStmt.executeQuery();
			
			
			JSONArray allRooms = new JSONArray();
			Housing h = null;
			while (rs.next()) {
				if(h == null) {
					h = new Housing(rs.getInt(1), rs.getString(2));
				}
				allRooms.add(new Room(rs.getInt(3), rs.getString(4), h).toJson());
			}
			
			rs.close();
			prepStmt.close();
			conn.close();
			
			if(h != null) {
				return h.toJSON(allRooms).toJSONString();
			}
		} catch (SQLException e) {
			return "SQLException";
		}
		return "{}";
	}
	
	@GET
	@Path("/room")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllRooms() {
		try {
			JSONArray allJSON = new JSONArray();
			
			Connection conn = ConnectionFactory.get();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet res = stmt.executeQuery("select id, name, housing from room");
			while (res.next()) {
				Room r = new Room(res.getInt(1), res.getString(2), null);
				allJSON.add(r.toJson(res.getInt(3)));
			}
			res.close();
			stmt.close();
			conn.close();
			
			
			
			return allJSON.toJSONString();
		} catch (SQLException e) {
			return "SQLException";
		}
	}
	
	@GET
	@Path("/room/{roomID}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllRooms(@PathParam("roomID") int roomID) {
		try {
			JSONObject room = null;
			
			Connection conn = ConnectionFactory.get();
			PreparedStatement prepStmt = conn.prepareStatement("select rh.id as hasID, rh.name as name, h.id as hardwareID, h.name as hardwareName, h.logo as logo, r.id as roomID, r.housing as housingID, r.Name as roomName from roomHasHardware rh JOIN room r ON rh.roomid = r.id JOIN hardware h ON rh.HARDWAREID = h.id where r.id = ? order by h.id");
			prepStmt.setInt(1, roomID);
			ResultSet rs = prepStmt.executeQuery();
			
			Room thisRoom = null;
			int housingID = -1;
			Hardware curr = null;
			JSONArray hardware = new JSONArray();
			JSONArray hasInRoom = new JSONArray();
			while (rs.next()) {
				if(thisRoom == null) {
					thisRoom = new Room(rs.getInt("ROOMID"), rs.getString("ROOMNAME"), null);
					housingID = rs.getInt("HOUSINGID");
					curr = new Hardware(rs.getInt("HARDWAREID"), rs.getString("HARDWARENAME"), rs.getString("LOGO"));
				}
				
				if(curr.getId() != rs.getInt("HARDWAREID")) {
					JSONObject currHW = curr.toJson();
					currHW.put("hasInRoom", hasInRoom);
					hardware.add(currHW);
					curr = new Hardware(rs.getInt("HARDWAREID"), rs.getString("HARDWARENAME"), rs.getString("LOGO"));
					hasInRoom = new JSONArray();
				}
				roomHasHardware rhh = new roomHasHardware(thisRoom, curr, rs.getString("NAME"), rs.getInt("HASID"));
				hasInRoom.add(rhh.toJSONwithoutHardwareOrRoom());
			}
			JSONObject currHW = curr.toJson();
			currHW.put("hasInRoom", hasInRoom);
			hardware.add(currHW);
			room = thisRoom.toJson();
			room.put("myHardware", hardware);
			rs.close();
			prepStmt.close();
			conn.close();
			
			
			
			return room.toJSONString();
		} catch (SQLException e) {
			return "SQLException";
		}
		
		
	}
	
	private ArrayList<Housing> _getAllHousing() throws SQLException {
		ArrayList<Housing> all = new ArrayList<Housing>();
		Connection conn = ConnectionFactory.get();
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet res = stmt.executeQuery("select id, name from housing");
		while (res.next()) {
			all.add(new Housing(res.getInt(1), res.getString(2)));
		}
		res.close();
		stmt.close();
		conn.close();
		
		return all;
	}
	
	private ArrayList<Hardware> _getAllHardware() throws SQLException {
		ArrayList<Hardware> all = new ArrayList<Hardware>();
		Connection conn = ConnectionFactory.get();
		
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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

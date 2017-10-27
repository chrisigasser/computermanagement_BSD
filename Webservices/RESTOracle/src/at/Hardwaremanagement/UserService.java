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
import at.Objects.Anwendung;
import at.Objects.Hardware;
import at.Objects.Housing;
import at.Objects.NetworkInfo;
import at.Objects.Room;
import at.Objects.User;
import at.Objects.roomHasHardware;

@Path("/UserService")

public class UserService {
	//http://192.168.194.150:8080/RESTOracle/rest/UserService/<PATH>
	//ResultSet beginnt mit zählen bei 1!!!!
	
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
	public String getHousing() {
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
	public String getHousing(@PathParam("houseid") int hid) {
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
	public String getRooms() {
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
	public String getRoom(@PathParam("roomID") int roomID) {
		try {
			JSONObject room = null;
			
			Connection conn = ConnectionFactory.get();
			PreparedStatement prepStmt = conn.prepareStatement("select rh.id as hasID, rh.name as name, h.id as hardwareID, h.hdesc as hdesc, h.name as hardwareName, h.logo as logo, r.id as roomID, r.housing as housingID, r.Name as roomName from roomHasHardware rh JOIN room r ON rh.roomid = r.id JOIN hardware h ON rh.HARDWAREID = h.id where r.id = ? order by h.id");
			prepStmt.setInt(1, roomID);
			ResultSet rs = prepStmt.executeQuery();
			
			Room thisRoom = null;
			Hardware curr = null;
			JSONArray hardware = new JSONArray();
			JSONArray hasInRoom = new JSONArray();
			while (rs.next()) {
				if(thisRoom == null) {
					thisRoom = new Room(rs.getInt("ROOMID"), rs.getString("ROOMNAME"), null);
					curr = new Hardware(rs.getInt("HARDWAREID"), rs.getString("HARDWARENAME"), rs.getString("LOGO"), rs.getString("hdesc"));
				}
				
				if(curr.getId() != rs.getInt("HARDWAREID")) {
					JSONObject currHW = curr.toJson();
					currHW.put("hasInRoom", hasInRoom);
					hardware.add(currHW);
					curr = new Hardware(rs.getInt("HARDWAREID"), rs.getString("HARDWARENAME"), rs.getString("LOGO"), rs.getString("hdesc"));
					hasInRoom = new JSONArray();
				}
				roomHasHardware rhh = new roomHasHardware(thisRoom, curr, rs.getString("NAME"), rs.getInt("HASID"));
				hasInRoom.add(rhh.toJSONwithoutHardwareOrRoom());
			}
			JSONObject currHW;
			if(curr != null) {
				currHW = curr.toJson();
				currHW.put("hasInRoom", hasInRoom);
				hardware.add(currHW);
				room = thisRoom.toJson();
				room.put("myHardware", hardware);
			}
			rs.close();
			prepStmt.close();
			conn.close();
			
			
			
			return (room==null)?"{}":room.toJSONString();
		} catch (SQLException e) {
			return "SQLException";
		}
		
		
	}
	
	@GET
	@Path("/user")
	@Produces(MediaType.APPLICATION_JSON)
	public String getUsers() {
		try {
			ArrayList<User> all = _getAllUsers();
			
			JSONArray allJSON = new JSONArray();
			for(User s : all) {
				allJSON.add(s.toJson());
			}
			return allJSON.toJSONString();
		} catch (SQLException e) {
			return "SQLException";
		}
	}
	
	@GET
	@Path("/user/{userID}")
	@Produces(MediaType.APPLICATION_JSON) 
	public String getUser(@PathParam("userID") int uid) {
		try {
			Connection conn = ConnectionFactory.get();
			PreparedStatement prepStmt = conn.prepareStatement("select uname, udesc from avail_User where id = ?");
			prepStmt.setInt(1, uid);
			ResultSet rs = prepStmt.executeQuery();
			
			User l = null;
			while (rs.next()) {
				l = new User(rs.getString(1), rs.getString(2), uid);
			}
			
			rs.close();
			prepStmt.close();
			conn.close();
			
			if(l != null) {
				return l.toJson().toJSONString();
			}
		} catch (SQLException e) {
			return "SQLException";
		}
		return "{}";
	}
	
	@GET
	@Path("/application")
	@Produces(MediaType.APPLICATION_JSON)
	public String getApplications() {
		try {
			ArrayList<Anwendung> all = _getAllApplications();
			
			JSONArray allJSON = new JSONArray();
			for(Anwendung s : all) {
				allJSON.add(s.toJson());
			}
			return allJSON.toJSONString();
		} catch (SQLException e) {
			return "SQLException";
		}
	}
	
	@GET
	@Path("/application/{appID}")
	@Produces(MediaType.APPLICATION_JSON) 
	public String getApplication(@PathParam("appID") int appID) {
		try {
			Connection conn = ConnectionFactory.get();
			PreparedStatement prepStmt = conn.prepareStatement("select name, adesc from anwendung where id = ?");
			prepStmt.setInt(1, appID);
			ResultSet rs = prepStmt.executeQuery();
			
			Anwendung l = null;
			while (rs.next()) {
				l = new Anwendung(appID, rs.getString(1), rs.getString(2));
			}
			
			rs.close();
			prepStmt.close();
			conn.close();
			
			if(l != null) {
				return l.toJson().toJSONString();
			}
		} catch (SQLException e) {
			return "SQLException";
		}
		return "{}";
	}
	
	@GET
	@Path("/room/hardware/{rhid}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllInfoForHardwareInRoom(@PathParam("rhid") int rhid) {
		/*
select rh.ID as rhID, rh.hardwareID as hwID, rh.roomid as roomID, rh.name as hwName from roomHAShardware rh where rh.id = 12;
select isDHCP, additionalInfo from networkInfo where part = 12;
select DISTINCT isAD from allowedUser where part = 12;
--OR when isAD = 0
select uname as uid from allowedUser where part = 12;
select a.id, a.name, a.adesc from hasAnwendung ha JOIN Anwendung a ON ha.AnwendungsID = a.id where part = 12;
select working from works where part = 12;
select info from furtherInformation where part = 12;
		 * 
		 */
		
		roomHasHardware base = _getSingleRHH(rhid);

		if(base != null) {
			JSONObject baseObject = base.toJSON();
			NetworkInfo nf = _getNetworkInfoForRHH(base);
			JSONObject userManagement = _getUserManagement(base);
			JSONObject allAnwendungen = _getAllApplications(base);
			if(nf != null)
				baseObject.put("networkInfo", nf.toJsonWithoutPart());
			baseObject.put("userManagement", userManagement);
			baseObject.put("applications", allAnwendungen);
			
			try {
				Connection conn = ConnectionFactory.get();
				PreparedStatement prepStmt = conn.prepareStatement("select working from works where part = ?");
				prepStmt.setInt(1, base.getId());
				ResultSet rs = prepStmt.executeQuery();
				
				while (rs.next()) {
					baseObject.put("working", rs.getBoolean(1));
				}
				
				rs.close();
				prepStmt.close();
				
				prepStmt = conn.prepareStatement("select info from furtherInformation where part = ?");
				prepStmt.setInt(1, base.getId());
				rs = prepStmt.executeQuery();
				
				while (rs.next()) {
					baseObject.put("furtherInfo", rs.getString(1));
				}
				
				rs.close();
				prepStmt.close();
				conn.close();
			}
			catch(SQLException ex) {
				
			}
			return baseObject.toJSONString();
		}
		else {
			return "{}";
		}
		
	}
	
	
	
	private JSONObject _getAllApplications(roomHasHardware base) {
		JSONObject userManagement = new JSONObject();
		JSONArray all = new JSONArray();
		try {
			Connection conn = ConnectionFactory.get();
			PreparedStatement prepStmt = conn.prepareStatement("select a.id, a.name, a.adesc from hasAnwendung ha JOIN Anwendung a ON ha.AnwendungsID = a.id where part = ?");
			prepStmt.setInt(1, base.getId());
			ResultSet rs = prepStmt.executeQuery();
			
			while (rs.next()) {
				Anwendung a = new Anwendung(rs.getInt(1), rs.getString(2), rs.getString(2));
				all.add(a.toJson());
			}
			
			rs.close();
			prepStmt.close();
			conn.close();
			userManagement.put("allApplications", all);
		}
		catch(SQLException ex) {
			
		}
		
		
		return userManagement;
	}

	private JSONObject _getUserManagement(roomHasHardware base) {
		JSONObject userManagement = new JSONObject();
		try {
			Connection conn = ConnectionFactory.get();
			PreparedStatement prepStmt = conn.prepareStatement("select DISTINCT isAD from allowedUser where part = ?");
			prepStmt.setInt(1, base.getId());
			ResultSet rs = prepStmt.executeQuery();
			
			boolean isSet = false;
			while (rs.next()) {
				if(rs.getBoolean(1) == true)
					isSet = true;
			}
			
			rs.close();
			prepStmt.close();
			conn.close();
			if(isSet)
				userManagement.put("managedByAD", true);
			else {
				JSONArray allUsers = new JSONArray();
				
				conn = ConnectionFactory.get();
				prepStmt = conn.prepareStatement("select s.uname as uname, s.ID as suid, s.UDESC as udesc from allowedUser au JOIN avail_User s ON au.uname = s.id where part = ?");
				prepStmt.setInt(1, base.getId());
				rs = prepStmt.executeQuery();
				
				while (rs.next()) {
					User s = new User(rs.getString("uname"), rs.getString("udesc"), rs.getInt("suid"));
					allUsers.add(s.toJson());
				}
				
				rs.close();
				prepStmt.close();
				conn.close();
				
				userManagement.put("users", allUsers);
			}
			
		}
		catch(SQLException ex) {
			
		}
		
		
		return userManagement;
	}

	private NetworkInfo _getNetworkInfoForRHH(roomHasHardware base) {
		try {
			Connection conn = ConnectionFactory.get();
			PreparedStatement prepStmt = conn.prepareStatement("select isDHCP, additionalInfo from networkInfo where part = ?");
			prepStmt.setInt(1, base.getId());
			ResultSet rs = prepStmt.executeQuery();
			
			NetworkInfo l = null;
			while (rs.next()) {
				l = new NetworkInfo(base, rs.getBoolean(1), rs.getString(2));
			}
			
			rs.close();
			prepStmt.close();
			conn.close();
			
			return l;
		} catch (SQLException e) {
			return null;
		}
	}
	
	private roomHasHardware _getSingleRHH(int id) {
		try {
			Connection conn = ConnectionFactory.get();
			PreparedStatement prepStmt = conn.prepareStatement("select rh.ID as rhID, rh.hardwareID as hwID, rh.roomid as roomID, rh.name as hwName from roomHAShardware rh where rh.id = ?");
			prepStmt.setInt(1, id);
			ResultSet rs = prepStmt.executeQuery();
			
			roomHasHardware l = null;
			while (rs.next()) {
				l = new roomHasHardware(new Room(rs.getInt("roomID"), "", null), new Hardware(rs.getInt("hwID"), "", "", ""), rs.getString("hwName"), rs.getInt("rhID"));
			}
			
			rs.close();
			prepStmt.close();
			conn.close();
			
			return l;
		} catch (SQLException e) {
			return null;
		}
	}
	
	private ArrayList<Anwendung> _getAllApplications() throws SQLException {
		ArrayList<Anwendung> all = new ArrayList<Anwendung>();
		Connection conn = ConnectionFactory.get();
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet res = stmt.executeQuery("select id, name, adesc from anwendung");
		while (res.next()) {
			all.add(new Anwendung(res.getInt(1), res.getString(2), res.getString(3)));
		}
		res.close();
		stmt.close();
		conn.close();
		
		return all;
	}
	
	private ArrayList<User> _getAllUsers() throws SQLException {
		ArrayList<User> all = new ArrayList<User>();
		Connection conn = ConnectionFactory.get();
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet res = stmt.executeQuery("select id, uname, udesc from avail_User");
		while (res.next()) {
			all.add(new User(res.getString(2), res.getString(3), res.getInt(1)));
		}
		res.close();
		stmt.close();
		conn.close();
		
		return all;
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
		ResultSet res = stmt.executeQuery("select id, name, logo, hdesc from hardware");
		while (res.next()) {
			all.add(new Hardware(res.getInt(1), res.getString(2), res.getString(3), res.getString(4)));
		}
		res.close();
		stmt.close();
		conn.close();
		
		return all;
	}

}

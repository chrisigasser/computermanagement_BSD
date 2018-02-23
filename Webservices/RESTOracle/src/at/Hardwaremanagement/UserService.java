package at.Hardwaremanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.RowSet;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path; 
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sun.javafx.collections.MappingChange.Map;

import at.Database.ConnectionFactory;
import at.Objects.Anwendung;
import at.Objects.Hardware;
import at.Objects.Housing;
import at.Objects.NetworkInfo;
import at.Objects.Room;
import at.Objects.roomHasHardware;

@Path("/UserService")

public class UserService {
	//http://192.168.194.150:8080/RESTOracle/rest/UserService/<PATH>
	//ResultSet beginnt mit zählen bei 1!!!!
	
	@POST
	@Path("/room/hardware/networkInfo")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String addNetworkInfo(@FormParam("hid") int parName,
								 @FormParam("isDHCP") int isDHCP,
								 @FormParam("addInfo") String addInfo) {
		
		Connection conn = null;
		int updated = 0;
		try {
			conn = ConnectionFactory.get();
			PreparedStatement update = conn.prepareStatement("UPDATE networkInfo SET isDHCP = ?, additionalInfo = ? WHERE part = ?");
			PreparedStatement insert = conn.prepareStatement("INSERT INTO networkInfo (part, isDHCP, additionalInfo) VALUES(?,?,?)");
			update.setInt(1, isDHCP);
			update.setString(2, addInfo);
			update.setInt(3, parName);
			updated = update.executeUpdate();
			if(updated == 0) {
				insert.setInt(2, isDHCP);
				insert.setString(3, addInfo);
				insert.setInt(1, parName);
				insert.executeUpdate();
				return "inserted";
			}
			return "Updated";
		}
		catch(SQLException e) {
			return e.getMessage();
		}
	}
	
	@POST
	@Path("/room/hardware/works")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String addWorks(@FormParam("hid") int parName,
								 @FormParam("isWorking") int isWorking) {
		
		Connection conn = null;
		int updated = 0;
		try {
			conn = ConnectionFactory.get();
			PreparedStatement update = conn.prepareStatement("UPDATE works SET working = ? WHERE part = ?");
			PreparedStatement insert = conn.prepareStatement("INSERT INTO works VALUES(?,?)");
			update.setInt(1, isWorking);
			update.setInt(2, parName);
			updated = update.executeUpdate();
			if(updated == 0) {
				insert.setInt(1, parName);
				insert.setInt(2, isWorking);
				insert.executeUpdate();
				return "inserted";
			}
			return "Updated";
		}
		catch(SQLException e) {
			return e.getMessage();
		}
	}
	
	@POST
	@Path("/room/hardware/furtherInfo")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String addFurtherInfo(@FormParam("hid") int parName,
								 @FormParam("info") String info) {
		
		Connection conn = null;
		int updated = 0;
		try {
			conn = ConnectionFactory.get();
			PreparedStatement update = conn.prepareStatement("UPDATE furtherInformation SET info = ? WHERE part = ?");
			PreparedStatement insert = conn.prepareStatement("INSERT INTO furtherInformation VALUES(?,?)");
			update.setString(1, info);
			update.setInt(2, parName);
			updated = update.executeUpdate();
			if(updated == 0) {
				insert.setInt(1, parName);
				insert.setString(2, info);
				insert.executeUpdate();
				return "inserted";
			}
			return "Updated";
		}
		catch(SQLException e) {
			return e.getMessage();
		}
	}
	
	
	
	
	@POST
	@Path("/room/hardware/application")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String addApplication(@FormParam("hid") int parName,
								 @FormParam("aID") int aID) {
		
		Connection conn = null;
		int updated = 0;
		try {
			conn = ConnectionFactory.get();
			PreparedStatement insert = conn.prepareStatement("insert into hasAnwendung VALUES(?, ?)");
			insert.setInt(1, aID);
			insert.setInt(2, parName);
			return insert.executeUpdate() + "";
		}
		catch(SQLException e) {
			return e.getMessage();
		}
	}
	
	@DELETE
	@Path("/room/hardware/application")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String removeApplication(@FormParam("hid") int parName,
								 @FormParam("aID") int aID) {
		
		Connection conn = null;
		int updated = 0;
		try {
			conn = ConnectionFactory.get();
			PreparedStatement del = conn.prepareStatement("delete from hasAnwendung where anwendungsid = ? AND part = ?");
			del.setInt(1, aID);
			del.setInt(2, parName);
			return del.executeUpdate() + "";
		}
		catch(SQLException e) {
			return e.getMessage();
		}
	}
	
	
	
	
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
	
	@POST
	@Path("/hardware")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN) 
	public Response addNewHardware(@FormParam("hname") String parName,
								   @FormParam("hdesc") String parDesc,
								   @FormParam("hlogo") String parLogo) {
		
		try {
			if(parName != null && parDesc != null) {
				Connection conn = ConnectionFactory.get();
				PreparedStatement stmt = conn.prepareStatement("insert into hardware VALUES(?,?,?,?)");
				
				Statement highes = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet res = highes.executeQuery("select MAX(id) as max from hardware");
				int max = -1;
				while (res.next()) {
					max = res.getInt("max");
				}
				res.close();
				highes.close();
				max++;
				
				stmt.setInt(1, max);
				stmt.setString(2, parName);
				stmt.setString(4, parDesc);
				stmt.setString(3, parLogo);
				
				
				int inserted = stmt.executeUpdate();
				stmt.close();
				conn.close();
				return Response.ok(inserted).build();
			}
			else {
				return javax.ws.rs.core.Response.status(400).build();
			}
		}
		catch(Exception ex) {
			return javax.ws.rs.core.Response.status(500).build();
		}
	}
	
	@DELETE
	@Path("/hardware")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN) 
	public Response deleteHardware(@FormParam("hid") int parId) {
		try {
			Connection conn = ConnectionFactory.get();
			PreparedStatement stmt = conn.prepareStatement("delete from hardware where id = ?");
			
			stmt.setInt(1, parId);
			
			
			int del = stmt.executeUpdate();
			stmt.close();
			conn.close();
			return Response.ok(del).build();
			
		} catch(Exception ex) {
			return javax.ws.rs.core.Response.status(500).build();
		}
	}
	
	@PUT
	@Path("/hardware")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN) 
	public Response updateHardware(@FormParam("hid") int parId,
								   @FormParam("hlogo") String parLogo,
								   @FormParam("hdesc") String parDesc,
								   @FormParam("hname") String parName) {
		try {
			Connection conn = ConnectionFactory.get();
			PreparedStatement stmt = conn.prepareStatement("update hardware set logo = ?, name = ?, hdesc = ? where id = ?");
			
			stmt.setString(1, parLogo);
			stmt.setString(2, parName);
			stmt.setString(3, parDesc);
			stmt.setInt(4, parId);
			
			
			int up = stmt.executeUpdate();
			stmt.close();
			conn.close();
			return Response.ok(up).build();
			
		} catch(Exception ex) {
			return Response.status(500).build();
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
			
			Connection conn = ConnectionFactory.get();
			PreparedStatement prepStmt = conn.prepareStatement("select rh.rhdesc as rhdesc, rh.id as hasID, rh.name as name, h.id as hardwareID, h.hdesc as hdesc, h.name as hardwareName, h.logo as logo, r.id as roomID, r.housing as housingID, r.Name as roomName from roomHasHardware rh JOIN room r ON rh.roomid = r.id JOIN hardware h ON rh.HARDWAREID = h.id where r.id = ? order by h.id");
			prepStmt.setInt(1, roomID);
			ResultSet rs = prepStmt.executeQuery();
			
			Room thisRoom = null;
			Hardware curr = null;
			JSONArray returner = new JSONArray();
			while (rs.next()) {
				if(thisRoom == null) {
					thisRoom = new Room(rs.getInt("ROOMID"), rs.getString("ROOMNAME"), null);
					curr = new Hardware(rs.getInt("HARDWAREID"), rs.getString("HARDWARENAME"), rs.getString("LOGO"), rs.getString("hdesc"));
				}
				
				if(curr.getId() != rs.getInt("HARDWAREID")) {
					curr = new Hardware(rs.getInt("HARDWAREID"), rs.getString("HARDWARENAME"), rs.getString("LOGO"), rs.getString("hdesc"));
				}
				roomHasHardware rhh = new roomHasHardware(thisRoom, curr, rs.getString("NAME"), rs.getInt("HASID"),  rs.getString("rhdesc"));
				returner.add(rhh.toJSONWithAllInfos());
			}
			rs.close();
			prepStmt.close();
			conn.close();
			
			
			
			return (thisRoom==null)?"{}":returner.toJSONString();
		} catch (SQLException e) {
			return "SQLException";
		}
		
		
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
	
	@POST
	@Path("/application")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN) 
	public Response addNewApp(@FormParam("aname") String parName,
							  @FormParam("adesc") String parDesc) {
		
		try {
			if(parName != null && parDesc != null) {
				Connection conn = ConnectionFactory.get();
				PreparedStatement stmt = conn.prepareStatement("insert into anwendung VALUES(?,?,?)");
				
				Statement highes = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet res = highes.executeQuery("select MAX(id) as max from anwendung");
				int max = -1;
				while (res.next()) {
					max = res.getInt("max");
				}
				res.close();
				highes.close();
				max++;
				
				stmt.setInt(1, max);
				stmt.setString(2, parName);
				stmt.setString(3, parDesc);
				
				
				int inserted = stmt.executeUpdate();
				stmt.close();
				conn.close();
				return Response.ok(inserted).build();
			}
			else {
				return javax.ws.rs.core.Response.status(400).build();
			}
		}
		catch(Exception ex) {
			return javax.ws.rs.core.Response.status(500).build();
		}
	}
	
	@DELETE
	@Path("/application")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN) 
	public Response deleteApp(@FormParam("aid") int aid) {
		try {
			Connection conn = ConnectionFactory.get();
			PreparedStatement stmt = conn.prepareStatement("delete from anwendung where id = ?");
			
			stmt.setInt(1, aid);
			
			
			int del = stmt.executeUpdate();
			stmt.close();
			conn.close();
			return Response.ok(del).build();
			
		} catch(Exception ex) {
			return Response.status(500).build();
		}
	}
	
	@PUT
	@Path("/application")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN) 
	public Response updateHardware(@FormParam("aid") int parid,
								   @FormParam("aname") String parName,
								   @FormParam("adesc") String parDesc) {
		try {
			Connection conn = ConnectionFactory.get();
			PreparedStatement stmt = conn.prepareStatement("update anwendung set name = ?, adesc = ? where id = ?");
			
			stmt.setString(1, parName);
			stmt.setString(2, parDesc);
			stmt.setInt(3, parid);
			
			
			int up = stmt.executeUpdate();
			stmt.close();
			conn.close();
			return Response.ok(up).build();
			
		} catch(Exception ex) {
			return Response.status(500).build();
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
		roomHasHardware base = _getSingleRHH(rhid);

		if(base != null) {
			JSONObject baseObject = base.toJSON();
			NetworkInfo nf = _getNetworkInfoForRHH(base);
			JSONArray allAnwendungen = _getAllApplications(base);
			if(nf != null)
				baseObject.put("networkInfo", nf.toJsonWithoutPart());
			
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
	
	@POST
	@Path("/room/hardware")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN) 
	public Response addHardwareToRoom(@FormParam("hid") int parHardware,
							          @FormParam("roomID") int parRoom,
							          @FormParam("name") String parName,
							          @FormParam("rhdesc") String parDesc) {
		
		try {
			if(parName != null && parDesc != null) {
				Connection conn = ConnectionFactory.get();
				PreparedStatement stmt = conn.prepareStatement("insert into roomHAShardware VALUES(?,?,?,?,?)");
				
				Statement highes = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet res = highes.executeQuery("select MAX(id) as max from roomHAShardware");
				int max = -1;
				while (res.next()) {
					max = res.getInt("max");
				}
				res.close();
				highes.close();
				max++;
				
				stmt.setInt(1, max);
				stmt.setInt(2, parHardware);
				stmt.setInt(3, parRoom);
				stmt.setString(4, parName);
				stmt.setString(5, parDesc);
				
				
				int inserted = stmt.executeUpdate();
				stmt.close();
				conn.close();
				return Response.ok(inserted).build();
			}
			else {
				return javax.ws.rs.core.Response.status(400).build();
			}
		}
		catch(Exception ex) {
			return javax.ws.rs.core.Response.status(500).build();
		}
	}
	
	@PUT
	@Path("/room/hardware")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN) 
	public Response changeHardware(@FormParam("hid") int parHardware,
							          @FormParam("name") String parName,
							          @FormParam("rhdesc") String parDesc) {
		try {
			if(parName != null && parDesc != null) {
				Connection conn = ConnectionFactory.get();
				PreparedStatement stmt = conn.prepareStatement("update roomHAShardware SET name = ?, rhdesc = ? where id = ?");
				stmt.setString(1, parName);
				stmt.setString(2, parDesc);
				stmt.setInt(3, parHardware);
				
				int updated = stmt.executeUpdate();
				stmt.close();
				conn.close();
				return Response.ok(updated).build();
			}
			else {
				return javax.ws.rs.core.Response.status(400).build();
			}
		}
		catch(Exception ex) {
			return javax.ws.rs.core.Response.status(500).build();
		}
	}
	
	
	@DELETE
	@Path("/room/hardware")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN) 
	public Response deleteHardwareFromRoom(@FormParam("rhid") int rhid) {
		try {
			Connection conn = ConnectionFactory.get();
			PreparedStatement stmt = conn.prepareStatement("delete from roomHasHardware where id = ?");
			
			stmt.setInt(1, rhid);
			
			
			int del = stmt.executeUpdate();
			stmt.close();
			conn.close();
			return Response.ok(del).build();
			
		} catch(Exception ex) {
			return javax.ws.rs.core.Response.status(500).build();
		}
	}
	
	
	private JSONArray _getAllApplications(roomHasHardware base) {
		JSONArray all = new JSONArray();
		try {
			Connection conn = ConnectionFactory.get();
			PreparedStatement prepStmt = conn.prepareStatement("select a.id, a.name, a.adesc from hasAnwendung ha JOIN Anwendung a ON ha.AnwendungsID = a.id where part = ?");
			prepStmt.setInt(1, base.getId());
			ResultSet rs = prepStmt.executeQuery();
			
			while (rs.next()) {
				Anwendung a = new Anwendung(rs.getInt(1), rs.getString(2), rs.getString(3));
				all.add(a.toJson());
			}
			
			rs.close();
			prepStmt.close();
			conn.close();
		}
		catch(SQLException ex) {
			
		}
		
		
		return all;
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
			PreparedStatement prepStmt = conn.prepareStatement("select rh.rhdesc as hdesc, rh.ID as rhID, rh.hardwareID as hwID, rh.roomid as roomID, rh.name as hwName from roomHAShardware rh where rh.id = ?");
			prepStmt.setInt(1, id);
			ResultSet rs = prepStmt.executeQuery();
			
			roomHasHardware l = null;
			while (rs.next()) {
				l = new roomHasHardware(new Room(rs.getInt("roomID"), "", null), new Hardware(rs.getInt("hwID"), "", "", ""), rs.getString("hwName"), rs.getInt("rhID"), rs.getString("hdesc"));
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

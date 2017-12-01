package at.Database;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;
import java.util.Properties;

import javax.sql.RowSet;

public class ConnectionFactory {
	
	private static String driver = "oracle.jdbc.driver.OracleDriver";
	private static String driverURL = "jdbc:oracle:thin:@";
	private static String user = "d4a04";
	private static String pwd = "d4a";
	private static String host = "192.168.128.152:1521:";
	private static String database = "ora11g";
	
	
	
	private static void setDriver(String driver) {
		ConnectionFactory.driver = driver;
		try {
			Class.forName(driver);
		}
		catch(ClassNotFoundException ex) {
			System.err.println(ex.getMessage());
		}
	}

	private static void setDriverURL(String driverURL) {
		ConnectionFactory.driverURL = driverURL;
	}

	private static void setUser(String user) {
		ConnectionFactory.user = user;
	}

	private static void setPwd(String pwd) {
		ConnectionFactory.pwd = pwd;
	}

	private static void setHost(String host) {
		ConnectionFactory.host = host;
	}

	private static void setDatabase(String database) {
		ConnectionFactory.database = database;
	}

	private static void setMyReference(ConnectionFactory myReference) {
		ConnectionFactory.myReference = myReference;
	}

	public static String getDriver() {
		return driver;
	}

	public static String getDriverURL() {
		return driverURL;
	}

	public static String getUser() {
		return user;
	}

	public static String getPwd() {
		return pwd;
	}

	public static String getHost() {
		return host;
	}

	public static String getDatabase() {
		return database;
	}

	public static ConnectionFactory getMyReference() {
		return myReference;
	}

	private static ConnectionFactory myReference = new ConnectionFactory();
	
	private ConnectionFactory() {
		try {
			Class.forName(driver);
		}
		catch(ClassNotFoundException ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	public static Connection get() throws SQLException{
		String url = driverURL +host + database;
		return DriverManager.getConnection(url,user,pwd);
	}
}

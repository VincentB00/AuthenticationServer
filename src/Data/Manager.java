package Data;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.gson.Gson;

public class Manager 
{
	public SQL sql;
	public Gson gson;
	public Random random;
	
	public Manager()
	{
		gson = new Gson();
		sql = null;
		random = new Random();
	}
	
	public Manager(String ip, int port, String username, String password, String schema)
	{
		sql = new SQL(ip, port, username, password, schema);
		gson = new Gson();
		random = new Random();
	}
	
	public void setSQL(String jsonBody) throws StatusException
	{
		SQLData sqlData = gson.fromJson(jsonBody, Data.SQLData.class);
		
		if(sqlData != null && sqlData.schema != null && sqlData.schema.compareTo("") != 0)
		{
			sql = new SQL(sqlData.ip, sqlData.port, sqlData.username, sqlData.password, sqlData.schema);
		}
		else
			throw new StatusException(400, "Error: body package", gson);
	}
	
	public static String toJson(String variable, String obj)
	{
		return String.format("{\"%s\":\"%s\"}", variable, obj);
	}
	public static String toJson(String variable, int obj)
	{
		return String.format("{\"%s\":%s}", variable, obj);
	}
	public static String toJson(String variable, double obj)
	{
		return String.format("{\"%s\":%s}", variable, obj);
	}
	
	public static String toJson(String[] variable, String[] obj)
	{
		String result = "{";
		for(int count = 0; count < variable.length; count++)
		{
			if(count >= variable.length - 1)
				result += String.format("\"%s\":\"%s\"", variable[count], obj[count]);
			else
				result += String.format("\"%s\":\"%s\",", variable[count], obj[count]);
		}
		result += "}";
		return result;
	}
	
	public String checkServerStatus() throws StatusException
	{
		String status = "normal";
		String result = "{";
		if(sql == null)
		{
			result += "\"SQL_connection\":\"fail\"";
			status = "bad";
			result += String.format(",\"status\":\"%s\"}", status);
			return result;
		}
		
		try(ResultSet rs = sql.executeQuery("select now();"))
		{
			String temp = "";
			while(rs.next())
				temp = rs.getString(1);
			
			rs.close();
			
			if(temp == null || temp.compareTo("") == 0)
			{
				status = "bad";
				result += "\"SQL_connection\":\"fail\"";
			}
			else
			{
				result += "\"SQL_connection\":\"pass\"";
				result += String.format(",\"SQL_datetime_test\":\"%s\"", temp);
			}
				
		}
		catch(Exception ex)
		{
			status = "bad";
			result += "\"SQL_connection\":\"fail\"";
		}
		
		sql.closeConnection();	
		
		result += String.format(",\"status\":\"%s\"}", status);
		return result;
	}
	
	public void log(String log)
	{
		sql.executeUpdate(String.format("INSERT INTO %s.log (log) VALUES ('%s');", sql.schema, log));
	}
	
	///////////////////////////website//////////////////////////////
	
	public void registerWebsite(String jsonBody) throws StatusException
	{
		Website website = gson.fromJson(jsonBody, Website.class);
		
		if(website == null || website.name == null || website.name.compareTo("") == 0)
		{
			throw new StatusException(400, "bad body package", gson);
		}
		
		int effectRow = sql.executeUpdate(String.format("INSERT INTO %s.websites (name, ipv4, ipv6, port) VALUES ('%s', '%s', '%s', '%s');", sql.schema, website.name, website.ipv4, website.ipv6, website.port));
		
		if(effectRow <= 0)
		{
			throw new StatusException(202, gson);
		}
	}
	
	/**
	 * this method will execute querry to find out full Website information
	 * @param jsonBody
	 * @return
	 * @throws StatusException
	 */
	public Website getWebsite(String jsonBody) throws StatusException
	{
		Website website = gson.fromJson(jsonBody, Website.class);
		Website newWebsite = null;
		
		if(website.id > 0)
		{
			try(ResultSet rs = sql.executeQuery(String.format("SELECT * FROM %s.websites where id = %s;", sql.schema, website.id)))
			{
				while(rs.next())
					newWebsite = new Website(rs.getInt("id"), rs.getString("name"), rs.getString("ipv4"), rs.getString("ipv6"), rs.getInt("port"));
			
				rs.close();
			}
			catch(Exception ex)
			{
				log(ex.getMessage());
			}
			
			if(newWebsite != null)
			{
				sql.closeConnection();
				return newWebsite;
			}
		}
		
		if((website.name == null || website.name.compareTo("") == 0) && (website.ipv4 == null || website.ipv4.compareTo("") == 0))
			throw new StatusException(400, "invalid website name XOR ipv4", gson);
		
		try(ResultSet rs = sql.executeQuery(String.format("SELECT * FROM %s.websites where name = '%s' or ipv4 = '%s';", sql.schema, website.name, website.ipv4)))
		{
			while(rs.next())
			{
				newWebsite = new Website(rs.getInt("id"), rs.getString("name"), rs.getString("ipv4"), rs.getString("ipv6"), rs.getInt("port"));
				if(newWebsite.name != null && website.name != null)
					if(newWebsite.name.compareTo(website.name) == 0)
						break;
				
				if(newWebsite.ipv4 != null && website.ipv4 != null)
					if(newWebsite.ipv4.compareTo(website.ipv4) == 0)
						break;
			}
			
			rs.close();
		}
		catch(Exception ex)
		{
			log(ex.getMessage());
		}
		
		if(newWebsite == null)
			throw new StatusException(406, "can't find website");
		
		sql.closeConnection();
		return newWebsite;
		
	}
	
	///////////////////////////user_group//////////////////////////////
	
	public void registerUserGroup(String jsonBody) throws StatusException
	{
		UserGroup userGroup = gson.fromJson(jsonBody, UserGroup.class);
		
		if(userGroup == null || userGroup.name == null || userGroup.name.compareTo("") == 0)
		{
			throw new StatusException(400, "bad body package", gson);
		}
		
		int effectRow = sql.executeUpdate(String.format("INSERT INTO %s.user_groups (name, level, description) VALUES ('%s', '%s', '%s');", sql.schema, userGroup.name.toUpperCase(), userGroup.level, userGroup.description));
	
		if(effectRow <= 0)
		{
			throw new StatusException(202, gson);
		}
	}
	
	public UserGroup getUserGroup(String jsonBody) throws StatusException
	{
		UserGroup userGroup = gson.fromJson(jsonBody, UserGroup.class);
		UserGroup newUserGroup = null;
		
		if(userGroup.name == null && userGroup.id <= 0)
			throw new StatusException(400, "Invalid/missing user group name |  user group id");
		
		if(userGroup.name != null)
		{
			try(ResultSet rs = sql.executeQuery(String.format("SELECT * FROM %s.user_groups where name = '%s';", sql.schema, userGroup.name)))
			{
				while(rs.next())
				{
					UserGroup newUserGroupT = new UserGroup(rs.getInt("id"), rs.getString("name"), rs.getInt("level"), rs.getString("description"));
					if(newUserGroupT.name.compareTo(userGroup.name) == 0)
					{
						newUserGroup = newUserGroupT;
						break;
					}
				}

				rs.close();
			}
			catch(Exception ex)
			{
				log(ex.getMessage());
			}
		}
		
		if(userGroup.id > 0 && newUserGroup == null)
		{
			try(ResultSet rs = sql.executeQuery(String.format("SELECT * FROM %s.user_groups where id = '%s';", sql.schema, userGroup.id)))
			{
				while(rs.next())
				{
					newUserGroup = new UserGroup(rs.getInt("id"), rs.getString("name"), rs.getInt("level"), rs.getString("description"));
				}

				rs.close();
			}
			catch(Exception ex)
			{
				log(ex.getMessage());
			}
		}
		
		sql.closeConnection();
		
		return newUserGroup;
	}
	
	///////////////////////////user//////////////////////////////
	
	public boolean checkValidUsername(String username)
	{
		boolean result = true;
		try(ResultSet rs = sql.executeQuery(String.format("SELECT * FROM %s.users where username = '%s';", sql.schema, username)))
		{
			while(rs.next())
			{
				String usernameT = rs.getString("username");
				if(username.compareTo(usernameT) == 0)
				{
					result = false;
					break;
				}
			}

			rs.close();
		}
		catch(Exception ex)
		{
			log(ex.getMessage());
			result = false;
		}
		
		sql.closeConnection();
		return result;
	}
	
	public int registerUser(String jsonBody) throws StatusException
	{
		int newUserID = 0;
		User user = gson.fromJson(jsonBody, User.class);
		AdditionalData ad = gson.fromJson(jsonBody, AdditionalData.class);
		
		if(user == null || user.username == null || user.password == null)
		{
			throw new StatusException(400, "bad body package", gson);
		}
		
		if(!checkValidUsername(user.username))
			throw new StatusException(400, "invalid username | username already exist", gson);
		
		if(ad.dev_key != null && !isValidDevKeyRaw(ad.dev_key))
			throw new StatusException(400, "invalid dev_key", gson);
			
		user.password = DigestUtils.sha256Hex(user.password);
		
//		int effectRow = sql.executeUpdate(String.format("INSERT INTO %s.users (username, password, email, full_name, phone_number) VALUES ('%s', '%s', '%s', '%s', '%s');", sql.schema, user.username, user.password, user.email, user.full_name, user.phone_number));
		
//		if(effectRow <= 0)
//		{
//			throw new StatusException(202, gson);
//		}
		
		try(ResultSet rs = sql.executeQuery(String.format("call %s.register_user('%s', '%s', '%s', '%s', '%s');", sql.schema, user.username, user.password, user.email, user.full_name, user.phone_number)))
		{
			while(rs.next())
				newUserID = rs.getInt("user_id");
			
			rs.close();
		}
		catch(Exception ex)
		{
			log(ex.getMessage());
			throw new StatusException(500);
		}
		
		sql.closeConnection();
		
		if(newUserID <= 0)
			throw new StatusException(202, gson);
		
		if(ad.dev_key != null)
		{
			Website website = null;
			
			if(ad.website_name != null)
			{
				website = getWebsite(Manager.toJson("name", ad.website_name));
			}
			else if(ad.ipv4 != null)
			{
				website = getWebsite(Manager.toJson("ipv4", ad.ipv4));
				
			}
			
			if(website == null || website.id <= 0)
				throw new StatusException(400, "bad additional request body");
			
			UserGroup userGroup = getUserGroup(Manager.toJson("name", ad.user_group_name));
			User userT = getUser(jsonBody);
			int userGroupID = userGroup.id;
			int websiteID = website.id;
			int userID = userT.id;
			
			int effectRow = sql.executeUpdate(String.format("INSERT INTO %s.privileges (user_id, user_group_id, website_id) VALUES ('%s', '%s', '%s');", sql.schema, userID, userGroupID, websiteID));
		}
		
		return newUserID;
	}
	
	public User getUser(int id)
	{
		User newUser = null;
		try(ResultSet rs = sql.executeQuery(String.format("SELECT * FROM %s.users where users.id = '%s';", sql.schema, id)))
		{
			while(rs.next())
				newUser = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getString("create_time"), rs.getString("full_name"), rs.getString("phone_number"));

			rs.close();
		}
		catch(Exception ex)
		{
			log(ex.getMessage());
		}
		
		sql.closeConnection();
		return newUser;
		
	}
	
	/**
	 * this method will execute querry to find out full user information
	 * @param JsonBody
	 * @return
	 * @throws StatusException
	 */
	public User getUser(String JsonBody) throws StatusException
	{
		User user = gson.fromJson(JsonBody, User.class);
		User newUser = null;
		
		if(user.id > 0)
		{
			newUser = getUser(user.id);
			
			if(newUser != null)
				return newUser;
		}
		
		if(user.username.compareTo("") == 0 || user.username == null || user.password.compareTo("") == 0 || user.password == null)
			throw new StatusException(400, "invalid username or password", gson);
		
		user.password = DigestUtils.sha256Hex(user.password);
		
		try(ResultSet rs = sql.executeQuery(String.format("SELECT * FROM %s.users where users.username = '%s' and users.password = '%s';", sql.schema, user.username, user.password)))
		{
			while(rs.next())
			{
				User temp = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getString("create_time"), rs.getString("full_name"), rs.getString("phone_number"));
				if(temp.username.compareTo(user.username) == 0)
				{
					newUser = temp;
					break;
				}
			}
			
			rs.close();
		}
		catch(Exception ex)
		{
			log(ex.getMessage());
		}
		
		if(newUser == null)
			throw new StatusException(403, "can't find user");
		
		sql.closeConnection();
		return newUser;
	}
	
	public String login(String jsonBody) throws StatusException
	{
		String token = "";
		
		User user = getUser(jsonBody);
		Website website = getWebsite(jsonBody);
		UserPrivilege userPrivilege = getUserPrivilege(user.id, website.id);
		
		if(userPrivilege == null)
			userPrivilege = createPrivilege(user.id, website.id);
		
//		if(user == null || user.username == null || user.password == null || website == null || website.name == null || website.name.compareTo("") == 0)
//			throw new StatusException(400, "Error: body package", gson);
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();
		
		token = DigestUtils.sha256Hex(dtf.format(now).toString() + user.username);
		
		int effectRow = sql.executeUpdate(String.format("INSERT INTO %s.session (token, session_data) VALUES ('%s', '%s');", sql.schema, token, gson.toJson(userPrivilege).toString()));
		
		if(effectRow <= 0)
			throw new StatusException(500, "something when wrong with database");
		
		return token;
	}
	
	public void logout(String jsonBody) throws StatusException
	{
		SessionData sessionData = gson.fromJson(jsonBody, SessionData.class);
		
		if(sessionData.token != null)
		{
			int effectRow = sql.executeUpdate(String.format("DELETE FROM %s.session WHERE (token = '%s');", sql.schema, sessionData.token));
			if(effectRow <= 0)
				throw new StatusException(500, "something when wrong with database");
		}
	}
	
	public UserPrivilege getUserPrivilege(int userID, int websiteID)
	{
		UserPrivilege userPrivilege = null;
		try(ResultSet rs = sql.executeQuery(String.format("call %s.get_user_privilege(%s, %s);", sql.schema, userID, websiteID)))
		{
			while(rs.next())
				userPrivilege = new UserPrivilege(rs.getInt("user_id"), rs.getInt("user_group_id"), rs.getInt("website_id"), rs.getString("user_full_name"), rs.getString("website_name"), rs.getString("user_group_name"), rs.getInt("user_level"));
			rs.close();
		}
		catch(Exception ex)
		{
			log(ex.getMessage());
			sql.closeConnection();
			return null;
		}
		
		sql.closeConnection();
		return userPrivilege;
	}
	
	public UserPrivilege createPrivilege(int userID, int websiteID) throws StatusException
	{
		int effectRow = sql.executeUpdate(String.format("INSERT INTO %s.privileges (user_id, user_group_id, website_id) VALUES ('%s', '%s', '%s');", sql.schema, userID, 2, websiteID));
		
		if(effectRow <= 0)
			throw new StatusException(500, "something when wrong with database");
		
		return getUserPrivilege(userID, websiteID);
	}
	
	///////////////////////////session//////////////////////////////
	
	public SessionData getSessionData(String jsonBody) throws StatusException
	{
		String token = getTokenFromJsonBody(jsonBody);
		SessionData newSessionData = null;
		
		try(ResultSet rs = sql.executeQuery(String.format("SELECT * FROM %s.session where token = '%s'", sql.schema, token)))
		{
			while(rs.next())
				newSessionData = new SessionData(rs.getString("token"), rs.getString("current_time"), rs.getString("session_data"));
		}
		catch(Exception ex)
		{
			log(ex.getMessage());
		}
		
		sql.closeConnection();
		return newSessionData;
	}
	
	public SessionData getSessionDataFromToken(String token) throws StatusException
	{
		return getSessionData(String.format("{\"token\":\"%s\"}", token));
	}
	
	public String checkSessionStatus(String token) throws StatusException
	{
		SessionData sessionData = getSessionData(String.format("{\"token\":\"%s\"}", token));
		
		if(sessionData == null)
			return String.format("{\"session\":\"%s\"}", "expire");
		else
			return String.format("{\"session\":\"%s\"}", "alive");
	}
	
	public void reNewSession(String jsonBody) throws StatusException
	{
		String token = getTokenFromJsonBody(jsonBody);
		
		int effectRow = sql.executeUpdate(String.format("UPDATE %s.session SET session.current_time = now() WHERE token = '%s';", sql.schema, token));
		
		if(effectRow <= 0)
			throw new StatusException(403, "something when wrong");
	}
	
	public String getTokenFromJsonBody(String jsonBody) throws StatusException
	{
		SessionData sessionToken = gson.fromJson(jsonBody, SessionData.class);
		
		if(sessionToken == null || sessionToken.token == null)
			throw new StatusException(400, "invalid or missing token");
		
		return sessionToken.token.toString();
	}
	
	public SessionData checkAndgetSessionDataFromParameter(HttpServletRequest request) throws StatusException
	{
		Object token = tryGetParameter(request, "token");
		
		if(token == null)
			throw new StatusException(400, "invalid or missing token");
		
		SessionData sessionData = getSessionDataFromToken(token.toString());
		
		if(sessionData == null)
			throw new StatusException(403, "session/token expire or invalid");
		
		return sessionData;
	}
	
	private Object tryGetParameter(HttpServletRequest request, String parameterName)
	{
		try
		{
			Object obj = request.getParameter(parameterName);
			return obj;
		}
		catch(Exception ex)
		{
			return null;
		}
	}
	
	///////////////////////////developer//////////////////////////////
	
	public DeveloperData getDevData(String jsonBody) throws StatusException
	{
		DeveloperData devData = gson.fromJson(jsonBody, DeveloperData.class);
		DeveloperData newDevData = null;
		
		if(devData.dev_key == null || devData.dev_key.compareTo("") == 0)
			throw new StatusException(403, "invalid or missing developer key");
		
		try(ResultSet rs = sql.executeQuery(String.format("SELECT * FROM %s.developer where dev_key = '%s';", sql.schema, devData.dev_key)))
		{
			while(rs.next())
			{
				DeveloperData temp = new DeveloperData(rs.getInt("id"), rs.getInt("user_id"), rs.getString("dev_key"), rs.getString("create_time"));
				if(temp.dev_key.compareTo(devData.dev_key) == 0)
				{
					newDevData = temp;
					rs.close();
					break;
				}				
			}
		}
		catch(Exception ex)
		{
			log(ex.getMessage());
		}
		
		sql.closeConnection();
		return newDevData;
	}
	
	public boolean isValidDevKey(String jsonBody) throws StatusException
	{
		DeveloperData devData = getDevData(jsonBody);
		
		return devData != null;
	}
	
	public boolean isValidDevKeyRaw(String dev_key)
	{
		boolean result = false;
		
		try(ResultSet rs = sql.executeQuery(String.format("SELECT * FROM %s.developer where dev_key = '%s';", sql.schema, dev_key)))
		{
			while(rs.next())
			{
				String devKeyT = rs.getString("dev_key");
				if(dev_key.compareTo(devKeyT) == 0)
				{
					result = true;
					break;
				}
			}
		}
		catch(Exception ex)
		{
			log(ex.getMessage());
		}
		
		sql.closeConnection();
		
		return result;
	}
	
	public void checkValidDevKey(String jsonBody) throws StatusException
	{
		DeveloperData devData = getDevData(jsonBody);
		
		if(devData == null)
			throw new StatusException(403, "invalid or missing developer key");
	}
}

package Data;

public class Session 
{
	public String token;
	public int user_id;
	public int website_id;
	public String current_time;
	
	public Session(String token, int user_id, int website_id, String current_time) {
		super();
		this.token = token;
		this.user_id = user_id;
		this.website_id = website_id;
		this.current_time = current_time;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getWebsite_id() {
		return website_id;
	}

	public void setWebsite_id(int website_id) {
		this.website_id = website_id;
	}

	public String getCurrent_time() {
		return current_time;
	}

	public void setCurrent_time(String current_time) {
		this.current_time = current_time;
	}
	
	
}

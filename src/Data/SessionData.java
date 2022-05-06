package Data;

public class SessionData 
{
	public String token;
	public String current_time;
	public String session_data;
	
	public SessionData(String token, String current_time, String session_data) {
		super();
		this.token = token;
		this.current_time = current_time;
		this.session_data = session_data;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCurrent_time() {
		return current_time;
	}

	public void setCurrent_time(String current_time) {
		this.current_time = current_time;
	}

	public String getSession_data() {
		return session_data;
	}

	public void setSession_data(String session_data) {
		this.session_data = session_data;
	}
	
	
}

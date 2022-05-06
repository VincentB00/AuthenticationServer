package Data;

public class User 
{
	public int id;
	public String username;
	public String password;
	public String email = "";
	public String create_time = "";
	public String full_name = "";
	public String phone_number = "";
	
	public User(int id, String username, String password, String email, String create_time, String full_name,
			String phone_number) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.create_time = create_time;
		this.full_name = full_name;
		this.phone_number = phone_number;
	}

	public User(String username, String password, String email, String create_time, String full_name,
			String phone_number) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.create_time = create_time;
		this.full_name = full_name;
		this.phone_number = phone_number;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}

package Data;

public class UserPrivilege 
{
	public int user_id;
	public int user_group_id;
	public int website_id;
	public String user_full_name;
	public String website_name;
	public String user_group_name;
	public int user_level;
	
	public UserPrivilege(int user_id, int user_group_id, int website_id, String user_full_name, String website_name,
			String user_group_name, int user_level) {
		super();
		this.user_id = user_id;
		this.user_group_id = user_group_id;
		this.website_id = website_id;
		this.user_full_name = user_full_name;
		this.website_name = website_name;
		this.user_group_name = user_group_name;
		this.user_level = user_level;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getUser_group_id() {
		return user_group_id;
	}

	public void setUser_group_id(int user_group_id) {
		this.user_group_id = user_group_id;
	}

	public int getWebsite_id() {
		return website_id;
	}

	public void setWebsite_id(int website_id) {
		this.website_id = website_id;
	}

	public String getUser_full_name() {
		return user_full_name;
	}

	public void setUser_full_name(String user_full_name) {
		this.user_full_name = user_full_name;
	}

	public String getWebsite_name() {
		return website_name;
	}

	public void setWebsite_name(String website_name) {
		this.website_name = website_name;
	}

	public String getUser_group_name() {
		return user_group_name;
	}

	public void setUser_group_name(String user_group_name) {
		this.user_group_name = user_group_name;
	}

	public int getUser_level() {
		return user_level;
	}

	public void setUser_level(int user_level) {
		this.user_level = user_level;
	}
	
	
}

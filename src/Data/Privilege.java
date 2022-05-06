package Data;

public class Privilege 
{
	public int user_id;
	public int user_group_id;
	public int website_id;
	
	public Privilege(int user_id, int user_group_id, int website_id) {
		super();
		this.user_id = user_id;
		this.user_group_id = user_group_id;
		this.website_id = website_id;
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
	
	
}

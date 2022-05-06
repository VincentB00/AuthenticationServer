package Data;

public class DeveloperData 
{
	public int id;
	public int user_id;
	public String dev_key;
	public String create_time;

	public DeveloperData(int id, int user_id, String dev_key, String create_time) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.dev_key = dev_key;
		this.create_time = create_time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getDev_key() {
		return dev_key;
	}

	public void setDev_key(String dev_key) {
		this.dev_key = dev_key;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

}

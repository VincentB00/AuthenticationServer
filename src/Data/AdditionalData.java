package Data;

public class AdditionalData 
{
	public String dev_key;
	public String ipv4;
	public String website_name;
	public String user_group_name;
	public int user_group_level;
	
	public AdditionalData(String dev_key, String user_group_name, int user_group_level) {
		super();
		this.dev_key = dev_key;
		this.user_group_name = user_group_name;
		this.user_group_level = user_group_level;
	}
	
	public AdditionalData() 
	{

	}
}

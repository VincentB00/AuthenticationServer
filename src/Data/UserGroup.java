package Data;

public class UserGroup 
{
	public int id;
	public String name;
	public int level = 0;
	public String description;
	
	public UserGroup(int id, String name, int level, String description) {
		super();
		this.id = id;
		this.name = name;
		this.level = level;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}

package Data;

public class Website 
{
	public int id;
	public String name;
	public String ipv4;
	public String ipv6;
	public int port;
	
	
	public Website(int id, String name, String ipv4, String ipv6, int port) 
	{
		super();
		this.id = id;
		this.name = name;
		this.ipv4 = ipv4;
		this.ipv6 = ipv6;
		this.port = port;
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


	public String getIpv4() {
		return ipv4;
	}


	public void setIpv4(String ipv4) {
		this.ipv4 = ipv4;
	}


	public String getIpv6() {
		return ipv6;
	}


	public void setIpv6(String ipv6) {
		this.ipv6 = ipv6;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}
	
	
}

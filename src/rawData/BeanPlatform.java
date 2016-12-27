package rawData;

import java.io.Serializable;

public class BeanPlatform implements Serializable {

	private String name;
	private String profileUrl;
	
	public BeanPlatform()
	{
		name = "";
		profileUrl = "";
	}
	
	public BeanPlatform(String name, String profileUrl)
	{
		this.name = name;
		this.profileUrl = profileUrl;
	}
	
	public String NAME()
	{
		return name;
	}
	
	public String URL()
	{
		return profileUrl;
	}
	
	public void editName(String newName)
	{
		this.name = newName;
	}
	
	public void editUrl(String newUrl)
	{
		this.profileUrl = newUrl;
	}
	
	public String toString()
	{
		return name;
	}
}

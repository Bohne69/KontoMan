package data;

import java.io.Serializable;

public class ProgramSettings implements Serializable {

	private String platformSavePath;
	
	private static ProgramSettings instance;
	
	public static ProgramSettings getInstance()
	{
		if(instance == null)
			instance = new ProgramSettings();
		return instance;
	}
	
	public static void setInstance(ProgramSettings s)
	{
		instance = s;
	}
	
	public ProgramSettings()
	{
		platformSavePath = "platforms.ser";
	}
	
	public String PLATFORMS_FILE_PATH()
	{
		return this.platformSavePath;
	}
	
}

package data;

import java.io.Serializable;

import dataStorage.DataSerializer;
import rawData.BeanDate;

public class ProgramSettings implements Serializable {

	private String platformSavePath;
	private BeanDate lastDate;
	private String logPath;
	
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
		logPath = "log.log";
		lastDate = new BeanDate(true);
	}
	
	public String PLATFORMS_FILE_PATH()
	{
		return this.platformSavePath;
	}
	
	public String LOG_PATH() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	public BeanDate getLastDate() {
		return lastDate;
	}

	public void setLastDate(BeanDate lastDate) {
		this.lastDate = lastDate;
	}
	
	public void load()
	{
		instance = DataSerializer.loadProgramSettings();
	}
	
	public void save()
	{
		DataSerializer.saveProgramSettings(ProgramSettings.this);
	}
}

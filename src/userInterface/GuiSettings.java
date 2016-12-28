package userInterface;

import java.awt.Color;
import java.io.Serializable;

import dataStorage.DataSerializer;

public class GuiSettings implements Serializable {

	//TODO
	public static Color THEME_COLOR = new Color(156,188,214);
	private String currentSaveFile;
	
	private static GuiSettings instance;
	
	public static GuiSettings getInstance()
	{
		if(instance == null)
		{
			load();
			if(instance == null)
				instance = new GuiSettings();
		}
		return instance;
	}
	
	public GuiSettings()
	{
		this.setCurrentSaveFile("");
	}

	public String getCurrentSaveFile() {
		return currentSaveFile;
	}

	public void setCurrentSaveFile(String currentSaveFile) {
		this.currentSaveFile = currentSaveFile;
		save();
	}
	
	public void save()
	{
		DataSerializer.saveGuiSettings(GuiSettings.this);
	}
	
	public static void load()
	{
		instance = DataSerializer.loadGuiSettings();
	}
}

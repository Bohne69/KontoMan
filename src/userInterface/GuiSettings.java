package userInterface;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.UIManager;

import dataStorage.DataSerializer;

public class GuiSettings implements Serializable {

	//TODO
	public static Color BACKGROUND_COLOR = UIManager.getColor("nimbusLightBackground");
	public static Color HIGHLIGHT_COLOR = UIManager.getColor("nimbusSelectionBackground");
	public static Color WARNING_COLOR = new Color(255, 166, 50);
	public static Color ERROR_COLOR = new Color(224, 62, 62);
	
	private double warningThreshold = 100;
	private String currentSaveFile;
	private String[] lastProjects = new String[5];
	
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
	
	public String[] getLastProjects()
	{
		return lastProjects;
	}
	
	public void addLastProject(String s)
	{
		for(String curr : lastProjects)
		{
			if(s.equals(curr))
				return;
		}
		
		int nextFreeSpot = 0;
		for(int i = 0; i < 4; i++)
		{
			if(lastProjects[i] == null)
				nextFreeSpot = i;
		}
		
		if(nextFreeSpot < 3){
			lastProjects[nextFreeSpot] = s;
		}
		else
		{
			lastProjects[4] = lastProjects[3];
			lastProjects[3] = lastProjects[2];
			lastProjects[2] = lastProjects[1];
			lastProjects[1] = lastProjects[0];
			lastProjects[0] = s;
		}
		
		save();
	}

	public void removeLastProject(String s)
	{
		for(int i = 0; i < 4; i++)
		{
			if(lastProjects[i] != null)
			{
				if(lastProjects[i].equals(s))
				{
					lastProjects[i] = null;
				}
			}
		}
		
		save();
	}
	
	public void setCurrentSaveFile(String currentSaveFile) {
		this.currentSaveFile = currentSaveFile;
		addLastProject(currentSaveFile);
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

	
	public double getWarningThreshold() {
		return warningThreshold;
	}

	
	public void setWarningThreshold(double warningThreshold) {
		this.warningThreshold = warningThreshold;
		save();
	}
}

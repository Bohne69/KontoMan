package userInterface;

import java.awt.Color;

public class GuiSettings {

	//TODO
	public Color THEME_COLOR = new Color(156,188,214);
	
	private static GuiSettings instance;
	
	public static GuiSettings getInstance()
	{
		if(instance == null)
			instance = new GuiSettings();
		return instance;
	}
	
	public GuiSettings()
	{
		
	}
	
}

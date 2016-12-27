package data;

import java.io.Serializable;
import java.util.ArrayList;

import dataStorage.DataSerializer;
import rawData.BeanPlatform;

public class Platforms implements Serializable {

	private ArrayList<BeanPlatform> platforms;
	
	private static Platforms instance;
		
	public static Platforms getInstance()
	{
		if(instance == null)
			instance = new Platforms();
		return instance;
	}
	
	public static void setInstance(Platforms p)
	{
		instance = p;
	}
	
	public Platforms()
	{
		platforms = new ArrayList<BeanPlatform>();
	}
	
	public void addPlatform(BeanPlatform p)
	{
		platforms.add(p);
		save();
	}
	
	public void removePlatform(BeanPlatform p)
	{
		platforms.remove(p);
		save();
	}
	
	public ArrayList<BeanPlatform> PLATFORMS()
	{
		return platforms;
	}
	
	public void save()
	{
		DataSerializer.savePlatforms(instance);
	}
	
	public void load()
	{
		setInstance(DataSerializer.loadPlatforms());
	}
}

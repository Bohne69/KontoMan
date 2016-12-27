package data;

import java.io.Serializable;
import java.util.ArrayList;

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
	
	
}

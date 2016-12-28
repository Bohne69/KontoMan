package dataStorage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import applicationLogic.Manager;
import data.Platforms;
import data.ProgramSettings;

public class DataSerializer {

	public static void saveManager(Manager p)
	{		
		try {
			String filepath = "saveData.ser";
			FileOutputStream fileOut = new FileOutputStream(filepath);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject( p );
			objectOut.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static Manager loadManager()
	{
		Manager res = null;
		try {
			String filepath = "saveData.ser";
			FileInputStream fileIn = new FileInputStream(filepath);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);   
			res = (Manager)objectIn.readObject();
			objectIn.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return res;
	}
	
	public static void saveProgramSettings(ProgramSettings p)
	{		
		try {
			String filepath = "settings.ser";
			FileOutputStream fileOut = new FileOutputStream(filepath);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject( p );
			objectOut.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static ProgramSettings loadProgramSettings()
	{
		ProgramSettings res = null;
		try {
			String filepath = "settings.ser";
			FileInputStream fileIn = new FileInputStream(filepath);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);   
			res = (ProgramSettings)objectIn.readObject();
			objectIn.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return res;
	}
		
	public static void savePlatforms(Platforms p)
	{		
		try {
			String filepath = "platforms.ser";
			FileOutputStream fileOut = new FileOutputStream(filepath);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject( p );
			objectOut.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static Platforms loadPlatforms()
	{
		Platforms res = null;
		try {
			String filepath = "platforms.ser";
			FileInputStream fileIn = new FileInputStream(filepath);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);   
			res = (Platforms)objectIn.readObject();
			objectIn.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return res;
	}
	
}

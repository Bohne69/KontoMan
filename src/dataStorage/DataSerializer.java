package dataStorage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import applicationLogic.Manager;
import data.Platforms;
import data.ProgramSettings;
import userInterface.GuiSettings;

public class DataSerializer {

	public static void saveGuiSettings(GuiSettings p)
	{		
		try {
			String filepath = "guiPreferences.ser";
			FileOutputStream fileOut = new FileOutputStream(filepath);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject( p );
			objectOut.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static GuiSettings loadGuiSettings()
	{
		GuiSettings res = null;
		try {
			String filepath = "guiPreferences.ser";
			FileInputStream fileIn = new FileInputStream(filepath);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);   
			res = (GuiSettings)objectIn.readObject();
			objectIn.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return res;
	}
	
	public static void saveManager(String filepath, Manager p)
	{		
		try {
			FileOutputStream fileOut = new FileOutputStream(filepath);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject( p );
			objectOut.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static Manager loadManager(String filepath) throws FileNotFoundException
	{
		Manager res = null;
		try {
			FileInputStream fileIn = new FileInputStream(filepath);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);   
			res = (Manager)objectIn.readObject();
			objectIn.close();
		} catch (Exception e) {
			throw new FileNotFoundException();
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

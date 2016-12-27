package dataStorage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import data.Platforms;
import data.ProgramSettings;

public class DataSerializer {

	
	
	public static void savePlatforms(Platforms p)
	{		
		try {
			String filepath = ProgramSettings.getInstance().PLATFORMS_FILE_PATH();
			FileOutputStream fileOut = new FileOutputStream(filepath);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject( p );
			objectOut.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public Platforms loadPlatforms()
	{
		Platforms res = null;
		try {
			String filepath = ProgramSettings.getInstance().PLATFORMS_FILE_PATH();
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

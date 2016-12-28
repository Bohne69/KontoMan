package dataStorage;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;

import data.ProgramSettings;

public class Logger {

	public static void log(String message)
	{
		try
		{
			String savePath = ProgramSettings.getInstance().LOG_PATH();
			File file = new File(savePath);
			FileWriter writer = new FileWriter(file, true);
			PrintWriter output = new PrintWriter(writer);
			output.println("[" + LocalDate.now().getDayOfMonth() + "."
					+ LocalDate.now().getMonthValue() + "."
					+ LocalDate.now().getYear() + ", "
					+ LocalTime.now().getHour() + ":" 
					+ LocalTime.now().getMinute() + "] "
					+ message);
			output.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
}

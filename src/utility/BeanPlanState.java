package utility;

public enum BeanPlanState {

	PLANNED,			// Plan ist not within the current Month
	SCHEDULED,			// Plan ist scheduled within the current Month
	LATE,				// Plan is not shipped or finished yet but past it's scheduled time
	SHIPPING,			// Item has been shipped and is on it's way
	LATE_SHIPPING,		// Item is on it's way but has exceeded it's scheduled delivery time
	TAXED,				// Item has been delivered to the local Tax office and has to be retrieved
	DONE,				// Item was successfully delivered/finished
	CANCELLED;			// Plan has been cancelled
	
	public static String stringify(BeanPlanState s)
	{
		switch(s)
		{
		case PLANNED:
			return "Geplant";			
		case SCHEDULED:
			return "In Bearbeitung";						
		case LATE:
			return "Bearbeitung verspätet";			
		case SHIPPING:
			return "Versand";	
		case LATE_SHIPPING:
			return "Versand verspätet";		
		case TAXED:
			return "Im Zoll";		
		case DONE:
			return "Fertiggestellt";		
		default:
			return "Abgebrochen";
		}
	}
	
	public static BeanPlanState fromString(String s)
	{
		if(s.equals("Geplant"))
		{
			return PLANNED;
		}
		else if (s.equals("In Bearbeitung"))
		{
			return SCHEDULED;
		}
		else if (s.equals("Bearbeitung verspätet"))
		{
			return LATE;
		}
		else if (s.equals("Versand"))
		{
			return SHIPPING;
		}
		else if (s.equals("Versand verspätet"))
		{
			return LATE_SHIPPING;
		}
		else if (s.equals("Im Zoll"))
		{
			return TAXED;
		}
		else if (s.equals("Bearbeitung verspätet"))
		{
			return LATE;
		}
		else if (s.equals("Fertiggestellt"))
		{
			return DONE;
		}
		else
		{
			return CANCELLED;
		}
	}
	
	public static String[] toArray()
	{
		return new String[]{
				"Geplant", "In Bearbeitung", "Bearbeitung verspätet", "Versand", "Versand verspätet", "Im Zoll", "Fertiggestellt", "Abgebrochen"
		};
	}
}

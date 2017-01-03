package utility;

public enum BeanMonths {

	NULL, // Causes January's to December's index to fit 1 - 12
	
	JANUAR,
	FEBRUAR,
	MÄRZ,
	APRIL,
	MAI,
	JUNI,
	JULI,
	AUGUST,
	SEPTEMBER,
	OKTOBER,
	NOVEMBER,
	DEZEMBER;
	
	public static BeanMonths fromInt(int i)
	{
		switch(i)
		{
		case 1:
			return JANUAR;			
		case 2:
			return FEBRUAR;			
		case 3:
			return MÄRZ;			
		case 4:
			return APRIL;			
		case 5:
			return MAI;	
		case 6:
			return JUNI;		
		case 7:
			return JULI;		
		case 8:
			return AUGUST;		
		case 9:
			return SEPTEMBER;		
		case 10:
			return OKTOBER;		
		case 11:
			return NOVEMBER;			
		case 12:
			return DEZEMBER;
		default:
			return NULL;
		}
	}
	
	public static String stringify(BeanMonths b)
	{
		switch(b)
		{
		case JANUAR:
			return "Januar";			
		case FEBRUAR:
			return "Feburar";			
		case MÄRZ:
			return "März";			
		case APRIL:
			return "April";			
		case MAI:
			return "Mai";	
		case JUNI:
			return "Juni";		
		case JULI:
			return "Juli";		
		case AUGUST:
			return "August";		
		case SEPTEMBER:
			return "September";		
		case OKTOBER:
			return "Oktober";		
		case NOVEMBER:
			return "November";			
		case DEZEMBER:
			return "Dezember";
		default:
			return "";
		}
	}
	
	public static String[] toArray()
	{
		return new String[]{
				"Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"
		};
	}
}
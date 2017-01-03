package utility;

public enum BeanMonthParts {

	NICHT_ANGEGEBEN,
	ANFANG,
	MITTE,
	ENDE;
	
	public static BeanMonthParts fromInt(int i)
	{
		if(i > 0 && i <= 10)
		{
			return ANFANG;
		}
		else if(i > 10 && i <= 20)
		{
			return MITTE;
		}
		else if(i > 20 && i <= 31)
		{
			return ENDE;
		}
		else
		{
			return NICHT_ANGEGEBEN;
		}
	}
	
	public static BeanMonthParts fromString(String s)
	{
		if(s.equals("Anfang"))
		{
			return ANFANG;
		}
		else if(s.equals("Mitte"))
		{
			return MITTE;
		}
		else if(s.equals("Ende"))
		{
			return ENDE;
		}
		else
		{
			return NICHT_ANGEGEBEN;
		}
	}
	
	public static String stringify(BeanMonthParts b)
	{
		switch(b)
		{
		case ANFANG:
			return "Anfang";
		case MITTE:
			return "Mitte";
		case ENDE:
			return "Ende";
		default:
			return "";
		}
	}
	
	public static String[] toArray()
	{
		return new String[]{
			"Keine Angabe", "Anfang", "Mitte", "Ende"	
		};
	}
}

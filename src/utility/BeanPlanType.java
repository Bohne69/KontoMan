package utility;

public enum BeanPlanType {

	DELIVERY,			// Order iterates through shipped -> received -> finished when updated
	DIRECT_BOOKING,		// Order immediatly goes to finished when updated
	MULTI_STAGE_PLAN;	// Order goes to finished and sets a new Plan when updated (e.g. Kickstarter Reward)
	
	public static String stringify(BeanPlanType s)
	{
		switch(s)
		{
		case DELIVERY:
			return "Versandbestellung";	
		case MULTI_STAGE_PLAN:
			return "Etappenplanung";	
		default:
			return "Buchung";						
		}
	}
	
	public static BeanPlanType fromString(String s)
	{
		if(s.equals("Versandbestellung"))
		{
			return DELIVERY;
		}
		else if(s.equals("Etappenplanung"))
		{
			return MULTI_STAGE_PLAN;
		}
		else
		{
			return DIRECT_BOOKING;
		}
	}
	
	public static String[] toArray()
	{
		return new String[]{
				"Versandbestellung", "Etappenplanung", "Buchung"
		};
	}
}

package rawData;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;

import utility.BeanMonthParts;
import utility.BeanMonths;

public class BeanDate implements Comparator<BeanDate>, Serializable{

	private int year;
	private BeanMonths month;
	private BeanMonthParts part;
	
	public BeanDate(boolean useCurrent)
	{
		if(useCurrent)
		{
			this.year = LocalDate.now().getYear();
			this.month = BeanMonths.fromInt(LocalDate.now().getMonthValue());
			this.part = BeanMonthParts.fromInt(LocalDate.now().getDayOfMonth());
		}
		else
		{
			this.year = 0;
			this.month = BeanMonths.NULL;
			this.part = BeanMonthParts.NICHT_ANGEGEBEN;
		}
	}
	
	public BeanDate(BeanMonthParts part, BeanMonths month, int year)
	{
		this.year = year;
		this.month = month;
		this.part = part;
	}
	
	public int YEAR()
	{
		return this.year;
	}
	
	public BeanMonths MONTH()
	{
		return this.month;
	}
	
	public BeanMonthParts PART()
	{
		return this.part;
	}
	
	public void editYear(int year)
	{
		this.year = year;
	}
	
	public void editMonth(BeanMonths month)
	{
		this.month = month;
	}
	
	public void editPart(BeanMonthParts part)
	{
		this.part = part;
	}

	public BeanDate getNextMonth()
	{
		if(month.compareTo(BeanMonths.DEZEMBER) > 0)
		{
			return new BeanDate(part, BeanMonths.fromInt(month.ordinal() + 1), year);
		}
		else
		{
			return new BeanDate(part, BeanMonths.JANUAR, year + 1);
		}
	}
	
	public BeanDate getDateInXMonths(int monthsFromNow)
	{
		BeanDate res = new BeanDate(part, month, year);
		
		for(int i = 0; i < monthsFromNow; i++)
		{
			res = res.getNextMonth();
		}
		
		return res;
	}
	
	
	
	public String toString()
	{
		String res = BeanMonthParts.stringify(part);
		
		if(res.isEmpty())
		{
			res += (BeanMonths.stringify(month) + " " + year);
		}
		else
		{
			res += (" " + BeanMonths.stringify(month) + " " + year);
		}
		
		return res;
	}
	
	public String toSimpleString()
	{
		return BeanMonths.stringify(month) + " " + year;
	}
	
	public int compare(BeanDate d1, BeanDate d2)
	{
		if(d1.PART().compareTo(d2.PART()) != 0)
		{
			return d1.PART().compareTo(d2.PART());
		}
		
		if(d1.MONTH().compareTo(d2.MONTH()) != 0)
		{
			return d1.MONTH().compareTo(d2.MONTH());
		}
		
		return d1.YEAR() - d2.YEAR();
	}
}

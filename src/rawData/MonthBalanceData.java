package rawData;

public class MonthBalanceData {

	private String date;
	private BeanMoney monthStart;
	private BeanMoney monthMinimum;
	private BeanMoney monthEnd;
	
	public MonthBalanceData()
	{
		
	}

	public BeanMoney getMonthStart() {
		return monthStart;
	}

	public void setMonthStart(BeanMoney monthStart) {
		this.monthStart = monthStart;
	}

	public BeanMoney getMonthMinimum() {
		return monthMinimum;
	}

	public void setMonthMinimum(BeanMoney monthMinimum) {
		this.monthMinimum = monthMinimum;
	}

	public BeanMoney getMonthEnd() {
		return monthEnd;
	}

	public void setMonthEnd(BeanMoney monthEnd) {
		this.monthEnd = monthEnd;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
		
}

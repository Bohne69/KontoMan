package rawData;

import java.io.Serializable;

public class BeanAccount implements Serializable{

	private BeanMoney balance;
	private BeanMoney monthlyBooking;
	
	public BeanAccount()
	{
		balance = new BeanMoney();
		balance = new BeanMoney();
	}
	
	public BeanAccount(double balance, double monthlyBooking)
	{
		this.balance = new BeanMoney(balance);
		this.monthlyBooking = new BeanMoney(monthlyBooking);
	}

	public BeanMoney BALANCE()
	{
		return balance;
	}
	
	public BeanMoney MONTHLY_BOOKING()
	{
		return monthlyBooking;
	}
	
	public void setBalance(double newBalance)
	{
		this.balance = new BeanMoney(newBalance);
	}
	
	public void setMonthlyBooking(double newMonthlyBooking)
	{
		this.monthlyBooking = new BeanMoney(newMonthlyBooking);
	}
	
	public void addBalance(double toAdd)
	{
		this.balance = new BeanMoney(this.balance.AMOUNT() + toAdd);
	}
	
	public void addMonthlyBooking(double toAdd)
	{
		this.monthlyBooking = new BeanMoney(this.monthlyBooking.AMOUNT() + toAdd);
	}

	public String toString()
	{
		return balance.toString();
	}
}

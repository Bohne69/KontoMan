package rawData;

import java.io.Serializable;

public class BeanMoney implements Serializable {

	private double amount;
	
	public BeanMoney()
	{
		this.amount = 0;
	}
	
	public BeanMoney(double amount)
	{
		this.amount = roundToCents(amount);
	}
	
	public double AMOUNT()
	{
		return amount;
	}
	
	public void setAmount(double newAmount)
	{
		this.amount = roundToCents(newAmount);
	}
	
	public void addAmount(double toAdd)
	{
		this.amount = roundToCents(amount + toAdd);
	}
	
	private static double roundToCents(double toRound)
	{
		double result;
		result = toRound*100;
		result = Math.round(result);
		result = result /100;
		return result;
	}
	
	public String toString()
	{
		return amount + "€";
	}
}

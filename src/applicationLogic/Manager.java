package applicationLogic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import data.ProgramSettings;
import dataStorage.DataSerializer;
import dataStorage.Logger;
import dataStorage.PDFGenerator;
import rawData.BeanAccount;
import rawData.BeanDate;
import rawData.BeanMoney;
import rawData.BeanPlan;
import utility.BeanPlanState;

public class Manager implements Serializable {

	private ArrayList<BeanPlan> plans;
	private BeanAccount account;
	private static Manager instance;
	
	public static Manager getInstance()
	{
		if(instance == null)
			instance = new Manager();
		return instance;
	}
	
	public static void setInstance(Manager m)
	{
		instance = m;
		if(m != null)
			m.update();
	}
	
	public Manager()
	{
		setPlans(new ArrayList<BeanPlan>());
		setAccount(new BeanAccount());
	}

	public BeanAccount getAccount() {
		return account;
	}

	public void setAccount(BeanAccount account) {
		this.account = account;
	}

	public ArrayList<BeanPlan> getPlans() {
		sortPlans();
		return plans;
	}

	public void setPlans(ArrayList<BeanPlan> plans) {
		this.plans = plans;
	}
	
	public void addPlan(BeanPlan p)
	{
		plans.add(p);
		Logger.log(p.toString() + " hinzugefügt");
		sortPlans();
		update();
	}
	
	public void updatePlanStates()
	{
		BeanDate current = new BeanDate(true);
		
		for(BeanPlan p : plans)
		{
			if(p.getDate().compare(p.getDate(), current)  == 0)
			{
				if(p.getState().equals(BeanPlanState.PLANNED))
				{
					p.setState(BeanPlanState.SCHEDULED);
				}
			}
			else if(p.getDate().compare(p.getDate(), current) < 0)
			{
				switch(p.getState())
				{
				case SCHEDULED:
					p.setState(BeanPlanState.LATE);
					break;
				case SHIPPING:
					p.setState(BeanPlanState.LATE_SHIPPING);
					break;
				case PLANNED:
					p.setState(BeanPlanState.LATE);
					break;
				default:
					break;
				}
			}
		}
	}

	public void updateMonthlyBooking()
	{
		BeanDate current = new BeanDate(true);
		if(current.compare(current, ProgramSettings.getInstance().getLastDate()) > 0)
		{
			while(current.compare(current, ProgramSettings.getInstance().getLastDate()) > 0)
			{
				account.addBalance(account.MONTHLY_BOOKING().AMOUNT());
				Logger.log("Monatliche Buchung von " + account.MONTHLY_BOOKING() + " für " + ProgramSettings.getInstance().getLastDate().toSimpleString());
				ProgramSettings.getInstance().setLastDate(ProgramSettings.getInstance().getLastDate().getNextMonth());
			}
		}	
	}
	
	public void update()
	{
		updateMonthlyBooking();
		updatePlanStates();
	}
	
	public void sortPlans()
	{
		Collections.sort(plans);
	}
	
	public void deletePlan(BeanPlan p)
	{
		Logger.log(p.toString() + " entfernt");
		plans.remove(p);
	}
	
	public ArrayList<BeanPlan> getPlansInMonth(BeanDate date)
	{
		ArrayList<BeanPlan> res = new ArrayList<BeanPlan>();
		
		for(BeanPlan p : plans)
		{
			if(p.getDate().compareNoParts(p.getDate(), date) == 0)
			{
				res.add(p);
			}
		}
		
		return res;
	}
	
	public BeanMoney getThisMonthsBalance()
	{
		BeanMoney res = new BeanMoney(0);
		res.addAmount(account.BALANCE().AMOUNT());
		
		ArrayList<BeanPlan> tmp = getPlansInMonth(new BeanDate(true));
		
		if(!tmp.isEmpty())
		{
			return res;
		}
		
		for(BeanPlan p : tmp)
		{
			res.addAmount(-(p.getAmount().AMOUNT()));
		}
		
		return res;
	}
	
	public BeanMoney getBalanceInMonths(int monthsFromNow)
	{
		if(monthsFromNow == 0)
			return account.BALANCE();
		
		BeanMoney tmpBalance = new BeanMoney(0);	
		tmpBalance.addAmount(account.BALANCE().AMOUNT());
		tmpBalance.addAmount(-(account.MONTHLY_BOOKING().AMOUNT()));
		BeanDate tmpDate = new BeanDate(true);	
		
		for(int i = 0; i <= monthsFromNow; i++)
		{
			tmpBalance.addAmount(account.MONTHLY_BOOKING().AMOUNT());
			
			for(BeanPlan p : getPlansInMonth(tmpDate))
			{
				tmpBalance.addAmount(-(p.getAmount().AMOUNT()));
			}
			
			tmpDate = tmpDate.getNextMonth();
		}
		
		return tmpBalance;
	}
	
	public List<Integer> getFutureMonths()
	{
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		BeanDate tempDate = new BeanDate(true);

		for(int i = 0; i < 12; i++)
		{
			result.add(tempDate.MONTH().ordinal());

			tempDate = tempDate.getNextMonth();
		}
		
		return result;
	}
	
	public List<Double> getFutureScores()
	{
		ArrayList<Double> result = new ArrayList<Double>();
		
		ArrayList<BeanPlan> alreadyCounted = new ArrayList<BeanPlan>();
		
		double tempBalance = 0;
		BeanDate tempDate = new BeanDate(true);
		
		tempBalance += account.BALANCE().AMOUNT();
		tempBalance -= account.MONTHLY_BOOKING().AMOUNT();
	
		for(BeanPlan p : plans)
		{
			if(p.getDate().compare(p.getDate(), new BeanDate(true)) < 0)
			{
				if(p.shouldCalculate())
				{
					tempBalance -= p.getAmount().AMOUNT();
					alreadyCounted.add(p);
				}
			}
		}
		
		for(int i = 0; i < 12; i++)
		{
			tempBalance += account.MONTHLY_BOOKING().AMOUNT();
			
			for(BeanPlan p : getPlansInMonth(tempDate))
			{
				if(!alreadyCounted.contains(p))
				{
					if(p.shouldCalculate())
					{
						tempBalance -= p.getAmount().AMOUNT();
					}
				}
			}
			
			result.add(tempBalance);
			
			tempDate = tempDate.getNextMonth();
		}
		
		return result;
	}
	
	//TODO Month < 0
	
	public void printAllPlans(String fileName) throws IOException
	{
		PDFGenerator.generatePlanList(fileName, plans);
	}
	
	//TODO printReceiptOverNextXMonths
	
	//TODO getDetailedReceiptOverNextMonths
	
	public void load(String path) throws FileNotFoundException
	{
		instance = DataSerializer.loadManager(path);
		sortPlans();
		update();
	}
	
	public void save(String path)
	{
		DataSerializer.saveManager(path, Manager.this);
	}
	
	public void booking(String topic, BeanMoney amount)
	{
		account.addBalance(amount.AMOUNT());
		Logger.log("Buchung durchgeführt: " + topic + " (" + amount.toString() + ")");
	}
	
	public void addToMonthlyBooking(String topic, BeanMoney amount)
	{
		account.addMonthlyBooking(amount.AMOUNT());
		Logger.log("Monatliche Buchung verändert: " + topic + " (" + account.MONTHLY_BOOKING().toString() + ")");
	}
	
	public void finalizePlan(BeanPlan p, BeanMoney finalAmount)
	{
		p.setAmount(finalAmount);
		account.addBalance(-p.getAmount().AMOUNT());
		Logger.log("Plan abgeschlossen: " + p.toString());
		plans.remove(p);
	}
}

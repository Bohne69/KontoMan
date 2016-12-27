package applicationLogic;

import java.io.Serializable;
import java.util.ArrayList;

import rawData.BeanAccount;
import rawData.BeanPlan;

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
		return plans;
	}

	public void setPlans(ArrayList<BeanPlan> plans) {
		this.plans = plans;
	}
	
	//TODO
	
}

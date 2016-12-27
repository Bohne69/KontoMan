package rawData;

import java.io.Serializable;

import utility.BeanPlanState;
import utility.BeanPlanType;

public class BeanPlan implements Serializable {

	private String description;
	private BeanDate date;
	private BeanMoney amount;
	private BeanPlanState state;	
	private BeanPlanType type;
	private BeanPlatform platform;
	
}

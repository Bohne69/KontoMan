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
	private String weblink;
	private String trackingId;
	
	public BeanPlan()
	{
		setDescription("");
		setDate(null);
		setAmount(null);
		setState(null);	
		setType(null);
		setPlatform(null);
		setWeblink("");
		setTrackingId("");
	}
	
	public BeanPlan(String description, BeanDate date, BeanMoney amount, BeanPlanState state, BeanPlanType type, BeanPlatform platform, String weblink, String trackingId)
	{
		this.setDescription(description);
		this.setDate(date);
		this.setAmount(amount);
		this.setState(state);	
		this.setType(type);
		this.setPlatform(platform);
		this.setWeblink(weblink);
		this.setTrackingId(trackingId);
	}

	
	public String getDescription() {
		return description;
	}

	
	public void setDescription(String description) {
		this.description = description;
	}

	public BeanDate getDate() {
		return date;
	}

	public void setDate(BeanDate date) {
		this.date = date;
	}

	public BeanMoney getAmount() {
		return amount;
	}

	public void setAmount(BeanMoney amount) {
		this.amount = amount;
	}

	public BeanPlanState getState() {
		return state;
	}

	public void setState(BeanPlanState state) {
		this.state = state;
	}

	public BeanPlanType getType() {
		return type;
	}

	public void setType(BeanPlanType type) {
		this.type = type;
	}

	public BeanPlatform getPlatform() {
		return platform;
	}

	public void setPlatform(BeanPlatform platform) {
		this.platform = platform;
	}

	public String getWeblink() {
		return weblink;
	}

	public void setWeblink(String weblink) {
		this.weblink = weblink;
	}

	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}
}

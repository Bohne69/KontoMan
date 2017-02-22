package rawData;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

import utility.BeanPlanState;
import utility.BeanPlanType;

public class BeanPlan implements Serializable, Comparable<BeanPlan> {

	private String description;
	private BeanDate date;
	private BeanMoney amount;
	private BeanPlanState state;	
	private BeanPlanType type;
	private BeanPlatform platform;
	private String weblink;
	private String trackingId;
	private boolean calculate = true;
		
	private String imgPath = "res/missing.png";
	private transient BufferedImage thumbnail;
	
	//TODO
	private BeanDate receiveDate;
	
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
		setReceiveDate(null);
	}
	
	public BeanPlan(String description, BeanDate date, BeanMoney amount, BeanPlanState state, BeanPlanType type, BeanPlatform platform, String weblink, String trackingId, BeanDate receiveDate)
	{
		this.setDescription(description);
		this.setDate(date);
		this.setAmount(amount);
		this.setState(state);	
		this.setType(type);
		this.setPlatform(platform);
		this.setWeblink(weblink);
		this.setTrackingId(trackingId);
		this.setReceiveDate(receiveDate);
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

	public String toString()
	{
		return description + " (" + platform + ") " + date + " - " + amount;
	}
	
	public BeanDate getReceiveDate() {
		return receiveDate;
	}
	
	public void setReceiveDate(BeanDate receiveDate) {
		this.receiveDate = receiveDate;
	}

	@Override
	public int compareTo(BeanPlan o) {
		return date.compare(date, o.date);
	}

	public boolean shouldCalculate() {
		return calculate;
	}

	public void setIfShouldCalculate(boolean calculate) {
		this.calculate = calculate;
	}

	
	public String getImgPath() {
		return imgPath;
	}

	
	public void setImgPath(String imgPath) throws IOException {
		this.imgPath = imgPath;
		this.thumbnail = null;
		Runtime.getRuntime().gc();
		getThumbnail();
	}

	public BufferedImage getThumbnail() throws IOException {
		
		if(imgPath.isEmpty()){
			thumbnail = ImageIO.read(new File("res/missing.png"));
		}else{
			if(thumbnail == null)
				try{thumbnail = scale(ImageIO.read(new File(imgPath)), 75);}catch(Exception e){thumbnail = ImageIO.read(new File("res/missing.png"));}
		}
		
		if(thumbnail == null)
			thumbnail = ImageIO.read(new File("res/missing.png"));
		
		return thumbnail;
		
	}

	private static BufferedImage scale (BufferedImage in, int toSize)
	{
		double factor = 0;
		double x = in.getWidth();
		double y = in.getHeight();
		
		if(x <= y)
		{
			factor = toSize / y;
			x*=factor;
			y*=factor;
		}
		else
		{
			factor = toSize / x;
			x*=factor;
			y*=factor;
		}
		
		in = resize(in, (int)x, (int)y);
		
		return in;
	}
	
	private static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}  
}

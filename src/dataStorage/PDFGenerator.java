package dataStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import applicationLogic.Manager;
import rawData.BeanDate;
import rawData.BeanMoney;
import rawData.BeanPlan;
import utility.BeanMonthParts;
import utility.BeanMonths;
import utility.BeanPlanState;
import utility.BeanPlanType;

public class PDFGenerator {
	
	final static float[] columnWidths = {1f, 3f, 1f};
	final static float titleSize = 15f;
	final static float subtitleSize = 10f;
	final static float mainSize = 8f;

	public static void generatePlanList(String filename, List<BeanPlan> list) throws IOException 
	{
		PdfWriter writer = new PdfWriter(filename);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);

		Paragraph title = new Paragraph("Abzufertigende und anstehende Pläne");
		title.setPaddingTop(-25f);
		title.setBold();
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFontSize(titleSize);
		document.add(title);
		
		document.setFontSize(10);
		
		Table maintable = new Table(5);
		maintable.setWidthPercent(100);
		maintable.setFontSize(mainSize);
		
		Paragraph desc = new Paragraph("Beschreibung");
		desc.setBold();
		
		Paragraph platform = new Paragraph("Plattform");
		platform.setBold();
		
		Paragraph date = new Paragraph("Datum");
		date.setBold();
		
		Paragraph state = new Paragraph("Status");
		state.setBold();
		
		Paragraph cost = new Paragraph("Kosten");
		cost.setBold();

		maintable.addCell(desc);
		maintable.addCell(platform);
		maintable.addCell(date);
		maintable.addCell(state);
		maintable.addCell(cost);
		
		for(BeanPlan p : list)
		{
			maintable.addCell(p.getDescription());
			maintable.addCell(p.getPlatform().toString());
			maintable.addCell(p.getDate().toString());				
			maintable.addCell(BeanPlanState.stringify(p.getState()));	
			maintable.addCell(p.getAmount().toString());	
		}
		
		document.add(maintable);
		
		document.close();
		pdf.close();
		writer.close();
	}

	public static void generatePlanDetails(String filename, BeanPlan plan) throws IOException
	{
		PdfWriter writer = new PdfWriter(filename);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);

		Paragraph title = new Paragraph("Plan: " + plan.getDescription());
		title.setPaddingTop(-25f);
		title.setBold();
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFontSize(titleSize);
		document.add(title);
		
		Paragraph dateTitle = new Paragraph("Bearbeitungsdatum:");
		dateTitle.setBold();
		dateTitle.setTextAlignment(TextAlignment.LEFT);
		dateTitle.setFontSize(subtitleSize);
		dateTitle.setPaddingBottom(-10f);
		document.add(dateTitle);
		Paragraph date = new Paragraph(plan.getDate().toString());
		date.setTextAlignment(TextAlignment.LEFT);
		date.setFontSize(mainSize);
		dateTitle.setPaddingBottom(-0f);
		document.add(date);
		
		Paragraph amountTitle = new Paragraph("Kosten:");
		amountTitle.setBold();
		amountTitle.setTextAlignment(TextAlignment.LEFT);
		amountTitle.setFontSize(subtitleSize);
		amountTitle.setPaddingBottom(-10f);
		document.add(amountTitle);
		Paragraph amount = new Paragraph(plan.getAmount().toString());
		amount.setTextAlignment(TextAlignment.LEFT);
		amount.setFontSize(mainSize);
		amountTitle.setPaddingBottom(-0f);
		document.add(amount);
		
		Paragraph stateTitle = new Paragraph("Aktueller Status (" + new BeanDate(true).toString() + "):");
		stateTitle.setBold();
		stateTitle.setTextAlignment(TextAlignment.LEFT);
		stateTitle.setFontSize(subtitleSize);
		stateTitle.setPaddingBottom(-10f);
		document.add(stateTitle);
		Paragraph state = new Paragraph(BeanPlanState.stringify(plan.getState()));
		state.setTextAlignment(TextAlignment.LEFT);
		state.setFontSize(mainSize);
		stateTitle.setPaddingBottom(-0f);
		document.add(state);
		
		Paragraph typeTitle = new Paragraph("Typ:");
		typeTitle.setBold();
		typeTitle.setTextAlignment(TextAlignment.LEFT);
		typeTitle.setFontSize(subtitleSize);
		typeTitle.setPaddingBottom(-10f);
		document.add(typeTitle);
		Paragraph type = new Paragraph(BeanPlanType.stringify(plan.getType()));
		type.setTextAlignment(TextAlignment.LEFT);
		type.setFontSize(mainSize);
		typeTitle.setPaddingBottom(-0f);
		document.add(type);
		
		Paragraph platformTitle = new Paragraph("Plattform:");
		platformTitle.setBold();
		platformTitle.setTextAlignment(TextAlignment.LEFT);
		platformTitle.setFontSize(subtitleSize);
		platformTitle.setPaddingBottom(-10f);
		document.add(platformTitle);
		Paragraph platform = new Paragraph(plan.getPlatform().toString());
		platform.setTextAlignment(TextAlignment.LEFT);
		platform.setFontSize(mainSize);
		platformTitle.setPaddingBottom(-0f);
		document.add(platform);
		
		if(plan.getType().equals(BeanPlanType.DELIVERY))
		{
			Paragraph receiveDateTitle = new Paragraph("Vorraussichtlicher Liefertermin:");
			receiveDateTitle.setBold();
			receiveDateTitle.setTextAlignment(TextAlignment.LEFT);
			receiveDateTitle.setFontSize(subtitleSize);
			receiveDateTitle.setPaddingBottom(-10f);
			document.add(receiveDateTitle);
			Paragraph receiveDate = new Paragraph(plan.getReceiveDate().toString());
			receiveDate.setTextAlignment(TextAlignment.LEFT);
			receiveDate.setFontSize(mainSize);
			receiveDateTitle.setPaddingBottom(-0f);
			document.add(receiveDate);
		}
		
		if(!plan.getWeblink().isEmpty())
		{
			Paragraph weblinkTitle = new Paragraph("Produktseite:");
			weblinkTitle.setBold();
			weblinkTitle.setTextAlignment(TextAlignment.LEFT);
			weblinkTitle.setFontSize(subtitleSize);
			weblinkTitle.setPaddingBottom(-10f);
			document.add(weblinkTitle);
			Paragraph weblink = new Paragraph(plan.getWeblink());
			weblink.setTextAlignment(TextAlignment.LEFT);
			weblink.setFontSize(mainSize);
			weblinkTitle.setPaddingBottom(-0f);
			document.add(weblink);
		}
		
		if(!plan.getTrackingId().isEmpty())
		{
			Paragraph trackingIDTitle = new Paragraph("Tracking ID:");
			trackingIDTitle.setBold();
			trackingIDTitle.setTextAlignment(TextAlignment.LEFT);
			trackingIDTitle.setFontSize(subtitleSize);
			trackingIDTitle.setPaddingBottom(-10f);
			document.add(trackingIDTitle);
			Paragraph trackingID = new Paragraph(plan.getTrackingId());
			trackingID.setTextAlignment(TextAlignment.LEFT);
			trackingID.setFontSize(mainSize);
			trackingIDTitle.setPaddingBottom(-0f);
			document.add(trackingID);
		}
		
		document.close();
		pdf.close();
		writer.close();
	}

	public static void generateFutureBalance(String filename) throws IOException
	{
		PdfWriter writer = new PdfWriter(filename);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);

		Paragraph title = new Paragraph("Voraussichtlicher Kontoverlauf über die nächsten 12 Monate");
		title.setPaddingTop(-25f);
		title.setBold();
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFontSize(titleSize);
		document.add(title);
		
		document.setFontSize(10);
		
		Table maintable = new Table(2);
		maintable.setWidthPercent(100);
		maintable.setFontSize(mainSize);
		
		Paragraph date = new Paragraph("Datum");
		date.setBold();
		
		Paragraph amount = new Paragraph("Kontostand zu Monatsende");
		amount.setBold();

		maintable.addCell(date);
		maintable.addCell(amount);
		
		BeanDate tmp = new BeanDate(true);
		List<BeanDate> months = new ArrayList<BeanDate>();
		for(int i = 0; i < 12; i++)
		{
			months.add(tmp);
			tmp = tmp.getNextMonth();
		}
		
		List<Double> amounts = Manager.getInstance().getFutureScores();
		
		for(int i = 0; i < 12; i++)
		{
			maintable.addCell(months.get(i).toSimpleString());
			maintable.addCell(new BeanMoney(amounts.get(i)).toString());
		}
		
		document.add(maintable);
		
		document.close();
		pdf.close();
		writer.close();
	}

	public static void generateDetailedAccountProgression(String filename) throws IOException
	{
		PdfWriter writer = new PdfWriter(filename);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);

		Paragraph title = new Paragraph("Kontoverlauf über die nächsten 12 Monate");
		title.setPaddingTop(-25f);
		title.setBold();
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFontSize(titleSize);
		document.add(title);
		
		document.setFontSize(8);
		
		BeanMoney currentBalance = new BeanMoney(Manager.getInstance().getAccount().BALANCE().AMOUNT());
		
		BeanDate tmp = new BeanDate(true);
		List<BeanDate> months = new ArrayList<BeanDate>();
		for(int i = 0; i < 12; i++)
		{
			months.add(new BeanDate(tmp.PART(), tmp.MONTH(), tmp.YEAR()));
			tmp = tmp.getNextMonth();
		}
		
		for(int i = 0; i < 12; i++)
		{
			Paragraph pageTitle = new Paragraph(months.get(i).toSimpleString());
			pageTitle.setBold();
			pageTitle.setTextAlignment(TextAlignment.CENTER);
			pageTitle.setFontSize(10f);
			document.add(pageTitle);
			document.setFontSize(8);
			
			
			Table maintable = new Table(3);
			maintable.setWidthPercent(100);
			maintable.setFontSize(mainSize);		
			Paragraph tablelabel1 = new Paragraph("Ereignis");
			tablelabel1.setBold();		
			Paragraph tablelabel2 = new Paragraph("Datum");
			tablelabel2.setBold();		
			Paragraph tablelabel3 = new Paragraph("Resultierender Kontostand");
			tablelabel3.setBold();		
			maintable.addCell(tablelabel1);
			maintable.addCell(tablelabel2);
			maintable.addCell(tablelabel3);
			
			
			maintable.addCell("Kontostand zu Monatsbeginn");
			maintable.addCell(new BeanDate(BeanMonthParts.ANFANG, months.get(i).MONTH(), months.get(i).YEAR()).toString());
			maintable.addCell(currentBalance.toString());
			
			
			if(i > 0)
			{
				maintable.addCell("Kontostand nach monatlicher Buchung");
				maintable.addCell(new BeanDate(BeanMonthParts.ANFANG, months.get(i).MONTH(), months.get(i).YEAR()).toString());
				currentBalance.addAmount(Manager.getInstance().getAccount().MONTHLY_BOOKING().AMOUNT());
				maintable.addCell(currentBalance.toString());
			}
	
			
			if(Manager.getInstance().getPlansInMonth(months.get(i)).size() > 1)
			{
				maintable.addCell(" ");
				maintable.addCell(" ");
				maintable.addCell(" ");
			}
			else if(Manager.getInstance().getPlansInMonth(months.get(i)).size() == 1 && Manager.getInstance().getPlans().get(0).shouldCalculate())
			{
				maintable.addCell(" ");
				maintable.addCell(" ");
				maintable.addCell(" ");
			}
			

			for(BeanPlan p : Manager.getInstance().getPlansInMonth(months.get(i)))
			{
				if(p.shouldCalculate())
				{
					maintable.addCell(p.getDescription() + " (" + p.getPlatform().NAME() + ", " + p.getAmount().toString() + ")");
					maintable.addCell(p.getDate().toString());
					currentBalance.addAmount(-p.getAmount().AMOUNT());
					maintable.addCell(currentBalance.toString());
				}
			}
			
			
			maintable.addCell(" ");
			maintable.addCell(" ");
			maintable.addCell(" ");
			
			
			maintable.addCell("Kontostand zu Monatsende");
			maintable.addCell(new BeanDate(BeanMonthParts.ENDE, months.get(i).MONTH(), months.get(i).YEAR()).toString());
			maintable.addCell(currentBalance.toString());
			
			
			document.add(maintable);
			
			if(i != 11)
				document.add(new AreaBreak());		
		}
		
		document.close();
		pdf.close();
		writer.close();
	}
}

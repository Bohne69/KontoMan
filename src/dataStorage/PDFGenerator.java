package dataStorage;

import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import rawData.BeanPlan;

public class PDFGenerator {
	
	final static float[] columnWidths = {1f, 3f, 1f};
	final static float footHeight = 100f;
	final static float titleSize = 15f;
	final static float subtitleSize = 10f;
	final static float mainSize = 8f;

	public static void generatePlanList(String filename, ArrayList<BeanPlan> plans) throws IOException 
	{
		PdfWriter writer = new PdfWriter(filename);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);

//		Paragraph subtitle = new Paragraph();
//		
//		subtitle.add("Pläne in chronologischer Reihenfolge:");
//		
//		subtitle.setTextAlignment(TextAlignment.CENTER);
//		subtitle.setFontSize(subtitleSize);
//		subtitle.setItalic();

		Paragraph title = new Paragraph("Abzufertigende und anstehende Pläne");
		title.setBold();
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFontSize(titleSize);
		document.add(title);
		
//		document.add(subtitle);
		
		document.setFontSize(10);
		
		//TODO
		
		Table maintable = new Table(5);
		maintable.setWidthPercent(100);
		maintable.setFontSize(mainSize);
		
		Paragraph desc = new Paragraph("Beschreibung");
		desc.setBold();
		
		Paragraph date = new Paragraph("Datum");
		date.setBold();
		
		Paragraph cost = new Paragraph("Kosten");
		cost.setBold();
		
		Paragraph state = new Paragraph("Status");
		state.setBold();
		
		Paragraph platform = new Paragraph("Plattform");
		platform.setBold();
		
		maintable.addCell(desc);
		maintable.addCell(date);
		maintable.addCell(platform);
		maintable.addCell(state);
		maintable.addCell(cost);
		
		for(BeanPlan p : plans)
		{
			maintable.addCell(p.getDescription());
			maintable.addCell(p.getDate().toString());			
			maintable.addCell(p.getPlatform().toString());	
			maintable.addCell(p.getState().toString());	
			maintable.addCell(p.getAmount().toString());	
		}
		
		document.add(maintable);
		
		document.close();
		pdf.close();
		writer.close();
	}
}

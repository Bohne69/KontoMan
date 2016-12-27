package userInterface;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import data.Platforms;
import rawData.BeanDate;
import rawData.BeanMoney;
import rawData.BeanPlan;
import rawData.BeanPlatform;
import userInterfaceUtility.BeanPlanListCellRenderer;
import utility.BeanPlanState;

public class TESTGUI extends JFrame {

	public TESTGUI()
	{
		super("test");
		
		setup();
		
		setPreferredSize(new Dimension(400,400));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void setup()
	{
		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		
		BeanPlan[] data = new BeanPlan[]{new BeanPlan("NEKOPARA OVA Crowdfunding support (150 Tier)", new BeanDate(true), new BeanMoney(100.01204), BeanPlanState.SCHEDULED,null,null,"",""),
				new BeanPlan("Onahole", new BeanDate(true), new BeanMoney(42.12204), BeanPlanState.LATE,null,null,"",""),
				new BeanPlan("WRsdf", new BeanDate(true), new BeanMoney(5124.12212), BeanPlanState.SHIPPING,null,null,"","")
		};
		
		JList list = new JList(data);
		list.setCellRenderer(new BeanPlanListCellRenderer());
		JScrollPane scroll = new JScrollPane(list);
		add(scroll);		
	}
}

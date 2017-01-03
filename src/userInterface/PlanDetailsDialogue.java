package userInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.time.LocalDate;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import applicationLogic.Manager;
import data.Platforms;
import rawData.BeanDate;
import rawData.BeanMoney;
import rawData.BeanPlan;
import rawData.BeanPlatform;
import utility.BeanMonthParts;
import utility.BeanMonths;
import utility.BeanPlanState;
import utility.BeanPlanType;

public class PlanDetailsDialogue extends JFrame {

	private GUI caller;
	private JComboBox platformIn;
	private BeanPlan plan;
	
	public static void open(GUI caller, BeanPlan plan)
	{
		new PlanDetailsDialogue(caller, plan);
	}
	
	private PlanDetailsDialogue(GUI caller, BeanPlan plan)
	{
			super("Plan bearbeiten");
	
			this.plan = plan;
			this.caller = caller;
			this.caller.setEnabled(false);
			
			ImageIcon img = new ImageIcon("res/icon.png");
			setIconImage(img.getImage());
			
			createGUI();
			
			//setPreferredSize(new Dimension(1400,800));	
			pack();
			setLocationRelativeTo(null);
			setVisible(true);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			
			addWindowListener(new WindowAdapter() {
				  public void windowClosing(WindowEvent e) {
					  caller.setEnabled(true);
					  dispose();
				}});

	}
	
	private void createGUI()
	{
		setLayout(new BorderLayout());
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
		JPanel bottom = new JPanel();
		
		JPanel descriptionPanel = new JPanel(new BorderLayout());
		JLabel descriptionLabel = new JLabel("Beschreibung: ");
		JTextField descriptionIn = new JTextField(plan.getDescription(),30);
		descriptionIn.setEditable(false);
		descriptionPanel.add(descriptionLabel, BorderLayout.WEST);
		descriptionPanel.add(descriptionIn, BorderLayout.EAST);
		main.add(descriptionPanel);

		JPanel datePanel = new JPanel(new BorderLayout());
		JLabel dateLabel = new JLabel("Bearbeitungsdatum: ");
		JTextField dateIn = new JTextField(plan.getDate().toString(),30);	
		dateIn.setEditable(false);
		datePanel.add(dateLabel, BorderLayout.WEST);
		datePanel.add(dateIn, BorderLayout.EAST);
		main.add(datePanel);
		
		JPanel receiveDatePanel = new JPanel(new BorderLayout());
		JLabel receiveDateLabel = new JLabel("Vor. Fertigstellungsdatum: ");
		JTextField receiveDateIn = new JTextField(plan.getReceiveDate().toString(),30);	
		receiveDateIn.setEditable(false);
		receiveDatePanel.add(receiveDateLabel, BorderLayout.WEST);
		receiveDatePanel.add(receiveDateIn, BorderLayout.EAST);
		main.add(receiveDatePanel);
				
		JPanel amountPanel = new JPanel(new BorderLayout());
		JLabel amountLabel = new JLabel("Kosten: ");
		JTextField amountIn = new JTextField(plan.getAmount().toString(),30);	
		amountIn.setEditable(false);
		amountPanel.add(amountLabel, BorderLayout.WEST);
		amountPanel.add(amountIn, BorderLayout.EAST);
		main.add(amountPanel);
		
		JPanel platformPanel = new JPanel(new BorderLayout());
		JLabel platformLabel = new JLabel("Plattform: ");
		JTextField platformIn = new JTextField(plan.getPlatform().toString(),30);	
		platformIn.setEditable(false);
		platformPanel.add(platformLabel, BorderLayout.WEST);
		platformPanel.add(platformIn, BorderLayout.EAST);
		main.add(platformPanel);
		
		JPanel typePanel = new JPanel(new BorderLayout());
		JLabel typeLabel = new JLabel("Typ: ");
		JTextField typeIn = new JTextField(BeanPlanType.stringify(plan.getType()),30);	
		typeIn.setEditable(false);
		typePanel.add(typeLabel, BorderLayout.WEST);
		typePanel.add(typeIn, BorderLayout.EAST);
		main.add(typePanel);
		
		JPanel statePanel = new JPanel(new BorderLayout());
		JLabel stateLabel = new JLabel("Status: ");
		JTextField stateIn = new JTextField(BeanPlanState.stringify(plan.getState()),30);	
		stateIn.setEditable(false);
		statePanel.add(stateLabel, BorderLayout.WEST);
		statePanel.add(stateIn, BorderLayout.EAST);
		main.add(statePanel);
		
		JPanel weblinkPanel = new JPanel(new BorderLayout());
		JLabel weblinkLabel = new JLabel("Produktseite: ");
		JTextField weblinkIn = new JTextField(plan.getWeblink(), 30);		
		weblinkIn.setEditable(false);
		weblinkPanel.add(weblinkLabel, BorderLayout.WEST);
		weblinkPanel.add(weblinkIn, BorderLayout.EAST);
		main.add(weblinkPanel);
		
		JPanel trackingIDPanel = new JPanel(new BorderLayout());
		JLabel trackingIDLabel = new JLabel("Tracking ID: ");
		JTextField trackingIDIn = new JTextField(plan.getTrackingId(), 30);	
		trackingIDIn.setEditable(false);
		trackingIDPanel.add(trackingIDLabel, BorderLayout.WEST);
		trackingIDPanel.add(trackingIDIn, BorderLayout.EAST);
		main.add(trackingIDPanel);
		
		JButton cancel = new JButton("Schließen");
		bottom.add(cancel);
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				caller.setEnabled(true);
				dispose();
			}
		});
		
		add(main, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);
	}
	
	private void updatePlatformBox()
	{
		platformIn.setModel(new DefaultComboBoxModel(Platforms.getInstance().PLATFORMS().toArray()));
		
		revalidate();
		repaint();
	}
}

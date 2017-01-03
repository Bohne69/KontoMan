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

public class AddPlanDialogue extends JFrame {

	private GUI caller;
	private JComboBox platformIn;
	
	public static void open(GUI caller)
	{
		new AddPlanDialogue(caller);
	}
	
	private AddPlanDialogue(GUI caller)
	{
			super("Plan hinzufügen");
			
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
		JTextField descriptionIn = new JTextField(30);
		descriptionPanel.add(descriptionLabel, BorderLayout.WEST);
		descriptionPanel.add(descriptionIn, BorderLayout.EAST);
		main.add(descriptionPanel);

		JPanel datePanel = new JPanel(new BorderLayout());
		JLabel dateLabel = new JLabel("Bearbeitungsdatum: ");
		JPanel dateSubPanel = new JPanel(new GridLayout(1,3));
		JComboBox datePartIn = new JComboBox(BeanMonthParts.toArray());
		datePartIn.setPreferredSize(new Dimension((int)(descriptionIn.getPreferredSize().getWidth()/3), (int)descriptionIn.getPreferredSize().getHeight()));
		dateSubPanel.add(datePartIn);
		JComboBox dateMonthIn = new JComboBox(BeanMonths.toArray());
		dateSubPanel.add(dateMonthIn);
		JSpinner dateYearIn = new JSpinner(new SpinnerNumberModel(LocalDate.now().getYear(), 2000, 9999, 1));
		dateSubPanel.add(dateYearIn);
		datePanel.add(dateLabel, BorderLayout.WEST);
		datePanel.add(dateSubPanel, BorderLayout.EAST);
		main.add(datePanel);
		
		JPanel receiveDatePanel = new JPanel(new BorderLayout());
		JLabel receiveDateLabel = new JLabel("Vor. Fertigstellungsdatum: ");
		JPanel receiveDateSubPanel = new JPanel(new GridLayout(1,3));
		JComboBox receiveDatePartIn = new JComboBox(BeanMonthParts.toArray());
		receiveDatePartIn.setPreferredSize(new Dimension((int)(descriptionIn.getPreferredSize().getWidth()/3), (int)descriptionIn.getPreferredSize().getHeight()));
		receiveDateSubPanel.add(receiveDatePartIn);
		JComboBox receiveDateMonthIn = new JComboBox(BeanMonths.toArray());
		receiveDateSubPanel.add(receiveDateMonthIn);
		JSpinner receiveDateYearIn = new JSpinner(new SpinnerNumberModel(LocalDate.now().getYear(), 2000, 9999, 1));
		receiveDateSubPanel.add(receiveDateYearIn);
		receiveDatePanel.add(receiveDateLabel, BorderLayout.WEST);
		receiveDatePanel.add(receiveDateSubPanel, BorderLayout.EAST);
		main.add(receiveDatePanel);
		
		JPanel amountPanel = new JPanel(new BorderLayout());
		JLabel amountLabel = new JLabel("Kosten: ");
		JSpinner amountIn = new JSpinner(new SpinnerNumberModel(0.0,0.0,9999999.0,0.01));
		amountIn.setPreferredSize(descriptionIn.getPreferredSize());
		amountPanel.add(amountLabel, BorderLayout.WEST);
		amountPanel.add(amountIn, BorderLayout.EAST);
		main.add(amountPanel);
		
		JPanel platformPanel = new JPanel(new BorderLayout());
		JLabel platformLabel = new JLabel("Plattform: ");
		JPanel platformSubPanel = new JPanel(new GridLayout(1,2));
		platformIn = new JComboBox(Platforms.getInstance().PLATFORMS().toArray());
		platformIn.setPreferredSize(new Dimension((int)(descriptionIn.getPreferredSize().getWidth()/2), (int)descriptionIn.getPreferredSize().getHeight()));
		platformSubPanel.add(platformIn);
		JButton platformEdit = new JButton("Anlegen...");
		platformEdit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){			
				JTextField nameIn = new JTextField();
				JTextField webIn = new JTextField();
				Object[] inputs = {"Name:", nameIn, "Website:", webIn};
				
				int option = JOptionPane.showConfirmDialog(AddPlanDialogue.this, inputs, "Plattform hinzufügen", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION)
				{
					if(nameIn.getText().isEmpty() || webIn.getText().isEmpty())
					{
						JOptionPane.showMessageDialog(getContentPane(), "Beide Felder müssen ausgefüllt werden!", "Erstellungsfehler", JOptionPane.ERROR_MESSAGE);
						return;
					}
					else
					{
						Platforms.getInstance().addPlatform(new BeanPlatform(nameIn.getText(), webIn.getText()));
						updatePlatformBox();
						return;
					}
				}
			}
		});
		platformSubPanel.add(platformEdit);
		platformPanel.add(platformLabel, BorderLayout.WEST);
		platformPanel.add(platformSubPanel, BorderLayout.EAST);
		main.add(platformPanel);
		
		JPanel typePanel = new JPanel(new BorderLayout());
		JLabel typeLabel = new JLabel("Typ: ");
		JComboBox typeIn = new JComboBox(BeanPlanType.toArray());
		typeIn.setPreferredSize(descriptionIn.getPreferredSize());
		typePanel.add(typeLabel, BorderLayout.WEST);
		typePanel.add(typeIn, BorderLayout.EAST);
		main.add(typePanel);
		
		JPanel statePanel = new JPanel(new BorderLayout());
		JLabel stateLabel = new JLabel("Status: ");
		JComboBox stateIn = new JComboBox(BeanPlanState.toArray());
		stateIn.setPreferredSize(descriptionIn.getPreferredSize());
		statePanel.add(stateLabel, BorderLayout.WEST);
		statePanel.add(stateIn, BorderLayout.EAST);
		main.add(statePanel);
		
		JPanel weblinkPanel = new JPanel(new BorderLayout());
		JLabel weblinkLabel = new JLabel("Produktseite: ");
		JTextField weblinkIn = new JTextField(30);
		weblinkPanel.add(weblinkLabel, BorderLayout.WEST);
		weblinkPanel.add(weblinkIn, BorderLayout.EAST);
		main.add(weblinkPanel);
		
		JPanel trackingIDPanel = new JPanel(new BorderLayout());
		JLabel trackingIDLabel = new JLabel("Tracking ID: ");
		JTextField trackingIDIn = new JTextField(30);
		trackingIDPanel.add(trackingIDLabel, BorderLayout.WEST);
		trackingIDPanel.add(trackingIDIn, BorderLayout.EAST);
		main.add(trackingIDPanel);
		
		JButton add = new JButton("Hinzufügen");
		bottom.add(add);
		add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				if(descriptionIn.getText().isEmpty())
				{
					JOptionPane.showMessageDialog(getContentPane(), "Es wurde keine Beschreibung eingegeben!", "Erstellungsfehler", JOptionPane.ERROR_MESSAGE);
					return;
				}
				else if((double)amountIn.getValue() == 0)
				{
					JOptionPane.showMessageDialog(getContentPane(), "Es kann kein Plan von 0€ angegeben werden!", "Erstellungsfehler", JOptionPane.ERROR_MESSAGE);
					return;
				}
				else if(platformIn.getSelectedItem() == null)
				{
					JOptionPane.showMessageDialog(getContentPane(), "Es muss eine Plattform ausgewählt werden!", "Erstellungsfehler", JOptionPane.ERROR_MESSAGE);
					return;
				}
				else
				{
					Manager.getInstance().addPlan(new BeanPlan(
							descriptionIn.getText(),
							new BeanDate(BeanMonthParts.fromString(datePartIn.getSelectedItem().toString()),BeanMonths.fromInt(dateMonthIn.getSelectedIndex()+1),(int)dateYearIn.getValue()),
							new BeanMoney((double)amountIn.getValue()),
							BeanPlanState.fromString(stateIn.getSelectedItem().toString()),
							BeanPlanType.fromString(typeIn.getSelectedItem().toString()),
							(BeanPlatform)platformIn.getSelectedItem(),
							weblinkIn.getText(),
							trackingIDIn.getText(),
							new BeanDate(BeanMonthParts.fromString(receiveDatePartIn.getSelectedItem().toString()),BeanMonths.fromInt(receiveDateMonthIn.getSelectedIndex()+1),(int)receiveDateYearIn.getValue())
					));
					caller.update();
					caller.setEnabled(true);
					dispose();
				}
			}
		});
		
		JButton cancel = new JButton("Abbrechen");
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

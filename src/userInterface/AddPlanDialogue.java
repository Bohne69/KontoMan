package userInterface;

import java.awt.BorderLayout;
import java.awt.Color;
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

import data.Platforms;
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
		JLabel dateLabel = new JLabel("Datum: ");
		JPanel dateSubPanel = new JPanel(new GridLayout(1,3));
		JComboBox datePartIn = new JComboBox(BeanMonthParts.toArray());
		dateSubPanel.add(datePartIn);
		JComboBox dateMonthIn = new JComboBox(BeanMonths.toArray());
		dateSubPanel.add(dateMonthIn);
		JSpinner dateYearIn = new JSpinner(new SpinnerNumberModel(LocalDate.now().getYear(), 2000, 9999, 1));
		dateSubPanel.add(dateYearIn);
		datePanel.add(dateLabel, BorderLayout.WEST);
		datePanel.add(dateSubPanel, BorderLayout.EAST);
		main.add(datePanel);
		
		JPanel amountPanel = new JPanel(new BorderLayout());
		JLabel amountLabel = new JLabel("Kosten: ");
		JSpinner amountIn = new JSpinner(new SpinnerNumberModel(0.0,0.0,9999999.0,0.01));
		amountPanel.add(amountLabel, BorderLayout.WEST);
		amountPanel.add(amountIn, BorderLayout.EAST);
		main.add(amountPanel);
		
		JPanel platformPanel = new JPanel(new BorderLayout());
		JLabel platformLabel = new JLabel("Plattform: ");
		JPanel platformSubPanel = new JPanel(new GridLayout(1,2));
		platformIn = new JComboBox(Platforms.getInstance().PLATFORMS().toArray());
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
						//TODO
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
		typePanel.add(typeLabel, BorderLayout.WEST);
		typePanel.add(typeIn, BorderLayout.EAST);
		main.add(typePanel);
		
		JPanel statePanel = new JPanel(new BorderLayout());
		JLabel stateLabel = new JLabel("Status: ");
		JComboBox stateIn = new JComboBox(BeanPlanState.toArray());
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
		
		JButton close = new JButton("Hinzufügen");
		bottom.add(close);
		close.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				//TODO ADD PLAN
				caller.setEnabled(true);
				dispose();
			}
		});
		
		JButton edit = new JButton("Abbrechen");
		bottom.add(edit);
		edit.addActionListener(new ActionListener(){
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

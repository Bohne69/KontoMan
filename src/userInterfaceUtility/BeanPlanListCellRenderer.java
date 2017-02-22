package userInterfaceUtility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import rawData.BeanPlan;
import userInterface.GuiSettings;

@SuppressWarnings("all")
public class BeanPlanListCellRenderer extends JPanel implements ListCellRenderer {

	private JLabel desc;
	private JLabel platform;
	private JLabel date;
//	private JLabel state;
	private JLabel amount;
	
	public BeanPlanListCellRenderer()
	{
		setOpaque(true);
		
		desc = new JLabel();
		platform = new JLabel();
		date = new JLabel();
//		state = new JLabel();
		amount = new JLabel();

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.WEST;
		c.weightx = 0.5;
		c.gridx = 0;
		
		c.gridy = 0;
//		desc.setPreferredSize(new Dimension(225,15));
		add(desc, c);
		
		c.gridy = 1;
//		platform.setPreferredSize(new Dimension(100,15));
		add(amount, c);
		
		c.gridy = 2;
//		date.setPreferredSize(new Dimension(150,15));
		add(platform, c);		
		
		c.gridy = 3;
//		state.setPreferredSize(new Dimension(150,15));
		add(date, c);
						
//		c.gridy = 4;
//		amount.setPreferredSize(new Dimension(75,15));
//		add(state, c);
		
		
		setBorder(BorderFactory.createSoftBevelBorder(0));
	}
	
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		BeanPlan entry = (BeanPlan) value;
		
		desc.setText(entry.getDescription());
		platform.setText(entry.getPlatform().NAME());
		date.setText(entry.getDate().toString() + " (" + entry.getState().stringify(entry.getState()) + ")");
		//state.setText(entry.getState().stringify(entry.getState()));
		amount.setText(entry.getAmount().toString());
				
		if(isSelected)
		{
			if(entry.shouldCalculate())
			{
				setBackground(GuiSettings.HIGHLIGHT_COLOR);
					setForeground(Color.white);
			}
			else
			{
				setBackground(GuiSettings.WARNING_COLOR);
					setForeground(Color.white);
			}
		}
		else
		{
			if(entry.shouldCalculate())
			{
				setBackground(GuiSettings.BACKGROUND_COLOR);
			      setForeground(Color.black);
			}
			else
			{
				setBackground(GuiSettings.ERROR_COLOR);
			      setForeground(Color.white);
			}
		}
		
		return this;
	}
}

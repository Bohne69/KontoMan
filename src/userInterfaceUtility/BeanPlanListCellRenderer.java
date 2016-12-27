package userInterfaceUtility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import rawData.BeanPlan;
import userInterface.GuiSettings;

@SuppressWarnings("all")
public class BeanPlanListCellRenderer extends JPanel implements ListCellRenderer {

	private static Color HIGHLIGHT_COLOR = GuiSettings.getInstance().THEME_COLOR;
	
	private JLabel desc;
	private JLabel date;
	private JLabel state;
	private JLabel amount;
	
	public BeanPlanListCellRenderer()
	{
		HIGHLIGHT_COLOR = GuiSettings.getInstance().THEME_COLOR;
		
//		setLayout(new GridLayout(1,4));
		setOpaque(true);
		
		desc = new JLabel();
		date = new JLabel();
		state = new JLabel();
		amount = new JLabel();
//		
//		desc.setHorizontalAlignment(SwingConstants.LEFT);
//		state.setHorizontalAlignment(SwingConstants.LEFT);	
//		amount.setHorizontalAlignment(SwingConstants.RIGHT);
//		
//		add(desc, BorderLayout.WEST);
//		add(state, BorderLayout.CENTER);	
//		add(amount, BorderLayout.EAST);
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.WEST;
		c.weightx = 0.5;
		c.gridy = 0;
		
		c.gridx = 0;
		desc.setPreferredSize(new Dimension(225,15));
		add(desc, c);
		
		c.gridx = 1;
		date.setPreferredSize(new Dimension(150,15));
		add(date, c);
		
		c.gridx = 2;
		state.setPreferredSize(new Dimension(150,15));
		add(state, c);
				
		c.gridx = 3;
		amount.setPreferredSize(new Dimension(75,15));
		add(amount, c);
	}
	
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		BeanPlan entry = (BeanPlan) value;
		
		desc.setText(entry.getDescription());
		date.setText(entry.getDate().toString());
		state.setText(entry.getState().stringify(entry.getState()));
		amount.setText(entry.getAmount().toString());
				
		if(isSelected)
		{
			setBackground(HIGHLIGHT_COLOR);
		      setForeground(Color.white);
		}
		else
		{
			setBackground(Color.white);
		      setForeground(Color.black);
		}
		
		return this;
	}
}

package userInterfaceUtility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
	private JLabel amount;
	
	private JLabel img;
	
	public BeanPlanListCellRenderer()
	{
		setOpaque(true);
		
		setLayout(new BorderLayout());
				
		JPanel main = new JPanel(new BorderLayout());
		add(main, BorderLayout.CENTER);
		
		main.setOpaque(false);
		
		desc = new JLabel();
		platform = new JLabel();
		date = new JLabel();
		amount = new JLabel();
		img = new JLabel(new ImageIcon());
		
		main.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.WEST;
		c.weightx = 0.5;
		c.gridx = 0;
		
		c.gridy = 0;
		main.add(desc, c);
		
		c.gridy = 1;
		main.add(amount, c);
		
		c.gridy = 2;
		main.add(platform, c);		
		
		c.gridy = 3;
		main.add(date, c);

		img.setMinimumSize(new Dimension(75,75));
		img.setMaximumSize(new Dimension(75,75));
		add(img, BorderLayout.WEST);
		
		setBorder(BorderFactory.createSoftBevelBorder(0));
	}
	
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		BeanPlan entry = (BeanPlan) value;
		
		if(!entry.getImgPath().isEmpty()){
			try { img.setIcon(new ImageIcon(entry.getThumbnail())); } catch (IOException e) { e.printStackTrace(); }
		}
		
		desc.setText(entry.getDescription());
		platform.setText(entry.getPlatform().NAME());
		date.setText(entry.getDate().toString() + " (" + entry.getState().stringify(entry.getState()) + ")");
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

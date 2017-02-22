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
import rawData.MonthBalanceData;
import userInterface.GuiSettings;

@SuppressWarnings("all")
public class MonthBalanceDataListCellRenderer extends JPanel implements ListCellRenderer {

	private JLabel date;
	private JLabel monthStart;
	private JLabel monthMinimum;
	private JLabel monthEnd;
	
	public MonthBalanceDataListCellRenderer()
	{
		setOpaque(true);
		
		date = new JLabel();
		monthStart = new JLabel();
		monthMinimum = new JLabel();
		monthEnd = new JLabel();

		setLayout(new GridLayout(2,4));

		add(date);
		add(new JLabel("Monatsanfang"));
		add(new JLabel("Monatsminimum"));
		add(new JLabel("Monatsende"));
		add(new JLabel(""));
		add(monthStart);
		add(monthMinimum);
		add(monthEnd);
		
		setBorder(BorderFactory.createSoftBevelBorder(0));
	}
	
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		MonthBalanceData entry = (MonthBalanceData) value;
		
		date.setText(entry.getDate());
		monthStart.setText(entry.getMonthStart().toString());
		monthMinimum.setText(entry.getMonthMinimum().toString());
		monthEnd.setText(entry.getMonthEnd().toString());
				

		if(entry.getMonthMinimum().AMOUNT() <= 0)
		{
			setBackground(GuiSettings.ERROR_COLOR);
			setForeground(Color.black);
		}
		else if(entry.getMonthMinimum().AMOUNT() <= GuiSettings.getInstance().getWarningThreshold())
		{
			setBackground(GuiSettings.WARNING_COLOR);
			setForeground(Color.black);
		}
		else
		{
			setBackground(GuiSettings.BACKGROUND_COLOR);
				setForeground(Color.black);
		}

		
		return this;
	}
}

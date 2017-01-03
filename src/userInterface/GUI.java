package userInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileNameExtensionFilter;

import applicationLogic.Manager;
import dataStorage.DataSerializer;
import userInterfaceUtility.BeanPlanListCellRenderer;

public class GUI extends JFrame {

	private JList plans;
	
	public GUI()
	{
		super("Bohnes Kontomanager V4");

		UIManager.put("nimbusBase", GuiSettings.THEME_COLOR);
		UIManager.put("nimbusBlueGrey", GuiSettings.THEME_COLOR);
		UIManager.put("control", GuiSettings.THEME_COLOR);
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		    if ("Nimbus".equals(info.getName())) {
		    	try {
					UIManager.setLookAndFeel(info.getClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e1) {
					JOptionPane.showMessageDialog(getContentPane(),"Beim laden des GUI Themes ist ein Fehler aufgetreten:\n" + e1.getMessage(),"GUI Theme Fehler",JOptionPane.ERROR_MESSAGE);
				}
		    }
		}
		
		try {
		    setIconImage(ImageIO.read(new File("res/icon.png")));
		}
		catch (IOException exc) {
		    exc.printStackTrace();
		}	
		
		if(!GuiSettings.getInstance().getCurrentSaveFile().isEmpty())
		{
			try {
				Manager.setInstance(DataSerializer.loadManager(GuiSettings.getInstance().getCurrentSaveFile()));
			} catch (FileNotFoundException e1) {
				GuiSettings.getInstance().setCurrentSaveFile("");
			}
		}
		
		createGUI();
		
		addWindowListener(new WindowAdapter() {
			  public void windowClosing(WindowEvent e) {

				  Object[] options = {"Abbrechen", "Beenden", "Speichern & Beenden"};
				  int option = JOptionPane.showOptionDialog(GUI.this, "Möchten sie die Verwaltung vorher speichern?", "Beenden", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
				  if(option == 0)
				  {
					  return;
				  }
				  else if(option == 1)
				  {
					  System.exit(0);
				  }
				  else
				  {
					  if(save())
					  {
						  System.exit(0);
					  }
				  }
				  
			  }});
			
		setPreferredSize(new Dimension(1520,890));
		
		pack();
		setLocationRelativeTo(null);
		
//		loadDisplayState();
//		updateDisplay();
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}
	
	private void createGUI()
	{
		setJMenuBar(createMenuBar());

		setLayout(new GridLayout(1,2));
		
		JPanel left = new JPanel(new BorderLayout());
		JPanel right = new JPanel(new BorderLayout());
		
		add(left);
		add(right);
		
		plans = new JList();
		plans.setCellRenderer(new BeanPlanListCellRenderer());
		JScrollPane planScroller = new JScrollPane(plans);
		left.add(planScroller, BorderLayout.CENTER);		
		createPopup();
		setPlanData();
		
		//TODO
	}
	
	private JMenuBar createMenuBar()
	{
		JMenuBar menu = new JMenuBar();
		
		JMenu file = new JMenu("Datei");
			JMenuItem newProject = new JMenuItem("Neues Projekt anlegen");
			newProject.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					int option = JOptionPane.showConfirmDialog(getContentPane(), "Das aktuelle Projekt wird geschlossen.\nFortfahren?", "Möglicher Datenverlust", JOptionPane.OK_CANCEL_OPTION);
					if(option == JOptionPane.OK_OPTION)
					{
						Manager.setInstance(new Manager());
						GuiSettings.getInstance().setCurrentSaveFile("");
						update();
					}
				}
			});
			file.add(newProject);
			JMenuItem open = new JMenuItem("Öffnen...");
			open.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					final JFileChooser fileChooser = new JFileChooser();
					FileNameExtensionFilter ff = new FileNameExtensionFilter("Kontomanager Output Datei", "kmo");
					fileChooser.setAcceptAllFileFilterUsed(false);
					fileChooser.setApproveButtonText("Öffnen");
					fileChooser.setFileFilter(ff);					
					if(fileChooser.showOpenDialog(getContentPane()) == 0)
					{
						File fileChooserResult = fileChooser.getSelectedFile();

						if(fileChooserResult.getAbsoluteFile().toString().endsWith(".kmo"))
						{
							try {
								Manager.getInstance().load(fileChooserResult.getAbsoluteFile().toString());
								GuiSettings.getInstance().setCurrentSaveFile(fileChooserResult.getAbsolutePath());
							} catch (FileNotFoundException e1) {
								JOptionPane.showMessageDialog(getContentPane(), "Fehler beim laden der Datei:\n"+ e1.getMessage(), "Ladefehler", JOptionPane.ERROR_MESSAGE);
							}
						}
						else
						{
							try {
								Manager.getInstance().load(fileChooserResult.getAbsolutePath() + ".kmo");
								GuiSettings.getInstance().setCurrentSaveFile(fileChooserResult.getAbsolutePath() + ".kmo");
							} catch (FileNotFoundException e1) {
								JOptionPane.showMessageDialog(getContentPane(), "Fehler beim laden der Datei:\n"+ e1.getMessage(), "Ladefehler", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}
			});
			file.add(open);
			JMenu lastProjects = new JMenu("Letzte geöffnete Projekte");
				boolean gotOne = false;
				for(String s : GuiSettings.getInstance().getLastProjects())
				{
					if(s != null)
					{
						if(!s.isEmpty())
						{
							gotOne = true;
							JMenuItem tmp = new JMenuItem(s);
							tmp.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e){
									try {
										int option = JOptionPane.showConfirmDialog(getContentPane(), "Das aktuelle Projekt wird geschlossen.\nFortfahren?", "Möglicher Datenverlust", JOptionPane.OK_CANCEL_OPTION);
										if(option == JOptionPane.OK_OPTION)
										{
											Manager.setInstance(DataSerializer.loadManager(s));;
											GuiSettings.getInstance().setCurrentSaveFile(s);
											update();
										}
									} catch (FileNotFoundException e1) {
										int option = JOptionPane.showConfirmDialog(getContentPane(), "Die Datei wurde nicht gefunden.\nSoll die Referenz entfernt werden?", "Datei nicht gefunden", JOptionPane.OK_CANCEL_OPTION);
										if(option == JOptionPane.OK_OPTION)
										{
											GuiSettings.getInstance().removeLastProject(s);
											updateMenuBar();
										}
									}
								}
							});
							lastProjects.add(tmp);
						}
					}
				}
				if(!gotOne)
				{
					gotOne = true;
					JMenuItem tmp = new JMenuItem("Keine Projekte Vorhanden");
					lastProjects.add(tmp);
				}
			file.add(lastProjects);
			JMenuItem save = new JMenuItem("Speichern");
			save.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					save();
				}
			});
			file.add(save);
			JMenuItem saveAs = new JMenuItem("Speichern unter...");
			saveAs.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					saveAs();
				}
			});
			file.add(saveAs);
			JMenuItem close = new JMenuItem("Beenden");
			close.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Object[] options = {"Abbrechen", "Beenden", "Speichern & Beenden"};
					  int option = JOptionPane.showOptionDialog(GUI.this, "Möchten sie die Verwaltung vorher speichern?", "Beenden", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
					  if(option == 0)
					  {
						  return;
					  }
					  else if(option == 1)
					  {
						  System.exit(0);
					  }
					  else
					  {
						  if(save())
						  {
							  System.exit(0);
						  }
					  }
					  
				  }});
			file.add(close);
		menu.add(file);
		
		JMenu edit = new JMenu("Bearbeiten");
			JMenuItem addPlan = new JMenuItem("Plan hinzufügen");
			addPlan.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					addPlan();
				}
			});
			edit.add(addPlan);
		menu.add(edit);
		
		return menu;
	}
	
	private void updateMenuBar()
	{
		setJMenuBar(createMenuBar());
	}
	
	private void createPopup()
	{
		//TODO
	}

	public void update()
	{
		setPlanData();
		//TODO
	}
	
	private void setPlanData()
	{
		plans.setListData(Manager.getInstance().getPlans().toArray());
	}

	// Button Functions
	
	private boolean save()
	{
		if(!GuiSettings.getInstance().getCurrentSaveFile().isEmpty())
		{
			Manager.getInstance().save(GuiSettings.getInstance().getCurrentSaveFile());
		}
		else
		{
			final JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter ff = new FileNameExtensionFilter("Kontomanager Output Datei", "kmo");
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setApproveButtonText("Speichern");
			fileChooser.setFileFilter(ff);					
			if(fileChooser.showOpenDialog(getContentPane()) == 0)
			{
				File fileChooserResult = fileChooser.getSelectedFile();

				if(fileChooserResult.getAbsoluteFile().toString().endsWith(".kmo"))
				{
					Manager.getInstance().save(fileChooserResult.getAbsoluteFile().toString());
					GuiSettings.getInstance().setCurrentSaveFile(fileChooserResult.getAbsolutePath());
				}
				else
				{
					Manager.getInstance().save(fileChooserResult.getAbsolutePath() + ".kmo");
					GuiSettings.getInstance().setCurrentSaveFile(fileChooserResult.getAbsolutePath() + ".kmo");
				}
			}
			else
			{
				updateMenuBar();
				return false;
			}
		}
		
		updateMenuBar();
		return true;
	}
	
	private void saveAs()
	{
		final JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter ff = new FileNameExtensionFilter("Kontomanager Output Datei", "kmo");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setApproveButtonText("Speichern");
		fileChooser.setFileFilter(ff);					
		if(fileChooser.showOpenDialog(getContentPane()) == 0)
		{
			File fileChooserResult = fileChooser.getSelectedFile();

			if(fileChooserResult.getAbsoluteFile().toString().endsWith(".kmo"))
			{
				Manager.getInstance().save(fileChooserResult.getAbsoluteFile().toString());
				GuiSettings.getInstance().setCurrentSaveFile(fileChooserResult.getAbsolutePath());
			}
			else
			{
				Manager.getInstance().save(fileChooserResult.getAbsolutePath() + ".kmo");
				GuiSettings.getInstance().setCurrentSaveFile(fileChooserResult.getAbsolutePath() + ".kmo");
			}
		}
		
		updateMenuBar();
	}
	
	private void addPlan()
	{
		AddPlanDialogue.open(GUI.this);
	}
}

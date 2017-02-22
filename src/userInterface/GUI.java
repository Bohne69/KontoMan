package userInterface;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import applicationLogic.Manager;
import dataStorage.DataSerializer;
import dataStorage.PDFGenerator;
import rawData.BeanAccount;
import rawData.BeanDate;
import rawData.BeanMoney;
import rawData.BeanPlan;
import rawData.MonthBalanceData;
import userInterfaceUtility.BeanPlanListCellRenderer;
import userInterfaceUtility.MonthBalanceDataListCellRenderer;
import utility.BeanPlanState;

@SuppressWarnings("all")
public class GUI extends JFrame {

	private JList plans;
	private JLabel balance;
	private JLabel monthlyBooking;
	private Graph graph;
	private JTextField planSearchBar;
	private JList monthBalance;
	private JSpinner warningThresholdSpinner;
	
	public GUI()
	{
		super("Bohnes Kontomanager V4");

		UIManager.put("control", new Color(102,104,106));
		UIManager.put("info", new Color(115,151,90));
		UIManager.put("nimbusAlertYellow", new Color(127,105,12));
		UIManager.put("nimbusBase", new Color(20,44,65));
		UIManager.put("nimbusDisabledText", new Color(65,65,65));
		UIManager.put("nimbusFocus", new Color(52,75,100));
		UIManager.put("nimbusGreen", new Color(80,90,20));
		UIManager.put("nimbusInfoBlue", new Color(18,41,85));
		UIManager.put("nimbusLightBackground", new Color(120,120,120));
		UIManager.put("nimbusOrange", new Color(90,44,0));
		UIManager.put("nimbusRed", new Color(80,18,12));
		UIManager.put("nimbusSelectedText", new Color(122,122,122));
		UIManager.put("nimbusSelectionBackground", new Color(26,47,64));
		UIManager.put("text", new Color(0,0,0));
		
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		    if ("Nimbus".equals(info.getName())) {
		    	try {
					UIManager.setLookAndFeel(info.getClassName());
					
					LookAndFeel laf = UIManager.getLookAndFeel();
					
					UIDefaults defs = laf.getDefaults();
					defs.put("Tree.drawHorizontalLines", true);
					defs.put("Tree.drawVerticalLines", true);
					defs.put("Tree.linesStyle", "dashed");
					
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
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
		update();
		
		cleanupImages();
		
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
			
		setPreferredSize(new Dimension(1720,820));
//		setPreferredSize(new Dimension(1520,890));
		
		pack();
		setLocationRelativeTo(null);
		
//		loadDisplayState();
//		updateDisplay();
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
		
		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	private void createGUI()
	{
		setJMenuBar(createMenuBar());

		setLayout(new GridLayout(1,2));
		
		JPanel left = new JPanel(new BorderLayout());
		JPanel right = new JPanel(new BorderLayout());
		
		JSplitPane middleSplitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
		middleSplitter.setOneTouchExpandable(true);
		middleSplitter.setResizeWeight(0.5);
		add(middleSplitter, BorderLayout.CENTER);
		//add(left);
		//add(right);
		
		JPanel leftTop = new JPanel(new BorderLayout());
		leftTop.add(new JLabel(" Suche: "), BorderLayout.WEST);
		planSearchBar = new JTextField();
		planSearchBar.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				update();
			}
			public void insertUpdate(DocumentEvent e) {
				update();
			}
			public void changedUpdate(DocumentEvent e) {
				update();
			}
		});
		leftTop.add(planSearchBar, BorderLayout.CENTER);
		left.add(leftTop, BorderLayout.NORTH);
		
		plans = new JList();
		plans.setCellRenderer(new BeanPlanListCellRenderer());
		JScrollPane planScroller = new JScrollPane(plans);
		left.add(planScroller, BorderLayout.CENTER);		
		createPopup();
		setPlanData();
		
		JPanel accountDetails = new JPanel(new GridLayout(1,3));
		//right.add(accountDetails, BorderLayout.NORTH);
		JLabel accountDetailsTitle = new JLabel("Aktuelle Kontodaten:");
		accountDetailsTitle.setHorizontalAlignment(SwingConstants.CENTER);
		accountDetailsTitle.setFont(new Font(accountDetailsTitle.getFont().getFontName(), Font.PLAIN, 15));
		balance = new JLabel("Kontostand: " + Manager.getInstance().getAccount().BALANCE().toString());
		balance.setHorizontalAlignment(SwingConstants.CENTER);
		monthlyBooking = new JLabel("Monatliche Buchung: " + Manager.getInstance().getAccount().MONTHLY_BOOKING().toString());
		monthlyBooking.setHorizontalAlignment(SwingConstants.CENTER);
		accountDetails.add(accountDetailsTitle);
		accountDetails.add(balance);
		accountDetails.add(monthlyBooking);
		
		JPanel monthBalanceDetails = new JPanel(new BorderLayout());
		monthBalanceDetails.setMinimumSize(new Dimension(1038, 482));
		monthBalance = new JList();
		monthBalance.setCellRenderer(new MonthBalanceDataListCellRenderer());
		JScrollPane monthBalanceScroller = new JScrollPane(monthBalance);
		monthBalanceDetails.add(accountDetails, BorderLayout.NORTH);
		monthBalanceDetails.add(monthBalanceScroller, BorderLayout.CENTER);
		//TODO
		
		graph = new Graph(Manager.getInstance().getFutureScores(), Manager.getInstance().getFutureMonths());
				
		JSplitPane graphSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, graph, monthBalanceDetails);
		graphSplitter.setOneTouchExpandable(true);
		graphSplitter.setResizeWeight(1);
		right.add(graphSplitter, BorderLayout.CENTER);
	}
	
	private JMenuBar createMenuBar()
	{
		JMenuBar menu = new JMenuBar();
		
		// FILE ---------------
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
								updateMenuBar();
								update();
							} catch (FileNotFoundException e1) {
								JOptionPane.showMessageDialog(getContentPane(), "Fehler beim laden der Datei:\n"+ e1.getMessage(), "Ladefehler", JOptionPane.ERROR_MESSAGE);
							}
						}
						else
						{
							try {
								Manager.getInstance().load(fileChooserResult.getAbsolutePath() + ".kmo");
								GuiSettings.getInstance().setCurrentSaveFile(fileChooserResult.getAbsolutePath() + ".kmo");
								updateMenuBar();
								update();
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
		
		// EDIT ---------------
		JMenu edit = new JMenu("Bearbeiten");
			JMenuItem addPlan = new JMenuItem("Plan hinzufügen");
			addPlan.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					addPlan();
				}
			});
			edit.add(addPlan);
			JMenuItem booking = new JMenuItem("Buchung durchführen");
			booking.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					
					JTextField topicIn = new JTextField();
					JSpinner amountIn = new JSpinner(new SpinnerNumberModel(0.0,-999999.0,999999.0,0.01));
					
					Object[] inputs = {"Betreff:", topicIn, "Menge:", amountIn};
					
					int option = JOptionPane.showConfirmDialog(getContentPane(), inputs, "Buchung durchführen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					if (option == JOptionPane.OK_OPTION)
					{
						if(topicIn.getText().isEmpty())
						{
							JOptionPane.showMessageDialog(getContentPane(), "Eine Buchung kann nicht ohne Betreff durchgeführt werden!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
						}
						else
						{
							Manager.getInstance().booking(topicIn.getText(), new BeanMoney((double)amountIn.getValue()));
							update();
						}
					}
				}
			});
			edit.add(booking);
			JMenuItem setAccount = new JMenuItem("Konto initialisieren");
			setAccount.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					
					JSpinner balanceIn = new JSpinner(new SpinnerNumberModel(Manager.getInstance().getAccount().BALANCE().AMOUNT(),-999999.0,999999.0,0.01));
					JSpinner monthlyBookingIn = new JSpinner(new SpinnerNumberModel(Manager.getInstance().getAccount().MONTHLY_BOOKING().AMOUNT(),-999999.0,999999.0,0.01));
					Object[] inputs = {"Kontostand:", balanceIn, "Monatliche Buchung:", monthlyBookingIn};
					int option = JOptionPane.showConfirmDialog(getContentPane(), inputs, "Konto initialisieren", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					if (option == JOptionPane.OK_OPTION)
					{
						 Manager.getInstance().setAccount(new BeanAccount((double)balanceIn.getValue(), (double)monthlyBookingIn.getValue()));
						 update();
					}
				}
			});
			edit.add(setAccount);
		menu.add(edit);
		
		// TOOLS ---------------
		JMenu tools = new JMenu("Werkzeuge");
			JMenu print = new JMenu("PDF Export");
				JMenuItem printPlans = new JMenuItem("Alle Pläne Exportieren");
				printPlans.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						final JFileChooser fileChooser = new JFileChooser();
						FileNameExtensionFilter ff = new FileNameExtensionFilter("PDF Dokument", "pdf");
						fileChooser.setAcceptAllFileFilterUsed(false);
						fileChooser.setApproveButtonText("Exportieren");
						fileChooser.setFileFilter(ff);					
						if(fileChooser.showOpenDialog(getContentPane()) == 0)
						{
							File fileChooserResult = fileChooser.getSelectedFile();

							if(fileChooserResult.getAbsoluteFile().toString().endsWith(".pdf"))
							{
								try {
									PDFGenerator.generatePlanList(fileChooserResult.getAbsolutePath(), Manager.getInstance().getPlans());
								} catch (IOException e1) {
									JOptionPane.showMessageDialog(getContentPane(), "Fehler beim exportieren der Datei: " + e1.getMessage(), "Export Fehler", JOptionPane.ERROR_MESSAGE);
								}
							}
							else
							{
								try {
									PDFGenerator.generatePlanList(fileChooserResult.getAbsolutePath() + ".pdf", Manager.getInstance().getPlans());
								} catch (IOException e1) {
									JOptionPane.showMessageDialog(getContentPane(), "Fehler beim exportieren der Datei: " + e1.getMessage(), "Export Fehler", JOptionPane.ERROR_MESSAGE);
								}
							}
						}
					}
				});
				print.add(printPlans);
				JMenuItem printFuture = new JMenuItem("Zukünftigen Kontoverlauf Exportieren");
				printFuture.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						final JFileChooser fileChooser = new JFileChooser();
						FileNameExtensionFilter ff = new FileNameExtensionFilter("PDF Dokument", "pdf");
						fileChooser.setAcceptAllFileFilterUsed(false);
						fileChooser.setApproveButtonText("Exportieren");
						fileChooser.setFileFilter(ff);					
						if(fileChooser.showOpenDialog(getContentPane()) == 0)
						{
							File fileChooserResult = fileChooser.getSelectedFile();

							if(fileChooserResult.getAbsoluteFile().toString().endsWith(".pdf"))
							{
								try {
									PDFGenerator.generateDetailedAccountProgression(fileChooserResult.getAbsolutePath());
								} catch (IOException e1) {
									JOptionPane.showMessageDialog(getContentPane(), "Fehler beim exportieren der Datei: " + e1.getMessage(), "Export Fehler", JOptionPane.ERROR_MESSAGE);
								}
							}
							else
							{
								try {
									PDFGenerator.generateDetailedAccountProgression(fileChooserResult.getAbsolutePath() + ".pdf");
								} catch (IOException e1) {
									JOptionPane.showMessageDialog(getContentPane(), "Fehler beim exportieren der Datei: " + e1.getMessage(), "Export Fehler", JOptionPane.ERROR_MESSAGE);
								}
							}
						}
					}
				});
				print.add(printFuture);
			tools.add(print);
			JMenuItem converter = new JMenuItem("Währungsrechner");
			converter.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					
					JPanel from = new JPanel(new GridLayout(1,2));
					JSpinner fromAmount = new JSpinner(new SpinnerNumberModel(Manager.getInstance().getAccount().BALANCE().AMOUNT(),-999999.0,999999.0,0.01));
					from.add(fromAmount);
					JComboBox fromCurrency = new JComboBox(new String[]{"Euro", "Dollar", "Yen"});
					from.add(fromCurrency);
					JComboBox toCurrency = new JComboBox(new String[]{"Euro", "Dollar", "Yen"});
					
					Object[] inputs = {"Von:", from, "Nach:", toCurrency};
					int option = JOptionPane.showConfirmDialog(getContentPane(), inputs, "Währungsrechner", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					if (option == JOptionPane.OK_OPTION)
					{
						try {
								Desktop.getDesktop().browse(new URI("https://www.google.de/?gws_rd=ssl#q=" + fromAmount.getValue().toString() + "+" + fromCurrency.getSelectedItem().toString() + "+in+" + toCurrency.getSelectedItem().toString()));
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(getContentPane(), e1.getMessage(), "Browser Error", JOptionPane.ERROR_MESSAGE);
							} catch (URISyntaxException e1) {
								JOptionPane.showMessageDialog(getContentPane(), e1.getMessage(), "Browser Error", JOptionPane.ERROR_MESSAGE);
							}
					}
				}
			});
			tools.add(converter);
		menu.add(tools);
		
		// SETTINGS ---------------
		JMenu settings = new JMenu("Optionen");
			JPanel threshold = new JPanel(new BorderLayout());
			threshold.add(new JLabel("Warnungsschwelle"), BorderLayout.WEST);
			warningThresholdSpinner = new JSpinner(new SpinnerNumberModel(GuiSettings.getInstance().getWarningThreshold(), 0, 99999, 1));
			warningThresholdSpinner.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					if((double)warningThresholdSpinner.getValue() != GuiSettings.getInstance().getWarningThreshold())
					{
						GuiSettings.getInstance().setWarningThreshold((double)warningThresholdSpinner.getValue());
						update();
					}
				}
			});
			threshold.add(warningThresholdSpinner, BorderLayout.CENTER);
			settings.add(threshold);
		menu.add(settings);
		
		return menu;
	}
	
	private void updateMenuBar()
	{
		setJMenuBar(createMenuBar());
	}
	
	private void createPopup()
	{
		// Main Menu
				JPopupMenu popupMenu = new JPopupMenu();
				
				// Show Submenu
				JMenu show = new JMenu("Anzeigen");
					JMenuItem details = new JMenuItem("Detailübersicht");
					details.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							PlanDetailsDialogue.open(GUI.this, (BeanPlan)plans.getSelectedValue());
						}
					});
					show.add(details);
					JMenuItem website = new JMenuItem("Produktseite");
					website.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							openWeblinkPage((BeanPlan)plans.getSelectedValue());
						}
					});
					JMenuItem tracking = new JMenuItem("Sendungsverfolgung");
					tracking.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							openTrackingPage((BeanPlan)plans.getSelectedValue());
						}
					});
					JMenuItem platformPage = new JMenuItem("Plattform Accountübersicht");
					platformPage.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							openPlatformWebPage((BeanPlan)plans.getSelectedValue());
						}
					});
					
				JMenuItem toggleCalc = new JMenuItem("Berechnung umschalten");
				toggleCalc.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						for(BeanPlan b : (ArrayList<BeanPlan>)plans.getSelectedValuesList())
						{
							b.setIfShouldCalculate(!b.shouldCalculate());
						}
						update();
					}
				});
				popupMenu.add(toggleCalc);
					
				JMenuItem addImage = new JMenuItem("Bild hinzufügen");
				addImage.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						final JFileChooser fileChooser = new JFileChooser();
						fileChooser.setMultiSelectionEnabled(true);
						FileNameExtensionFilter ff = new FileNameExtensionFilter("Bilder", "png", "jpg", "jpeg", "gif");
						fileChooser.setAcceptAllFileFilterUsed(false);
						fileChooser.setApproveButtonText("Verwenden");
						fileChooser.setFileFilter(ff);					
						if(fileChooser.showOpenDialog(getContentPane()) == 0)
						{
							File fileChooserResult = fileChooser.getSelectedFile();

							File target = new File("img/" + fileChooserResult.getName());

							try { Files.copy(fileChooserResult.toPath(), target.toPath(), REPLACE_EXISTING);
							((BeanPlan)plans.getSelectedValue()).setImgPath(target.getAbsolutePath()); } catch (IOException e1) {e1.printStackTrace();};
							
							update();
						}
					}
				});
				
				JMenu edit = new JMenu("Bearbeiten");
					JMenuItem change = new JMenuItem("Verändern");
					change.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							EditPlanDialogue.open(GUI.this, (BeanPlan)plans.getSelectedValue());
						}
					});
					edit.add(change);
					JMenu editState = new JMenu("Status ändern");
						JMenuItem planned = new JMenuItem("Geplant");
						planned.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								((BeanPlan)plans.getSelectedValue()).setState(BeanPlanState.PLANNED);
								Manager.getInstance().updatePlanStates();
								update();
							}
						});
						editState.add(planned);
						JMenuItem shipping = new JMenuItem("Versand");
						shipping.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								((BeanPlan)plans.getSelectedValue()).setState(BeanPlanState.SHIPPING);
								Manager.getInstance().updatePlanStates();
								update();
							}
						});
						editState.add(shipping);
						JMenuItem taxed = new JMenuItem("Im Zoll");
						taxed.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								((BeanPlan)plans.getSelectedValue()).setState(BeanPlanState.TAXED);
								Manager.getInstance().updatePlanStates();
								update();
							}
						});
						editState.add(taxed);
					edit.add(editState);
					JMenuItem finish = new JMenuItem("Abschließen");
					finish.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							Manager.getInstance().finalizePlan((BeanPlan)plans.getSelectedValue(), ((BeanPlan)plans.getSelectedValue()).getAmount());
							update();
						}
					});
					edit.add(finish);
					JMenuItem delete = new JMenuItem("Löschen");
					delete.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							Manager.getInstance().deletePlan((BeanPlan)plans.getSelectedValue());
							update();
						}
					});
					edit.add(delete);
				
				JMenuItem print = new JMenuItem("Als PDF Exportieren");
				print.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e)
						{
							if(plans.getSelectedValuesList().size() > 1)
							{
								final JFileChooser fileChooser = new JFileChooser();
								FileNameExtensionFilter ff = new FileNameExtensionFilter("PDF Dokument", "pdf");
								fileChooser.setAcceptAllFileFilterUsed(false);
								fileChooser.setApproveButtonText("Exportieren");
								fileChooser.setFileFilter(ff);					
								if(fileChooser.showOpenDialog(getContentPane()) == 0)
								{
									File fileChooserResult = fileChooser.getSelectedFile();

									if(fileChooserResult.getAbsoluteFile().toString().endsWith(".pdf"))
									{
										try {
											PDFGenerator.generatePlanList(fileChooserResult.getAbsolutePath(), (List<BeanPlan>)plans.getSelectedValuesList());
										} catch (IOException e1) {
											JOptionPane.showMessageDialog(getContentPane(), "Fehler beim exportieren der Datei: " + e1.getMessage(), "Export Fehler", JOptionPane.ERROR_MESSAGE);
										}
									}
									else
									{
										try {
											PDFGenerator.generatePlanList(fileChooserResult.getAbsolutePath() + ".pdf", (List<BeanPlan>)plans.getSelectedValuesList());
										} catch (IOException e1) {
											JOptionPane.showMessageDialog(getContentPane(), "Fehler beim exportieren der Datei: " + e1.getMessage(), "Export Fehler", JOptionPane.ERROR_MESSAGE);
										}
									}
								}
							}
							else
							{
								final JFileChooser fileChooser = new JFileChooser();
								FileNameExtensionFilter ff = new FileNameExtensionFilter("PDF Dokument", "pdf");
								fileChooser.setAcceptAllFileFilterUsed(false);
								fileChooser.setApproveButtonText("Exportieren");
								fileChooser.setFileFilter(ff);					
								if(fileChooser.showOpenDialog(getContentPane()) == 0)
								{
									File fileChooserResult = fileChooser.getSelectedFile();

									if(fileChooserResult.getAbsoluteFile().toString().endsWith(".pdf"))
									{
										try {
											PDFGenerator.generatePlanDetails(fileChooserResult.getAbsolutePath(), (BeanPlan)plans.getSelectedValue());
										} catch (IOException e1) {
											JOptionPane.showMessageDialog(getContentPane(), "Fehler beim exportieren der Datei: " + e1.getMessage(), "Export Fehler", JOptionPane.ERROR_MESSAGE);
										}
									}
									else
									{
										try {
											PDFGenerator.generatePlanDetails(fileChooserResult.getAbsolutePath() + ".pdf", (BeanPlan)plans.getSelectedValue());
										} catch (IOException e1) {
											JOptionPane.showMessageDialog(getContentPane(), "Fehler beim exportieren der Datei: " + e1.getMessage(), "Export Fehler", JOptionPane.ERROR_MESSAGE);
										}
									}
								}
							}
						}
					});
									
				// Add MouseListener
		        plans.addMouseListener(new MouseAdapter() {
		         public void mouseClicked(MouseEvent me) {
		            if (SwingUtilities.isRightMouseButton(me) && !plans.isSelectionEmpty())
		            {
		               if(plans.getSelectedValuesList().size() > 1)
		               {
		            	   popupMenu.remove(addImage);
		            	   popupMenu.remove(show);
		            	   popupMenu.remove(edit);
		            	   popupMenu.add(print);
		            	               	   
		            	   popupMenu.show(plans, me.getX(), me.getY());
		               }
		               else
		               {
		            	   if(!((BeanPlan)plans.getSelectedValue()).getWeblink().isEmpty())
		            		   show.add(website);
		            	   else
		            		   show.remove(website);
		            	   
		            	   if(!((BeanPlan)plans.getSelectedValue()).getTrackingId().isEmpty())
		            		   show.add(tracking);
		            	   else
		            		   show.remove(tracking);
		            	   
		            	   show.add(platformPage);
		            	   
		            	   popupMenu.add(addImage);
		            	   popupMenu.add(show);
		            	   popupMenu.add(edit);
		            	   popupMenu.add(print);
		            	   
		            	   popupMenu.show(plans, me.getX(), me.getY());
		               }
		            }
		            else if(SwingUtilities.isLeftMouseButton(me) && !plans.isSelectionEmpty() && me.getClickCount() == 2)
		            {
		            	PlanDetailsDialogue.open(GUI.this, (BeanPlan)plans.getSelectedValue());
		            }
		         }});

	}

	public void update()
	{
		setPlanData();
		setAccountData();
		setGraphData();
		setMonthlyBalanceView();
	}
	
	private void setGraphData()
	{
		graph.setScores(Manager.getInstance().getFutureScores(), Manager.getInstance().getFutureMonths());
	}
	
	private void setPlanData()
	{
		plans.setListData(searchPlans(planSearchBar.getText()).toArray());
	}
	
	private ArrayList<BeanPlan> searchPlans(String toSearch)
	{
		if(toSearch.isEmpty())
		{
			return Manager.getInstance().getPlans();
		}
		
		toSearch = toSearch.toLowerCase();
		
		ArrayList<BeanPlan> res = new ArrayList<BeanPlan>();
		
		for(BeanPlan b : Manager.getInstance().getPlans())
		{
			
			if(b.getDescription().toLowerCase().contains(toSearch)
					|| b.getPlatform().NAME().toLowerCase().contains(toSearch)
					|| b.getState().toString().toLowerCase().contains(toSearch)
					|| (b.getAmount().AMOUNT()+"").toLowerCase().contains(toSearch))
			{
				res.add(b);
			}
	
		}
		
		return res;
	}
	
	private void setAccountData()
	{
		balance.setText("Kontostand: " + Manager.getInstance().getAccount().BALANCE().toString());
		monthlyBooking.setText("Monatliche Buchung: " + Manager.getInstance().getAccount().MONTHLY_BOOKING().toString());
	}

	private void setMonthlyBalanceView()
	{
		MonthBalanceData[] data = new MonthBalanceData[12];
		for(int i = 0; i < 12; i++){data[i] = new MonthBalanceData();}
		
		BeanMoney currentBalance = new BeanMoney(Manager.getInstance().getAccount().BALANCE().AMOUNT());
		BeanDate tmp = new BeanDate(true);
		List<BeanDate> months = new ArrayList<BeanDate>();
		for(int i = 0; i < 12; i++)
		{
			months.add(new BeanDate(tmp.PART(), tmp.MONTH(), tmp.YEAR()));
			tmp = tmp.getNextMonth();
		}
		
		data[0].setMonthStart(new BeanMoney(currentBalance.AMOUNT()));
		
		for(int i = 0; i < 12; i++)
		{
			if(i > 0)
			{
				currentBalance.addAmount(Manager.getInstance().getAccount().MONTHLY_BOOKING().AMOUNT());
				data[i].setMonthStart(new BeanMoney(currentBalance.AMOUNT()));
			}
			data[i].setDate(months.get(i).toSimpleString());
			
			data[i].setMonthMinimum(new BeanMoney(currentBalance.AMOUNT()));
			for(BeanPlan p : Manager.getInstance().getPlansInMonth(months.get(i)))
			{
				if(p.shouldCalculate())
				{
					currentBalance.addAmount(-p.getAmount().AMOUNT());
					if(data[i].getMonthMinimum().AMOUNT()  > currentBalance.AMOUNT())
					{
						data[i].setMonthMinimum(new BeanMoney(currentBalance.AMOUNT()));
					}
				}
			}
			
			data[i].setMonthEnd(new BeanMoney(currentBalance.AMOUNT()));
		}
		
		monthBalance.setListData(data);
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

	private void openTrackingPage(BeanPlan p)
	{
		if(!p.getTrackingId().isEmpty())
		{
			
			String transporter = p.getTrackingId().substring (0,2);
			System.out.println(transporter);
			
			// EMS
			if(transporter.equals("EM") || transporter.equals("RM"))
			{
				try {
					Desktop.getDesktop().browse(new URI("https://trackings.post.japanpost.jp/services/srv/search/direct?searchKind=S004&locale=en&reqCodeNo1=" + p.getTrackingId() + "&x=32&y=9"));
				} catch (IOException e) {
					JOptionPane.showMessageDialog(getContentPane(), e.getMessage(), "Browser Error", JOptionPane.ERROR_MESSAGE);
				} catch (URISyntaxException e) {
					JOptionPane.showMessageDialog(getContentPane(), e.getMessage(), "Browser Error", JOptionPane.ERROR_MESSAGE);
				}
			  return;
			}
			// DHL
			else if(transporter.equals("JJ"))
			{
				try {
					Desktop.getDesktop().browse(new URI("https://nolp.dhl.de/nextt-online-public/set_identcodes.do?lang=de&idc=" + p.getTrackingId() + "&rfn=&extendedSearch=true"));
				} catch (IOException e) {
					JOptionPane.showMessageDialog(getContentPane(), e.getMessage(), "Browser Error", JOptionPane.ERROR_MESSAGE);
				} catch (URISyntaxException e) {
					JOptionPane.showMessageDialog(getContentPane(), e.getMessage(), "Browser Error", JOptionPane.ERROR_MESSAGE);
				}
			  return;
			}
			else
			{
				try {
					Desktop.getDesktop().browse(new URI(p.getTrackingId()));
				} catch (IOException e) {
					JOptionPane.showMessageDialog(getContentPane(), e.getMessage(), "Browser Error", JOptionPane.ERROR_MESSAGE);
				} catch (URISyntaxException e) {
					JOptionPane.showMessageDialog(getContentPane(), e.getMessage(), "Browser Error", JOptionPane.ERROR_MESSAGE);
				}
			  return;
			}
		}
		else
		{
			JOptionPane.showMessageDialog(getContentPane(), "The selected Plan has no Tracking ID connected to it!", "Plan Tracking Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void openWeblinkPage(BeanPlan p)
	{
		if(!p.getWeblink().isEmpty())
		{
			  try {
					Desktop.getDesktop().browse(new URI(p.getWeblink()));
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Browser Error", JOptionPane.ERROR_MESSAGE);
				} catch (URISyntaxException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Browser Error", JOptionPane.ERROR_MESSAGE);
				}
			  return;
		}
		else
		{
			JOptionPane.showMessageDialog(null, "The selected Plan has no Weblink connected to it!", "Plan Weblink Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void openPlatformWebPage(BeanPlan p)
	{
		if(!p.getWeblink().isEmpty())
		{
			  try {
					Desktop.getDesktop().browse(new URI(p.getPlatform().URL()));
				} catch (IOException e) {
					JOptionPane.showMessageDialog(getContentPane(), e.getMessage(), "Browser Error", JOptionPane.ERROR_MESSAGE);
				} catch (URISyntaxException e) {
					JOptionPane.showMessageDialog(getContentPane(), e.getMessage(), "Browser Error", JOptionPane.ERROR_MESSAGE);
				}
			  return;
		}
		else
		{
			JOptionPane.showMessageDialog(getContentPane(), "The selected Plan has no Weblink connected to it!", "Plan Weblink Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cleanupImages()
	{
		ArrayList<File> allFiles = new ArrayList<File>();
		for(final File f : new File("img").listFiles())
		{
			allFiles.add(f);
		}
		
		for(final File f : new File("img").listFiles()) 
		{
			for(BeanPlan p : Manager.getInstance().getPlans())
			{
				if(f.getAbsolutePath().equals(p.getImgPath()))
				{
					allFiles.remove(f);
				}
			}
	    }
		
		for(File f : allFiles)
		{
			f.delete();
		}
	}
}

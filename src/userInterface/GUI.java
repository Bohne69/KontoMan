package userInterface;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
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
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileNameExtensionFilter;
import applicationLogic.Manager;
import dataStorage.DataSerializer;
import dataStorage.PDFGenerator;
import rawData.BeanAccount;
import rawData.BeanMoney;
import rawData.BeanPlan;
import userInterfaceUtility.BeanPlanListCellRenderer;
import utility.BeanPlanState;

@SuppressWarnings("all")
public class GUI extends JFrame {

	private JList plans;
	private JLabel balance;
	private JLabel monthlyBooking;
	private Graph graph;
	
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
			
		setPreferredSize(new Dimension(1446,519));
//		setPreferredSize(new Dimension(1520,890));
		
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
		
		JPanel accountDetails = new JPanel(new GridLayout(1,3));
		right.add(accountDetails, BorderLayout.NORTH);
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
		
		graph = new Graph(Manager.getInstance().getFutureScores(), Manager.getInstance().getFutureMonths());
		right.add(graph, BorderLayout.CENTER);
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
									PDFGenerator.generateFutureBalance(fileChooserResult.getAbsolutePath());
								} catch (IOException e1) {
									JOptionPane.showMessageDialog(getContentPane(), "Fehler beim exportieren der Datei: " + e1.getMessage(), "Export Fehler", JOptionPane.ERROR_MESSAGE);
								}
							}
							else
							{
								try {
									PDFGenerator.generateFutureBalance(fileChooserResult.getAbsolutePath() + ".pdf");
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
			//TODO
			tools.add(converter);
		menu.add(tools);
		
		// SETTINGS ---------------
		JMenu settings = new JMenu("Optionen");
			JMenuItem preferences = new JMenuItem("Einstellungen");
			//TODO
			settings.add(preferences);
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
		            	   
		            	   popupMenu.add(show);
		            	   popupMenu.add(edit);
		            	   popupMenu.add(print);
		            	   
		            	   popupMenu.show(plans, me.getX(), me.getY());
		               }
		            }
		         }});
		      
	}

	public void update()
	{
		setPlanData();
		setAccountData();
		setGraphData();
	}
	
	private void setGraphData()
	{
		graph.setScores(Manager.getInstance().getFutureScores(), Manager.getInstance().getFutureMonths());
	}
	
	private void setPlanData()
	{
		plans.setListData(Manager.getInstance().getPlans().toArray());
	}
	
	private void setAccountData()
	{
		balance.setText("Kontostand: " + Manager.getInstance().getAccount().BALANCE().toString());
		monthlyBooking.setText("Monatliche Buchung: " + Manager.getInstance().getAccount().MONTHLY_BOOKING().toString());
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

	private void openWeblinkPage(BeanPlan p)
	{
		if(!p.getWeblink().isEmpty())
		{
			  try {
					Desktop.getDesktop().browse(new URI(p.getWeblink()));
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

}

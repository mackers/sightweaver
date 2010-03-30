package sightweaver;

import java.io.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import org.w3c.dom.*;

/**
 * The SWJFrame class creates a single graphical user interface window and all components
 * needed. The menu, status bar, and table scrollers are created with the window. The 
 * class keeps state for a single document, so multiple SWJFrames with multiple documents
 * are ok.
 *
 * @author	David McNamara
 * @version	$Id: SWJFrame.java 242 2003-04-14 20:43:29Z mackers $
 */
public class SWJFrame extends JFrame implements SWJTableContainer, ActionListener {

	private Document openDoc;
	private SWJTable[] tables;
	private int openTable;
	private JPanel contentPane, statusBar;
	private JScrollPane scrollPane;
	private JLabel idPane, abbrPane, assocPane, axisPane;
	private	JMenuBar menuBar;
	private	JMenu fileMenu, tableMenu, cellMenu;
	private	JMenuItem fileImportMenuItem, fileExportMenuItem, fileExitMenuItem,
			tableEditSummaryMenuItem, tableEditCaptionMenuItem,
			cellEditHeaderMenuItem, cellAddHeaderScopeMenuItem,
			cellMakeHeaderMenuItem, cellMakeDataMenuItem,
			cellAddHeaderMenuItem, cellClearHeadersMenuItem,
			cellEditAxisMenuItem, fileViewSourceMenuItem;
	private JRadioButtonMenuItem[] tableMenuItems;
	private JFileChooser fileChooser;

	/**
	 * Creates the SWJFrame window with the default menus, menuitems, status bar
	 * and a blank table scoll pane. This method also sets the look and feel
	 * and other frame options.
	 */
	public SWJFrame () {
		// get the global look and feel
                try {
                        UIManager.setLookAndFeel(
                                UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) { }

		// set window attributes
		super.setSize(640,480);

		// set the window closing action 
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				windowClose();
			}
		});

		// draw the menu
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		// add the file menu
		fileMenu = new JMenu(SWUtil.getString("fileMenu"));
		fileMenu.setMnemonic(SWUtil.getString("fileMenuMN").charAt(0));
		fileMenu.getAccessibleContext().setAccessibleDescription(SWUtil.getString("fileMenuDesc"));
		fileImportMenuItem = new JMenuItem(SWUtil.getString("fileImportMenuItem"));
		fileImportMenuItem.setMnemonic(SWUtil.getString("fileImportMenuItemMN").charAt(0));
		fileImportMenuItem.getAccessibleContext().setAccessibleDescription(SWUtil.getString("fileImportMenuItemDesc"));
		fileImportMenuItem.addActionListener(this);
		fileExportMenuItem = new JMenuItem(SWUtil.getString("fileExportMenuItem"));
		fileExportMenuItem.setMnemonic(SWUtil.getString("fileExportMenuItemMN").charAt(0));
		fileExportMenuItem.getAccessibleContext().setAccessibleDescription(SWUtil.getString("fileExportMenuItemDesc"));
		fileExportMenuItem.addActionListener(this);
		fileExportMenuItem.setEnabled(false);
		fileExitMenuItem = new JMenuItem(SWUtil.getString("fileExitMenuItem"));
		fileExitMenuItem.setMnemonic(SWUtil.getString("fileExitMenuItemMN").charAt(0));
		fileExitMenuItem.getAccessibleContext().setAccessibleDescription(SWUtil.getString("fileExitMenuItemDesc"));
		fileExitMenuItem.addActionListener(this);
		fileViewSourceMenuItem = new JMenuItem(SWUtil.getString("fileViewSourceMenuItem"));
		fileViewSourceMenuItem.setMnemonic(SWUtil.getString("fileViewSourceMenuItemMN").charAt(0));
		fileViewSourceMenuItem.getAccessibleContext().setAccessibleDescription(SWUtil.getString("fileViewSourceMenuItemDesc"));
		fileViewSourceMenuItem.addActionListener(this);
		fileViewSourceMenuItem.setEnabled(false); //disabled by default
		fileMenu.add(fileImportMenuItem);
		fileMenu.add(fileExportMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(fileViewSourceMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(fileExitMenuItem);

		// add the cell menu
		cellMenu = new JMenu(SWUtil.getString("cellMenu"));
		cellMenu.setMnemonic(SWUtil.getString("cellMenuMN").charAt(0));
		cellMenu.getAccessibleContext().setAccessibleDescription(SWUtil.getString("cellMenuDesc"));
		cellEditHeaderMenuItem = new JMenuItem(SWUtil.getString("cellEditHeaderMenuItem"));
		cellEditHeaderMenuItem.setMnemonic(SWUtil.getString("cellEditHeaderMenuItemMN").charAt(0));
		cellEditHeaderMenuItem.getAccessibleContext().setAccessibleDescription(SWUtil.getString("cellEditHeaderMenuItemDesc"));
		cellEditHeaderMenuItem.addActionListener(this);
		cellEditAxisMenuItem = new JMenuItem(SWUtil.getString("cellEditAxisMenuItem"));
		cellEditAxisMenuItem.setMnemonic(SWUtil.getString("cellEditAxisMenuItemMN").charAt(0));
		cellEditAxisMenuItem.getAccessibleContext().setAccessibleDescription(SWUtil.getString("cellEditAxisMenuItemDesc"));
		cellEditAxisMenuItem.addActionListener(this);
		cellAddHeaderMenuItem = new JMenuItem(SWUtil.getString("cellAddHeaderMenuItem"));
		cellAddHeaderMenuItem.setMnemonic(SWUtil.getString("cellAddHeaderMenuItemMN").charAt(0));
		cellAddHeaderMenuItem.getAccessibleContext().setAccessibleDescription(SWUtil.getString("cellAddHeaderMenuItemDesc"));
		cellAddHeaderMenuItem.addActionListener(this);
		cellClearHeadersMenuItem = new JMenuItem(SWUtil.getString("cellClearHeadersMenuItem"));
		cellClearHeadersMenuItem.setMnemonic(SWUtil.getString("cellClearHeadersMenuItemMN").charAt(0));
		cellClearHeadersMenuItem.getAccessibleContext().setAccessibleDescription(SWUtil.getString("cellClearHeadersMenuItemDesc"));
		cellClearHeadersMenuItem.addActionListener(this);
		cellAddHeaderScopeMenuItem = new JMenuItem(SWUtil.getString("cellAddHeaderScopeMenuItem"));
		cellAddHeaderScopeMenuItem.setMnemonic(SWUtil.getString("cellAddHeaderScopeMenuItemMN").charAt(0));
		cellAddHeaderScopeMenuItem.getAccessibleContext().setAccessibleDescription(SWUtil.getString("cellAddHeaderScopeMenuItemDesc"));
		cellAddHeaderScopeMenuItem.addActionListener(this);
		cellMakeHeaderMenuItem = new JMenuItem(SWUtil.getString("cellMakeHeaderMenuItem"));
		cellMakeHeaderMenuItem.setMnemonic(SWUtil.getString("cellMakeHeaderMenuItemMN").charAt(0));
		cellMakeHeaderMenuItem.getAccessibleContext().setAccessibleDescription(SWUtil.getString("cellMakeHeaderMenuItemDesc"));
		cellMakeHeaderMenuItem.addActionListener(this);
		cellMakeDataMenuItem = new JMenuItem(SWUtil.getString("cellMakeDataMenuItem"));
		cellMakeDataMenuItem.setMnemonic(SWUtil.getString("cellMakeDataMenuItemMN").charAt(0));
		cellMakeDataMenuItem.getAccessibleContext().setAccessibleDescription(SWUtil.getString("cellMakeDataMenuItemDesc"));
		cellMakeDataMenuItem.addActionListener(this);
		cellMenu.add(cellEditHeaderMenuItem);
		cellMenu.add(cellEditAxisMenuItem);
		cellMenu.addSeparator();
		cellMenu.add(cellAddHeaderMenuItem);
		cellMenu.add(cellClearHeadersMenuItem);
		cellMenu.add(cellAddHeaderScopeMenuItem);
		cellMenu.addSeparator();
		cellMenu.add(cellMakeHeaderMenuItem);
		cellMenu.add(cellMakeDataMenuItem);
		cellMenu.setEnabled(false); // disabled by default

		// add the table menu
		tableMenu = new JMenu(SWUtil.getString("tableMenu"));
		tableMenu.setMnemonic(SWUtil.getString("tableMenuMN").charAt(0));
		tableMenu.getAccessibleContext().setAccessibleDescription(SWUtil.getString("tableMenuDesc"));
		tableEditSummaryMenuItem = new JMenuItem(SWUtil.getString("tableEditSummaryMenuItem"));
		tableEditSummaryMenuItem.setMnemonic(SWUtil.getString("tableEditSummaryMenuItemMN").charAt(0));
		tableEditSummaryMenuItem.getAccessibleContext().setAccessibleDescription(SWUtil.getString("tableEditSummaryMenuItemDesc"));
		tableEditSummaryMenuItem.addActionListener(this);
		tableEditCaptionMenuItem = new JMenuItem(SWUtil.getString("tableEditCaptionMenuItem"));
		tableEditCaptionMenuItem.setMnemonic(SWUtil.getString("tableEditCaptionMenuItemMN").charAt(0));
		tableEditCaptionMenuItem.getAccessibleContext().setAccessibleDescription(SWUtil.getString("tableEditCaptionMenuItemDesc"));
		tableEditCaptionMenuItem.addActionListener(this);
		tableMenu.add(tableEditSummaryMenuItem);
		tableMenu.add(tableEditCaptionMenuItem);
		tableMenu.addSeparator();
		tableMenu.setEnabled(false); // disabled by default
		
		// add the dynamic table menu items
		tableMenuItems = new JRadioButtonMenuItem[10];
		ButtonGroup group = new ButtonGroup();
		//tableMenuItems.getAccessibleContent().setAccessibleName(SWUtil.getString("tableList"));
		//tableMenuItems.getAccessibleContent().setAccessibleDescription(SWUtil.getString("tableListDesc"));
		for (int i=0; i<tableMenuItems.length; i++) {
			tableMenuItems[i] = new JRadioButtonMenuItem("-");
			tableMenuItems[i].setMnemonic(new Integer(i).toString().charAt(0));
			tableMenuItems[i].setVisible(false);
			tableMenuItems[i].addActionListener(this);
			tableMenuItems[i].getAccessibleContext().setAccessibleDescription(SWUtil.getString("table" + i + "Desc"));
			tableMenu.add(tableMenuItems[i]);
			group.add(tableMenuItems[i]);
			// TODO: set accessible content to "first table in document" (bug 0006)
		}
		
		// add menus to menu bar
		menuBar.add(fileMenu);
		menuBar.add(tableMenu);
		menuBar.add(cellMenu);

		// create the file chooser
		fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);

		// create the status bar and panels
		statusBar = new JPanel();
		statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
		
		idPane = new JLabel(" ");
		idPane.setBorder(BorderFactory.createLoweredBevelBorder());
		idPane.setPreferredSize(new Dimension(70,20));

		abbrPane = new JLabel(" ");
		abbrPane.setBorder(BorderFactory.createLoweredBevelBorder());
		abbrPane.setPreferredSize(new Dimension(70,20));
			
		assocPane = new JLabel(" ");
		assocPane.setBorder(BorderFactory.createLoweredBevelBorder());
		assocPane.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
			
		axisPane = new JLabel(" ");
		axisPane.setBorder(BorderFactory.createLoweredBevelBorder());
		axisPane.setPreferredSize(new Dimension(120,20));

		statusBar.add(idPane);
		statusBar.add(abbrPane);
		statusBar.add(assocPane);
		statusBar.add(axisPane);

		// set empty status bar labels
		this.setIDStatus("");
		this.setAbbrStatus("");
		this.setHeadersStatus("");
		this.setAxisStatus("");

		// create the scroll pane
		scrollPane = new JScrollPane(new JTable());

		// set the window title
		this.updateTitle();
		
		this.refreshContentPane();
	}

	private void refreshContentPane() {
		// create the content pane
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(statusBar, BorderLayout.SOUTH);
		this.setContentPane(contentPane);
		
		// show/refresh the window
                //this.pack(); 
                this.setVisible(true); 

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == fileImportMenuItem) {
			// TODO: check if a document is already open (and needs saving) (bug 0007)
			
			//fileChooser.setCurrentDirectory(new File("/home/mackers/college/fyp/fyp/samples/"));
			//fileChooser.setCurrentDirectory(new File("n:\\college\\fyp\\fyp\\samples\\"));
			fileChooser.setCurrentDirectory(new File("/home/mackers/mnt/matrix/college/fyp/fyp/samples/"));
			//fileChooser.setCurrentDirectory(new File("/home/mackers/tmp2/"));
			// add the filters
			fileChooser.resetChoosableFileFilters();
			fileChooser.addChoosableFileFilter(new RDFFilter());
			fileChooser.addChoosableFileFilter(new CSVFilter());
			fileChooser.addChoosableFileFilter(new HTMLFilter());
			// show the dialog
			int retval = fileChooser.showOpenDialog(this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				this.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				try {
					Document doc = new Document(file);
					this.openDocument(doc);
				} catch (ParseException pe) {
					JOptionPane.showMessageDialog(this,
							SWUtil.getString("parseError") + "\n" +
							pe.getMessage(),
							SWUtil.getString("appName"),
							JOptionPane.ERROR_MESSAGE
							);
					//pe.printStackTrace();
				} catch (ImportException ie) {
					JOptionPane.showMessageDialog(this,
							SWUtil.getString("importError") + "\n" +
							ie.getMessage(),
							SWUtil.getString("appName"),
							JOptionPane.ERROR_MESSAGE
							);
					//ie.printStackTrace();
				} catch (Exception e2) { 
					JOptionPane.showMessageDialog(this,
							SWUtil.getString("unknownError") + "\n" +
							e2.getMessage(),
							SWUtil.getString("appName"),
							JOptionPane.ERROR_MESSAGE
							);
					e2.printStackTrace();
				}
				this.getContentPane().setCursor(Cursor.getDefaultCursor());
			}
		} else if (e.getSource() == fileExportMenuItem) {
			// add the filters
			fileChooser.resetChoosableFileFilters();
			fileChooser.addChoosableFileFilter(new HTMLFilter());
			// show the dialog
			int retval = fileChooser.showSaveDialog(this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				try {
					openDoc.export(file);
				} catch (ExportException ee) {
					JOptionPane.showMessageDialog(this,
							SWUtil.getString("exportError") + "\n" +
							ee.getMessage(),
							SWUtil.getString("appName"),
							JOptionPane.ERROR_MESSAGE
							);
				} catch (Exception e2) { 
					JOptionPane.showMessageDialog(this,
							SWUtil.getString("unknownError") + "\n" +
							e2.getMessage(),
							SWUtil.getString("appName"),
							JOptionPane.ERROR_MESSAGE
							);
					e2.printStackTrace();
				}
			}
		} else if (e.getSource() == fileViewSourceMenuItem) {
			// create a new (non-modal) window with a read-only textarea
			// containing the source
			try {
				String source = openDoc.getSource();
				JFrame vsFrame = new JFrame(SWUtil.getString("source"));
				JTextArea vsTextArea = new JTextArea(source);
				vsTextArea.setEditable(false);
				JScrollPane areaScrollPane = new JScrollPane(vsTextArea);
				areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				areaScrollPane.setPreferredSize(new Dimension(500, 350));
				vsFrame.getContentPane().add(areaScrollPane);
				vsFrame.pack();
				vsFrame.setVisible(true);
			} catch (ExportException ee) {
				JOptionPane.showMessageDialog(this,SWUtil.getString("viewSourceExportException"),SWUtil.getString("appName"),JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource() == fileExitMenuItem) {
			// close the window
			this.windowClose();
		} else if (e.getSource() == tableEditSummaryMenuItem) {
			// show the edit summary dialog
			SWEditSummaryDialog d = new SWEditSummaryDialog(this, tables[openTable].getDocumentTable().getSummary());
			if (d.showDialog()) tables[openTable].getDocumentTable().setSummary(d.getValue());
		} else if (e.getSource() == tableEditCaptionMenuItem) {
			// show the edit caption dialog
			SWEditCaptionDialog d = new SWEditCaptionDialog(this, tables[openTable].getDocumentTable().getCaption());
			if (d.showDialog()) {
				tables[openTable].getDocumentTable().setCaption(d.getValue());
				// update where the caption is displayed
				this.updateTitle();
				this.updateTableList();
			}
		} else if (e.getSource() == cellEditHeaderMenuItem) {
			// show the edit header info dialog
			SWEditHeaderInfoDialog d = new SWEditHeaderInfoDialog(this, tables[openTable].getSelectedID(), tables[openTable].getSelectedAbbr());
			if (d.showDialog()) {
				if (openDoc.isUniqueId(d.getIDValue())) {
					tables[openTable].setSelectedID(d.getIDValue());
				} else {
					JOptionPane.showMessageDialog(this,
						SWUtil.getString("idNotUnique"),
						SWUtil.getString("appName"),
						JOptionPane.ERROR_MESSAGE);
				}
				if (d.getAbbrValue().length() < tables[openTable].getSelectedContent().length()) {
					tables[openTable].setSelectedAbbr(d.getAbbrValue());
				} else {
					JOptionPane.showMessageDialog(this,
						SWUtil.getString("abbrLonger"),
						SWUtil.getString("appName"),
						JOptionPane.ERROR_MESSAGE);
				}
			}
		} else if (e.getSource() == cellAddHeaderMenuItem) {
			// show the add header dialog
			SWAddHeaderDialog d = new SWAddHeaderDialog(this, tables[openTable].getDocumentTable().getHeaders());
			if (d.showDialog()) {
				tables[openTable].addSelectedHeader(d.getValue());
			}
		} else if (e.getSource() == cellClearHeadersMenuItem) {
			// clear the headers
			tables[openTable].clearSelectedHeaders();
		} else if (e.getSource() == cellAddHeaderScopeMenuItem) {
			// show the add header scope dialog
			SWAddHeaderScopeDialog d = new SWAddHeaderScopeDialog(this);
			if (d.showDialog()) {
				// set the scope
				if (d.getValue().equals(SWUtil.getString("row"))) tables[openTable].setSelectedRowScope();
				if (d.getValue().equals(SWUtil.getString("column"))) tables[openTable].setSelectedColumnScope();
			}
		} else if (e.getSource() == cellEditAxisMenuItem) {
			// show the edit axis dialog
			SWEditAxisDialog d = new SWEditAxisDialog(this, tables[openTable].getSelectedAxis());
			if (d.showDialog()) tables[openTable].setSelectedAxis(d.getValue());
		} else if (e.getSource() == cellMakeHeaderMenuItem) {
			// make cell(s) header
			tables[openTable].makeSelectedHeaders();
		} else if (e.getSource() == cellMakeDataMenuItem) {
			// make cell(s) data
			tables[openTable].makeSelectedData();
		} else if (e.getSource().getClass().getName().equals("javax.swing.JRadioButtonMenuItem")) {
			// radio button must be switching tables
			for (int i=0; i<tableMenuItems.length; i++) {
				if (e.getSource() == tableMenuItems[i]) {
					this.showTable(i);
					break;
				}
			}
		}
	}

	private void windowClose() {
		// TODO: check if document is saved (bug 0008)
		this.dispose();
		System.exit(0);
	}

	private void openDocument(Document d) {
		// set this as the open document
		openDoc = d;
		// set up the JTables
		Table[] docTables = openDoc.getTables();
		tables = new SWJTable[docTables.length];
		// add to the table container
		for (int i=0; i<docTables.length; i++) {
			// create the tablemodel & table
			TableModel swjt = new SWJTableModel(docTables[i]);
			tables[i] = new SWJTable(swjt, this);
		}
		// update list of tables in the table menu
		this.updateTableList();
		// show the first table
		this.showTable(0);
		// enable menus and things
		fileExportMenuItem.setEnabled(true);
		fileViewSourceMenuItem.setEnabled(true);
		tableMenu.setEnabled(true);
		cellMenu.setEnabled(true);
	}

	private void showTable(int tableNum) {
		if (tableNum > tables.length - 1) return;
		// create a new scroll pane using this table
		scrollPane = new JScrollPane(tables[tableNum]);
		// make background white
		scrollPane.getViewport().setBackground(Color.white);
		// repaint
		this.refreshContentPane();
		// set the open table int
		openTable = tableNum;
		// update the window title
		this.updateTitle();
		// update the radio box in the list of tables
		tableMenuItems[tableNum].setSelected(true);
		// reset status bar and selection information
		tables[tableNum].updateSelection();
	}

	private void updateTitle() {
		String title = SWUtil.getString("appName");
		if (openDoc != null) {
			title += " - " + openDoc.getTitle() + " - " + this.getTableTitle(openTable);
		}
		this.setTitle(title);
	}
	
	private void updateTableList() {
		// update list of tables in table menu 
		for (int i=0; (i<openDoc.getTables().length) && (i<tableMenuItems.length); i++) {
			// set the table menu item
			// TODO: set menu item to first 12 letters only (bug 0019)
			tableMenuItems[i].setText(this.getTableTitle(i));
			tableMenuItems[i].setVisible(true);
		}
		for (int i=openDoc.getTables().length; i<tableMenuItems.length; i++) {
			// hide old tables
			tableMenuItems[i].setVisible(false);
		}
	}

	private String getTableTitle(int tid) {
		if (tables[tid].getDocumentTable().getTitle().equals("")) {
			return SWUtil.getString("table") + tid;
		} else {
			return tables[tid].getDocumentTable().getTitle();
		}
	}

	/**
	 * Sets the status bar ID value.
	 *
	 * @param s	the new ID vlaue
	 */
	public void setIDStatus(String s) { 
		String t = SWUtil.getString("id") + ": ";
		if (s != null) t += s;
		idPane.setText(t);
	}
	
	/**
	 * Sets the status bar axis value.
	 *
	 * @param s	the new axis value
	 */
	public void setAxisStatus(String s) { 
		String t = SWUtil.getString("axis") + ": ";
		if (s != null) t += s;
		axisPane.setText(t);
	}
	
	/**
	 * Sets the status bar abbreviation value.
	 *
	 * @param s	the new abbreviation
	 */
	public void setAbbrStatus(String s) { 
		String t = SWUtil.getString("abbreviation") + ": ";
		if (s != null) t += s;
		abbrPane.setText(t);
	}
	
	/**
	 * Sets the status bar headers string.
	 *
	 * @param s	the header string
	 */
	public void setHeadersStatus(String s) { 
		String t = SWUtil.getString("headers") + ": ";
		if (s != null) t += s;
		assocPane.setText(t);
	}

	/**
	 * Enables or disables the edit header menu item.
	 *
	 * @param e	the new enabled status
	 */
	public void setEditHeaderMenuItemEnabled(boolean e) {
		cellEditHeaderMenuItem.setEnabled(e);
	}
	
	/**
	 * Enables or disables the edit axis menu item.
	 *
	 * @param e	the new enabled status
	 */
	public void setEditAxisMenuItemEnabled(boolean e) {
		cellEditAxisMenuItem.setEnabled(e);
	}
	
	/**
	 * Enables or disables the add header menu item.
	 *
	 * @param e	the new enabled status
	 */
	public void setAddHeaderMenuItemEnabled(boolean e) {
		cellAddHeaderMenuItem.setEnabled(e);
	}
	
	/**
	 * Enables or disables the clear headers menu item.
	 *
	 * @param e	the new enabled status
	 */
	public void setClearHeadersMenuItemEnabled(boolean e) {
		cellClearHeadersMenuItem.setEnabled(e);
	}
	
	/**
	 * Enables or disables the add header scope menu item.
	 *
	 * @param e	the new enabled status
	 */
	public void setAddHeaderScopeMenuItemEnabled(boolean e) {
		cellAddHeaderScopeMenuItem.setEnabled(e);
	}
	
	/**
	 * Enables or disables the make header menu item.
	 *
	 * @param e	the new enabled status
	 */
	public void setMakeHeaderMenuItemEnabled(boolean e) {
		cellMakeHeaderMenuItem.setEnabled(e);
	}
	
	/** 
	 * Enables or disables the make data menu item.
	 *
	 * @param e	the new enabled status
	 */
	public void setMakeDataMenuItemEnabled(boolean e) {
		cellMakeDataMenuItem.setEnabled(e);
	}
}

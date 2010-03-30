package sightweaver;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This class is an implementation of the JTable class used for displaying
 * the HTML table in the sightweaver graphical user interface.
 * <p>
 * This class adds functions for setting certain attributes on all selected
 * cells. It also has event handlers for cell selection that enabling/disabling 
 * menu items and setting status bar values in the main SWJFrame.
 *
 * @author	David McNamara
 * @version	$Id: SWJTable.java 174 2003-04-03 19:58:16Z mackers $
 */
public class SWJTable extends JTable implements MouseListener {

	private TableCellRenderer cellRenderer = new SWCellRenderer();
	private ListSelectionModel listSelectionModel;
	private SWJTableModel myTableModel;
	private SWJFrame parentFrame;
	private final int rowHeight = 20;
	private String idStatus;
	private String axisStatus;
	private String abbrStatus;
	private String headersStatus;
	private int headerCellCount;
	private int dataCellCount;
	
	/**
	 * Creates the SWJTable with the supplied table model and owner frame.
	 *
	 * @param tm	the table model to use
	 * @param f	the owner frame
	 */
	public SWJTable(TableModel tm, SWJFrame f) {
		// do necessary table setup
		super(tm);
		// store the parent frame
		parentFrame = f;
		// store the tablemodel
		myTableModel = (SWJTableModel)tm;
		// set the row height
		this.setRowHeight(rowHeight);
		// set selection options & add the selection listener
		this.setCellSelectionEnabled(true);
		this.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		// hide the headers
		this.setTableHeader(null);
		// add a mouse listener so we can recalculate selected cells
		this.addMouseListener(this);
	}

	/**
	 * Returns the Table object associated with this table display.
	 *
	 * @return	the table object associated with this table display
	 * @see		sightweaver.Table
	 */
	public Table getDocumentTable() {
	        return myTableModel.getDocumentTable();
	}

	/**
	 * Returns the cell renderer for the cell at the specified location.
	 *
	 * @param row	the cell row
	 * @param col	the cell column
	 * @return	the cell renderer at this location
	 */
	public TableCellRenderer getCellRenderer(int row, int col) {
		return cellRenderer;
	}

	/**
	 * Returns the common ID of the selected cells.
	 *
	 * @return	the id
	 */
	public String getSelectedID() {
		return idStatus;
	}

	/**
	 * Sets the same id on all selected cells.
	 *
	 * @param id	the id to set
	 */
	public void setSelectedID(String id) {
		idStatus = id;
		for (int i=0; i<myTableModel.getRowCount(); i++) {
			for (int j=0; j<myTableModel.getColumnCount(); j++) {
				if ((isCellSelected(i,j)) && (myTableModel.getCellAt(i,j) != null)) {
					myTableModel.getCellAt(i,j).setID(id);
				}
			}
		}
		dispatchStatusSetters();
	}

	/**
	 * Returns the common axis of the selected cells.
	 *
	 * @return	the axis
	 */
	public String getSelectedAxis() {
		return axisStatus;
	}

	/**
	 * Sets the same axis on all selected cells
	 *
	 * @param a 	the axis to set
	 */
	public void setSelectedAxis(String a) {
		axisStatus = a;
		for (int i=0; i<myTableModel.getRowCount(); i++) {
			for (int j=0; j<myTableModel.getColumnCount(); j++) {
				if ((isCellSelected(i,j)) && (myTableModel.getCellAt(i,j) != null)) {
					myTableModel.getCellAt(i,j).setAxis(a);
				}
			}
		}
		dispatchStatusSetters();
	}
	
	/**
	 * Returns the common abbreviation of the selected cells.
	 *
	 * @return	the abbreviation
	 */
	public String getSelectedAbbr() {
		return abbrStatus;
	}

	/**
	 * Sets the same abbreviation on all selected cells
	 *
	 * @param a	the abbreviation to set
	 */
	public void setSelectedAbbr(String a) {
		abbrStatus = a;
		for (int i=0; i<myTableModel.getRowCount(); i++) {
			for (int j=0; j<myTableModel.getColumnCount(); j++) {
				if ((isCellSelected(i,j)) && 
						(myTableModel.getCellAt(i,j) != null) &&
						(myTableModel.getCellAt(i,j).isHeader())) {
						((HeaderCell)(myTableModel.getCellAt(i,j))).setAbbr(a);
				}
			}
		}
		dispatchStatusSetters();
	}

	/**
	 * Adds a header to all selected cells.
	 *
	 * @param h	the header to add
	 */
	public void addSelectedHeader(HeaderCell h) {
		for (int i=0; i<myTableModel.getRowCount(); i++) {
			for (int j=0; j<myTableModel.getColumnCount(); j++) {
				if ((isCellSelected(i,j)) && (myTableModel.getCellAt(i,j) != null)) {
					try {
						myTableModel.getCellAt(i,j).addHeader(h);
					} catch (HeadersFullException e) {
						//TODO: warn user (bug 0009);
					}
					headersStatus = myTableModel.getCellAt(i,j).getHeaderString();
				}
			}
		}
		dispatchStatusSetters();
	}

	/**
	 * Clears all headers on all selected cells.
	 */
	public void clearSelectedHeaders() {
		for (int i=0; i<myTableModel.getRowCount(); i++) {
			for (int j=0; j<myTableModel.getColumnCount(); j++) {
				if ((isCellSelected(i,j)) && (myTableModel.getCellAt(i,j) != null)) {
					myTableModel.getCellAt(i,j).clearHeaders();
				}
			}
		}
		headersStatus = "";
		dispatchStatusSetters();
	}

	/**
	 * Sets row scope on all selected (header) cells. This has the effect of associating
	 * all selected cells with the cells to their immediate right.
	 */
	public void setSelectedRowScope() {
		for (int i=0; i<myTableModel.getRowCount(); i++) {
			for (int j=0; j<myTableModel.getColumnCount(); j++) {
				if ((isCellSelected(i,j)) && (myTableModel.getCellAt(i,j) != null)) {
					// TODO: if cell spans multiple rows then set all to left (bug 0010)
					for (int k=j; k<myTableModel.getColumnCount(); k++) {
						if (myTableModel.getCellAt(i,k) != null) {
							try {
								myTableModel.getCellAt(i,k).addHeader(myTableModel.getCellAt(i,j));
							} catch (HeadersFullException e) {
								//TODO: warn user (bug 0009);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Sets column scope on all selected (header) cells. This has the effect of associating
	 * all selected cells with the cells immediately below them.
	 */
	public void setSelectedColumnScope() {
		for (int i=0; i<myTableModel.getRowCount(); i++) {
			for (int j=0; j<myTableModel.getColumnCount(); j++) {
				if ((isCellSelected(i,j)) && (myTableModel.getCellAt(i,j) != null)) {
					// TODO: if cell spans multiple columns then set all below (bug 0011)
					for (int k=i; k<myTableModel.getRowCount(); k++) {
						if (myTableModel.getCellAt(k,j) != null) {
							try {
								myTableModel.getCellAt(k,j).addHeader(myTableModel.getCellAt(i,j));
							} catch (HeadersFullException e) {
								//TODO: warn user (bug 0009);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Returns a concatenated string containing the textual content of all selected cells.
	 *
	 * @return	the text content
	 */
	public String getSelectedContent() {
		String s = "";
		for (int i=0; i<myTableModel.getRowCount(); i++) {
			for (int j=0; j<myTableModel.getColumnCount(); j++) {
				if ((isCellSelected(i,j)) && (myTableModel.getCellAt(i,j) != null)) {
						s += myTableModel.getCellAt(i,j).getTextContent();
				}
			}
		}
		return s;
	}

	/**
	 * Transforms all selected cells into header cells.
	 */
	public void makeSelectedHeaders() {
		for (int i=0; i<myTableModel.getRowCount(); i++) {
			for (int j=0; j<myTableModel.getColumnCount(); j++) {
				if ((isCellSelected(i,j)) && 
						(myTableModel.getCellAt(i,j) != null) && 
						(!myTableModel.getCellAt(i,j).isNull())) {
					myTableModel.setCellAt(myTableModel.getCellAt(i,j).makeHeaderCell(),i,j);
					// TODO: give a header id (bug 0012)
				}
			}
		}
		updateSelection();
	}

	/**
	 * Transforms all selected cells into data cells.
	 */
	public void makeSelectedData() {
		for (int i=0; i<myTableModel.getRowCount(); i++) {
			for (int j=0; j<myTableModel.getColumnCount(); j++) {
				if ((isCellSelected(i,j)) && 
						(myTableModel.getCellAt(i,j) != null) && 
						(!myTableModel.getCellAt(i,j).isNull())) {
					myTableModel.setCellAt(((HeaderCell)myTableModel.getCellAt(i,j)).makeDataCell(),i,j);
				}
			}
		}
		updateSelection();
	}
	
	public void mouseClicked(MouseEvent e) {
		this.updateSelection();
	}

	/**
	 * Updates the owner frame's status bar and menu items by checking what cells are selected. 
	 */
	public void updateSelection() {
		// vars to hold status bar information
		idStatus = null;
		axisStatus = null;
		abbrStatus = null;
		headersStatus = null;
		headerCellCount = 0;
		dataCellCount = 0;
		// check each cell for a selection
		for (int i=0; i<myTableModel.getRowCount(); i++) {
			for (int j=0; j<myTableModel.getColumnCount(); j++) {
				if (isCellSelected(i,j)) {
					Cell c = myTableModel.getCellAt(i,j);
					if (c == null) continue;
					// set this cell selected
					c.setSelected(true);
					// TODO: improve these algorithms to check for
					//       differing attributes only once (bug 0013)
					// get the id. if idstatus is null, then we haven't
					// set any id yet => this is the first cell
					// otherwise we have multiple, so blank status bar
					if (idStatus == null) {
						idStatus = c.getID();
					} else {
						idStatus = "";
					}
					// set the axis only if all cells have same axis
					if (axisStatus == null) {
						axisStatus = c.getAxis();
					} else if (axisStatus.equals(c.getAxis())) {
						// this cell has same axis as previous cell
					} else {
						axisStatus = "";
					}
					// set the headers cell only if all are same
					if (headersStatus == null) {
						headersStatus = c.getHeaderString();
					} else if (headersStatus.equals(c.getHeaderString())) {
					} else {
						headersStatus = "";
					}
					// set the abbr only if all cells have same abbr
					if (c.isHeader()) {
						HeaderCell hc = (HeaderCell)c;
						if (abbrStatus == null) {
							abbrStatus = hc.getAbbr();
						} else if (abbrStatus.equals(hc.getAbbr())) {
						} else {
							abbrStatus = "";
						}
						headerCellCount++;
					} else if (!c.isNull()) {
						dataCellCount++;
					}
				} else {
					myTableModel.setCellSelected(i,j,false);
				}
			}
		}
		// enable/disable menu items based on number and types of cells selection
		parentFrame.setEditHeaderMenuItemEnabled((headerCellCount == 1) && (dataCellCount == 0)); // 1 header cell selected only
		parentFrame.setEditAxisMenuItemEnabled((headerCellCount > 0) || (dataCellCount > 0)); // more than one cell selected
		parentFrame.setAddHeaderMenuItemEnabled((headerCellCount > 0) || (dataCellCount > 0)); // more than one cell selected
		parentFrame.setClearHeadersMenuItemEnabled((headerCellCount > 0) || (dataCellCount > 0)); // more than one cell selected
		parentFrame.setAddHeaderScopeMenuItemEnabled((headerCellCount > 0) && (dataCellCount == 0)); // one or more header cell
		parentFrame.setMakeHeaderMenuItemEnabled((headerCellCount == 0) && (dataCellCount > 0)); // one or more data cell
		parentFrame.setMakeDataMenuItemEnabled((headerCellCount > 0) && (dataCellCount == 0)); // one or more header cell

		// update the status bar
		dispatchStatusSetters();
	}

	private void dispatchStatusSetters() {
		// update the status bar
		parentFrame.setAxisStatus(axisStatus);
		parentFrame.setAbbrStatus(abbrStatus);
		parentFrame.setIDStatus(idStatus);
		parentFrame.setHeadersStatus(headersStatus);
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}
}

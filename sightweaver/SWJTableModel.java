package sightweaver;

import javax.swing.*;
import javax.swing.table.*;

/**
 * The SWJTableModel class is an extension of the <code>AbstractTableModel</code>
 * class. This table model stores a reference to the associated table object.
 *
 * @author	David McNamara
 * @version	$Id: SWJTableModel.java 175 2003-04-03 20:01:18Z mackers $
 */
public class SWJTableModel extends AbstractTableModel {

	private Table docTable;
	private Cell[][] cells;

	/**
	 * Create a SWJTableModel with the specified <code>Table</code>.
	 *
	 * @param dt	the associated table object
	 * @see		sightweaver.Table
	 */
	public SWJTableModel(Table dt) {
		// set the document table
		docTable = dt;
		// get the cells
		cells = docTable.getCells();
	}

	public Table getDocumentTable() {
		return docTable;
	}

        public int getColumnCount() {
	        return cells[0].length;
	}
	        
	public int getRowCount() {
	        return cells.length;
	}

	public String getColumnName(int col) {
	        return "";
	}

        public Object getValueAt(int row, int col) {
		return (Object)cells[row][col];
        }

	public Cell getCellAt(int row, int col) {
		return cells[row][col];
	}

	public void setCellAt(Cell c, int row, int col) {
		cells[row][col] = c;
	}

        public boolean isCellEditable(int row, int col) {
        	return false;
	}

	public Class getColumnClass(int c) {
        	return getValueAt(0, c).getClass();
	}

	public void setCellSelected(int row, int col, boolean s) {
		if (cells[row][col] != null) 
			cells[row][col].setSelected(s);
	}

}

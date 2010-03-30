package sightweaver;

import org.w3c.dom.*;
import java.util.Vector;

/**
 * The table class represents a single table in the document. It holds state for 
 * tables including a 2-dimensional array of cells in the table and the table's
 * summary and caption. The table's DOM element is also stored.
 *
 * @author	David McNamara
 * @version	$Id: Table.java 235 2003-04-14 14:37:35Z mackers $
 */
public class Table {

	private Cell[][] cells;
	private String summary;
	private String caption;
	private org.w3c.dom.Element domElement;

	public static final int TABLE_MISSING_SUMMARY = 0x01;
	public static final int TABLE_MISSING_CAPTION = 0x02;
	public static final int HEADER_MISSING_ABBREVIATION = 0x04;
	public static final int CELL_MISSING_HEADER = 0x08;

	/**
	 * Creates a new table with the specified cell content, summary,
	 * caption and element. The cell array and element are mandatory.
	 * A null summary or caption string will make the table have a
	 * blank summary or caption.
	 *
	 * @param c	the array of cell objects
	 * @param s	the table summary
	 * @param cp	the table caption
	 * @param e	the table DOM element
	 * @see		org.w3c.dom.Element
	 */
	public Table (Cell[][] c, String s, String cp, Element e) {
		cells = c;
		if (s == null) 
			summary = "";
		else 
			summary = s;
		if (cp == null)
			caption = "";
		else 
			caption = cp;
		domElement = e;
	}

	/**
	 * Returns an 2-dimensional array of cells in the table.
	 *
	 * @return	the array of cells
	 */
	public Cell[][] getCells() {
		return cells;
	}

	/**
	 * Returns the width of the table, that is, the maximum number
	 * of columns in the table.
	 *
	 * @return	the table width
	 */
	public int getWidth() {
		return cells[0].length;
	}

	/**
	 * Returns the height of the table, that is, the number of rows
	 * in the table.
	 *
	 * @return	the table height
	 */
	public int getHeight() {
		return cells.length;
	}

	/**
	 * @see		#getCaption()
	 */
	public String getTitle() {
		return caption;
	}

	/**
	 * Returns the table's summary
	 *
	 * @return	the table's sumamry
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * Sets the table's summary. Also sets the appropriate attribute
	 * in the DOM element.
	 *
	 * @param s	the table summary
	 */
	public void setSummary(String s) {
		summary = s;
		domElement.setAttribute("summary",summary);
	}

	/**
	 * Returns the table's caption
	 *
	 * @return	the table's caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * Sets the table's caption. Also adds or updates the appropriate element
	 * in the table's DOM element.
	 *
	 * @param cap	the new caption
	 */
	public void setCaption(String cap) {
		caption = cap;
		// create a new caption element
		org.w3c.dom.Element c = domElement.getOwnerDocument().createElement("caption");
		c.appendChild(domElement.getOwnerDocument().createTextNode(cap));
		// remove existing caption
		for (int i=0; i<domElement.getChildNodes().getLength(); i++) {
			if (domElement.getChildNodes().item(i).getNodeName().equals("caption")) {
				domElement.removeChild(domElement.getChildNodes().item(i));
			}
		}
		// else append the child
		domElement.insertBefore(c, domElement.getChildNodes().item(0));
	}

	/**
	 * Returns an array of header cells in this table.
	 *
	 * @return	the array of header cells.
	 */
	public HeaderCell[] getHeaders() {
		Vector v = new Vector();
                for (int i=0; i<cells.length; i++) {
                        for (int j=0; j<cells[i].length; j++) {
				if ((cells[i][j] != null) && (cells[i][j].isHeader())) {
					v.add((HeaderCell)cells[i][j]);
				}
			}
		}
		HeaderCell[] h = new HeaderCell[v.size()];
		for (int i=0; i<h.length; i++) {
			h[i] = (HeaderCell)v.elementAt(i);
		}
		return h;
	}

	/**
	 * Checks that the required accessibility features exist in this table.
	 * Returns an positive number if there are features missing, or 0 if
	 * the table is ok.
	 *
	 * @return	0 if ok, otherwise a bitmasked int
	 */
	public int isAccessible() {
		int t = 0;
		
		// 1) all tables should have a summary
		if (summary.equals("")) t += TABLE_MISSING_SUMMARY;
		
		// 2) all tables should have a caption
		if (caption.equals("")) t += TABLE_MISSING_CAPTION;
		
		// 3) check for headers > 10 chars without abbrev
		HeaderCell[] hc = this.getHeaders();
		for (int i=0; i<hc.length; i++) {
			if ((hc[i].getTextContent().length() > 10) &&
					(hc[i].getAbbr().equals(""))) {
				t += HEADER_MISSING_ABBREVIATION;
				break;
			}
		}

		// 4) check for data cells with content but no headers
		cellrow: for (int i=0; i<cells.length; i++) {
			for (int j=0; j<cells[i].length; j++) {
				if ((cells[i][j] != null) && 
						(!cells[i][j].isHeader()) &&
						(!cells[i][j].hasHeader()) &&
						(!cells[i][j].getTextContent().equals(""))) {
					t += CELL_MISSING_HEADER;
					break cellrow;
				}
			}
		}
		
		return t;
	}

	/**
	 * Prints a textual representation of the table's structure to 
	 * standard out. Header cells are indicated by a 'h', spanned cells
	 * are indicated by a '-' and other cells by 'c'.
	 */
	public void dumpStructure() {
		for (int i=0; i<this.getHeight(); i++) {
			for (int j=0; j<this.getWidth(); j++) {
				if (cells[i][j].isHeader()) {
					System.err.print("h");
				} else if (cells[i][j].isNull()) {
					System.err.print("-");
				} else {
					System.err.print("c");
				}
			}
			System.err.println();
		}
	}

}


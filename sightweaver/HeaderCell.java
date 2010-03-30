package sightweaver;

import javax.swing.*;
import javax.swing.text.*;

/**
 * HeaderCell extends the base Cell and represents a header cell; that is
 * a cell that represents a logical header cell in the table.
 * <p>
 * The header cell class also stores the cell's abbreviation and has a
 * method to convert the cell to a data cell.
 *
 * @author	David McNamara
 * @version	$Id: HeaderCell.java 176 2003-04-04 12:50:54Z mackers $
 */
public class HeaderCell extends Cell {

	protected String abbr;

	/**
	 * Creates a header cell with defaults.
	 */
	public HeaderCell() {
		super();
		abbr = "";
	}

	/**
	 * Returns the text content of this cell. If the header has
	 * an ID or abbreviation defined, then this will be used instead.
	 *
	 * @return	a string reprentation of this cell
	 */
	public String toString() {
		if ((abbr != null) && (!abbr.equals(""))) {
			return abbr;
		} else if (!getTextContent().equals("")){
			return getTextContent();
		} else {
			return id;
		}
	}
	
	protected void updateAtts() {
		super.updateAtts();
		if (!abbr.equals("")) this.setAttribute("abbr",abbr);
	}
	
	protected MutableAttributeSet getAttributeSet() {
		MutableAttributeSet attr = new SimpleAttributeSet();
		// headers are bold
		StyleConstants.setBold(attr, true);
		return attr;
	}
	
	/**
	 * Returns whether this cell is a header or not, which it is.
	 *
	 * @return	<code>true</code>
	 */
	public boolean isHeader() {
		return true;
	}

	/**
	 * Returns this header cell's abbreviation
	 *
	 * @return	the header cell's abbreviation
	 */
	public String getAbbr() {
		return abbr;
	}

	/**
	 * Set this cell's abbreviation
	 *
	 * @param a	the abbreviation string
	 */
	public void setAbbr(String a) {
		abbr = a;
		this.updateAtts();
	}
		
	/**
	 * Clones this cell as a data cell and returns it.  The cloned cell
	 * has the same attributes, headers, and domelement. It should be noted
	 * that the original cell's position in the DOM tree is replaced with
	 * the new data cell.
	 *
	 * @return	the new data cell
	 * @see		sightweaver.Cell
	 */               
	public Cell makeDataCell() {
                // create a new header cell
                Cell c = new Cell();
                // copy over the attributes
                c.setContent(this.getContent());
                c.setSelected(this.getSelected());
                c.setAxis(this.getAxis());
		c.setID(this.getID());
                for (int i=0; i<headers.length; i++) {
                        if (headers[i] != null) {
                                try {
                                        c.addHeader(headers[i]);
                                } catch (HeadersFullException e) {}
                        }
                }
                if (domElement != null) {
                        // replace this cell's "td" element with a "th" element
                        c.setElement(SWUtil.changeTagName(domElement, "td"));
                }
                // update the cell's child cells to point to the new headercell
                for (int i=0; i<childCells.length; i++) {
                        if (childCells[i] != null) {
                                childCells[i].setParentCell(c);
                        }
                }

                // return the new headercell
                return c;
	}
}

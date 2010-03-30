package sightweaver;

import org.w3c.dom.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.Color;

/**
 * Cell is the base class for all types of table cells and represents
 * a single cell in a table containing a grid of such cells.
 * <p>
 * Each cell stores both an internal representation of itself and a 
 * org.w3c.dom.Element representation, and these are synchronized with each 
 * attribute set operation. If this cell spans multiple rows or columns, 
 * then references are stored to all of the related cells. Methods are
 * also provided to transform cells into different cell types.
 * <p>
 * The cell class also contains a method to return a graphical component
 * representation of the cell should a graphical user interface require this.
 * 
 * @author	David McNamara
 * @version	$Id: Cell.java 171 2003-04-03 17:06:24Z mackers $
 */
public class Cell {

	protected String id;
	protected String axis;
	protected Cell[] headers;
	protected NullCell[] childCells;
	
	protected boolean selected;
	protected String text;
	protected org.w3c.dom.Element domElement;
	protected org.w3c.dom.NodeList content;
	protected Color selBGColor = new Color(192,192,255);
	protected Color defBGColor = Color.white;

	/**
	 * Creates a data cell with no headers or child cells and default
	 * attributes. The org.w3c.dom.Element must be provided later using
	 * the setElement method.
	 */
	public Cell() {
		headers = new Cell[10];
		childCells = new NullCell[20];
		id = "";
		axis = "";
	}

	protected void updateAtts() {
		if (domElement == null) return;
		if (!id.equals("")) this.setAttribute("id",id);
		if (!axis.equals("")) this.setAttribute("axis",axis);
		if (this.hasHeader()) this.setAttribute("headers",this.getHeaderString(" ", false));
	}

	/**
	 * Sets the content of this cell; that is, all child nodes in the DOM
	 * tree.
	 *
	 * @param nl	the NodeList containing the child nodes
	 * @see		org.w3c.dom.NodeList
	 */
	public void setContent(org.w3c.dom.NodeList nl) {
		content = nl;
	}

	/**
	 * Returns the contents of this cell as a nodelist.
	 *
	 * @return 	a nodelist containing the childnodes of this cell
	 * @see		org.w3c.dom.NodeList
	 */
	public org.w3c.dom.NodeList getContent() {
		return content;
	}

	/**
	 * Sets the org.w3c.Element representing this cell in the
	 * document's DOM.
	 *
	 * @param e	the element
	 * @see		org.w3c.dom.Element
	 */
	public void setElement(org.w3c.dom.Element e) {
		domElement = e;
	}

	/**
	 * Returns the org.w3c.Element representing this cell in the
	 * document's DOM.
	 *
	 * @return	the element
	 * @see		org.w3c.dom.Element
	 */
	public org.w3c.dom.Element getElement() {
		return domElement;
	}

	/**
	 * Sets a DOM attribute in this cell's DOM element. If the 
	 * attribute already exists, it will be overriden with
	 * the new value.
	 *
	 * @param n	the name of the attribute
	 * @param v	the value of the attribute
	 */
	public void setAttribute(String n, String v) {
		if (domElement != null) 
			domElement.setAttribute(n,v);
	}

	/**
	 * Returns an specified attribute of this cell's DOM
	 * element. An empty string will be returned if the
	 * attribute does not exist.
	 * 
	 * @param n	the name of the attribute
	 * @return	the value of the attribute
	 */
	public String getAttribute(String attName) {
		if (domElement == null) 
			return "";
		else
			return domElement.getAttribute(attName);
	}

	/**
	 * Returns a textual representation of this cell, which 
	 * is a concatenated string containing the value of all
	 * text nodes in this Cell's content.
	 *
	 * @return	the cell's text content
	 */
	public String getTextContent() {
		if (text == null) {
			text = SWUtil.textFromElement(domElement);
		}
		return text;
	}

	/**
	 * @see		#getTextContent()
	 */
	public String toString() {
		return this.getTextContent();
	}

	/**
	 * Returns a graphical representation of this cell. For data
	 * cells this is a swing component containing the textual
	 * content of the cell. A different background color will
	 * be used if the cell is selected.
	 *
	 * @return	a swing component representing the cell
	 * @see		javax.swing.JComponent
	 */
	public javax.swing.JComponent getComponent() {
		// create the jpanel for this component
		JPanel jp = new JPanel();
		// extend the panel to the edge of the cell
		jp.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
		// set the background color if selected
		if (selected) {
			jp.setBackground(selBGColor);
		} else {
			jp.setBackground(defBGColor);
		}
		// create a styled document
		StyledDocument dsd = new DefaultStyledDocument();
		// get this class's style
		MutableAttributeSet attr = this.getAttributeSet();
		// insert the text content of this cell
		try {
			dsd.insertString(0,this.getTextContent(),attr);
		} catch (BadLocationException e) {
		}
		// create a text pane with the style of this class (or subclass)
		JTextPane jtp = new JTextPane(dsd);
		// set the border
		jtp.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
		// we want to see the selection color
		jtp.setOpaque(false);
		// add to panel
		jp.add(jtp);
		// return the component
		return (JComponent)jp;
	}

	protected MutableAttributeSet getAttributeSet() {
		MutableAttributeSet attr = new SimpleAttributeSet();
		return attr;
	}

	/**
	 * Sets this cell's selection status to selected or not selected. 
	 * In a graphical context, a selected cell is a cell that has been
	 * clicked on or otherwise indicated and should be render differently
	 * to provide feedback to the user.
	 *
	 * @param s	the cell's selection status
	 */
	public void setSelected(boolean s) {
		selected = s;
	}

	/**
	 * Returns the selection status of this cell.
	 *
	 * @return	the selection status of this cell
	 */
	public boolean getSelected() {
		return selected;
	}

	/**
	 * Returns if this cell is a null cell or not. The NullCell
	 * class is a specialized cell representing cells that are being 
	 * spanned by another cell.
	 *
	 * @return	is this cell a null cell or not
	 * @see		sightweaver.NullCell
	 */
	public boolean isNull() {
		return false;
	}

	/**
	 * Returns if this cell is a header cell or not. The HeaderCell
	 * class is a specialized cell representing header cells.
	 *
	 * @return	is this cell a header cell or not
	 * @see		sightweaver.HeaderCell
	 */
	public boolean isHeader() {
		return false;
	}
	
	/**
	 * Returns this cell's ID.
	 *
	 * @return	this cell's ID
	 */
	public String getID() {
		return id;
	}

	/**
	 * Sets this cell's ID. The element's attribute is also updated.
	 *
	 * @param i	the cell's ID
	 */
	public void setID(String i) {
		id = i;
		this.updateAtts();
	}

	/**
	 * Returns the cell's axis.
	 *
	 * @return	the cell's axis.
	 */
	public String getAxis() {
		return axis;
	}
	
	/**
	 * Sets the cell's axis. The element's attribute is also updated.
	 *
	 * @param a	the cell's axis
	 */
	public void setAxis(String a) {
		axis = a;
		this.updateAtts();
	}
	
	/**
	 * Adds a header to this cell's list of headers. The element's
	 * attribute is also updated.
	 *
	 * @param h	the header cell to be added
	 * @throws	HeadersFullException if there already 10 headers
	 * @see		sightweaver.HeadersFullException
	 */
	public void addHeader(Cell h) throws HeadersFullException {
		int fp = -1;
		// check if in headers already
		for (int i=0; i<headers.length; i++) {
			if (headers[i] != null) {
				if (headers[i] == h) return;
			} else {
				fp = i;
			}
		}
		// check if full
		if (fp == -1) throw new HeadersFullException();
		// add header
		headers[fp] = h;
		// update attributes
		this.updateAtts();
	}

	/**
	 * Clears the list of headers for this cell.
	 */
	public void clearHeaders() {
		for (int i=0; i<headers.length; i++) {
			headers[i] = null;
		}
		this.updateAtts();
	}

	/**
	 * Returns whether this cell has any headers or not.
	 *
	 * @return	has this cell any headers
	 */
	public boolean hasHeader() {
		for (int i=0; i<headers.length; i++) {
			if (headers[i] != null) return true;
		}
		return false;
	}
	
	/**
	 * Returns an array of all this cell's headers. The
	 * size of the array will always be 10, the maximum
	 * number of headers, and may contain null references.
	 *
	 * @return	this cell's headers
	 */
	public Cell[] getHeaders() {
		return headers;
	}

	/**
	 * Returns a string representation of this cell's headers
	 * using a comma as the delimiter.
	 *
	 * @return	a string representation of this cell's headers
	 * @see		#getHeaderString(String,boolean)
	 */
	public String getHeaderString() {
		return this.getHeaderString(", ", true);
	}
	
	/**
	 * Returns a string representation of this cell's headers
	 * using a given delimiter. If the header cell contents text
	 * then this will be used. Otherwise, its ID is used, unless 
	 * <code>useTextContent</code> is set to false.
	 *
	 * @param delim			the delimiter between header cells
	 * @param useTextContent	use the text content instead of the ID
	 * @return			the string representation
	 */
	public String getHeaderString(String delim, boolean useTextContent) {
		String s = "";
		// add header text for all associated headers
		for (int i=0; i<headers.length; i++) {
			if (headers[i] != null) {
				if ((!useTextContent) || (headers[i].getTextContent().equals("")))
					s += headers[i].getID() + delim;
				else 
					s += headers[i].getTextContent() + delim;
			}
		}
		// chop last ", ", if necessary
		if (s.endsWith(delim)) {
			s = s.substring(0,s.length() - delim.length());
		}
		return s;
	}
	
	/**
	 * Clones this cell as a header cell and returns it. The cloned cell
	 * has the same attributes, headers, and domelement. It should be noted
	 * that the original cell's position in the DOM tree is replaced with
	 * the new header cell.
	 * 
	 * @return	the new header cell
	 * @see		sightweaver.HeaderCell
	 */
	public HeaderCell makeHeaderCell() {
		// create a new header cell
		HeaderCell h = new HeaderCell();
		// copy over the attributes
		h.setContent(this.getContent());
		h.setSelected(this.getSelected());
		h.setID(this.getID());
		h.setAxis(this.getAxis());
		for (int i=0; i<headers.length; i++) {
			if (headers[i] != null) {
				try {
					h.addHeader(headers[i]);
				} catch (HeadersFullException e) {}
			}
		}
		if (domElement != null) {
			// replace this cell's "td" element with a "th" element
			h.setElement(SWUtil.changeTagName(domElement, "th"));
		}
		// update the cell's child cells to point to the new headercell
		for (int i=0; i<childCells.length; i++) {
			if (childCells[i] != null) {
				childCells[i].setParentCell(h);
			}
		}
		
		// return the new headercell
		return h;
	}

	protected void addChild(NullCell c) {
		for (int i=0; i<childCells.length; i++) {
			if (childCells[i] == null) {
				childCells[i] = c;
				return;
			}
		}
	}
}

package sightweaver;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.Color;
import java.awt.Graphics;

/**
 * NullCell extends the base Cell class and represents a null cell; that
 * is a cell that is spanned by another cell.
 * <p>
 * This class includes all the same features as the the basic cell, however
 * some attributes such as the cells element and content don't make sense
 * in a null cell context.
 * <p>
 * The null cell has a parent cell attribute that is used to keep a reference
 * to the cell that is spanning it. The cell also returns a different 
 * component for graphical implementations.
 *
 * @author	David McNamara
 * @version	$Id: NullCell.java 171 2003-04-03 17:06:24Z mackers $
 */
public class NullCell extends Cell {

	private Cell parentCell;
	//protected Color defBGColor = Color.lightGray;
	protected Color defFGColor = Color.lightGray;

	/**
	 * Creates a null cell.
	 *
	 * @param p	this null cell's 'parent'
	 */
	public NullCell(Cell p) {
		parentCell = p;
		parentCell.addChild(this);
	}

	/**
	 * Returns the text content of this cell, which is an
	 * empty string.
	 *
	 * @return	the text content
	 */
	public String getTextContent() {
		return "";
	}
	
	/**
	 * Returns a graphical representation of this cell. For null
	 * cells this is a blank component with a gray diagonal line
	 * through it.
	 *
	 * @return	a swing component representing this cell.
	 * @see		javax.swing.JComponent
	 */
	public javax.swing.JComponent getComponent() {
		// create the jpanel for this component
		JPanel jp = new NullCellPanel();
		// extend the panel to the edge of the cell
		jp.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
		// set background color
		jp.setBackground(defBGColor);
		// set foreground color
		jp.setForeground(defFGColor);
		// return the component
		return (JComponent)jp;
	}

	/**
	 * Returns this null cell's parent cell.
	 *
	 * @return	this cell's parent cell
	 */
	public Cell getParentCell() {
		return parentCell;
	}

	/**
	 * Sets this null cell's parent cell.
	 *
	 * @param c	the parent cell
	 */
	public void setParentCell(Cell c) {
		parentCell = c;
	}

	/**
	 * Returns if this cell is a null cell or not. Returns <code>true</code>
	 * for this cell type.
	 *
	 * @return	true for null cells
	 */
	public boolean isNull() {
		return true;
	}
}

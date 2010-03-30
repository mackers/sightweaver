package sightweaver;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A public class to decide how a cell is rendered in the table. Unless the cell
 * doesn't exist, will return the cell's component.
 *
 * @author	David McNamara
 * @version	$Id: SWCellRenderer.java 175 2003-04-03 20:01:18Z mackers $
 */
public class SWCellRenderer implements TableCellRenderer {
	public Component getTableCellRendererComponent(JTable table, Object value, 
				boolean isSelected, boolean hasFocus,
				int row, int column) {
		//Cell vcell = ((SWJTableModel)table.getModel()).getCellAt(row, column);
		if (value != null) {
			Cell vcell = (Cell)value;
			return vcell.getComponent();
		} else {
			return null;
		}
	}
}

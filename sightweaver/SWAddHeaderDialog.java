package sightweaver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A dialog for adding headers to cells.
 *
 * @author	David McNamara
 * @version	$Id: SWAddHeaderDialog.java 199 2003-04-07 14:37:21Z mackers $
 */
public class SWAddHeaderDialog extends SWAbstractDialog implements ActionListener {

	JComboBox comboBox;
	HeaderCell[] headers;
	
	/**
	 * Constructs the dialog and adds the specified headers.
	 *
	 * @param owner	the owning frame
	 * @param h	an array of headers in the document to add
	 */
	public SWAddHeaderDialog (JFrame owner, HeaderCell[] h) {
		// create a dialog
		super(owner, SWUtil.getString("addHeader"));
		this.getRootPane().setPreferredSize(new Dimension(400,110));
		headers = h;
		
		// create the strings array to display in the combo box
		String[] s = new String[headers.length];
		for (int i=0; i<headers.length; i++) {
			s[i] = headers[i].toString();
		}

		// add the components
		JPanel cPanel = new JPanel();
		cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));
		cPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		comboBox = new JComboBox(s);
		comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel l = new JLabel(SWUtil.getString("addHeaderExpl"));
		l.setLabelFor(comboBox);
		l.setAlignmentX(Component.LEFT_ALIGNMENT);
		cPanel.add(l);
		cPanel.add(Box.createRigidArea(new Dimension(0,5)));
		cPanel.add(comboBox);
		this.getContentPane().add(cPanel, BorderLayout.CENTER);

		// register the action listeners
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
	}

	/**
	 * Returns the header selected by the user
	 * 
	 * @return	the header selected by the user
	 */
	public HeaderCell getValue() {
		return headers[comboBox.getSelectedIndex()];
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancelButton) {
			cancelled = true;
			this.dispose();
		} else if (e.getSource() == okButton) {
			// ok button checks here
			cancelled = false;
			this.dispose();
		}
	}
}

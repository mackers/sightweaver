package sightweaver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A dialog to allow the user to edit a header cell's attributes.
 * 
 * @author	David McNamara
 * @version	$Id: SWEditHeaderInfoDialog.java 199 2003-04-07 14:37:21Z mackers $
 */
public class SWEditHeaderInfoDialog extends SWAbstractDialog implements ActionListener {

	JTextField idTextBox, abbrTextBox;
	
	/**
	 * Creates the dialog with the specified ID and abbreviation.
	 *
	 * @param owner	the owning frame
	 * @param id	the id to use
	 * @param abbr	the abbreviation to use
	 */
	public SWEditHeaderInfoDialog (JFrame owner, String id, String abbr) {
		// create a dialog
		super(owner, SWUtil.getString("editHeaderInfo"));
		this.getRootPane().setPreferredSize(new Dimension(400,200));

		// id panel
		JPanel idPanel = new JPanel();
		idPanel.setLayout(new BoxLayout(idPanel, BoxLayout.X_AXIS));
		idPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		idPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		idTextBox = new JTextField(id, 10);
		JLabel l = new JLabel(SWUtil.getString("id"));
		l.setLabelFor(idTextBox);
		l.setAlignmentX(Component.LEFT_ALIGNMENT);
		idPanel.add(l);
		idPanel.add(Box.createRigidArea(new Dimension(5,0)));
		idPanel.add(idTextBox);

		// abbr panel
		JPanel abbrPanel = new JPanel();
		abbrPanel.setLayout(new BoxLayout(abbrPanel, BoxLayout.X_AXIS));
		abbrPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		abbrPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		abbrTextBox = new JTextField(abbr, 10);
		JLabel l2 = new JLabel(SWUtil.getString("abbreviation"));
		l2.setLabelFor(abbrTextBox);
		l2.setAlignmentX(Component.LEFT_ALIGNMENT);
		abbrPanel.add(l2);
		abbrPanel.add(Box.createRigidArea(new Dimension(5,0)));
		abbrPanel.add(abbrTextBox);

		// add these to the main panel
		JPanel cPanel = new JPanel();
		cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));
		cPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		cPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		cPanel.add(new JLabel(SWUtil.getString("editIDExpl")));
		cPanel.add(Box.createRigidArea(new Dimension(5,0)));
		cPanel.add(idPanel);
		cPanel.add(new JLabel(SWUtil.getString("editAbbrExpl")));
		cPanel.add(Box.createRigidArea(new Dimension(5,0)));
		cPanel.add(abbrPanel);
		
		this.getContentPane().add(cPanel, BorderLayout.CENTER);

		// register the action listeners
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
	}

	/** 
	 * Returns the id the user specified.
	 *
	 * @return	the id the user specified
	 */
	public String getIDValue() {
		return idTextBox.getText();
	}

	/**
	 * Returns the abbreviation the user specified.
	 *
	 * @return	the abbreviation the user specified
	 */
	public String getAbbrValue() {
		return abbrTextBox.getText();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancelButton) {
			cancelled = true;
			this.dispose();
		} else if (e.getSource() == okButton) {
			// ok button checks here
			if (idTextBox.getText().length() > 10) {
				JOptionPane.showMessageDialog(this,
					SWUtil.getString("idTooLong"),
					SWUtil.getString("appName"),
					JOptionPane.ERROR_MESSAGE);
				return;
			} else if (abbrTextBox.getText().length() > 10) {
				JOptionPane.showMessageDialog(this,
					SWUtil.getString("abbrTooLong"),
					SWUtil.getString("appName"),
					JOptionPane.ERROR_MESSAGE);
				return;
			}
			cancelled = false;
			this.dispose();
		}
	}
}

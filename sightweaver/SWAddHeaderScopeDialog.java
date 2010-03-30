package sightweaver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A dialog to add header scope.
 *
 * @author	David McNamara
 * @version	$Id: SWAddHeaderScopeDialog.java 199 2003-04-07 14:37:21Z mackers $
 */
public class SWAddHeaderScopeDialog extends SWAbstractDialog implements ActionListener {

	JComboBox comboBox;

	/**
	 * Constructs the dialog.
	 *
	 * @param owner	the owning frame
	 */
	public SWAddHeaderScopeDialog (JFrame owner) {
		// create a dialog
		super(owner, SWUtil.getString("addHeaderScope"));
		this.getRootPane().setPreferredSize(new Dimension(400,125));

		// add the components
		JPanel cPanel = new JPanel();
		cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));
		cPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		String[] options = {SWUtil.getString("row"),SWUtil.getString("column")};
		comboBox = new JComboBox(options);
		comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel l = new JLabel(SWUtil.getString("addHeaderScopeExpl"));
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

	/*
	 * Returns what scope the user selected.
	 *
	 * @return	the scope the user selected
	 */
	public String getValue() {
		return (String)comboBox.getSelectedItem();
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

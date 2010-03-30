package sightweaver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.StringTokenizer;

/**
 * A dialog to allow the user to edit a cell's axis.
 *
 * @author	David McNamara
 * @version	$Id: SWEditAxisDialog.java 199 2003-04-07 14:37:21Z mackers $
 */
public class SWEditAxisDialog extends SWAbstractDialog implements ActionListener {

	JTextField textBox;

	/**
	 * Create the dialog with the specified axis as the default string.
	 *
	 * @param owner the owning frame
	 * @param axis	the default axis
	 */
	public SWEditAxisDialog (JFrame owner, String axis) {
		// create a dialog
		super(owner, SWUtil.getString("editAxis"));
		this.getRootPane().setPreferredSize(new Dimension(400,110));

		// add the components
		JPanel cPanel = new JPanel();
		cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));
		cPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		cPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		textBox = new JTextField(axis,25);
		JLabel l = new JLabel(SWUtil.getString("editAxisExpl"));
		l.setLabelFor(textBox);
		l.setAlignmentX(Component.LEFT_ALIGNMENT);
		cPanel.add(l);
		cPanel.add(Box.createRigidArea(new Dimension(0,5)));
		cPanel.add(textBox);
		this.getContentPane().add(cPanel, BorderLayout.CENTER);

		// register the action listeners
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
	}

	/**
	 * Returns the axis that the user has selected.
	 *
	 * @return	the axis that the user has selected.
	 */
	public String getValue() {
		return textBox.getText();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancelButton) {
			cancelled = true;
			this.dispose();
		} else if (e.getSource() == okButton) {
			// ok button checks here
			StringTokenizer st = new StringTokenizer(textBox.getText(),",");
			while (st.hasMoreTokens()) {
				String t = st.nextToken();
				if (t.length() > 20) {
					// is too long
					JOptionPane.showMessageDialog(this,
						SWUtil.getString("axisTooLong") + " " + t,
						SWUtil.getString("appName"),
						JOptionPane.ERROR_MESSAGE);
					return;
				} else if (t.matches("[\\W]")) {
					// TODO: test this
					// has punctuation
					JOptionPane.showMessageDialog(this,
						SWUtil.getString("axisNotValid") + " " + t,
						SWUtil.getString("appName"),
						JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			cancelled = false;
			this.dispose();
		}
	}
}

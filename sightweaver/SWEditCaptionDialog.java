package sightweaver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A dialog to allow the user to edit a table caption
 */
public class SWEditCaptionDialog extends SWAbstractDialog implements ActionListener {

	JTextField textBox;

	/**
	 * Creates the dialog with the specified caption.
	 *
	 * @param owner		the owning frame
	 * @param caption	the caption to use
	 */
	public SWEditCaptionDialog (JFrame owner, String caption) {
		// create a dialog
		super(owner, SWUtil.getString("editCaption"));
		this.getRootPane().setPreferredSize(new Dimension(400,110));

		// add the components
		JPanel cPanel = new JPanel();
		cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));
		cPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		cPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		textBox = new JTextField(caption,25);
		textBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel l = new JLabel(SWUtil.getString("editCaptionExpl"));
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
	 * Returns the caption that the user specified.
	 *
	 * @return	the caption the user specified.
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
			if (textBox.getText().length() > 240) {
				JOptionPane.showMessageDialog(this,
					SWUtil.getString("captionTooLong"),
					SWUtil.getString("appName"),
					JOptionPane.ERROR_MESSAGE);
				return;
			} else {
				cancelled = false;
				this.dispose();
			}
		}
	}
}

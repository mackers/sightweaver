package sightweaver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A dialog to allow the user to edit the table sumamry.
 *
 * @author	David McNamara
 * @version	$Id: SWEditSummaryDialog.java 199 2003-04-07 14:37:21Z mackers $
 */
public class SWEditSummaryDialog extends SWAbstractDialog implements ActionListener {

	JTextArea textBox;

	/**
	 * Create the dialog using the specified summary.
	 *
	 * @param owner		the owning frame
	 * @param summary	the caption to use
	 */
	public SWEditSummaryDialog (JFrame owner, String summary) {
		// create a dialog
		super(owner, SWUtil.getString("editSummary"));
		this.getRootPane().setPreferredSize(new Dimension(400,300));

		// add the components
		JPanel cPanel = new JPanel();
		cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));
		cPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		cPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		textBox = new JTextArea(summary);
		JScrollPane jsp = new JScrollPane(textBox);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.setPreferredSize(new Dimension(200, 80));
		jsp.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel l = new JLabel(SWUtil.getString("editSummaryExpl"));
		l.setLabelFor(textBox);
		l.setAlignmentX(Component.LEFT_ALIGNMENT);
		cPanel.add(l);
		cPanel.add(Box.createRigidArea(new Dimension(0,5)));
		cPanel.add(jsp);
		this.getContentPane().add(cPanel, BorderLayout.CENTER);

		// register the action listeners
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
	}

	/**
	 * Return the summary the user specified.
	 *
	 * @return	the summary the user specified
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
			if (textBox.getText().length() > 2000) {
				JOptionPane.showMessageDialog(this,
					SWUtil.getString("summaryTooLong"),
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

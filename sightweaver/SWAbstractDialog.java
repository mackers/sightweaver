package sightweaver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * SWAbstractDialog is the abstract class from which most
 * sightweaver dialogs are extended. This class sets
 * up the basic window behaviour and buttons for an
 * Ok/Cancel style dialog. Subclasses should add further
 * components.
 *
 * @author	David McNamara
 * @version	$Id: SWAbstractDialog.java 199 2003-04-07 14:37:21Z mackers $
 */
public abstract class SWAbstractDialog extends JDialog {

	protected JButton okButton, cancelButton;
	protected boolean cancelled;
	
	/**
	 * Creates a generic Ok/Cancel dialog with the specified
	 * owner frame and window title.
	 *
	 * @param owner	the owner frame
	 * @param title	the window title
	 */
	public SWAbstractDialog (JFrame owner, String title) {
		// create a dialog
		super(owner, title, true);

		// set some standard options
		this.setResizable(false);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setLocationRelativeTo(owner);
		this.getContentPane().setLayout(new BorderLayout());

		// add ok and cancel buttons to their own panel
		JPanel okCancelPanel = new JPanel();
		okCancelPanel.setLayout(new BoxLayout(okCancelPanel, BoxLayout.X_AXIS));
		okCancelPanel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
		okCancelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		okButton = new JButton(SWUtil.getString("ok"));
		okButton.setDefaultCapable(true);
		okButton.setMnemonic(KeyEvent.VK_O);
		cancelButton = new JButton(SWUtil.getString("cancel"));
		cancelButton.setMnemonic(KeyEvent.VK_C);
		okCancelPanel.add(okButton);
		okCancelPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		okCancelPanel.add(cancelButton);
		this.getRootPane().setDefaultButton(okButton);
		this.getContentPane().add(okCancelPanel, BorderLayout.SOUTH);

		// TODO: nicer button layout (bug 0004)

		// escape key handling
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				hide();
		}};
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		this.getRootPane().registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

	}

	/**
	 * Shows the dialog and returns the user reponse. 
	 *
	 * @return	true if the user clicked ok, false otherwise
	 */
	public boolean showDialog() {
		this.pack();
		this.show();
		return !cancelled;
	}

}

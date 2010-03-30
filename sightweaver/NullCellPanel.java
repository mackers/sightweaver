package sightweaver;

/** 
 * The component that a <code>NullCell</code> returns to a Graphical 
 * User Agent's request for its table component.
 *
 * @author	David McNamara
 * @version	$Id: NullCellPanel.java 174 2003-04-03 19:58:16Z mackers $
 */
public class NullCellPanel extends javax.swing.JPanel {

    public void paintComponent(java.awt.Graphics g) {
	            super.paintComponent(g);
		    g.drawLine(0,super.getHeight(),super.getWidth(),0);
    }
}

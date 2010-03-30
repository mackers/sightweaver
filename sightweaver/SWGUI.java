package sightweaver;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The main class for the graphical user interface. Creates a single 
 * <code>SWJFrame</code>
 *
 * @author	David McNamara
 * @version	$Id: SWGUI.java 175 2003-04-03 20:01:18Z mackers $
 */
public class SWGUI {

	private static JFrame guiFrame;

	/**
	 * The main method creates a new <code>SWJFrame</code> window.
	 *
	 * @param args	Not used
	 */
	public static void main(String[] args) { 
		guiFrame = new SWJFrame();
	} 
}

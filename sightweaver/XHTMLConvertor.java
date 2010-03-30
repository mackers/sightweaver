package sightweaver;

import java.io.*;
import org.w3c.dom.*;

/**
 * This interface defines methods for classes that will
 * convert HTML to XHTML.
 * 
 * @author	David McNamara
 * @version	$Id: XHTMLConvertor.java 178 2003-04-04 13:58:04Z mackers $
 */
public interface XHTMLConvertor {

	/**
	 * Sets the HTML to be converted.
	 *
	 * @param s	the HTML to be converted.
	 */
	public void setHTML(String s);

	/**
	 * Performs the conversion on the HTML that was set with
	 * <code>setHTML</code>.
	 *
	 * @return	a DOM document representation of the XHTML 
	 * @see		org.w3c.dom.Document
	 */
	public org.w3c.dom.Document convert() throws ParseException;

}

package sightweaver;

import java.io.*;
import org.w3c.dom.*;

/**
 * An interface for importer classes.
 * <p>
 * The interface declares methods for common methods to
 * be implemented by HTML, CSV, etc. importer classes that 
 * want to be used as an import filter for importing
 * documents.
 *
 * @author	David McNamara
 * @version	$Id: Importer.java 173 2003-04-03 18:53:01Z mackers $
 */
public interface Importer {
	
	/**
	 * Perform the main parsing routine on the inputstream
	 * that has been set with <code>setInputStream(InputStream)</code>
	 *
	 * @throws	ParseException if the input stream can't be parsed
	 */
	public void parse() throws ParseException;

	/**
	 * Sets the input stream that this importer will retrieve the
	 * document to be imported from.
	 *
	 * @param is	the input stream
	 */
	public void setInputStream(InputStream is);

	/**
	 * After a successful <code>parse()</code>, the document can
	 * be retrieved with the getDomDocument method.
	 *
	 * @return	the DOM document
	 * @see		org.w3c.dom.Document
	 */
	public org.w3c.dom.Document getDomDocument();
	
	/**
	 * After a successful <code>parse()</code> the table objects can
	 * be retrieved with this method.
	 *
	 * @return	an array of tables
	 * @see		sightweaver.Table
	 */
	public Table[] getTables() throws ImportException;

	/**
	 * Gets the document title
	 *
	 * @return	the document title
	 */
	public String getTitle();
}

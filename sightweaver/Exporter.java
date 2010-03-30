package sightweaver;

import java.io.File;

/**
 * An interface for Exporter classes.
 * <p>
 * Exporter classes should allow a document to be exported to a file
 * or the document source to be exported to a string. They should 
 * implement a constructor that takes a sightweaver document as a
 * parameter and stores it until one of the export functions is
 * called.
 * <p>
 * Implementing classes can assume that the document is well-formed
 * and has been checked for all neccessary accessibility information.
 * <p>
 * Possible exporter types could be HTML, XHTML or RDF.
 * 
 * @author	David McNamara
 * @version	$Id: Exporter.java 178 2003-04-04 13:58:04Z mackers $
 */
public interface Exporter {

	/**
	 * Exports the document to a file.
	 *
	 * @param f	the file to export to.
	 */
	public void exportToFile(File f) throws ExportException;

	/**
	 * Exports the document to a string.
	 *
	 * @return	a string form of the document
	 */
	public String exportToString() throws ExportException;
}

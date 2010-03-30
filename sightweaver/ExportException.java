package sightweaver;

/**
 * An ExportException is thrown when the document cannot be exported
 * because of a filesystem error or because of a problem with the
 * accessibility of the document.
 *
 * @author	David McNamara
 * @version	$Id: ExportException.java 174 2003-04-03 19:58:16Z mackers $
 */
public class ExportException extends Exception {
	public ExportException() { }
	public ExportException(String s) { 
		super(s);
	}
}

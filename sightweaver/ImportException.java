package sightweaver;

/**
 * An ImportException is thrown when there is a problem with the
 * file that has been selected to be imported. For example, if
 * the file cannot be read or if no suitable filter was found.
 * <p>
 * Note that a problem with the actual contents of the file throws
 * a <code>ParseException</code>.
 * 
 * @author	David McNamara
 * @version	$Id: ImportException.java 175 2003-04-03 20:01:18Z mackers $
 * 
 */
public class ImportException extends Exception {
	public ImportException() { }
	public ImportException(String s) { 
		super(s);
	}
}

package sightweaver;

/**
 * A ParseException is thrown when there is a problem
 * with the contents of a file that is being imported.
 * <p>
 * An example of which may be non-well-formed XML in
 * the XHTML importer or missing tables in any format.
 *
 * @author	David McNamara
 * @version	$Id: ParseException.java 175 2003-04-03 20:01:18Z mackers $
 */
public class ParseException extends Exception {
	public ParseException() { }
	public ParseException(String s) {
		super(s);
	}
}

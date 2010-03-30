package sightweaver;

/**
 * A HeadersFullException is thrown when a cell has
 * its maximum number of headers and another header
 * is attempted to be added.
 *
 * @author	David McNamara
 * @version	$Id: HeadersFullException.java 173 2003-04-03 18:53:01Z mackers $
 */
public class HeadersFullException extends Exception {

	/**
	 * Empty constuctor
	 */
	public HeadersFullException() { }

	/**
	 * Construct the exception with the supplied
	 * message.
	 * 
	 * @param s	the exception message
	 */
	public HeadersFullException(String s) { 
		super(s);
	}
}

package sightweaver;

/**
 * This interface declares functions for classes used to 
 * preparse HTML made by specific programs.
 *
 * @author	David McNamara
 * @version	$Id: HTMLPreParser.java 178 2003-04-04 13:58:04Z mackers $
 */
public interface HTMLPreParser {

	/**
	 * This sole method should be implemented to
	 * perform preparsing on a HTML string.
	 *
	 * @param s	the HTML to preparse
	 * @return	the preparsed HTML
	 */
	public String preParse(String s);
}

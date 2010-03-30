package sightweaver;

/**
 * Preparses generic HTML files. Actually just returns the same HTML.
 *
 * @author      David McNamara
 * @version     $Id: NullPreParser.java 175 2003-04-03 20:01:18Z mackers $
*/
public class NullPreParser implements HTMLPreParser {
	public String preParse(String s) {
		 return s;
	}
}

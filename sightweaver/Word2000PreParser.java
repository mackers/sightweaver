package sightweaver;

/**
 * Preparses HTML files made in Word 2000.
 *
 * @author      David McNamara
 * @version     $Id: Word2000PreParser.java 245 2003-04-14 21:03:38Z mackers $
*/
public class Word2000PreParser implements HTMLPreParser {
	public String preParse(String s) {
		//s = s.replaceAll("xmlns:o=\"urn:schemas-microsoft-com:office:office\"", "");
		//s = s.replaceAll("xmlns:w=\"urn:schemas-microsoft-com:office:word\"", "");
		return s;
	}
}

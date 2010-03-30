package sightweaver;

/**
 * Preparses HTML files made in Word 97.
 *
 * @author      David McNamara
 * @version     $Id: Word97PreParser.java 253 2003-04-15 11:50:05Z mackers $
*/
public class Word97PreParser implements HTMLPreParser {
	public String preParse(String s) {
		s = s.replaceAll("VALIGN=\"TOP\"","valign=\"top\"");
		s = s.replaceAll("VALIGN=\"MIDDLE\"","valign=\"middle\"");
		s = s.replaceAll("BORDERCOLOR=\"#......\"","");
		return s;
	}
}

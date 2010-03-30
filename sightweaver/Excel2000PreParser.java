package sightweaver;

/**
 * Preparses HTML files made in Excel 2000.
 *
 * @author      David McNamara
 * @version     $Id: Excel2000PreParser.java 242 2003-04-14 20:43:29Z mackers $
*/
public class Excel2000PreParser implements HTMLPreParser {
	public String preParse(String s) {
                s = s.replaceAll("xmlns:o=\"urn:schemas-microsoft-com:office:office\"", "");
		s = s.replaceAll("xmlns:x=\"urn:schemas-microsoft-com:office:excel\"","");
		return s;
	}
}

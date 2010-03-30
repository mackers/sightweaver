package sightweaver;

/**
 * Preparses HTML files made in Word XP.
 *
 * @author      David McNamara
 * @version     $Id: WordXPPreParser.java 242 2003-04-14 20:43:29Z mackers $
*/
public class WordXPPreParser implements HTMLPreParser { 
	public String preParse(String s) {
		s = s.replaceAll("xmlns:o=\"urn:schemas-microsoft-com:office:office\"", "");
		s = s.replaceAll("xmlns:w=\"urn:schemas-microsoft-com:office:word\"", "");
		s = s.replaceAll("</?o:p>","");
		 return s;
	}
}

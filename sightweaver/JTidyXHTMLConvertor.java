package sightweaver;

import java.io.*;
import org.w3c.dom.*;
import org.w3c.tidy.Tidy;
import java.util.Properties;

/**
 * JTidyXHTMLConvertor converts HTML to XHTML using the JTidy program.
 *
 * @author	David McNamara
 * @version	$Id: JTidyXHTMLConvertor.java 178 2003-04-04 13:58:04Z mackers $
 */
public class JTidyXHTMLConvertor implements XHTMLConvertor {

	String html;

	public void setHTML(String s) {
		html = s;
	}

        public org.w3c.dom.Document convert() throws ParseException {
		if (html == null) throw new ParseException(SWUtil.getString("internalError"));
		Tidy tidy = new Tidy();

		// read the configuration from the props file
		try {
			Properties p = new Properties();
			p.load(ClassLoader.getSystemResourceAsStream("sightweaver/jtidy.properties"));
			tidy.setConfigurationFromProps(p);
		} catch (Exception e) {
			throw new ParseException(SWUtil.getString("noJTidyConfiguration"));
		}
		
		// set some mandatory properties
		tidy.setXHTML(true);
		
		// make an input stream from this string
		InputStream is = new ByteArrayInputStream(html.getBytes());
		
		// parse using tidy
		org.w3c.dom.Document doc = tidy.parseDOM(is, null); 

		// check for errors
		if (tidy.getParseErrors() > 0) {
			throw new ParseException(SWUtil.getString("xhtmlConvertorError"));
		}

		return doc;
	}

}

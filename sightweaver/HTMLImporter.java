package sightweaver;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import java.io.*;

/**
 * Implements an <code>Importer</code> to import (non-XHTML) HTML files.
 * <p>
 * <code>Importer</code>'s parse method is implemented with a HTML to XHTML
 * converter. <code>XHTMLImporter</code>'s methods then do the necessary
 * work to import the XHTML into a usable form.
 *
 * @author	David McNamara
 * @version	$Id: HTMLImporter.java 253 2003-04-15 11:50:05Z mackers $
 */
public class HTMLImporter extends XHTMLImporter {

	XHTMLConvertor xhtmlConvertor;
	HTMLPreParser htmlPP;

	public void parse() throws ParseException {
                if (inputStream == null) throw new ParseException(SWUtil.getString("internalError"));
		
		// convert the inputstream to a string
		String s = "";
		try {
			//inputStream.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			String line = "";
			while ( (line = in.readLine()) != null) {
				s += line;
			}
		} catch (Exception e) {
			throw new ParseException(SWUtil.getString("readError"));
		}

		// check is actually HTML
		if (!SWUtil.isHTML(s)) {
			throw new ParseException(SWUtil.getString("notHTML"));
		}
		
		// create the preparser 
		String gen = SWUtil.getGenerator(s);
		if (gen.equals("Microsoft Word 97")) {		// PC WORD 97
			htmlPP = new Word97PreParser();
		} else if (gen.equals("Microsoft Word 9")) {	// PC WORD 2000
			htmlPP = new Word2000PreParser();
		} else if (gen.equals("Microsoft Word 10")) {	// PC WORD XP
			htmlPP = new WordXPPreParser();
		} else if (gen.equals("Microsoft Excel 9")) {	// PC EXCEL 2000
			htmlPP = new Excel2000PreParser();
		} else {
			htmlPP = new NullPreParser();
		}
		
		// run it through the preparser
		String spp = htmlPP.preParse(s);

		// create the xhtmlconvertor
		try {
			xhtmlConvertor = (XHTMLConvertor)Class.forName(SWUtil.getString("XHTMLConvertor")).newInstance();
		} catch (Exception e) {
			throw new ParseException(SWUtil.getString("missingXHTMLConvertorError"));
		}

		// set the html input stream
		xhtmlConvertor.setHTML(spp);
		
		// this will throw an parse exception if converting fails
		domDoc = xhtmlConvertor.convert(); 
		
		// "postparser" step - remove remaining word crap
		domDoc.getDocumentElement().removeAttribute("xmlns:o");
		domDoc.getDocumentElement().removeAttribute("xmlns:w");
		domDoc.getDocumentElement().removeAttribute("xmlns:x");
	}

}

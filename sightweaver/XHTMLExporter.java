package sightweaver;

import org.w3c.dom.*;
import java.io.*;
import org.w3c.dom.*;
import org.w3c.tidy.Tidy;

/**
 * <code>XHTMLExporter</code> Implements the <code>Exporter</code> 
 * interface to export documents as XHTML.
 *
 * @author	David McNamara
 * @version	$Id: XHTMLExporter.java 242 2003-04-14 20:43:29Z mackers $
 */
public class XHTMLExporter implements Exporter {

	Document doc;
	Tidy tidy;
	
	public XHTMLExporter(Document d) {
		doc = d;
		// use tidy's pretty printer to export
		tidy = new Tidy();
		tidy.setIndentContent(true);
		tidy.setWrapAttVals(true);
		tidy.setIndentAttributes(true);
		tidy.setXmlPi(true);
		tidy.setXmlOut(true);
		tidy.setXHTML(true);
		tidy.setAltText("");
		tidy.setCharEncoding(org.w3c.tidy.Configuration.UTF8);
		tidy.setHideEndTags(false);
		tidy.setQuoteAmpersand(true);
		tidy.setQuoteNbsp(true);
		tidy.setSmartIndent(true);
		tidy.setMakeClean(true);
		tidy.setWord2000(true);
	}
	
	public void exportToFile(File f) throws ExportException {
		// open the file for writing
		FileOutputStream out;
		try {
			out = new FileOutputStream(f);
		} catch (IOException e) {
			throw new ExportException(SWUtil.getString("writeError"));
		}
		tidy.pprint(doc.getDOMDocument(),out);
	}

	public String exportToString() throws ExportException {
		OutputStream out = new ByteArrayOutputStream();
		tidy.pprint(doc.getDOMDocument(),out);
		return out.toString();
	}
}

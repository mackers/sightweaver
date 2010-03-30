package sightweaver;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import javax.xml.parsers.*;
import java.io.*;
import java.math.BigInteger;

/**
 * Implements an <code>Importer</code> to import CSV (Comma Separated Values) files.
 * <p>
 * <code>Importer</code>'s parse method is implemented with a CSV to XHTML convertor
 * algorithm. This class extends <code>XHTMLImporter</code> so that the HTML DOM will
 * then be operated on like a regular XHTML file.
 *
 * @author	David McNamara
 * @version	$Id: CSVImporter.java 173 2003-04-03 18:53:01Z mackers $
 */
public class CSVImporter extends XHTMLImporter {

	public void parse() throws ParseException {
		Reader r = new BufferedReader(new InputStreamReader(inputStream));
		StreamTokenizer st = new StreamTokenizer(r);
		StringBuffer doc = new StringBuffer();
	    
		// We want to read the file one line at a time, so end-ofline matters
		st.eolIsSignificant(true);
		// The delimiter between fields is a comma, not a space
		st.whitespaceChars(',', ',');
		// All strings are in double quotes
		st.quoteChar('"');
	
		// Write the XML declaration and the root element
		doc.append("<html>\n");
		doc.append("  <body>\n");
		doc.append("    <table>\n");

	
		// Get the first token, then check its type
		try {
			st.nextToken();
		} catch (java.io.IOException e) {
			throw new ParseException(e.getMessage());
		}
		
		while (st.ttype != StreamTokenizer.TT_EOF) {
			// We're not at EOF, so start a row
			doc.append("      <tr>\n"); 
			while (st.ttype !=
			      StreamTokenizer.TT_EOL && st.ttype !=
			      StreamTokenizer.TT_EOF) {
				// We use the BigInteger class here to write long numbers out Without
				// this, the date fields (which are written something like (19991013)
				// get converted to scientific notation....
				if (st.ttype == StreamTokenizer.TT_NUMBER) { 
					doc.append("        <td>");
					doc.append((BigInteger.valueOf((long)st.nval)).toString());
					doc.append("</td>\n"); 
				} else if (st.ttype !=StreamTokenizer.TT_EOL && st.ttype !=
					StreamTokenizer.TT_EOF) {
					// For reasons that escape me, if the token is "+", it is interpreted
					// as NULL.
					if (st.sval != null) { 
						doc.append("        <td>");
						doc.append(st.sval.trim()); 
						doc.append("</td>\n");
					} 
				} 
				try {
					st.nextToken();
				} catch (java.io.IOException e) {
					throw new ParseException(e.getMessage());
				}
			}
	
			// We've hit either the end of the line or the end of the file, so close
			// the row.
			doc.append("      </tr>\n"); 
			try {
				st.nextToken();
			} catch (java.io.IOException e) {
				throw new ParseException(e.getMessage());
			}
		}

		// Now we're at the end of the file, so close the XML document, 
		// flush the buffer to disk, and close the newly-created file. 
		doc.append("    </table>\n");
		doc.append("  </body>\n");
		doc.append("</html>\n");

		// parse this
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			StringReader sr = new StringReader(doc.toString());
			domDoc = builder.parse(new InputSource(sr));
		} catch (Exception e) {
			throw new ParseException(this.toString() + e.toString());
		}
	}

}

package sightweaver;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import java.io.*;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * <code>XHTMLImporter</code> implements the <code>Importer</code> class
 * to allow for the import of XHTML documents.
 * <p>
 * This class has a number of known sub classes which inherit a number
 * of methods in order to re-use the functions to extract table information
 * from the DOM.
 *
 * @author	David McNamara
 * @version	$Id: XHTMLImporter.java 175 2003-04-03 20:01:18Z mackers $
 */
public class XHTMLImporter implements Importer {

	protected InputStream inputStream;
	protected org.w3c.dom.Document domDoc;
	private final String tableElementName = "table";
	private final String tableSummaryAttribute = "summary";
	private final String tableCaptionElement = "caption";
	private final String tableColgroupElement = "colgroup";
	private final String tableColElement = "col";
	private final String tableRowElement = "tr";
	private final String tableDataElement = "td";
	private final String tableHeaderElement = "th";
	private final String tableSpanAttribute = "span";
	private final String tableColSpanAttribute = "colspan";
	private final String tableRowSpanAttribute = "rowspan";
	private final String headElementName = "head";
	private final String titleElementName = "title";
	private final String idAttribute = "id";
	private final String axisAttribute = "axis";
	private final String abbrAttribute = "abbr";
	private final String headersAttribute = "headers";
	private final String scopeAttribute = "scope";
	private final String theadElementName = "thead";
	private final String strongElementName = "strong";
	
	public void setInputStream(InputStream is) {
		inputStream = is;
	}

	public void parse() throws ParseException {
		if (inputStream == null) throw new ParseException(SWUtil.getString("internalError"));
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			domDoc = builder.parse(inputStream);
		} catch (java.net.ConnectException ce) {
			// TODO: something about this (bug 0015)
			throw new ParseException(SWUtil.getString("cxException"));
		} catch (Exception e) {
			throw new ParseException(this.toString() + e.toString());
		}
        }
	
	public org.w3c.dom.Document getDomDocument() {
		return domDoc;
        }

	public String getTitle() {
		if (domDoc == null) return "";
		NodeList headElements = domDoc.getElementsByTagName(headElementName);
		if (headElements.getLength() > 0) {
			NodeList titleElements = ((Element)headElements.item(0)).getElementsByTagName(titleElementName);
			if ((titleElements.getLength() > 0) && (titleElements.item(0).hasChildNodes())) {
				return titleElements.item(0).getFirstChild().getNodeValue();
			}
		}
		return "";
	}

	public Table[] getTables() throws ImportException {
		org.w3c.dom.NodeList tableElements = domDoc.getElementsByTagName(tableElementName);
		if (tableElements != null) {
			// create the table array with the number of table elements in the document
			int numOfTables = tableElements.getLength();
			Table[] tables = new Table[numOfTables];
			// for each table in the document, create a table element
			for (int i=0; i<numOfTables; i++) {
				org.w3c.dom.Element e = (Element)tableElements.item(i);
				// TODO: check is layout table (bug 0018)
				if ((e.getElementsByTagName(tableElementName) != null) &&
					(e.getElementsByTagName(tableElementName).getLength() > 1)) {
					// if has more than 1 tables elements then it must be nested
					// (the first table is itself, presumably)
					throw new ImportException(SWUtil.getString("nestedTables"));
				}		
				// get the cells
				Cell[][] c = this.getCells(e);
				// get the summary
				String s = this.getTableSummary(e);
				// get the caption
				String cp = this.getTableCaption(e);
				// construct the table
				tables[i] = new Table(c, s, cp, e);
			}
			return tables;
		}
		return null;
	}

	private Cell[][] getCells(org.w3c.dom.Element e) {
		// store the table - id associations in a hastable for quick indexing
		Hashtable ht = new Hashtable();
		int headerInc = 0;
		// first calculate the number of columns
		org.w3c.dom.NodeList colgroups = e.getElementsByTagName(tableColgroupElement);
		org.w3c.dom.NodeList cols = e.getElementsByTagName(tableColElement);
		org.w3c.dom.NodeList rows = e.getElementsByTagName(tableRowElement);
		int numOfColumns = 0;
		if (((colgroups != null) && (colgroups.getLength() > 0)) ||
		    ((cols != null) && (cols.getLength() > 0))) {
			// if there are 'colgroup' or 'col' elements then we can calculate from that
			for (int i=0; i<cols.getLength(); i++) {
				numOfColumns += this.getSpan((org.w3c.dom.Element)cols.item(i));
			}
			for (int i=0; i<colgroups.getLength(); i++) {
				if (colgroups.item(i).getChildNodes().getLength() == 0) {
					numOfColumns += this.getSpan((org.w3c.dom.Element)colgroups.item(i));
				}
			}
		} else {
			// otherwise, we count the maximum td or th elements in a tr
			for (int i=0; i<rows.getLength(); i++) {
				int thiscols = 0;
				org.w3c.dom.NodeList rcells = ((org.w3c.dom.Element)rows.item(i)).getChildNodes();
				for (int j=0; j<rcells.getLength(); j++) {
					if ((rcells.item(j).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) &&
					    ((rcells.item(j).getNodeName().equals(tableHeaderElement)) ||
					     (rcells.item(j).getNodeName().equals(tableDataElement)))) {
						    thiscols += this.getSpan((org.w3c.dom.Element)rcells.item(j), tableColSpanAttribute);
					}
				}
				if (thiscols > numOfColumns) numOfColumns = thiscols;
			}
		}
		// the number of rows are the number of tr elements in the table
		Cell[][] cells = new Cell[rows.getLength()][numOfColumns];
		// flag to determine if table contains any header info
		boolean containsHeaderInfo = false;
		// create the cell objects
		for (int i=0; i<rows.getLength(); i++) { // for each table row 
			org.w3c.dom.NodeList rcells = ((org.w3c.dom.Element)rows.item(i)).getChildNodes();
			int cc = 0;
			for (int j=0; j<rcells.getLength(); j++) { // for each table column
				// only want header and data elements
				if ((rcells.item(j).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) && 
					(rcells.item(j).getNodeName().equals(tableHeaderElement) ||
					rcells.item(j).getNodeName().equals(tableDataElement))) {
					// discard this cell if we've gone past the end of the row
					if (cc >= numOfColumns) {
						continue;
					}
					// find a free place on this row
					int pos = -1;
					for (int k=cc; k<numOfColumns; k++) {
						if (cells[i][k] == null) {
							pos = k;
							break;
						}
					}
					if (pos == -1) {
						// no free positions, discard this cell
						continue;
					}
					// put this cell into position
					// the type of cell depends on the node name
					if (rcells.item(j).getNodeName().equals(tableHeaderElement)) {
						cells[i][pos] = new HeaderCell();
						containsHeaderInfo = true;
					} else if (rcells.item(j).getNodeName().equals(tableDataElement)) {
						cells[i][pos] = new Cell();    
				    	} else {
						continue;
					}
					// store this cell element
					org.w3c.dom.Element el = (org.w3c.dom.Element)rcells.item(j);
					cells[i][pos].setElement(el);
					// set the content of this object to match the cell
					cells[i][pos].setContent(rcells.item(j).getChildNodes());
					// set the id
					String id = this.getID(el);
					if ((id != null) && (id != "")) {
						cells[i][pos].setID(this.getID(el));
					} else if (cells[i][pos].isHeader()) {
						// assign a id
						// loops until we get a free id
						do {
							id = "h" + headerInc++;
						} while (domDoc.getElementById(id) != null);
						cells[i][pos].setID(id);
					}
					// save this in the hashtable for easy lookup by other cells
					ht.put((Object)id, (Object)cells[i][pos]);
					// add headers in the 'headers' attribute
					String[] hs = this.getHeaders(el);
					for (int k=0; k<hs.length; k++) {
						if (ht.containsKey((Object)hs[k])) {
							try {
								cells[i][pos].addHeader((Cell)ht.get((Object)hs[k]));
							} catch (HeadersFullException hfe) {
							}
						}		
					}
					// set the abbr
					if (cells[i][pos].isHeader()) {
						((HeaderCell)cells[i][pos]).setAbbr(this.getAbbr(el));
					}
					// set the axis
					cells[i][pos].setAxis(this.getAxis(el));
					// if this cell spans, fill right or down
					int colSpan = this.getSpan(el, tableColSpanAttribute);
					int rowSpan = this.getSpan(el, tableRowSpanAttribute);
					// fill cols across
					for (int k=1; k<colSpan; k++) {
						if (pos+k < numOfColumns) 
							cells[i][pos+k] = new NullCell(cells[i][pos]);
					}
					// fill rows down
					for (int k=1; k<rowSpan; k++) {
						if (i+k < rows.getLength())
							cells[i+k][pos] = new NullCell(cells[i][pos]);
						// fill cols for these rows if necessary
						for (int l=1; l<colSpan; l++) {
							if (pos+l < numOfColumns)
								cells[i+k][pos+l] = new NullCell(cells[i][pos]);
						}
					}
					// update the column counter
					cc = pos + colSpan;
				}
			}
		}
		
		// if no header info, make cells inside thead headers.
		if (!containsHeaderInfo) {
			org.w3c.dom.NodeList theads = e.getElementsByTagName(theadElementName);
			if (theads.getLength() == 1) { // only 1 thead allowed
				containsHeaderInfo = true;
				org.w3c.dom.NodeList theadstrs = ((org.w3c.dom.Element)(theads.item(0))).getElementsByTagName(tableRowElement);
				for (int i=0; i<theadstrs.getLength(); i++) {
					for (int j=0; j<numOfColumns; j++) {
						if ((cells[i][j] != null) &&
							(!cells[i][j].isHeader())) {
							// clone this cell and make it a header
							cells[i][j] = cells[i][j].makeHeaderCell();
							// assign an id
							String id = "";
							do {
								id = "h" + headerInc++;
							} while (domDoc.getElementById(id) != null);
							cells[i][j].setID(id);
							// make the scope = col
							cells[i][j].setAttribute(scopeAttribute,"col");
						}
					}
				}
			}
			
		}
		
		// if still no header information, try to find headers using style info
		if (!containsHeaderInfo) {
			for (int i=0; i<cells.length; i++) {
				for (int j=0; j<cells[i].length; j++) {
					if ((cells[i][j] == null) ||
						(cells[i][j].isHeader()) || 
						(cells[i][j].isNull())) continue;
					if (SWUtil.isAllTextInsideTag(cells[i][j].getElement(),strongElementName)) {
						// all the text in this cell is inside a strong element
						// assume is header
						cells[i][j] = cells[i][j].makeHeaderCell();
						String id = "";
						do {
							id = "h" + headerInc++;
						} while (domDoc.getElementById(id) != null);
							cells[i][j].setID(id);
						}
						containsHeaderInfo = true;					
				}
			}
		}

		// if still no header info, inform user that they must manually specify headers (TODO, bug 0020)
		
		// add scopes - check each cell for a scope attribute and modify cells underneath
		// or to the right.
		for (int i=0; i<cells.length; i++) {
			for (int j=0; j<cells[i].length; j++) {
				if (cells[i][j] == null) continue;
				String scope = cells[i][j].getAttribute(scopeAttribute);
				if (scope == null) {
				} else if (scope.equals("row")) {
					// add this cell as a header to all cells to the
					// right of this
					for (int k=j+1; k<cells[i].length; k++) {
						if (cells[i][k] != null) {
							try {
								cells[i][k].addHeader(cells[i][j]);
							} catch (HeadersFullException hfe) {}
						}
					}
					// skip rest of this row
					j=cells[i].length;
				} else if (scope.equals("col")) {
					// add this cell as a header to all cells 
					// underneath this
					for (int k=i+1; k<cells.length; k++) {
						if (cells[k][j] != null) {
							try {
								cells[k][j].addHeader(cells[i][j]);
							} catch (HeadersFullException hfe) {}
						}
					}
				} else if (scope.equals("rowgroup")) {
					// TODO: handle this (bug 0016)
				} else if (scope.equals("colgroup")) {
					// TODO: handle this (bug 0016)
				}
			}
		}

		// now find all cells with no associated headers.
		for (int i=0; i<cells.length; i++) {
			for (int j=0; j<cells[i].length; j++) {
				if ((cells[i][j] == null) ||
					(cells[i][j].isHeader()) || 
					(cells[i][j].isNull()) || 
					(cells[i][j].hasHeader())) continue;
				// this cell has no header
				// we need to use the header finder algorithm
				Cell[] hch = new HeaderCell[10];
				Cell[] hcv = new HeaderCell[10];
				int hchi = 0;
				int hcvi = 0;
				// search left
				boolean foundH = false;
				for (int k=j; k>=0; k--) {
					// next if null cell
					if (cells[i][k] == null) continue;
					Cell c;
					if (cells[i][k].isNull()) {
						c = ((NullCell)cells[i][k]).getParentCell();
					} else {
						c = cells[i][k];
					}
					if (c.isHeader()) {
						// found a header
						if (c.hasHeader()) {
							// if this header has headers, add these and stop
							hch = c.getHeaders();
							break;
						} else {
							// otherwise add and keep searching left
							foundH = true;
							if (hchi<10) hch[hchi++] = c;
						}
					} else if (foundH) {
						// found data after header, stop
						break;
					}
				}
				// search up
				foundH = false;
				for (int k=i; k>=0; k--) {
					// next if null cell
					if (cells[k][j] == null) continue;
					Cell c;
					if (cells[k][j].isNull()) {
						c = ((NullCell)cells[k][j]).getParentCell();
					} else {
						c = cells[k][j];
					}
					if (c.isHeader()) {
						// found a header
						if (c.hasHeader()) {
							// if this header has headers, add these and stop
							hcv = c.getHeaders();
							break;
						} else {
							// otherwise add and keep searching up
							foundH = true;
							if (hcvi<10) hcv[hcvi++] = c;
						}
					} else if (foundH) {
						// found data after header, stop
						break;
					}
				}
				// add these headers to the cell
				for (int k=0; k<10; k++) {
					try {
						// up
						if (hcv[k] != null) cells[i][j].addHeader(hcv[k]);
					} catch (HeadersFullException hfe) {
						break;
					}
				}
				for (int k=0; k<10; k++) {
					try {
						// left
						if (hch[k] != null) cells[i][j].addHeader(hch[k]);
					} catch (HeadersFullException hfe) {
						break;
					}
				}
			}
		}
		
		return cells;

	}

	private String getTableSummary(org.w3c.dom.Element e) {
		// return the summary attribute of the table (if any)
		return e.getAttribute(tableSummaryAttribute);
	}

	private String getTableCaption(org.w3c.dom.Element e) {
		org.w3c.dom.NodeList captionElements = e.getElementsByTagName(tableCaptionElement);
		// check if there's a caption element
		if ((captionElements != null) && (captionElements.getLength() > 0)) {
			// if there's more than one, we'll use the first
			org.w3c.dom.Element captionElement = (Element)captionElements.item(0);
			// get the text nodes
			org.w3c.dom.NodeList captionTextNodes = captionElement.getChildNodes();
			String caption = "";
			for (int i=0; i<captionTextNodes.getLength(); i++) {
				// append all text nodes to the caption string
				if (captionTextNodes.item(i).getNodeType() == org.w3c.dom.Node.TEXT_NODE) {
					caption += captionTextNodes.item(i).getNodeValue();
				}
			}
			return caption;
		}
		return null;
	}

	private int getSpan(org.w3c.dom.Element e) {
		return this.getSpan(e, tableSpanAttribute);
	}
	
	private int getSpan(org.w3c.dom.Element e, String sa) {
		String colspan = e.getAttribute(sa);
		if (colspan.equals("")) {
			return 1;
		} else {
			return Integer.parseInt(colspan);
		}
	}

	private String getID(org.w3c.dom.Element e) {
		return e.getAttribute(idAttribute);
	}

	private String getAxis(org.w3c.dom.Element e) {
		return e.getAttribute(axisAttribute);
	}

	private String getAbbr(org.w3c.dom.Element e) {
		return e.getAttribute(abbrAttribute);
	}

	private String getScope(org.w3c.dom.Element e) {
		return e.getAttribute(scopeAttribute);
	}

	private String[] getHeaders(org.w3c.dom.Element e) {
		StringTokenizer st = new StringTokenizer(e.getAttribute(headersAttribute));
		String[] s = new String[st.countTokens()];
		for (int i=0; i<s.length; i++) {
			s[i] = st.nextToken();
		}
		return s;
	}
}

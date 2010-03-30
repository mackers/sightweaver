package sightweaver;

import java.io.*;
import org.w3c.dom.*;

/**
 * The Document class represents a single (HTML or other) document that the 
 * program is to import, manipulate and export.
 * <p>
 * The class holds document wide attributes such as the DOM document, title 
 * and filename. It also holds an array of tables in the document.
 *
 * @author	David McNamara
 * @version	$Id: Document.java 192 2003-04-06 22:59:51Z mackers $
 */
public class Document {

	private Table[] tables; 
	private org.w3c.dom.Document domDocument;
	private Importer importer;
	private Exporter exporter;
	private File origFile;
	private String title;
	
	/**
	 * Creates a standard default document but does not do any importing.
	 */
	public Document() {
		// create the exporter
		exporter = new XHTMLExporter(this);
	}
	
	/**
	 * Creates a document and imports the document in the specified file.
	 *
	 * @param f	the file to import
	 * @throws	ImportException if the file cannot be read
	 * @throws	ParseException if there is a problem with the file's content
	 */
	public Document(File f) throws ImportException, ParseException {
		this();
		this.importDocument(f);
	}

	/**
	 * Imports the document in specified file. Sets this document's DOM object.
	 * 
	 * @param f	the file to import
	 * @throws	ImportException if the file cannot be read
	 * @throws	ParseException if there is a problem with the file's content
	 */
	public void importDocument(File f) throws ImportException, ParseException {
		if (f == null) throw new ImportException(SWUtil.getString("noFileSpecifiedError"));
		origFile = f;
		try {
			//this.importDocument(new BufferedReader(new FileReader(f)));
			this.importDocument(new FileInputStream(f));
		} catch (FileNotFoundException fnfe) {
			throw new ImportException(fnfe.getMessage());
		}
	}

	/**
	 * Imports the document in the input stream. Sets this document's DOM object.
	 * 
	 * @param is	the input stream to read the document from
	 * @throws	ImportException if the file cannot be read
	 * @throws	ParseException if there is a problem with the file's content
	 */
	public void importDocument(InputStream is) throws ImportException, ParseException {
		// we need to determine what type of document this is
		// and create the appropriate type of importer
		if (origFile != null) {
			// get the extension of this file to see if we can determine what it is
			String ext = SWUtil.getFileExtension(origFile);
			if (ext.equals("doc")) {
				// we don't support the doc format
				throw new ImportException(SWUtil.getString("docFormatNotSupported"));
			} else if (ext.equals("xsl")) {
				// we don't support the xsl format
				throw new ImportException(SWUtil.getString("xslFormatNotSupported"));
			} else if (ext.equals("rdf")) {
				// attempt to construct an RDFImporter
				try {
					importer = (Importer)Class.forName("sightweaver.RDFImporter").newInstance();
				} catch (Exception e) {
					throw new ImportException(SWUtil.getString("noFilterFound"));
				}
			} else if (ext.equals("csv")) {
				// attempt to construct a CSVImporter
				try {
					importer = (Importer)Class.forName("sightweaver.CSVImporter").newInstance();
				} catch (Exception e) {
					throw new ImportException(SWUtil.getString("noFilterFound"));
				}
			} else if (ext.equals("xhtml")) {
				// attempt to construct a XHTMLImporter
				try {
					importer = (Importer)Class.forName("sightweaver.XHTMLImporter").newInstance();
				} catch (Exception e) {
					throw new ImportException(SWUtil.getString("noFilterFound"));
				}
			} else {
				// assume html, pass to Importer
				importer = new HTMLImporter();
			}
		} else {
			// no file was opened, just a bufferedreader
			// so we assume to be html and ask importer to determine
			importer = new HTMLImporter();
		}
		// set the importer's input stream
		importer.setInputStream(is);
		// try and parse the document (this will throw a parse exception)
		importer.parse();
		// get the document
		domDocument = importer.getDomDocument();
		// create the table objects for this document
		tables = importer.getTables();
		if ((tables == null) || (tables.length == 0)) 
			throw new ImportException(SWUtil.getString("noTablesFound"));
		// save the title for this document
		title = importer.getTitle();
		// set the generator tag
		domDocument = SWUtil.setGenerator(domDocument,SWUtil.getString("generator"));
	}

	/**
	 * Tests whether the specified ID is not already used in this document.
	 *
	 * @param id	the id to test for
	 * @return	true if this id is not already used in the document
	 */
	public boolean isUniqueId(String id) {
		// TODO: fix me (bug 0002)
		return (domDocument.getElementById(id) == null);
	}

	/**
	 * Returns the DOM document element of this document object.
	 *
	 * @return	the DOM document
	 * @see		org.w3c.dom.Document
	 */
	public org.w3c.dom.Document getDOMDocument() {
		return domDocument;
	}

	/**
	 * Returns an array of tables in this document.
	 *
	 * @return	an array of tables in this document
	 */
	public Table[] getTables() {
		return tables;
	}

	/**
	 * Returns the title of this document.
	 *
	 * @return	the document title
	 */
	public String getTitle() {
		if (!title.equals("")) {
			// if we have a document title, use that
			return title;
		} else if (origFile != null) {
			// otherwise use the filename
			return origFile.getName();
		} else {
			// otherwise use untitled
			return SWUtil.getString("emptyDocumentTitle");
		}
	}

	/**
	 * Exports the document to a specified file. Performs neccessary
	 * accessibility checks first.
	 *
	 * @param f	the file to export to
	 * @throws	ExportException if can't export
	 */
	public void export(File f) throws ExportException {
		// check that all tables are accessible
		for (int i=0; i<tables.length; i++) {
			int a = tables[i].isAccessible();
			if (a != 0) {
				throw new InaccessibleTableExportException(i,a);
			}
		}
		
		// check for a file object
		if (f == null) throw new ExportException(SWUtil.getString("noFileSpecifiedError"));

		// get the extension
		String ext = SWUtil.getFileExtension(f);

		// add an extension
		if (ext.equals("")) f = new File(f.toString() + "." + SWUtil.getString("defaultExportExtension"));
		
		// check if can create a new file and can write to that file
		try {
			if (!f.createNewFile() && (!f.canWrite())) throw new ExportException(SWUtil.getString("noWriteError"));
		} catch (IOException e) {
			throw new ExportException(SWUtil.getString("writeError"));
		}
			
		// export the file
		exporter.exportToFile(f);
	}

	/**
	 * Returns the document's HTML source as a string. Uses the exporter class so
	 * output will be pretty printed.
	 *
	 * @return	the HTML source
	 * @throws	ExportException if the document can not be exported
	 */
	public String getSource() throws ExportException {
		return exporter.exportToString();
	}
}


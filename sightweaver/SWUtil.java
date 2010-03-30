package sightweaver;

import java.util.*;
import java.util.regex.*;
import org.w3c.dom.*;

/**
 * SWUtil groups together some common functions that don't really fit anywhere else.
 */
public class SWUtil {

	//private static final String defaultLanguage = "en";
	//private static final String defaultCountry = "US";
	private static ResourceBundle rB = null;
	private static Locale currentLocale = null;
	
	/**
	 * Gets a string from the stringbundle. If the stringbundle hasn't been loaded yet,
	 * then load using the default language and country.
	 *
	 * @param propName	the name of the property
	 * @return		the value of the property
	 */
	public static String getString(String propName) {
		try {
			if (rB == null) {
				//currentLocale = new Locale(defaultLanguage, defaultCountry);
				currentLocale = Locale.getDefault();
				rB = ResourceBundle.getBundle("sightweaver.sw",currentLocale,ClassLoader.getSystemClassLoader());
			}
			return rB.getString(propName);
		} catch (Exception e) {
			return "?";
		}
	}

	/**
	 * Returns the file extension part of a file object. The extension (or suffix)
	 * is usually the part of the filename that comes after the period (.).
	 *
	 * @param f 	the file object
	 * @return	the extension of this file object
	 */
	public static String getFileExtension(java.io.File f) {
		try {
			String fileName = f.getName();
			int periodPos = fileName.lastIndexOf(".");
			if (periodPos == -1) return "";
			return fileName.substring(periodPos + 1);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Tests whether a string appears to be a HTML document or not. 
	 *
	 * @param html	the HTML string
	 * @return	true if the string looks like HTML
	 */
	public static boolean isHTML(String html) {
		/*
		Pattern p = Pattern.compile("<\\w+[\\w\\d]*\\s*.*>");
		Matcher m = p.matcher(html);
		return m.matches();
		*/
		// TODO: the above regular expression has mysteriously broke itself 
		//       let's return true for the moment and hopefully it will fix itself (bug 0014)
		return true;
	}

	/**
	 * Returns the 'generator' tag in a HTML document.
	 *
	 * @param html	the HTML document
	 * @return	the generator string
	 */
	public static String getGenerator(String html) {
		Pattern p = Pattern.compile(".*<meta.*?name=['\"]?generator['\"]?.*?content=['\"]?(.*?)['\"]?>.*",Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL);
		Matcher m = p.matcher(html);
		if (m.matches()) {
			return m.group(1);
		} else {
			return "fook";
		}
	}

	/**
	 * Sets the 'generator' tag in a DOM HTML document.
	 *
	 * @param d	the DOM document
	 * @param g	the generator string
	 * @return	the modified DOM document
	 * @see		org.w3c.dom.Document
	 */
	public static org.w3c.dom.Document setGenerator(org.w3c.dom.Document d, String g) {
		org.w3c.dom.NodeList metas = d.getElementsByTagName("meta");
		for (int i=0; i<metas.getLength(); i++) {
			NamedNodeMap a = metas.item(i).getAttributes();
			for (int j=0; j<a.getLength(); j++) {
				if (a.item(j).getNodeName().toLowerCase().equals("name") &&
						a.item(j).getNodeValue().toLowerCase().equals("generator")) {
					((Element)metas.item(i)).setAttribute("content",g);
					return d;
				}
			}
		}
		// TODO: add generator tag (bug 0023) if none exists
		return d;
	}
	

	/**
	 * Returns all the concatenated text value of all text nodes in this element.
	 *
	 * @param e	the element
	 * @return	the text content
	 * @see		org.w3c.dom.Element
	 */
	public static String textFromElement(Element e) {
		NodeList nl = e.getChildNodes();
		String text = "";
		for (int i=0; i<nl.getLength(); i++) {
			if (nl.item(i).getNodeType() == Node.TEXT_NODE) {
				text += nl.item(i).getNodeValue();
			} else if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
				text += SWUtil.textFromElement((Element)nl.item(i));
			}
		}
		return text;
	}

	/**
	 * Tests whether all textual content in this element is contained within
	 * the specified tag.
	 *
	 * @param e 	the element
	 * @param t	the tag name
	 * @return	true if all text is inside the tag
	 */
	public static boolean isAllTextInsideTag(Element e, String t) {
		return isAllTextInsideTag(e,t,false);
	}

	private static boolean isAllTextInsideTag(Element e, String t, boolean inTag) {
		NodeList nl = e.getChildNodes();
		for (int i=0; i<nl.getLength(); i++) {
			if (nl.item(i).getNodeType() == Node.TEXT_NODE) {
				return inTag;
			} else if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
				if (nl.item(i).getNodeName().equals(t)) {
					return isAllTextInsideTag((Element)(nl.item(i)), t, true);
				} else {
					return isAllTextInsideTag((Element)(nl.item(i)), t, false);
				}
			}
		}
		return false;
	}

	/**
	 * Changes an element's tag name.
	 *
	 * @param ed	the element to change
	 * @param to	the new tag name
	 * @return	the new element
	 */
	public static Element changeTagName(Element ed, String to) {
                        // replace this cell's "td" element with a "th" element
                        Element eh = ed.getOwnerDocument().createElement(to);
                        // copy all childnodes
                        for (int i=0; i<ed.getChildNodes().getLength(); i++) {
                        	eh.appendChild(ed.getChildNodes().item(i));
                        }
                        // copy all attributes
                        for (int i=0; i<ed.getAttributes().getLength(); i++) {
                        	eh.setAttributeNode((Attr)(ed.getAttributes().item(i)));
                        }
			ed.getParentNode().replaceChild(eh,ed);
			return eh;
	}
}

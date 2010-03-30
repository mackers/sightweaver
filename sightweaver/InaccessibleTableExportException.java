package sightweaver;

/**
 * An InaccessibleTableExportException is thrown when the document 
 * cannot be exported because a table therein is inaccessible.
 *
 * @author	David McNamara
 * @version	$Id: InaccessibleTableExportException.java 196 2003-04-07 01:23:00Z mackers $
 */
public class InaccessibleTableExportException extends ExportException {

	private int tableNum;
	private int accessibilityError;
	
	/**
	 * Creates a InaccessibleTableExportException with an
	 * accessibility error in a particular table.
	 *
	 * @param t	the table the error exists in
	 * @param a	the accessibility error code
	 */
	public InaccessibleTableExportException(int i, int a) { 
		super();
		tableNum = i;
		accessibilityError = a;
	}

	/**
	 * Returns the table in the document that this error was
	 * thrown on.
	 */
	public int getTableNumber() {
		return tableNum;
	}

	/**
	 * Returns the accessibility error thrown by the table.
	 */
	public int getAccessibilityError() {
		return accessibilityError;
	}

	/**
	 * Override the getMessage function
	 *
	 * @return	the message detail of this exception
	 */
	public String getMessage() {
		String msg = "";
		if ((accessibilityError & Table.TABLE_MISSING_SUMMARY) == Table.TABLE_MISSING_SUMMARY) msg += SWUtil.getString("tableMissingSummary") + "\n";
		if ((accessibilityError & Table.TABLE_MISSING_CAPTION) == Table.TABLE_MISSING_CAPTION) msg += SWUtil.getString("tableMissingCaption") + "\n";
		if ((accessibilityError & Table.HEADER_MISSING_ABBREVIATION) == Table.HEADER_MISSING_ABBREVIATION) msg += SWUtil.getString("headerMissingAbbreviation") + "\n";
		if ((accessibilityError & Table.CELL_MISSING_HEADER) == Table.CELL_MISSING_HEADER) msg += SWUtil.getString("cellMissingHeader") + "\n";
		return msg;
	}
}

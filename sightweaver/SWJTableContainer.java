package sightweaver;

/**
 * The SWJTableContainer interface declares methods that must be
 * implemented by a window, frame or other container that
 * wishes to hold and interact with one or more <code>SWJTable</code>s.
 *
 * @author	David McNamara
 * @version	$Id: SWJTableContainer.java 212 2003-04-09 12:56:57Z mackers $
 */
public interface SWJTableContainer {

        /**
         * Enables or disables the edit header menu item.
         *
         * @param e     the new enabled status
         */
	public void setEditHeaderMenuItemEnabled(boolean e);

        /**
         * Enables or disables the edit axis menu item.
         *
         * @param e     the new enabled status
         */
	public void setEditAxisMenuItemEnabled(boolean e);

        /**
         * Enables or disables the add header menu item.
         *
         * @param e     the new enabled status
         */
	public void setAddHeaderMenuItemEnabled(boolean e);

        /**
         * Enables or disables the clear headers menu item.
         *
         * @param e     the new enabled status
         */
	public void setClearHeadersMenuItemEnabled(boolean e);

        /**
         * Enables or disables the add header scope menu item.
         *
         * @param e     the new enabled status
         */
	public void setAddHeaderScopeMenuItemEnabled(boolean e);

        /**
         * Enables or disables the make header menu item.
         *
         * @param e     the new enabled status
         */
	public void setMakeHeaderMenuItemEnabled(boolean e);

        /** 
         * Enables or disables the make data menu item.
         *
         * @param e     the new enabled status
         */
	public void setMakeDataMenuItemEnabled(boolean e);

        /**
         * Sets the status bar axis value.
         *
         * @param s     the new axis value
         */
	public void setAxisStatus(String s);

        /**
         * Sets the status bar abbreviation value.
         *
         * @param s     the new abbreviation
         */
	public void setAbbrStatus(String s);

        /**
         * Sets the status bar ID value.
         *
         * @param s     the new ID vlaue
         */
	public void setIDStatus(String s);

        /**
         * Sets the status bar headers string.
         *
         * @param s     the header string
         */
	public void setHeadersStatus(String s);
}

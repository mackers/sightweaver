package sightweaver;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 * A file filter for selecting only HTML files. Files
 * are selected if their extension contains "htm" in any
 * case.
 * 
 * @author David McNamara
 * @version $Id: HTMLFilter.java 173 2003-04-03 18:53:01Z mackers $
 */
public class HTMLFilter extends FileFilter {

    //Accept all directories and all html file types
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = SWUtil.getFileExtension(f).toLowerCase();
        return ((extension != null) && (extension.indexOf("htm") != -1));
    }

    //The description of this filter
    public String getDescription() {
        return SWUtil.getString("htmlFilterDesc");
    }
}

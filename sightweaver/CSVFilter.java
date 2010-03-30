package sightweaver;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 * A file filter for selecting only CSV files
 *
 * @author David McNamara
 * @version $Id: CSVFilter.java 173 2003-04-03 18:53:01Z mackers $
 */
public class CSVFilter extends FileFilter {

    //Accept all directories and all html file types
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = SWUtil.getFileExtension(f).toLowerCase();
        return ((extension != null) && (extension.equals("csv")));
    }

    //The description of this filter
    public String getDescription() {
        return SWUtil.getString("csvFilterDesc");
    }
}

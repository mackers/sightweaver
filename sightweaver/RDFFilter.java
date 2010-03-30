package sightweaver;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 * A file filter for selecting only RDF files
 * 
 * @author David McNamara
 * @version $Id: RDFFilter.java 173 2003-04-03 18:53:01Z mackers $
*/
 
public class RDFFilter extends FileFilter {

    //Accept all directories and all html file types
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = SWUtil.getFileExtension(f).toLowerCase();
        return ((extension != null) && (extension.equals("rdf")));
    }

    //The description of this filter
    public String getDescription() {
        return SWUtil.getString("rdfFilterDesc");
    }
}

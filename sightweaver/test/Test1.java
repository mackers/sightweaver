package sightweaver.test;

import sightweaver.*;
import java.io.*;

class Test1 {
	public static void main (String args[]) {
		File file = new File("/home/mackers/college/fyp/fyp/samples/table1.html");
		try {
			Document doc = new Document(file);
			Table[] t = doc.getTables();
			t[0].dumpStructure();
			doc.export(new File("/home/mackers/tmp/swout.html"));
		} catch (Exception e2) {
			System.err.println("Error: " + e2.getMessage());
			e2.printStackTrace();
		}
	}
}

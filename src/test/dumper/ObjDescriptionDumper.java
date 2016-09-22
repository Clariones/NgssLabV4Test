package test.dumper;

import java.io.File;
import java.io.PrintStream;

import com.terapico.system.ObjectCollection;

public class ObjDescriptionDumper extends AbsFileDumper implements Dumper {

	@Override
	protected File getOutputFile() {
		return new File("testoutput/step1_ObjectDescription.txt");
	}

	@Override
	protected void print(PrintStream ps, Object obj) {
		ObjectCollection oc = (ObjectCollection) obj;
		ps.println(oc.getClassName());
		ps.println(obj);
	}

	
}

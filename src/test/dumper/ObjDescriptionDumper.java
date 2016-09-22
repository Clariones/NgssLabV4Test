package test.dumper;

import java.io.File;
import java.io.PrintStream;

import com.terapico.system.ObjectCollection;
import com.terapico.system.ObjectDescriptor;

public class ObjDescriptionDumper extends AbsFileDumper implements Dumper {

	@Override
	protected File getOutputFile() {
		return new File("testoutput/step1_ObjectDescription.txt");
	}

	@Override
	protected void print(PrintStream ps, Object obj) {
		ObjectDescriptor oc = (ObjectDescriptor) obj;
		ps.println(oc.getClassName());
		ps.println(obj);
	}

	
}

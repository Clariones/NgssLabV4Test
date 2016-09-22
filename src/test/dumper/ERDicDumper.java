package test.dumper;

import java.io.File;
import java.io.PrintStream;

public class ERDicDumper extends AbsFileDumper implements Dumper {

	@Override
	protected File getOutputFile() {
		return new File("testoutput/step1_ObjectDescription.txt");
	}

	@Override
	protected void print(PrintStream ps, Object obj) {
		ps.println(obj);
	}

	
}

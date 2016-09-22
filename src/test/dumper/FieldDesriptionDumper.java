package test.dumper;

import java.io.File;
import java.io.PrintStream;

import com.terapico.system.CMRField;
import com.terapico.system.FieldDescriptor;
import com.terapico.system.ObjectDescriptor;

public class FieldDesriptionDumper extends AbsFileDumper implements Dumper {

	@Override
	protected File getOutputFile() {
		return new File("testoutput/step2.1_FieldDescription.txt");
	}

	@Override
	protected void print(PrintStream ps, Object obj) {
		if (obj instanceof ObjectDescriptor){
			ps.println("======================================");
			ps.println(((ObjectDescriptor) obj).getBeanName());
			return;
		}
		if (obj instanceof FieldDescriptor){
			ps.println(obj);
			return;
		}
		if (obj instanceof CMRField){
			CMRField rf = (CMRField) obj;
			ps.println(rf.getFieldName()+": referenced by " + rf.getType() +"."+rf.getFieldVar());
		}
	}

	
}

package test.dumper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

public abstract class AbsFileDumper implements Dumper{
	protected abstract File getOutputFile();
	protected abstract void print(PrintStream ps, Object obj);
	protected boolean firstOpen = true;

	@Override
	public void dump(Object obj) throws Exception {
		ensureOutputFile();
		PrintStream ps = new PrintStream(new FileOutputStream(getOutputFile(), true));
		if (firstOpen) {
			ps.println(new Date());
		}
		
		if (obj instanceof String){
			ps.println(obj);
		}else{
			print(ps, obj);
		}
		
		
		ps.flush();
		ps.close();
	}

	protected void ensureOutputFile() throws IOException {
		if (!firstOpen){
			return;
		}
		File outputFile = getOutputFile();
		if (outputFile.exists()){
			outputFile.delete();
		}else{
			outputFile.getParentFile().mkdirs();
		}
		outputFile.createNewFile();
		firstOpen = false;
	}
	
	
}

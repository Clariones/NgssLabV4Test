package test.dumper.artifact;

import java.io.File;
import java.io.PrintStream;
import java.util.List;

import org.ngss.platform.artifact.ArtifactWebConsoleCreatePage;
import org.ngss.platform.buildplan.BuildPlan;

import test.dumper.AbsFileDumper;

public class WebConsoleCreatePage extends AbsFileDumper {

	@Override
	protected File getOutputFile() {
		return new File("testoutput/artifact/webConsoleCreatePage");
	}

	@Override
	protected void print(PrintStream ps, Object obj) {
		ArtifactWebConsoleCreatePage art = (ArtifactWebConsoleCreatePage) obj;
		List<BuildPlan> plans = art.getBuildPlans();
		ps.println("=====web console create page for " + art.getRootLogicalUnit().getRootNode().getKey()
				+ "=====");
		for(BuildPlan plan : plans){
			ps.println(BuildPlanDumper.plan2String(plan));
		}
		ps.println();
	}

}

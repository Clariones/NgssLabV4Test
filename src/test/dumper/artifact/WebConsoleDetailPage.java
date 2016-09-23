package test.dumper.artifact;

import java.io.File;
import java.io.PrintStream;
import java.util.List;

import org.ngss.platform.artifact.ArtifactWebConsoleDetailPage;
import org.ngss.platform.buildplan.BuildPlan;

import test.dumper.AbsFileDumper;

public class WebConsoleDetailPage extends AbsFileDumper {

	@Override
	protected File getOutputFile() {
		return new File("testoutput/artifact/webConsoleDetailPage");
	}

	@Override
	protected void print(PrintStream ps, Object obj) {
		ArtifactWebConsoleDetailPage art = (ArtifactWebConsoleDetailPage) obj;
		List<BuildPlan> plans = art.getBuildPlans();
		ps.println("=====web console detail page for " + art.getRootLogicalUnit().getRootNode().getKey()
				+ "=====");
		for(BuildPlan plan : plans){
			ps.println(BuildPlanDumper.plan2String(plan));
		}
		ps.println();
	}

}

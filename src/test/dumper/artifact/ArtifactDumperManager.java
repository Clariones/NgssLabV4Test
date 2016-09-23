package test.dumper.artifact;

import java.util.List;

import org.ngss.platform.artifact.Artifact;

import test.dumper.Dumper;

public class ArtifactDumperManager implements Dumper {
	protected Dumper webConsoleCreatePage = new WebConsoleCreatePage();
	protected Dumper webConsoleDetailPage = new WebConsoleDetailPage();
	
	@Override
	public void dump(Object obj) throws Exception {
		List<Artifact> artifacts = (List<Artifact>) obj;
		for(Artifact art : artifacts){
			switch (art.getArtifactType()){
			case frontEnd_webConsole_create_page:
				webConsoleCreatePage.dump(art);
				break;
			case frontEnd_webConsole_detail_page:
				webConsoleDetailPage.dump(art);
				break;
			default:
				throw new RuntimeException("Artifact type " + art.getArtifactType() +" not supported yet");
			}
		}
	}

}

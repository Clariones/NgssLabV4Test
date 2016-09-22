package test.dumper;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.ngss.platform.entitynode.EntityNode;
import org.ngss.platform.entitynode.EntityNodeFactory;

public class EntityNodeRepositoryDumper extends AbsFileDumper implements Dumper {

	@Override
	protected File getOutputFile() {
		return new File("testoutput/step3_EntityNodes.txt");
	}

	@Override
	protected void print(PrintStream ps, Object obj) {
		EntityNodeFactory repo = (EntityNodeFactory) obj;
		List<EntityNode> allNodes = new ArrayList(repo.getAllEntity());
		Collections.sort(allNodes, new Comparator<EntityNode>(){
			public int compare(EntityNode o1, EntityNode o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});
		for(EntityNode node: allNodes){
			ps.printf("%s%s%s\n", node.getKey(), 
					node.tmp_getPrototype() == null ? "" : " prototype is ",
					node.tmp_getPrototype() == null ? "" : node.tmp_getPrototype().getKey());
		}
	}

	
}

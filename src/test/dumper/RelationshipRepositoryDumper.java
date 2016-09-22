package test.dumper;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.ngss.platform.actingrole.ActingRole;
import org.ngss.platform.entitynode.EntityNode;
import org.ngss.platform.participantdefinition.ParticipantDefinition;
import org.ngss.platform.relationship.Relationship;
import org.ngss.platform.relationship.RelationshipAttributes;
import org.ngss.platform.relationship.RelationshipFactory;

public class RelationshipRepositoryDumper extends AbsFileDumper implements Dumper {

	@Override
	protected File getOutputFile() {
		return new File("testoutput/step4_Relationships.txt");
	}

	@Override
	protected void print(PrintStream ps, Object obj) {
		RelationshipFactory factory = (RelationshipFactory) obj;
		List<Relationship> allRelas = new ArrayList<>(factory.getAllRelationships());
		Collections.sort(allRelas, new Comparator<Relationship>(){
			public int compare(Relationship o1, Relationship o2) {
				String key1 = o1.getParticipants().get(o1.getDefinition().getMasterRole()).getKey();
				String key2 = o2.getParticipants().get(o2.getDefinition().getMasterRole()).getKey();
				return key1.compareTo(key2);
			}
		});
		for(Relationship rela : allRelas){
			EntityNode masterNode = rela.getParticipants().get(rela.getDefinition().getMasterRole());
			StringBuilder sb = new StringBuilder();
			sb.append(rela.getId()).append(": ").append(masterNode.getKey()).append(' ');
			sb.append(rela.getDefinition().getKey()).append(" with");
			Iterator<Entry<ActingRole, ParticipantDefinition>> it = rela.getDefinition().getParticipants().entrySet().iterator();
			while(it.hasNext()){
				Entry<ActingRole, ParticipantDefinition> ent = it.next();
				EntityNode passiveNode = rela.getParticipants().get(ent.getKey());
				if (passiveNode == masterNode){
					continue;
				}
				sb.append(' ').append(passiveNode.getKey());
				if (rela.getParticipantAttributes() == null){
					continue;
				}
				Map<RelationshipAttributes, Object> attrs = rela.getParticipantAttributes().get(ent.getKey());
				if (attrs == null || attrs.isEmpty()){
					continue;
				}
				sb.append('(');
				Iterator<Entry<RelationshipAttributes, Object>> attrIt = attrs.entrySet().iterator();
				while(attrIt.hasNext()){
					Entry<RelationshipAttributes, Object> attrKV = attrIt.next();
					sb.append(attrKV.getKey()).append('=').append(attrKV.getValue()).append(',');
				}
				sb.append(')');
			}
			ps.println(sb.toString());
		}
	}

	
}

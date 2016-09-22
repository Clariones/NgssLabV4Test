package test.dumper;

import java.util.HashSet;
import java.util.Map;

import org.ngss.platform.actingrole.ActingRole;
import org.ngss.platform.entitynode.EntityNode;
import org.ngss.platform.relationship.Relationship;
import org.ngss.platform.relationshipdefinition.RelationshipDefinition;

public class RelationshipStdoutDumper implements Dumper {

	@Override
	public void dump(Object obj) throws Exception {
		Relationship rela = (Relationship) obj;
		RelationshipDefinition def = rela.getDefinition(); 
		StringBuilder sb = new StringBuilder();
		ActingRole mRole = def.getMasterRole();
		Map<ActingRole, EntityNode> parts = rela.getParticipants();
		HashSet<ActingRole> roles = new HashSet<>(parts.keySet());
		EntityNode mNode = parts.get(mRole);
		roles.remove(mRole);
		
		sb.append(def.getKey()).append('-').append(rela.getId()).append(':');
		sb.append(mRole.getName()).append("=").append(mNode.getName());
		
		for(ActingRole role : roles){
			sb.append(' ').append(role.getName()).append("=").append(parts.get(role).getName());
		}
		System.out.println(sb.toString());
	}

}

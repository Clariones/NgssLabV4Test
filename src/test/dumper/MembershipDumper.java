package test.dumper;

import java.io.File;
import java.io.PrintStream;
import java.util.List;
import java.util.Set;

import org.ngss.platform.logicalunit.LogicalUnit;
import org.ngss.platform.logicalunit.LogicalUnitFactory;
import org.ngss.platform.membership.Membership;
import org.ngss.platform.relationshipdefinition.RelationshipDefinition.Multiplicity;

public class MembershipDumper extends AbsFileDumper implements Dumper {

	@Override
	protected File getOutputFile() {
		return new File("testoutput/step5_Memberships.txt");
	}

	@Override
	protected void print(PrintStream ps, Object obj) {
		LogicalUnitFactory factory = (LogicalUnitFactory) obj;
		Set<LogicalUnit> allLus = factory.getAllLogicalUnits();
		for(LogicalUnit lu : allLus){
			List<Membership> members = lu.getMembershipsList();
			if (members == null || members.isEmpty()){
				ps.println("LogicalUnit " + lu.getRootNode().getKey() + " has no any membership");
				continue;
			}
			ps.println("LogicalUnit " + lu.getRootNode().getKey()+" memberships: " + members.size());
			StringBuilder sb = new StringBuilder();
			for(Membership member : members){
				sb.append("    [").append(member.getType()).append("]");
				Multiplicity multi = member.getRelationship().getRoleMultiplicity(member.getActingRole());
				switch (multi){
				case only_one: 
					sb.append(" has a ");
					break;
				case no_repeat:
					sb.append(" has set of ");
					break;
				case many:
					sb.append(" has a list of ");
					break;
				default:
					sb.append(" (has ").append(multi).append(" of) ");
					break;
				}
				sb.append(member.getEntityNode().getKey()).append("\n");
			}
			ps.println(sb.toString());
		}
	}

	
}

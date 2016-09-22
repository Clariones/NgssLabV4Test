package test.dumper.artifact;

import java.util.Map;

import org.ngss.platform.actingrole.ActingRole;
import org.ngss.platform.buildplan.BuildPlan;
import org.ngss.platform.buildplan.BuildPlanInfo;
import org.ngss.platform.common.NgssContext;
import org.ngss.platform.logicalunit.LogicalUnit;
import org.ngss.platform.membership.Membership;
import org.ngss.platform.relationship.RelationshipAttributes;

public class BuildPlanDumper {

	private static ActingRole roleTo = NgssContext.getActingRoleRepository().getOrCreate(ActingRole.TO);

	public static String plan2String(BuildPlan plan) {
		StringBuilder sb = new StringBuilder();
		switch (plan.getType()) {
		case webConsole_reference_input:
			return dumpMemberPlan(plan, sb, "I want a SEARCH box, to select");
		case webConsole_readonly_input:
			return dumpMemberPlan(plan, sb, "I want a LABEL, only show value of");
		case webConsole_contains_input:
			return dumpMemberPlan(plan, sb, "I want a INPUT box, to directly input");
		case webConsole_submit_and_cancel:
			return "  I want submit and cancel functionality in this page for all fields";
		default:
			throw new RuntimeException(plan.getType() + " not supported yet");
		}
	}

	protected static String dumpMemberPlan(BuildPlan plan, StringBuilder sb, String description) {
		Map<BuildPlanInfo, Object> infos = plan.getPlanInfos();
		LogicalUnit masterLu = (LogicalUnit) infos.get(BuildPlanInfo.master_logicalUnit);
		LogicalUnit memberLu = (LogicalUnit) infos.get(BuildPlanInfo.member_logicalUnit);
		Membership membership = (Membership) infos.get(BuildPlanInfo.membership);
		String memberName = (String) membership.getRelationship().getRoleAttribute(roleTo,
				RelationshipAttributes.memberName);
		sb.append("  ").append(description).append(" ").append(masterLu.getRootNode().getName()).append(".")
				.append(memberName);
		String dataType = memberLu.getRootNode().getDataType();
		sb.append("(a ").append(dataType).append(")");
		return sb.toString();
	}

}

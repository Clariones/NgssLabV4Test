package test;

import java.util.Iterator;
import java.util.Set;

import org.ngss.platform.actingrole.ActingRole;
import org.ngss.platform.bizdomain.BizDomain;
import org.ngss.platform.common.NgssContext;
import org.ngss.platform.entitynode.EntityNode;
import org.ngss.platform.entitynode.EntityNodeFactory;
import org.ngss.platform.logicalunit.LogicalUnitFactory;
import org.ngss.platform.relationship.Relationship;
import org.ngss.platform.relationship.RelationshipAttributes;
import org.ngss.platform.relationship.RelationshipFactory;
import org.ngss.platform.relationshipdefinition.RelationshipDefinition;
import org.ngss.platform.relationshipdefinition.RelationshipDefinition.Multiplicity;

import com.terapico.system.CMRField;
import com.terapico.system.FieldDescriptor;
import com.terapico.system.ObjectCollection;
import com.terapico.system.ObjectDescriptor;

import test.dumper.DumperUtils;

public class DataModelMapper {

	protected BizDomain userDomain;
	protected BizDomain commonDomain;

	public void setupDatas(ObjectCollection oc) {
		setupDomains(oc);
		setupEntityNodes(oc);
		handleRefFields(oc);
		setupLogicalUnits();
	}

	private void setupLogicalUnits() {
		LogicalUnitFactory luFactory = NgssContext.getLogicalUnitFactory();
		RelationshipFactory relaFactory = NgssContext.getRelationshipFactory();
		Set<Relationship> allRelas = relaFactory.getAllRelationships();
		for (Relationship rela : allRelas) {
			rela.processMemberShip(luFactory);
		}
		DumperUtils.logicalUnitMembers(luFactory);
	}

	private void handleRefFields(ObjectCollection oc) {
		Iterator objDespIt = oc.getVector().iterator();
		EntityNodeFactory entityRepo = NgssContext.getEntityNodeFactory();
		while (objDespIt.hasNext()) {
			ObjectDescriptor objDesp = (ObjectDescriptor) objDespIt.next();
			Iterator refIt = objDesp.getRefer().iterator();
			while (refIt.hasNext()) {
				CMRField rf = (CMRField) refIt.next();

				String name = objDesp.getObjName();
				EntityNode ownerNode = entityRepo.getOrCreate(name, userDomain);
				String refType = rf.getType();
				EntityNode refNode = entityRepo.getOrCreate(refType, userDomain);
				assert(refNode.getRelationships().size() > 0) : "Should not be an empty node";

				for (Relationship rela : refNode.getRelationships()) {
					handleRefField(ownerNode, refNode, rela);
				}
			}
		}
		DumperUtils.entityNodes("\n===============After REF fields handled==============\n");
		DumperUtils.entityNodes(NgssContext.getEntityNodeFactory());
		DumperUtils.relationships("\n===============After REF fields handled==============\n");
		DumperUtils.relationships(NgssContext.getRelationshipFactory());
	}

	protected void handleRefField(EntityNode ownerNode, EntityNode refNode, Relationship rela) {
		ActingRole masterRole = rela.getDefinition().getMasterRole();
		if (!rela.getParticipants().containsValue(ownerNode)) {
			return;
		}
		if (!rela.getDefinition().getName().equals(RelationshipDefinition.HAS_MEMBER)) {
			return;
		}
		if (refNode == ownerNode) {
			return;
		}
		Relationship newRela = NgssContext.getRelationshipFactory().create(rela.getDefinition());
		ActingRole roleFrom = NgssContext.getActingRoleRepository().getOrCreate(ActingRole.FROM);
		ActingRole roleTo = NgssContext.getActingRoleRepository().getOrCreate(ActingRole.TO);
		newRela.addParticipant(roleFrom, ownerNode, null);
		newRela.addParticipant(roleTo, refNode, null);
		newRela.setParticipantAttribute(roleTo, RelationshipAttributes.multiplicity, Multiplicity.no_repeat);
		ownerNode.addRelationship(newRela);
	}

	protected void setupEntityNodes(ObjectCollection oc) {
		Iterator objDespIt = oc.getVector().iterator();
		while (objDespIt.hasNext()) {
			ObjectDescriptor objDesp = (ObjectDescriptor) objDespIt.next();
			setupEntity(objDesp);
			Iterator fldDespIt = objDesp.iterator();
			while (fldDespIt.hasNext()) {
				FieldDescriptor fldDesp = (FieldDescriptor) fldDespIt.next();
				setupContainsField(objDesp, fldDesp);
			}
			Iterator refIt = objDesp.getRefer().iterator();
			while (refIt.hasNext()) {
				DumperUtils.fieldDescription(refIt.next());
			}
		}
		DumperUtils.entityNodes(NgssContext.getEntityNodeFactory());
		DumperUtils.relationships(NgssContext.getRelationshipFactory());
	}

	private void setupContainsField(ObjectDescriptor od, FieldDescriptor fldDesp) {
		EntityNodeFactory entityRepo = NgssContext.getEntityNodeFactory();
		String name = od.getObjName();
		String memberName = fldDesp.getName();
		String type = fldDesp.getType();
		System.out.println(
				name + " has a " + (fldDesp.isObj() ? "Object " : "") + type + " member called: " + memberName);

		EntityNode owner = entityRepo.getOrCreate(name, userDomain);
		EntityNode member = null;
		if (fldDesp.isObj()) {
			member = entityRepo.getOrCreate(type, userDomain);
			RelationshipFactory relaFactory = NgssContext.getRelationshipFactory();
			RelationshipDefinition relDefHasMember = NgssContext.getRelationshipDefinitionRepository()
					.get(RelationshipDefinition.HAS_MEMBER, commonDomain);
			Relationship hasMember = relaFactory.create(relDefHasMember);
			ActingRole roleFrom = NgssContext.getActingRoleRepository().getOrCreate(ActingRole.FROM);
			ActingRole roleTo = NgssContext.getActingRoleRepository().getOrCreate(ActingRole.TO);
			hasMember.addParticipant(roleFrom, owner, null);
			hasMember.addParticipant(roleTo, member, null);
			if (fldDesp.isVerbField()){
				System.out.println("Found VERB");
				hasMember.setParticipantAttribute(roleTo, RelationshipAttributes.is_action, true);
			}
			owner.addRelationship(hasMember);
			return;
		}

		memberName = name + "_" + memberName;
		switch (type) {
		case "string_country_code":
		case "string_password":
		case "string":
			member = getEntity(EntityNode.STRING, memberName);
			break;
		case "date":
			member = getEntity(EntityNode.DATE, memberName);
			break;
		case "int":
			member = getEntity(EntityNode.INTEGER, memberName);
			break;
		case "double":
			member = getEntity(EntityNode.FLOAT, memberName);
			break;
		case "money":
			member = getEntity(EntityNode.MONEY, memberName);
			break;
		case "string_image":
			member = getEntity(EntityNode.IMAGE, memberName);
			break;
		case "string_email":
			member = getEntity(EntityNode.EMAIL, memberName);
			break;
		case "string_cn_phone":
			member = getEntity(EntityNode.PHONE_NUMBER, memberName);
			break;
		case "string_document":
			member = getEntity(EntityNode.DOCUMENT, memberName);
			break;
		case "bool":
			member = getEntity(EntityNode.BOOLEAN, memberName);
			break;
		}

		if (member == null) {
			throw new RuntimeException(fldDesp + " not handled");
		}
		RelationshipFactory relaFactory = NgssContext.getRelationshipFactory();
		RelationshipDefinition relDefHasMember = NgssContext.getRelationshipDefinitionRepository()
				.get(RelationshipDefinition.HAS_MEMBER, commonDomain);
		Relationship hasMember = relaFactory.create(relDefHasMember);
		ActingRole roleFrom = NgssContext.getActingRoleRepository().getOrCreate(ActingRole.FROM);
		ActingRole roleTo = NgssContext.getActingRoleRepository().getOrCreate(ActingRole.TO);
		hasMember.addParticipant(roleFrom, owner, null);
		hasMember.addParticipant(roleTo, member, null);
		memberName = fldDesp.getName();
		hasMember.setParticipantAttribute(roleTo, RelationshipAttributes.memberName, memberName);
		if (memberName.equals("id")) {
			hasMember.setParticipantAttribute(roleTo, RelationshipAttributes.is_identifier,
					RelationshipAttributes.IdentifierType.system_generated);
		}
		owner.addRelationship(hasMember);
	}

	private EntityNode getEntity(String propName, String memberName) {
		EntityNodeFactory entityRepo = NgssContext.getEntityNodeFactory();
		EntityNode propertyNode = entityRepo.getOrCreate(propName, commonDomain);
		EntityNode tgtNode = entityRepo.getOrCreate(memberName, userDomain);
		tgtNode.addPrototype(propertyNode);
		return tgtNode;
	}

	private void setupEntity(ObjectDescriptor od) {
		EntityNodeFactory entityRepo = NgssContext.getEntityNodeFactory();
		String name = od.getObjName();
		entityRepo.getOrCreate(name, userDomain);
	}

	protected void setupDomains(ObjectCollection oc) {
		this.userDomain = NgssContext.getBizDomainRepository().getOrCreate(oc.getName().toLowerCase());
		System.out.println("user domain is " + userDomain);
		commonDomain = NgssContext.getBizDomainRepository().getOrCreate(BizDomain.COMMON);
	}

}

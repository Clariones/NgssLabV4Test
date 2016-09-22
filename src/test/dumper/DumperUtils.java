package test.dumper;

import org.ngss.platform.entitynode.EntityNodeFactory;
import org.ngss.platform.logicalunit.LogicalUnitFactory;
import org.ngss.platform.relationship.RelationshipFactory;

import com.terapico.system.ObjectDescriptor;

public class DumperUtils {
	protected static Dumper objDesriptionDumper = new ObjDescriptionDumper();
	protected static Dumper erDicDumper = new ERDicDumper();
	protected static Dumper fieldDescriptionDumper = new FieldDesriptionDumper();
	protected static Dumper entityNodeRepositoryDumper = new EntityNodeRepositoryDumper();
	protected static Dumper relationshipRepositoryDumper = new RelationshipRepositoryDumper();
	protected static Dumper membershipDumper = new MembershipDumper();
	
	protected static ObjectDescriptor curObjDesp = null;
	
	public static void objectDescription(Object obj){
			doDump(objDesriptionDumper, obj);
			if (curObjDesp != obj){
				curObjDesp = (ObjectDescriptor) obj;
				doDump(fieldDescriptionDumper, obj);
			}
	}
	public static void erDic(Object obj){
		doDump(erDicDumper, obj);
	}

	private static void doDump(Dumper dumper, Object obj) {
		try {
			dumper.dump(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void fieldDescription(Object fldDesp) {
		doDump(fieldDescriptionDumper, fldDesp);
	}
	public static void entityNodes(Object entityNodeFactory) {
		doDump(entityNodeRepositoryDumper, entityNodeFactory);
	}
	public static void relationships(RelationshipFactory entityNodeFactory) {
		doDump(relationshipRepositoryDumper, entityNodeFactory);
	}
	public static void logicalUnitMembers(LogicalUnitFactory luFactory) {
		doDump(membershipDumper, luFactory);
	}
}

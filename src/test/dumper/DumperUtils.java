package test.dumper;

import java.util.List;

import org.ngss.platform.artifact.Artifact;

import com.terapico.system.ObjectDescriptor;

import test.dumper.artifact.ArtifactDumperManager;

public class DumperUtils {
	protected static Dumper objDesriptionDumper = new ObjDescriptionDumper();
	protected static Dumper erDicDumper = new ERDicDumper();
	protected static Dumper fieldDescriptionDumper = new FieldDesriptionDumper();
	protected static Dumper entityNodeRepositoryDumper = new EntityNodeRepositoryDumper();
	protected static Dumper relationshipRepositoryDumper = new RelationshipRepositoryDumper();
	protected static Dumper membershipDumper = new MembershipDumper();
	protected static Dumper relationshipStdoutDumper = new RelationshipStdoutDumper();
	protected static Dumper artifactDumperManager = new ArtifactDumperManager();
	
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
	public static void relationships(Object entityNodeFactory) {
		doDump(relationshipRepositoryDumper, entityNodeFactory);
	}
	public static void logicalUnitMembers(Object luFactory) {
		doDump(membershipDumper, luFactory);
	}
	public static void relationship(Object obj){
		doDump(relationshipStdoutDumper, obj);
	}
	public static void artifacts(List<Artifact> artifacts) {
		doDump(artifactDumperManager, artifacts);
	}
}

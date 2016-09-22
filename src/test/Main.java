package test;

import java.util.Iterator;
import java.util.Vector;

import com.terapico.system.ObjectCollection;
import com.terapico.system.ObjectCollectionHome;
import com.terapico.system.ObjectDescriptor;

import test.dumper.DumperUtils;

public class Main {
	static ProjectContext prjCtx = new ProjectContext();
	static DataModelMapper dmm = new DataModelMapper();
	
	public static void main(String[] args) throws Exception {
		
		// TODO Auto-generated method stub
		String name = "b2b";
		prjCtx.objectCollection=(ObjectCollection)ObjectCollectionHome.getHome(name);
		
		Vector objectDesps = prjCtx.objectCollection.getVector();
		Iterator objDespIt = objectDesps.iterator();
		while(objDespIt.hasNext()){
			ObjectDescriptor objDesp = (ObjectDescriptor) objDespIt.next();
			DumperUtils.objectDescription(objDesp);
			Iterator fldDespIt =  objDesp.iterator();
			while(fldDespIt.hasNext()){
				DumperUtils.fieldDescription(fldDespIt.next());
			}
			Iterator refIt = objDesp.getRefer().iterator();
			while(refIt.hasNext()){
				DumperUtils.fieldDescription(refIt.next());
			}
		}
		
		DumperUtils.erDic(prjCtx.objectCollection.getERDic());
		
		
		dmm.setupDatas(prjCtx.objectCollection);
	}
}

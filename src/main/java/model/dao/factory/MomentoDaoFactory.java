package model.dao.factory;

import model.dao.MomentoDao;
import model.dao.impl.MomentoDaoFirebaseImpl;
import model.dao.impl.MomentoDaoHibernateImpl;
import model.dao.impl.MusicaDaoFirebaseImpl;

/**
 * Factory de instancias da classe Momento
 * 
 * Pattern Data Access Object
 *
 * Mon Jan 26 22:35:34 BRST 2015
 *
 */
public class MomentoDaoFactory {

	public static final int HIBERNATE = 0;
	public static final int FIREBASE = 1;
	
	public static MomentoDao createMomentoDao(int whichFactory) {
		switch (whichFactory) {
	    	case HIBERNATE: 
	    		return new MomentoDaoHibernateImpl();
	    	case FIREBASE:
	    		return new MomentoDaoFirebaseImpl();
	    	default:
			return null;
		}
	}
	
	public static MomentoDao createMomentoDao() {
		return MomentoDaoFactory.createMomentoDao(MomentoDaoFactory.FIREBASE);
	}
	
}

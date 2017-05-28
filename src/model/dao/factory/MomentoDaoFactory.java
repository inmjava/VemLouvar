package model.dao.factory;

import model.dao.MomentoDao;
import model.dao.impl.MomentoDaoHibernateImpl;

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
	
	public static MomentoDao createMomentoDao(int whichFactory) {
		switch (whichFactory) {
	    	case HIBERNATE: 
	    		return new MomentoDaoHibernateImpl();
	    	default:
			return null;
		}
	}
	
	public static MomentoDao createMomentoDao() {
		return MomentoDaoFactory.createMomentoDao(MomentoDaoFactory.HIBERNATE);
	}
	
}

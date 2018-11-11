package model.dao.factory;

import model.dao.MusicaDao;
import model.dao.impl.MusicaDaoHibernateImpl;

/**
 * Factory de instancias da classe Musica
 * 
 * Pattern Data Access Object
 *
 * Mon Jan 26 22:35:34 BRST 2015
 *
 */
public class MusicaDaoFactory {
	
	public static final int HIBERNATE = 0;
	
	public static MusicaDao createMusicaDao(int whichFactory) {
		switch (whichFactory) {
	    	case HIBERNATE: 
	    		return new MusicaDaoHibernateImpl();
	    	default:
			return null;
		}
	}
	
	public static MusicaDao createMusicaDao() {
		return MusicaDaoFactory.createMusicaDao(MusicaDaoFactory.HIBERNATE);
	}
	
}

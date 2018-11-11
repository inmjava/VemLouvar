package model;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.MappingException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private static Session session;

	public static SessionFactory getSessionFactory() throws MappingException {
		if (sessionFactory == null) {
			Logger log = Logger.getLogger("org.hibernate");
			log.setLevel(Level.DEBUG);
			
			Configuration configuration = new Configuration();
		    configuration.configure();
		    StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			sessionFactory =  configuration.buildSessionFactory(serviceRegistry);
		}
		return sessionFactory;
	}

	public static Session getSession() {
		if(session == null){
			session = getSessionFactory().openSession();
		}
		return session;
	}
	
	public static void closeSessionFactory(){
		getSession().close();
		getSessionFactory().close();
		sessionFactory = null;
		session = null;
	}
}

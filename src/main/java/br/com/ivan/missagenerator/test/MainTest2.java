package br.com.ivan.missagenerator.test;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import model.HibernateUtil;
import model.Momento;
import model.Musica;
import model.dao.MomentoDao;
import model.dao.MusicaDao;
import model.dao.factory.MomentoDaoFactory;
import model.dao.factory.MusicaDaoFactory;

public class MainTest2 {

	private static Momento momento;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		boolean isTrue = false;
		
		MusicaDao musicaDao = MusicaDaoFactory.createMusicaDao();
		Musica musica = (Musica) musicaDao.listar(0);
		System.out.println(musica.getNome());
		MomentoDao momentoDao = MomentoDaoFactory.createMomentoDao();
		momento = momentoDao.listar(2);
		Momento momento3 = momentoDao.listar(3);
		musicaDao.salvar(musica);
		System.out.println(momento.getNome());
		System.out.println("=======");
		if(isTrue){
			momento.getMusicas().add(musica);
		} else {
			momento.getMusicas().remove(musica);
		}
		musicaDao.salvar(musica);
		for (Iterator iterator = musicaDao.listarMomentos(musica).iterator(); iterator.hasNext();) {
			Momento momento2 = (Momento) iterator.next();
			System.out.println(momento2);
			
		}
		System.out.println("=======");
		if(isTrue){
			momento3.getMusicas().add(musica);
		} else {
			momento3.getMusicas().remove(musica);
		}
		musicaDao.salvar(musica);
		musica = (Musica) musicaDao.listar(0);
		for (Iterator iterator = musicaDao.listarMomentos(musica).iterator(); iterator.hasNext();) {
			Momento momento2 = (Momento) iterator.next();
			System.out.println(momento2);
		}
		
		HibernateUtil.closeSessionFactory();
	}
	
	public static void main3(String[] args) {
		Session session = HibernateUtil.getSession();
		List list = session.createQuery("from Momento").list();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Momento momento = (Momento) iterator.next();
			System.out.println(momento);
			
		}
		HibernateUtil.closeSessionFactory();
	}
	
	public static void main4(String[] args) {
		String listas = "momentos";
		
		System.out.println((listas.charAt(0)+"").toUpperCase() + listas.substring(1));
		
	}
}

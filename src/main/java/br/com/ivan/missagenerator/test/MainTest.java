package br.com.ivan.missagenerator.test;

import model.HibernateUtil;
import model.Momento;
import model.Musica;
import model.dao.MomentoDao;
import model.dao.MusicaDao;
import model.dao.factory.MomentoDaoFactory;
import model.dao.factory.MusicaDaoFactory;

public class MainTest {

	public static void main(String[] args) throws Exception {
		
		MusicaDao md = MusicaDaoFactory.createMusicaDao();
		Musica musica = md.listar(0);
		
		MomentoDao md2 = MomentoDaoFactory.createMomentoDao();
		Momento momento = md2.listar(1);
		
		System.out.println(musica.getMomentos());
		System.out.println(momento.getMusicas());
		
		HibernateUtil.closeSessionFactory();
		
//		musica.getMomentos().add(momento);
//		md.salvar(musica);
	}
}

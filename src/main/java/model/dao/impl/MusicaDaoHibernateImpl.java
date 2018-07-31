package model.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;

import model.HibernateUtil;
import model.Musica;
import model.dao.MusicaDao;

/**
 * Implementacao do acesso a dados da classe Musica via 
 * Hibernate
 * 
 * Pattern Data Access Object
 *
 * Mon Jan 26 22:35:34 BRST 2015
 *
 */
 public class MusicaDaoHibernateImpl implements MusicaDao {
	
	private static boolean dev = false;
	
	public MusicaDaoHibernateImpl() {
		
	}
	
	/**
	 * Salva objeto da classe Musica.
	 * 
	 * @param musica Objeto da classe Musica
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public void salvar(Musica musica) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		try {
			session.save(musica);
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			if (dev) {
				e.printStackTrace();
			}
			throw e;
		} finally {
			//HibernateUtil.closeSession();
		}
	}

	
	/**
	 * Altera objeto da classe Musica.
	 * 
	 * @param musica Objeto da classe Musica
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public void alterar(Musica musica) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		try {
			session.update(musica);
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			if (dev) {
				e.printStackTrace();
			}
			throw e;
		} finally {
			//HibernateUtil.closeSession();
		}
	}
	
	/**
	 * Exclui objeto da classe Musica.
	 * 
	 * @param musica Objeto da classe Musica
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public void excluir(Musica musica) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		try {
			session.delete(musica);
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			if (dev) {
				e.printStackTrace();
			}
			throw e;
		} finally {
			//HibernateUtil.closeSession();
		}
	}
	
	/**
	 * Conta o n?mero objetos da classe Musica.
	 */
	public int contar() throws Exception {
		Session session = HibernateUtil.getSession();
		try {
			Criteria q = session.createCriteria(Musica.class).setProjection(Projections.rowCount());
			return (Integer) q.list().get(0);
		} catch (HibernateException e) {
			if (dev) {
				e.printStackTrace();
			}
			throw e;
		}
	}
	
	/**
	 * Listagem de objetos da classe Musica.
	 * 
	 * @param example Objeto com atributos para listagem 
	 * @param qtdPagina quantidade de itens listados em cada pagina 
	 * @param numPagina numero da pagina a ser buscada
	 * @return Lista de objetos Musica
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	@SuppressWarnings("unchecked")
	public Collection<Musica> listar(Musica example, Integer qtdPagina, Integer numPagina) throws Exception {
		Collection<Musica> coll = new ArrayList<Musica>();
		Session session = HibernateUtil.getSession();
		try {
			Criteria q = session.createCriteria(Musica.class);
			if(example != null){
				Example sample = Example.create(example);
				sample.enableLike();
				sample.excludeZeroes();
				q.add(sample);
			}
			if (qtdPagina != null && numPagina != null) {
				q.setMaxResults(qtdPagina.intValue());
				q.setFirstResult( (numPagina.intValue()-1) * qtdPagina.intValue() );
			}
			List<Musica> list = q.list();
			coll = list;
		} catch (HibernateException e) {
			if (dev) {
				e.printStackTrace();
			}
			throw e;
		} 
		return coll;
	}
	
	/**
	 * Listagem de objetos da classe Musica.
	 * 
	 * @param example Objeto com atributos para listagem 
	 * @return Lista de objetos Musica
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Collection<Musica> listar(Musica example) throws Exception {
		return this.listar(example, null, null);
	}
	
	/**
	 * Listagem de objetos da classe Musica.
	 * 
	 * @param qtdPagina quantidade de itens listados em cada pagina 
	 * @param numPagina numero da pagina a ser buscada
	 * @return Lista de objetos Musica
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Collection<Musica> listar(Integer qtdPagina, Integer numPagina) throws Exception {
		return this.listar(null, qtdPagina, numPagina);
	}
	
	/**
	 * Listagem de objetos da classe Musica.
	 * 
	 * @return Lista de objetos Musica
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Collection<Musica> listar() throws Exception {
		return this.listar(null, null, null);
	}
		
	/**
	 * Obtem um objeto da classe Musica.
	 * 
	 * @param id Chave primaria do objeto
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Musica listar(long id) throws Exception {
		Session session = HibernateUtil.getSession();
		try {
			return (Musica)session.get(Musica.class, id);
		} catch (HibernateException e) {
			if (dev) {
				e.printStackTrace();
			}
			throw e;
		}
	}
	
	/**
	 * Obtem objetos momentos da classe Musica.
	 * 
	 * @param id Chave primaria do objeto
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	@SuppressWarnings("unchecked")
	public List listarMomentos(long id) throws Exception {
		Session session = HibernateUtil.getSession();
		try {
			Query query = session.createQuery("select b from Musica a join a.momentos b where a.id = :id");
			query.setParameter("id", id);
			return query.list();
		} catch (HibernateException e) {
			if (dev) {
				e.printStackTrace();
			}
			throw e;
		} 
	}
	
	/**
	 * Obtem objetos momentos da classe Musica.
	 * 
	 * @param m Objeto Musica
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public List listarMomentos(Musica m) throws Exception {
		return this.listarMomentos(m.getId());
	}
}

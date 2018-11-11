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
import model.Momento;
import model.dao.MomentoDao;

/**
 * Implementacao do acesso a dados da classe Momento via 
 * Hibernate
 * 
 * Pattern Data Access Object
 *
 * Mon Jan 26 22:35:34 BRST 2015
 *
 */
 public class MomentoDaoHibernateImpl implements MomentoDao {
	
	private static boolean dev = false;
	
	public MomentoDaoHibernateImpl() {
		
	}
	
	/**
	 * Salva objeto da classe Momento.
	 * 
	 * @param momento Objeto da classe Momento
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public void salvar(Momento momento) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		try {
			session.save(momento);
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
	 * Altera objeto da classe Momento.
	 * 
	 * @param momento Objeto da classe Momento
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public void alterar(Momento momento) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		try {
			session.update(momento);
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
	 * Exclui objeto da classe Momento.
	 * 
	 * @param momento Objeto da classe Momento
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public void excluir(Momento momento) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		try {
			session.delete(momento);
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
	 * Conta o n?mero objetos da classe Momento.
	 */
	public int contar() throws Exception {
		Session session = HibernateUtil.getSession();
		try {
			Criteria q = session.createCriteria(Momento.class).setProjection(Projections.rowCount());
			return (Integer) q.list().get(0);
		} catch (HibernateException e) {
			if (dev) {
				e.printStackTrace();
			}
			throw e;
		}
	}
	
	/**
	 * Listagem de objetos da classe Momento.
	 * 
	 * @param example Objeto com atributos para listagem 
	 * @param qtdPagina quantidade de itens listados em cada pagina 
	 * @param numPagina numero da pagina a ser buscada
	 * @return Lista de objetos Momento
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	@SuppressWarnings("unchecked")
	public Collection<Momento> listar(Momento example, Integer qtdPagina, Integer numPagina) throws Exception {
		Collection<Momento> coll = new ArrayList<Momento>();
		Session session = HibernateUtil.getSession();
		try {
			Criteria q = session.createCriteria(Momento.class);
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
			List<Momento> list = q.list();
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
	 * Listagem de objetos da classe Momento.
	 * 
	 * @param example Objeto com atributos para listagem 
	 * @return Lista de objetos Momento
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Collection<Momento> listar(Momento example) throws Exception {
		return this.listar(example, null, null);
	}
	
	/**
	 * Listagem de objetos da classe Momento.
	 * 
	 * @param qtdPagina quantidade de itens listados em cada pagina 
	 * @param numPagina numero da pagina a ser buscada
	 * @return Lista de objetos Momento
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Collection<Momento> listar(Integer qtdPagina, Integer numPagina) throws Exception {
		return this.listar(null, qtdPagina, numPagina);
	}
	
	/**
	 * Listagem de objetos da classe Momento.
	 * 
	 * @return Lista de objetos Momento
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Collection<Momento> listar() throws Exception {
		return this.listar(null, null, null);
	}
		
	/**
	 * Obtem um objeto da classe Momento.
	 * 
	 * @param id Chave primaria do objeto
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Momento listar(long id) throws Exception {
		Session session = HibernateUtil.getSession();
		try {
			return (Momento)session.get(Momento.class, id);
		} catch (HibernateException e) {
			if (dev) {
				e.printStackTrace();
			}
			throw e;
		}
	}
	
	/**
	 * Obtem objetos musicas da classe Momento.
	 * 
	 * @param id Chave primaria do objeto
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	@SuppressWarnings("unchecked")
	public List listarMusicas(long id, String filtro) throws Exception {
		Session session = HibernateUtil.getSession();
		try {
			Query query = null;
			if(filtro == null || filtro.equals("")){
				query = session.createQuery("select b from Momento a join a.musicas b where a.id = :id");
			} else {
				query = session.createQuery("select b from Momento a join a.musicas b where a.id = :id and (b.nome like :filtro or b.apresentacao like :filtro)");
				query.setParameter("filtro", "%" + filtro + "%");
			}
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
	 * Obtem objetos musicas da classe Momento.
	 * 
	 * @param id Chave primaria do objeto
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	@SuppressWarnings("unchecked")
	public List listarMomentosPorFiltroMusica(String filtro) throws Exception {
		Session session = HibernateUtil.getSession();
		try {
			Query query = null;
			if(filtro == null || filtro.equals("")){
				query = session.createQuery("select a from Momento a");
			} else {
				query = session.createQuery("select distinct a from Momento a join a.musicas b where (b.nome like :filtro or b.apresentacao like :filtro) order by a.id");
				query.setParameter("filtro", "%" + filtro + "%");
			}
			return query.list();
		} catch (HibernateException e) {
			if (dev) {
				e.printStackTrace();
			}
			throw e;
		} 
	}
	
	public static void main(String[] args) throws Exception {
		List l = new MomentoDaoHibernateImpl().listarMomentosPorFiltroMusica("deixa");
		for (Object object : l) {
			System.out.println(object);
		}
	}
	
	/**
	 * Obtem objetos musicas da classe Momento.
	 * 
	 * @param id Chave primaria do objeto
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	@SuppressWarnings("unchecked")
	public List listarMusicas(long id) throws Exception {
		return listarMusicas(id, null);
	}
	
	/**
	 * Obtem objetos musicas da classe Momento.
	 * 
	 * @param m Objeto Momento
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public List listarMusicas(Momento m) throws Exception {
		return this.listarMusicas(m.getId());
	}

	@Override
	public List listarMusicas(Momento m, String filtro) throws Exception {
		return this.listarMusicas(m.getId(),filtro);
	}
}

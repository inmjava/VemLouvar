package model.dao;

import java.util.Collection;
import java.util.List;

import model.Momento;

/**
 * Interface para controle de objetos persistentes da classe Momento
 * 
 * Pattern Data Access Object
 * 
 * Mon Jan 26 22:35:34 BRST 2015
 *
 */
public interface MomentoDao {
	
	/**
	 * Salva objeto da classe Momento.
	 * 
	 * @param momento Objeto da classe Momento
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public void salvar(Momento momento) throws Exception;

	/**
	 * Altera objeto da classe Momento.
	 * 
	 * @param momento Objeto da classe Momento
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public void alterar(Momento momento) throws Exception;
	
	/**
	 * Exclui objeto da classe Momento.
	 * 
	 * @param momento Objeto da classe Momento
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public void excluir(Momento momento) throws Exception;
	
	/**
	 * Conta o n?mero objetos da classe Momento.
	 */
	public int contar() throws Exception;
	
	/**
	 * Listagem de objetos da classe Momento.
	 * 
	 * @param example Objeto com atributos para listagem 
	 * @param qtdPagina quantidade de itens listados em cada pagina 
	 * @param numPagina numero da pagina a ser buscada
	 * @return Lista de objetos Momento
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Collection<Momento> listar(Momento example, Integer qtdPagina, Integer numPagina) throws Exception;
	
	/**
	 * Listagem de objetos da classe Momento.
	 * 
	 * @param example Objeto com atributos para listagem 
	 * @return Lista de objetos Momento
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Collection<Momento> listar(Momento example) throws Exception;
	
	/**
	 * Listagem de objetos da classe Momento.
	 * 
	 * @param qtdPagina quantidade de itens listados em cada pagina 
	 * @param numPagina numero da pagina a ser buscada
	 * @return Lista de objetos Momento
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Collection<Momento> listar(Integer qtdPagina, Integer numPagina) throws Exception;
	
	/**
	 * Listagem de objetos da classe Momento.
	 * 
	 * @return Lista de objetos Momento
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Collection<Momento> listar() throws Exception;
	
	/**
	 * Obtem um objeto da classe Momento.
	 * 
	 * @param id Chave primaria do objeto
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Momento listar(long id) throws Exception;
	
	/**
	 * Obtem objetos musicas da classe Momento.
	 * 
	 * @param id Chave primaria do objeto
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public List listarMusicas(long id) throws Exception;
	
	/**
	 * Obtem objetos musicas da classe Momento.
	 * 
	 * @param m Objeto Momento
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public List listarMusicas(Momento m) throws Exception;
	
	/**
	 * Obtem objetos musicas da classe Momento.
	 * 
	 * @param m Objeto Momento
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public List listarMusicas(Momento m, String filtro) throws Exception;
	
	/**
	 * Obtem objetos musicas da classe Momento.
	 * 
	 * @param id Chave primaria do objeto
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	@SuppressWarnings("unchecked")
	public List listarMomentosPorFiltroMusica(String filtro) throws Exception;
	
}

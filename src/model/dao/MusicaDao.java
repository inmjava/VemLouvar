package model.dao;

import java.util.Collection;
import java.util.List;

import model.Musica;

/**
 * Interface para controle de objetos persistentes da classe Musica
 * 
 * Pattern Data Access Object
 * 
 * Mon Jan 26 22:35:34 BRST 2015
 *
 */
public interface MusicaDao {
	
	/**
	 * Salva objeto da classe Musica.
	 * 
	 * @param musica Objeto da classe Musica
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public void salvar(Musica musica) throws Exception;

	/**
	 * Altera objeto da classe Musica.
	 * 
	 * @param musica Objeto da classe Musica
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public void alterar(Musica musica) throws Exception;
	
	/**
	 * Exclui objeto da classe Musica.
	 * 
	 * @param musica Objeto da classe Musica
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public void excluir(Musica musica) throws Exception;
	
	/**
	 * Conta o n?mero objetos da classe Musica.
	 */
	public int contar() throws Exception;
	
	/**
	 * Listagem de objetos da classe Musica.
	 * 
	 * @param example Objeto com atributos para listagem 
	 * @param qtdPagina quantidade de itens listados em cada pagina 
	 * @param numPagina numero da pagina a ser buscada
	 * @return Lista de objetos Musica
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Collection<Musica> listar(Musica example, Integer qtdPagina, Integer numPagina) throws Exception;
	
	/**
	 * Listagem de objetos da classe Musica.
	 * 
	 * @param example Objeto com atributos para listagem 
	 * @return Lista de objetos Musica
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Collection<Musica> listar(Musica example) throws Exception;
	
	/**
	 * Listagem de objetos da classe Musica.
	 * 
	 * @param qtdPagina quantidade de itens listados em cada pagina 
	 * @param numPagina numero da pagina a ser buscada
	 * @return Lista de objetos Musica
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Collection<Musica> listar(Integer qtdPagina, Integer numPagina) throws Exception;
	
	/**
	 * Listagem de objetos da classe Musica.
	 * 
	 * @return Lista de objetos Musica
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Collection<Musica> listar() throws Exception;
	
	/**
	 * Obtem um objeto da classe Musica.
	 * 
	 * @param id Chave primaria do objeto
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Musica listar(long id) throws Exception;
	
	/**
	 * Obtem objetos momentos da classe Musica.
	 * 
	 * @param id Chave primaria do objeto
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public List listarMomentos(long id) throws Exception;
	
	/**
	 * Obtem objetos momentos da classe Musica.
	 * 
	 * @param m Objeto Musica
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public List listarMomentos(Musica m) throws Exception;
	
}

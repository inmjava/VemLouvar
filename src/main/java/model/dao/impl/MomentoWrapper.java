package model.dao.impl;

import java.util.ArrayList;
import java.util.List;

import model.Momento;
import model.Musica;

public class MomentoWrapper {
	private Long id;
	private String nome;
	private List<Long> musicas;

	public MomentoWrapper() {
	}

	public MomentoWrapper(Momento m) {
		id = m.getId();
		nome = m.getNome();
		musicas = new ArrayList<Long>();
		for (Object o : m.getMusicas()) {
			musicas.add(((Musica) o).getId());
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Long> getMusicas() {
		return musicas;
	}

	public void setMusicas(List<Long> musicas) {
		this.musicas = musicas;
	}

}

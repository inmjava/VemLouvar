package model.dao.impl;

import java.util.ArrayList;
import java.util.List;

import model.Momento;
import model.Musica;

public class MusicaWrapper {
	private Long id;
	private String nome;
	private String cifra;
	private String apresentacao;
	private String link;
	private List<Long> momentos;

	public MusicaWrapper() {
	}

	public MusicaWrapper(Musica m) {
		id = m.getId();
		nome = m.getNome();
		cifra = m.getCifra();
		apresentacao = m.getApresentacao();
		link = m.getLink();
		momentos = new ArrayList<>();
		for (Object o : m.getMomentos()) {
			momentos.add(((Momento) o).getId());
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

	public String getCifra() {
		return cifra;
	}

	public void setCifra(String cifra) {
		this.cifra = cifra;
	}

	public String getApresentacao() {
		return apresentacao;
	}

	public void setApresentacao(String apresentacao) {
		this.apresentacao = apresentacao;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public List<Long> getMomentos() {
		return momentos;
	}

	public void setMomentos(List<Long> momentos) {
		this.momentos = momentos;
	}
}

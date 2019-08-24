package br.com.ivan.missagenerator.test;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.HibernateUtil;
import model.Momento;
import model.Musica;
import model.dao.MomentoDao;
import model.dao.MusicaDao;
import model.dao.factory.MomentoDaoFactory;
import model.dao.factory.MusicaDaoFactory;

public class MainTest {

	public static void main2(String[] args) throws Exception {

		MusicaDao md = MusicaDaoFactory.createMusicaDao();
		Musica musica = md.listar(0);

		MomentoDao md2 = MomentoDaoFactory.createMomentoDao();
		Momento momento = md2.listar(1);

		System.out.println(musica.getMomentos());
		System.out.println(momento.getMusicas());

		HibernateUtil.closeSessionFactory();

//			musica.getMomentos().add(momento);
//			md.salvar(musica);
	}

	public static void main4(String[] args) throws Exception {
		FileInputStream serviceAccount = new FileInputStream(
				"db/vemlouvar-30a00-firebase-adminsdk-vii8z-675b00f05d.json");

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://vemlouvar-30a00.firebaseio.com").build();

		FirebaseApp.initializeApp(options);

		// Get a reference to our posts
		final FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference ref = database.getReference("musicas");

		ref.addChildEventListener(new ChildEventListener() {

			@Override
			public void onChildRemoved(DataSnapshot snapshot) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
				System.out.println(snapshot);

			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub

			}
		});
		Scanner scanner = new Scanner(System.in);
		while (!scanner.nextLine().equals("Quit")) {
		}
		System.exit(0);

	}

	public static void main(String[] args) throws Exception {

//			BasicConfigurator.configure();

		FileInputStream serviceAccount = new FileInputStream(
				"db/vemlouvar-30a00-firebase-adminsdk-vii8z-675b00f05d.json");

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://vemlouvar-30a00.firebaseio.com").build();

		FirebaseApp.initializeApp(options);

		// Get a reference to our posts
		final FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference ref = database.getReference("musicas");

		MusicaDao musicaDao = MusicaDaoFactory.createMusicaDao();
		Collection<Musica> musgas = musicaDao.listar();
		for (Musica musica : musgas) {
			ref.push().setValue(new MinhaMusica(musica), null);
			System.out.println(musica.getNome());
		}
		
		ref = database.getReference("momentos");
		MomentoDao momentoDao = MomentoDaoFactory.createMomentoDao();
		Collection<Momento> momentos = momentoDao.listar();
		for (Momento momento : momentos) {
			ref.push().setValue(new MeuMomento(momento), null);
			System.out.println(momento.getNome());
		}

		Scanner scanner = new Scanner(System.in);
		while (!scanner.nextLine().equals("Quit")) {
		}
		System.exit(0);
	}

}

class MeuMomento {
	private Long id;
	private String nome;
	private List musicas;

	public MeuMomento(Momento m) {
		id = m.getId();
		nome = m.getNome();
		musicas = new ArrayList();
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

	public List getMusicas() {
		return musicas;
	}

	public void setMusicas(List musicas) {
		this.musicas = musicas;
	}

}

class MinhaMusica {
	private Long id;
	private String nome;
	private String cifra;
	private String apresentacao;
	private String link;
	private List momentos;

	public MinhaMusica(Musica m) {
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

	public List getMomentos() {
		return momentos;
	}

	public void setMomentos(List momentos) {
		this.momentos = momentos;
	}
}

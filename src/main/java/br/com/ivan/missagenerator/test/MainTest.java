package br.com.ivan.missagenerator.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

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

	public static FirebaseDatabase getMyFirbaseDataBase() {

		try {
			// BasicConfigurator.configure();
			FileInputStream serviceAccount = new FileInputStream(
					"db/vemlouvar-30a00-firebase-adminsdk-vii8z-675b00f05d.json");

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://vemlouvar-30a00.firebaseio.com").build();

			FirebaseApp.initializeApp(options);
			return FirebaseDatabase.getInstance();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	public static FirebaseDatabase getMyFirestore() {

		try {
			// BasicConfigurator.configure();
			FileInputStream serviceAccount = new FileInputStream(
					"db/vemlouvar-30a00-firebase-adminsdk-vii8z-675b00f05d.json");

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://vemlouvar-30a00.firebaseio.com").build();

			FirebaseApp.initializeApp(options);
			return FirebaseDatabase.getInstance();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	public static void main5(String[] args) {

		FirebaseDatabase myFirbaseDataBase = getMyFirbaseDataBase();
		DatabaseReference myRef = myFirbaseDataBase.getReference();

//		carregarMyTest(myRef);
//		readMyTest(myRef);
		readMusicaWrapper(myRef);
//		carregarMusicasEMomentos(myRef);
//		readOneMusicaWrapper(myRef);
//		readOneMusicaByValue(myRef);

		waiting();

	}

	public static Firestore getMyFirestoreDataBase() {

		try {
			// BasicConfigurator.configure();
			FileInputStream serviceAccount = new FileInputStream(
					"db/vemlouvar-30a00-firebase-adminsdk-vii8z-675b00f05d.json");

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://vemlouvar-30a00.firebaseio.com").build();

			FirebaseApp.initializeApp(options);
			return FirestoreClient.getFirestore();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws Exception {

		Firestore db = getMyFirestoreDataBase();

		ApiFuture<QuerySnapshot> apiFuture = db.collection("musicas").whereEqualTo("nome", "Sl 24").get();

		List<QueryDocumentSnapshot> documents = apiFuture.get().getDocuments();
		for (DocumentSnapshot document : documents) {
			System.out.println(document.getId() + " => " + document.toObject(MusicaWrapper.class).getNome());
		}

	}

	private static void carregarMusicasEMomentos(Firestore db) throws Exception {
		CollectionReference collection = db.collection("musicas");

		MusicaDao musicaDao = MusicaDaoFactory.createMusicaDao();
		Collection<Musica> musgas = musicaDao.listar();
		for (Musica musica : musgas) {
			collection.document(musica.getId() + "").set(new MusicaWrapper(musica));
		}
		System.out.println("foi1");

		collection = db.collection("momentos");

		MomentoDao momentoDao = MomentoDaoFactory.createMomentoDao();
		Collection<Momento> momentos = momentoDao.listar();
		for (Momento momento : momentos) {
			collection.document(momento.getId() + "").set(new MomentoWrapper(momento));
		}
		System.out.println("foi2");
	}

	private static void readOneMusicaByValue(DatabaseReference myDb) {
		DatabaseReference myRef = myDb.child("musicas/");
		myRef.addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				System.out.println(snapshot.child("nome").getValue());

			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub

			}
		});

	}

	private static void carregarMusicasEMomentos(DatabaseReference myRef) {
		try {
			// Get a reference to our posts
			final FirebaseDatabase database = FirebaseDatabase.getInstance();
			DatabaseReference ref = database.getReference("musicas");

			MusicaDao musicaDao = MusicaDaoFactory.createMusicaDao();
			Collection<Musica> musgas = musicaDao.listar();
			for (Musica musica : musgas) {
				ref = database.getReference("musicas/" + musica.getId());
				ref.setValue(new MusicaWrapper(musica), null);
				System.out.println(musica.getNome());
			}

			ref = database.getReference("momentos");
			MomentoDao momentoDao = MomentoDaoFactory.createMomentoDao();
			Collection<Momento> momentos = momentoDao.listar();
			for (Momento momento : momentos) {
				ref = database.getReference("momentos/" + momento.getId());
				ref.setValue(new MomentoWrapper(momento), null);
				System.out.println(momento.getNome());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void readOneMusicaWrapper(DatabaseReference myDb) {
		DatabaseReference myRef = myDb.child("musicas/");
		myRef.child("52").addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				System.out.println(snapshot.child("nome").getValue());

			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub

			}
		});

	}

	private static void readMyTest(DatabaseReference myDb) {

		DatabaseReference myRef = myDb.child("mytest/");

		new Thread(new Runnable() {

			@Override
			public void run() {
				myRef.addChildEventListener(new ChildEventListener() {

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
						MyWrapper value = snapshot.getValue(MyWrapper.class);
						System.out.println(value.getTest());

					}

					@Override
					public void onCancelled(DatabaseError error) {
						// TODO Auto-generated method stub

					}
				});

			}
		}).start();
	}

	private static void readMusicaWrapper(DatabaseReference myDb) {

		DatabaseReference myRef = myDb.child("musicas/");

		SnapshotWrapper sw = new SnapshotWrapper();
		myRef.addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
					System.out.println(dataSnapshot.getValue(MusicaWrapper.class).getId());
				}
			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub

			}
		});

	}

	private static void carregarMyTest(DatabaseReference myDb) {
		for (int i = 0; i < 1000; i++) {
			MyWrapper mw = new MyWrapper();
			mw.setTest(i + 1);
			DatabaseReference myRef = myDb.child("mytest/" + mw.getTest());
			myRef.setValue(mw, new CompletionListener() {

				@Override
				public void onComplete(DatabaseError error, DatabaseReference ref) {
					System.out.println("foi");

				}
			});
		}
	}

	public static void main4(String[] args) throws Exception {

//		BasicConfigurator.configure();
		FileInputStream serviceAccount = new FileInputStream(
				"db/vemlouvar-30a00-firebase-adminsdk-vii8z-675b00f05d.json");

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://vemlouvar-30a00.firebaseio.com").build();

		FirebaseApp.initializeApp(options);

		// Get a reference to our posts
		final FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference ref = database.getReference("momentos");

		ref.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
				String momentoJson = dataSnapshot.getValue().toString();
				System.out.println(momentoJson);
				MomentoWrapper momento = new Gson().fromJson(momentoJson, MomentoWrapper.class);
				System.out.println(momento.getNome());
			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) {
			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				System.out.println("Pau!");
				System.out.println(databaseError);
			}
		});

		System.out.println("Terminou");

		Scanner scanner = new Scanner(System.in);
		while (!scanner.nextLine().equals("Quit")) {
		}
		scanner.close();
		System.exit(0);

	}

	public static void waiting() {

//		Scanner scanner = new Scanner(System.in);
//		while (!scanner.nextLine().equals("Quit")) {
//		}
//		scanner.close();
//		System.exit(0);
//		
		try {
			Thread.sleep(10000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main3(String[] args) throws Exception {

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
			ref.push().setValue(new MusicaWrapper(musica), null);
			System.out.println(musica.getNome());
		}

		ref = database.getReference("momentos");
		MomentoDao momentoDao = MomentoDaoFactory.createMomentoDao();
		Collection<Momento> momentos = momentoDao.listar();
		for (Momento momento : momentos) {
			ref.push().setValue(new MomentoWrapper(momento), null);
			System.out.println(momento.getNome());
		}

		Scanner scanner = new Scanner(System.in);
		while (!scanner.nextLine().equals("Quit")) {
		}
		System.exit(0);
	}

}

class SnapshotWrapper {
	public DataSnapshot ds = null;
}

class MomentoWrapper {
	private Long id;
	private String nome;
	private List musicas;

	public MomentoWrapper() {
	}

	public MomentoWrapper(Momento m) {
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

class MusicaWrapper {
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

	class MomentoWrapper2 {
		private String id;
		private String nome;
		private List musicas;

		private MomentoWrapper2() {
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
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

	class MusicaWrapper2 {
		private String id;
		private String nome;
		private String cifra;
		private String apresentacao;
		private String link;
		private List momentos;

		public MusicaWrapper2() {
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
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

		public List<String> getMomentos() {
			return momentos;
		}

		public void setMomentos(List<String> momentos) {
			this.momentos = momentos;
		}
	}
}

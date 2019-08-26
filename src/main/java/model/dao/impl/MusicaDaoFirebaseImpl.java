package model.dao.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.NotImplementedException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query.Direction;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import model.Musica;
import model.dao.MusicaDao;

public class MusicaDaoFirebaseImpl implements MusicaDao {
	
	private static final String DB_NAME = "musicas";

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
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Firestore db = getMyFirestoreDataBase();
//		System.out.println((db.collection(DB_NAME).orderBy("id", Direction.DESCENDING).limit(1).get().get().getDocuments().get(0).getLong("id") + 1));;
		
		System.out.println(db.collection(DB_NAME).get().get().size());
		
	}

	@Override
	public void salvar(Musica musica) throws Exception {
		Firestore db = getMyFirestoreDataBase();
		long proximoId = db.collection(DB_NAME).orderBy("id", Direction.DESCENDING).limit(1).get().get().getDocuments().get(0).getLong("id") + 1;
		musica.setId(proximoId);
		db.collection(DB_NAME).document(musica.getId()+"").set(new MusicaWrapper(musica));

	}

	@Override
	public void alterar(Musica musica) throws Exception {
		Firestore db = getMyFirestoreDataBase();
		db.collection(DB_NAME).document(musica.getId()+"").set(new MusicaWrapper(musica));
	}

	@Override
	public void excluir(Musica musica) throws Exception {
		Firestore db = getMyFirestoreDataBase();
		db.collection(DB_NAME).document(musica.getId()+"").delete();

	}

	@Override
	public int contar() throws Exception {
		Firestore db = getMyFirestoreDataBase();
		return db.collection(DB_NAME).get().get().size();
	}

	@Override
	public Collection<Musica> listar(Musica example, Integer qtdPagina, Integer numPagina) throws Exception {
		throw new NotImplementedException("Não foi implementado listar(Musica example, Integer qtdPagina, Integer numPagina) => MusicaDaoFirebaseImpl");
	}

	@Override
	public Collection<Musica> listar(Musica example) throws Exception {
		Firestore db = getMyFirestoreDataBase();
		CollectionReference collection = db.collection(DB_NAME);

		if(example.getId() != null) {
			collection.whereEqualTo("id", example.getId());
		}
		if(example.getNome() != null) {
			collection.whereEqualTo("nome", example.getNome());
		}
		if(example.getApresentacao() != null) {
			collection.whereEqualTo("apresentacao", example.getApresentacao());
		}
		if(example.getCifra() != null) {
			collection.whereEqualTo("cifra", example.getCifra());
		}
		List<QueryDocumentSnapshot> documents = collection.get().get().getDocuments();
		for (QueryDocumentSnapshot document : documents) {
			// continue here
		}
		return null;
//		return .whereEqualTo("nome", example.getNome());
	}

	@Override
	public Collection<Musica> listar(Integer qtdPagina, Integer numPagina) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Musica> listar() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Musica listar(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List listarMomentos(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List listarMomentos(Musica m) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}

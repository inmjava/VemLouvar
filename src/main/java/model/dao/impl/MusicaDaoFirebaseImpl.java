package model.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.NotImplementedException;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query.Direction;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import br.com.ivan.missagenerator.business.FirebaseUtils;
import model.Momento;
import model.Musica;
import model.dao.MusicaDao;

public class MusicaDaoFirebaseImpl implements MusicaDao {

	private static final String DB_NAME = "musicas";
	private static final String DB_NAME_CHILD = "momentos";

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Firestore db = FirebaseUtils.getMyFirestoreDataBase();
//		System.out.println((db.collection(DB_NAME).orderBy("id", Direction.DESCENDING).limit(1).get().get().getDocuments().get(0).getLong("id") + 1));;
		
		System.out.println(db.collection(DB_NAME).get().get().size());
		
	}

	@Override
	public void salvar(Musica musica) throws Exception {
		Firestore db = FirebaseUtils.getMyFirestoreDataBase();
		long proximoId = db.collection(DB_NAME).orderBy("id", Direction.DESCENDING).limit(1).get().get().getDocuments().get(0).getLong("id") + 1;
		musica.setId(proximoId);
		db.collection(DB_NAME).document(musica.getId()+"").set(new MusicaWrapper(musica));

	}

	@Override
	public void alterar(Musica musica) throws Exception {
		Firestore db = FirebaseUtils.getMyFirestoreDataBase();
		db.collection(DB_NAME).document(musica.getId()+"").set(new MusicaWrapper(musica));
	}

	@Override
	public void excluir(Musica musica) throws Exception {
		Firestore db = FirebaseUtils.getMyFirestoreDataBase();
		db.collection(DB_NAME).document(musica.getId()+"").delete();

	}

	@Override
	public int contar() throws Exception {
		Firestore db = FirebaseUtils.getMyFirestoreDataBase();
		return db.collection(DB_NAME).get().get().size();
	}

	@Override
	public Collection<Musica> listar(Musica example, Integer qtdPagina, Integer numPagina) throws Exception {
		throw new NotImplementedException("N�o foi implementado listar(Musica example, Integer qtdPagina, Integer numPagina) => MusicaDaoFirebaseImpl");
	}

	@Override
	public Collection<Musica> listar(Musica example) throws Exception {
		Firestore db = FirebaseUtils.getMyFirestoreDataBase();
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
		ArrayList<MusicaWrapper> listMW = new ArrayList<>();
		for (QueryDocumentSnapshot document : documents) {
			listMW.add(document.toObject(MusicaWrapper.class));
		}
		return convertListMW2ListM(listMW);
	}

	private Collection<Musica> convertListMW2ListM(ArrayList<MusicaWrapper> listMW) {
		ArrayList<Musica> listM = new ArrayList<>();
		for (MusicaWrapper musicaWrapper : listMW) {
			listM.add(new Musica(musicaWrapper));
		}
		return listM;
	}

	@Override
	public Collection<Musica> listar(Integer qtdPagina, Integer numPagina) throws Exception {
		throw new NotImplementedException("N�o foi implementado listar(Integer qtdPagina, Integer numPagina) => MusicaDaoFirebaseImpl");
	}

	@Override
	public Collection<Musica> listar() throws Exception {
		return listar(new Musica());
	}

	@Override
	public Musica listar(long id) throws Exception {
		Firestore db = FirebaseUtils.getMyFirestoreDataBase();
		CollectionReference collection = db.collection(DB_NAME);
		MusicaWrapper musicaWrapper = collection.document(""+id).get().get().toObject(MusicaWrapper.class);
		return new Musica(musicaWrapper);
	}

	@Override
	public List listarMomentos(long id) throws Exception {
		Musica musica = listar(id);
		ArrayList<Momento> momentosList = new ArrayList<>();
		for (Object momentoObj : musica.getMomentos()) {
			Long momentoL = (Long) momentoObj;
			Firestore db = FirebaseUtils.getMyFirestoreDataBase();
			CollectionReference collection = db.collection(DB_NAME_CHILD);
			MomentoWrapper momentoWrapper = collection.document(""+momentoL).get().get().toObject(MomentoWrapper.class);
			momentosList.add(new Momento(momentoWrapper));
		}
		return momentosList;
	}

	@Override
	public List listarMomentos(Musica m) throws Exception {
		return listarMomentos(m.getId());
	}

}

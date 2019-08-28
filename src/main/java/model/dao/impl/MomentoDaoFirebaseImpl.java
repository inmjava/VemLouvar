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
import model.dao.MomentoDao;

public class MomentoDaoFirebaseImpl implements MomentoDao {

	private static final String DB_NAME = "momentos";
	private static final String DB_NAME_CHILD = "musicas";
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Firestore db = FirebaseUtils.getMyFirestoreDataBase();
		System.out.println(db.collection(DB_NAME).get().get().toObjects(MomentoWrapper.class).get(0).getMusicas());
	}

	@Override
	public void salvar(Momento momento) throws Exception {
		Firestore db = FirebaseUtils.getMyFirestoreDataBase();
		long proximoId = db.collection(DB_NAME).orderBy("id", Direction.DESCENDING).limit(1).get().get().getDocuments().get(0).getLong("id") + 1;
		momento.setId(proximoId);
		db.collection(DB_NAME).document(momento.getId()+"").set(new MomentoWrapper(momento));

	}

	@Override
	public void alterar(Momento momento) throws Exception {
		Firestore db = FirebaseUtils.getMyFirestoreDataBase();
		db.collection(DB_NAME).document(momento.getId()+"").set(new MomentoWrapper(momento));
	}

	@Override
	public void excluir(Momento momento) throws Exception {
		Firestore db = FirebaseUtils.getMyFirestoreDataBase();
		db.collection(DB_NAME).document(momento.getId()+"").delete();

	}

	@Override
	public int contar() throws Exception {
		Firestore db = FirebaseUtils.getMyFirestoreDataBase();
		return db.collection(DB_NAME).get().get().size();
	}

	@Override
	public Collection<Momento> listar(Momento example, Integer qtdPagina, Integer numPagina) throws Exception {
		throw new NotImplementedException("N�o foi implementado listar(Momento example, Integer qtdPagina, Integer numPagina) => MomentoDaoFirebaseImpl");
	}

	@Override
	public Collection<Momento> listar(Momento example) throws Exception {
		Firestore db = FirebaseUtils.getMyFirestoreDataBase();
		CollectionReference collection = db.collection(DB_NAME);

		if(example.getId() != null) {
			collection.whereEqualTo("id", example.getId());
		}
		if(example.getNome() != null) {
			collection.whereEqualTo("nome", example.getNome());
		}
		List<QueryDocumentSnapshot> documents = collection.get().get().getDocuments();
		ArrayList<MomentoWrapper> listMW = new ArrayList<>();
		for (QueryDocumentSnapshot document : documents) {
			listMW.add(document.toObject(MomentoWrapper.class));
		}
		return convertListMW2ListM(listMW);
	}

	private List<Momento> convertListMW2ListM(ArrayList<MomentoWrapper> listMW) {
		ArrayList<Momento> listM = new ArrayList<>();
		for (MomentoWrapper momentoWrapper : listMW) {
			listM.add(new Momento(momentoWrapper));
		}
		return listM;
	}

	@Override
	public Collection<Momento> listar(Integer qtdPagina, Integer numPagina) throws Exception {
		throw new NotImplementedException("N�o foi implementado listar(Integer qtdPagina, Integer numPagina) => MomentoDaoFirebaseImpl");
	}

	@Override
	public Collection<Momento> listar() throws Exception {
		return listar(new Momento());
	}

	@Override
	public Momento listar(long id) throws Exception {
		Firestore db = FirebaseUtils.getMyFirestoreDataBase();
		CollectionReference collection = db.collection(DB_NAME);
		MomentoWrapper momentoWrapper = collection.document(""+id).get().get().toObject(MomentoWrapper.class);
		return new Momento(momentoWrapper);
	}
	
	
	@Override
	public List listarMusicas(long id) throws Exception {
		Firestore db = FirebaseUtils.getMyFirestoreDataBase();
		List<Long> idMusicas = db.collection(DB_NAME).get().get().toObjects(MomentoWrapper.class).get(0).getMusicas();
		ArrayList<Musica> musicasList = new ArrayList<>();
		for (Long idMusica : idMusicas) {
			db = FirebaseUtils.getMyFirestoreDataBase();
			CollectionReference collection = db.collection(DB_NAME_CHILD);
			MusicaWrapper musicaWrapper = collection.document(""+idMusica).get().get().toObject(MusicaWrapper.class);
			musicasList.add(new Musica(musicaWrapper));
		}
		return musicasList;
	}

	@Override
	public List listarMusicas(Momento m) throws Exception {
		return listarMusicas(m.getId());
	}

	@Override
	public List listarMusicas(Momento m, String filtro) throws Exception {
		return listarMusicas(m.getId());
	}

	@Override
	public List listarMomentosPorFiltroMusica(String filtro) throws Exception {
		Firestore db = FirebaseUtils.getMyFirestoreDataBase();
		CollectionReference collection = db.collection(DB_NAME);

		List<QueryDocumentSnapshot> documents = collection.get().get().getDocuments();
		ArrayList<MomentoWrapper> listMW = new ArrayList<>();
		for (QueryDocumentSnapshot document : documents) {
			listMW.add(document.toObject(MomentoWrapper.class));
		}
		return convertListMW2ListM(listMW);
	}

}

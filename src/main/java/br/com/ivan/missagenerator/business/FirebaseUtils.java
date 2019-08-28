package br.com.ivan.missagenerator.business;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class FirebaseUtils {

	private static Firestore myFirestore;

	public static Firestore getMyFirestoreDataBase() {
		if (myFirestore == null) {
			try {
				// BasicConfigurator.configure();
				FileInputStream serviceAccount = new FileInputStream(
						"db/vemlouvar-30a00-firebase-adminsdk-vii8z-675b00f05d.json");

				FirebaseOptions options = new FirebaseOptions.Builder()
						.setCredentials(GoogleCredentials.fromStream(serviceAccount))
						.setDatabaseUrl("https://vemlouvar-30a00.firebaseio.com").build();

				FirebaseApp.initializeApp(options);
				myFirestore = FirestoreClient.getFirestore();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
		}
		return myFirestore;
	}
	
	public static CollectionReference getCollection(String dbname) {
		return FirebaseUtils.getMyFirestoreDataBase().collection(dbname);
	}

}

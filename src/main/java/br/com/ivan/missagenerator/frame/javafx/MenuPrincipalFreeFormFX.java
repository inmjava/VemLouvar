package br.com.ivan.missagenerator.frame.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuPrincipalFreeFormFX extends Application {

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("freeform.fxml"));

		Scene scene = new Scene(root, 900, 700);

		stage.setTitle("FXML Welcome");
		stage.setScene(scene);
		stage.show();
	}
}
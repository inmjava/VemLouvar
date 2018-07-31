package br.com.ivan.missagenerator.frame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuPrincipalFX extends Application {

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/vemlouvarfreeform.fxml"));

		Scene scene = new Scene(root, 853, 597);

		stage.setTitle("FXML Welcome");
		stage.setScene(scene);
		stage.show();
	}
}
/**
 * Sample Skeleton for 'vemlouvarfreeform.fxml' Controller Class
 */

package br.com.ivan.missagenerator.frame;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import br.com.ivan.missagenerator.business.Processador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MenuPrincipalControllerFXML {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtMissa"
    private TextArea txtMissa; // Value injected by FXMLLoader

    @FXML // fx:id="txtMomento"
    private TextField txtMomento; // Value injected by FXMLLoader

    @FXML // fx:id="txtPesquisa"
    private TextField txtPesquisa; // Value injected by FXMLLoader

    @FXML // fx:id="txtCifra"
    private TextArea txtCifra; // Value injected by FXMLLoader

    @FXML // fx:id="txtNome"
    private TextField txtNome; // Value injected by FXMLLoader

    @FXML // fx:id="txtApresentacao"
    private TextArea txtApresentacao; // Value injected by FXMLLoader

	private AutoCompletionBinding<String> bindAutoCompletion;

	private void gerarAutoComplete(Collection<String> opcoesComplete) {
		if(bindAutoCompletion != null) {
			bindAutoCompletion.dispose();
		}
		bindAutoCompletion = TextFields.bindAutoCompletion(txtPesquisa, opcoesComplete);
		bindAutoCompletion.setMinWidth(500);
    	txtPesquisa.setText("");
    	txtPesquisa.requestFocus();
	}

    @FXML
    void pesquisarCifra(ActionEvent event) throws IOException {
    	gerarAutoComplete(Processador.pesquisarLinks(txtPesquisa.getText()));
    }

    @FXML
    void pesquisarApresentacao(ActionEvent event) throws IOException {
    	gerarAutoComplete(Processador.pesquisarLinksLetras(txtPesquisa.getText()));
    }

    @FXML
    void pesquisarMusica(ActionEvent event) {

    }

    @FXML
    void novaMusica(ActionEvent event) {

    }

    @FXML
    void salvarMusica(ActionEvent event) {

    }

    @FXML
    void refraoMais(ActionEvent event) {

    }

    @FXML
    void refraoMenos(ActionEvent event) {

    }

    @FXML
    void quebrarLinhas(ActionEvent event) {

    }

    @FXML
    void ligarLinhas(ActionEvent event) {

    }

    @FXML
    void autoOrganizar(ActionEvent event) {

    }

    @FXML
    void gerarPpt(ActionEvent event) {

    }

    @FXML
    void gerarDocx(ActionEvent event) {

    }

    @FXML
    void gravarMissa(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
//        assert txtMissa != null : "fx:id=\"txtMissa\" was not injected: check your FXML file 'vemlouvarfreeform.fxml'.";
//        assert txtMomento != null : "fx:id=\"txtMomento\" was not injected: check your FXML file 'vemlouvarfreeform.fxml'.";
//        assert txtPesquisa != null : "fx:id=\"txtPesquisa\" was not injected: check your FXML file 'vemlouvarfreeform.fxml'.";
//        assert txtCifra != null : "fx:id=\"txtCifra\" was not injected: check your FXML file 'vemlouvarfreeform.fxml'.";
//        assert txtNome != null : "fx:id=\"txtNome\" was not injected: check your FXML file 'vemlouvarfreeform.fxml'.";
//        assert txtApresentacao != null : "fx:id=\"txtApresentacao\" was not injected: check your FXML file 'vemlouvarfreeform.fxml'.";

    }
}

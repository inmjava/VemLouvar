/**
 * Sample Skeleton for 'vemlouvarfreeform.fxml' Controller Class
 */

package br.com.ivan.missagenerator.frame.javafx;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.fife.ui.autocomplete.ShorthandCompletion;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import model.Momento;
import model.Musica;
import model.dao.MomentoDao;
import model.dao.factory.MomentoDaoFactory;

public class FreeFormControllerFXML {

    @FXML
    private AutoCompleteTextArea txtFreeForm;

    @FXML
    private TextArea txtCifra;

    @FXML
    private TextArea txtApresentacao;
	
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
    	List<String> completions = listarETratarErrosCompletions();
		txtFreeForm.setCompletions(completions);
    }

	public static List<String> listarETratarErrosCompletions() {
		List<String> completions = null;
		try {
			completions = gerarCompletionsMusicasMomentos();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return completions;
	}

	private static List<String> gerarCompletionsMusicasMomentos() throws Exception {
		List<String> completions = new ArrayList<>();
		MomentoDao momentoDao = MomentoDaoFactory.createMomentoDao();
		for (Momento m : momentoDao.listar()) {
			List<Musica> musicas = momentoDao.listarMusicas(m);
			for (Musica musica : musicas) {
				String textoMusica = StringUtils.rightPad(m.getId() + ":", 5)
						+ StringUtils.rightPad(musica.getId() + ":", 5) + " "
						+ StringUtils.rightPad(m.getNome() + ":", 20) + " "
						+ StringUtils.rightPad(musica.getNome() + "", 0);
				completions.add(m.getNome() + " - " + musica.getNome() + " - " + musica.getApresentacao());
			}
		}
		return completions;
	}

}
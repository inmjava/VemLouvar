package br.com.ivan.missagenerator.business.autocomplete.provider;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Segment;

import org.apache.commons.lang3.StringUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class ProcessadorAutocomplete {

	public static String obterConteudoLinhaProvider(JTextComponent comp) {
		return getConteudoLinhaSelecionada((RSyntaxTextArea) comp);
	}

	public static String getConteudoLinhaSelecionada(RSyntaxTextArea txtArea) {
		try {
			int posicaoCursor = txtArea.getCaretPosition();
			int numLinha = txtArea.getLineOfOffset(posicaoCursor);
			int cursorInicioLinha = txtArea.getLineStartOffset(numLinha);
			int cursorFimLinha = txtArea.getLineEndOffset(numLinha);

			return txtArea.getText().substring(cursorInicioLinha, cursorFimLinha);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	

	public static int ordinalIndexOf(String str, String substr, int n) {
		int pos = str.indexOf(substr);
		while (--n > 0 && pos != -1)
			pos = str.indexOf(substr, pos + 1);
		return pos;
	}

	public static String madeMusicLine(String idMomento) {
		return StringUtils.rightPad(idMomento + ":", 5);
	}

	public static String madeMusicLine(String idMomento, String idMusica) {
		return madeMusicLine(idMomento) + StringUtils.rightPad(idMusica + ":", 5) + " ";
	}

	public static String madeMusicLine(String idMomento, String idMusica, String nomeMomento) {
		return madeMusicLine(idMomento, idMusica) + StringUtils.rightPad(nomeMomento + ":", 20) + " ";
	}

	public static String madeMusicLine(String idMomento, String idMusica, String nomeMomento, String nomeMusica) {
		return madeMusicLine(idMomento, idMusica, nomeMomento) + StringUtils.rightPad(nomeMusica + ":", 50) + " ";
	}

	public static String madeMusicLine(String idMomento, String idMusica, String nomeMomento, String nomeMusica,
			String linkMusica) {
		return madeMusicLine(idMomento, idMusica, nomeMomento, nomeMusica) + StringUtils.rightPad(linkMusica, 0);
	}

	public static String getLinhaIdMomento(String linha) {
		return linha.split(":")[0];
	}

	public static String getLinhaIdMusica(String linha) {
		return linha.split(":")[1];
	}

	public static String getLinhaNomeMomento(String linha) {
		return linha.split(":")[2];
	}

	public static String getLinhaNomeMusica(String linha) {
		return linha.split(":")[3];
	}

	public static String getLinhaLinkMusica(String linha) {
		return linha.split(":")[4];
	}
}

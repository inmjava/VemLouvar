package br.com.ivan.missagenerator.business.provider;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.ShorthandCompletion;

import antlr.StringUtils;
import br.com.ivan.missagenerator.business.Processador;

public class MomentoContainsProvider extends DefaultCompletionProvider {
	
//	public static void main(String[] args) {
//		System.out.println("       aaaaaa          ".trim() + "12");
//	}
	

	@Override
	protected List<Completion> getCompletionsImpl(JTextComponent comp) {
		List<Completion> retVal = new ArrayList<Completion>();
		String text = getAlreadyEnteredText(comp);

		if (text!=null) {

			int index = 0;
			String idMusica = text.split(":")[1].trim();
			String nomeMomento = text.split(":")[2].trim();

			while (index<completions.size()) {
				Completion c = completions.get(index);
				String momentoOptions = c.getInputText();
				String id = momentoOptions.split(":")[0];
				String nome = momentoOptions.split(":")[1];
				if(!nome.toUpperCase().contains(nomeMomento.toUpperCase())) {
					index++;
					continue;
				}
				
				String newMusicLine = Processador.madeMusicLine(id, idMusica, nome);
				newMusicLine = newMusicLine.substring(0, newMusicLine.lastIndexOf(":"));
				retVal.add(new ShorthandCompletion(c.getProvider(), momentoOptions, newMusicLine));
				index++;
			}

		}
		return retVal;
	}

	@Override
	public String getAlreadyEnteredText(JTextComponent comp) {
		Document doc = comp.getDocument();

		int dot = comp.getCaretPosition();
		Element root = doc.getDefaultRootElement();
		int index = root.getElementIndex(dot);
		Element elem = root.getElement(index);
		int start = elem.getStartOffset();

		int end = elem.getEndOffset()-1;
		if (dot < end) {
			dot = end;
//			comp.setCaretPosition(dot);
		}

		int len = dot - start;

		try {
			doc.getText(start, len, seg);
		} catch (BadLocationException ble) {
			ble.printStackTrace();
			return EMPTY_STRING;
		}
		String texto = seg.toString();
		
		int ordinalIndexOf = Processador.ordinalIndexOf(texto, ":", 3);
		
		dot = start + ordinalIndexOf;
		comp.setCaretPosition(dot);
		len = dot - start;

		try {
			doc.getText(start, len, seg);
		} catch (BadLocationException ble) {
			ble.printStackTrace();
			return EMPTY_STRING;
		}
		
		texto = seg.toString();
		return texto;
	}

}

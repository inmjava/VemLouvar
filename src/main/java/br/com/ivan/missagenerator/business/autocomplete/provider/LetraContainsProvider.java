package br.com.ivan.missagenerator.business.autocomplete.provider;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.StringUtils;
import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.ShorthandCompletion;

import br.com.ivan.missagenerator.business.Processador;

public class LetraContainsProvider extends DefaultCompletionProvider {
	
	int count = -1;

	@Override
	protected List<Completion> getCompletionsImpl(JTextComponent comp) {
		String linha = getAlreadyEnteredText(comp);
		String searchMusic = ProcessadorAutocomplete.getLinhaNomeMusica(linha);
		int contarEspacos = StringUtils.countMatches(searchMusic, " ");
		if(count != contarEspacos) {
			count = contarEspacos;
			this.completions.clear();
			ArrayList<String> linksCifras = Processador.pesquisarLinksLetras(searchMusic);
			for (String link : linksCifras) {
				completions.add(new ShorthandCompletion(this, link, link));	
			}
		}
		return this.completions;
	}
	
//	private void acertarCaret(JTextComponent comp, String linha) {
//		Document doc = comp.getDocument();
//
//		int dot = comp.getCaretPosition();
//		Element root = doc.getDefaultRootElement();
//		int index = root.getElementIndex(dot);
//		Element elem = root.getElement(index);
//		int start = elem.getStartOffset();
//		
//		
//		int inicio = ProcessadorAutocomplete.ordinalIndexOf(linha, ":", 3) + start;
//		int fim = ProcessadorAutocomplete.ordinalIndexOf(linha, ":", 4) + start;
//		
//		if(dot != )
//		
//	}

	@Override
	public String getAlreadyEnteredText(JTextComponent comp) {
		return ProcessadorAutocomplete.obterConteudoLinhaProvider(comp);
	}
}

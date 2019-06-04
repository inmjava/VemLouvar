package br.com.ivan.missagenerator.business.autocomplete.provider;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.StringUtils;
import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.ShorthandCompletion;

import br.com.ivan.missagenerator.business.Processador;

public class CifraContainsProvider extends DefaultCompletionProvider {
	
	int count = -1;

	@Override
	protected List<Completion> getCompletionsImpl(JTextComponent comp) {
		String searchMusic = getAlreadyEnteredText(comp);
		int contarEspacos = StringUtils.countMatches(searchMusic, " ");
		if(count != contarEspacos) {
			count = contarEspacos;
			this.completions.clear();
			ArrayList<String> linksCifras = Processador.pesquisarLinks(searchMusic);
			for (String link : linksCifras) {
				completions.add(new ShorthandCompletion(this, link, link));	
			}
		}
		System.out.println(this.completions);
		return this.completions;
	}
	
	@Override
	public String getAlreadyEnteredText(JTextComponent comp) {
		return ProcessadorAutocomplete.obterConteudoLinhaProvider(comp);
	}
}

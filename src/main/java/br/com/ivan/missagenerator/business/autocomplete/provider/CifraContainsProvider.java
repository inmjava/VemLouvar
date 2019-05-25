package br.com.ivan.missagenerator.business.autocomplete.provider;

import java.util.List;

import javax.swing.text.JTextComponent;

import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.ShorthandCompletion;

public class CifraContainsProvider extends DefaultCompletionProvider {

	@Override
	protected List<Completion> getCompletionsImpl(JTextComponent comp) {
		if(completions.size() == 0) {
			completions.add(new ShorthandCompletion(this, "Não foram encontrados", ""));
			completions.add(new ShorthandCompletion(this, "Resultados", ""));
		}
		return this.completions;
	}
	
	@Override
	public String getAlreadyEnteredText(JTextComponent comp) {
		return ProcessadorAutocomplete.obterConteudoLinhaProvider(comp);
	}
}

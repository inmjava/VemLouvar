package br.com.ivan.missagenerator.business.provider;

import java.util.List;

import javax.swing.text.JTextComponent;

import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;

import br.com.ivan.missagenerator.business.Processador;

public class SalmoContainsProvider extends DefaultCompletionProvider {

	@Override
	protected List<Completion> getCompletionsImpl(JTextComponent comp) {
		return completions;
	}
	
	@Override
	public String getAlreadyEnteredText(JTextComponent comp) {
		return Processador.obterConteudoLinhaProvider(comp, EMPTY_STRING, seg);
	}
}
